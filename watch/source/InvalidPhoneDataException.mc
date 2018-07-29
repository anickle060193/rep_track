using Toybox.Lang;

class InvalidPhoneDataException extends Lang.Exception
{
    private var _message;

    function initialize( message )
    {
        Exception.initialize();

        _message = message;
    }

    function getErrorMessage()
    {
        return _message;
    }
}