using Toybox.WatchUi as Ui;

class CustomBehaviorDelegate extends Ui.BehaviorDelegate
{
    function onBack()
    {
        return false;
    }

    function onForward()
    {
        return false;
    }

    function onScrollToNextItem()
    {
        return false;
    }

    function onScrollToPreviousItem()
    {
        return false;
    }

    function onSwipe( swipeEvent )
    {
        var direction = swipeEvent.getDirection();
        if( direction == Ui.SWIPE_RIGHT )
        {
            onBack();
            return true;
        }
        else if( direction == Ui.SWIPE_UP )
        {
            return onScrollToNextItem();
        }
        else if( direction == Ui.SWIPE_DOWN )
        {
            return onScrollToPreviousItem();
        }
        else if( direction == Ui.SWIPE_LEFT )
        {
            return onForward();
        }

        return false;
    }

    function onNextPage()
    {
        if( !Utils.hasTouchScreen() )
        {
            return onScrollToNextItem();
        }
        return false;
    }

    function onPreviousPage()
    {
        if( !Utils.hasTouchScreen() )
        {
            return onScrollToPreviousItem();
        }
        return false;
    }

    function onSelect()
    {
        return onForward();
    }

    function onKey( event )
    {
        if( event.getKey() == Ui.KEY_ENTER )
        {
            return onForward();
        }
        return false;
    }

    function onTap( event )
    {
        return onForward();
    }
}