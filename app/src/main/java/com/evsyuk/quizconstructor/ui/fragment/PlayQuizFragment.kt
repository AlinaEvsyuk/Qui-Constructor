package com.evsyuk.quizconstructor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.evsyuk.quizconstructor.R
import com.evsyuk.quizconstructor.data.model.QuizData
import com.evsyuk.quizconstructor.data.model.QuizResult
import com.evsyuk.quizconstructor.databinding.FragmentPlayQuizBinding
import com.evsyuk.quizconstructor.utils.shortToast
import kotlinx.coroutines.launch

class PlayQuizFragment : Fragment() {

    private var _binding: FragmentPlayQuizBinding? = null
    private val binding get() = _binding!!

    private val args: PlayQuizFragmentArgs by navArgs()
    private var currentPosition = 0
    private var rightAnswer = 0
    private var wrongAnswer = 0
    private lateinit var quizData: QuizData
    private var isCheckedAnswer = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().actionBar?.hide()
        _binding = FragmentPlayQuizBinding.inflate(inflater, container, false)
        quizData = args.quizData
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(args.quizData.categoryId){
            HomeFragment.MULTIPLE_ANS_ID -> {
//                mView.answerId2.visibility = View.VISIBLE
            }
            HomeFragment.INPUT_ANS_ID -> {
                binding.radioOption1.visibility = View.GONE
                binding.radioOption2.visibility = View.GONE
                binding.radioOption3.visibility = View.GONE
                binding.radioOption4.visibility = View.GONE
                binding.answerId.visibility = View.VISIBLE
            }
            HomeFragment.CHOICE_ANS_ID -> {
                binding.radioOption3.visibility = View.GONE
                binding.radioOption4.visibility = View.GONE
            }
        }
        updateView()
        binding.btnCheckAnswer.setOnClickListener {
            checkButton()
        }
    }

    private fun checkButton() {
        if (isCheckedAnswer) {
            isCheckedAnswer = false
            if (currentPosition + 1 == args.quizData.totalQuestion) {
                val quizResult = QuizResult(
                    rightAnswer = rightAnswer,
                    wrongAnswer = wrongAnswer,
                    totalQuestion = args.quizData.totalQuestion
                )
                findNavController().navigate(
                    PlayQuizFragmentDirections.actionQuizFragmentToResultFragment(
                        quizResult
                    )
                )
            } else {
                binding.apply {
                    textAnswerStatus.isVisible = false
                    textRightAnswer.isVisible = false
                    btnCheckAnswer.text = resources.getString(R.string.lbl_check_answer)
                    val selectedAnswer =
                        requireActivity().findViewById<RadioButton>(binding.radioGroupOption.checkedRadioButtonId)
                    selectedAnswer.setBackgroundResource(R.drawable.bg_option_unselected)
                    radioGroupOption.clearCheck()
                }
                updateView()
            }
        } else {
            if (binding.radioGroupOption.checkedRadioButtonId == -1) {
                shortToast(resources.getString(R.string.error_choose_answer))
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    checkAnswer(args.quizData)
                }
                isCheckedAnswer = true
            }
        }
    }

    private fun checkAnswer(quizData: QuizData) {
        if (currentPosition + 1 != quizData.totalQuestion) {
            binding.btnCheckAnswer.text = resources.getString(R.string.lbl_next)
            val answer = quizData.questionList[currentPosition].answer
            val selectedAnswer =
                requireActivity().findViewById<RadioButton>(binding.radioGroupOption.checkedRadioButtonId)
            if (answer == selectedAnswer.text || answer == binding.answerId.editText?.text.toString().trim()) {
                rightAnswer++
//                selectedAnswer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryGreen))
                selectedAnswer.setBackgroundResource(R.drawable.bg_right_option)
                binding.textAnswerStatus.isVisible = true
                binding.textAnswerStatus.text = resources.getString(R.string.right)
            } else {
                wrongAnswer++
                selectedAnswer.setBackgroundResource(R.drawable.bg_wrong_option)
                binding.apply {
                    textAnswerStatus.isVisible = true
                    binding.textAnswerStatus.text = resources.getString(R.string.lbl_wrong)
                    textRightAnswer.isVisible = true
                    textRightAnswer.text = resources.getString(R.string.message_right_answer, answer)
                }
            }
        } else {
            binding.btnCheckAnswer.text = resources.getString(R.string.lbl_result)
        }
        currentPosition++
    }

    private fun updateView() {
        loadQuestionAnimation()
        binding.apply {
            textQuizTitle.text = quizData.quizTitle
            textQuestionStatement.text = quizData.questionList[currentPosition].questionStatement
            radioOption1.text = quizData.questionList[currentPosition].option1
            radioOption2.text = quizData.questionList[currentPosition].option2
            radioOption3.text = quizData.questionList[currentPosition].option3
            radioOption4.text = quizData.questionList[currentPosition].option4
        }
    }

    private fun loadQuestionAnimation() = binding.apply {
        radioOption1.animate()
            .alpha(1f)
            .duration = 400L
        radioOption2.animate()
            .alpha(1f)
            .duration = 400L
        radioOption3.animate()
            .alpha(1f)
            .duration = 400L
        radioOption4.animate()
            .alpha(1f)
            .duration = 400L
        btnCheckAnswer.animate()
            .alpha(1f)
            .duration = 400L
        textQuestionStatement.animate()
            .alpha(1f)
            .duration = 400L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}