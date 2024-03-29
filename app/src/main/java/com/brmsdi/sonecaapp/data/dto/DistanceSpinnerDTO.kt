package com.brmsdi.sonecaapp.data.dto

import com.brmsdi.sonecaapp.model.Distance
import com.brmsdi.sonecaapp.model.TypeDistance

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class DistanceSpinnerDTO(private val distance: Distance) : SpinnerDTO<Distance> {
    override fun getModel() = distance
    override fun toString(): String {
        val typeDistance = TypeDistance.getForID(distance.typeDistance)
        return "${distance.value} ${typeDistance.type}"
    }
}