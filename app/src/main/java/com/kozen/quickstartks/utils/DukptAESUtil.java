package com.kozen.quickstartks.utils;


import android.util.Log;

import com.kozen.financial.engine.FinancialEngine;
import com.kozen.financial.security.ISecurityManager;
import com.kozen.quickstartks.TransInitActivity;


public class DukptAESUtil {
    private static final String TAG = DukptAESUtil.class.getName();
    private static ISecurityManager securityManager = TransInitActivity.isFinancialInit ? FinancialEngine.INSTANCE.getSecurityManager() : null;

    public class KeyIndexConstants {
        public static final int DUKPT_AES_PIN_KEY_128_INDEX = 2;
        public static final int DUKPT_AES_PIN_KEY_192_INDEX = 3;
        public static final int DUKPT_AES_PIN_KEY_256_INDEX = 4;
    }

    public class KeyConstants {
        public static final String DUKPT_AES_PIN_KEY_128 = "1273671EA26AC29AFA4D1084127652A1"; //
        public static final String DUKPT_AES_PIN_KEY_128_KSN = "123456789012345600000001";

        // 5B6DEE2B5B7FABFFA32591F35BF8F23DD9329AE85131E584
        public static final String DUKPT_AES_PIN_KEY_192 = "5B6DEE2B5B7FABFFA32591F35BF8F23DD9329AE85131E584"; //
        public static final String DUKPT_AES_PIN_KEY_192_KSN = "123456789012345600000001";

        public static final String DUKPT_AES_PIN_KEY_256 = "CE9CE0C101D1138F97FB6CAD4DF045A7083D4EAE2D35A31789D01CCF0949550F"; //
        public static final String DUKPT_AES_PIN_KEY_256_KSN = "123456789012345600000001";
    }


    public static byte[] getCheckValue(byte[] checkValue) {
        if (checkValue == null) {
            return new byte[5];
        }
        byte[] value = new byte[checkValue.length + 1];
        value[0] = (byte) checkValue.length;
        System.arraycopy(checkValue, 0, value, 1, checkValue.length);
        return value;
    }


    public static int writeKey(int srcKeyIndex,
                               int destKeyIndex,
                               String destKeyStr,
                               String destKsnStr,
                               byte[] destCheckValue) {

        byte[] destKeyValue = HexUtil.parseHex(destKeyStr);
        byte[] destKsnValue = HexUtil.parseHex(destKsnStr);
        if (securityManager != null) {
            return securityManager.writeKeyDukpt(destKeyIndex, destKeyValue, destKsnValue, destCheckValue);
        }
        return -1;
    }

    public static int writeKey(int srcKeyIndex,
                               int destKeyIndex,
                               byte[] destKeyValue,
                               byte[] destKsnValue,
                               byte[] destCheckValue) {

        if (securityManager != null) {
            return securityManager.writeKeyDukpt(destKeyIndex, destKeyValue, destKsnValue, destCheckValue);
        }
        return -1;
    }


    public static int dataEncDec(int keyIndex,
                                 int keyMode,
                                 int algType,
                                 int mode,
                                 byte[] dataIn,
                                 byte[] aesIv,
                                 byte[] dataOut) {

        int result = -1;
        if (securityManager == null)
            return result;

        result = securityManager.calcDukpt(keyIndex, keyMode,algType,mode,dataIn,aesIv,dataOut);

        return result;
    }


    public static int calcMac(int keyIndex,
                              int mode,
                              int algType,
                              byte[] dataIn,
                              byte[] ksnOut,
                              byte[] dataOut) {
        int result = -1;
        if (securityManager != null) {
            result = securityManager.calcMacDukpt(keyIndex, algType, mode, dataIn, dataOut);
        }
        return result;
    }

    private static final int PED_NO_PROTECT_KEY = 0;

    public static int injectDUKPTAES256() {
        int result = -1;

        result = writeKey(PED_NO_PROTECT_KEY,
                KeyIndexConstants.DUKPT_AES_PIN_KEY_256_INDEX,
                KeyConstants.DUKPT_AES_PIN_KEY_256,
                KeyConstants.DUKPT_AES_PIN_KEY_256_KSN,
                null
        );
        Log.d(TAG, "writeKey: " + result);

        if (result == 0) {
//        01H - TLK
//        02H - TMK
//        03H - TPK
//        04H - TAK
//        05H - TDK
//        06H - TEK
//        07H - TIK
            int keyType = 0x07;
            int keyIdx = KeyIndexConstants.DUKPT_AES_PIN_KEY_256_INDEX;
            byte[] kcvInfo = new byte[4];
            int rst = -1;

            if (securityManager == null)
                return rst;
            securityManager.getKCV(keyIdx,keyType,kcvInfo);


            Log.d(TAG, "writeKey rst: " + rst);
            Log.d(TAG, "writeKey keyType: " + keyType);
            Log.d(TAG, "writeKey keyIdx: " + keyIdx);
            if ((kcvInfo != null) && (kcvInfo.length != 0)) {
                Log.d(TAG, "writeKey kcvInfo kcv value: " + HexUtil.toHexString(kcvInfo));
            }
        }

        return 0;
    }
}
