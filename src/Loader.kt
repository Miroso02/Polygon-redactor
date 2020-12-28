import java.awt.Image
import java.awt.Point
import java.io.*
import javax.imageio.ImageIO
import javax.swing.JFileChooser

private fun getFile(startDirectory: String) : File?
{
    val chooser = JFileChooser(File(startDirectory))
    val ret = chooser.showDialog(null, "Choose file")
    if (ret == JFileChooser.APPROVE_OPTION)
        return chooser.selectedFile
    return null
}

fun loadBG() : Image?
{   
    val file = getFile(".") ?: return null
    return ImageIO.read(file)
}

fun loadAll()
{
    val folder = File(".")
    val files = folder.listFiles { pathname -> pathname.name.endsWith(".txt") } ?: emptyArray()
    for (file in files)
    {
        if (file == null) continue
        val points = Array(2) { ArrayList<Int>() }
        val br = BufferedReader(InputStreamReader(file.inputStream()))
        val pt = Point(-1, 0)
        br.forEachLine {
            val values = it.split(" ")
            if (pt.x == -1)
            {
                pt.x = Integer.parseInt(values[0])
                pt.y = Integer.parseInt(values[1])
            }
            else
            {
                points[0].add(Integer.parseInt(values[0]))
                points[1].add(Integer.parseInt(values[1]))
            }
        }
        countries.add(Country(points[0], points[1], pt, file.name.takeWhile { it != '.' }))
    }
}

fun loadPolygon() : Country?
{
    val points = Array(2) { ArrayList<Int>() }
    val file = getFile("./Countries") ?: return null
    val br = BufferedReader(InputStreamReader(file.inputStream()))
    val pt = Point(-1, 0)
    br.forEachLine {
        val values = it.split(" ")
        if (pt.x == -1)
        {
            pt.x = Integer.parseInt(values[0])
            pt.y = Integer.parseInt(values[1])
        }
        else
        {
            points[0].add(Integer.parseInt(values[0]))
            points[1].add(Integer.parseInt(values[1]))
        }
    }
    return Country(points[0], points[1], pt, file.name.takeWhile { it != '.' })
}

fun savePolygon(country: Country)
{
    val minX = country.xPoints.reduce { acc, elem -> Integer.min(acc, elem) }
    val minY = country.yPoints.reduce { acc, elem -> Integer.min(acc, elem) }
    val writer = PrintWriter(FileOutputStream("${country.name}.txt"))
    writer.println("${minX + pointAt00.x} ${minY + pointAt00.y}")
    for (i in 0 until country.xPoints.size)
        writer.println("${country.xPoints[i] - minX} ${country.yPoints[i] - minY}")
    writer.close()
}