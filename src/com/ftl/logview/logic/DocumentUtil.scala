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
import com.ftl.logview.logic.TextUtil

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
    	TextUtil.collectPositions(text, styles(s)).foreach(pos => doc.setCharacterAttributes(startPos + pos(0), pos(1), style, false))
    }
  }
}