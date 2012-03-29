package com.ftl.logview.logic

import scala.collection.Map

import javax.swing.text.DefaultStyledDocument
import javax.swing.text.StyleContext

// to handle document. Highlighting etc
object DocumentUtil {

  def highlightText(doc: DefaultStyledDocument, sc: StyleContext, styles: Map[String, String], text: String) {
    var pos: Int = 0
    text.split("\n").foreach(line => { highlightLine(doc, sc, styles, line, pos); pos += line.length() })
  }

  def highlightLine(doc: DefaultStyledDocument, sc: StyleContext, styles: Map[String, String], text: String, startPos: Int) {
    styles.keys.foreach(s => TextUtil.collectPositions(text, styles(s)).foreach(pos => doc.setCharacterAttributes(startPos + pos(0), pos(1), sc.getStyle(s), false)))
  }
}