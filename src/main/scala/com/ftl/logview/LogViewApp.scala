package com.ftl.logview
import java.io.File

import scala.swing.SwingApplication

import com.ftl.logview.gui.LogViewMainFrame

/*
 * Here comes the sun... so this is the main object to start the app
 * 
 */
object LogViewApp extends SwingApplication {

  override def startup(args: Array[String]) {

    // argument is needed
    if (args.length == 0) {
      println("Usage: [log_file] properties_file ")
      sys.exit(0)
    }

    // log file
    val fLog = resourceFromUserDirectory(args(0))
    fileCheck(fLog)

    // properties file
    var fProperties: File = null
    if (args.length == 2) {
      fProperties = resourceFromUserDirectory(args(1))
      fileCheck(fProperties)
    }

    // create first log viewer
    new LogViewBundle(fLog, if (fProperties == null) None else Some(fProperties))

    // view
    LogViewMainFrame.visible = true
  }

  def resourceFromClassloader(path: String): java.net.URL =
    this.getClass.getResource(path)

  def resourceFromUserDirectory(path: String): java.io.File =
    new java.io.File(util.Properties.userDir, path)

  /**
   * Check whether the file is OK or not
   */
  private def fileCheck(f: File) {
    if (!f.exists()) {
      println("File does not exist! " + "File: " + f.getAbsoluteFile())
      sys.exit(-1)
    }

    if (!f.isFile()) {
      println("Parameter is not a file!" + "File: " + f.getAbsoluteFile())
      sys.exit(-1)
    }

    if (!f.canRead()) {
      println("File is not readable!" + "File: " + f.getAbsoluteFile())
      sys.exit(-1)
    }
  }
}