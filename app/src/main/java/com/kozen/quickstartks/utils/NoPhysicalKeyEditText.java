package com.kozen.quickstartks.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class NoPhysicalKeyEditText extends EditText {
    public NoPhysicalKeyEditText(Context context) {
        super(context);
    }

    public NoPhysicalKeyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoPhysicalKeyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 核心：拦截物理按键的 KeyEvent 事件
     * @param keyCode 按键码（如 KEYCODE_0、KEYCODE_A 等）
     * @param event 按键事件
     * @return true 表示消费事件（不传递），false 表示放行
     */
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        // 拦截所有物理按键的 DOWN/UP 事件
        // 若需保留部分按键（如返回键 KEYCODE_BACK），可添加判断：
        // if (keyCode == KeyEvent.KEYCODE_BACK) {
        //     return super.onKeyPreIme(keyCode, event);
        // }
        return true; // 消费事件，不传递给 EditText 处理
    }

    /**
     * 兜底：重写 onKeyDown 确保物理按键完全被拦截
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true; // 拦截物理按键的按下事件
    }
}
