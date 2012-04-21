package com.ftl.logview.gui

import java.awt.Color
import java.awt.Dimension
import scala.swing.event.ButtonClicked
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.ButtonGroup
import scala.swing.Dialog
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.RadioButton
import scala.swing.TextField
import com.ftl.logview.logic.StyleUtil
import com.ftl.logview.model.Highlighted
import com.ftl.logview.LogViewBundle
import javax.swing.text.StyleConstants
import javax.swing.JColorChooser
import scala.swing.BoxPanel
import scala.swing.Orientation
import com.ftl.logview.model.AffectType
import com.ftl.logview.model.HighlightType

/**
 * Style input panel - get style expression and Color
 */
class StyleInputPanel(bundle: LogViewBundle, initialExp: Highlighted) extends Dialog {
  require(initialExp != null)

  preferredSize = new Dimension(650, 200)
  modal = true

  title = "Style input"

  // expression definition
  var expression = new TextField(initialExp.exp, 50)

  var sampleText = new Label("Sample text")
  sampleText.foreground = initialExp.fgColor
  sampleText.background = initialExp.bgColor
  sampleText.opaque = true

  val colorBtnBg = new Button(" Background color ") {
    reactions += {
      case ButtonClicked(b) =>
        initialExp.bgColor = JColorChooser.showDialog(null, "Background color", initialExp.bgColor)
        sampleText.background = initialExp.bgColor
    }
  }

  val colorBtnFg = new Button(" Foreground color ") {
    reactions += {
      case ButtonClicked(b) =>
        initialExp.fgColor = JColorChooser.showDialog(null, "Foreground color", initialExp.fgColor)
        sampleText.foreground = initialExp.fgColor
    }
  }

  // ok button
  val okBtn = new Button("OK") {
    reactions += {
      case ButtonClicked(b) =>
        // remove old
        bundle.styles -= initialExp.exp

        // add as new
        val newStyle = new Highlighted(expression.text,
          if (lineRadio.selected) AffectType.LINE else AffectType.EXP,
          if (skippedRadio.selected) HighlightType.SKIPPED else HighlightType.HIGHLIGHTED,
          initialExp.bgColor, initialExp.fgColor)
        StyleUtil.addStyle(bundle.sc, newStyle.exp, newStyle.fgColor, newStyle.bgColor)
        bundle.styles += (newStyle.name -> newStyle)
        StyleInputPanel.this.close()
    }
  }

  // cancel button
  val cancelBtn = new Button("Cancel") {
    reactions += {
      case ButtonClicked(b) => StyleInputPanel.this.close()
    }
  }

  // Valid for Expression or LINE
  val lineRadio = new RadioButton("Line")
  val expRadio = new RadioButton("Expression")
  val btnGroup = new ButtonGroup {
    buttons += lineRadio
    buttons += expRadio
  }
  if (initialExp.affectType == AffectType.LINE) {
    lineRadio.selected = true
  } else {
    expRadio.selected = true
  }

  // Skipped or highlighted
  val skippedRadio = new RadioButton("Skipped")
  val highlightedRadio = new RadioButton("Highlighted")
  val styleBtnGroup = new ButtonGroup {
    buttons += skippedRadio
    buttons += highlightedRadio
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
      add(new FlowPanel {
        contents += new BoxPanel(Orientation.Vertical) {
          contents += lineRadio
          contents += expRadio
        }
        contents += new BoxPanel(Orientation.Vertical) {
          contents += skippedRadio
          contents += highlightedRadio
        }

      }, BorderPanel.Position.South)
    }, BorderPanel.Position.North)

    add(new FlowPanel {
      contents += okBtn
      contents += cancelBtn
    }, BorderPanel.Position.Center)
  }

  visible = true

}