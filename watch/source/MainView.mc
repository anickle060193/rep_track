using Toybox.WatchUi as Ui;
using Toybox.Communications as Comm;
using Toybox.System as Sys;

class MainView extends Ui.View
{
    function initialize()
    {
        View.initialize();
    }

    function onLayout( dc )
    {
        setLayout( Rez.Layouts.Main( dc ) );
    }

    function onShow()
    {
        Sys.println( "Main SHOW" );
    }

    function onUpdate( dc )
    {
        View.onUpdate( dc );
    }

    function onHide()
    {
        Sys.println( "Main HIDE" );
    }
}

class MainDelegate extends Ui.BehaviorDelegate
{
    function initialize()
    {
        BehaviorDelegate.initialize();

        Comm.registerForPhoneAppMessages( method( :onPhoneAppMessage ) );
    }

    private function onPhoneAppMessage( message )
    {
        Sys.println( "Message: " + message.data );

        if( message.data.hasKey( "type" )
         && message.data.get( "type" ).equals( "workout" )
         && message.data.hasKey( "workout" ) )
        {
            var workoutMap = message.data.get( "workout" );
            try
            {
                var workout = new Workout( workoutMap );

                Sys.println( "Workout received" );

                Comm.registerForPhoneAppMessages( null );

                var delegate = new WorkoutDelegate( workout );
                Ui.switchToView( delegate.createCurrentView(), delegate, Ui.SLIDE_LEFT );
            }
            catch( ex instanceof InvalidPhoneDataException )
            {
                Sys.println( "Invalid workout received: " + ex.getErrorMessage() );
            }
        }
    }

    function createCurrentView()
    {
        return new MainView();
    }
}