package com.ftl.logview.gui
import scala.swing.event.ButtonClicked
import scala.swing.Menu
import scala.swing.MenuBar
import scala.swing.MenuItem
import scala.swing.Dialog
import scala.swing.Separator
import com.ftl.logview.LogViewBundle
import scala.swing.FileChooser
import java.io.File

/**
 * Main app menu
 */
class LogViewMenu extends MenuBar {

  val fileMenu = new Menu("File") {

    contents += new MenuItem("Open log file") {
      reactions += {
        case ButtonClicked(b) => {
          var fc = new FileChooser
          fc.showOpenDialog(this)
          fc.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
          fc.multiSelectionEnabled = false
          var file = fc.selectedFile
          if (file != null) {
        	  new LogViewBundle(file, None)
          }
        }
      }
      tooltip = "Open new log file in a new tab"
    }
    
     contents += new MenuItem("Open style file") {
      reactions += {
        case ButtonClicked(b) => {
          var fc = new FileChooser
          fc.showOpenDialog(this)
          fc.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
          fc.multiSelectionEnabled = false
          var file = fc.selectedFile
          if (file != null) {
            // TODO style
          }
        }
      }
      tooltip = "Open style file and use on selected tab"
    }

    contents += new Separator

    contents += new MenuItem("Close tab") {
      reactions += {
        case ButtonClicked(b) => {
          LogViewMainFrame.tabbedPane.pages.remove(LogViewMainFrame.tabbedPane.selection.index)
        }
      }
      tooltip = "Close selected tab"
    }

    contents += new MenuItem("Close all tab") {
      reactions += {
        case ButtonClicked(b) => {
          LogViewMainFrame.tabbedPane.pages.remove(0, LogViewMainFrame.tabbedPane.pages.length)
        }
      }
      tooltip = "Close all tabs"
    }

    contents += new Separator

    contents += new MenuItem("Exit") {
      reactions += {
        case ButtonClicked(b) => {
          exit(0)
        }
      }
    }

    tooltip = "Exit"

  }

  val helpMenu = new Menu("Help") {
    contents += new MenuItem("About") {
      reactions += {
        case ButtonClicked(b) => {
          Dialog.showMessage(null, "LogViwer", "About")
        }
      }
    }
  }

  contents += fileMenu
  contents += helpMenu

}