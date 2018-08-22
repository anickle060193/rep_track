using Toybox.System as Sys;

module Utils
{
    var systemSettings = null;

    function getSystemSettings()
    {
        if( systemSettings == null )
        {
            systemSettings = Sys.getDeviceSettings();
        }
        return systemSettings;
    }

    function hasTouchScreen()
    {
        return getSystemSettings().isTouchScreen;
    }
}