using Toybox.WatchUi as Ui;
using Toybox.Communications as Comm;
using Toybox.System as Sys;

class WorkoutView extends Ui.View
{
    private var _workoutName;

    function initialize( workoutName )
    {
        View.initialize();

        _workoutName = workoutName;
    }

    function onLayout( dc )
    {
        setLayout( Rez.Layouts.Workout( dc ) );

        var workoutNameLabel = View.findDrawableById( "WorkoutName" );
        workoutNameLabel.setText( _workoutName );
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

class WorkoutDelegate extends CustomBehaviorDelegate
{
    private var _workout;

    function initialize( workout )
    {
        BehaviorDelegate.initialize();

        _workout = workout;
    }

    function onForward()
    {
        if( _workout.exercises.size() > 0 )
        {
            var delegate = new ExercisesDelegate( _workout );
            Ui.pushView( delegate.createCurrentView(), delegate, Ui.SLIDE_LEFT );
        }
        return true;
    }

    function createCurrentView()
    {
        return new WorkoutView( _workout.name );
    }
}