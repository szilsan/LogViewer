package com.ftl.logview
import scala.swing.SwingApplication
import java.awt.Dimension
import java.io.File

/*
 * Here comes the sun... so the main objevct to start the app
 * 
 */
object LogViewApp extends SwingApplication {

  override def startup(args: Array[String]) {

    // argument is needed
    if (args.length == 0) {
      println("Usage: parameter[1] = log file")
      exit(0)
    }

    // open file
    var f: File = resourceFromUserDirectory(args(0))

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

    // show
    val t = new LogViewFrame(f)
    if (t.size == new Dimension(0, 0)) t.pack()
    t.visible = true
  }
  
  def resourceFromClassloader(path: String): java.net.URL =
    this.getClass.getResource(path)

  def resourceFromUserDirectory(path: String): java.io.File =
    new java.io.File(util.Properties.userDir, path)
}