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
import com.ftl.logview.model.Skipped

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

  var skippedList = new ListBuffer[Skipped]
  var styles = Map.empty[String, Highlighted]

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
    val styleExpression = "[a-f|A-F|0-9]{6},[ ]*[a-f|A-F|0-9]{6},[ ]*(LINE|line|EXP|exp)[ ]*,[ ]*(.)*"
    val shortcutExpression = "[A-Z_]*[ ]*=[ ]*(control|alt)[ ]*[A-Z][ ]*"

    try {
      for (line <- Source.fromFile(propertyFile.get).getLines()) {
        commentExpression.r.findFirstMatchIn(line) match {
          case Some(s) => None
          case None => styleExpression.r.findFirstIn(line) match {
            case Some(s) => createStyle(s)
            case None => shortcutExpression.r.findFirstIn(line) match {
              case Some(s) => handleShortcut(s)
              case None => skippedExpression.r.findFirstIn(line) match {
                case None => None
                case Some(s) => skippedList += Skipped.createSkipped(s)
              }
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

    StyleUtil.addStyle(sc, highlighted.name, highlighted.fgColor, highlighted.bgColor)
    styles += (highlighted.name -> highlighted)
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
      // skipped expressions
      fw.write("[Skipped: line or expression skipped, expression]\n")
      skippedList.foreach(s => fw.write(s.toSave + "\n"))

      // styles
      fw.write("\n[Styles: background color, foreground color, line or expression highlighting, expression]\n")
      styles.keys.foreach(s => fw.write(styles.get(s).get.toSave + "\n"))

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