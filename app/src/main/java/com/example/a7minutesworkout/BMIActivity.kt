package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }

    private var binding : ActivityBmiBinding ?= null
    private var currentVisibleView : String = METRIC_UNITS_VIEW
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar != null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Calculate BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener{ _ , checkedId : Int ->

            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else {
             makeVisibleUSUnitsView()   }
        }

        binding?.btnCalculate?.setOnClickListener {
            calculateUnits()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.textInputEditTextWeight?.visibility = View.VISIBLE
        binding?.textInputEditTextHeight?.visibility = View.VISIBLE
        binding?.tilUsUnitWeight?.visibility = View.GONE
        binding?.llMetricUSUnitHeightFeetInch?.visibility = View.GONE

        binding?.textInputEditTextHeight?.text!!.clear()
        binding?.textInputEditTextWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE

    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.textInputEditTextWeight?.visibility = View.GONE
        binding?.textInputEditTextHeight?.visibility = View.GONE
        binding?.tilUsUnitWeight?.visibility = View.VISIBLE
        binding?.tilUsUnitWeight?.visibility = View.VISIBLE
        binding?.llMetricUSUnitHeightFeetInch?.visibility = View.VISIBLE

        binding?.tilUsUnitWeight?.text!!.clear()
        binding?.tilUSUnitFeet?.text!!.clear()
        binding?.tilUSUnitInch?.text!!.clear()


        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE

    }



    private fun validateMetricUnits():Boolean {

        var isValid = true

        if(binding?.textInputEditTextWeight?.text.toString().isEmpty())
        { isValid = false}


        else if (binding?.textInputEditTextHeight?.text.toString().isEmpty())
        { isValid = false }

        return isValid
    }


    private fun validateUSUnits():Boolean {
        var isValid = true

        when {
             binding?.tilUSUnitInch?.text!!.toString().isEmpty() -> { isValid = false }
             binding?.tilUSUnitFeet?.text!!.toString().isEmpty() -> { isValid = false }
             binding?.tilUsUnitWeight?.text!!.toString().isEmpty() -> { isValid = false }
             }

        return isValid
    }


    private fun calculateUnits(){
        if (currentVisibleView == METRIC_UNITS_VIEW)
        {
            if(validateMetricUnits()) {
                val heightValue : Float = binding?.textInputEditTextHeight?.text.toString().toFloat() / 100

                val weightValue : Float = binding?.textInputEditTextWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue * heightValue)

                displayBMIResult(bmi)

            } else {
                Toast.makeText(this@BMIActivity , "Please Enter Valid Values" , Toast.LENGTH_SHORT).show() }
        }else {
                if(validateUSUnits()){
                    val usUnitHeightValueFeet : String = binding?.tilUSUnitFeet?.text.toString()
                    val usUnitHeightValueInch : String = binding?.tilUSUnitInch?.text.toString()
                    val usUnitWeightValue : Float = binding?.tilUsUnitWeight?.text.toString().toFloat()

                    val heightValue: Float = usUnitHeightValueFeet.toFloat() * 12 + usUnitHeightValueInch.toFloat()

                    val  bmi  = 703 * ( usUnitWeightValue / (heightValue * heightValue))
                     displayBMIResult(bmi)
                } else {
                    Toast.makeText(this@BMIActivity , "Please Enter Valid Values" , Toast.LENGTH_SHORT).show() }
        }
    }

    private fun displayBMIResult(bmi : Float) {

        val bmiLabel: String
        val bmiDescription : String

        if(bmi.compareTo(15f) <= 0  )
        {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You need to take better care of yourself. Eat more."
        }else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You need to take better care of yourself. Eat more."
        }
        else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations you are in good shape"
        }
        else if(bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops you really need to take care of yourself! workout maybe!"
        }
        else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese class | (Moderately Obese)"
            bmiDescription = "Oops you really need to take care of yourself! workout maybe!"
        }
        else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese class || (Severely Obese)"
            bmiDescription = "OMG! you are in very dangerous condition act now!"
        } else
        {
             bmiLabel = "Obese class |||  (Very severely Obese)"
            bmiDescription = "OMG! you are in very dangerous condition act now!"

        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription

    }




}