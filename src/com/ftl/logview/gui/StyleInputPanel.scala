package com.ftl.logview.gui
import java.awt.Color
import java.awt.Dimension
import scala.swing.event.ButtonClicked
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.Dialog
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.TextField
import com.ftl.logview.LogViewBundle
import javax.swing.JColorChooser
import com.ftl.logview.logic.StyleUtil

/**
 * Style input panel - get style expression and Color
 */
class StyleInputPanel(bundle: LogViewBundle) extends Dialog {
  preferredSize = new Dimension(650, 140)
  modal = true

  title = "Style input"

  // expression definition
  var expression = new TextField(50)

  // color choosers
  var expColorBg = Color.WHITE
  var expColorFg = Color.BLACK

  var sampleText = new Label("Sample text")
  sampleText.foreground = expColorFg
  sampleText.background = expColorBg
  sampleText.opaque = true
  

  var colorBtnBg = new Button(" Background color ") {
    reactions += {
      case ButtonClicked(b) =>
        expColorBg = JColorChooser.showDialog(null, "Background color", expColorBg)
        sampleText.background = expColorBg
    }
  }

  var colorBtnFg = new Button(" Foreground color ") {
    reactions += {
      case ButtonClicked(b) =>
        expColorFg = JColorChooser.showDialog(null, "Foreground color", expColorFg)
        sampleText.foreground = expColorFg
    }
  }

  // ok button
  var okBtn: Button = new Button("OK") {
    reactions += {
      case ButtonClicked(b) =>
        StyleUtil.addStyle(bundle.sc, expression.text, expColorFg, expColorBg)
        bundle.styles += (expression.text -> expression.text)
        bundle.logViewFrame.reloadData
        StyleInputPanel.this.close()
    }
  }

  // cancel button
  var cancelBtn: Button = new Button("Cancel") {
    reactions += {
      case ButtonClicked(b) => StyleInputPanel.this.close()
    }
  }

  contents = new BorderPanel {

    add(new BorderPanel {
      add(new FlowPanel {
        contents += new Label("Expression: ")
        contents += expression
      }, BorderPanel.Position.North)
      add(new FlowPanel {
        contents += colorBtnBg
        contents += colorBtnFg
        contents += sampleText
      }, BorderPanel.Position.Center)
    }, BorderPanel.Position.North)

    add(new FlowPanel {
      contents += okBtn
      contents += cancelBtn
    }, BorderPanel.Position.Center)
  }

  visible = true

}