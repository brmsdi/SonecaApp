package com.brmsdi.sonecaapp.model

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
enum class TypeDistance(val id: Int, val type: String) {
    METERS(0, "m"),
    KM(1, "km"),
    OTHER(2, "Outro");

    companion object {
        fun getForID(id: Long) : TypeDistance {
            for (type in entries) {
                if (type.id.toLong() == id) return type
            }
            return OTHER
        }
    }
}