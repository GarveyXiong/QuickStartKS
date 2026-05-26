package com.kozen.quickstartks.pinpad;

import com.kozen.financial.constant.ConstantSecurity;

public enum EEncAlgType {
    TDES(ConstantSecurity.ENCRYPTION_ALGORITHM_TDES),
    AES(ConstantSecurity.ENCRYPTION_ALGORITHM_AES),
    SM4(ConstantSecurity.ENCRYPTION_ALGORITHM_SM4);

    private final int encAlgType;

    private EEncAlgType(int encAlgType) {
        this.encAlgType = encAlgType;
    }

    public int getEncAlgType() {
        return encAlgType;
    }
}
