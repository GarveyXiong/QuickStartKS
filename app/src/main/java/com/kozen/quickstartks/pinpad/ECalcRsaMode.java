package com.kozen.quickstartks.pinpad;

import com.kozen.financial.constant.ConstantSecurity;

public enum ECalcRsaMode {
    NO_PADDING(ConstantSecurity.PED_CALC_RSA_MODE_NO_PADDING),
    PKCS1_PADDING(ConstantSecurity.PED_CALC_RSA_MODE_PKCS1_PADDING),
    OAEP_PADDING(ConstantSecurity.PED_CALC_RSA_MODE_OAEP_PADDING);

    private int calcRsaMode;

    private ECalcRsaMode(int calcRsaMode) {
        this.calcRsaMode = calcRsaMode;
    }

    public int getCalcRsaMode() {
        return this.calcRsaMode;
    }
}
