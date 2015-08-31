package com.example.fahim.ebookdl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.File;

public class Browser extends Activity {

    private WebView webView;

    final Context myApp = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Bundle bundle = getIntent().getExtras();

        webView = (WebView) findViewById(R.id.webView);

        /* JavaScript must be enabled if you want it to work, obviously */
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);

        webView.loadUrl(bundle.getString("URL"));

        webView.setWebViewClient(new WVClient(bundle.getString("NAME")));
    }

    private class WVClient extends WebViewClient implements DownloadListener {

        private String filename;

        public WVClient(String filename){
            this.filename = filename;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView v, String u) {
            v.loadUrl(u);
            v.setDownloadListener(this);
            return true;
        }

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.i("PDF ", "Download: " + url);
            Log.i("PDF ", "Length: " + contentLength);

            String servicestring = Context.DOWNLOAD_SERVICE;
            DownloadManager downloadmanager;
            downloadmanager = (DownloadManager) getSystemService(servicestring);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setDestinationInExternalPublicDir("/it-ebooks", filename+".pdf");
            Long reference = downloadmanager.enqueue(request);
        }
    }

    private void startWebView(String url) {


    }


    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

