package com.kozen.quickstartks.utils;



import static com.kozen.quickstartks.QrPaymentActivity.mQrAmount;

import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/getQrCode")
    String getQrCode() {
        NetworkMonitor.getInstance().notifyListener();
        return mQrAmount;
    }
}

