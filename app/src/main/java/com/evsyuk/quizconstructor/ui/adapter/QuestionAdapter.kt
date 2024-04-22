package com.evsyuk.quizconstructor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.evsyuk.quizconstructor.R
import com.evsyuk.quizconstructor.data.model.CreateQuestionData
import com.evsyuk.quizconstructor.databinding.SingleCreateQuestionBinding
import com.evsyuk.quizconstructor.ui.fragment.HomeFragment

class QuestionAdapter(
    private val categoryId: String,
    private val onDelete: (Int) -> Unit = {}
) :
    ListAdapter<CreateQuestionData, QuestionAdapter.CreateQuestionViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateQuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = SingleCreateQuestionBinding.inflate(view, parent, false)
        return CreateQuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreateQuestionViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CreateQuestionViewHolder(
        private val binding: SingleCreateQuestionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgDeleteItem.setOnClickListener {
                onDelete(adapterPosition)
            }
        }

        fun bind(questionData: CreateQuestionData) = binding.apply {
            val context = root.context
            textQuestionIndex.text = (questionData.questionIndex + 1).toString()
            textQuestionStatement.text = questionData.questionStatement
            textOption1.text = context.getString(R.string.lbl_option, 1, questionData.option1)
            textOption2.text = context.getString(R.string.lbl_option, 2, questionData.option2)
            textOption3.text = context.getString(R.string.lbl_option, 3, questionData.option3)
            textOption4.text = context.getString(R.string.lbl_option, 4, questionData.option4)
            textAnswer.text = context.getString(R.string.lbl_answer, questionData.answer)
            textSecondAnswer.text = context.getString(R.string.lbl_second_answer, questionData.answer2)

            when (categoryId) {
                HomeFragment.MULTIPLE_ANS_ID -> {
                    textSecondAnswer.visibility = View.VISIBLE
                }
                HomeFragment.INPUT_ANS_ID -> {
                    textOption2.visibility = View.GONE
                    textOption3.visibility = View.GONE
                    textOption4.visibility = View.GONE
                }
                HomeFragment.CHOICE_ANS_ID -> {
                    textOption3.visibility = View.GONE
                    textOption4.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CreateQuestionData>() {
            override fun areContentsTheSame(
                oldItem: CreateQuestionData,
                newItem: CreateQuestionData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: CreateQuestionData,
                newItem: CreateQuestionData
            ): Boolean {
                return oldItem.questionIndex == newItem.questionIndex
            }
        }
    }
}
