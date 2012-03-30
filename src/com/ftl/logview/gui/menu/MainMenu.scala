package com.ftl.logview.gui.menu
import scala.swing.event.ButtonClicked
import scala.swing.Dialog
import scala.swing.FileChooser
import scala.swing.Menu
import scala.swing.MenuBar
import scala.swing.MenuItem
import scala.swing.Separator
import com.ftl.logview.LogViewBundle
import com.ftl.logview.gui.LogViewMainFrame

/**
 * Main app menu
 */
object MainMenu extends MenuBar {

  contents += FileMenu
  contents += HighlightMenu
  contents += HelpMenu

}