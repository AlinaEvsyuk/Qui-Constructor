package com.evsyuk.quizconstructor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.evsyuk.quizconstructor.data.QuizDatabaseRepository
import com.evsyuk.quizconstructor.data.model.QuizData
import com.evsyuk.quizconstructor.databinding.FragmentQuizAllBinding
import com.evsyuk.quizconstructor.ui.adapter.QuizAdapter
import com.evsyuk.quizconstructor.ui.viewmodel.QuizListViewModel
import com.evsyuk.quizconstructor.utils.DatabaseResponse
import com.evsyuk.quizconstructor.utils.ViewModelFactory
import com.evsyuk.quizconstructor.utils.listener.QuizClickListener
import com.evsyuk.quizconstructor.utils.shortToast
import kotlinx.coroutines.flow.collectLatest

class QuizAllFragment : Fragment(), QuizClickListener {

    private var _binding: FragmentQuizAllBinding? = null
    private val binding get() = _binding!!

    private val args: QuizAllFragmentArgs by navArgs()
    private val quizAdapter: QuizAdapter by lazy { QuizAdapter(this) }
    private val quizListViewModel: QuizListViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(QuizDatabaseRepository(requireContext())))[QuizListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startShimmer()
        binding.rvRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = quizAdapter
        }

        quizListViewModel.getQuizByCategory(args.categoryId)

        loadData()
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            quizListViewModel.quizList.collectLatest {
                when (it) {
                    is DatabaseResponse.Success -> {
                        quizAdapter.submitList(it.data)
                        stopShimmer()
                    }
                    is DatabaseResponse.Failure -> {
                        it.message?.let { it1 -> shortToast(it1) }
                        stopShimmer()
                    }
                    is DatabaseResponse.Loading -> {
                        startShimmer()
                    }
                }
            }
        }
    }

    override fun quizClickListener(quizData: QuizData) {
        findNavController().navigate(
            QuizAllFragmentDirections.actionNavigationQuizListToPlayQuiz(
                quizData
            )
        )
    }

    private fun startShimmer() {
        binding.apply {
            homeShimmerEffect.visibility = View.VISIBLE
            homeShimmerEffect.startShimmer()
            rvRecyclerView.visibility = View.GONE
        }
    }

    private fun stopShimmer() {
        binding.apply {
            homeShimmerEffect.visibility = View.GONE
            homeShimmerEffect.stopShimmer()
            rvRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
