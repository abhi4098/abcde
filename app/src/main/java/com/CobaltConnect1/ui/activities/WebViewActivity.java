package com.CobaltConnect1.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.CobaltConnect1.R;
import com.CobaltConnect1.utils.Config;

import java.util.Set;

/**
 * Created by Abhinandan on 1/3/18.
 */

public class WebViewActivity extends Activity {

    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_webview);

        // The URL that will fetch the Access Token, Merchant ID, and Employee ID
        String url = "https://sandbox.dev.clover.com/oauth/authorize" +
                "?client_id=" + Config.APP_ID +
                "&response_type=token" +
                "&redirect_uri=" + Config.APP_DOMAIN;


      //  String url = "http://app.cobaltconnect.net/user/oauth.php?do=cloveroauth&token=43821";
        // Creates the WebView
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Parses the fetched URL
                String accessTokenFragment = "#access_token=";
                String merchantIdFragment = "&merchant_id=";
                String employeeIdFragment = "&employee_id=";

                int accessTokenStart = url.indexOf(accessTokenFragment);
                int merchantIdStart = url.indexOf(merchantIdFragment);
                int employeeIdStart = url.indexOf(employeeIdFragment);

                if (accessTokenStart > -1) {
                    Uri uri = Uri.parse(url);
                    String server = uri.getAuthority();
                    String path = uri.getPath();
                    String protocol = uri.getScheme();
                    Set<String> args = uri.getQueryParameterNames();
                    String merchantId = uri.getQueryParameter("merchant_id");
                    String employeeId = uri.getQueryParameter("employee_id");
                    //String accessToken = uri.getQueryParameter("access_token");
                    String accessToken = url.substring(accessTokenStart + accessTokenFragment.length(), url.length());
                    Log.e("abhi", "onPageStarted: "+url );
                    Log.e("abhi", "onPageStarted: " +accessToken);
                    Log.e("abhi", "onPageStarted: " + merchantId);
                    Log.e("abhi", "onPageStarted: " + employeeId);

                    // Sends the info back to the MainActivity
                    Intent output = new Intent();
                    output.putExtra(SignInActivity.ACCESS_TOKEN_KEY, accessToken);
                    output.putExtra(SignInActivity.MERCHANT_ID_KEY, merchantId);
                    output.putExtra(SignInActivity.EMPLOYEE_ID_KEY, employeeId);
                    setResult(RESULT_OK, output);
                    finish();
                }
            }
        });
        // Loads the WebView
        webView.loadUrl(url);
    }
}
