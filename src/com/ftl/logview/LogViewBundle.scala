package com.ftl.logview

import java.awt.Color
import java.io.File
import java.io.FileWriter
import java.io.IOException

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.Dialog

import com.ftl.logview.gui.LogViewFrame
import com.ftl.logview.logic.FileChangeWatcher

import javax.swing.text.StyleConstants
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

  var skippedList = new ListBuffer[String]
  var styles = Map.empty[String, String]
  if (!propertyFile.isEmpty) {
    propertiesLoading()
  }

  // watcher for log file changing
  def logFileWatcher = new FileChangeWatcher(logFile)

  // GUI
  val logViewFrame = new LogViewFrame(this)

  /**
   * Load properties
   */
  def propertiesLoading() {

    val commentExpression = """^[\[](.)*[\]]$"""
    val skippedExpression = "(.)*"
    val styleExpression = "[a-f|A-F|0-0]{6},[ ][a-f|A-F|0-0]{6},[ ](.)*"

    try {
      for (line <- Source.fromFile(propertyFile.get).getLines()) {

        commentExpression.r.findFirstMatchIn(line) match {
          case Some(s) => None
          case None => styleExpression.r.findFirstIn(line) match {
            case Some(s) => createStyle(s)
            case None => skippedExpression.r.findFirstIn(line) match {
              case None => None
              case Some(s) => skippedList += s
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

  def refreshByProperties(file: File) {
    require(file != null)

    propertyFile = Some(file)

    propertiesLoading()
    logViewFrame.refreshData
  }

  private def createStyle(str: String) {
    val parameters = str.trim().split(",")

    val fg: Color = new Color(Integer.parseInt(parameters(0).trim(), 16))
    val bg: Color = new Color(Integer.parseInt(parameters(1).trim(), 16))
    val exp = parameters(2).trim()

    StyleUtil.addStyle(sc, exp, fg, bg)
    styles += (exp -> exp)
  }

  def propertiesSaving(file: File) {
    require(file != null)

    propertyFile = new Some(file)

    try {
      val fw = new FileWriter(propertyFile.get)
      // skipped expressions
      fw.write("[Skipped]\n")
      skippedList.foreach(s => fw.write(s + "\n"))

      // styles
      fw.write("\n[Styles]\n")
      for (styleName <- styles.keys) {
        val style = sc.getStyle(styleName)
        fw.write(style.getAttribute(StyleConstants.Foreground).asInstanceOf[Color].getRGB().toHexString.substring(2) + ", " +
          style.getAttribute(StyleConstants.Background).asInstanceOf[Color].getRGB().toHexString.substring(2) + ", " +
          styleName + "\n")
      }
      Dialog.showMessage(null, "Property file is saved", "Info", Dialog.Message.Info)
      fw.close()
    } catch {
      case ioex: IOException => {
        Dialog.showMessage(null, "Error: " + ioex.getMessage(), "Error", Dialog.Message.Error)
        return
      }
    }
  }
}