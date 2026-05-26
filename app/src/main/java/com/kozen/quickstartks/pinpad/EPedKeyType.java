package com.kozen.quickstartks.pinpad;

import com.kozen.financial.constant.ConstantSecurity;

public enum EPedKeyType {
    TLK(ConstantSecurity.PED_TLK),
    TMK(ConstantSecurity.PED_TMK),
    TPK(ConstantSecurity.PED_TPK),
    TAK(ConstantSecurity.PED_TAK),
    TDK(ConstantSecurity.PED_TDK),
    TEK(ConstantSecurity.PED_TEK),
    TIK(ConstantSecurity.PED_TIK),
    TTK(ConstantSecurity.PED_TTK),
    //尚未实现
    SM4_TAK(0x34),
    SM4_TDK(0x35),
    SM4_TMK(0x32),
    SM4_TPK(0x33);

    private final int pedKeyType;

    private EPedKeyType(int pedKeyType) {
        this.pedKeyType = pedKeyType;
    }

    public int getPedkeyType() {
        return pedKeyType;
    }
}
