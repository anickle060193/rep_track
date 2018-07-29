using Toybox.Sensor;
using Toybox.System as Sys;

class Accel
{
    private var _sampleRate;
    private var _period;

    function initialize()
    {
        _sampleRate = Sensor.getMaxSampleRate();
        _period = 1;
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
        var options = {
            :period => _period,
            :sampleRate => _sampleRate,
            :enableAccelerometer => true
        };
        try
        {
            Sensor.registerSensorDataListener( accelerometerDataCallback, options );
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
        Sensor.unregisterSensorDataListener();
    }
}