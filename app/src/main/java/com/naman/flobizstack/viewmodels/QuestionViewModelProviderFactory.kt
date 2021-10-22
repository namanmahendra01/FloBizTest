package com.naman.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.naman.news.repository.QuestionRepository

class QuestionViewModelProviderFactory(val questionRepository:QuestionRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionViewModel(questionRepository = questionRepository)as T
    }
}