package com.danielpasser.questionnaire.model

data class Question(
    val id: Int,
    val questionText: String,
    val answerOptions: List<String>?,
    val isLastEditText: Boolean,
    val isAnswerNeeded: Boolean,
    var answerText: String?,
    var selectedItem: Int,
    val isAnswered: Boolean,
    var editText:String
) {
    constructor(questionResponse: QuestionResponse) : this(
        questionResponse.id,
        questionResponse.questionText,
        questionResponse.answerOptions,
        questionResponse.isLastEditText,
        questionResponse.isAnswerNeeded,
        "",
        -1,
        false,
        ""
    )

}
