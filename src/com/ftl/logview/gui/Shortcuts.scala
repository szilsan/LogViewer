package com.ftl.logview.gui
import java.awt.event.InputEvent
import javax.swing.KeyStroke
import java.awt.event.KeyEvent

object Shortcuts {

  var loadedFileOpenMenuItem: Option[KeyStroke] = None
  var loadedPropertyLoadMenuItem: Option[KeyStroke] = None
  var loadedPropertySaveMenuItem: Option[KeyStroke] = None
  var loadedExitMenuItem: Option[KeyStroke] = None
  var loadedTabCLoseMenuItem: Option[KeyStroke] = None
  var loadedAllTabCLoseMenuItem: Option[KeyStroke] = None
  var loadedHighlightListMenuItem: Option[KeyStroke] = None
  var loadedSkippedListMenuItem: Option[KeyStroke] = None
  var loadedHelpMenuItem: Option[KeyStroke] = None
  var loadedAboutMenuItem: Option[KeyStroke] = None

  // defaults
  // file menu
  def fileOpenMenuItem = Some(loadedFileOpenMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)))

  def propertyLoadMenuItem = Some(loadedPropertyLoadMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK)))
  def propertySaveMenuItem = Some(loadedPropertySaveMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK)))

  def exitMenuItem = Some(loadedExitMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK)))

  def tabCLoseMenuItem = Some(loadedTabCLoseMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK)))
  def allTabCLoseMenuItem = Some(loadedAllTabCLoseMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK)))

  // highlighted menu
  def highlightListMenuItem = Some(loadedHighlightListMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK)))
  def skippedListMenuItem = Some(loadedSkippedListMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK)))

  // help menu
  def helpMenuItem = Some(loadedHelpMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK)))
  def aboutMenuItem = Some(loadedAboutMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK)))

  def setShortcut(shortcutType: String, shortcut: KeyStroke) {
    require(shortcut != null)

    val st = ShortcutType.values.find(_.toString == shortcutType)
    st match {
      case None => None
      case Some(s) =>
        s match {
          case ShortcutType.LOG_FILE_OPEN => loadedFileOpenMenuItem = Some(shortcut)
        }
    }

  }

  object ShortcutType extends Enumeration {
    val LOG_FILE_OPEN, PROPERTY_FILE_OPEN = Value

  }

}