package com.danielpasser.questionnaire.api

import com.danielpasser.questionnaire.model.Answer
import com.danielpasser.questionnaire.model.QuestionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("questions")
    suspend fun getQuestions() : List<QuestionResponse>

    @POST("answers")
    suspend fun sendAnswers(
       
        @Body answers: List<Answer>
    )
    
}