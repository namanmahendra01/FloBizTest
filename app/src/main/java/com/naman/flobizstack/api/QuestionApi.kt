package com.naman.flobizstack.api

import com.naman.flobizstack.models.Item
import com.naman.flobizstack.models.StackQuestions
import com.naman.news.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionApi {

    @GET("/2.2/questions")
    suspend fun getQuestions( @Query("order")
                                  countryCode:String="desc",
                              @Query("sort")
                                  sort:String="activity",
                              @Query("site")
                                  site:String="stackoverflow",
                             ):Response<StackQuestions>

}