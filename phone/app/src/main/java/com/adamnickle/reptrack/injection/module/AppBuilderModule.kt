package com.adamnickle.reptrack.injection.module

import com.adamnickle.reptrack.MainActivity
import com.adamnickle.reptrack.ui.completedExerciseSet.CompletedExerciseSetFragment
import com.adamnickle.reptrack.ui.devices.SelectDeviceActivity
import com.adamnickle.reptrack.ui.exercise.ExerciseFragment
import com.adamnickle.reptrack.ui.settings.SettingsActivity
import com.adamnickle.reptrack.ui.settings.SettingsFragment
import com.adamnickle.reptrack.ui.uncompletedExerciseSet.UncompletedExerciseSetFragment
import com.adamnickle.reptrack.ui.workout.WorkoutFragment
import com.adamnickle.reptrack.ui.workouts.WorkoutListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress( "unused" )
@Module
abstract class AppBuilderModule
{
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeWorkoutListFragment(): WorkoutListFragment

    @ContributesAndroidInjector
    abstract fun contributeWorkoutFragment(): WorkoutFragment

    @ContributesAndroidInjector
    abstract fun contributeExerciseFragment(): ExerciseFragment

    @ContributesAndroidInjector
    abstract fun contributeUncompletedExerciseSetFragment(): UncompletedExerciseSetFragment

    @ContributesAndroidInjector
    abstract fun contributeCompletedExerciseSetFragment(): CompletedExerciseSetFragment

    @ContributesAndroidInjector
    abstract fun contributeSelectDeviceActivity(): SelectDeviceActivity

    @ContributesAndroidInjector
    abstract fun contributeSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment
}