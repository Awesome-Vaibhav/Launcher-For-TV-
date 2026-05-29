package com.example

import org.junit.Test
import java.awt.*
import java.awt.geom.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class IconGeneratorTest {

    @Test
    fun generateIcons() {
        println("=== STARTING ICON GENERATION TEST ===")
        val densities = mapOf(
            "mdpi" to 48,
            "hdpi" to 72,
            "xhdpi" to 96,
            "xxhdpi" to 144,
            "xxxhdpi" to 192
        )

        // The base path of the res folders is src/main/res relative to the app module
        val rawResDir = File("src/main/res")
        val resDir = if (rawResDir.exists()) rawResDir else File("app/src/main/res")
        println("Writing resources to directory: ${resDir.absolutePath}")

        densities.forEach { (density, size) ->
            val mipmapDir = File(resDir, "mipmap-$density")
            if (!mipmapDir.exists()) {
                mipmapDir.mkdirs()
                println("Created directory: ${mipmapDir.absolutePath}")
            }

            // 1. Standard (rect/squircle) launcher icon
            val normalImg = drawBeautifulLogo(size, false)
            val normalFile = File(mipmapDir, "ic_launcher.png")
            ImageIO.write(normalImg, "png", normalFile)

            // 2. Circular launcher icon
            val roundImg = drawBeautifulLogo(size, true)
            val roundFile = File(mipmapDir, "ic_launcher_round.png")
            ImageIO.write(roundImg, "png", roundFile)

            println("Generated launcher icons for $density ($size x $size px)")
        }

        println("=== ICON GENERATION COMPLETED SUCCESSFULLY ===")
    }

    private fun drawBeautifulLogo(size: Int, isRound: Boolean): BufferedImage {
        val img = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val g = img.createGraphics()

        // Enable extremely high quality rendering hints
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

        // 1. Circle Clip mask if circular round icon requested
        if (isRound) {
            val mask = Area(Ellipse2D.Double(0.0, 0.0, size.toDouble(), size.toDouble()))
            g.setClip(mask)
        }

        // 2. Dark Cosmic Base Gradient Background
        val bgGrad = GradientPaint(
            0f, 0f, Color.decode("#040609"),
            size.toFloat(), size.toFloat(), Color.decode("#0a0e17")
        )
        g.paint = bgGrad
        g.fillRect(0, 0, size, size)

        // 3. Main Glass Card/Tile (always a squircle centered in the icon)
        val margin = size * 0.04
        val tileWidth = size - (margin * 2)
        val tileRadius = size * 0.20
        val squircle = RoundRectangle2D.Double(margin, margin, tileWidth, tileWidth, tileRadius, tileRadius)

        // Dark glass tile interior
        val tileGrad = GradientPaint(
            0f, 0f, Color.decode("#0f131f"),
            size.toFloat(), size.toFloat(), Color.decode("#07090e")
        )
        g.paint = tileGrad
        g.fill(squircle)

        // Cyber Glowing border gradient from Neon Cyan to Neon Pink/Magenta
        val borderGrad = GradientPaint(
            0f, 0f, Color.decode("#00E5FF"),
            size.toFloat(), size.toFloat(), Color.decode("#FF007F")
        )
        g.paint = borderGrad
        g.stroke = BasicStroke((size * 0.015f).coerceAtLeast(1.0f))
        g.draw(squircle)

        // 4. Center Ambient Neon Backlight Glow
        val glowRadius = size * 0.25f
        val glowCenter = size * 0.5f
        val glowColors = floatArrayOf(0.0f, 1.0f)
        val glowColorValues = arrayOf(Color(16, 26, 45, 120), Color(0, 0, 0, 0))
        val glowPaint = RadialGradientPaint(
            glowCenter, glowCenter, glowRadius,
            glowColors, glowColorValues
        )
        g.paint = glowPaint
        g.fill(Ellipse2D.Double(
            (glowCenter - glowRadius).toDouble(), (glowCenter - glowRadius).toDouble(),
            (glowRadius * 2).toDouble(), (glowRadius * 2).toDouble()
        ))

        // 5. Drawing the Glowing Neon stylized 'A' arch!
        val leftStart = Point2D.Double(size * 0.34, size * 0.64)
        val apex = Point2D.Double(size * 0.50, size * 0.26)
        val rightEnd = Point2D.Double(size * 0.66, size * 0.64)

        // Curve paths for elegance
        val leftPath = Path2D.Double()
        leftPath.moveTo(leftStart.x, leftStart.y)
        leftPath.curveTo(size * 0.35, size * 0.44, size * 0.41, size * 0.26, apex.x, apex.y)

        val rightPath = Path2D.Double()
        rightPath.moveTo(apex.x, apex.y)
        rightPath.curveTo(size * 0.59, size * 0.26, size * 0.65, size * 0.44, rightEnd.x, rightEnd.y)

        // Left section: Cyan glow
        val blueBase = Color.decode("#00E5FF")
        val blueGlow1 = Color(0, 229, 255, 30)
        val blueGlow2 = Color(0, 229, 255, 95)

        g.color = blueGlow1
        g.stroke = BasicStroke(size * 0.12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g.draw(leftPath)
        g.color = blueGlow2
        g.stroke = BasicStroke(size * 0.07f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g.draw(leftPath)
        g.color = blueBase
        g.stroke = BasicStroke(size * 0.04f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g.draw(leftPath)
        g.color = Color.WHITE
        g.stroke = BasicStroke(size * 0.014f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g.draw(leftPath)

        // Right section: Magenta/Pink glow
        val pinkBase = Color.decode("#FF007F")
        val pinkGlow1 = Color(255, 0, 127, 30)
        val pinkGlow2 = Color(255, 0, 127, 95)

        g.color = pinkGlow1
        g.stroke = BasicStroke(size * 0.12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g.draw(rightPath)
        g.color = pinkGlow2
        g.stroke = BasicStroke(size * 0.07f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g.draw(rightPath)
        g.color = pinkBase
        g.stroke = BasicStroke(size * 0.04f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g.draw(rightPath)
        g.color = Color.WHITE
        g.stroke = BasicStroke(size * 0.014f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g.draw(rightPath)

        // 6. Planetary Orbit Ring (diagonal rotating ellipse acting as the middle of 'A')
        val originalTransform = g.transform
        g.translate(size * 0.5, size * 0.45)
        g.rotate(-18.0 * Math.PI / 180.0)

        val ringPath = Ellipse2D.Double(-size * 0.35, -size * 0.08, size * 0.70, size * 0.16)
        // Draw glow and center line of ring
        g.color = Color(0, 229, 255, 60)
        g.stroke = BasicStroke(size * 0.05f)
        g.draw(ringPath)
        g.color = Color(176, 0, 255, 150)
        g.stroke = BasicStroke(size * 0.02f)
        g.draw(ringPath)
        g.color = Color.WHITE
        g.stroke = BasicStroke(size * 0.008f)
        g.draw(ringPath)

        // Left Orbit planet
        val sphereLeft = Ellipse2D.Double(-size * 0.28, -size * 0.06, size * 0.06, size * 0.06)
        g.color = Color.decode("#00E5FF")
        g.fill(sphereLeft)
        g.color = Color.WHITE
        g.fill(Ellipse2D.Double(-size * 0.265, -size * 0.045, size * 0.03, size * 0.03))

        // Right Orbit planet
        val sphereRight = Ellipse2D.Double(size * 0.22, -size * 0.02, size * 0.055, size * 0.055)
        g.color = Color.decode("#FF007F")
        g.fill(sphereRight)
        g.color = Color.WHITE
        g.fill(Ellipse2D.Double(size * 0.232, -size * 0.008, size * 0.028, size * 0.028))

        // Reset rotation transform Matrix
        g.transform = originalTransform

        // 7. Twinkling 4-pointed Organic Sparkles
        fun drawStar(gx: Graphics2D, sx: Double, sy: Double, radius: Double, color: Color) {
            val starPath = GeneralPath()
            starPath.moveTo(sx, sy - radius)
            starPath.quadTo(sx, sy, sx + radius, sy)
            starPath.quadTo(sx, sy, sx, sy + radius)
            starPath.quadTo(sx, sy, sx - radius, sy)
            starPath.quadTo(sx, sy, sx, sy - radius)
            starPath.closePath()
            gx.color = color
            gx.fill(starPath)
            gx.color = Color.WHITE
            gx.fill(Ellipse2D.Double(sx - radius/4, sy - radius/4, radius/2, radius/2))
        }

        drawStar(g, size * 0.69, size * 0.27, size * 0.07, Color(0, 229, 255, 120))
        drawStar(g, size * 0.26, size * 0.35, size * 0.05, Color(255, 0, 127, 120))

        // Cyber floating light points
        g.color = Color(0, 229, 255, 200)
        g.fill(Ellipse2D.Double(size * 0.22, size * 0.58, size * 0.015, size * 0.015))
        g.color = Color(255, 0, 127, 200)
        g.fill(Ellipse2D.Double(size * 0.77, size * 0.42, size * 0.012, size * 0.012))

        // 8. Custom Centered "All Apps" Text
        val fontS = (size * 0.095f).coerceAtLeast(8.0f)
        val font = Font("SansSerif", Font.BOLD, fontS.toInt())
        g.font = font
        val fm = g.fontMetrics
        val textWidth = fm.stringWidth("All Apps")
        val textX = (size - textWidth) / 2
        val textY = size * 0.82f + fm.ascent / 2

        // Soft outer wording backlight
        g.color = Color(0, 229, 255, 100)
        g.drawString("All Apps", (textX - 1f).toFloat(), (textY - 1f).toFloat())
        g.color = Color(255, 0, 127, 100)
        g.drawString("All Apps", (textX + 1f).toFloat(), (textY + 1f).toFloat())

        // Linear neon text color source
        val textGrad = GradientPaint(
            textX.toFloat(), 0f, Color.decode("#00E5FF"),
            (textX + textWidth).toFloat(), 0f, Color.decode("#FF007F")
        )
        g.paint = textGrad
        g.drawString("All Apps", textX.toFloat(), textY.toFloat())

        g.dispose()
        return img
    }
}
