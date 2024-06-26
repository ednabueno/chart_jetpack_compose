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

import android.graphics.RectF
import androidx.annotation.RestrictTo
import com.patrykandpatrick.vico.core.chart.CartesianChart
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.context.DrawContext
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.ceil
import com.patrykandpatrick.vico.core.util.Point

/**
 * An extension of [DrawContext] that holds additional data required to render a [CartesianChart].
 */
public interface ChartDrawContext : DrawContext {
    /**
     * The bounds in which the [CartesianChart] will be drawn.
     */
    public val chartBounds: RectF

    /**
     * Holds information on the [CartesianChart]’s horizontal dimensions.
     */
    public val horizontalDimensions: HorizontalDimensions

    /**
     * The point inside the chart’s coordinates where physical touch is occurring.
     */
    public val markerTouchPoint: Point?

    /**
     * The current amount of horizontal scroll.
     */
    public val horizontalScroll: Float

    /**
     * The zoom factor.
     */
    public val zoom: Float
}

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun MeasureContext.getMaxScrollDistance(
    chartWidth: Float,
    horizontalDimensions: HorizontalDimensions,
): Float =
    (layoutDirectionMultiplier * (horizontalDimensions.getContentWidth(this) - chartWidth))
        .run { if (isLtr) coerceAtLeast(minimumValue = 0f) else coerceAtMost(maximumValue = 0f) }
        .ceil

internal fun ChartDrawContext.getMaxScrollDistance() = getMaxScrollDistance(chartBounds.width(), horizontalDimensions)
