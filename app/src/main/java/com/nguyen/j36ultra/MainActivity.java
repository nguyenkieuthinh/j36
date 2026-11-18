package com.nguyen.j36ultra;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private WebView webView;
    private ImageView cursorView;

    private float cursorX = 320f;
    private float cursorY = 240f;
    private final float STEP = 25f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        webView = (WebView) findViewById(R.id.webView);
        cursorView = (ImageView) findViewById(R.id.cursorView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com");

        webView.requestFocus();

        webView.post(new Runnable() {
            @Override
            public void run() {
                cursorX = webView.getWidth() / 2f;
                cursorY = webView.getHeight() / 2f;
                updateCursorPosition();
            }
        });
    }

    private void updateCursorPosition() {
        cursorView.setX(cursorX);
        cursorView.setY(cursorY);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();

            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    cursorY = Math.max(0, cursorY - STEP);
                    if (cursorY == 0) webView.scrollBy(0, -60);
                    updateCursorPosition();
                    return true;

                case KeyEvent.KEYCODE_DPAD_DOWN:
                    cursorY = Math.min(webView.getHeight() - 24, cursorY + STEP);
                    if (cursorY >= webView.getHeight() - 30) webView.scrollBy(0, 60);
                    updateCursorPosition();
                    return true;

                case KeyEvent.KEYCODE_DPAD_LEFT:
                    cursorX = Math.max(0, cursorX - STEP);
                    updateCursorPosition();
                    return true;

                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    cursorX = Math.min(webView.getWidth() - 24, cursorX + STEP);
                    updateCursorPosition();
                    return true;

                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_BUTTON_A:
                    simulateClick(cursorX, cursorY);
                    return true;

                case KeyEvent.KEYCODE_BUTTON_B:
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void simulateClick(float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 50;

        MotionEvent downEvent = MotionEvent.obtain(
                downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        MotionEvent upEvent = MotionEvent.obtain(
                downTime, eventTime + 50, MotionEvent.ACTION_UP, x, y, 0);

        webView.dispatchTouchEvent(downEvent);
        webView.dispatchTouchEvent(upEvent);

        downEvent.recycle();
        upEvent.recycle();
    }
}

