package colector.co.com.collector.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.IOException;

import colector.co.com.collector.settings.AppSettings;

/**
 * Created by dherrera on 17/10/15.
 */
public class Utilities {


    /**
     * Check if device has access to internet and conectivity service is available
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null){

            Log.w(AppSettings.TAG,"primera validación");

            if(isInternetAvailable()) {

                Log.w(AppSettings.TAG,"segunda validación");
                return true;
            }
        }

        return false;
    }

    /**
     * Check if device has access to internet
     * @return
     */
    private static boolean isInternetAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
