/*
 * Copyright 2024 by Patryk Goworowski and Patrick Michalik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.patrykandpatrick.vico.core.chart.draw

import android.graphics.Canvas
import android.graphics.RectF
import androidx.annotation.RestrictTo
import com.patrykandpatrick.vico.core.chart.CartesianChart
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.context.DrawContext
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.getClosestMarkerEntryModel
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import com.patrykandpatrick.vico.core.util.Point

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun chartDrawContext(
    canvas: Canvas,
    elevationOverlayColor: Int,
    measureContext: MeasureContext,
    markerTouchPoint: Point?,
    horizontalDimensions: HorizontalDimensions,
    chartBounds: RectF,
    horizontalScroll: Float,
    zoom: Float,
): ChartDrawContext =
    object : ChartDrawContext, MeasureContext by measureContext {
        override val chartBounds: RectF = chartBounds

        override var canvas: Canvas = canvas

        override val elevationOverlayColor: Long = elevationOverlayColor.toLong()

        override val markerTouchPoint: Point? = markerTouchPoint

        override val zoom: Float = zoom

        override val horizontalDimensions: HorizontalDimensions = horizontalDimensions

        override val horizontalScroll: Float = horizontalScroll

        override fun withOtherCanvas(
            canvas: Canvas,
            block: (DrawContext) -> Unit,
        ) {
            val originalCanvas = this.canvas
            this.canvas = canvas
            block(this)
            this.canvas = originalCanvas
        }
    }

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun ChartDrawContext.drawMarker(
    marker: Marker,
    markerTouchPoint: Point?,
    chart: CartesianChart,
    markerVisibilityChangeListener: MarkerVisibilityChangeListener?,
    wasMarkerVisible: Boolean,
    setWasMarkerVisible: (Boolean) -> Unit,
    lastMarkerEntryModels: List<Marker.EntryModel>,
    onMarkerEntryModelsChange: (List<Marker.EntryModel>) -> Unit,
) {
    markerTouchPoint
        ?.let(chart.entryLocationMap::getClosestMarkerEntryModel)
        ?.let { markerEntryModels ->
            marker.draw(
                context = this,
                bounds = chart.bounds,
                markedEntries = markerEntryModels,
                chartValues = chartValues,
            )
            if (wasMarkerVisible.not()) {
                markerVisibilityChangeListener?.onMarkerShown(
                    marker = marker,
                    markerEntryModels = markerEntryModels,
                )
                setWasMarkerVisible(true)
            }
            val didMarkerMove = lastMarkerEntryModels.hasMoved(markerEntryModels)
            if (wasMarkerVisible && didMarkerMove) {
                onMarkerEntryModelsChange(markerEntryModels)
                if (lastMarkerEntryModels.isNotEmpty()) {
                    markerVisibilityChangeListener?.onMarkerMoved(
                        marker = marker,
                        markerEntryModels = markerEntryModels,
                    )
                }
            }
        } ?: marker
        .takeIf { wasMarkerVisible }
        ?.also {
            markerVisibilityChangeListener?.onMarkerHidden(marker = marker)
            setWasMarkerVisible(false)
        }
}

private fun List<Marker.EntryModel>.xPosition(): Float? = firstOrNull()?.entry?.x

private fun List<Marker.EntryModel>.hasMoved(other: List<Marker.EntryModel>): Boolean = xPosition() != other.xPosition()
