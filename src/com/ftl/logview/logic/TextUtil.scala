package com.ftl.logview.logic

import scala.collection.mutable.ArrayBuffer

import com.ftl.logview.model.ExpressionType
import com.ftl.logview.model.Skipped

/**
 * Common text utils
 */
object TextUtil {

  /**
   * Collect positions of a findText in a text
   */
  def collectPositions(text: String, find: String): ArrayBuffer[Array[Int]] = {
    require(text != null && find != null)

    val positions: ArrayBuffer[Array[Int]] = new ArrayBuffer[Array[Int]]

    for (mi: String <- find.r.findAllIn(text).toSet) {
      var pos = 0

      do {
        pos = text.indexOf(mi, pos)
        if (pos != -1) {
          positions += Array(pos, mi.length())
          pos += mi.length()
        }
      } while (pos != -1)
      pos = 0
    }

    positions
  }

  /**
   * Delete unwanted text from a text. Skipped texts are in a List called skipped. It can contain regular expressions.
   */
  def deleteSkippedTexts(text: String, skipped: List[Skipped]): String = {
    require(text != null)

    var sb: String = text

    for (s <- skipped) {
      sb = (if (s.expType == ExpressionType.LINE) "(.)*" + s.exp + "(.)*[\r\n]*" else s.exp).r.replaceAllIn(sb, "")
    }

    sb
  }
}