package com.kozen.quickstartks.pinpad;

import com.kozen.financial.constant.ConstantSecurity;

public enum EAuthentication {
    AUTH_GENERATION(ConstantSecurity.AUTHENTICATION_GENERATION),
    AUTH_VERIFICATION(ConstantSecurity.AUTHENTICATION_VERIFICATION),
    AUTH_BOTH(ConstantSecurity.AUTHENTICATION_BOTH);

    private final int authentication;

    private EAuthentication(int authentication) {
        this.authentication = authentication;
    }

    public int getAuthentication() {
        return this.authentication;
    }
}
