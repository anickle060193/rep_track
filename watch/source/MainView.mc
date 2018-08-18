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

class MainDelegate extends CustomBehaviorDelegate
{
    function initialize()
    {
        BehaviorDelegate.initialize();

        Comm.registerForPhoneAppMessages( method( :onPhoneAppMessage ) );
    }

    private function onPhoneAppMessage( message )
    {
        if( message == null )
        {
          Sys.println( "Message Receieved: Message is null" );
          return;
        }

        if( message.data == null )
        {
          Sys.println( "Message Receieved: Message data is null" );
          return;
        }

        Sys.println( "Message Receieved - Data: " + message.data );

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