package com.example.agenda;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;

public class SuperWebView extends WebView {
    public SuperWebView(Context context) {
        super(context);
    }

    public SuperWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility != View.GONE) super.onWindowVisibilityChanged(View.VISIBLE);
    }
}
