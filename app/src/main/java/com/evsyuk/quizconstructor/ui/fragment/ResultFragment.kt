package com.evsyuk.quizconstructor.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.evsyuk.quizconstructor.R
import com.evsyuk.quizconstructor.data.model.QuizResult
import com.evsyuk.quizconstructor.databinding.FragmentResultBinding
import kotlin.math.round

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        updateUi(args.quizResult)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        binding.button.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(resultModel: QuizResult) = binding.apply {
        textResultQuestion.text = resources.getString(
            R.string.result_status,
            resultModel.rightAnswer,
            resultModel.totalQuestion
        )
        val result = (resultModel.rightAnswer.toDouble() / resultModel.totalQuestion) * 100
        btnResult.text = "${round(result)}%"
    }
}