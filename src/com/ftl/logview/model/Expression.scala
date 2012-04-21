package com.ftl.logview.model

/**
 * Abstract class for any expression
 */
abstract class Expression(expression: String, expressionType: ExpressionType.Value = ExpressionType.LINE) {
  def exp = expression
  def expType = expressionType
  
  def toSave:String
}