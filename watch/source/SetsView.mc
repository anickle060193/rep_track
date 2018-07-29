using Toybox.WatchUi as Ui;
using Toybox.System as Sys;

class SetsView extends Ui.View
{
    private var _exerciseName;
    private var _setIndex;
    private var _set;

    function initialize( exerciseName, setIndex, set )
    {
        View.initialize();

        _exerciseName = exerciseName;
        _setIndex = setIndex;
        _set = set;
    }

    function onLayout( dc )
    {
        setLayout( Rez.Layouts.Sets( dc ) );

        var width = dc.getWidth();
        var height = dc.getHeight();

        var exerciseNameLabel = View.findDrawableById( "ExerciseName" );
        exerciseNameLabel.setLocation( width * 0.5, height * 0.15 );

        var setNameLabel = View.findDrawableById( "SetName" );
        setNameLabel.setLocation( width * 0.5, height * 0.3 );

        var repsLabel = View.findDrawableById( "RepsLabel" );
        repsLabel.setLocation( width * 0.1, height * 0.5 );

        var repCountLabel = View.findDrawableById( "RepCount" );
        repCountLabel.setLocation( width * 0.55, height * 0.5 );

        var weightLabel = View.findDrawableById( "WeightLabel" );
        weightLabel.setLocation( width * 0.1, height * 0.68 );

        var weightAmountLabel = View.findDrawableById( "Weight" );
        weightAmountLabel.setLocation( width * 0.65, height * 0.85 );

        var weightUnitsLabel = View.findDrawableById( "WeightUnitsLabel" );
        weightUnitsLabel.setLocation( width * 0.68, height * 0.85 );

        exerciseNameLabel.setText( _exerciseName );

        setNameLabel.setText( "Set " + ( _setIndex + 1 ).toString() );

        repCountLabel.setText( _set.repCount.toString() );
        weightAmountLabel.setText( _set.weight.toString() );
    }

    function onShow()
    {
    }

    function onUpdate( dc )
    {
        View.onUpdate( dc );
    }

    function onHide()
    {
    }
}

class SetsDelegate extends Ui.BehaviorDelegate
{
    private var _workout;
    private var _exerciseIndex;
    private var _currentSetIndex;

    function initialize( workout, exerciseIndex )
    {
        BehaviorDelegate.initialize();

        _workout = workout;
        _exerciseIndex = exerciseIndex;

        _currentSetIndex = 0;
    }

    function onSwipe( swipeEvent )
    {
        var direction = swipeEvent.getDirection();
        if( direction == Ui.SWIPE_RIGHT )
        {
            onBack();
        }
        else if( direction == Ui.SWIPE_UP )
        {
            var setCount = _workout.exercises[ _exerciseIndex ].sets.size();
            if( _currentSetIndex < setCount - 1 )
            {
                _currentSetIndex += 1;
                Ui.switchToView( createCurrentView(), self, Ui.SLIDE_UP );
            }
        }
        else if( direction == Ui.SWIPE_DOWN )
        {
            if( _currentSetIndex > 0 )
            {
                _currentSetIndex -= 1;
                Ui.switchToView( createCurrentView(), self, Ui.SLIDE_DOWN );
            }
        }
        else if( direction == Ui.SWIPE_LEFT )
        {
            var recordingDelegate = new RecordingDelegate( _workout, _exerciseIndex, _currentSetIndex );
            Ui.pushView( recordingDelegate.createCurrentView(), recordingDelegate, Ui.SLIDE_LEFT );
        }
        return true;
    }

    function onBack()
    {
        Ui.popView( Ui.SLIDE_RIGHT );
        return true;
    }

    function createCurrentView()
    {
        var exercise = _workout.exercises[ _exerciseIndex ];
        return new SetsView( exercise.name, _currentSetIndex, exercise.sets[ _currentSetIndex ] );
    }
}