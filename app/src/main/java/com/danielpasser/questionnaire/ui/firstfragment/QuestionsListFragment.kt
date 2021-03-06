package com.danielpasser.questionnaire.ui.firstfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.danielpasser.questionnaire.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielpasser.questionnaire.model.Question
import com.danielpasser.questionnaire.ui.adapter.QuestionsAdapter
import com.danielpasser.questionnaire.ui.decorator.ItemDecorator
import com.danielpasser.questionnaire.utils.DataState


@AndroidEntryPoint
class QuestionsListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val questionsAdapter = QuestionsAdapter()
    private val viewModel: QuestionsListFragmentViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_question_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(view)
        setupAdapter(view)
        setupObservers()
        viewModel.getQuestions()
    }

    private fun setupObservers() {
        viewModel.dataStatesendAnswer.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    Toast.makeText(context, R.string.answers_were_sent, Toast.LENGTH_SHORT).show()
                    viewModel.getQuestionsAgain()
                    showRecycleView(true)
                    showProgressBar(false)
                }
                is DataState.Error -> {
                    showProgressBar(false)
                    displayError(dataState.exception?.message)
                }
                is DataState.Loading -> {
                    showProgressBar(true)
                }
            }
        })

        viewModel.dataStateGetQuestion.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<List<Question>> -> {
                    changeListViewData(dataState.data)
                    showRecycleView(true)
                    showProgressBar(false)

                }
                is DataState.Error -> {
                    showProgressBar(false)
                    showRecycleView(false)
                    displayError(dataState.exception?.message)
                }
                is DataState.Loading -> {
                    showRecycleView(false)
                    showProgressBar(true)
                }
            }
        })
    }

    private fun displayError(message: String?) {
        if (message != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showRecycleView(isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setupUI(view: View) {
        progressBar = view.findViewById(R.id.progressBar)

        view.findViewById<Button>(R.id.button)
            .setOnClickListener {
                val questions = questionsAdapter.getQuestions()
                if (questions == null)
                    Toast.makeText(activity, R.string.not_all_answered, Toast.LENGTH_LONG).show()
                else
                    viewModel.sendAnswer(questions)
            }

    }

    private fun setupAdapter(view: View) {
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_movies)

        recyclerView.apply {

            layoutManager = LinearLayoutManager(view.context)
            addItemDecoration(ItemDecorator(50))
            adapter = questionsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.getQuestions()
                    }
                   // super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
    }

    private fun changeListViewData(questions: List<Question>) {
        questionsAdapter.updateData(questions)
    }

}