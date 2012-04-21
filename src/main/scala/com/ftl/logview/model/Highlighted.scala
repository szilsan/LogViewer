package com.ftl.logview.model

import java.awt.Color

/**
 * For highlighted texts
 */
class Highlighted(expression: String, affectTypeParam: AffectType.Value = AffectType.LINE, highlighTypeParam: HighlightType.Value = HighlightType.HIGHLIGHTED) {
  def exp = expression
  def affectType = affectTypeParam
  def highlightType = highlighTypeParam

  var bgColor = Color.WHITE
  var fgColor = Color.BLACK

  def this(expression: String, affectType: AffectType.Value, highlighTypeParam: HighlightType.Value, bgColor: Color, fgColor: Color) {
    this(expression, affectType, highlighTypeParam)
    this.bgColor = bgColor
    this.fgColor = fgColor
  }

  override def toString() = {
    "Highlighted[expression= " + expression +
      " affect type= " + affectType.toString +
      " bgColor= " + bgColor + " fgColor= " + fgColor +
      " highlight type= " + highlightType.toString
  }

  def toSave = {
    fgColor.getRGB().toHexString.substring(2) + "," + bgColor.getRGB().toHexString.substring(2) + "," + affectType.toString + "," + highlightType.toString + "," + exp
  }
}

object Highlighted {
  def createHighlighted(description: String): Option[Highlighted] = {
    val parameters = description.trim().split(",")

    val fgColor: Color = new Color(Integer.parseInt(parameters(0).trim(), 16))
    val bgColor: Color = new Color(Integer.parseInt(parameters(1).trim(), 16))
    val expressionType = AffectType.values.find(_.toString == parameters(2).trim.toUpperCase)
    val highlightType = HighlightType.values.find(_.toString == parameters(3).trim.toUpperCase)
    val expression = parameters(4).trim()

    expressionType match {
      case None =>
        println("Invalid affect type: " + description)
        None
      case Some(s) =>
        highlightType match {
          case None =>
            println("Invalid highlight type: " + description)
            None
          case Some(p) =>
            Some(new Highlighted(expression, s, p, bgColor, fgColor))
        }
    }
  }
}