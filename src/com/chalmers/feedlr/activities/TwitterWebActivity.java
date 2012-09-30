/**
 * Class description
 * 
 * @author Olle Werme
 */

package com.chalmers.feedlr.activities;

import com.chalmers.feedlr.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TwitterWebActivity extends Activity {
	
	private static final String CALLBACK_URL = "feedlr://twitter";
	
	private Intent intent;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.twitter_webview);
	    
	    intent = getIntent();
	    String url = (String)intent.getExtras().get("URL");
	    WebView webView = (WebView)findViewById(R.id.webview);
	    
	    webView.setWebViewClient( new WebViewClient() {
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            if(url.contains(CALLBACK_URL)) {
	                Uri uri = Uri.parse( url );
	                String oauthVerifier = uri.getQueryParameter( "oauth_verifier" );
	                intent.putExtra( "oauth_verifier", oauthVerifier );
	                setResult( RESULT_OK, intent );
	                finish();
	                return true;
	            }
	            return false;
	        }
	    });
	    webView.loadUrl(url);
	}
}
