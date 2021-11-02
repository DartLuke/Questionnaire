package com.danielpasser.questionnaire.ui.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.danielpasser.questionnaire.R
import com.danielpasser.questionnaire.model.Question
import com.danielpasser.questionnaire.utils.ReflectionTextWatcher


sealed class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    class RadioButtonViewHolder(
        item: View,
        private val onDataChangedListener: OnDataChangedListener
    ) : MyViewHolder(item) {

        private val radioGroup: RadioGroup by lazy { itemView.findViewById(R.id.radio_group) }
        private fun setupRadioGroup(question: Question) {
            //clean radio group
            radioGroup.setOnCheckedChangeListener(null)
            radioGroup.removeAllViews()
            if (radioGroup.checkedRadioButtonId != null) radioGroup.clearCheck()

            //fill radio group
            if (question.answerOptions != null) {
                for (answer in question.answerOptions) {
                    var rdbtn = RadioButton(itemView.context)
                    rdbtn.id = View.generateViewId()
                    rdbtn.text = answer
                    radioGroup.addView(rdbtn)
                }
            }
            //set selected item
            if (question.selectedItem != -1)
                radioGroup.check(radioGroup.children.elementAt(question.selectedItem).id)

            radioGroup?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = itemView.findViewById(checkedId)

                if (question.isLastEditText && radioGroup.indexOfChild(radio)
                    == radioGroup.childCount - 1) question.answerText=question.editText

//                if (!question.isLastEditText || question.isLastEditText && radioGroup.indexOfChild(
//                        radio
//                    ) != radioGroup.childCount - 1
//                )
                   else question.answerText = radio.text.toString()



                question.selectedItem = radioGroup.indexOfChild(radio)
                onDataChangedListener.onDataChanged(question, layoutPosition)
            })


        }



        private fun setupEditText(question: Question) {
            val editText = itemView.findViewById<EditText>(R.id.edit_text)
            if (question.isLastEditText) editText.visibility =
                View.VISIBLE
            else {
                editText.visibility = View.GONE
                return
            }

            ReflectionTextWatcher.removeAll(editText)
            editText.text.clear()

            editText.setText(question.editText)

            editText.addTextChangedListener {
                question.answerText = editText.text.toString()
                question.editText = editText.text.toString()
                onDataChangedListener.onDataChanged(question, layoutPosition)
                radioGroup.check(radioGroup.children.elementAt(radioGroup.childCount - 1).id)
            }
        }

        fun bind(question: Question) {
            setupTitle(question)
            setupRadioGroup(question)
            setupEditText(question)
        }
    }

    class EditTextViewHolder(
        item: View,
        private val onDataChangedListener: OnDataChangedListener
    ) : MyViewHolder(item) {
        fun bind(question: Question) {
            itemView.findViewById<TextView>(R.id.text_view_title).text = question.questionText


            val editText = itemView.findViewById<EditText>(R.id.edit_text)
            editText.text.clear()

            ReflectionTextWatcher.removeAll(editText)

            editText.setText(question.editText)

            editText.addTextChangedListener {
                question.answerText = editText.text.toString()
                question.editText = editText.text.toString()
                onDataChangedListener.onDataChanged(question, layoutPosition)
            }
        }
    }

    class TitleViewHolder(
        item: View
    ) : MyViewHolder(item) {
        fun bind() {
        }
    }

    interface OnDataChangedListener {
        fun onDataChanged(question: Question, position: Int)
    }

    internal fun setupTitle(question: Question) {
        val title = itemView.findViewById<TextView>(R.id.text_view_title)
        title.text = question.questionText

        if (question.isAnswerNeed) {
            val c = SpannableString("*")
            c.setSpan(
                ForegroundColorSpan(Color.RED),
                0,
                c.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            title.append(c)
        }
    }

}
