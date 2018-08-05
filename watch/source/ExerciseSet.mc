using Toybox.Lang as Lang;

class ExerciseSet
{
    var id;
    var completed;
    var weight;
    var repCount;

    function initialize( exerciseSetMap )
    {
        if( !( exerciseSetMap instanceof Lang.Dictionary ) )
        {
            throw new InvalidPhoneDataException( "Exercise Set map is not a Dictionary." );
        }

        if( !exerciseSetMap.hasKey( "id" ) )
        {
            throw new InvalidPhoneDataException( "Exercise Set map does not have id." );
        }
        id = exerciseSetMap.get( "id" );
        if( !( id instanceof Lang.Long ) )
        {
            throw new InvalidPhoneDataException( "Exercise Set map id is not a long." );
        }

        if( !exerciseSetMap.hasKey( "completed" ) )
        {
            throw new InvalidPhoneDataException( "Exercise Set map does not have completed." );
        }
        completed = exerciseSetMap.get( "completed" );
        if( !( completed instanceof Lang.Boolean ) )
        {
            throw new InvalidPhoneDataException( "Exercise Set completed is not a boolean." );
        }

        if( !exerciseSetMap.hasKey( "weight" ) )
        {
            throw new InvalidPhoneDataException( "Exercise Set map does not have weight." );
        }
        weight = exerciseSetMap.get( "weight" );
        if( !( weight instanceof Lang.Float ) )
        {
            throw new InvalidPhoneDataException( "Exercise Set weight is not a float." );
        }
        weight = weight.toNumber();

        if( !exerciseSetMap.hasKey( "repCount" ) )
        {
            throw new InvalidPhoneDataException( "Exercise Set map does not have repCount." );
        }
        repCount = exerciseSetMap.get( "repCount" );
        if( !( repCount instanceof Lang.Number ) )
        {
            throw new InvalidPhoneDataException( "Exercise Set repCount is not an int." );
        }
    }
}