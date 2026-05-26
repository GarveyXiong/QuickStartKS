package com.kozen.quickstartks;

import android.content.Context;
import android.util.AttributeSet;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class NoTipDecoratedBarcodeView extends DecoratedBarcodeView {

    public NoTipDecoratedBarcodeView(Context context) {
        super(context);
    }

    public NoTipDecoratedBarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTipDecoratedBarcodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setStatusText("");
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        // 不调用父类的 onDraw 方法中绘制提示语的部分
        // 或者可以在这里添加自己的绘制逻辑，但不包含提示语绘制
        super.onDraw(canvas);
    }
}    