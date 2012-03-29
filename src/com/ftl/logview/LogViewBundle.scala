package com.ftl.logview
import java.io.File
import java.nio.file.Path
import java.nio.file.FileSystems
import com.ftl.logview.logic.FileChangeWatcher
import javax.swing.text.StyleContext

/**
 * Contains everything for one log file handling
 */
class LogViewBundle(lf:File, pf:File) {

  // files
  def logFile = lf
  def propertyFile = pf
  
  // style context for styles
  def sc = new StyleContext
  
  // watcher for log file changing
  def logFileWatcher = new FileChangeWatcher(logFile)
  
  // GUI
  def LogViewFrame = new LogViewFrame
  
  
}