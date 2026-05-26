package com.kozen.quickstartks.utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;



public class LogPrintUtil {
    @SuppressLint("RestrictedApi")
    public static void printBundle(Bundle bundle, String LOG_TAG){
        if(bundle == null){
            return;
        }
        for (String key: bundle.keySet())
        {
            Object value = bundle.get(key);
            if (key.equals("emvData")|| key.equals("encryptData") || key.equals("data")
                    || key.equals("cardTrack1")|| key.equals("cardTrack2")|| key.equals("cardTrack3")
                    || key.equals("cardAttribute")||key.equals("cardSerialNum")){
                byte[] bytes = bundle.getByteArray(key);
                Log.e(LOG_TAG,"Bundle Content=====>>Key=" + key + ", value=" + PosUtils.bytesToHexString(bytes));
            }else {
                Log.e(LOG_TAG,"Bundle Content=====>>Key=" + key + ", value=" +value);
            }

        }
    }
}
