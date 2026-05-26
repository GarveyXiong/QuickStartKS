package com.kozen.quickstartks.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class QrCodeGenerator {

    public static Bitmap generateQrCode(String content, int width, int height) {
        try {
            // 创建参数设置，强制边距为0
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 0); // 关键设置：移除边距
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 高纠错级别

            // 使用QRCodeWriter替代MultiFormatWriter
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 创建像素数组
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixels[y * width + x] = bitMatrix.get(x, y) ?
                            0xFF000000 : 0xFFFFFFFF; // 黑色和白色
                }
            }

            // 创建Bitmap
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}    