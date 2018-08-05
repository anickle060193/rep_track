using Toybox.WatchUi as Ui;
using Toybox.Communications as Comm;
using Toybox.Graphics as Gfx;
using Toybox.System as Sys;

class RecordingView extends Ui.View
{
    private var _recording;

    function initialize()
    {
        View.initialize();
        
        _recording = false;
    }

    function onLayout( dc )
    {
        setLayout( Rez.Layouts.Recording( dc ) );

        var recordLabel = View.findDrawableById( "RecordLabel" );
        recordLabel.setText( "Record" );
    }

    function onShow()
    {
    }

    function onUpdate( dc )
    {
        var recordLabel = View.findDrawableById( "RecordLabel" );
        if( _recording )
        {
            recordLabel.setText( "Stop" );
            recordLabel.setColor( Gfx.COLOR_WHITE );
        }
        else
        {
            recordLabel.setText( "Record" );
            recordLabel.setColor( Gfx.COLOR_RED );
        }
        
        View.onUpdate( dc );
    }

    function onHide()
    {
    }
    
    function setRecording( recording )
    {
        _recording = recording;
        
        Ui.requestUpdate();
    }
}

class RecordingDelegate extends Ui.BehaviorDelegate
{
    private var _workout;
    private var _exercise;
    private var _exerciseSet;

    private var _recordingView;
    private var _recording;
    
    private var _accel;

    function initialize( workout, exerciseIndex, setIndex )
    {
        BehaviorDelegate.initialize();
        
        _workout = workout;
        _exercise = _workout.exercises[ exerciseIndex ];
        _exerciseSet = _exercise.sets[ setIndex ];
        
        _recordingView = null;
        _recording = false;
        
        _accel = new Accel();
    }
    
    function onTap( clickEvent )
    {
        if( _recording )
        {
            _accel.stop();
            _recording = false;
        }
        else
        {
            _recording = _accel.start( method( :onAccelerometerCallback ) );
        }
        
        updateView();
        
        var message = {
            "type" => "recording",
            "recording" => _recording
        };
        Comm.transmit( message, { }, new RecordingCommListener( method( :onTransmitStatus ) ) );
    }
    
    private function onAccelerometerCallback( sensorData )
    {
        var message = {
            "type"=> "accelerometer",
            "workoutId" => _workout.id,
            "exerciseId" => _exercise.id,
            "exerciseSetId" => _exerciseSet.id,
            "x" => sensorData.accelerometerData.x,
            "y" => sensorData.accelerometerData.y,
            "z" => sensorData.accelerometerData.z,
            "time" => Sys.getTimer(),
            "sampleRate" => _accel.getSampleRate(),
            "period" => _accel.getPeriod()
        };
        Comm.transmit( message, { }, new RecordingCommListener( method( :onTransmitStatus ) ) );
    }

    private function onTransmitStatus( success )
    {
        Sys.println( "Transmit status: " + success );
    }

    function onSwipe( swipeEvent )
    {
        var direction = swipeEvent.getDirection();
        if( direction == Ui.SWIPE_RIGHT )
        {
            onBack();
        }
        return true;
    }

    function onBack()
    {
        if( !_recording )
        {
            Ui.popView( Ui.SLIDE_RIGHT );
        }
        return true;
    }

    function createCurrentView()
    {
        _recordingView = new RecordingView();
        updateView();
        return _recordingView;
    }
    
    private function updateView()
    {
        if( _recordingView != null )
        {
            _recordingView.setRecording( _recording );
        }
    }
}

class RecordingCommListener extends Comm.ConnectionListener
{
    private var _callback;
    
    function initialize( callback )
    {
        _callback = callback;
    }
    
    function onComplete()
    {
        _callback.invoke( true );
    }
    
    function onError()
    {
        _callback.invoke( false );
    }
}