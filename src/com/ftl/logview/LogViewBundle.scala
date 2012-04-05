package com.ftl.logview

import java.awt.Color
import java.io.File
import java.io.IOException

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.Dialog

import com.ftl.logview.gui.LogViewFrame
import com.ftl.logview.logic.FileChangeWatcher

import javax.swing.text.StyleContext
import logic.StyleUtil

/**
 * Contains everything for one log file handling
 */
class LogViewBundle(lf: File, pf: Option[File]) {

  require(lf != null && lf != None)

  // files
  def logFile = lf
  def propertyFile = pf

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
    try {
      for (line <- Source.fromFile(propertyFile.get).getLines()) {
        if (line.startsWith("skip")) {
          skippedList += line.substring(line.indexOf("=") + 1).trim()
        }
        if (line.startsWith("style")) {
          var name = line.substring(0, line.indexOf("="))
          var parameters = line.substring(line.indexOf("=") + 1).trim().split(",")

          var fg: Color = new Color(Integer.parseInt(parameters(0).trim(), 16))
          var bg: Color = new Color(Integer.parseInt(parameters(1).trim(), 16))
          var exp = parameters(2).trim()

          StyleUtil.addStyle(sc, exp, fg, bg)
          styles += (exp -> exp)
        }
      }
    } catch {
      case ioex: IOException => {
        Dialog.showMessage(null, "Error: " + ioex.getMessage(), "Error", Dialog.Message.Error)
        return
      }
    }
  }

  def propertiesSaving(file: File) {
    require(file != null)

    try {
    	for (styleName <- styles.keys) {
    	  val style = sc.getStyle(styleName)

    	  
    	}
    } catch {
      case ioex: IOException => {
        Dialog.showMessage(null, "Error: " + ioex.getMessage(), "Error", Dialog.Message.Error)
        return
      }
    }
  }
}