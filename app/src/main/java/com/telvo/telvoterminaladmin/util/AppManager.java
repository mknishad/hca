package com.telvo.telvoterminaladmin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Invariant on 9/17/17.
 */

public class AppManager {

    public static boolean hasInternetConnection(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo().isConnectedOrConnecting();

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
            return false;
        }
    }

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    /*public static DTOBase getClassObject(String response, DTOBase dtoBase){
        Gson gson = new Gson();
        DTOBase result= gson.fromJson(response,dtoBase.getClass());
        return result;
    }

    public static String getClassString(DTOBase dtoBase){
        Gson gson = new Gson();
        return  gson.toJson(dtoBase);
    }*/

}
