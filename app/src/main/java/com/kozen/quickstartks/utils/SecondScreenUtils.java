package com.kozen.quickstartks.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kozen.component.secondaryScreen.IResultCallback;
import com.kozen.component.secondaryScreen.ISecondaryScreen;
import com.kozen.component_client.ComponentEngine;
import com.kozen.quickstartks.R;
import com.kozen.quickstartks.TransInitActivity;

public class SecondScreenUtils {

    private static String TAG = "SecondScreenUtils";
    private String RES_ROOT_PATH =
            "${Environment.getExternalStorageDirectory().absolutePath}/ViceScreen";
    private int brightnessState = 0; //屏幕亮度等级，第一次默认60%
    private static ISecondaryScreen secondaryScreen = TransInitActivity.isComponenInit ? ComponentEngine.INSTANCE.getSecondaryScreenManager() : null;

    /**
     * 按一次上电，再按下电
     */
    public static void powerControl(boolean powerState) {
        if (SecondScreenUtils.secondaryScreen != null) {
            SecondScreenUtils.secondaryScreen.power(powerState);
        }
    }

    public static void showPic() {
        String picPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ViceScreen" + "/img4.png";
        if (SecondScreenUtils.secondaryScreen != null) {
            secondaryScreen.showPic(picPath);
        }
    }

    public static void showView(Context context, int layoutId) {
        if (SecondScreenUtils.secondaryScreen != null) {
            int[] viceScreenWidth =
                    SecondScreenUtils.secondaryScreen.getScreenResolution();
//        (0) ?: 378
            int[] viceScreenHeight =
                    SecondScreenUtils.secondaryScreen.getScreenResolution();
            //(1) ?: 172
            View layoutV =
                    layoutToView(context, layoutId, viceScreenWidth[0], viceScreenHeight[1]);
            SecondScreenUtils.secondaryScreen.show(layoutV, new IResultCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    }
            );
        }

    }

    public static void showQrView(Context context, int layoutId, String text) {
        if (SecondScreenUtils.secondaryScreen != null) {
            int[] viceScreenWidth =
                    SecondScreenUtils.secondaryScreen.getScreenResolution();
//        (0) ?: 378
            int[] viceScreenHeight =
                    SecondScreenUtils.secondaryScreen.getScreenResolution();
            //(1) ?: 172
            View layoutV =
                    layoutToView(context, layoutId, viceScreenWidth[0], viceScreenHeight[1]);
            TextView textView = layoutV.findViewById(R.id.tv_amount);
            textView.setText(text);
            SecondScreenUtils.secondaryScreen.show(layoutV, new IResultCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    }
            );
        }

    }

    public static void showView(Context context, int layoutId, String text) {
        if (SecondScreenUtils.secondaryScreen != null) {
            View layoutV =
                    layoutToView(context, layoutId, 378, 172);
            TextView textView = layoutV.findViewById(R.id.tv_amount);
            Log.e(TAG, "showView===>>>text:" + text);
            textView.setText(text);
            SecondScreenUtils.secondaryScreen.show(layoutV, new IResultCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    }
            );
        }

    }

    public static void showDefaultImage() {
        String newPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/default.png";
        if (SecondScreenUtils.secondaryScreen != null) {
            SecondScreenUtils.secondaryScreen.showPic(newPath);
        }

    }


    public static View layoutToView(Context context, int layoutId, int widthPX, int heightPX) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup rootView = (ViewGroup) inflater.inflate(layoutId, null);

        // 精确测量和布局
        int widthSpec = View.MeasureSpec.makeMeasureSpec(widthPX, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(heightPX, View.MeasureSpec.EXACTLY);

        rootView.measure(widthSpec, heightSpec);
        rootView.layout(0, 0, widthPX, heightPX);

        return rootView;
    }
}
