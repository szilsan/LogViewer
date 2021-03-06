package com.ftl.logview.gui

import scala.swing.BorderPanel
import scala.swing.TabbedPane
import com.ftl.logview.LogViewBundle
import scala.swing.event.WindowClosing

class LogViewFrame(viewBundle: LogViewBundle) extends BorderPanel {

  // GUI
  // panels
  val logViewPanel = new LogViewPanel(viewBundle)
  val logViewSouthPanel = new LogViewSouthPanel(viewBundle)

  // GUI init
  createGui
  var page = new TabbedPane.Page(viewBundle.logFile.getName(), this, viewBundle.logFile.getAbsolutePath())
  LogViewMainFrame.tabAndBundle.put(page, viewBundle)

  LogViewMainFrame.tabbedPane.pages += page

  // init data
  refreshData

  logViewPanel.requestFocus

  listenTo(this)
  reactions += {
    case WindowClosing(e) => {
      println("Exiting...")
      System.exit(0)
    }
  }

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