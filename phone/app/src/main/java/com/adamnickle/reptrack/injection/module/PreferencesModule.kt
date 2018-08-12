package com.adamnickle.reptrack.injection.module

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.adamnickle.reptrack.RepTrackApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule
{
    @Provides
    @Singleton
    fun provideSharedPreferences( app: RepTrackApp ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences( app )
}