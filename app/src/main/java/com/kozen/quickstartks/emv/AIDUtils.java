package com.kozen.quickstartks.emv;

import com.kozen.financial.aidl.emv.EmvAid;
import com.kozen.financial.util.emv.tlv.BerTag;
import com.kozen.financial.util.emv.tlv.BerTlvBuilder;
import com.kozen.financial.util.emv.tlv.HexUtil;
import com.kozen.quickstartks.emvconfig.EMVConfig;
import com.kozen.quickstartks.utils.PosUtils;

import java.util.ArrayList;
import java.util.List;

public class AIDUtils {

    public static final String MASTERCARD_AID_ROOT = "A000000004";
    public static final String UPI_AID_ROOT = "A000000333";
    public static EmvAid createAID(String aid, String version, EMVConfig emvConfig) {
        EmvAid appConifg = new EmvAid();
        appConifg.AID = PosUtils.hexStringToBytes(aid);
        appConifg.Version = PosUtils.hexStringToBytes(version);
        appConifg.SelectIndicator = true;
        appConifg.dDOL = PosUtils.hexStringToBytes("9F0206");
        appConifg.tDOL = PosUtils.hexStringToBytes("9F3704");
        appConifg.TACDenial = PosUtils.hexStringToBytes("0010000000");
        appConifg.TACOnline = PosUtils.hexStringToBytes("dc4004f800");
        appConifg.TACDefault = PosUtils.hexStringToBytes("dc4000a800");
        appConifg.Threshold = 10000;
        appConifg.TargetPercentage = 0;
        appConifg.MaxTargetPercentage = 99;
        appConifg.FloorLimit = 0;
        if (emvConfig != null) {
            appConifg.ContactlessTransLimit = Integer.valueOf(emvConfig.contactlessTransactionLimit);
            appConifg.ContactlessCVMLimit = Integer.valueOf(emvConfig.contactlessCvmLimit);
            appConifg.ContactlessFloorLimit = Integer.valueOf(emvConfig.contactlessFloorLimit);
            if (appConifg.ContactlessTransLimit <= appConifg.ContactlessCVMLimit) {
                appConifg.ContactlessTransLimit = 1999999999;
            }
        } else {
            appConifg.ContactlessTransLimit = 9999900;
            appConifg.ContactlessCVMLimit = 15000;
            appConifg.ContactlessFloorLimit = 0;
        }
        appConifg.DynamicTransLimit = 9999900;
        return appConifg;
    }

    public static List<EmvAid> generateAids(EMVConfig emvConfig) {
        List<EmvAid> aidList = new ArrayList<>();
        EmvAid aid;

        // VISA
        aid = createAID("A0000000031010", "008C", emvConfig);
//        aid.ContactlessTransLimit
        aidList.add(aid);

        aid = createAID("A0000000032010", "008C", emvConfig);
        aid.AcquirerIdentifier = PosUtils.hexStringToBytes("000000000001");
        aidList.add(aid);

        aid = createAID("A0000000033010", "008C", emvConfig);
        aid.CombinationData = getKernel(getLimit(999999999999L, 999999999999L, 0, 999999999999L),
                                        HexUtil.parseHex("DF0101019F660436204000"),
                                        null,null,null,null);
        aidList.add(aid);

        // Unionpay
        aid = createAID("A000000333010101", "0030", emvConfig);
        aidList.add(aid);

        aid = createAID("A000000333010102", "0030", emvConfig);
        aidList.add(aid);

        aid = createAID("A000000333010103", "0030", emvConfig);
        aidList.add(aid);
        // only for Only for USA
        aid = createAID("A000000333010108", "0030", emvConfig);
        aidList.add(aid);

        aid = createAID("A000000333010106", "0030", emvConfig);
        aidList.add(aid);

		//A0000006351010
        aid = createAID("A0000006351010", "0030", emvConfig);
        aidList.add(aid);

        // MasterCard
        aid = createAID("A00000000410", "0002", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0400000000");
        aid.TACOnline = PosUtils.hexStringToBytes("f850acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc50aca000");
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("6C00000000000000");
        aidList.add(aid);

        aid = createAID("A00000000430", "0002", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0400000000");
        aid.TACOnline = PosUtils.hexStringToBytes("f850acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc50aca000");
        // aid.ContactlessCVMLimit = 50000;
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("4C00800000000000");
        aidList.add(aid);

        aid = createAID("A0000000043060", "0002", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0000800000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc50bcf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc50bca000");
        // aid.ContactlessCVMLimit = 50000;
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("4C00800000000000");
        aidList.add(aid);

        aid = createAID("A0000000041010", "0002", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0000000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc50808800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc50b8a000");
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("6C00000000000000");
        aidList.add(aid);

        // Discover
        aid = createAID("A0000001523010", "0001", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fce09cf800");
        aid.TACDefault = PosUtils.hexStringToBytes("dc00002000");
        aidList.add(aid);

        aid = createAID("A0000001524010", "0001", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fce09cf800");
        aid.TACDefault = PosUtils.hexStringToBytes("dc00002000");
        aidList.add(aid);

        // AMEX
        aid = createAID("A00000002501", "0001", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0000000000");
        aid.TACOnline = PosUtils.hexStringToBytes("c800000000");
        aid.TACDefault = PosUtils.hexStringToBytes("c800000000");
        aidList.add(aid);

        aid = createAID("A000000025010402", "0001", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0000000000");
        aid.TACOnline = PosUtils.hexStringToBytes("c800000000");
        aid.TACDefault = PosUtils.hexStringToBytes("c800000000");
        aidList.add(aid);

        // RuPay
        aid = createAID("A0000005241010", "0064", emvConfig);
        aidList.add(aid);

        // MIR
        aid = createAID("A0000006581010", "0100", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800");
        aidList.add(aid);

        aid = createAID("A0000006581099", "0100", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800");
        aidList.add(aid);

        aid = createAID("A0000006582010", "0100", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800");
        aidList.add(aid);

        // JCB
        aid = createAID("A0000000651010", "0021", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800");
        aidList.add(aid);

        aid = createAID("A0000003710001", "0001", emvConfig);
        aid.dDOL = PosUtils.hexStringToBytes("9F3704");
        aid.tDOL = PosUtils.hexStringToBytes("9F3704");
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800");
        aidList.add(aid);

        /**
         * AMEX
         */
        aid = createAID("A00000002501", "0001",emvConfig);
        aidList.add(aid);

        /**
         * AMEX
         */
        aid = createAID("A0000005241010", "0002",emvConfig);
        aidList.add(aid);

        /**
         * MIR
         */
        aid = createAID("A0000006581010", "0100",emvConfig);
        aidList.add(aid);
        aid = createAID("A0000006581099", "0100",emvConfig);
        aidList.add(aid);
        aid = createAID("A0000006582010", "0100",emvConfig);
        aidList.add(aid);


        /**
         * verve Card
         */
        aid = createAID("A0000003710001", "0001",emvConfig);
        aidList.add(aid);

        /**
         * NSICCS
         */
        aid = createAID("A0000006021010", "0100",emvConfig);
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800");
        aid.TerminalCountryCode = PosUtils.hexStringToBytes("0360");
        aid.TransCurrencyCode = PosUtils.hexStringToBytes("0360");
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("647A800000000000");
        aid.TerminalCapabilities = PosUtils.hexStringToBytes("E078C8");
        aid.AdditionalTerminalCapabilities = PosUtils.hexStringToBytes("F00080F000");
        aidList.add(aid);

        /**
         * NAPAS
         */
        aid = createAID("A0000007271010", "0100",emvConfig);
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800");
        aid.TerminalCountryCode = PosUtils.hexStringToBytes("0360");
        aid.TransCurrencyCode = PosUtils.hexStringToBytes("0360");
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("647A800000000000");
        aid.TerminalCapabilities = PosUtils.hexStringToBytes("E078C8");
        aid.AdditionalTerminalCapabilities = PosUtils.hexStringToBytes("F00080F000");
        aidList.add(aid);

        /**
         * TROY
         */
        aid = createAID("A0000006723010", "0100",emvConfig);
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000");
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800");
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800");
        aid.TerminalCountryCode = PosUtils.hexStringToBytes("0360");
        aid.TransCurrencyCode = PosUtils.hexStringToBytes("0360");
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("647A800000000000");
        aid.TerminalCapabilities = PosUtils.hexStringToBytes("E078C8");
        aid.AdditionalTerminalCapabilities = PosUtils.hexStringToBytes("F00080F000");
        aidList.add(aid);

        /**
         * 马来西亚
         * AID = a0000006150001
         * Version = 0001
         * SelectIndicator = false
         * TypeIndicator = true
         * AcquirerIdentifier = null!
         * dDOL = 9f3704
         * tDOL = 9f1a0295059a039c01
         * TACDenial = 0000800000
         * TACOnline = b050048000
         * TACDefault = b050048000
         * Threshold = 0
         * TargetPercentage = 0
         * MaxTargetPercentage = 0
         * FloorLimit = 0
         * ContactlessTransLimit = 99999999
         * ContactlessTransLimitL = 99999999
         * ContactlessCVMLimit = 25001
         * ContactlessCVMLimitL = 25001
         * ContactlessFloorLimit = 0
         * ContactlessFloorLimitL = 0
         * DynamicTransLimit = 99999999
         * DynamicTransLimitL = 0
         * TerminalCountryCode = 0458
         * MerchantCategoryCode = 0020
         * TransCurrencyCode = 0458
         * TransCurrencyExp = 02
         * TerminalType = 22
         * TerminalCapabilities = 204800
         * AdditionalTerminalCapabilities = ff80f0a001
         * TerminalRiskManagementData = null!
         * CombinationType = 0
         * CombinationData = null!
         * }]
         */
        aid = createAID("a0000006150001", "0001",emvConfig);
        aid.TACDenial = PosUtils.hexStringToBytes("0000800000");
        aid.TACOnline = PosUtils.hexStringToBytes("b050048000");
        aid.TACDefault = PosUtils.hexStringToBytes("b050048000");
        aid.TerminalCountryCode = PosUtils.hexStringToBytes("0458");
        aid.TransCurrencyCode = PosUtils.hexStringToBytes("0458");
        aid.TerminalCapabilities = PosUtils.hexStringToBytes("204800");
        aid.AdditionalTerminalCapabilities = PosUtils.hexStringToBytes("ff80f0a001");
        aidList.add(aid);

        return aidList;
    }

    public static boolean isMastercard(String app) {
        return app != null && app.startsWith(MASTERCARD_AID_ROOT);
    }

    public static boolean isMastercard(EmvAid kaid) {
        return isMastercard(PosUtils.bytesToHexString(kaid.AID));
    }

    public static boolean isMastercard(byte[] app) {
        return app != null && isMastercard(PosUtils.bytesToHexString(app));
    }

    /**
     * 构建内核相关 TLV 数据
     * 对应原 Kotlin 的 getKernel 函数，Java 中无默认参数，需重载或传 null
     * @param kernel 内核数据
     * @param visa visa 数据
     * @param unionpay 银联数据
     * @param mastercard 万事达数据
     * @param discover 发现卡数据
     * @param mir 俄罗斯Mir卡数据
     * @return 拼接后的 TLV 字节数组
     */
    private static byte[] getKernel(byte[] kernel,
                                    byte[] visa,
                                    byte[] unionpay,
                                    byte[] mastercard,
                                    byte[] discover,
                                    byte[] mir) {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();

        if (kernel != null) {
            tlvBuilder.addBytes(new BerTag("DF10"), kernel);
        }
        if (visa != null) {
            tlvBuilder.addBytes(new BerTag("DF11"), visa);
        }
        if (unionpay != null) {
            tlvBuilder.addBytes(new BerTag("DF12"), unionpay);
        }
        if (mastercard != null) {
            tlvBuilder.addBytes(new BerTag("DF13"), mastercard);
        }
        if (discover != null) {
            tlvBuilder.addBytes(new BerTag("DF14"), discover);
        }
        if (mir != null) {
            tlvBuilder.addBytes(new BerTag("DF17"), mir);
        }

        return tlvBuilder.buildArray();
    }

    /**
     * 【可选重载】适配 Kotlin 默认参数（简化调用，不传的参数默认 null）
     * 若不需要简化调用，可省略此重载方法
     */
    protected static byte[] getKernel(byte[] kernel) {
        return getKernel(kernel, null, null, null, null, null);
    }

    /**
     * 构建限额相关 TLV 数据
     * 对应原 Kotlin 的 getLimit 函数
     * @param contactlessTransLimit 非接交易限额
     * @param contactlessCVMLimit 非接 CVM 限额
     * @param contactlessFloorLimit 非接底限
     * @param contactlessDynamicLimit 非接动态限额
     * @return 拼接后的 TLV 字节数组
     */
    protected static byte[] getLimit(long contactlessTransLimit,
                                     long contactlessCVMLimit,
                                     long contactlessFloorLimit,
                                     long contactlessDynamicLimit) {
        BerTlvBuilder tlvBuilder = new BerTlvBuilder();
        tlvBuilder.addBytes(new BerTag("DF01"), getAmount(contactlessTransLimit));
        tlvBuilder.addBytes(new BerTag("DF02"), getAmount(contactlessCVMLimit));
        tlvBuilder.addBytes(new BerTag("DF03"), getAmount(contactlessFloorLimit));
        tlvBuilder.addBytes(new BerTag("DF04"), getAmount(contactlessDynamicLimit));
        return tlvBuilder.buildArray();
    }

    /**
     * 将金额数字转换为 12 位补零的十六进制字节数组
     * 对应原 Kotlin 的 getAmount 函数
     * @param value 金额数值
     * @return 12 位字符串对应的十六进制字节数组
     */
    protected static byte[] getAmount(long value) {
        StringBuilder builder = new StringBuilder(12);
        builder.append(value);
        // 补前导零，直到长度为 12
        while (builder.length() < 12) {
            builder.insert(0, '0');
        }
        // 调用 HexUtil 解析十六进制字符串为字节数组
        return HexUtil.parseHex(builder.toString());
    }
}
