package com.brmsdi.sonecaapp.data.listeners.models

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
data class Alarm(
    val id: Int?,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Distance,
    val daysOfWeek: MutableList<Int>,
    val hour: Int,
    val minutes: Int
)