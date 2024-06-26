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

package com.patrykandpatrick.vico.core.scroll

import android.graphics.RectF
import androidx.annotation.RestrictTo
import com.patrykandpatrick.vico.core.chart.CartesianChart
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.scroll.Scroll.Absolute
import com.patrykandpatrick.vico.core.scroll.Scroll.Relative

/** Represents a [CartesianChart] scroll value or delta. */
public sealed interface Scroll {
    /** Represents a [CartesianChart] scroll value. */
    public fun interface Absolute : Scroll {
        /** Returns the scroll value. */
        public fun getValue(
            context: MeasureContext,
            horizontalDimensions: HorizontalDimensions,
            bounds: RectF,
            maxValue: Float,
        ): Float

        /** Houses [Scroll.Absolute] singletons and factory functions. */
        public companion object {
            /** Corresponds to zero. */
            public val Start: Absolute = Absolute { _, _, _, _ -> 0f }

            /** Corresponds to the maximum scroll value. */
            public val End: Absolute = Absolute { _, _, _, maxValue -> maxValue }

            /** Uses a scroll value of the specified number of pixels. */
            public fun pixels(pixels: Float): Absolute = Absolute { _, _, _, _ -> pixels }

            /**
             * Scrolls to the specified _x_ coordinate, positioning it anywhere between the start edge ([bias] = 0) and
             * the end edge ([bias] = 1) of the [CartesianChart].
             */
            public fun x(
                x: Float,
                bias: Float = 0f,
            ): Absolute =
                Absolute { context, horizontalDimensions, bounds, _ ->
                    horizontalDimensions.startPadding + (x - context.chartValues.minX) / context.chartValues.xStep *
                        horizontalDimensions.xSpacing - bias * bounds.width()
                }
        }
    }

    /** Represents a [CartesianChart] scroll delta. */
    public fun interface Relative : Scroll {
        /** Returns the scroll delta. */
        public fun getDelta(
            context: MeasureContext,
            horizontalDimensions: HorizontalDimensions,
            bounds: RectF,
            maxValue: Float,
        ): Float

        /** Houses [Scroll.Relative] factory functions. */
        public companion object {
            /** Scrolls by the specified number of pixels. */
            public fun pixels(pixels: Float): Relative = Relative { _, _, _, _ -> pixels }

            /** Scrolls by the specified number of _x_ units. */
            public fun x(x: Float): Relative =
                Relative { context, horizontalDimensions, _, _ ->
                    x / context.chartValues.xStep * horizontalDimensions.xSpacing
                }
        }
    }
}

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun Scroll.getDelta(
    context: MeasureContext,
    horizontalDimensions: HorizontalDimensions,
    bounds: RectF,
    maxValue: Float,
    value: Float,
): Float =
    when (this) {
        is Absolute -> getValue(context, horizontalDimensions, bounds, maxValue) - value
        is Relative -> getDelta(context, horizontalDimensions, bounds, maxValue)
    }
