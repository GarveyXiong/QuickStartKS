package com.kozen.quickstartks.pinpad;

import com.kozen.financial.constant.ConstantSecurity;

public enum EMacMode {
    CBC(ConstantSecurity.MAC_MODE_CBC),
    XOR_ECB_MAC(ConstantSecurity.MAC_MODE_XOR_ECB_MAC),
    ANSI_X9_19(ConstantSecurity.MAC_MODE_ANSI_X9_19),
    ANSI_X9_9(ConstantSecurity.MAC_MODE_ANSI_X9_9);

    private final int macMode;

    private EMacMode(int macMode) {
        this.macMode = macMode;
    }

    public int getMacMode() {
        return macMode;
    }
}
