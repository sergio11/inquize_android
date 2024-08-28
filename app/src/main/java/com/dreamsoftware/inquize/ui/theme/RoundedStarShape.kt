package com.dreamsoftware.inquize.ui.theme

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * A [Shape] that represents a RoundedStarShape ("curvy circle").
 *
 * @author https://github.com/pz64/RoundedStarShape-JetPack-Compose/tree/main
 */
class RoundedStarShape(
    private val sides: Int = 10,
    private val curve: Double = 0.08, // default from author was 0.09
    rotation: Float = 0f,
    iterations: Int = 360
) : Shape {

    private companion object {
        const val TWO_PI = 2 * PI
    }

    private val steps = (TWO_PI) / min(iterations, 360)

    // Note: Author did not negate the rotation value.
    // Without negating the rotation value, the animation
    // plays backwards when an appropriate rotation is given.
    // (Hint: remove negation and try applying rotation
    // to this composable and to another composable using
    // graphicsLayer { rotationZ = //your value in degrees }.
    private val rotationDegree = (PI / 180) * -rotation

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Generic(Path().apply {


        val r = min(size.height, size.width) * 0.4 * mapRange(1.0, 0.0, 0.5, 1.0, curve)

        val xCenter = size.width * .5f
        val yCenter = size.height * .5f

        moveTo(xCenter, yCenter)

        var t = 0.0

        while (t <= TWO_PI) {
            val x = r * (cos(t - rotationDegree) * (1 + curve * cos(sides * t)))
            val y = r * (sin(t - rotationDegree) * (1 + curve * cos(sides * t)))
            lineTo((x + xCenter).toFloat(), (y + yCenter).toFloat())

            t += steps
        }

        val x = r * (cos(t - rotationDegree) * (1 + curve * cos(sides * t)))
        val y = r * (sin(t - rotationDegree) * (1 + curve * cos(sides * t)))
        lineTo((x + xCenter).toFloat(), (y + yCenter).toFloat())

    })


    private fun mapRange(a: Double, b: Double, c: Double, d: Double, x: Double): Double {
        return (x - a) / (b - a) * (d - c) + c
    }
}