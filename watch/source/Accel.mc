using Toybox.Lang;
using Toybox.Sensor;
using Toybox.System as Sys;

class Accel
{
    private var _hasAccelerometer;
    private var _sampleRate;
    private var _period;
    private var _recording;

    function initialize()
    {
        _hasAccelerometer = ( Sensor has :getMaxSampleRate
                           && Sensor has :registerSensorDataListener
                           && Sensor has :unregisterSensorDataListener );
        if( _hasAccelerometer )
        {
            _sampleRate = Sensor.getMaxSampleRate();
            _period = 1;
        }
        else
        {
            Sys.println( "Device does not have an accelerometer." );
            _sampleRate = 1;
            _period = 1;
        }

        _recording = false;
    }

    function hasAccelerometer()
    {
        return _hasAccelerometer;
    }

    function getSampleRate()
    {
        return _sampleRate;
    }

    function getPeriod()
    {
        return _period;
    }

    function start( accelerometerDataCallback )
    {
        if( _recording )
        {
            throw new Lang.Exception( "Accel already recording" );
        }

        if( !_hasAccelerometer )
        {
            return false;
        }

        var options = {
            :period => _period,
            :sampleRate => _sampleRate,
            :enableAccelerometer => true
        };
        try
        {
            Sensor.registerSensorDataListener( accelerometerDataCallback, options );
            _recording = true;
            return true;
        }
        catch( e )
        {
            Sys.println( "An error occurred registering sensor data listener:" + e.getErrorMessage() );
        }
        return false;
    }

    function stop()
    {
        if( !_recording )
        {
            throw new Lang.Exception( "Accel not recording" );
        }

        if( !_hasAccelerometer )
        {
            return;
        }

        Sensor.unregisterSensorDataListener();
        _recording = false;
    }
}