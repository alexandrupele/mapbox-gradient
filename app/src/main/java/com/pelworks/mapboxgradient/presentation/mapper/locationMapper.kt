package com.pelworks.mapboxgradient.presentation.mapper

import com.mapbox.geojson.Point
import com.pelworks.mapboxgradient.domain.model.Location
import com.pelworks.mapboxgradient.presentation.model.GradientStop
import com.pelworks.mapboxgradient.presentation.model.MapState
import com.pelworks.mapboxgradient.presentation.model.MercatorPoint
import com.pelworks.mapboxgradient.presentation.model.RgbColor
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.hypot
import kotlin.math.ln
import kotlin.math.sin

fun List<Location>.toMapState(): MapState {
    val points = map { Point.fromLngLat(it.lon, it.lat) }
    return MapState(
        points = points,
        gradientStops = computeGradientStops(
            points = points,
            pointColors = map { it.toSpeedColor().toRgb() }
        )
    )
}

/**
 * Uses Mercator projection to compute line progress (percentage)
 */
fun computeGradientStops(
    points: List<Point>,
    pointColors: List<RgbColor>,
): List<GradientStop> {
    val mercatorPoints = points.map { it.toMercatorPoint() }

    // Compute distances between each pair of points
    val segmentLengths = mercatorPoints
        .zipWithNext { a, b -> hypot(b.x - a.x, b.y - a.y) }

    val totalLength = segmentLengths.sum().takeIf { it > 0 } ?: return emptyList()

    // Compute normalized cumulative distances (stops)
    val cumulativeDistances = segmentLengths
        .runningFold(0.0) { acc, d -> acc + d }
        .map { it / totalLength }

    // Pair stops with colors and round
    return cumulativeDistances
        .zip(pointColors) { stop, color ->
            GradientStop(
                progress = BigDecimal(stop).setScale(6, RoundingMode.HALF_UP).toDouble(),
                rgbColor = color
            )
        }
        .distinctBy { it.progress }
}

private fun projectX(lon: Double): Double = lon / 360.0 + 0.5

private fun projectY(lat: Double): Double {
    val sin = sin(lat * Math.PI / 180.0)
    val y = 0.5 - 0.25 * ln((1 + sin) / (1 - sin)) / Math.PI
    return y.coerceIn(0.0, 1.0)
}

private fun Point.toMercatorPoint() =
    MercatorPoint(
        x = projectX(longitude()),
        y = projectY(latitude())
    )
