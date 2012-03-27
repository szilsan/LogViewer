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

class LogViewFrame extends MainFrame {

  val refreshText = "Automatic refresh"
  val refreshRate = 5

  val windowSizeX = 800
  val windowSizeY = 600

  var file: File = null
  val workingArea: TextArea = new TextArea

  val automaticRefreshCheckbox: CheckBox = new CheckBox

  val refreshTimer: Timer = new Timer(1000, new java.awt.event.ActionListener {
    var counter = 0
    def actionPerformed(e: java.awt.event.ActionEvent) {
      println(" timer" )

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

      listenTo(automaticRefreshCheckbox)

      reactions += {
        case ButtonClicked(`automaticRefreshCheckbox`) =>
          if (automaticRefreshCheckbox.selected) {
            refreshTimer.start()
          } else {
            createAutomaticRefreshCheckbox
            refreshTimer.stop()
          }
      }
    }
  }

  private def createAutomaticRefreshCheckbox: CheckBox = {
    automaticRefreshCheckbox.text = refreshText
    automaticRefreshCheckbox
  }

  private def refreshData {
    println("refresh")
    workingArea.text = ""
    for (line <- Source.fromFile(file).getLines()) {
      workingArea.append(line + "\n")
    }
  }
}