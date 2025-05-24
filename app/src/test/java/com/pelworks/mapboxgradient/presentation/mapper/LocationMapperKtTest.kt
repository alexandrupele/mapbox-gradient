package com.pelworks.mapboxgradient.presentation.mapper

import com.mapbox.geojson.Point
import com.pelworks.mapboxgradient.presentation.model.RgbColor
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class LocationMapperKtTest {

    @Test
    fun `test computeGradientStops with two points`() {
        val points = listOf(
            Point.fromLngLat(10.0, 10.0),
            Point.fromLngLat(20.0, 20.0)
        )
        val colors = listOf(
            RgbColor(1.0, 0.0, 0.0),
            RgbColor(0.0, 1.0, 0.0)
        )

        val result = computeGradientStops(points, colors)

        // Should return 2 gradient stops
        assertEquals(2, result.size)

        // First stop should be at 0.0
        assertEquals(0.0, result[0].progress, 1e-6)

        // Second stop should be at 1.0
        assertEquals(1.0, result[1].progress, 1e-6)

        // Colors should match input
        assertEquals(colors[0], result[0].rgbColor)
        assertEquals(colors[1], result[1].rgbColor)
    }

    @Test
    fun `test computeGradientStops with three points`() {
        val points = listOf(
            Point.fromLngLat(0.0, 0.0),
            Point.fromLngLat(0.0, 1.0),
            Point.fromLngLat(0.0, 3.0)
        )
        val colors = listOf(
            RgbColor(1.0, 0.0, 0.0),
            RgbColor(0.0, 1.0, 0.0),
            RgbColor(0.0, 0.0, 1.0)
        )

        val result = computeGradientStops(points, colors)

        assertEquals(3, result.size)

        // Progress checks — based on actual distance ratios (1.0, 2.0)
        assertEquals(0.0, result[0].progress, 1e-6)
        assertEquals(0.333198, result[1].progress, 1e-6)
        assertEquals(1.0, result[2].progress, 1e-6)

        // Ensure colors stay in order
        assertEquals(colors[0], result[0].rgbColor)
        assertEquals(colors[1], result[1].rgbColor)
        assertEquals(colors[2], result[2].rgbColor)
    }

    @Test
    fun `test computeGradientStops with zero distance`() {
        val points = listOf(
            Point.fromLngLat(0.0, 0.0),
            Point.fromLngLat(0.0, 0.0)
        )
        val colors = listOf(
            RgbColor(1.0, 0.0, 0.0),
            RgbColor(0.0, 1.0, 0.0)
        )

        val result = computeGradientStops(points, colors)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `test computeGradientStops deduplicates progress`() {
        val points = listOf(
            Point.fromLngLat(0.0, 0.0),
            Point.fromLngLat(1e-12, 0.0), // practically same as first
            Point.fromLngLat(1.0, 0.0)
        )
        val colors = listOf(
            RgbColor(1.0, 0.0, 0.0),
            RgbColor(0.0, 1.0, 0.0),
            RgbColor(0.0, 0.0, 1.0)
        )

        val result = computeGradientStops(points, colors)

        // Assert only two gradient stops remain due to progress deduplication
        assertEquals(2, result.size)

        // First and last colors should remain
        assertEquals(colors.first(), result.first().rgbColor)
        assertEquals(colors.last(), result.last().rgbColor)

        // Ensure progress is 0.0 and 1.0
        assertEquals(0.0, result.first().progress, 1e-6)
        assertEquals(1.0, result.last().progress, 1e-6)
    }
}