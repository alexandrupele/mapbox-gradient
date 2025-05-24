package com.pelworks.mapboxgradient.presentation.model

import com.mapbox.geojson.Point

data class MapState(
    val points: List<Point>,
    val gradientStops: List<GradientStop>,
)

data class GradientStop(
    val progress: Double,
    val rgbColor: RgbColor,
)