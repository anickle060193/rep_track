package com.adamnickle.reptrack.ui.devices

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.BuildConfig
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.connectIQ.ConnectIQHelper
import com.adamnickle.reptrack.databinding.SelectDeviceActivityBinding
import com.adamnickle.reptrack.utils.property.autoCleared
import com.garmin.android.connectiq.ConnectIQ.IQSdkErrorStatus.*
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SelectDeviceActivity: DaggerAppCompatActivity()
{
    companion object
    {
        const val DEVICE_EXTRA = "device"
    }

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var connectIQHelper: ConnectIQHelper

    private var binding by autoCleared<SelectDeviceActivityBinding>()

    private var adapter by autoCleared<ConnectIQDeviceListAdapter>()

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )

        binding = DataBindingUtil.setContentView( this, R.layout.select_device_activity )

        setSupportActionBar( binding.toolbar )
        supportActionBar?.setDisplayHomeAsUpEnabled( true )

        setResult( Activity.RESULT_CANCELED )

        adapter = ConnectIQDeviceListAdapter( appExecutors ) { device ->
            setResult( Activity.RESULT_OK, Intent().also { intent ->
                intent.putExtra( DEVICE_EXTRA, device.deviceIdentifier )
            } )
            finish()
        }

        binding.devicesList.adapter = adapter

        binding.devicesListRefresh.setOnRefreshListener( this::refreshDevicesList )

        if( connectIQHelper.isInitialized )
        {
            refreshDevicesList()
        }
        else
        {
            connectIQHelper.initialize(this, false) { status ->
                if(status != null)
                {
                    val errorMessage = when(status)
                    {
                        GCM_NOT_INSTALLED -> "Garmin Connect App is not installed"
                        GCM_UPGRADE_NEEDED -> "Garmin Connect App update is required"
                        SERVICE_ERROR -> "A service error has occurred"
                        else -> "An error has occurred"
                    }

                    Snackbar.make( binding.root, errorMessage, Snackbar.LENGTH_LONG )
                            .show()
                }
                else
                {
                    refreshDevicesList()
                }
            }
        }

        if( BuildConfig.DEBUG )
        {
            window.addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON )
        }
    }

    override fun onCreateOptionsMenu( menu: Menu ): Boolean
    {
        menuInflater.inflate( R.menu.select_device_activity, menu )
        return true
    }

    override fun onOptionsItemSelected( item: MenuItem ): Boolean
    {
        return when( item.itemId )
        {
            R.id.refresh -> {
                binding.devicesListRefresh.isRefreshing = true
                return true
            }
            else -> super.onOptionsItemSelected( item )
        }
    }

    private fun refreshDevicesList()
    {
        adapter.submitList( connectIQHelper.getDevices() )

        binding.devicesListRefresh.isRefreshing = false
    }
}