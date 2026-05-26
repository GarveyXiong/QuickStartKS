package com.kozen.quickstartks.emvconfig;

public interface IBerTlvLogger {

    boolean isDebugEnabled();

    void debug(String aFormat, Object... args);
}
