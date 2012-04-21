package com.ftl.logview.logic

import scala.collection.Map
import javax.swing.text.DefaultStyledDocument
import javax.swing.text.StyleContext
import com.ftl.logview.model.Highlighted
import com.ftl.logview.model.AffectType
import com.ftl.logview.model.HighlightType

// to handle document. Highlighting etc
object DocumentUtil {

  def highlightText(doc: DefaultStyledDocument, sc: StyleContext, styles: Map[String, Highlighted], text: String) {
    var pos: Int = 0
    text.split("\n").foreach(line => { highlightLine(doc, sc, styles, line, pos); pos += line.length() })
  }

  def highlightLine(doc: DefaultStyledDocument, sc: StyleContext, styles: Map[String, Highlighted], text: String, startPos: Int) {
    styles.keys.filter(k => styles.get(k).get.highlightType == HighlightType.HIGHLIGHTED).
      foreach(s => TextUtil.collectPositions(text, (if (styles(s).affectType == AffectType.LINE) "(.)*" + styles(s).exp + "(.)*" else styles(s).exp)).
        foreach(pos => doc.setCharacterAttributes(startPos + pos(0), pos(1), sc.getStyle(s), false)))
  }
}

