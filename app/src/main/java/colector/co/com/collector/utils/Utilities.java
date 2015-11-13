package colector.co.com.collector.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

import java.io.IOException;

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

            if(isInternetAvailable()) {

                return true;
            }
        }

        return false;
    }

    /**
     * Check if device has access to internet
     * @return
     */
    public static boolean isInternetAvailable() {
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


    /**
     * Get UUID from system
     * @return
     */
    public static String getUUID(Context context){
        TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getDeviceId();
    }

    /**
     * Add new element to array
     * @param array
     * @param push
     * @return
     */
    public static int[] push(int[] array, int push) {
        int[] longer = new int[array.length + 1];
        for (int i = 0; i < array.length; i++)
            longer[i] = array[i];
        longer[array.length] = push;
        return longer;
    }
}
