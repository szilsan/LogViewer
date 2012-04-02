package com.ftl.logview.gui.menu
import scala.swing.Menu
import scala.swing.MenuItem
import scala.swing.event.ButtonClicked
import scala.swing.FileChooser
import com.ftl.logview.LogViewBundle
import com.ftl.logview.gui.LogViewMainFrame
import scala.swing.Dialog
import scala.swing.Separator

object FileMenu extends Menu("File") {
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
        val file = fc.selectedFile
        if (file != null) {
          val bundle = LogViewMainFrame.tabAndBundle.get(LogViewMainFrame.tabbedPane.selection.page)
          if (bundle.isDefined) {
            Dialog.showMessage(null, "Not implemented", "Info")
          }
        }
      }
    }
    tooltip = "Open style file and use on selected tab"
  }

  contents += new Separator

  contents += new MenuItem("Close tab") {
    reactions += {
      case ButtonClicked(b) => {
        if (LogViewMainFrame.tabAndBundle.nonEmpty) {
          var page = LogViewMainFrame.tabbedPane.selection
          LogViewMainFrame.tabAndBundle.remove(page.page)
          LogViewMainFrame.tabbedPane.pages.remove(page.index)
        } else {
          Dialog.showMessage(null, "There is no tab opened", "Warning", Dialog.Message.Warning)
        }
      }
    }
    tooltip = "Close selected tab"
  }

  contents += new MenuItem("Close all tab") {
    reactions += {
      case ButtonClicked(b) => {
        LogViewMainFrame.tabAndBundle = LogViewMainFrame.tabAndBundle.empty
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
    tooltip = "Exit"
  }
}