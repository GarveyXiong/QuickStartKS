package com.kozen.quickstartks.pinpad;

import android.app.Activity;
import android.app.Dialog;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kozen.financial.constant.ConstantEmv.POIEmvCoreManager;
import com.kozen.financial.constant.ConstantEmv.POIEmvCoreManager.EmvPinConstraints;
import com.kozen.financial.emv.IEmvManager;
import com.kozen.financial.engine.FinancialEngine;
import com.kozen.financial.pinpad.IPinpadManager;
import com.kozen.financial.pinpad.PinpadInputCallback;
import com.kozen.financial.security.ISecurityManager;
import com.kozen.quickstartks.R;
import com.kozen.quickstartks.TransInitActivity;
import com.kozen.quickstartks.utils.PinpadUtils;
import com.kozen.quickstartks.utils.PosUtils;

public class PinPadPHY {

    public static final int PLAIN_PIN = 1;
    public static final int ONLINE_PIN = 2;
    public static final int ENCIPHER_PIN = 3;
    public boolean isOnlinePin = true;
    public static final int DEFAULT_KEY_NUMS = 12;
    private String DEFAULT_EXP_PIN_LEN_IND = "0,4,5,6,7,8,9,10,11,12";
    private int DEFAULT_TIMEOUT_MS = 30000;

    private int keyIndex;
    private int keyMode = 1;
    private boolean isKeyboardFix = true;
    private boolean isEncrypt;
    private String pinCard;
    private int pinType;
    private boolean pinBypass;
    private int pinCounter;
    private byte[] pinRandom;
    private byte[] pinModule;
    private byte[] pinExponent;

    private String title;
    private String message;

    private IEmvManager emvManager;
    private IPinpadManager pinpadManager;
    private PinEventListener pinEventListener;
    private Dialog dialog;
    private TextView tvMessage;
    private TextView tv_pwd;
    PinInputFinish pinInputFinish;

    private OrientationEventListener mOrientationListener;
    private int mOrientation = 0;
    private boolean isOrientation = false;
    private Bundle pinInfo;

    public interface PinInputFinish {
        void onSuccess(byte[] pinBlock, byte[] pinKsn);

        void onError(int verifyResult, int pinTryCntOut);
    }

    public PinPadPHY(Activity context, Bundle bundle, int keyMode, int keyIndex, PinInputFinish pinInputFinish) {
        this.emvManager = TransInitActivity.isFinancialInit ? FinancialEngine.INSTANCE.getEmvManager() : null;
        this.pinpadManager = TransInitActivity.isFinancialInit ? FinancialEngine.INSTANCE.getPinpadManager() : null;
        this.pinEventListener = new PinEventListener();
        this.keyMode = keyMode;
        this.keyIndex = keyIndex;
        this.pinInputFinish = pinInputFinish;
        pinInfo = bundle;
        switch (bundle.getInt(EmvPinConstraints.PIN_TYPE, -1)) {
            case POIEmvCoreManager.PIN_PLAIN_PIN:
                pinType = PLAIN_PIN;
                break;
            case POIEmvCoreManager.PIN_ONLINE_PIN:
                pinType = ONLINE_PIN;
                break;
            case POIEmvCoreManager.PIN_ENCIPHER_PIN:
                pinType = ENCIPHER_PIN;
                break;
            default:
                break;
        }

        if (bundle.containsKey(EmvPinConstraints.PIN_ENCRYPT)) {
            isEncrypt = bundle.getBoolean(EmvPinConstraints.PIN_ENCRYPT);
        }
        if (bundle.containsKey(EmvPinConstraints.PIN_CARD)) {
            pinCard = bundle.getString(EmvPinConstraints.PIN_CARD);
        }
        if (bundle.containsKey(EmvPinConstraints.PIN_BYPASS)) {
            pinBypass = bundle.getBoolean(EmvPinConstraints.PIN_BYPASS);
        }
        if (bundle.containsKey(EmvPinConstraints.PIN_COUNTER)) {
            pinCounter = bundle.getInt(EmvPinConstraints.PIN_COUNTER);
        }
        if (bundle.containsKey(EmvPinConstraints.PIN_CARD_RANDOM)) {
            pinRandom = bundle.getByteArray(EmvPinConstraints.PIN_CARD_RANDOM);
        }
        if (bundle.containsKey(EmvPinConstraints.PIN_MODULE)) {
            pinModule = bundle.getByteArray(EmvPinConstraints.PIN_MODULE);
        }
        if (bundle.containsKey(EmvPinConstraints.PIN_EXPONENT)) {
            pinExponent = bundle.getByteArray(EmvPinConstraints.PIN_EXPONENT);
        }

        switch (pinType) {
            case ONLINE_PIN:
                title = "Online PIN";
                isOnlinePin = true;
                break;
            case PLAIN_PIN:
            case ENCIPHER_PIN:
                isOnlinePin = false;
                title = "Offline PIN";
                if (pinCounter > 1) {
                    message = "PIN " + pinCounter + " ";
                } else if (pinCounter == 1) {
                    message = "PIN Last Times";
                }
                break;
            default:
                break;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.pinpad_input, null);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvMessage = view.findViewById(R.id.tvMessage);
        tv_pwd = view.findViewById(R.id.tv_pwd);

        tvTitle.setText(title);
        tvMessage.setText(message);

        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        showDialog();
    }

    public int showDialog() {
        int result;

        switch (pinType) {
            case PLAIN_PIN:
                result = onVerifyPlainPin();
                break;
            case ONLINE_PIN:
                result = onOnlinePin();
                break;
            case ENCIPHER_PIN:
                result = onVerifyEncipherPin();
                break;
            default:
                result = -1;
                break;
        }

        return result;
    }

    public void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (mOrientationListener != null) {
            mOrientationListener.disable();
        }
    }

    private int onVerifyPlainPin() {
        pinpadManager.startInputPin(pinInfo, pinEventListener);
        return 0;
    }

    private int onVerifyEncipherPin() {
        ISecurityManager securityManager = TransInitActivity.isFinancialInit ? FinancialEngine.INSTANCE.getSecurityManager() : null;
        if (pinModule == null || securityManager == null) {
            return -1;
        }

        securityManager.writeRsaKey(keyIndex, pinModule, pinExponent);
        pinpadManager.startInputPin(pinInfo, pinEventListener);
        return 0;
    }

    private int onOnlinePin() {

        // XCSW add for PinpadRotate start
        mOrientationListener = new OrientationEventListener(dialog.getContext(), SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                int curOrientation = PinpadUtils.doGetScreenOrientation(dialog.getContext());
                if (curOrientation != mOrientation) {
                    mOrientation = curOrientation;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isOrientation = true;
                            pinpadManager.startInputPin(pinInfo, pinEventListener);
                        }
                    }, 100);
                }
            }
        };
        if (PinpadUtils.doCheckSupportRotate() && mOrientationListener.canDetectOrientation()) {
            mOrientation = PinpadUtils.doGetScreenOrientation(dialog.getContext());
            mOrientationListener.enable();
        } else {
            mOrientationListener.disable();
        }
        // XCSW add for PinpadRotate end

        byte[] data = new byte[24];
        if (!isEncrypt) {
            byte[] temp = CalcPinBlock.calcPinBlock(pinCard).getBytes();
            System.arraycopy(temp, 0, data, 0, 16);
        } else {
            byte[] temp = pinCard.getBytes();
            System.arraycopy(temp, 0, data, 0, 16);
        }

        byte[] formatData = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(formatData, 0, data, 16, 8);

        pinpadManager.startInputPin(pinInfo, pinEventListener);
        return 0;
        //return hsmManage.PedGetPinBlock(keyMode, keyIndex, 0, DEFAULT_TIMEOUT_MS, data, DEFAULT_EXP_PIN_LEN_IND);
    }

    private class PinEventListener implements PinpadInputCallback {

        private String TAG = "PinEventListener";

        @Override
        public void onInput(int len, int key) {
            Log.d(TAG, "onInput====>>len:" + len + " key:" + key);
            //FinancialEngine.INSTANCE.getGeneralManager().setBeep(true, 200, 200);
            StringBuilder info = new StringBuilder();
            while (0 != (len--)) {
                info.append("*");
            }
            if (info.length() <= 12) {
                tv_pwd.setText(info.toString());
            }
        }

        @Override
        public void onPinSuccess(int verifyResult, byte[] pinBlock, String ksn) {
            Log.d(TAG, "onPinSuccess====>>verifyResult:" + verifyResult);
            myOnPinSuccess(pinBlock, PosUtils.hexStringToBytes(ksn));
            closeDialog();
        }

        @Override
        public void onPinError(int verifyResult, int pinTryCntOut) {
            Log.e(TAG, "onPinError====>>verifyResult:" + verifyResult);
            myOnPinError(verifyResult, pinTryCntOut);
            closeDialog();
        }

        @Override
        public void onScreenRotation() {

        }
    }

    private void myOnPinSuccess(byte[] pinBlock, byte[] pinKsn) {
        Bundle bundle = new Bundle();
        bundle.putInt(EmvPinConstraints.OUT_PIN_VERIFY_RESULT, EmvPinConstraints.VERIFY_SUCCESS);
        bundle.putInt(EmvPinConstraints.OUT_PIN_TRY_COUNTER, 0);
        if (pinBlock != null) {
            bundle.putByteArray(EmvPinConstraints.OUT_PIN_BLOCK, pinBlock);
        }
        if (emvManager != null) {
            emvManager.setPinResponse(bundle);
        }
        pinInputFinish.onSuccess(pinBlock, pinKsn);
    }

    private void myOnPinError(int verifyResult, int pinTryCntOut) {
        Bundle bundle = new Bundle();
        bundle.putInt(EmvPinConstraints.OUT_PIN_VERIFY_RESULT, verifyResult);
        bundle.putInt(EmvPinConstraints.OUT_PIN_TRY_COUNTER, pinTryCntOut);
        if (emvManager != null) {
            emvManager.setPinResponse(bundle);
        }

        pinInputFinish.onError(verifyResult, pinTryCntOut);
    }

    static class CalcPinBlock {

        static String calcPinBlock(String accountNumber) {
            return "0000" + extractAccountNumberPart(accountNumber);
        }

        static String extractAccountNumberPart(String accountNumber) {
            String accountNumberPart;
            accountNumberPart = takeLastN(accountNumber, 13);
            accountNumberPart = takeFirstN(accountNumberPart, 12);
            return accountNumberPart;
        }

        static String takeLastN(String str, int n) {
            if (str.length() > n) {
                return str.substring(str.length() - n);
            } else {
                if (str.length() < n) {
                    return zero(str, n);
                } else {
                    return str;
                }
            }
        }

        static String takeFirstN(String str, int n) {
            if (str.length() > n) {
                return str.substring(0, n);
            } else {
                if (str.length() < n) {
                    return zero(str, n);
                } else {
                    return str;
                }
            }
        }

        static String zero(String str, int len) {
            str = str.trim();
            StringBuilder builder = new StringBuilder(len);
            int fill = len - str.length();
            while (fill-- > 0) {
                builder.append((char) 0);
            }
            builder.append(str);
            return builder.toString();
        }
    }
}
