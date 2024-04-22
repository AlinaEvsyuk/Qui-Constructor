package com.evsyuk.quizconstructor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.evsyuk.quizconstructor.data.model.QuizResultModel
import com.evsyuk.quizconstructor.databinding.FragmentResult1Binding
import com.evsyuk.quizconstructor.databinding.FragmentResultBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class ResultFragment1 : Fragment() {

    private var _binding: FragmentResult1Binding? = null
    private val binding get() = _binding!!
    private val args: ResultFragmentArgs by navArgs()
    private var mScore: Int = 0
    private var mWrongAns: Int = 0
    private var mQuestionsCount: Int = 0
    private lateinit var mCategoryId: String
    private lateinit var mResultList: ArrayList<QuizResultModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResult1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initFunctionality()
        initListener()

    }


    private fun initView() {

        binding.rvcontentScore.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initFunctionality() {
        binding.txtScore.text = args.quizResult.rightAnswer.toString()
        binding.txtWrong.text = args.quizResult.wrongAnswer.toString()
        val actualScore =
            (args.quizResult.rightAnswer.toFloat() / (args.quizResult.rightAnswer + args.quizResult.wrongAnswer).toFloat()) * 10
        when (kotlin.math.round(actualScore)) {
            10f, 9f, 8f -> binding.greetingText.text = ("Великолепно! Отличный результат!")
            7f, 6f, 5f -> binding.greetingText.text = ("Xopoшо. Hо могло быть еше лучше.")
            else -> binding.greetingText.text = ("Плохо. Подготовьтесь и пробуйте снова.")
        }
        showPieChart()
//        mAdapter = ResultAdapter(mContext, mActivity, mResultList)
//        mRecyclerResult.adapter = mAdapter
    }

    private fun initListener() {
        binding.btnPlayAgain.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showPieChart() {
        val mPieChart = binding.piechart
//        mPieChart.usePercentValues = true
        mPieChart.isDrawHoleEnabled = true
        mPieChart.transparentCircleRadius = 65f
        mPieChart.holeRadius = 65f
//        mPieChart.description = "Результаты"
        mPieChart.animateXY(2000, 2000)
        val yvalues = mutableListOf<Entry>()
        yvalues.add(Entry(mScore.toFloat(), 0f))
        yvalues.add(Entry(mWrongAns.toFloat(), 2f))
//        val dataSet = PieDataSet(yvalues, "")
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS)

        val xVals = ArrayList<String>()
        xVals.add("Beрнo")
        xVals.add("неверно")

//        val data = PieData(xVals, dataSet)
//        data.setValueFormatter(PercentFormatter())
//        mpieChart.data = data
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
