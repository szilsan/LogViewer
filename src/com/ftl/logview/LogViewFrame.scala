package com.ftl.logview
import scala.swing.Frame
import scala.swing.Panel
import scala.swing.event.WindowClosing
import java.awt.Dimension
import java.io.File
import scala.io.Source
import scala.swing.TextArea
import scala.swing.GridBagPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.ScrollPane
import scala.swing.CheckBox
import scala.swing.BorderPanel
import scala.swing.FlowPanel
import scala.swing.event.ButtonClicked
import javax.swing.Timer
import java.io.RandomAccessFile
import scala.swing.Button
import scala.swing.Component
import javax.swing.JEditorPane
import scala.swing.Swing
import javax.swing.JTextPane
import javax.swing.text.StyleContext
import javax.swing.text.DefaultStyledDocument
import javax.swing.text.Style
import javax.swing.text.StyleConstants
import java.awt.Color
import java.io.IOException

class LogViewFrame extends MainFrame {

  val refreshText = "Automatic refresh [tail] "
  val refreshRate = 5

  val windowSizeX = 800
  val windowSizeY = 600

  // file handling
  var file: File = null
  var filePosition: Long = 0

  // for highlighting
  val sc = DocumentHandler.sc
  var doc: DefaultStyledDocument = new DefaultStyledDocument(sc);
  val editorPane: JTextPane = createTextPane

  // refresh
  val automaticRefreshCheckbox: CheckBox = new CheckBox
  val forceRefreshButton: Button = new Button("Force refresh")
  val refreshTimer: Timer = new Timer(1000, new java.awt.event.ActionListener {
    var counter = 0
    def actionPerformed(e: java.awt.event.ActionEvent) {
      if (counter / refreshRate == 1) {
        counter = 0
        refreshData
      } else {
        counter += 1
      }
      automaticRefreshCheckbox.text = refreshText + " in " + (refreshRate - counter)
    }
  })

  def this(fileToOpen: File) {
    this

    centerOnScreen
    listenTo(this)
    reactions += {
      case WindowClosing(e) => {
        println("Exiting...")
        System.exit(0)
      }
    }
    contents = createGui
    require(fileToOpen != null)
    file = fileToOpen
    title = "LogViewer - " + file.getAbsoluteFile()
    refreshData
  }

  private def refreshData {
    try {
      val raf: RandomAccessFile = new RandomAccessFile(file.getAbsoluteFile(), "r")
      raf.seek(filePosition)

      var line: String = null
      do {
        line = raf.readLine()
        if (line != null) {
          filePosition = raf.getFilePointer()
//          editorPane.setText(editorPane.getText() + line + "\n")
          doc.insertString(doc.getLength(), line+"\n", null)
        }
      } while (line != null)

      raf.close()
    } catch {
      case ioex: IOException => {
    	  
        return
      }
    }
    DocumentHandler.highlightText(doc, sc, editorPane.getText())
  }

  // GUI
  private def createGui: Panel = {
    new BorderPanel {

      add(createNorthArea, BorderPanel.Position.North)
      add(createCenterArea, BorderPanel.Position.Center)
      add(createSouthArea, BorderPanel.Position.South)

    }
  }

  private def createCenterArea: ScrollPane = {
    new ScrollPane(createTextPanel) {
      preferredSize = new Dimension(windowSizeX, windowSizeY)
      opaque = true
    }
  }

  private def createNorthArea: Panel = {
    new Panel {

    }
  }

  private def createSouthArea: Panel = {
    new FlowPanel {
      contents += createAutomaticRefreshCheckbox
      contents += forceRefreshButton

      listenTo(automaticRefreshCheckbox, forceRefreshButton)

      reactions += {
        case ButtonClicked(`automaticRefreshCheckbox`) =>
          if (automaticRefreshCheckbox.selected) {
            refreshTimer.start()
          } else {
            createAutomaticRefreshCheckbox
            refreshTimer.stop()
          }
        case ButtonClicked(`forceRefreshButton`) =>
          forceRefreshButton.enabled = false
          val isRunning = refreshTimer.isRunning()
          refreshTimer.stop()
          editorPane.setText("")
          filePosition = 0
          refreshData
          if (isRunning) {
            refreshTimer.start()
          }
          forceRefreshButton.enabled = true
      }
    }
  }

  private def createAutomaticRefreshCheckbox: CheckBox = {
    automaticRefreshCheckbox.text = refreshText
    automaticRefreshCheckbox
  }

  private def createForceRefreshButton: Button = {
    forceRefreshButton
  }

  private def createTextPanel: Component = new Component() {
    override lazy val peer: JEditorPane = editorPane
  }

  private def createTextPane: JTextPane = new JTextPane(doc) {
    setEditable(false)
    setBorder(Swing.EmptyBorder(5))
  }
}