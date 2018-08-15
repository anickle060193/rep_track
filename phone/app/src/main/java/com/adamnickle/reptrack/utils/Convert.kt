package com.adamnickle.reptrack.utils

object Convert
{
    fun mGtoMPS( mg: Float ): Float = mg / 1000.0f * Constants.Gravity

    fun mGtoFTPS( mg: Float ): Float = mGtoMPS( mg ) * Constants.MT_TO_FT

    fun convertAcceleration( acceleration: Float, accelerationUnit: Int ): Float = when( accelerationUnit )
    {
        0 -> acceleration
        1 -> Convert.mGtoMPS( acceleration )
        2 -> Convert.mGtoFTPS( acceleration )
        else -> throw IllegalArgumentException( "Unknown acceleration unit: $accelerationUnit" )
    }
}