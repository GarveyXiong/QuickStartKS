package com.kozen.quickstartks.pinpad;

import android.content.Context;
import android.util.Log;

import com.kozen.financial.constant.ConstantSecurity;
import com.kozen.financial.engine.FinancialEngine;
import com.kozen.financial.engine.InitListener;
import com.kozen.financial.security.ISecurityManager;

public class HelperForPed {
    private Context context;
    private static HelperForPed pinPadHelp = null;
    private static ISecurityManager secManager = null;
    private static final String TAG = "HelperForPed";

    private HelperForPed(Context context) {
        this.context = context;
    }

    public static synchronized HelperForPed getInstance(Context context) {
        if (pinPadHelp == null) {

            FinancialEngine.INSTANCE.init(context, new InitListener() {
                @Override
                public void onResult(int ret, String msg) {
                    Log.d(TAG, "FinancialEngine.INSTANCE.init.onResult===>>ret:" + ret + " msg:" + msg);
                    if (ret == 0) {
                        pinPadHelp = new HelperForPed(context);
                        secManager = FinancialEngine.INSTANCE.getSecurityManager();
                    }
                }
            });
        }
        return pinPadHelp;
    }

    /**
     * Write keys
     *
     * @param srcKeyType   Source key type, Such as EPedKeyType.TLK, EPedKeyType.TMK
     * @param srcKeyIndex  Source key index,TLK: Only 1 group is supported, and the index range is [1,1];
     *                     TMK: Supports 64 groups, the index range is [1, 64].
     * @param destKeyType  Dest key type, as follows: EPedKeyType TMK,TAK,TDK,TPK,TEK,TTK
     * @param destkeyIndex Dest key index,[1,64] TPK, TAK, TDK, TEK, TTK share the index space. Key indexes can not be duplicated. If
     *                     duplication occurs, the later written key will overwrite the previously injected key.
     * @param destKeyValue Dest key value, supported key lengths: [8, 16, 24, 32]
     * @param kcv          KCV value
     * @return 0: The operation is successfully executed; Others values: The operation fails. Please refer to SecurityError for more information
     */
    int writeKey(EPedKeyType srcKeyType, int srcKeyIndex, EPedKeyType destKeyType, int destkeyIndex, byte[] destKeyValue, byte[] kcv) {
        return secManager.writeKey(srcKeyType.getPedkeyType(), srcKeyIndex, destKeyType.getPedkeyType(), destkeyIndex, destKeyValue, kcv);
    }

    /**
     * @param srcKeyType
     * @param srcKeyIndex
     * @param destKeyType
     * @param destkeyIndex
     * @param destKeyValue
     * @param encAlgType
     * @param kcv
     * @return
     */
    int writeKeyMKSK(EPedKeyType srcKeyType, int srcKeyIndex, EPedKeyType destKeyType, int destkeyIndex, byte[] destKeyValue, EEncAlgType encAlgType, byte[] kcv) {

        return secManager.writeKeyMKSK(srcKeyType.getPedkeyType(), srcKeyIndex, destKeyType.getPedkeyType(), encAlgType.getEncAlgType(), destkeyIndex, destKeyValue, kcv);
    }

    int writeKeyDukpt(int keyIndex, byte[] keyIn, byte[] ksnIn, byte[] kcv) {
        return secManager.writeKeyDukpt(keyIndex, keyIn, ksnIn, kcv);
    }

    int writeKeyDukptDes(int tlkIndex, int tikIndex, byte[] keyIn, byte[] ksnIn, byte[] kcv) {
        return secManager.writeKeyDukptDes(tikIndex, tlkIndex, keyIn, ksnIn, kcv);
    }

    int eraseAllKey() {
        return secManager.eraseAllKey();
    }

    int generateRsaKey(int pubKeyIndex, int priKeyIndex, int size) {
        return secManager.generateRsaKey(pubKeyIndex, priKeyIndex, size);
    }

    int writeRsaKey(int keyIndex, byte[] modulus, byte[] exponent) {
        return secManager.writeRsaKey(keyIndex, modulus, exponent);
    }

    int readRsaKey(int keyIndex, byte[] keyOut, byte[] modulusOut, byte[] exponentOut) {
        return secManager.readRsaKey(keyIndex, keyOut, modulusOut, exponentOut);
    }

    int getRandom(int length, byte[] keyOut) {
        return secManager.getRandom(length, keyOut);
    }

    int getKCV(int keyIndex, EPedKeyType keyType, byte[] kcvOut) {
        return secManager.getKCV(keyIndex, keyType.getPedkeyType(), kcvOut);
    }

    int calcRsa(int keyIndex, ECalcRsaMode mode, byte[] dataIn, byte[] dataOut) {
        return secManager.calcRsa(keyIndex, mode.getCalcRsaMode(), dataIn, dataOut);
    }


    /**
     * @param keyIndex
     * @param cryptOperate
     * @param dataIn
     * @param dataOut
     * @return
     */
    int calcDes(int keyIndex, ECryptOperate cryptOperate, byte[] dataIn, byte[] dataOut) {
        int mode = ConstantSecurity.PED_CALC_DES_MODE_CBC_ENC;
        switch (cryptOperate) {
            case ECB_DEC:
                mode = ConstantSecurity.PED_CALC_DES_MODE_ECB_DEC;
                break;
            case ECB_ENC:
                mode = ConstantSecurity.PED_CALC_DES_MODE_ECB_ENC;
                break;
            case CBC_DEC:
                mode = ConstantSecurity.PED_CALC_DES_MODE_CBC_DEC;
                break;
        }
        return secManager.calcDes(keyIndex, mode, dataIn, dataOut);
    }

    int calcDukpt(int keyIndex, ECryptOperate cryptOperate, EAlgType algType, byte[] dataIn, byte[] aesIv, byte[] dataOut) {
        int calcMode = ConstantSecurity.PED_CALC_DUKPT_MODE_ENC, macMode = ConstantSecurity.DUKPT_MAC_MODE_CBC;
        switch (cryptOperate) {
            case ECB_DEC:
                calcMode = ConstantSecurity.PED_CALC_DUKPT_MODE_DEC;
                macMode = ConstantSecurity.DUKPT_MAC_MODE_ECB;
                break;
            case ECB_ENC:
                calcMode = ConstantSecurity.PED_CALC_DUKPT_MODE_ENC;
                macMode = ConstantSecurity.DUKPT_MAC_MODE_ECB;
                break;
            case CBC_DEC:
                calcMode = ConstantSecurity.PED_CALC_DUKPT_MODE_DEC;
                macMode = ConstantSecurity.DUKPT_MAC_MODE_CBC;
                break;
        }
        return secManager.calcDukpt(keyIndex, calcMode, algType.getAlgType(), macMode, dataIn, aesIv, dataOut);
    }

    /**
     * @param keyIndex
     * @param macMode
     * @param iv
     * @param dataIn
     * @param dataOut
     * @return
     */
    int calcMac(int keyIndex, EMacMode macMode, byte[] iv, byte[] dataIn, byte[] dataOut) {
        return secManager.calcMac(keyIndex, macMode.getMacMode(), iv, dataIn, dataOut);
    }

    int calcMacDukpt(int keyIndex,EAuthentication auth,EAlgType algType,EMacMode macMode, boolean selfIncreasing,byte[] dataIn, byte[] dataOut){


        return 0;//secManager.calcMacDukpt(keyIndex,);
    }
}
