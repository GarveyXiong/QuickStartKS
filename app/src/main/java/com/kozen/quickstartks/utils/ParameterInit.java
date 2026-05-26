package com.kozen.quickstartks.utils;

import android.util.Log;

import com.kozen.financial.aidl.emv.EmvAid;
import com.kozen.financial.aidl.emv.EmvCapk;
import com.kozen.financial.aidl.emv.EmvExceptionFile;
import com.kozen.financial.aidl.emv.EmvRevocationIPK;
import com.kozen.financial.constant.ConstantSecurity;
import com.kozen.financial.emv.IEmvManager;
import com.kozen.financial.engine.FinancialEngine;
import com.kozen.financial.pinpad.IPinpadManager;
import com.kozen.financial.security.ISecurityManager;
import com.kozen.quickstartks.TransInitActivity;
import com.kozen.quickstartks.emv.AIDUtils;
import com.kozen.quickstartks.emv.PaywaveKernel;
import com.kozen.quickstartks.emvconfig.CAPKUtils;
import com.kozen.quickstartks.emvconfig.CLSettingUtils;
import com.kozen.quickstartks.emvconfig.EMVConfig;

import java.util.ArrayList;
import java.util.List;

public class ParameterInit {
    private static final String TAG = "paraInit";
    private static IEmvManager emvManager = TransInitActivity.isFinancialInit ? FinancialEngine.INSTANCE.getEmvManager() : null;
    private static IPinpadManager pinpadManager = TransInitActivity.isFinancialInit ? FinancialEngine.INSTANCE.getPinpadManager() : null;
    private static ISecurityManager securityManager = TransInitActivity.isFinancialInit ? FinancialEngine.INSTANCE.getSecurityManager() : null;

    public class KeyIndexConstants {
        public static final int MASTER_KEY_INDEX = 2;
        public static final int DUKPT_PIN_KEY_INDEX = 2;
        public static final int SESSION_DATA_KEY_INDEX = 10;
        public static final int SESSION_PIN_KEY_INDEX = 11;
        public static final int SESSION_MAC_KEY_INDEX = 12;

        public static final int MASTER_KEY_AES_INDEX = 5;
        public static final int SESSION_DATA_KEY_AES_INDEX = 6;
        public static final int SESSION_PIN_KEY_AES_INDEX = 7;
        public static final int SESSION_MAC_KEY_AES_INDEX = 8;

    }

    public class KeyConstants {
        public static final String TMK_3DES_DATA = "C1D0F8FB4958670DBA40AB1F3752EF0D";

        public static final String TDK_3DES_DATA = "952FB7A4E173E9735E899C60029B181C";  // plainText: C1D0F8FB4958670DBA40AB1F3752EF0D

        public static final String TPK_3DES_DATA = "952FB7A4E173E9735E899C60029B181C";  // plainText: C1D0F8FB4958670DBA40AB1F3752EF0D

        public static final String TAK_3DES_DATA = "952FB7A4E173E9735E899C60029B181C";  // plainText: C1D0F8FB4958670DBA40AB1F3752EF0D
        public static final String DUKPT_3DES_IPEK = "EC77946D7BEFAD60DFDC6D5028AF1BEF"; //
        //1FF229BF144379CA47C2EC11216725EF
        //    public static String DUKPT_KSN   = "62994901190000000001";
        public static final String DUKPT_3DES_KSN = "FFFFFF00000000000001";


        public static final String TMK_AES_DATA = "41473E32EA160D224F8198CEBCC0855A";


        // TDK need to encrypted by TMK
        // plainText: C1D0F8FB4958670DBA40AB1F3752EF0D
        public static final String TDK_AES_DATA = "D7FED63BAF7686D9E725CA5CA2F9A1EE";  // plainText: 24F131957314EF7F2CD8604019CE39A8

        // TPK need to encrypted by TMK
        // plainText: C1D0F8FB4958670DBA40AB1F3752EF0D
        public static final String TPK_AES_DATA = "9863DB85584C6520301E0705D3EF5C6A";

        // TAK need to encrypted by TMK
        // plainText: C1D0F8FB4958670DBA40AB1F3752EF0D
        public static final String TAK_AES_DATA = "9863DB85584C6520301E0705D3EF5C6A";

    }

    enum symAlgorithm {
        KEY_ALG_3DES(ConstantSecurity.ENCRYPTION_ALGORITHM_TDES),
        KEY_ALG_AES(ConstantSecurity.ENCRYPTION_ALGORITHM_AES);
        private int value;

        symAlgorithm(int i) {
            value = i;
        }
    }

    enum DUKPT_PEK_USAGE {
        //        0x00 - Use Request or both ways MAC key
//        0x01 - Use Request or both ways Data key
//        0x02 - Use DATA Response key
//        0x03 - Use PIN Encryption key
        REQUEST_MAC(0x00),
        REQUEST_DATA(0x01),
        RESPONSE_MAC(0x02),
        PIN_ENCYPTION(0x03);

        private int value;

        DUKPT_PEK_USAGE(int value) {
            this.value = value;
        }
    }

    ;


    private static final int PED_NO_PROTECT_KEY = 0;
    private static final int PED_NO_PROTECT_KEY_INDEX = 0;

    private static int updateKeyMKSK(int protectKeyType,
                                     int protectKeyIndex,
                                     String Keydata,
                                     int keyType,
                                     int keyIndex) {
        byte[] keyData = PosUtils.hexStringToBytes(Keydata);
        int rt = -1;
        if (securityManager != null) {
            rt = securityManager.writeKeyMKSK(protectKeyType, protectKeyIndex, keyType, symAlgorithm.KEY_ALG_3DES.value, keyIndex, keyData, null);
        }

        return rt;
    }

    private static int updateKeyMKSKAES(int protectKeyType,
                                        int protectKeyIndex,
                                        String Keydata,
                                        int keyType,
                                        int keyIndex) {
        byte[] keyData = PosUtils.hexStringToBytes(Keydata);
        int rt = -1;
        if (securityManager != null) {
            rt = securityManager.writeKeyMKSK(protectKeyType, protectKeyIndex, keyType, symAlgorithm.KEY_ALG_AES.value, keyIndex, keyData, null);
        }
        return rt;
    }

    private static int injectMKSK() {
        int result = -1;

        if (securityManager == null) {
            Log.d(TAG, "injectMKSK: =============-1,securityManager is null!");
            return result;
        }

        Log.d(TAG, "injectMKSK: =============0");
        // TODO: 2023/10/9 1.inject plainText TMK
        result = updateKeyMKSK(
                PED_NO_PROTECT_KEY,
                PED_NO_PROTECT_KEY_INDEX,
                KeyConstants.TMK_3DES_DATA,
                ConstantSecurity.PED_TMK,
                KeyIndexConstants.MASTER_KEY_INDEX
        );
        Log.d(TAG, "injectMKSK: =============1");
        if (result != 0) {
            Log.d(TAG, "injectMKSK: updateKeyMKSK PED_TMK failed");
            return result;
        }
        // TODO: 2023/10/9 2.inject TDK
        result = updateKeyMKSK(
                ConstantSecurity.PED_TMK,
                KeyIndexConstants.MASTER_KEY_INDEX,
                KeyConstants.TDK_3DES_DATA,
                ConstantSecurity.PED_TDK,
                KeyIndexConstants.SESSION_DATA_KEY_INDEX
        );
        Log.d(TAG, "injectMKSK: =============2");
        if (result != 0) {
            Log.d(TAG, "injectMKSK: updateKeyMKSK PED_TDK failed");
            return result;
        }

        // TODO: 2023/10/9 3.inject TPK
        result = updateKeyMKSK(
                ConstantSecurity.PED_TMK,
                KeyIndexConstants.MASTER_KEY_INDEX,
                KeyConstants.TPK_3DES_DATA,
                ConstantSecurity.PED_TPK,
                KeyIndexConstants.SESSION_PIN_KEY_INDEX
        );
        Log.d(TAG, "injectMKSK: =============3");
        if (result != 0) {
            Log.d(TAG, "injectMKSK: updateKeyMKSK PED_TPK failed");
            return result;
        }


        // TODO: 2023/10/9 4.inject TAK
        result = updateKeyMKSK(
                ConstantSecurity.PED_TMK,
                KeyIndexConstants.MASTER_KEY_INDEX,
                KeyConstants.TAK_3DES_DATA,
                ConstantSecurity.PED_TAK,
                KeyIndexConstants.SESSION_MAC_KEY_INDEX
        );
        Log.d(TAG, "injectMKSK: =============4");
        if (result != 0) {
            Log.d(TAG, "injectMKSK: updateKeyMKSK PED_TAK failed");
            return result;
        }

        boolean testMKSK = false;
        if (testMKSK) {

            //key is:C1D0F8FB4958670DBA40AB1F3752EF0D
            String crytoStr = "B02310D37A8A9D7952C1C1D5F8F73D61";
            byte[] cryptoSrcData = PosUtils.hexStringToBytes(crytoStr);
            byte[] rspBuf = new byte[cryptoSrcData.length];
            byte[] macResult = new byte[8];
            byte[] iv = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            result = securityManager.calcDes(KeyIndexConstants.SESSION_DATA_KEY_INDEX,
                    ConstantSecurity.PED_CALC_DES_MODE_ECB_ENC, cryptoSrcData, rspBuf);
            Log.d(TAG, "PedCalDes PED_CALC_DES_MODE_ECB_DEC: " + result);
            Log.d(TAG, "PedCalDes: " + PosUtils.bytesToHexString(rspBuf));
            //expected:51c24e4553c50666dd7793e362a55a74

//                        0x02: ANSI-X9.19 MAC
//                        0x03: ANSI-X9.9 MAC
            result = securityManager.calcMac(KeyIndexConstants.SESSION_MAC_KEY_INDEX,
                    0x02, iv, cryptoSrcData, macResult);
            Log.d(TAG, "PedGetMac ANSI-X9.19 MAC: " + result);
            Log.d(TAG, "PedGetMac: " + PosUtils.bytesToHexString(macResult));
            //expected:B39294C40541D8D1

            result = securityManager.calcMac(KeyIndexConstants.SESSION_MAC_KEY_INDEX,
                    0x03, iv, cryptoSrcData, macResult);
            Log.d(TAG, "PedGetMac ANSI-X9.9 MAC: " + result);
            Log.d(TAG, "PedGetMac: " + PosUtils.bytesToHexString(macResult));
            //expected:5A74234CCCD676F1
        }

        boolean useAESMKSK = false;
        if (useAESMKSK) {
            result = updateKeyMKSKAES(
                    PED_NO_PROTECT_KEY,
                    PED_NO_PROTECT_KEY_INDEX,
                    KeyConstants.TMK_AES_DATA,
                    ConstantSecurity.PED_TMK,
                    KeyIndexConstants.MASTER_KEY_AES_INDEX
            );
            if (result != 0) {
                Log.d(TAG, "injectMKSKAES: updateKeyMKSK PED_TMK failed");
                return result;
            }
            Log.d(TAG, "injectMKSKAES: success");
            //        keyType
//        01H - TLK
//        02H - TMK
//        03H - TPK
//        04H - TAK
//        05H - TDK
//        06H - TEK
//        07H - TIK
            byte[] kcvBuf = new byte[8];
            byte[] aucCheckBufIn = new byte[5];
            boolean isDukpt = true;

            result = -1;


            if (securityManager.getKCV(KeyIndexConstants.MASTER_KEY_AES_INDEX, 0x02, kcvBuf) != 0) {
                Log.d(TAG, "MK-AES Index: " + KeyIndexConstants.MASTER_KEY_AES_INDEX + " No Key");
            } else {
                Log.d(TAG, "MK-AES Index:" + KeyIndexConstants.MASTER_KEY_AES_INDEX + " have Key" +
                        " KCV =" + kcvBuf.toString());
            }


            // TODO: 2023/10/9 2.inject TDK
            result = updateKeyMKSKAES(
                    ConstantSecurity.PED_TMK,
                    KeyIndexConstants.MASTER_KEY_AES_INDEX,
                    KeyConstants.TDK_AES_DATA,
                    ConstantSecurity.PED_TDK,
                    KeyIndexConstants.SESSION_DATA_KEY_AES_INDEX
            );
            if (result != 0) {
                Log.d(TAG, "injectMKSKAES: updateKeyMKSK PED_TDK failed");
                return result;
            }

            // TODO: 2023/10/9 3.inject TPK
            result = updateKeyMKSKAES(
                    ConstantSecurity.PED_TMK,
                    KeyIndexConstants.MASTER_KEY_AES_INDEX,
                    KeyConstants.TPK_AES_DATA,
                    ConstantSecurity.PED_TPK,
                    KeyIndexConstants.SESSION_PIN_KEY_AES_INDEX
            );
            if (result != 0) {
                Log.d(TAG, "injectMKSKAES: updateKeyMKSK PED_TPK failed");
                return result;
            }


            // TODO: 2023/10/9 4.inject TAK
            result = updateKeyMKSKAES(
                    ConstantSecurity.PED_TMK,
                    KeyIndexConstants.MASTER_KEY_AES_INDEX,
                    KeyConstants.TAK_AES_DATA,
                    ConstantSecurity.PED_TAK,
                    KeyIndexConstants.SESSION_MAC_KEY_AES_INDEX
            );
            if (result != 0) {
                Log.d(TAG, "injectMKSKAES: updateKeyMKSK PED_TAK failed");
                return result;
            }

            boolean testAESMKSK = true;
            if (testAESMKSK) {
                String crytoStr = "B02310D37A8A9D7952C1C1D5F8F73D61C1D0F8FB4958670DBA40AB1F3752EF0D";
                byte[] cryptoSrcData = PosUtils.hexStringToBytes(crytoStr);
                byte[] rspBuf = new byte[cryptoSrcData.length];
                byte[] macResult = new byte[8];
                byte[] iv = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

                result = securityManager.calcDes(KeyIndexConstants.SESSION_DATA_KEY_AES_INDEX,
                        ConstantSecurity.PED_CALC_DES_MODE_ECB_ENC, cryptoSrcData, rspBuf);

                Log.d(TAG, "PedCalDes PED_CALC_DES_MODE_AES_ECB_ENC: " + result);
                Log.d(TAG, "PedCalDes: " + PosUtils.bytesToHexString(rspBuf));


//                result = POIHsmManage.getDefault().PedCalDes(
//                        KeyIndexConstants.SESSION_DATA_KEY_AES_INDEX,
//                        POIHsmManage.PED_CALC_DES_MODE_AES_CBC_ENC,
//                        cryptoSrcData,
//                        rspBuf);
//                Log.d(TAG, "PedCalDes PED_CALC_DES_MODE_AES_CBC_ENC: " + result);
//                Log.d(TAG, "PedCalDes: " + PosUtils.bytesToHexString(rspBuf.buffer));

                result = securityManager.calcMac(KeyIndexConstants.SESSION_MAC_KEY_AES_INDEX,
                        0x00, iv, cryptoSrcData, macResult);

                Log.d(TAG, "PedGetMac AES CMAC: " + result);
                Log.d(TAG, "PedGetMac: " + PosUtils.bytesToHexString(macResult));

                result = securityManager.calcMac(KeyIndexConstants.SESSION_MAC_KEY_AES_INDEX,
                        0x01, iv, cryptoSrcData, macResult);

                Log.d(TAG, "PedGetMac AES XOR-ECB-MAC: " + result);
                Log.d(TAG, "PedGetMac: " + PosUtils.bytesToHexString(macResult));

            }
        }
        return 0;
    }

    public static int updateDukptKey(int idx, String dukptKey, String ksn) {
        byte[] keyData = PosUtils.hexStringToBytes(dukptKey);
        byte[] ksnData = PosUtils.hexStringToBytes(ksn);
        int rt = -1;

//        PED_NO_PROTECT_KEY,
//                PED_NO_PROTECT_KEY_INDEX,
        if (securityManager != null) {
            rt = securityManager.writeKeyDukptDes(idx,
                    PED_NO_PROTECT_KEY_INDEX, keyData,
                    ksnData, null);
        }

        return rt;
    }


    private static int injectDUKPT() {
        int result = -1;

        result = updateDukptKey(KeyIndexConstants.DUKPT_PIN_KEY_INDEX,
                KeyConstants.DUKPT_3DES_IPEK,
                KeyConstants.DUKPT_3DES_KSN
        );
        Log.e(TAG, "injectDUKPT==========>>>>result:" + result);
        boolean testDUKPT = true;
        if (testDUKPT) {

            //IPEK: EC77946D7BEFAD60DFDC6D5028AF1BEF KSN:FFFFFF00000000000001
            String crytoStr = "B02310D37A8A9D7952C1C1D5F8F73D61";
            byte[] cryptoSrcData = PosUtils.hexStringToBytes(crytoStr);
            byte[] rspBuf = new byte[cryptoSrcData.length];
            byte[] macResult = new byte[8];
            byte[] initVector = new byte[8];
            byte[] rspKsn = new byte[10];

            //这里没有文档，反编译代码看的，后面需要验证
            result = securityManager.calcDukptDes(KeyIndexConstants.DUKPT_PIN_KEY_INDEX,
                    ConstantSecurity.DUKPT_KEY_SELECT_DATA_REQUEST,
                    ConstantSecurity.OPERATION_DIRECTION_ENCRYPT,
                    ConstantSecurity.OPERATION_MODE_ECB,
                    ConstantSecurity.NOT_SELF_INCREASING,
                    cryptoSrcData,
                    null,
                    rspBuf,
                    rspKsn);

            Log.d(TAG, "PedDukptDes PED_CALC_DES_MODE_CBC_ENC: " + result);
            Log.d(TAG, "PedDukptDes: " + PosUtils.bytesToHexString(rspBuf));
            Log.d(TAG, "PedDukptDes ksn: " + PosUtils.bytesToHexString(rspKsn));
            //expected:D3E9745DD5DE8494570F31DFF54B9DB7

            macResult = new byte[8];
            rspKsn = new byte[10];
            result = securityManager.calcMacDukptDes(KeyIndexConstants.DUKPT_PIN_KEY_INDEX,
                    ConstantSecurity.OPERATION_MODE_ECB,
                    ConstantSecurity.KSN_NOT_AUTO_INCREASING_BY_DUKPT_TDES_MAC_BOTH_KEY,
                    ConstantSecurity.MAC_ALGORITHM_ANSI_X9_19,
                    cryptoSrcData, null, macResult, rspKsn);

            Log.d(TAG, "PedGetMacDukpt ANSI-X9.19 MAC: " + result);
            Log.d(TAG, "PedGetMacDukpt: " + PosUtils.bytesToHexString(macResult));
            Log.d(TAG, "PedGetMacDukpt ksn: " + PosUtils.bytesToHexString(rspKsn));
            //expected:7AEA4D2BA639B7B3

            macResult = new byte[8];
            rspKsn = new byte[12];
            result = securityManager.calcMacDukpt(KeyIndexConstants.DUKPT_PIN_KEY_INDEX, 0x12, 0x00, cryptoSrcData, macResult);
            Log.d(TAG, "PedGetMacDukpt ANSI-X9.9 MAC: " + result);
            Log.d(TAG, "PedGetMacDukpt: " + PosUtils.bytesToHexString(macResult));
            Log.d(TAG, "PedGetMacDukpt ksn: " + PosUtils.bytesToHexString(rspKsn));
            //expected:2901D17AA4AA28B2

        }
        return 0;
    }


    static public int initKey(boolean EraseAllKey) {
        int result = -1;
        if (securityManager == null) {
            Log.e(TAG, "initKey: securityManager is null");
            return result;
        }
        if (EraseAllKey) {
            result = securityManager.eraseAllKey();
//            if (result != 0) {
//                Log.d(TAG, "initKey: PedErase failed");
//                return result;
//            }
        }

        result = injectMKSK();
        if (result != 0) {
            Log.d(TAG, "initKey: injectMKSK failed");
            return result;
        }
        Log.d(TAG, "initKey: injectMKSK success");

        result = injectDUKPT();
        if (result != 0) {
            Log.d(TAG, "initKey: injectDUKPT failed");
            return result;
        }
        Log.d(TAG, "initKey: injectDUKPT success");


        boolean DUKPTAES256 = false;
        if (DUKPTAES256) {
            result = DukptAESUtil.injectDUKPTAES256();
            if (result != 0) {
                Log.d(TAG, "initKey: injectDUKPT AES failed");
                return result;
            }
            Log.d(TAG, "initKey: injectDUKPT AES success");
        }

        return 0;
    }


    public static int loadAids(List<EmvAid> aids) {
        int result = -1;
        if (emvManager == null) {
            return -1;
        }
        emvManager.deleteAid();
        ArrayList<EmvAid> finalList = new ArrayList<>(aids);

        // this is only the certification profile
        for (Object aid : finalList) {
            EmvAid kaid = (EmvAid) aid;
            // convert everything to partial match
            kaid.SelectIndicator = true;
            if (AIDUtils.isMastercard(kaid) && kaid.DynamicTransLimit <= 0) {
                kaid.DynamicTransLimit = kaid.ContactlessTransLimit;
            }
        }

        for (Object aid : finalList) {
            EmvAid kaid = (EmvAid) aid;
            Log.d(TAG, "loading aid[" + PosUtils.bytesToHexString(kaid.AID) + "]");
            result = emvManager.setAid(kaid);
            if (result != 0) {
                Log.d(TAG, "loadAids aid[" + PosUtils.bytesToHexString(kaid.AID) + "] failed result:" + result);
                return result;
            }
            Log.d(TAG, "aid load success");
        }
        Log.d(TAG, "loadAids success");
        return 0;
    }

    public static int loadCAPKs(List<EmvCapk> capks) {
        int result = -1;
        if (emvManager == null) {
            return -1;
        }
        emvManager.deleteCapk();
        for (Object capk : capks) {
            result = emvManager.setCapk((EmvCapk) capk);
            Log.d(TAG, "loading capk[" + PosUtils.bytesToHexString(((EmvCapk) capk).RID) + "]");
            if (result != 0) {
                Log.d(TAG, "loadCAPKs capk[" +
                        PosUtils.bytesToHexString(((EmvCapk) capk).RID) + "] failed");
                return result;
            }
            Log.d(TAG, "capk load success");
        }
        return 0;
    }

    public static void loadExceptionFile() {
        if (emvManager == null) {
            return;
        }
        emvManager.deleteExceptionFile();
        EmvExceptionFile exceptionFile = new EmvExceptionFile();
        exceptionFile.PAN = PosUtils.hexStringToBytes("5413339123401596");
        exceptionFile.SerialNo = PosUtils.hexStringToBytes("00");
        emvManager.setExceptionFile(exceptionFile);
        exceptionFile = new EmvExceptionFile();
        exceptionFile.PAN = PosUtils.hexStringToBytes("5413339123401196");
        exceptionFile.SerialNo = PosUtils.hexStringToBytes("01");
        emvManager.setExceptionFile(exceptionFile);
    }

    public static void loadRevocationIPK() {
        if (emvManager == null) {
            return;
        }
        emvManager.deleteRevocationIPK();
        EmvRevocationIPK revocationIPK = new EmvRevocationIPK();
        revocationIPK.RID = PosUtils.hexStringToBytes("A000000124");
        revocationIPK.SerialNo = PosUtils.hexStringToBytes("001000");
        revocationIPK.CapkIndex = (byte) 0xF8;
        emvManager.setRevocationIPK(revocationIPK);
        revocationIPK = new EmvRevocationIPK();
        revocationIPK.RID = PosUtils.hexStringToBytes("A000000224");
        revocationIPK.SerialNo = PosUtils.hexStringToBytes("001000");
        revocationIPK.CapkIndex = (byte) 0xF8;
        emvManager.setRevocationIPK(revocationIPK);
    }

    public static int loadContactlessConfig() {
        int result = -1;

        // paywave(VISA) contactless setting
        result = PaywaveKernel.loadDefault();
        result = CLSettingUtils.loadVisaDRL();
        if (result != 0) {
            Log.d(TAG, "loadContactlessConfig: paywave load failed");
            return result;
        }
        Log.d(TAG, "loadContactlessConfig: paywave load success");

        // Paypass(MasterCard) contactless setting
        result = CLSettingUtils.loadPaypass();
        if (result != 0) {
            Log.d(TAG, "loadContactlessConfig: paypass load failed");
            return result;
        }
        Log.d(TAG, "loadContactlessConfig: paypass load success");

        // Discover contactless setting
        result = CLSettingUtils.loadDiscover();
        if (result != 0) {
            Log.d(TAG, "loadContactlessConfig: discover load failed");
            return result;
        }
        Log.d(TAG, "loadContactlessConfig: discover load success");
        // AMEX contactless setting
        result = CLSettingUtils.loadAmex();
        result = CLSettingUtils.loadAmexDRL();
        if (result != 0) {
            Log.d(TAG, "loadContactlessConfig: AMEX load failed");
            return result;
        }
        Log.d(TAG, "loadContactlessConfig: AMEX load success");
        // UPI contactless setting
        result = CLSettingUtils.loadUnionPay();
        if (result != 0) {
            Log.d(TAG, "loadContactlessConfig: UnionPay load failed");
            return result;
        }

        Log.d(TAG, "loadContactlessConfig: UnionPay load success");
        // AMEX contactless setting
        result = CLSettingUtils.loadMir();
        if (result != 0) {
            Log.d(TAG, "loadContactlessConfig: MIR load failed");
            return result;
        }

        Log.d(TAG, "loadContactlessConfig: MIR load success");
        // JCB contactless setting
        result = CLSettingUtils.loadJCB();
        if (result != 0) {
            Log.d(TAG, "loadContactlessConfig: JCB load failed");
            return result;
        }
        Log.d(TAG, "loadContactlessConfig: JCB load success");
        // Rupay contactless setting
        result = CLSettingUtils.loadRupayService();
        if (result != 0) {
            Log.d(TAG, "loadContactlessConfig: Rupay load failed");
            return result;
        }
        Log.d(TAG, "loadContactlessConfig: Rupay load success");
        return 0;
    }


    static public int initEMVConifg(boolean EraseAllAID) {
        int result = -1;
        if (emvManager == null) {
            return result;
        }

        if (EraseAllAID) {
            result = emvManager.deleteAid();
            if (result != 0) {
                Log.d(TAG, "initEMVConifg: EmvDeleteAid failed");
                return result;
            }
            Log.d(TAG, "initEMVConifg: EmvDeleteAid success");
            result = emvManager.deleteCapk();
            if (result != 0) {
                Log.d(TAG, "initEMVConifg: EmvDeleteCapk failed");
                return result;
            }
            Log.d(TAG, "initEMVConifg: EmvDeleteCapk success");
        }


        EMVConfig emvConfig = EMVConfig.getDefault();
        List<EmvAid> aids = AIDUtils.generateAids(emvConfig);
        result = loadAids(aids);
        if (result != 0) {
            Log.d(TAG, "initEMVConifg: loadAids failed");
            return result;
        }
        Log.d(TAG, "initEMVConifg: loadAids success");

        List<EmvCapk> capks = CAPKUtils.generateCAPKs();
        result = loadCAPKs(capks);
        if (result != 0) {
            Log.d(TAG, "initEMVConifg: loadCAPKs failed");
            return result;
        }
        Log.d(TAG, "initEMVConifg: loadCAPKs success");

        loadExceptionFile();
        loadRevocationIPK();

        result = loadContactlessConfig();
        if (result != 0) {
            Log.d(TAG, "initEMVConifg: loadContactlessConfig failed");
            return result;
        }
        Log.d(TAG, "initEMVConifg: loadContactlessConfig success");

        return 0;
    }


}
