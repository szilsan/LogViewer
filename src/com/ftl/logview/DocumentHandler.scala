package com.ftl.logview
import javax.swing.text.DefaultStyledDocument
import javax.swing.text.StyleContext
import javax.swing.text.Style
import javax.swing.text.StyleConstants
import java.awt.Color
import scala.collection.mutable.ListBuffer
import scala.collection.Map
import scala.collection.immutable.List
import scala.collection.mutable.ArrayBuffer

// to handle document. Highlighting etc
object DocumentHandler {

  val sc: StyleContext = new StyleContext

  def highlightText(doc: DefaultStyledDocument, sc: StyleContext,styles: Map[String,String], text: String) {
    var pos: Int = 0
    text.split("\n").foreach(line => { highlightLine(doc, sc, styles, line, pos); pos += line.length() })
  }

  def highlightLine(doc: DefaultStyledDocument, sc: StyleContext,styles: Map[String,String], text: String, startPos: Int) {
    for (s<-styles.keys) {
    	var style = sc.getStyle(s)
    	collectPositions(text, styles(s)).foreach(pos => doc.setCharacterAttributes(startPos + pos(0), pos(1), style, false))
    }
  }

  /**
   * Collect positions of a findText in a text
   */
  def collectPositions(text: String, find: String): ArrayBuffer[Array[Int]] = {
    var positions: ArrayBuffer[Array[Int]] = new ArrayBuffer[Array[Int]]
    var exp = find.r
    var pos=0
    
    for (mi <-exp.findAllIn(text)) {
      // TODO multiple find in the same text
      var start = text.indexOf(mi, pos)
      positions += Array(start, mi.length())
    }
    
    positions
  }

  def configureStyle(style: Style, bg: Color, fg: Color) {
    if (fg != null)
      StyleConstants.setForeground(style, fg)
    if (bg != null)
      StyleConstants.setBackground(style, bg)
  }

  def deleteSkippedTexts(text: String, skipped: List[String]): String = {
    var sb:String = text
    
    for (s <- skipped) {
      var regexp = s.r
      sb = regexp.replaceAllIn(sb, "")
    }
  	sb
  }
}