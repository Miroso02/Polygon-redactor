import java.awt.*
import java.lang.NumberFormatException
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

val frame = JFrame()
var country = Country(ArrayList(), ArrayList(), Point(0, 0), "")
var bgImage : Image? = null
var pointAt00 = Point(0, 0)
var scale = 1.0
val countryName = JTextField()
val cords = JLabel("0 : 0")
val enableInput = JCheckBox("Enable drawing")
val moveAll = JCheckBox("Move all")
val countries = ArrayList<Country>()

var startPosition = Point(0, 0)
fun main()
{
    frame.title = "Polygon redactor"
    val menuBar = JMenuBar()
    val file = JMenu("File")
    menuBar.add(file)
    frame.jMenuBar = menuBar

    countryName.document.addDocumentListener(object : DocumentListener
    {
        override fun changedUpdate(e: DocumentEvent?) { setName() }
        override fun insertUpdate(e: DocumentEvent?) { setName() }
        override fun removeUpdate(e: DocumentEvent?) { setName() }
        private fun setName() { country.name = countryName.text }
    })
    countryName.preferredSize = Dimension(100, 20)

    val leftPanel = JPanel()
    leftPanel.layout = BorderLayout()
    leftPanel.add(countryName, BorderLayout.NORTH)
    leftPanel.add(initButtons(), BorderLayout.CENTER)
    leftPanel.add(initInfoPanel(), BorderLayout.SOUTH)
    frame.layout = BorderLayout()
    frame.add(leftPanel, BorderLayout.WEST)
    frame.add(Painter())
    frame.pack()

    frame.setSize(800, 600)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true
}

fun initButtons() : JPanel
{
    val buttonPanel = JPanel()

    val save = JButton("Save")
    save.addActionListener { savePolygon(country) }
    val loadBG = JButton("Load BG")
    loadBG.addActionListener { bgImage = loadBG(); frame.repaint() }
    val load = JButton("Load")
    load.addActionListener {
        country = loadPolygon()?: country
        if (!enableInput.isSelected && !countries.contains(country))
            countries.add(country)
        countryName.text = country.name
        frame.repaint()
    }
    val clear = JButton("Clear")
    clear.addActionListener { country.xPoints.clear(); country.yPoints.clear(); countryName.text = ""; frame.repaint() }
    val undo = JButton("Undo")
    undo.addActionListener { country.yPoints.removeLast(); country.xPoints.removeLast(); frame.repaint() }
    val new = JButton("New")
    new.addActionListener {
        country = Country(ArrayList(), ArrayList(), Point(0, 0), "")
        countryName.text = ""
        countries.add(country)
    }
    val saveAll = JButton("Save all")
    saveAll.addActionListener { countries.forEach { savePolygon(it) } }
    val loadAll = JButton("Load all")
    loadAll.addActionListener { loadAll(); frame.repaint() }
    val loadPanel = JPanel()
    loadPanel.layout = GridLayout(2, 2)
    for (elem in listOf(save, saveAll, load, loadAll))
    {
        elem.alignmentX = Component.CENTER_ALIGNMENT
        loadPanel.add(elem)
    }

    enableInput.addActionListener { frame.repaint() }
    buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.Y_AXIS)
    buttonPanel.background = Color.GRAY
    for (elem in listOf(clear, undo, loadBG, new))
    {
        elem.alignmentX = Component.CENTER_ALIGNMENT
        buttonPanel.add(Box.createRigidArea(Dimension(0, 5)))
        buttonPanel.add(elem)
    }
    buttonPanel.add(loadPanel)
    enableInput.background = Color.GRAY
    enableInput.alignmentX = Component.CENTER_ALIGNMENT
    moveAll.background = Color.GRAY
    moveAll.alignmentX = Component.CENTER_ALIGNMENT
    buttonPanel.add(enableInput)
    buttonPanel.add(moveAll)
    return buttonPanel
}

val zeroX = JTextField("0")
val zeroY = JTextField("0")
fun initInfoPanel() : JPanel
{
    cords.alignmentX = Component.CENTER_ALIGNMENT
    val editXY0Panel = JPanel()
    editXY0Panel.layout = GridLayout(2, 2)
    editXY0Panel.background = Color.CYAN
    editXY0Panel.add(JLabel("x", JLabel.CENTER))
    editXY0Panel.add(JLabel("y", JLabel.CENTER))
    editXY0Panel.add(zeroX)
    editXY0Panel.add(zeroY)

    val infoPanel = JPanel()
    infoPanel.background = Color.CYAN
    infoPanel.layout = BoxLayout(infoPanel, BoxLayout.Y_AXIS)
    infoPanel.add(Box.createVerticalGlue())
    val lbl = JLabel("0:0 point", JLabel.CENTER)
    infoPanel.add(lbl)
    lbl.alignmentX = Component.CENTER_ALIGNMENT
    infoPanel.add(editXY0Panel)
    val setXY0 = JButton("Set 0:0 point")
    setXY0.addActionListener {
        pointAt00.x = try { Integer.parseInt(zeroX.text) } catch (e: NumberFormatException) { zeroX.text = "0"; 0 }
        pointAt00.y = try { Integer.parseInt(zeroY.text) } catch (e: NumberFormatException) { zeroY.text = "0"; 0 }
        frame.repaint()
    }
    setXY0.alignmentX = Component.CENTER_ALIGNMENT
    infoPanel.add(setXY0)
    infoPanel.add(cords)

    return infoPanel
}