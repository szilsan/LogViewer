package com.ftl.logview.gui.menu
import java.awt.event.KeyEvent
import java.io.File

import scala.swing.event.ButtonClicked
import scala.swing.event.Key
import scala.swing.Action
import scala.swing.Dialog
import scala.swing.FileChooser
import scala.swing.Menu
import scala.swing.MenuItem
import scala.swing.Separator

import com.ftl.logview.gui.LogViewMainFrame
import com.ftl.logview.gui.Shortcuts
import com.ftl.logview.LogViewBundle

object FileMenu extends Menu("File") {
  mnemonic = Key.F

  contents += new MenuItem(new Action("Open log file") {
    mnemonic = KeyEvent.VK_O
    accelerator = Shortcuts.fileOpenMenuItem
    def apply() {}
  }) {
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

  contents += new MenuItem(new Action("Open properties file") {
    mnemonic = KeyEvent.VK_P
    accelerator = Shortcuts.propertyLoadMenuItem
    def apply() {}
  }) {
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
            bundle.get.refreshByProperties(file)
          }
        }
      }
    }
    tooltip = "Open style file and use on selected tab"
  }

  contents += new Separator

  contents += new MenuItem(new Action("Save properties") {
    mnemonic = KeyEvent.VK_S
    accelerator = Shortcuts.propertySaveMenuItem
    def apply() {}
  }) {
    reactions += {
      case ButtonClicked(b) =>
        val bundle = LogViewMainFrame.tabAndBundle.get(LogViewMainFrame.tabbedPane.selection.page)
        if (bundle.isDefined) {
          bundle.get.propertyFile match {
            case None =>
              val file = selectFileToSave()
              if (file != null) {
                Dialog.showMessage(this, bundle.get.propertiesSaving(file), "Info")
              }
            case Some(s) => Dialog.showMessage(this, bundle.get.propertiesSaving(s), "Info")
          }
        }
    }
  }

  contents += new MenuItem("Save properties as ...") {
    mnemonic = Key.A
    reactions += {
      case ButtonClicked(b) =>
        val file = selectFileToSave()
        if (file != null) {
          val bundle = LogViewMainFrame.tabAndBundle.get(LogViewMainFrame.tabbedPane.selection.page)
          if (bundle.isDefined) {
            Dialog.showMessage(this, bundle.get.propertiesSaving(file), "Info")
          }
        }
    }
  }

  contents += new Separator

  contents += new MenuItem(new Action("Close tab") {
    mnemonic = KeyEvent.VK_C
    accelerator = Shortcuts.tabCLoseMenuItem
    def apply() {}
  }) {
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

  contents += new MenuItem(new Action("Close all tab") {
    mnemonic = KeyEvent.VK_C
    accelerator = Shortcuts.allTabCLoseMenuItem
    def apply() {}
  }) {
    reactions += {
      case ButtonClicked(b) => {
        LogViewMainFrame.tabAndBundle = LogViewMainFrame.tabAndBundle.empty
        LogViewMainFrame.tabbedPane.pages.remove(0, LogViewMainFrame.tabbedPane.pages.length)
      }
    }
    tooltip = "Close all tabs"
  }

  contents += new Separator

  contents += new MenuItem(new Action("Exit") {
    mnemonic = KeyEvent.VK_X
    accelerator = Shortcuts.exitMenuItem
    def apply() {}
  }) {
    reactions += {
      case ButtonClicked(b) => {
        sys.exit(0)
      }
    }
    tooltip = "Exit"
  }

  def selectFileToSave(): File = {
    var fc = new FileChooser
    fc.showSaveDialog(this)
    fc.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
    fc.multiSelectionEnabled = false
    fc.selectedFile
  }

}