package com.ftl.logview

import java.io.File
import java.io.FileWriter
import java.io.IOException

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.Dialog

import com.ftl.logview.gui.LogViewFrame
import com.ftl.logview.gui.Shortcuts
import com.ftl.logview.logic.FileChangeWatcher
import com.ftl.logview.model.Highlighted

import javax.swing.text.StyleContext
import logic.StyleUtil

/**
 * Contains everything for one log file handling
 */
class LogViewBundle(lf: File, pf: Option[File]) {

  require(lf != null && lf != None)

  // files
  def logFile = lf
  var propertyFile = pf

  var sc = new StyleContext

  var styles = ListBuffer.empty[Highlighted]

  propertyFile match {
    case None => None
    case Some(s) => propertiesLoading
  }

  // watcher for log file changing
  def logFileWatcher = new FileChangeWatcher(logFile)

  // GUI
  val logViewFrame = new LogViewFrame(this)

  /**
   * Load properties. No refresh
   */
  def propertiesLoading() {
    val commentExpression = """^[\[](.)*[\]]$"""
    val skippedExpression = "[ ]*(LINE|line|EXP|exp)[ ]*,[ ]*(.)+"
    val styleExpression = "[a-f|A-F|0-9]{6},[ ]*[a-f|A-F|0-9]{6},[ ]*(LINE|line|EXP|exp)[ ]*,[ ]*(SKIPPED|skipped|HIGHLIGHTED|highlighted)[ ]*,[ ]*(.)*"
    val shortcutExpression = "[A-Z_]*[ ]*=[ ]*(control|alt)[ ]*[A-Z][ ]*"

    try {
      for (line <- Source.fromFile(propertyFile.get).getLines()) {
        commentExpression.r.findFirstMatchIn(line) match {
          case Some(s) => None
          case None => styleExpression.r.findFirstIn(line) match {
            case Some(s) => createStyle(s)
            case None => shortcutExpression.r.findFirstIn(line) match {
              case Some(s) => handleShortcut(s)
              case None => None
            }
          }
        }
      }
    } catch {
      case ioex: IOException => {
        Dialog.showMessage(null, "Error: " + ioex.getMessage(), "Error", Dialog.Message.Error)
        return
      }
    }
  }

  /**
   * Reload properties and refresh the view
   */
  def refreshByProperties(file: File) {
    require(file != null)

    propertyFile = Some(file)

    propertiesLoading()
    logViewFrame.refreshData
  }

  /**
   * Set shortcut
   */
  private def handleShortcut(str: String) {
    Shortcuts.changeKeyBinding(str)
  }

  /**
   * Create a style from the specified string
   */
  private def createStyle(str: String) {
    val highlighted = Highlighted.createHighlighted(str)

    highlighted match {
      case None => println("Style can not be created: " + str)
      case Some(s) =>
        StyleUtil.addStyle(sc, s.exp, s.fgColor, s.bgColor)
        styles += s
    }
  }

  /**
   * Save properties into the given file
   */
  def propertiesSaving(file: File): String = {
    require(file != null)

    propertyFile = new Some(file)
    var message = ""

    val fw = new FileWriter(propertyFile.get)
    try {
      // styles
      fw.write("\n[Styles: background color, foreground color, line or expression highlighting, skipped or highlighted, expression]\n")
      styles.foreach(s => fw.write(s.toSave + "\n"))

      // shortcuts
      fw.write("\n[Shortcuts]")
      // TODO

      fw.write("\n[Log pattern]")
      // TODO

      message = "Property file is saved"
    } catch {
      case ioex: IOException => {
        message = "Error:" + ioex.getMessage()
      }
    } finally {
      fw.close()
    }
    message
  }
}