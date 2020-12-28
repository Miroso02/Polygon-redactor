import java.awt.*
import java.awt.event.*
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.math.pow

class Painter : JPanel()
{
    init
    {
        background = Color.LIGHT_GRAY
        addMouseListener(object : MouseAdapter()
        {
            override fun mouseClicked(e: MouseEvent?)
            {
                if(e == null) return
                if (enableInput.isSelected) {
                    country.addPoint(e.point - pointAt00)
                    frame.repaint()
                } else {
                    countries.forEach { if (it.mouseOn(e.point)) { country = it; countryName.text = it.name } }
                }
            }

            override fun mousePressed(e: MouseEvent?) {
                startPosition = e?.point!!
            }
        })
        addMouseMotionListener(object: MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent?) {
                if (e == null) return
                cords.text = "${e.point.x * scale - pointAt00.x} : ${e.point.y * scale - pointAt00.y} ${country.mouseOn(e.point)}"
            }
            override fun mouseDragged(e: MouseEvent?)
            {
                if (e == null) return
                if (SwingUtilities.isRightMouseButton(e))
                {
                    pointAt00.x += e.point.x - startPosition.x
                    pointAt00.y += e.point.y - startPosition.y
                    zeroX.text = pointAt00.x.toString()
                    zeroY.text = pointAt00.y.toString()
                }
                if (SwingUtilities.isLeftMouseButton(e) && !enableInput.isSelected)
                {
                    if (moveAll.isSelected)
                        countries.forEach { it.translate(Point(e.point.x - startPosition.x, e.point.y - startPosition.y)) }
                    else
                        country.translate(Point(e.point.x - startPosition.x, e.point.y - startPosition.y))
                }
                startPosition = e.point
                frame.repaint()
            }
        })
        addMouseWheelListener {
            scale *= 2.0.pow(-it.wheelRotation.toDouble())
            frame.repaint()
        }
    }
    override fun paintComponent(g: Graphics?)
    {
        val g2d = g as Graphics2D
        if (bgImage != null)
        {
            g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f)
            g.drawImage(bgImage, pointAt00.x, pointAt00.y, null)
            g.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)
        }
        country.display(g)
        if (enableInput.isSelected)
            g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f)
        countries.forEach { it.display(g) }
        g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)
    }
}