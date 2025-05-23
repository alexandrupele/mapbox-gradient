package com.pelworks.mapboxgradient.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.style.StyleContract
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.interpolate
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.pelworks.mapboxgradient.presentation.model.GradientStop

@Composable
fun MainScreen(
    state: MainState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        state.mapState?.let { mapState ->
            MapboxMap(
                compass = {},
                scaleBar = {},
                mapViewportState = rememberMapViewportState {
                    flyTo(
                        cameraOptions {
                            center(state.mapState.points.first())
                            zoom(15.0)
                        },
                        MapAnimationOptions.mapAnimationOptions { duration(3000) }
                    )
                }
            ) {
                MapEffect(Unit) { mapView ->
                    mapView.mapboxMap.loadStyle(
                        createStyle(
                            points = mapState.points,
                            gradientStops = mapState.gradientStops
                        )
                    )
                }
            }
        }
    }
}

fun createStyle(
    points: List<Point>,
    gradientStops: List<GradientStop>,
): StyleContract.StyleExtension {
    return style(style = Style.SATELLITE_STREETS) {
        val sourceId = "session"
        val lineLayerId = "line"
        val lineString = LineString.fromLngLats(points)
        val feature = Feature.fromGeometry(lineString)

        +geoJsonSource(sourceId) {
            feature(feature)
            lineMetrics(true)
        }

        +lineLayer(layerId = lineLayerId, sourceId = sourceId) {
            lineCap(LineCap.ROUND)
            lineJoin(LineJoin.ROUND)
            lineWidth(3.0)

            lineGradient(
                interpolate {
                    linear()
                    lineProgress()
                    gradientStops
                        .forEach { (progress, rgb) ->
                            val (r, g, b) = rgb
                            stop(progress) {
                                rgb {
                                    literal(r)
                                    literal(g)
                                    literal(b)
                                }
                            }
                        }
                }
            )
        }
    }
}