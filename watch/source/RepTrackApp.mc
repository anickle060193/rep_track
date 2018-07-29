using Toybox.Application as App;
using Toybox.Communications as Comm;
using Toybox.WatchUi as Ui;
using Toybox.System as Sys;

class RepTrackApp extends App.AppBase
{
    function initialize()
    {
        AppBase.initialize();
    }

    function onStart( state )
    {
        Sys.println( "App Start" );
    }

    function onStop( state )
    {
        Sys.println( "App Stop" );
        Comm.registerForPhoneAppMessages( null );
    }

    function getInitialView()
    {
        var delegate = new MainDelegate();
        return [ delegate.createCurrentView(), delegate ];
    }
}
