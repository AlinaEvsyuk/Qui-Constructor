package com.evsyuk.quizconstructor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evsyuk.quizconstructor.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun navigateToQuizCategory(categoryId: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToQuizList(
                categoryId
            )
        )
    }

    private fun setupClickListeners() {
        binding.apply {
            btnCreateQuiz.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToCreateQuiz())
            }

            categorySingleAns.setOnClickListener {
                navigateToQuizCategory(SINGLE_ANS_ID)
            }

            categoryMultipleAns.setOnClickListener {
                navigateToQuizCategory(MULTIPLE_ANS_ID)
            }

            categoryInputAns.setOnClickListener {
                navigateToQuizCategory(INPUT_ANS_ID)
            }

            categoryChoiceAns.setOnClickListener {
                navigateToQuizCategory(CHOICE_ANS_ID)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SINGLE_ANS_ID = "category_single_ans"
        const val MULTIPLE_ANS_ID = "category_multiple_ans"
        const val INPUT_ANS_ID = "category_input_ans"
        const val CHOICE_ANS_ID = "category_choice_ans"
    }

}