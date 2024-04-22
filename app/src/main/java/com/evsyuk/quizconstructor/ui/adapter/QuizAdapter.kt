package com.evsyuk.quizconstructor.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.evsyuk.quizconstructor.R
import com.evsyuk.quizconstructor.data.model.QuizData
import com.evsyuk.quizconstructor.databinding.SingleQuizBinding
import com.evsyuk.quizconstructor.utils.listener.QuizClickListener

class QuizAdapter(
    private val quizClickListener: QuizClickListener
) :
    ListAdapter<QuizData, QuizAdapter.QuizListViewHolder>(CategoryItemDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizListViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = SingleQuizBinding.inflate(view, parent, false)
        return QuizListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, quizClickListener)
        }
    }

    inner class QuizListViewHolder(
        private val binding: SingleQuizBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quizData: QuizData, quizClickListener: QuizClickListener) = with(binding) {
            val context = root.context
            txtQuizTitle.text = quizData.quizTitle
            txtTotalQuiz.text = context.getString(R.string.total_question, quizData.totalQuestion.toString())
            textQuizCategory.text = context.getString(R.string.category_name, quizData.categoryName)
            startQuiz.setOnClickListener {
                quizClickListener.quizClickListener(quizData)
            }
        }
    }

    companion object {
        val CategoryItemDiffUtil = object : DiffUtil.ItemCallback<QuizData>() {
            override fun areItemsTheSame(
                oldItem: QuizData,
                newItem: QuizData
            ): Boolean {
                return oldItem.quizTitle == newItem.quizTitle
            }

            override fun areContentsTheSame(
                oldItem: QuizData,
                newItem: QuizData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
