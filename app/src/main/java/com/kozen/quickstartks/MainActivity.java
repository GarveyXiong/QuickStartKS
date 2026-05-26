package com.kozen.quickstartks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kozen.financial.engine.FinancialEngine;
import com.kozen.financial.engine.InitListener;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static WeakReference<MainActivity> sInstanceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstanceRef = new WeakReference<>(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sInstanceRef != null) {
            sInstanceRef.clear();
            sInstanceRef = null;
        }
    }

    public static MainActivity getInstance(){
        return sInstanceRef != null ? sInstanceRef.get() : null;
    }


}