package com.ftl.logview.gui

import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.KeyStroke
import com.ftl.logview.ShortcutType

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

  var findKeyStroke: Option[KeyStroke] = None
  var findNextKeyStroke: Option[KeyStroke] = None

  // defaults
  // file menu
  def fileOpenMenuItem = Some(loadedFileOpenMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)))

  def propertyLoadMenuItem = Some(loadedPropertyLoadMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK)))
  def propertySaveMenuItem = Some(loadedPropertySaveMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK)))

  def exitMenuItem = Some(loadedExitMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK)))

  def tabCLoseMenuItem = Some(loadedTabCLoseMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK)))
  def allTabCLoseMenuItem = Some(loadedAllTabCLoseMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK)))

  // highlighted menu
  def highlightListMenuItem = Some(loadedHighlightListMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK)))
  def skippedListMenuItem = Some(loadedSkippedListMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK)))

  // help menu
  def helpMenuItem = Some(loadedHelpMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK)))
  def aboutMenuItem = Some(loadedAboutMenuItem.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK)))

  // shortcuts for panel
  def findShortCut = Some(findKeyStroke.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK)))
  def findNextShortcut = Some(findNextKeyStroke.getOrElse(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)))

  def setShortcut(shortcutType: String, shortcut: KeyStroke) {
    require(shortcut != null)

    val st = ShortcutType.values.find(_.toString == shortcutType)
    st match {
      case None => None
      case Some(s) =>
        s match {
          case ShortcutType.FIND => loadedFileOpenMenuItem = Some(shortcut)
        }
    }

  }

  /**
   * stringToProcess: you get this string from propeorty file (example: FIND = control F)
   */
  def changeKeyBinding(stringToProcess: String) {
    require(stringToProcess != null)

    val equalPosition = stringToProcess.indexOf("=")
    if (equalPosition == -1) {
      println("invalid property: " + stringToProcess)
      return ;
    }

    val stTypeText = stringToProcess.substring(0, equalPosition).trim.toUpperCase
    val stType = ShortcutType.values.find(_.toString == stTypeText)
    stType match {
      case None => println("Invalid shortcut type: " + stTypeText)
      case Some(s) =>
        s match {
          case ShortcutType.FIND => findKeyStroke = Some(KeyStroke.getKeyStroke(stringToProcess.substring(equalPosition + 1, stringToProcess.length)))
          case ShortcutType.FIND_NEXT => findNextKeyStroke = Some(KeyStroke.getKeyStroke(stringToProcess.substring(equalPosition + 1, stringToProcess.length)))
        }
    }

  }

  // http://tips4java.wordpress.com/2008/10/10/key-bindings/	
  def addKeyBinding(component: JComponent, keyBinding: String, action: AbstractAction, name: String) {
    require(component != null && keyBinding != null && action != null)

    val keyStroke = KeyStroke.getKeyStroke(keyBinding)
    component.getInputMap().put(keyStroke, name)
    component.getActionMap().put(name, action)
  }

  def addKeyBinding(component: JComponent, keyStroke: Option[KeyStroke], action: AbstractAction, name: String) {
    keyStroke match {
      case None => None
      case Some(s) =>
        component.getInputMap().put(s, name)
        component.getActionMap().put(name, action)
    }
  }

  def removeKeyBinding(component: JComponent, name: String) {
    if (component.getInputMap.keys != null) {
      component.getInputMap().keys.find(component.getInputMap.get(_) == name) match {
        case None => None
        case Some(s) =>
          component.getInputMap.remove(s)
          component.getActionMap.remove(name)
      }
    }
  }
}