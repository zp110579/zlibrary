package com.zee.extendobject

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 *created by zee on 2021/5/25.
 *WebView正常的加载
 */
fun WebView.settingsAndLoadUrl(url: String, onTitle: (title: String) -> Unit = {}): WebView {
    settings.apply {
        layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        domStorageEnabled = true
        blockNetworkImage = false
        javaScriptEnabled = true
        cacheMode = WebSettings.LOAD_NO_CACHE
        setSupportZoom(false)
    }


    isHorizontalScrollBarEnabled = false
    setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    requestFocusFromTouch()
    isHorizontalScrollBarEnabled = false

    webChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            if (title != null) {
                onTitle.invoke(title)
            }
        }
    }

    webViewClient = object : WebViewClient() {
        //解决内部跳转的问题
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url);
            return false
        }
    }

    loadUrl(url)
    return this
}