package com.ftl.logview
import javax.swing.text.DefaultStyledDocument
import javax.swing.text.StyleContext
import javax.swing.text.Style
import javax.swing.text.StyleConstants
import java.awt.Color
import scala.collection.mutable.ListBuffer

// to handle document. Highlighting etc
object DocumentHandler {

  val sc: StyleContext = new StyleContext

  val style1 = sc.addStyle("style1", null)
  configureStyle(style1, Color.BLUE, Color.RED)
  
  val style2 = sc.addStyle("style2", null)
  configureStyle(style2, Color.BLUE, null)
  

  def highlightText(doc: DefaultStyledDocument, sc: StyleContext, text: String) {
    var pos: Int = 0
    text.split("\n").foreach(line => { highlightLine(doc, sc, line, pos); pos += line.length() })
  }

  def highlightLine(doc: DefaultStyledDocument, sc: StyleContext, text: String, startPos: Int) {
    val st = "log"
    collectPositions(text, st).foreach(pos => doc.setCharacterAttributes(startPos + pos, st.length(), style1, false))
    
    val st2 = "ass"
    collectPositions(text,st2).foreach(pos => doc.setCharacterAttributes(startPos + pos, st2.length(), style2, false))

  }

  /**
   * Collect positions of a findText in a text
   */
  def collectPositions(text: String, find: String): ListBuffer[Int] = {
    var positions: ListBuffer[Int] = new ListBuffer[Int]
    var pos = 0

    do {
      pos = text.indexOf(find, pos)

      if (pos != -1) {
        positions += pos
        pos += find.length()
      }
    } while (pos != -1)
    positions
  }

  def configureStyle(style: Style, bg: Color, fg: Color) {
    if (fg != null)
      StyleConstants.setForeground(style, fg)
    if (bg != null)
      StyleConstants.setBackground(style, bg)
  }
}