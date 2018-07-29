using Toybox.Lang as Lang;

class Workout
{
    var id;
    var name;
    var exercises;

    function initialize( workoutMap )
    {
        if( !( workoutMap instanceof Lang.Dictionary ) )
        {
            throw new InvalidPhoneDataException( "Workout map is not a Dictionary." );
        }

        if( !workoutMap.hasKey( "id" ) )
        {
            throw new InvalidPhoneDataException( "Workout map does not have id." );
        }
        id = workoutMap.get( "id" );
        if( !( id instanceof Lang.String ) )
        {
            throw new InvalidPhoneDataException( "Workout map id is not a string." );
        }

        if( !workoutMap.hasKey( "name" ) )
        {
            throw new InvalidPhoneDataException( "Workout map does not have name." );
        }
        name = workoutMap.get( "name" );
        if( !( name instanceof Lang.String ) )
        {
            throw new InvalidPhoneDataException( "Workout map name is not a string." );
        }

        if( !workoutMap.hasKey( "exercises" ) )
        {
            throw new InvalidPhoneDataException( "Workout map does not have exercises." );
        }
        var exs = workoutMap.get( "exercises" );
        if( !( exs instanceof Lang.Array ) )
        {
            throw new InvalidPhoneDataException( "Workout map exercises is not an array." );
        }
        var exercisesCount = exs.size();
        exercises = new [ exercisesCount ];
        for( var i = 0; i < exercisesCount; i++ )
        {
            exercises[ i ] = new Exercise( exs[ i ] );
        }
    }
}