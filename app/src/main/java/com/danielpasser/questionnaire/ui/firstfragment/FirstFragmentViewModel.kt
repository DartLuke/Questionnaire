package com.danielpasser.questionnaire.ui.firstfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielpasser.questionnaire.api.Api
import com.danielpasser.questionnaire.model.AnswerBody
import com.danielpasser.questionnaire.model.Question
import com.danielpasser.questionnaire.model.QuestionResponse
import com.danielpasser.questionnaire.utils.DataState
import com.danielpasser.questionnaire.utils.Mapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstFragmentViewModel @Inject constructor(private val retrofit: Api) : ViewModel() {

    val dataStateGetQuestion: LiveData<DataState<List<Question>>> get() = _dataStateGetQuestion
    private var _dataStateGetQuestion = MutableLiveData<DataState<List<Question>>>()

    val dataStatesendAnswer: LiveData<DataState<Boolean>> get() = _dataStateSendAnswer
    private var _dataStateSendAnswer = MutableLiveData<DataState<Boolean>>()
    private var questionsResponse = arrayListOf<QuestionResponse>()

    fun sendAnswer(question: List<Question>) {

        val answers = Mapper.questionsListToAnswersList(question)
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _dataStateSendAnswer.postValue(DataState.Loading)
                retrofit.sendAnswers(AnswerBody(answers ))
                _dataStateSendAnswer.postValue(DataState.Success(true))

            } catch (e: Exception) {
                _dataStateSendAnswer.postValue(DataState.Error(e))
                Log.e("test", e.toString())
            }
        }

    }

    fun getQuestionsAgain() {
        val questions =
            Mapper.questionResponseListToQuestionList(questionsResponse)

        _dataStateGetQuestion.value=DataState.Success(questions)

    }

    fun getQuestions() {
        viewModelScope.launch  (Dispatchers.IO) {
            try {
                _dataStateGetQuestion.postValue(DataState.Loading)
                questionsResponse =
                    retrofit.getQuestions() as ArrayList<QuestionResponse>
                val questions =
                    Mapper.questionResponseListToQuestionList(questionsResponse)

                _dataStateGetQuestion.postValue(DataState.Success(questions))
                Log.e("test", questions.toString())
            } catch (e: Exception) {
                _dataStateGetQuestion.postValue(DataState.Error(e))
                Log.e("test", e.toString())
            }
        }
    }
}
