package com.evsyuk.quizconstructor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evsyuk.quizconstructor.R
import com.evsyuk.quizconstructor.data.QuizDatabaseRepository
import com.evsyuk.quizconstructor.data.model.CategoryItem
import com.evsyuk.quizconstructor.data.model.QuizData
import com.evsyuk.quizconstructor.data.model.CreateQuestionData
import com.evsyuk.quizconstructor.databinding.DialogQuestionAddBinding
import com.evsyuk.quizconstructor.databinding.FragmentCreateQuizBinding
import com.evsyuk.quizconstructor.ui.adapter.QuestionAdapter
import com.evsyuk.quizconstructor.ui.fragment.HomeFragment.Companion.CHOICE_ANS_ID
import com.evsyuk.quizconstructor.ui.fragment.HomeFragment.Companion.INPUT_ANS_ID
import com.evsyuk.quizconstructor.ui.fragment.HomeFragment.Companion.MULTIPLE_ANS_ID
import com.evsyuk.quizconstructor.ui.fragment.HomeFragment.Companion.SINGLE_ANS_ID
import com.evsyuk.quizconstructor.ui.viewmodel.CreateQuizViewModel
import com.evsyuk.quizconstructor.utils.DatabaseResponse
import com.evsyuk.quizconstructor.utils.ViewModelFactory
import com.evsyuk.quizconstructor.utils.shortToast
import com.evsyuk.quizconstructor.utils.snack
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest

class CreateQuizFragment : Fragment() {

    private var _binding: FragmentCreateQuizBinding? = null
    private val binding get() = _binding!!

    private val createQuizViewModel: CreateQuizViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(QuizDatabaseRepository(requireContext())))[CreateQuizViewModel::class.java]
    }
    private val createQuestionAdapter: QuestionAdapter by lazy {
        QuestionAdapter(selectedCategory.categoryId) {
            createQuizViewModel.removeQuestion(it)
        }
    }
    private var categoryList = arrayListOf<CategoryItem>()
    private lateinit var selectedCategory: CategoryItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateQuizBinding.inflate(inflater, container, false)
        initCategory()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addViewForProgress()
//        initRecyclerView()
        if (this::selectedCategory.isInitialized) {
            observeList()
        }
        binding.fabAddQuestion.setOnClickListener {
            if (this::selectedCategory.isInitialized) {
                showDialog(selectedCategory.categoryId)
            }
        }
        initUploadClickListener()
    }

    private fun observeList() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        createQuizViewModel.questionList.collectLatest {
            createQuestionAdapter.submitList(it)
        }
    }

    private fun addViewForProgress() = binding.apply {
        btnSaveQuiz.setDisableViews(
            listOf(
                inputQuizTitle,
                categoryQuiz,
                fabAddQuestion,
                rvQuestionList
            )
        )
    }

    private fun initCategory() {
        categoryList = arrayListOf(
            CategoryItem(SINGLE_ANS_ID, resources.getString(R.string.category_single_answer)),
            CategoryItem(MULTIPLE_ANS_ID, resources.getString(R.string.category_multiple_answers)),
            CategoryItem(INPUT_ANS_ID, resources.getString(R.string.category_input_answer)),
            CategoryItem(CHOICE_ANS_ID, resources.getString(R.string.category_choice_answer)),
        )
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1, categoryList
        )

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        (binding.categoryQuiz.editText as? AutoCompleteTextView)?.setAdapter(categoryAdapter)
        (binding.categoryQuiz.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, _, position, _ ->
            val categoryItem = parent.getItemAtPosition(position) as CategoryItem
            selectedCategory = categoryItem
            (binding.categoryQuiz).isEnabled = false
            initRecyclerView()
            observeList()
        }
    }

    private fun initRecyclerView() = binding.apply {
        createQuestionAdapter.submitList(emptyList())
        rvQuestionList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = createQuestionAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun showDialog(categoryId: String) {
        val mView = DialogQuestionAddBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .create()
        dialog.setView(mView.root)
        dialog.setTitle(requireContext().getString(R.string.lbl_create_quiz))
        dialog.show()
        when (categoryId) {
            MULTIPLE_ANS_ID -> {
                mView.answerId2.visibility = View.VISIBLE
            }
            INPUT_ANS_ID -> {
                mView.edOption1.visibility = View.GONE
                mView.edOption2.visibility = View.GONE
                mView.edOption3.visibility = View.GONE
                mView.edOption4.visibility = View.GONE
            }
            CHOICE_ANS_ID -> {
                mView.edOption3.visibility = View.GONE
                mView.edOption4.visibility = View.GONE
            }
        }
        mView.textAdd.setOnClickListener {
            val questionStatement = mView.edQuetionStatement.editText?.text.toString().trim()
            val option1 = mView.edOption1.editText?.text.toString().trim()
            val option2 = mView.edOption2.editText?.text.toString().trim()
            val option3 = mView.edOption3.editText?.text.toString().trim()
            val option4 = mView.edOption4.editText?.text.toString().trim()
            val answer = mView.answerId.editText?.text.toString().trim()
            val answer2 = mView.answerId2.editText?.text.toString().trim()

            when {
                questionStatement.isEmpty() -> {
                    mView.edQuetionStatement.error =
                        resources.getString(R.string.error_enter_question_statement)
                }
                option1.isEmpty() -> {
                    mView.edOption1.error = resources.getString(R.string.error_option, 1)
                }
                checkEmpty(option2, mView.edOption2) -> {
                    mView.edOption2.error = resources.getString(R.string.error_option, 2)
                }
                checkEmpty(option3, mView.edOption3) -> {
                    mView.edOption3.error = resources.getString(R.string.error_option, 3)
                }
                checkEmpty(option4, mView.edOption4) -> {
                    mView.edOption4.error = resources.getString(R.string.error_option, 4)
                }
                checkEmpty(answer, mView.answerId) -> {
                    mView.answerId.error = resources.getString(R.string.error_write_answer)
                }
                checkEmpty(answer2, mView.answerId2) -> {
                    mView.answerId2.error = resources.getString(R.string.error_write_second_answer)
                }
                !checkOption(option1, option2, option3, option4, answer) -> {
                    mView.answerId.error = resources.getString(R.string.error_answer_not_match)
                }
                categoryId == MULTIPLE_ANS_ID && !checkSecondOption(option1, option2, option3, option4, answer, answer2) -> {
                    mView.answerId2.error = resources.getString(R.string.error_answer_not_match)
                }
                else -> {
                    saveDataToList(
                        mView.edQuetionStatement.editText!!.text.toString(),
                        mView.edOption1.editText!!.text.toString(),
                        mView.edOption2.editText!!.text.toString(),
                        mView.edOption3.editText!!.text.toString(),
                        mView.edOption4.editText!!.text.toString(),
                        mView.answerId.editText!!.text.toString(),
                        mView.answerId2.editText!!.text.toString()
                    )
                    dialog.dismiss()
                }
            }
        }
        mView.textCancel.setOnClickListener {
            dialog.cancel()
        }
    }

    private fun checkEmpty(option: String, textInputLayout: TextInputLayout): Boolean {
        return (textInputLayout.visibility == View.VISIBLE) && option.isEmpty()
    }

    private fun checkOption(
        option1: String,
        option2: String,
        option3: String,
        option4: String,
        answer: String,
    ): Boolean {
        return when {
            answer.equals(option1, false) -> true
            answer.equals(option2, false) -> true
            answer.equals(option3, false) -> true
            answer.equals(option4, false) -> true
            else -> false
        }
    }

    private fun checkSecondOption(
        option1: String,
        option2: String,
        option3: String,
        option4: String,
        answer: String,
        answer2: String
    ): Boolean {
        return when {
            answer2.equals(option1, false) && !answer2.equals(answer, false) -> true
            answer2.equals(option2, false) && !answer2.equals(answer, false) -> true
            answer2.equals(option3, false) && !answer2.equals(answer, false) -> true
            answer2.equals(option4, false) && !answer2.equals(answer, false) -> true
            else -> false
        }
    }

    private fun initUploadClickListener() = binding.apply {
        btnSaveQuiz.setOnClickListener {
            if (validateInputs()) {
                uploadQuiz()
                btnSaveQuiz.activate()
            }
        }
    }

    private fun validateInputs(): Boolean {
        return when {
            createQuestionAdapter.itemCount < 5 -> {
                shortToast(resources.getString(R.string.error_add_min_question))
                false
            }
            binding.inputQuizTitle.editText?.text.isNullOrEmpty() -> {
                binding.inputQuizTitle.editText?.error = resources.getString(R.string.error_enter_quiz_title)
                return false
            }
            !this::selectedCategory.isInitialized -> {
                binding.categoryQuiz.editText?.error = resources.getString(R.string.error_select_category)
                return false
            }
            else -> true
        }
    }

    private fun uploadQuiz() {
        val quizData = QuizData(
            quizTitle = binding.inputQuizTitle.editText!!.text.toString(),
            questionList = createQuestionAdapter.currentList,
            categoryId = selectedCategory.categoryId,
            categoryName = selectedCategory.categoryName,
            totalQuestion = createQuestionAdapter.itemCount
        )
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            createQuizViewModel.uploadQuiz(quizData)
            createQuizViewModel.uploadResponse.collectLatest { databaseResponse ->
                when (databaseResponse) {
                    is DatabaseResponse.Success -> {
                        binding.btnSaveQuiz.finished()
                        binding.root.snack(resources.getString(R.string.message_quiz_created)) {}
                        findNavController().navigateUp()
                    }
                    is DatabaseResponse.Failure -> {
                        binding.root.snack("${databaseResponse.message}") {}
                        binding.btnSaveQuiz.reset()
                    }
                    is DatabaseResponse.Loading -> {
                        binding.btnSaveQuiz.activate()
                    }
                }
            }
        }
    }

    private fun saveDataToList(
        questionStatement: String,
        op1: String,
        op2: String,
        op3: String,
        op4: String,
        answer: String,
        answer2: String,
    ) {
        val createQuestionData = CreateQuestionData(
            questionStatement = questionStatement,
            option1 = op1,
            option2 = op2,
            option3 = op3,
            option4 = op4,
            answer = answer,
            answer2 = answer2
        )
        createQuizViewModel.addQuestion(createQuestionData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}