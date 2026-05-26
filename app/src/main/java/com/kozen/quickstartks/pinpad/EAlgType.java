package com.kozen.quickstartks.pinpad;

import com.kozen.financial.constant.ConstantSecurity;

public enum EAlgType {
    ALG_2TDEA(ConstantSecurity.KEY_ALG_TYPE_2TDEA),
    ALG_3TDEA(ConstantSecurity.KEY_ALG_TYPE_3TDEA),
    ALG_AES_128(ConstantSecurity.KEY_ALG_TYPE_AES_128),
    ALG_AES_192(ConstantSecurity.KEY_ALG_TYPE_AES_192),
    ALG_AES_256(ConstantSecurity.KEY_ALG_TYPE_AES_256);

    private final int algType;

    private EAlgType(int algType) {
        this.algType = algType;
    }

    public int getAlgType() {
        return algType;
    }
}
