package com.naman.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naman.flobizstack.models.Item
import com.naman.flobizstack.models.StackQuestions

import com.naman.news.repository.QuestionRepository
import com.naman.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class QuestionViewModel(val questionRepository: QuestionRepository):ViewModel(){

     val questions:MutableLiveData<Resource<StackQuestions>> = MutableLiveData()
    var questionsPage =1
    var questionsResponse:StackQuestions?=null

init {
    getQuestions()
}
    fun getQuestions()=viewModelScope.launch {

        questions.postValue(Resource.Loading())
        val response=questionRepository.getQuestions()
        questions.postValue(handleQuesionsResponse(response))

    }

    private fun handleQuesionsResponse(response: Response<StackQuestions>):Resource<StackQuestions>{
        if (response.isSuccessful){
            response.body()?.let {

                questionsPage++;
                if (questionsResponse==null){
                    questionsResponse=it
                }else{
                    val oArticles=questionsResponse?.items
                    val nArticles=it.items
                    oArticles?.addAll(nArticles)
                }
                return Resource.Success(questionsResponse?:it)
            }
        }
        return Resource.Error(response.message())
    }

    fun insert(item: Item)=viewModelScope.launch {
        questionRepository.insert(item)
    }

    fun delete(item: Item)=viewModelScope.launch {
        questionRepository.deleteArticles(item)
    }
     fun getItem()=
        questionRepository.getItem()

}