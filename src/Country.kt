import java.awt.Graphics
import java.awt.Point
import java.lang.ArithmeticException

class Country(val xPoints: ArrayList<Int>, val yPoints: ArrayList<Int>, position: Point, var name: String)
{
    init
    {
        translate(position)
    }

    fun display(g: Graphics?)
    {
        g?.drawPolygon(xPoints.map { (scale * it).toInt() + pointAt00.x }.toIntArray(),
                        yPoints.map { (scale * it).toInt() + pointAt00.y }.toIntArray(),
                        xPoints.size)
    }
    fun mouseOn(point: Point) = checkPointInPolygon(this.xPoints, this.yPoints, point)
    fun addPoint(point: Point)
    {
        xPoints.add(point.x)
        yPoints.add(point.y)
    }
    fun translate(transl : Point)
    {
        for (i in xPoints.indices)
        {
            xPoints[i] += transl.x
            yPoints[i] += transl.y
        }
    }
    fun scale(k: Double)
    {
        for (i in xPoints.indices)
        {
            xPoints[i] = (xPoints[i] * k).toInt()
            yPoints[i] = (yPoints[i] * k).toInt()
        }
    }
    fun zoom(p: Point, k: Double)
    {
        translate(-p)
        scale(k)
        translate(p)
    }
}

fun checkPointInPolygon(xPoints: ArrayList<Int>, yPoints: ArrayList<Int>, point: Point): Boolean
{
    var intersectAmount = 0
    for (i in yPoints.indices)
    {
        val x1 = xPoints[i]
        val y1 = yPoints[i]
        val x2 = xPoints[if (i + 1 < xPoints.size) i + 1 else 0]
        val y2 = yPoints[if (i + 1 < yPoints.size) i + 1 else 0]
        val xp = point.x - pointAt00.x
        val yp = point.y - pointAt00.y

        val xInt = try {
            x2 + (yp - y2) * (x1 - x2) / (y1 - y2)
        } catch (e: ArithmeticException) { 10000 }

        if (yp in Integer.min(y1, y2)..Integer.max(y1, y2) && xp < xInt)
            intersectAmount++
    }
    return intersectAmount % 2 == 1
}

operator fun Point.unaryMinus() = Point(-x, -y)
operator fun Point.times(k: Double) = Point((x * k).toInt(), (y * k).toInt())
operator fun Point.plus(p: Point) = Point(x + p.x, y + p.y)
operator fun Point.minus(p: Point) = Point(x - p.x, y - p.y)