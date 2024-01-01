package com.brmsdi.sonecaapp.data.listeners.models

import com.brmsdi.sonecaapp.data.listeners.dtos.SpinnerDTO

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class DistanceSpinner(private val distance: Distance) : SpinnerDTO<Distance> {
    override fun getModel() = distance
    override fun toString(): String {
        return "${distance.value} ${distance.typeDistance.type}"
    }
}