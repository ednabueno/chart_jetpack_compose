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

package com.patrykandpatrick.vico.sample.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.decoration.rememberHorizontalBox
import com.patrykandpatrick.vico.compose.chart.decoration.rememberHorizontalLine
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.toDynamicShader
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.theme.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.theme.vicoTheme
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.ComponentShader
import com.patrykandpatrick.vico.core.component.text.VerticalPosition
import com.patrykandpatrick.vico.core.model.CartesianChartModel
import com.patrykandpatrick.vico.core.model.ColumnCartesianLayerModel

private val model = CartesianChartModel(ColumnCartesianLayerModel.build { series(1, 2, 3, 4) })

public val Color.Companion.DimmedGray: Color
    get() = Color(0xFFAAAAAA)

@Composable
private fun ProvidePreviewVicoTheme(content: @Composable () -> Unit) {
    Surface(
        color = Color.Transparent,
        modifier =
            Modifier
                .background(color = Color.LightGray, shape = RoundedCornerShape(size = 4.dp))
                .padding(8.dp),
    ) {
        ProvideVicoTheme(
            vicoTheme.copy(
                cartesianLayerColors = listOf(Color.DimmedGray),
                lineColor = Color.DimmedGray,
                textColor = Color.DimmedGray,
            ),
            content,
        )
    }
}

@Preview(widthDp = 250)
@Composable
public fun ThresholdLine() {
    ProvidePreviewVicoTheme {
        CartesianChartHost(
            modifier = Modifier,
            chart =
                rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                    decorations =
                        listOf(
                            rememberHorizontalLine(
                                y = { 2f },
                                line = rememberLineComponent(color = Color.Black, thickness = 2.dp),
                                labelComponent =
                                    rememberTextComponent(Color.Black, padding = dimensionsOf(horizontal = 8.dp)),
                            ),
                        ),
                ),
            model = model,
            scrollState = rememberVicoScrollState(scrollEnabled = false),
        )
    }
}

@Preview(widthDp = 250)
@Composable
public fun ThresholdLineWithCustomText() {
    ProvidePreviewVicoTheme {
        CartesianChartHost(
            modifier = Modifier,
            chart =
                rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    decorations =
                        listOf(
                            rememberHorizontalLine(
                                y = { 2f },
                                line = rememberLineComponent(color = Color.Black, thickness = 2.dp),
                                labelComponent =
                                    rememberTextComponent(
                                        color = Color.White,
                                        lineCount = 3,
                                        background =
                                            rememberShapeComponent(
                                                shape =
                                                    Shapes.roundedCornerShape(
                                                        bottomLeftPercent = 25,
                                                        bottomRightPercent = 25,
                                                    ),
                                                color = Color.Black,
                                            ),
                                        padding =
                                            dimensionsOf(
                                                start = 8.dp,
                                                top = 2.dp,
                                                end = 8.dp,
                                                bottom = 4.dp,
                                            ),
                                        margins = dimensionsOf(horizontal = 4.dp),
                                    ),
                                label = { "Horizontal line 1 📐" },
                                verticalLabelPosition = VerticalPosition.Bottom,
                            ),
                            rememberHorizontalLine(
                                y = { 3f },
                                line = rememberLineComponent(color = Color.DarkGray, thickness = 2.dp),
                                labelComponent =
                                    rememberTextComponent(
                                        color = Color.White,
                                        lineCount = 3,
                                        background =
                                            rememberShapeComponent(
                                                shape =
                                                    Shapes.cutCornerShape(
                                                        topLeftPercent = 25,
                                                        topRightPercent = 25,
                                                    ),
                                                color = Color.DarkGray,
                                            ),
                                        padding =
                                            dimensionsOf(
                                                start = 8.dp,
                                                top = 4.dp,
                                                end = 8.dp,
                                                bottom = 2.dp,
                                            ),
                                        margins = dimensionsOf(horizontal = 4.dp),
                                    ),
                                label = { "Horizontal line 2 📐" },
                            ),
                        ),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                ),
            model = model,
            scrollState = rememberVicoScrollState(scrollEnabled = false),
        )
    }
}

@Preview(widthDp = 250)
@Composable
public fun RangedThresholdLine() {
    ProvidePreviewVicoTheme {
        CartesianChartHost(
            modifier = Modifier,
            chart =
                rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    decorations =
                        listOf(
                            rememberHorizontalBox(
                                y = { 2f..3f },
                                box = rememberShapeComponent(color = Color.Black.copy(alpha = .5f)),
                                labelComponent =
                                    rememberTextComponent(
                                        color = Color.Black,
                                        padding = dimensionsOf(horizontal = 8.dp),
                                    ),
                            ),
                        ),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                ),
            model = model,
            scrollState = rememberVicoScrollState(scrollEnabled = false),
        )
    }
}

@Preview(widthDp = 250)
@Composable
public fun RangedThresholdLineWithBrushShader() {
    ProvidePreviewVicoTheme {
        CartesianChartHost(
            modifier = Modifier,
            chart =
                rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    decorations =
                        listOf(
                            rememberHorizontalBox(
                                y = { 2f..3f },
                                box =
                                    rememberShapeComponent(
                                        color = Color.Black,
                                        dynamicShader =
                                            Brush.verticalGradient(
                                                colors =
                                                    listOf(
                                                        Color.Black.copy(0.75f),
                                                        Color.Black.copy(0.25f),
                                                    ),
                                            ).toDynamicShader(),
                                    ),
                                labelComponent =
                                    rememberTextComponent(
                                        color = Color.Black,
                                        padding = dimensionsOf(horizontal = 8.dp),
                                    ),
                            ),
                        ),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                ),
            model = model,
            scrollState = rememberVicoScrollState(scrollEnabled = false),
        )
    }
}

@Preview(widthDp = 250)
@Composable
public fun RangedThresholdLineWithComponentShader() {
    ProvidePreviewVicoTheme {
        CartesianChartHost(
            modifier = Modifier,
            chart =
                rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    decorations =
                        listOf(
                            rememberHorizontalBox(
                                y = { 2f..3f },
                                box =
                                    rememberShapeComponent(
                                        color = Color.Black,
                                        dynamicShader =
                                            ComponentShader(
                                                rememberShapeComponent(shape = Shapes.pillShape, color = Color.Black),
                                                componentSizeDp = 4f,
                                            ),
                                        strokeWidth = 2.dp,
                                        strokeColor = Color.Black,
                                    ),
                                labelComponent =
                                    rememberTextComponent(
                                        color = Color.Black,
                                        padding = dimensionsOf(horizontal = 8.dp),
                                    ),
                            ),
                        ),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                ),
            model = model,
            scrollState = rememberVicoScrollState(scrollEnabled = false),
        )
    }
}
