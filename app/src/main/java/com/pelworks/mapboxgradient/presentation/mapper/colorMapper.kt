package com.pelworks.mapboxgradient.presentation.mapper

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.toColorInt
import com.pelworks.mapboxgradient.domain.model.Location
import kotlin.math.roundToInt

fun Int.toRgb(): Triple<Double, Double, Double> {
    val r = ((this shr 16) and 0xff).toDouble()
    val g = ((this shr 8) and 0xff).toDouble()
    val b = (this and 0xff).toDouble()
    return Triple(r, g, b)
}

@ColorInt
fun Location.toSpeedColor(): Int {
    val roundedSpeed = if (spd < 0) 0 else spd.roundToInt()
    return when (roundedSpeed) {
        0 -> Speed0ms
        1 -> Speed1ms
        2 -> Speed2ms
        3 -> Speed3ms
        4 -> Speed4ms
        5 -> Speed5ms
        6 -> Speed6ms
        7 -> Speed7ms
        8 -> Speed8ms
        9 -> Speed9ms
        10 -> Speed10ms
        11 -> Speed11ms
        12 -> Speed12ms
        13 -> Speed13ms
        14 -> Speed14ms
        15 -> Speed15ms
        16 -> Speed16ms
        17 -> Speed17ms
        18 -> Speed18ms
        19 -> Speed19ms
        20 -> Speed20ms
        21 -> Speed21ms
        22 -> Speed22ms
        else -> Color.BLACK
    }
}

private val Speed0ms = "#FFFFFFFF".toColorInt()
private val Speed1ms = "#FFAFFBD3".toColorInt()
private val Speed2ms = "#FF5FF7A7".toColorInt()
private val Speed3ms = "#FF10F37B".toColorInt()
private val Speed4ms = "#FFFDFE62".toColorInt()
private val Speed5ms = "#FFFFD552".toColorInt()
private val Speed6ms = "#FFEFB445".toColorInt()
private val Speed7ms = "#FFEA9639".toColorInt()
private val Speed8ms = "#FFE3722B".toColorInt()
private val Speed9ms = "#FFDE541F".toColorInt()
private val Speed10ms = "#FFD83413".toColorInt()
private val Speed11ms = "#FFD21406".toColorInt()
private val Speed12ms = "#FFC40E04".toColorInt()
private val Speed13ms = "#FFB80D04".toColorInt()
private val Speed14ms = "#FFAF0D04".toColorInt()
private val Speed15ms = "#FFA10C04".toColorInt()
private val Speed16ms = "#FF980B04".toColorInt()
private val Speed17ms = "#FF8D0B04".toColorInt()
private val Speed18ms = "#FF830B04".toColorInt()
private val Speed19ms = "#FF770B04".toColorInt()
private val Speed20ms = "#FF6C0A04".toColorInt()
private val Speed21ms = "#FF620A04".toColorInt()
private val Speed22ms = "#FF580804".toColorInt()