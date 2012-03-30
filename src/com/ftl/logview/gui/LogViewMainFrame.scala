package com.ftl.logview.gui
import java.awt.Dimension

import scala.swing.BorderPanel
import scala.swing.MainFrame
import scala.swing.TabbedPane

/**
 * Main frame of the app GUI
 */
object LogViewMainFrame extends MainFrame {

  title = "LogViewer"

  val windowSizeX = 800
  val windowSizeY = 600
  preferredSize = new Dimension(windowSizeX, windowSizeY)

  var tabbedPane = new TabbedPane

  contents = new BorderPanel {
    add(tabbedPane, BorderPanel.Position.Center)
  }

  menuBar = new LogViewMenu

  pack

  centerOnScreen()
}