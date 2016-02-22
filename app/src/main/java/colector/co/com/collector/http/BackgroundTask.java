package colector.co.com.collector.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import colector.co.com.collector.R;
import colector.co.com.collector.model.response.ErrorResponse;
import colector.co.com.collector.session.AppSession;

/**
 * Clase generica para el consumo de servicios del API Rest </br>
 * 
 * @author dherrera
 * 
 */
public class BackgroundTask extends AsyncTask<String, Integer, Object> {

	private ProgressDialog pDialog;
	private Object param;
	private Object responseClass;
	private AsyncResponse response;
	private HttpRequest request;
	private final Gson gson = new Gson();

	public BackgroundTask(Context context, Object param, Object responseClass, AsyncResponse response) {

		pDialog = new ProgressDialog(context);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage(context.getString(R.string.generalProgressDialogMessage));
		pDialog.setCancelable(false);

		this.param = param;
		this.responseClass = responseClass;
		this.response = response;

	}

	@Override
	protected Object doInBackground(String... params) {
		Object toReturn = new Object();
		
		try {
			request = HttpRequest.post(params[0]);
			// Headers
			request.contentType("application/json");
			request.acceptCharset("application/json");
			if(AppSession.getInstance().getUser() != null) {
				Log.i("COLLECTOR",">>>>>>>TOKEN: "+AppSession.getInstance().getUser().getToken());
				request.header("token",AppSession.getInstance().getUser().getToken());
			}

			// SSL Configuration
			request.trustAllCerts();
			request.trustAllHosts();
			// Body
			if (param != null) {
				Log.i("COLLECTOR",">>>>>>>REQUEST: "+gson.toJson(param).toString());
				request.send(gson.toJson(param));
			}

			String response = request.body();

			Log.i("COLLECTOR",">>>>>>>RESPONSE: "+response);

			if (request.ok() || request.created()) {
				toReturn = gson.fromJson(response, responseClass.getClass());
			} else if (request.serverError()) {
				toReturn = gson.fromJson(response, ErrorResponse.class);
			} else if (request.unathorizedRequest()) {
				toReturn = new ErrorResponse(401, "no autorizado");
			}

		} catch (HttpRequest.HttpRequestException e) {
			toReturn = new ErrorResponse(404, e.getMessage());
		}
		return toReturn;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		int progreso = values[0].intValue();

		pDialog.setProgress(progreso);
	}

	@Override
	protected void onPreExecute() {
		pDialog.show();
	}

	@Override
	protected void onPostExecute(Object result) {
		pDialog.dismiss();
		if (response != null) {
			response.callback(result);
		}
	}

	@Override
	protected void onCancelled() {
		pDialog.dismiss();
	}

}
