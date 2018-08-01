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

    fun initialize( context: Context, wireless: Boolean, callback: ( ConnectIQ.IQSdkErrorStatus? ) -> Unit )
    {
        if( isInitialized )
        {
            throw IllegalStateException( "Connect IQ has already been initialized." )
        }

        val applicationContext = context.applicationContext

        connectIQ = ConnectIQ.getInstance( applicationContext, if( wireless ) ConnectIQ.IQConnectType.WIRELESS else ConnectIQ.IQConnectType.TETHERED )
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

    fun getApplicationInfo( deviceId: Long, applicationId: String, callback: ConnectIQ.IQApplicationInfoListener )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )

        connectIQ?.getApplicationInfo( applicationId, device, callback )
    }

    fun openApplication( deviceId: Long, applicationId: String, callback: ConnectIQ.IQOpenApplicationListener )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )
        val application = IQApp( applicationId )
        connectIQ?.openApplication( device, application, callback )
    }

    fun sendMessage( deviceId: Long, applicationId: String, message: Any, callback: ConnectIQ.IQSendMessageListener )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )
        val application = IQApp( applicationId )

        appExecutors.networkIO().execute {
            connectIQ?.sendMessage( device, application, message ) { iqDevice, iqApp, status ->
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

    abstract class ConnectIQEventListener: ConnectIQ.IQDeviceEventListener, ConnectIQ.IQApplicationEventListener

    fun startListeningToDevice( deviceId: Long, applicationId: String, listener: ConnectIQEventListener )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )
        val application = IQApp( applicationId )

        connectIQ?.registerForEvents( device, listener, application, listener )
    }

    fun stopListeningToDevice( deviceId: Long, applicationId: String )
    {
        assertSqkReady()

        val device = IQDevice( deviceId, "" )
        connectIQ?.unregisterForEvents( device )
    }
}