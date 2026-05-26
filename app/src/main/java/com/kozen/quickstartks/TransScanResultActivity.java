package com.kozen.quickstartks;

import static com.kozen.quickstartks.utils.Utils.TRANS_QR;
import static com.kozen.quickstartks.utils.Utils.USD_TAG;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kozen.component.constant.KeyboardConstant;
import com.kozen.component.keyboard.InputCallback;
import com.kozen.component_client.ComponentEngine;
import com.kozen.financial.constant.ConstantPrinter;
import com.kozen.quickstartks.emv.utils.EmvCard;
import com.kozen.quickstartks.emv.utils.Utility;
import com.kozen.quickstartks.utils.DialogUtils;
import com.kozen.quickstartks.utils.ScreenUtils;
import com.kozen.quickstartks.utils.SecondScreenUtils;
import com.kozen.quickstartks.utils.Utils;

public class TransScanResultActivity extends BaseActivity {

    private ScrollView sl_receipt;
    private TextView tv_result_confirm;
    private View reslut_line;
    private String mAmount;
    private SoundPool mSoundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_result);
        if (getSupportActionBar().isShowing() && ScreenUtils.getScreenHeight() <= 480) {
            getSupportActionBar().hide();
        }
        int code = getIntent().getIntExtra(TransActivity.TransResult_Code, -1);
        int Card_Type = getIntent().getIntExtra(TransActivity.TransResult_Card_Type, 0);
        mAmount = getIntent().getStringExtra(TransActivity.TransResult_Amount);
        String currency = getIntent().getStringExtra(Utils.CURRENCY_TAG);
        byte[] data = getIntent().getByteArrayExtra(TransActivity.TransResult_Data);
        ImageView imageView = findViewById(R.id.iv_result_image);
        reslut_line = findViewById(R.id.reslut_line);
        TextView tv_result = findViewById(R.id.tv_result);
        TextView tv_card_number = findViewById(R.id.tv_card_number);
        TextView tv_card_user = findViewById(R.id.tv_card_user);
        tv_card_number.setText(R.string.sub_trans_payent_code);
        tv_card_user.setVisibility(View.INVISIBLE);
        TextView tv_amount = findViewById(R.id.tv_amount);
        tv_result_confirm = findViewById(R.id.tv_result_confirm);
        sl_receipt = findViewById(R.id.sl_receipt);
        LinearLayout content = findViewById(R.id.content);
        LinearLayout result_data_ll = findViewById(R.id.result_data_ll);
        tv_result_confirm.setVisibility(View.GONE);
        tv_amount.setText(mAmount);
        if (data != null) {
            EmvCard emvCard = new EmvCard(data);

            if (emvCard.getCardNumber() != null) {
                tv_card_number.setText(Utility.formatCard(emvCard.getCardNumber(), true));
            } else {
                tv_card_number.setText("");
            }

//            tv_card_user.setText(EmvCardType.getCardType(Card_Type));

            if (emvCard.getCardHolderName() != null) {
                tv_card_user.setText(emvCard.getCardHolderName());
            }
        }

        mAmount = (USD_TAG.equals(currency) ? "$" : "€") + mAmount;
//        mAmount = (USD_TAG.equals(currency) ? "$" : "€") + mAmount;
        if (code == 0) {
            playSound();
            if (TransInitActivity.isExistSecScreen) {
                SecondScreenUtils.showView(TransScanResultActivity.this, R.layout.second_trans_result_success, mAmount);
            }

            sl_receipt.setVisibility(View.VISIBLE);
            tv_result_confirm.setVisibility(View.VISIBLE);
            imageView.clearAnimation();
            tv_result.setText(getString(R.string.trans_result_success));
            tv_result_confirm.setText(getString(R.string.trans_result_print));
            imageView.setImageResource(R.drawable.result_success);
            tv_result_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sl_receipt.scrollTo(0, 0);
                    slideUp(content);
//                String data = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa";
//                Printer.printImage(TransResultActivity.this, Printer.generateBitmap(TransResultActivity.this,data,"fonts/SBSansCondMonoRegular.ttf"));
//                    Printer.printImage(TransResultActivity.this, layoutToBitmap(content), new Printer.PrintFinish() {
//                    View viewBitmap = LayoutInflater.from(TransResultActivity.this).inflate(R.layout.receipt_content, null);

                    if (TransInitActivity.isPOISdk) {
                        PrinterPOI.printImage(TransScanResultActivity.this, layoutToBitmap(TransScanResultActivity.this), new PrinterPOI.PrintFinish() {
                            @Override
                            public void onSuccess(String data) {
                            }

                            @Override
                            public void onError(int errorCode) {
                                content.clearAnimation();
                                if (ConstantPrinter.STATUS_NO_PAPER == errorCode) {
                                    DialogUtils.showAlertDialogCenter(TransScanResultActivity.this, new DialogUtils.DialogCallback() {
                                        @Override
                                        public void onConfirm() {

                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                                }

                            }
                        });
                    } else {
                        Printer.printImage(TransScanResultActivity.this, layoutToBitmap(TransScanResultActivity.this), new Printer.PrintFinish() {
                            @Override
                            public void onSuccess(String data) {
                            }

                            @Override
                            public void onError(int errorCode) {
                                content.clearAnimation();
                                if (ConstantPrinter.STATUS_NO_PAPER == errorCode) {
                                    DialogUtils.showAlertDialogCenter(TransScanResultActivity.this, new DialogUtils.DialogCallback() {
                                        @Override
                                        public void onConfirm() {

                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                                }

                            }
                        });
                    }
                }
            });

        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSoundPool != null) {
            mSoundPool.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sl_receipt.setVisibility(View.GONE);
        reslut_line.setVisibility(View.GONE);
        tv_result_confirm.setVisibility(View.GONE);

        try {
            if ("K1211".equals(Build.MODEL)) {
                int code = ComponentEngine.INSTANCE.getKeyboardManager().startPhysicalKeyboard(new InputCallback() {
                    @Override
                    public void onKey(KeyboardConstant.KeyCode keyCode, KeyboardConstant.KeyAction keyAction) {
//                                    String text = keyCode.getValue()+"=="+((keyAction.getAction() == 0)? "按下":"起");
//                                    Toast.makeText(TransInitActivity.this,text,Toast.LENGTH_SHORT).show();

                        if ((keyAction.getAction() != 0)) {
                            if (TransScanResultActivity.this.getWindow().getDecorView().getVisibility() == View.VISIBLE) {
                                if (KeyboardConstant.KeyCode.BUTTON_ENTER == keyCode) {
                                    TransScanResultActivity.this.finish();

                                }
                            }
                        }

                    }
                });
            }

        } catch (Exception e) {
            finish();
        }
        TRANS_QR = false;
    }

    public void showPic(View view) {
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0, 0,                // toXDelta
                0 - view.getHeight() - 20  // fromYDelta
        );
        animate.setDuration(3500);
        animate.setFillBefore(true);
        view.startAnimation(animate);
    }

    private void playSound() {

// 1. 初始化SoundPool（Android 21+）
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(10); // 最大同时播放数
        builder.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
        mSoundPool = builder.build();

// 2. 加载音效（从Resources加载）
        int soundId = mSoundPool.load(this, R.raw.pay_successful, 0); // 优先级1

// 3. 播放音效
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) { // 加载成功
                    soundPool.play(soundId,  // 音效ID
                            1.0f,  // 左声道音量（0.0-1.0）
                            1.0f,  // 右声道音量
                            0,     // 优先级（0为最低）
                            0,     // 循环次数（-1为无限循环）
                            1.0f); // 播放速率（1.0为正常）
                }
            }
        });

//
    }

    public Bitmap layoutToBitmap(Activity activity) {
        // 小票宽度
        int RECEIPT_WIDTH_PX = 384;
//
//        // 测量 LinearLayout 的宽度
//        view.measure(View.MeasureSpec.makeMeasureSpec(RECEIPT_WIDTH_PX, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//        // 这里多打印 200 的高度可以理解为走纸操作（也可以用 printerManager.addPrintLine(new TextPrintLine(" ", 0, 100)) 的方式）
//        int heightPx = view.getMeasuredHeight() + 200;
//
//        // 布局 LinearLayout
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//
//        // 创建 Bitmap
//        Bitmap bitmap = Bitmap.createBitmap(RECEIPT_WIDTH_PX, heightPx, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//
//        // 绘制 LinearLayout 到 Bitmap
//        view.draw(canvas);


        XmlResourceParser xmlParser = getResources().getLayout(R.layout.receipt_content);

        View view = LayoutInflater.from(activity).inflate(xmlParser, null);

        view.measure(View.MeasureSpec.makeMeasureSpec(RECEIPT_WIDTH_PX, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST));

        view.layout(0, 0,
                view.getMeasuredWidth(),
                view.getMeasuredHeight());


        Bitmap bitmap = Bitmap.createBitmap(RECEIPT_WIDTH_PX, view.getMeasuredHeight() + 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        view.draw(canvas);
        return bitmap;
    }

    private void startLoadingAnimation(ImageView iv) {
        Animation animation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(1000);
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        iv.startAnimation(animation);//开始动画
    }

}
