using Toybox.WatchUi as Ui;
using Toybox.System as Sys;

class ExercisesView extends Ui.View
{
    private var _exercise;

    function initialize( exercise )
    {
        View.initialize();

        _exercise = exercise;
    }

    function onLayout( dc )
    {
        setLayout( Rez.Layouts.Exercises( dc ) );

        var height = dc.getHeight();

        var exerciseNameLabel = View.findDrawableById( "ExerciseName" );
        exerciseNameLabel.setLocation( Ui.LAYOUT_HALIGN_CENTER, height * 0.1 );

        var exerciseSetCount = View.findDrawableById( "ExerciseSetCount" );
        exerciseSetCount.setLocation( Ui.LAYOUT_HALIGN_CENTER, height * 0.6 );

        exerciseNameLabel.setText( _exercise.name );
        exerciseSetCount.setText( _exercise.sets.size().toString() );
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

class ExercisesDelegate extends CustomBehaviorDelegate
{
    private var _workout;
    private var _currentExerciseIndex;

    function initialize( workout )
    {
        BehaviorDelegate.initialize();

        _workout = workout;

        _currentExerciseIndex = 0;
    }

    function onBack()
    {
        Ui.popView( Ui.SLIDE_RIGHT );
        return true;
    }

    function onForward()
    {
        var exercise = _workout.exercises[ _currentExerciseIndex ];
        if( exercise.sets.size() > 0 )
        {
            var delegate = new SetsDelegate( _workout, _currentExerciseIndex );
            Ui.pushView( delegate.createCurrentView(), delegate, Ui.SLIDE_LEFT );
        }
        return true;
    }

    function onScrollToNextItem()
    {
        var exerciseCount = _workout.exercises.size();
        if( _currentExerciseIndex < exerciseCount - 1 )
        {
            _currentExerciseIndex += 1;
            Ui.switchToView( createCurrentView(), self, Ui.SLIDE_UP );
        }
        return true;
    }

    function onScrollToPreviousItem()
    {
        if( _currentExerciseIndex > 0 )
        {
            _currentExerciseIndex -= 1;
            Ui.switchToView( createCurrentView(), self, Ui.SLIDE_DOWN );
        }
        return true;
    }

    function createCurrentView()
    {
        return new ExercisesView( _workout.exercises[ _currentExerciseIndex ] );
    }
}