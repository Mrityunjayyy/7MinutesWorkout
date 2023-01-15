package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding:ActivityExerciseBinding ?= null

    private var restTimer: CountDownTimer?= null
    private var restProgress:Int = 0

    private var exerciseTimer: CountDownTimer?= null
    private var exerciseProgress:Int = 0

    private var exerciseList:ArrayList<ExerciseModel> ?= null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech ?= null
    private var player : MediaPlayer?= null

    private var exerciseAdapter : ExerciseStatusAdapter ?= null

    private var restTimerDuration : Long = 1
    private var exerciseTimerDuration : Long = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        setSupportActionBar(binding?.toolBarExercise)
        if(supportActionBar != null)
        {
          supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolBarExercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }
        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this , this )

        setUpRestView()
        setupExerciseStatusRecyclerView()

    }


    private fun customDialogForBackButton()
    {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding?.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnYes.setOnClickListener { this@ExerciseActivity.finish()
        customDialog.dismiss()  }


        dialogBinding.btnNO.setOnClickListener {  customDialog.dismiss() }

        customDialog.show()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    private fun setupExerciseStatusRecyclerView(){
         binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }



     private fun setUpRestView()
    {


        try {
            val soundURI = Uri.parse(
                "android.resource://com.example.a7minutesworkout/" + R.raw.press_start
            )
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false    
            player?.start()
        }catch(e:Exception){
        e.printStackTrace()
    }


        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.upComingLabel?.visibility = View.VISIBLE
        binding?.upComingExerciseName?.visibility = View.VISIBLE


        if (restTimer != null)
        {
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.upComingExerciseName?.text = exerciseList!![currentExercisePosition +1 ].getName()

        setRestProgressBar()

     }


    private fun setUpExerciseView()
    {
       binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.upComingLabel?.visibility = View.INVISIBLE
        binding?.upComingExerciseName?.visibility = View.INVISIBLE

        if (exerciseTimer != null)
        {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()

    }



    private fun setRestProgressBar()
    {
        binding?.progressBar?.progress = restProgress

        restTimer = object: CountDownTimer(restTimerDuration*10000 , 1000)
        {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setUpExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar()
    {
        binding?.progressBar?.progress = exerciseProgress

        exerciseTimer = object: CountDownTimer( exerciseTimerDuration*30000 , 1000)
        {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1 )
                {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setUpRestView()
                }else
                {  finish()
                    val intent = Intent(this@ExerciseActivity , FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    private fun speakOut (text : String){
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH , null , "")
    }


    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null)
        {
                restTimer?.cancel()
                restProgress = 0
        }


        if (exerciseTimer != null)
        {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts !=  null)
        {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null)
        {
            player!!.stop()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if ( status == TextToSpeech.SUCCESS)
        {
            val result = tts!!.setLanguage(Locale.ENGLISH)
        }

        if(status == TextToSpeech.LANG_NOT_SUPPORTED || status == TextToSpeech.LANG_MISSING_DATA)
        { Log.e("TTS", "The language specified is not supported")}
        else
        { Log.e("TTS", "Initialization Failed")}
    }


}