package com.danielpasser.questionnaire.model

import android.widget.EditText
import android.widget.RadioGroup
import java.text.ChoiceFormat

data class QuestionResponse(
    val id: Int,
    val questionText: String,
    val answerOptions: List<String>,
    val isLastEditText: Boolean,
    val isAnswerNeed:Boolean,
    )




