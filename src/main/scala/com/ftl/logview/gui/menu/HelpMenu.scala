package com.ftl.logview.gui.menu

import java.awt.event.KeyEvent

import scala.swing.event.ButtonClicked
import scala.swing.event.Key
import scala.swing.Action
import scala.swing.Dialog
import scala.swing.Menu
import scala.swing.MenuItem
import scala.swing.Separator

import com.ftl.logview.gui.Shortcuts

object HelpMenu extends Menu("Help") {

  mnemonic = Key.H

  contents += new MenuItem(new Action("Help") {
    mnemonic = KeyEvent.VK_H
    accelerator = Shortcuts.helpMenuItem
    def apply() {}
  }) {
    val helpMessage = "Shortcuts:\n" +
      "HOME - Jump to beginning of the line\n" +
      "END  - Jump to end of the line\n" +
      "CTRL + HOME - Jump to the beginning of the text\n" +
      "CTRL + END - Jump to the end of the text\n" +
      "CTRL + F - Find \n" +
      "CTRL + N - Find next\n"
    reactions += {
      case ButtonClicked(b) => {
        Dialog.showMessage(null, helpMessage, "Help")
      }
    }
  }

  contents += new Separator

  contents += new MenuItem(new Action("About") {
    mnemonic = KeyEvent.VK_A
    accelerator = Shortcuts.aboutMenuItem
    def apply() {}
  }) {
    val aboutMessage = """LogViewer written in Scala """"
    reactions += {
      case ButtonClicked(b) => {
        Dialog.showMessage(null, aboutMessage, "About")
      }
    }
  }
}