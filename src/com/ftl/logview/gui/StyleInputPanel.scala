package com.ftl.logview.gui
import scala.swing.Dialog
import java.awt.Dimension
import scala.swing.TextField
import scala.swing.Button
import scala.swing.Panel
import scala.swing.BorderPanel

/**
 * Style input panel - get style expression and Color
 */
class StyleInputPanel extends Dialog {
  preferredSize = new Dimension(320, 200)
  modal = true

  var expression: TextField = new TextField

  var okBtn: Button = new Button("OK")
  var cancelBtn: Button = new Button("Cancel")

  
  
  contents = new BorderPanel {
	  add(expression, BorderPanel.Position.North)
  }
 
  visible = true
}