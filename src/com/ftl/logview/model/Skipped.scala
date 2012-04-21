package com.ftl.logview.model

class Skipped(expression: String, expressionType: ExpressionType.Value = ExpressionType.LINE) extends Expression(expression, expressionType) {

  override def toString() = {
    "Skipped[expression= " + expression + " type= " + expressionType.toString + "]"
  }

  override def toSave() = {
    expressionType.toString + "," + expression
  }
}

object Skipped {
  def createSkipped(description: String) = {
    val parameters = description.trim().split(",")

    val expressionType = ExpressionType.values.find(_.toString == parameters(0).trim.toUpperCase)
    val expression = parameters(1).trim()

    expressionType match {
      case None =>
        println("Invalid expression type (use LINE): " + description)
        new Skipped(expression)
      case Some(s) => new Skipped(expression, s)
    }
  }
}