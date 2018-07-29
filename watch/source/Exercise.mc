using Toybox.Lang as Lang;

class Exercise
{
    var name;
    var sets;

    function initialize( exerciseMap )
    {
        if( !( exerciseMap instanceof Lang.Dictionary ) )
        {
            throw new InvalidPhoneDataException( "Exercise map is not a Dictionary." );
        }

        if( !exerciseMap.hasKey( "name" ) )
        {
            throw new InvalidPhoneDataException( "Exercise map does not have name." );
        }
        name = exerciseMap.get( "name" );
        if( !( name instanceof Lang.String ) )
        {
            throw new InvalidPhoneDataException( "Exercise map name is not a string." );
        }

        if( !exerciseMap.hasKey( "sets" ) )
        {
            throw new InvalidPhoneDataException( "Exercise map does not have sets." );
        }
        var s = exerciseMap.get( "sets" );
        if( !( s instanceof Lang.Array ) )
        {
            throw new InvalidPhoneDataException( "Exercise map sets is not an array." );
        }
        var setsCount = s.size();
        sets = new [ setsCount ];
        for( var i = 0; i < setsCount; i++ )
        {
            sets[ i ] = new ExerciseSet( s[ i ] );
        }
    }
}