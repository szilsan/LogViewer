package com.ftl.logview.gui

import scala.swing.BorderPanel
import scala.swing.TabbedPane

import com.ftl.logview.LogViewBundle
import com.ftl.logview.LogViewMainFrame

class LogViewFrame(viewBundle: LogViewBundle) extends BorderPanel {

  // GUI
  // panels
  val logViewPanel = new LogViewPanel(viewBundle)
  val logViewSouthPanel = new LogViewSouthPanel(viewBundle)
  val title = viewBundle.logFile.getAbsolutePath()

  // GUI init
  createGui
  LogViewMainFrame.tabbedPane.pages += new TabbedPane.Page(title, this)
  
  // init data
  refreshData

  // refresh [tail] log data
  def refreshData = {
    logViewPanel.refreshData
  }
  
  // reload log file
  def reloadData = {
    logViewPanel.reloadData
  }

  // GUI
  private def createGui {
    add(new BorderPanel {

      add(logViewPanel, BorderPanel.Position.Center)
      add(logViewSouthPanel, BorderPanel.Position.South)

    }, BorderPanel.Position.Center)
  }

}