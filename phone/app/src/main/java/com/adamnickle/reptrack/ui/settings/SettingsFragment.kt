package com.adamnickle.reptrack.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.adamnickle.reptrack.R
import javax.inject.Inject

class SettingsFragment: DaggerPreferenceFragment()
{
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val bindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
        val stringValue = value.toString()

        if( preference is ListPreference)
        {
            val index = preference.findIndexOfValue( stringValue )

            if( index >= 0 )
            {
                preference.summary = preference.entries[ index ]
            }
            else
            {
                preference.summary = null
            }
        }
        else
        {
            preference.summary = stringValue
        }

        return@OnPreferenceChangeListener true
    }

    override fun onCreatePreferences( savedInstanceState: Bundle?, rootKey: String? )
    {
        addPreferencesFromResource( R.xml.preferences )

        bindPreferenceSummaryToValue( findPreference( "acceleration_units" ) )
    }

    override fun findPreference( key: CharSequence ): Preference
    {
        return super.findPreference( key ) ?: throw IllegalArgumentException( "Could not find preference for '$key' key." )
    }

    private fun bindPreferenceSummaryToValue( preference: Preference )
    {
        preference.onPreferenceChangeListener = bindPreferenceSummaryToValueListener

        val value = sharedPreferences.getString( preference.key, "" )

        bindPreferenceSummaryToValueListener.onPreferenceChange( preference, value )
    }
}