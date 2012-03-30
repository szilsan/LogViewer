package com.ftl.logview.gui.menu
import scala.swing.event.ButtonClicked
import scala.swing.Dialog
import scala.swing.Menu
import scala.swing.MenuItem

object HelpMenu extends Menu("Help") {
  contents += new MenuItem("About") {
    reactions += {
      case ButtonClicked(b) => {
        Dialog.showMessage(null, "LogViwer", "About")
      }
    }
  }
}