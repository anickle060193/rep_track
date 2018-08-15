package com.adamnickle.reptrack.utils

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.adamnickle.reptrack.R

object AccelerationTransforms
{
    private val accelerationUnitPreference = PreferenceLiveData<Int>( "acceleration_units" )

    fun mapConvertAcceleration( accelerationLiveData: LiveData<Float> ): LiveData<Float>
    {
        val mediator = MediatorLiveData<Float>()
        mediator.addSource( accelerationLiveData ) { acceleration ->
            accelerationUnitPreference.value?.let { accelerationUnit ->
                mediator.value = Convert.convertAcceleration( acceleration, accelerationUnit )
            }
        }
        mediator.addSource( accelerationUnitPreference ) { accelerationUnit ->
            accelerationLiveData.value?.let { acceleration ->
                mediator.value = Convert.convertAcceleration( acceleration, accelerationUnit )
            }
        }
        return mediator
    }

    fun mapConvertAccelerations( accelerationsLiveData: LiveData<List<Float>> ): LiveData<List<Float>>
    {
        val mediator = MediatorLiveData<List<Float>>()
        mediator.addSource( accelerationsLiveData ) { accelerations ->
            accelerationUnitPreference.value?.let { accelerationUnit ->
                mediator.value = accelerations.map { acceleration -> Convert.convertAcceleration( acceleration, accelerationUnit ) }
            }
        }
        mediator.addSource( accelerationUnitPreference ) { accelerationUnit ->
            accelerationsLiveData.value?.let { accelerations ->
                mediator.value = accelerations.map { acceleration -> Convert.convertAcceleration( acceleration, accelerationUnit ) }
            }
        }
        return mediator
    }

    fun mapFormatAcceleration( accelerationLiveData: LiveData<Float>, resources: Resources ): LiveData<String>
    {
        val accelerationFormats = resources.getStringArray( R.array.acceleration_formats )

        val mediator = MediatorLiveData<String>()
        mediator.addSource( accelerationLiveData ) { acceleration ->
            accelerationUnitPreference.value?.let { accelerationUnit ->
                mediator.value = Format.formatAcceleration( acceleration, accelerationUnit, accelerationFormats )
            }
        }
        mediator.addSource( accelerationUnitPreference ) { accelerationUnit ->
            accelerationLiveData.value?.let { acceleration ->
                mediator.value = Format.formatAcceleration( acceleration, accelerationUnit, accelerationFormats )
            }
        }
        return mediator
    }
}