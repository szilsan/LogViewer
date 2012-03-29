package com.ftl.logview
import java.io.File

import scala.swing.SwingApplication

/*
 * Here comes the sun... so this is the main object to start the app
 * 
 */
object LogViewApp extends SwingApplication {

  var fLog: File = null
  var fProperties: File = null

  override def startup(args: Array[String]) {

    // argument is needed
    if (args.length == 0) {
      println("Usage: [log_file] properties_file ")
      exit(0)
    }

    // log file
    fLog = resourceFromUserDirectory(args(0))
    fileCheck(fLog)

    // properties file
    if (args.length == 2) {
      fProperties = resourceFromUserDirectory(args(1))
      fileCheck(fProperties)
    }

    // create first log viewer
    val firstLogTab = new LogViewBundle(fLog, fProperties)

    // view
    LogViewMainFrame.pack()
    LogViewMainFrame.centerOnScreen()
    LogViewMainFrame.visible=true
  }

  def resourceFromClassloader(path: String): java.net.URL =
    this.getClass.getResource(path)

  def resourceFromUserDirectory(path: String): java.io.File =
    new java.io.File(util.Properties.userDir, path)

  private def fileCheck(f: File) {
    if (!f.exists()) {
      println("File does not exist! " + "File: " + f.getAbsoluteFile())
      exit(-1)
    }

    if (!f.isFile()) {
      println("Parameter is not a file!" + "File: " + f.getAbsoluteFile())
      exit(-1)
    }

    if (!f.canRead()) {
      println("File is not readable!" + "File: " + f.getAbsoluteFile())
      exit(-1)
    }
  }
}