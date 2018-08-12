package com.adamnickle.reptrack.ui.settings

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.preference.*
import android.view.MenuItem
import android.view.WindowManager
import com.adamnickle.reptrack.BuildConfig
import com.adamnickle.reptrack.R

class SettingsActivity : DaggerAppCompatPreferenceActivity()
{
    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )

        supportActionBar?.setDisplayHomeAsUpEnabled( true )

        if( BuildConfig.DEBUG )
        {
            window.addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON )
        }
    }

    override fun onIsMultiPane(): Boolean = isXLargeTablet( this )

    override fun onBuildHeaders( target: List<PreferenceActivity.Header> )
    {
        loadHeadersFromResource( R.xml.pref_headers, target )
    }

    override fun isValidFragment( fragmentName: String ): Boolean
    {
        return PreferenceFragment::class.java.name == fragmentName
                || GeneralPreferenceFragment::class.java.name == fragmentName
                || DataSyncPreferenceFragment::class.java.name == fragmentName
                || NotificationPreferenceFragment::class.java.name == fragmentName
    }

    abstract class PrefFragment: PreferenceFragment()
    {
        override fun onCreate( savedInstanceState: Bundle? )
        {
            super.onCreate( savedInstanceState )
            setHasOptionsMenu( true )
        }

        override fun onOptionsItemSelected( item: MenuItem ): Boolean = when( item.itemId )
        {
            android.R.id.home ->
            {
                startActivity( Intent( activity, SettingsActivity::class.java ) )
                true
            }
            else -> super.onOptionsItemSelected( item )
        }
    }

    class GeneralPreferenceFragment : PrefFragment()
    {
        override fun onCreate( savedInstanceState: Bundle? )
        {
            super.onCreate( savedInstanceState )
            addPreferencesFromResource( R.xml.pref_general )

            bindPreferenceSummaryToValue( findPreference( "example_text" ) )
            bindPreferenceSummaryToValue( findPreference( "example_list" ) )
        }
    }

    class NotificationPreferenceFragment : PrefFragment()
    {
        override fun onCreate( savedInstanceState: Bundle? )
        {
            super.onCreate( savedInstanceState )
            addPreferencesFromResource( R.xml.pref_notification )

            bindPreferenceSummaryToValue( findPreference( "notifications_new_message_ringtone" ) )
        }
    }

    class DataSyncPreferenceFragment : PrefFragment()
    {
        override fun onCreate( savedInstanceState: Bundle? )
        {
            super.onCreate( savedInstanceState )
            addPreferencesFromResource( R.xml.pref_data_sync )

            bindPreferenceSummaryToValue( findPreference( "sync_frequency" ) )
        }
    }

    companion object
    {
        private val bindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            if( preference is ListPreference )
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
            else if( preference is RingtonePreference )
            {
                if( stringValue.isBlank() )
                {
                    preference.setSummary( R.string.pref_ringtone_silent )
                }
                else
                {
                    val ringtone = RingtoneManager.getRingtone( preference.context, Uri.parse( stringValue ) )

                    if( ringtone == null )
                    {
                        preference.setSummary( null )
                    }
                    else
                    {
                        val name = ringtone.getTitle( preference.context )
                        preference.setSummary( name )
                    }
                }
            }
            else
            {
                preference.summary = stringValue
            }

            return@OnPreferenceChangeListener true
        }

        private fun isXLargeTablet( context: Context ): Boolean
        {
            return ( context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK ) >= Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

        private fun bindPreferenceSummaryToValue( preference: Preference )
        {
            preference.onPreferenceChangeListener = bindPreferenceSummaryToValueListener

            val value = PreferenceManager
                    .getDefaultSharedPreferences( preference.context )
                    .getString( preference.key, "" )

            bindPreferenceSummaryToValueListener.onPreferenceChange( preference, value )
        }
    }
}
