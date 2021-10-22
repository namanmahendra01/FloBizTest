package com.naman.flobizstack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.core.app.Person.fromBundle
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.naman.flobizstack.databinding.ActivityQuestionWebViewBinding
import com.naman.news.ui.QuestionViewModel

class QuestionWebView : AppCompatActivity() {

    lateinit var viewModel:QuestionViewModel
    lateinit var binding: ActivityQuestionWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityQuestionWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var i :Intent=intent
        val url = i.getStringExtra("url")


        binding.webView .apply {
            webViewClient = WebViewClient()
            url?.let { loadUrl(it) }
        }
    }
}