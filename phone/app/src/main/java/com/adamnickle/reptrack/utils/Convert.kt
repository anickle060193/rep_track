package com.adamnickle.reptrack.utils

object Convert
{
    fun mGtoMPS( mg: Float ): Float
    {
        return mg / 1000.0f * Constants.Gravity
    }
}