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

class LogViewFrame extends MainFrame {

  val refreshText = "Automatic refresh [tail] "
  val refreshRate = 5

  val windowSizeX = 800
  val windowSizeY = 600

  var file: File = null
  var filePosition: Long = 0

  val workingArea: TextArea = new TextArea

  val automaticRefreshCheckbox: CheckBox = new CheckBox
  val forceRefreshButton: Button = new Button("Force refresh")

  val refreshTimer: Timer = new Timer(1000, new java.awt.event.ActionListener {
    var counter = 0
    def actionPerformed(e: java.awt.event.ActionEvent) {
      println(" timer")

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

    println("File: " + file.getAbsoluteFile())

    refreshData

  }

  private def createGui: Panel = {
    new BorderPanel {

      add(createNorthArea, BorderPanel.Position.North)
      add(createCenterArea, BorderPanel.Position.Center)
      add(createSouthArea, BorderPanel.Position.South)

    }
  }

  private def createCenterArea: ScrollPane = {
    new ScrollPane(workingArea) {
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
          val isRunning = refreshTimer.isRunning()
          refreshTimer.stop()
          workingArea.text = ""
          filePosition = 0
          refreshData
          if (isRunning) {
            refreshTimer.start()
          }
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

  private def refreshData {
    val raf: RandomAccessFile = new RandomAccessFile(file.getAbsoluteFile(), "r")
    raf.seek(filePosition)

    var line: String = null
    do {
      line = raf.readLine()
      if (line != null) {
        workingArea.append(line + "\n")
      }
    } while (line != null)

    filePosition = raf.getFilePointer()
    raf.close()
  }
}