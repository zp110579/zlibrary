package com.zee.libs.example.ui.activitys

import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zee.activity.BaseZActivity
import com.zee.libs.example.R
import kotlinx.android.synthetic.main.activity_webview.*

/**
 *created by zee on 2021/7/22.
 *
 */
class WebViewActivity : BaseZActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_webview
    }

    override fun initViews() {
        webview.settings.apply {
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            domStorageEnabled = true
            blockNetworkImage = true
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            setSupportZoom(true)
        }
        webview.webViewClient=object: WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }


        webview.loadUrl("https://ibtctest.wn.work/agreement/zh_CN/globalNode.html?uid=371774")
    }

    override fun onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            super.onBackPressed()
        }
    }
}