package com.ftl.logview.gui

import scala.swing.Alignment
import scala.swing.GridPanel
import scala.swing.Label

import com.ftl.logview.model.Highlighted

/**
 * To show a highlight item on the list
 */
class ExpDialogPanel(initStyle: Highlighted) extends GridPanel(1, 2) {
  def style = initStyle

  val labelRight = new Label {
    text = initStyle.exp

    background = initStyle.bgColor
    foreground = initStyle.fgColor

    horizontalAlignment = Alignment.Center

    opaque = true
  }

  val labelLeft = new Label {
    text = initStyle.exp
    horizontalAlignment = Alignment.Center
    opaque = true
  }

  contents += labelRight
  contents += labelLeft

  tooltip = "Affected for " + initStyle.affectType + " Highlight type: " + style.highlightType

}