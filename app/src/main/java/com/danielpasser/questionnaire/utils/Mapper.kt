package com.danielpasser.questionnaire.utils

import com.danielpasser.questionnaire.model.Answer
import com.danielpasser.questionnaire.model.QuestionResponse
import com.danielpasser.questionnaire.model.Question

object Mapper {

    fun questionToAnswer(question: Question): Answer {
        return Answer(question.id, question.questionText,question.answerText)
    }

    fun questionsListToAnswersList(questions: List<Question>): List<Answer> {
        return questions.map { questionToAnswer(it) }
    }
fun questionResponseListToQuestionList(questions: List<QuestionResponse>): List<Question>
{
    return questions.map { Question(it) }
}

}