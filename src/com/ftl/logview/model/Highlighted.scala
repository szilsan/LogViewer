package com.ftl.logview.model

import java.awt.Color

/**
 * For highlighted texts
 */
class Highlighted(expression: String, expressionType: ExpressionType.Value = ExpressionType.LINE) extends Expression(expression, expressionType) {

  var bgColor = Color.WHITE
  var fgColor = Color.BLACK

  def name = expression

  def this(expression: String, expressionType: ExpressionType.Value, bgColor: Color, fgColor: Color) {
    this(expression, expressionType)
    this.bgColor = bgColor
    this.fgColor = fgColor
  }

  override def toString() = {
    "Highlighted[expression= " + expression + " type= " + expressionType.toString + " bgColor= " + bgColor + " fgColor=" + fgColor
  }
}

object Highlighted {
  def createHighlighted(description: String) = {
    val parameters = description.trim().split(",")

    val fgColor: Color = new Color(Integer.parseInt(parameters(0).trim(), 16))
    val bgColor: Color = new Color(Integer.parseInt(parameters(1).trim(), 16))
    val expressionType = ExpressionType.values.find(_.toString == parameters(2).trim.toUpperCase)
    val expression = parameters(3).trim()

    expressionType match {
      case None =>
        println("Invalid expression type (use LINE): " + description)
        new Highlighted(expression, ExpressionType.LINE, bgColor = bgColor, fgColor = fgColor)
      case Some(s) => new Highlighted(expression, s, bgColor, fgColor)
    }
  }
}