package com.ftl.logview.gui
import java.awt.Dimension

import scala.collection.mutable.Map
import scala.swing.BorderPanel
import scala.swing.MainFrame
import scala.swing.TabbedPane

import com.ftl.logview.gui.menu.MainMenu
import com.ftl.logview.LogViewBundle

/**
 * Main frame of the app GUI
 */
object LogViewMainFrame extends MainFrame {

  // to know which tab is connected to which bundle
  var tabAndBundle = Map.empty[TabbedPane.Page, LogViewBundle]
  
  title = "LogViewer"

  val windowSizeX = 800
  val windowSizeY = 600
  
  preferredSize = new Dimension(windowSizeX, windowSizeY)

  var tabbedPane = new TabbedPane

  contents = new BorderPanel {
    add(tabbedPane, BorderPanel.Position.Center)
  }

  menuBar = MainMenu

  pack

  centerOnScreen()
}