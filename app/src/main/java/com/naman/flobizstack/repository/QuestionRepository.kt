package com.naman.news.repository

import com.naman.flobizstack.DBroom.QuestionDatabase
import com.naman.flobizstack.api.RetrofitInstance
import com.naman.flobizstack.models.Item


class QuestionRepository(val db: QuestionDatabase) {

    suspend fun getQuestions() =
        RetrofitInstance.api.getQuestions()


    suspend fun insert(article:Item)=db.getQuestionDao().insert(article)

    suspend fun deleteArticles(article: Item)=db.getQuestionDao().deleteArticle(article)
    fun getItem()=db.getQuestionDao().getItem()

}