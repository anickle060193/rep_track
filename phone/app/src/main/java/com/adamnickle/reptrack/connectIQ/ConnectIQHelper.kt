package com.adamnickle.reptrack.connectIQ

import android.content.Context
import com.adamnickle.reptrack.AppExecutors
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectIQHelper @Inject constructor()
{
    companion object
    {
        const val REP_TRACK_APP_ID = "2649146e00df42358fb239388daf724b"

        private val REP_TRACK_APP = IQApp( REP_TRACK_APP_ID )
    }

    @Inject
    lateinit var appExecutors: AppExecutors

    private var connectIQ: ConnectIQ? = null

    private var sdkReady = false

    val isInitialized get() = connectIQ != null

    val isSdkReady get() = sdkReady

    private fun assertSqkReady()
    {
        if( !isInitialized
         || !isSdkReady )
        {
            throw IllegalStateException( "Connect IQ SDK is not ready." )
        }
    }

    fun initialize( context: Context, callback: ( ConnectIQ.IQSdkErrorStatus? ) -> Unit )
    {
        if( isInitialized )
        {
            throw IllegalStateException( "Connect IQ has already been initialized." )
        }

        val applicationContext = context.applicationContext

        connectIQ = ConnectIQ.getInstance( applicationContext, ConnectIQ.IQConnectType.TETHERED )
        connectIQ?.initialize( applicationContext, true, object: ConnectIQ.ConnectIQListener
        {
            override fun onSdkReady()
            {
                sdkReady = true

                callback.invoke( null )
            }

            override fun onInitializeError( iqSdkErrorStatus: ConnectIQ.IQSdkErrorStatus )
            {
                sdkReady = false

                callback.invoke( iqSdkErrorStatus )
            }

            override fun onSdkShutDown()
            {
                sdkReady = false
                this@ConnectIQHelper.connectIQ = null
            }
        } )
    }

    fun getDevices(): List<IQDevice>
    {
        assertSqkReady()

        return connectIQ?.connectedDevices ?: listOf()
    }

    fun getDeviceStatus( deviceId: Long ): IQDevice.IQDeviceStatus
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )
        return connectIQ?.getDeviceStatus( device ) ?: IQDevice.IQDeviceStatus.UNKNOWN
    }

    fun getApplicationInfo( deviceId: Long, listener: ConnectIQ.IQApplicationInfoListener )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )

        connectIQ?.getApplicationInfo( REP_TRACK_APP_ID, device, listener )
    }

    fun openApplication( deviceId: Long, listener: ConnectIQ.IQOpenApplicationListener )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )
        connectIQ?.openApplication( device, REP_TRACK_APP, listener )
    }

    fun sendMessage( deviceId: Long, message: Any, listener: ConnectIQ.IQSendMessageListener )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )

        appExecutors.networkIO().execute {
            connectIQ?.sendMessage( device, REP_TRACK_APP, message, listener )
        }
    }

    abstract class ConnectIQEventListener: ConnectIQ.IQDeviceEventListener, ConnectIQ.IQApplicationEventListener

    fun startListeningToDevice( deviceId: Long, listener: ConnectIQEventListener )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )

        connectIQ?.registerForEvents( device, listener, REP_TRACK_APP, listener )
    }

    fun stopListeningToDevice( deviceId: Long )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )
        connectIQ?.unregisterForEvents( device )
    }

    fun stopListeningToAll()
    {
        assertSqkReady()

        connectIQ?.unregisterAllForEvents()
    }
}