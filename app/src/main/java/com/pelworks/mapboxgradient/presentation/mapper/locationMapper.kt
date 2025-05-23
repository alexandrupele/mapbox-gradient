package com.pelworks.mapboxgradient.presentation.mapper

import com.mapbox.geojson.Point
import com.pelworks.mapboxgradient.domain.model.Location
import com.pelworks.mapboxgradient.presentation.model.GradientStop
import com.pelworks.mapboxgradient.presentation.model.MapState
import com.pelworks.mapboxgradient.presentation.model.MercatorPoint
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.hypot
import kotlin.math.ln
import kotlin.math.sin

fun List<Location>.toMapState(): MapState {
    val points = map { Point.fromLngLat(it.lon, it.lat) }
    return MapState(
        points = points,
        gradientStops = computeMercatorGradientStops(
            points = points,
            colors = map { it.toSpeedColor() }
        )
    )
}

fun computeMercatorGradientStops(
    points: List<Point>,
    colors: List<Int>,
): List<GradientStop> {
    // Project all points
    val mercatorPoints = points.map { it.toMercatorPoint() }
    val pointColors = colors.map { it.toRgb() }

    // Compute distances in projected space
    val distances = mutableListOf<Double>()
    var total = 0.0
    for (i in 1 until mercatorPoints.size) {
        val dx = mercatorPoints[i].x - mercatorPoints[i - 1].x
        val dy = mercatorPoints[i].y - mercatorPoints[i - 1].y
        val d = hypot(dx, dy)
        distances += d
        total += d
    }

    // Build stops
    val raw = mutableListOf<Pair<Double, Triple<Double, Double, Double>>>()
    var acc = 0.0
    raw += 0.0 to pointColors[0]
    for (i in 1 until pointColors.size) {
        acc += distances[i - 1]
        raw += (acc / total).coerceIn(0.0, 1.0) to pointColors[i]
    }

    // Deduplicate and sort
    return raw
        .map { (p, rgb) -> BigDecimal(p).setScale(6, RoundingMode.HALF_UP).toDouble() to rgb }
        .distinctBy { it.first }
        .sortedBy { it.first }
        .map { (p, rgb) -> GradientStop(p, rgb) }
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
