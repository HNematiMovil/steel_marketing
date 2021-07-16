package ir.hadinemati.steelmarketing.Views;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ir.hadinemati.steelmarketing.R;

public class UploadWebView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_webupload);
        WebView webView = new WebView(this);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl("https://marketing.steel-man.ir/upload");

        setContentView(webView);

    }
}
