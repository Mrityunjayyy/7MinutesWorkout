package com.example.a7minutesworkout

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>
    {
        val exerciseList = ArrayList<ExerciseModel>()


        val jumpingJacks = ExerciseModel(1,
            "Jumping Jacks",
             R.drawable.ic_jumping_jacks,
            false,
            false
        )
        exerciseList.add(jumpingJacks)

        val highKnees = ExerciseModel(2, "High Knees running in place",
            R.drawable.ic_high_knees_running_in_place , false, false )
            exerciseList.add(highKnees)

        val abdominalCrunch = ExerciseModel(3, "Abdominal Crunch",
            R.drawable.ic_abdominal_crunch , false, false )
            exerciseList.add(abdominalCrunch)


        val lunges = ExerciseModel(4, "Lunges",
            R.drawable.ic_lunge , false, false )
            exerciseList.add(lunges)

        val plank = ExerciseModel(5, "Plank",
            R.drawable.ic_plank , false, false )
            exerciseList.add(plank)


        val pushUP = ExerciseModel(6, "Push Ups",
            R.drawable.ic_push_up, false, false )
            exerciseList.add(pushUP)

        val pushUpAndRotation = ExerciseModel(7, "Push Ups and Rotation",
            R.drawable.ic_push_up_and_rotation, false, false )
           exerciseList.add(pushUpAndRotation)

        val sidePlank = ExerciseModel(8, "Side Plank",
            R.drawable.ic_side_plank, false, false )
           exerciseList.add(sidePlank)


        val squat = ExerciseModel(9, "Squats",
            R.drawable.ic_squat, false, false )
           exerciseList.add(squat)


        val stepUpOntoChair = ExerciseModel(10, "Step Up on Chair",
            R.drawable.ic_step_up_onto_chair, false, false )
           exerciseList.add(stepUpOntoChair)


        val tricepsDipsOnChair = ExerciseModel(11, "Triceps Dips on Chair",
            R.drawable.ic_triceps_dip_on_chair, false, false )
           exerciseList.add(tricepsDipsOnChair)

        val wallSit = ExerciseModel(12, "Wall Sit",
            R.drawable.ic_wall_sit, false, false )
           exerciseList.add(wallSit)

        return  exerciseList
    }

}