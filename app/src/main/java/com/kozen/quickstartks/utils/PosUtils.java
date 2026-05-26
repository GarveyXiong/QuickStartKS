package com.kozen.quickstartks.utils;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;


public class PosUtils {
    private static final String TAG = "PosUtils";
    private static final char[] CHARS_TABLES = "0123456789ABCDEF".toCharArray();
    private static final byte[] BYTES = new byte[128];

    public PosUtils() {
    }

    public static int hexCharToInt(char var0) {
        if (var0 >= '0' && var0 <= '9') {
            return var0 - 48;
        } else if (var0 >= 'A' && var0 <= 'F') {
            return var0 - 65 + 10;
        } else if (var0 >= 'a' && var0 <= 'f') {
            return var0 - 97 + 10;
        } else {
            throw new RuntimeException("invalid hex char '" + var0 + "'");
        }
    }

    public static String bytesToAscii(byte[] var0, int var1, int var2) {
        if (var0 != null && var0.length != 0 && var1 >= 0 && var2 > 0) {
            if (var1 < var0.length && var0.length - var1 >= var2) {
                String var3 = null;
                byte[] var4 = new byte[var2];
                System.arraycopy(var0, var1, var4, 0, var2);

                try {
                    var3 = new String(var4, "ISO8859-1");
                } catch (UnsupportedEncodingException var6) {
                }

                return var3;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String bytesToAscii(byte[] var0, int var1) {
        return bytesToAscii(var0, 0, var1);
    }

    public static String bytesToAscii(byte[] var0) {
        return bytesToAscii(var0, 0, var0.length);
    }

    public static String bytesToHexString(byte[] var0, int var1, int var2) {
        if (var0 == null) {
            return "null!";
        } else {
            StringBuilder var4 = new StringBuilder(2 * var2);

            for (int var5 = 0; var5 < var2; ++var5) {
                int var3 = 15 & var0[var1 + var5] >> 4;
                var4.append("0123456789abcdef".charAt(var3));
                var3 = 15 & var0[var1 + var5];
                var4.append("0123456789abcdef".charAt(var3));
            }

            return var4.toString();
        }
    }

    public static String bytesToHexString(byte[] var0, int var1) {
        return var0 == null ? "null!" : bytesToHexString(var0, 0, var1);
    }

    public static String bytesToHexString(byte[] var0) {
        return var0 == null ? "null!" : bytesToHexString(var0, var0.length);
    }

    public static byte[] hexStringToBytes(String var0) {
        if (var0 == null) {
            return null;
        } else {
            int var2 = var0.length();

            try {
                byte[] var1 = new byte[var2 / 2];

                for (int var3 = 0; var3 < var2; var3 += 2) {
                    var1[var3 / 2] = (byte) (hexCharToInt(var0.charAt(var3)) << 4 | hexCharToInt(var0.charAt(var3 + 1)));
                }

                return var1;
            } catch (RuntimeException var4) {
                return null;
            }
        }
    }

    public static byte[] shortToBytesLe(short var0) {
        byte[] var1 = new byte[2];

        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = (byte) (var0 >> var2 * 8 & 255);
        }

        return var1;
    }

    public static byte[] shortToBytesBe(short var0) {
        byte[] var1 = new byte[2];

        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var1.length - var2 - 1] = (byte) (var0 >> var2 * 8 & 255);
        }

        return var1;
    }

    public static byte[] intToBytesLe(int var0) {
        byte[] var1 = new byte[4];

        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = (byte) (var0 >> var2 * 8 & 255);
        }

        return var1;
    }

    public static byte[] intToBytesBe(int var0) {
        byte[] var1 = new byte[4];

        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var1.length - var2 - 1] = (byte) (var0 >> var2 * 8 & 255);
        }

        return var1;
    }

    public static int bytesToIntLe(byte[] var0) {
        if (var0 != null && var0.length <= 4) {
            int var1 = 0;

            for (int var2 = 0; var2 < var0.length; ++var2) {
                var1 += (var0[var2] & 255) << var2 * 8;
            }

            return var1;
        } else {
            throw new RuntimeException("invalid arg");
        }
    }

    public static int bytesToIntLe(byte[] var0, int var1, int var2) {
        return bytesToIntLe(Arrays.copyOfRange(var0, var1, var2));
    }

    public static int bytesToIntBe(byte[] var0) {
        if (var0 != null && var0.length <= 4) {
            int var1 = 0;

            for (int var2 = 0; var2 < var0.length; ++var2) {
                var1 += (var0[var2] & 255) << (var0.length - var2 - 1) * 8;
            }

            return var1;
        } else {
            throw new RuntimeException("invalid arg");
        }
    }

    public static int bytesToIntBe(byte[] var0, int var1, int var2) {
        return bytesToIntBe(Arrays.copyOfRange(var0, var1, var2));
    }

    public static int bytesToIntLe(byte var0, byte var1, byte var2, byte var3) {
        boolean var4 = false;
        int var5 = var0 & 255;
        var5 += (var1 & 255) << 8;
        var5 += (var2 & 255) << 16;
        var5 += (var3 & 255) << 24;
        return var5;
    }

    public static int bytesToIntBe(byte var0, byte var1, byte var2, byte var3) {
        boolean var4 = false;
        int var5 = (var0 & 255) << 24;
        var5 += (var1 & 255) << 16;
        var5 += (var2 & 255) << 8;
        var5 += var3 & 255;
        return var5;
    }

    public static short bytesToShortLe(byte[] var0) {
        if (var0 != null && var0.length <= 2) {
            short var1 = 0;

            for (int var2 = 0; var2 < var0.length; ++var2) {
                var1 = (short) (var1 + ((var0[var2] & 255) << var2 * 8));
            }

            return var1;
        } else {
            throw new RuntimeException("invalid arg");
        }
    }

    public static short bytesToShortLe(byte[] var0, int var1, int var2) {
        return bytesToShortLe(Arrays.copyOfRange(var0, var1, var2));
    }

    public static short bytesToShortBe(byte[] var0) {
        if (var0 != null && var0.length <= 2) {
            short var1 = 0;

            for (int var2 = 0; var2 < var0.length; ++var2) {
                var1 = (short) (var1 + ((var0[var2] & 255) << (var0.length - var2 - 1) * 8));
            }

            return var1;
        } else {
            throw new RuntimeException("invalid arg");
        }
    }

    public static short bytesToShortBe(byte[] var0, int var1, int var2) {
        return bytesToShortBe(Arrays.copyOfRange(var0, var1, var2));
    }

    public static short bytesToShortLe(byte var0, byte var1) {
        boolean var2 = false;
        short var3 = (short) (var0 & 255);
        var3 += (short) ((var1 & 255) << 8);
        return var3;
    }

    public static short bytesToShortBe(byte var0, byte var1) {
        boolean var2 = false;
        short var3 = (short) ((var0 & 255) << 8);
        var3 += (short) (var1 & 255);
        return var3;
    }

    public static void byteArraySetByte(byte[] var0, byte var1, int var2) {
        var0[var2] = var1;
    }

    public static void byteArraySetByte(byte[] var0, int var1, int var2) {
        var0[var2] = (byte) (var1 & 255);
    }

    public static void byteArraySetBytes(byte[] var0, byte[] var1, int var2) {
        System.arraycopy(var1, 0, var0, var2, var1.length);
    }

    public static void byteArraySetWord(byte[] var0, int var1, int var2) {
        var0[var2] = (byte) (var1 & 255);
        var0[var2 + 1] = (byte) (var1 >> 8 & 255);
    }

    public static void byteArraySetWordBe(byte[] var0, int var1, int var2) {
        var0[var2] = (byte) (var1 >> 8 & 255);
        var0[var2 + 1] = (byte) (var1 & 255);
    }

    public static void byteArraySetInt(byte[] var0, int var1, int var2) {
        var0[var2] = (byte) (var1 & 255);
        var0[var2 + 1] = (byte) (var1 >> 8 & 255);
        var0[var2 + 2] = (byte) (var1 >> 16 & 255);
        var0[var2 + 3] = (byte) (var1 >> 24 & 255);
    }

    public static void byteArraySetIntBe(byte[] var0, int var1, int var2) {
        var0[var2] = (byte) (var1 >> 24 & 255);
        var0[var2 + 1] = (byte) (var1 >> 16 & 255);
        var0[var2 + 2] = (byte) (var1 >> 8 & 255);
        var0[var2 + 3] = (byte) (var1 & 255);
    }

    public static void delayms(int var0) {
        if (var0 > 0) {
            try {
                Thread.sleep((long) var0);
            } catch (InterruptedException var2) {
            }
        }

    }

    public static boolean isAscii(char var0) {
        return var0 <= 127;
    }

    public static boolean isAscii(String var0) {
        for (int var1 = 0; var1 < var0.length(); ++var1) {
            if (!isAscii(var0.charAt(var1))) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasAsciiChar(String var0) {
        for (int var1 = 0; var1 < var0.length(); ++var1) {
            if (isAscii(var0.charAt(var1))) {
                return true;
            }
        }

        return false;
    }

    public static boolean isDigitOrEnCharacter(byte var0) {
        return var0 >= 48 && var0 <= 57 || var0 >= 65 && var0 <= 90 || var0 >= 97 && var0 <= 122;
    }

    public static String bcdToDecString(byte[] var0) {
        StringBuffer var1 = new StringBuffer(var0.length * 2);

        for (int var2 = 0; var2 < var0.length; ++var2) {
            var1.append((byte) ((var0[var2] & 240) >>> 4));
            var1.append((byte) (var0[var2] & 15));
        }

        return var1.toString().substring(0, 1).equalsIgnoreCase("0") ? var1.toString().substring(1) : var1.toString();
    }

    public static byte[] decStringToBcd(String var0) {
        int var1 = var0.length();
        int var2 = var1 % 2;
        if (var2 != 0) {
            var0 = "0" + var0;
            var1 = var0.length();
        }

        byte[] var3 = new byte[var1];
        if (var1 >= 2) {
            var1 /= 2;
        }

        byte[] var4 = new byte[var1];
        var3 = var0.getBytes();

        for (int var7 = 0; var7 < var0.length() / 2; ++var7) {
            int var5;
            if (var3[2 * var7] >= 48 && var3[2 * var7] <= 57) {
                var5 = var3[2 * var7] - 48;
            } else if (var3[2 * var7] >= 97 && var3[2 * var7] <= 122) {
                var5 = var3[2 * var7] - 97 + 10;
            } else {
                var5 = var3[2 * var7] - 65 + 10;
            }

            int var6;
            if (var3[2 * var7 + 1] >= 48 && var3[2 * var7 + 1] <= 57) {
                var6 = var3[2 * var7 + 1] - 48;
            } else if (var3[2 * var7 + 1] >= 97 && var3[2 * var7 + 1] <= 122) {
                var6 = var3[2 * var7 + 1] - 97 + 10;
            } else {
                var6 = var3[2 * var7 + 1] - 65 + 10;
            }

            int var8 = (var5 << 4) + var6;
            byte var9 = (byte) var8;
            var4[var7] = var9;
        }

        return var4;
    }

    public static byte[] stringToBcd(String var0) {
        return stringToBcd(var0, var0 != null ? var0.length() : 0);
    }

    public static byte[] stringToBcd(String var0, int var1) {
        if (var1 % 2 != 0) {
            ++var1;
        }

        while (var0.length() < var1) {
            var0 = "0" + var0;
        }

        byte[] var2 = new byte[var0.length() / 2];
        char[] var3 = var0.toCharArray();
        boolean var4 = false;
        int var5 = 0;

        for (int var7 = 0; var7 < var3.length; var7 += 2) {
            boolean var6 = false;
            int var8;
            if (var3[var7] >= '0' && var3[var7] <= '9') {
                var8 = var3[var7] - 48 << 4;
            } else {
                if (var3[var7] >= 'a' && var3[var7] <= 'f') {
                    var3[var7] = (char) (var3[var7] - 32);
                }

                var8 = var3[var7] - 48 - 7 << 4;
            }

            if (var3[var7 + 1] >= '0' && var3[var7 + 1] <= '9') {
                var8 += var3[var7 + 1] - 48;
            } else {
                if (var3[var7 + 1] >= 'a' && var3[var7 + 1] <= 'f') {
                    var3[var7 + 1] = (char) (var3[var7 + 1] - 32);
                }

                var8 += var3[var7 + 1] - 48 - 7;
            }

            var2[var5] = (byte) var8;
            ++var5;
        }

        return var2;
    }

    public static String bcdToString(byte[] var0) {
        return bcdToString(var0, 0, var0 != null ? var0.length : 0);
    }

    public static String bcdToString(byte[] var0, int var1, int var2) {
        if (var2 > 0 && var1 >= 0 && var0 != null) {
            StringBuffer var3 = new StringBuffer();

            for (int var4 = 0; var4 < var2; ++var4) {
                var3.append(Integer.toHexString((var0[var4 + var1] & 240) >> 4));
                var3.append(Integer.toHexString(var0[var4 + var1] & 15));
            }

            return var3.toString();
        } else {
            return null;
        }
    }

    public static String toHexString(byte[] var0) {
        return toHexString(var0, 0, var0.length);
    }

    public static String toHexString(byte[] var0, int var1) {
        return toHexString(var0, 0, var1);
    }

    public static String toHexString(byte[] var0, int var1, int var2) {
        char[] var3 = new char[var2 * 2];
        int var4 = var1;

        for (int var5 = 0; var4 < var1 + var2; ++var4) {
            byte var6 = var0[var4];
            var3[var5++] = CHARS_TABLES[(var6 & 240) >>> 4];
            var3[var5++] = CHARS_TABLES[var6 & 15];
        }

        return new String(var3);
    }

    public static byte[] parseHex(String var0) {
        char[] var1 = var0.replace("\n", "").replace(" ", "").toUpperCase().toCharArray();
        byte[] var2 = new byte[var1.length / 2];
        int var3 = 0;

        for (int var4 = 0; var4 < var2.length; ++var4) {
            byte var5 = BYTES[var1[var3++] & 127];
            byte var6 = BYTES[var1[var3++] & 127];
            var2[var4] = (byte) ((var5 << 4) + var6);
        }

        return var2;
    }

    static {
        for (int var0 = 0; var0 < 10; ++var0) {
            BYTES[48 + var0] = (byte) var0;
            BYTES[65 + var0] = (byte) (10 + var0);
            BYTES[97 + var0] = (byte) (10 + var0);
        }

    }

    //eg. "100.99" to 10099
    public static long strAmount2Long(String strAmount) {
        BigDecimal amountBigDecimal = new BigDecimal(strAmount);
        return amountBigDecimal.multiply(new BigDecimal(100)).longValue();
    }
}
