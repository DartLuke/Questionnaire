package com.danielpasser.questionnaire.model

data class Question(
    val id: Int,
    val questionText: String,
    val answerOptions: List<String>?,
    val isLastEditText: Boolean,
    val isAnswerNeed: Boolean,
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
        questionResponse.isAnswerNeed,
        "",
        -1,
        false,
        ""
    )

}
