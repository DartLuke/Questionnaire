package com.danielpasser.questionnaire.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danielpasser.questionnaire.R
import com.danielpasser.questionnaire.model.Question

class QuestionsAdapter() : RecyclerView.Adapter<MyViewHolder>(),
    MyViewHolder.OnDataChangedListener {

    private val questions: ArrayList<Question> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return when (viewType) {
            R.layout.item_view_radio_group -> MyViewHolder.RadioButtonViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false), this
            )

            R.layout.item_view_edit_text -> MyViewHolder.EditTextViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false), this
            )

            R.layout.item_view_title -> MyViewHolder.TitleViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )

            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder.TitleViewHolder -> holder.bind()
            is MyViewHolder.RadioButtonViewHolder -> holder.bind(questions[position - 1])
            is MyViewHolder.EditTextViewHolder -> holder.bind(questions[position - 1])
        }

    }

    override fun getItemCount(): Int = questions.size + 1

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return R.layout.item_view_title

        if (questions[position - 1].answerOptions == null || questions[position - 1].answerOptions?.isEmpty() == true) return R.layout.item_view_edit_text
        else return R.layout.item_view_radio_group

    }

    fun updateData(questions: List<Question>) {
        this.questions.apply {
            clear()
            addAll(questions)
        }
        notifyDataSetChanged()
    }

    override fun onDataChanged(question: Question, position: Int) {
       Log.v("Test", question.toString())
        questions[position - 1] = question
    }


    fun getQuestions(): List<Question>? {
        return if (allQuestionsAnswered())
            questions
        else null
    }

    private fun allQuestionsAnswered(): Boolean {
        for (question in questions)
            if (question.isAnswerNeeded && question.answerText.isNullOrEmpty()) return false
        return true
    }

}