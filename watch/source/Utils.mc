using Toybox.System as Sys;

module Utils
{
    private var systemSettings = null;

    private function getSystemSettings()
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