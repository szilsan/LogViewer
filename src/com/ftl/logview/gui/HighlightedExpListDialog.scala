package com.ftl.logview.gui
import com.ftl.logview.LogViewBundle
import scala.swing.Dialog
import java.awt.Dimension
import scala.swing.ListView
import scala.swing.Button
import scala.swing.event.ButtonClicked
import scala.swing.BorderPanel
import scala.swing.FlowPanel

/**
 * Show skipped expressions.
 * Possibility to add and delete new expression
 *
 */
class HighlightExpListDialog(bundle: LogViewBundle) extends Dialog {
  require(bundle != null)

  modal = true
  preferredSize = new Dimension(220, 300)

  var highlightList = new ListView[String]
  highlightList.selection.intervalMode  = ListView.IntervalMode.Single
  refreshHighlightList

  val btnOk = new Button("Ok") {
    reactions += {
      case ButtonClicked(b) => {
        HighlightExpListDialog.this.close()
      }
    }
  }

  var btnAdd = new Button("Add") {
    reactions += {
      case ButtonClicked(b) => {
        new StyleInputPanel(bundle)
        refreshHighlightList
      }
    }
  }

  var btnDel = new Button("Delete") {
    reactions += {
      case ButtonClicked(b) => {
      }

    }
  }

  contents = new BorderPanel {
    add(highlightList, BorderPanel.Position.Center)
    add(new FlowPanel() {
      contents += btnAdd
      contents += btnDel
      contents += btnOk
    }, BorderPanel.Position.South)
  }

  visible = true

  private def refreshHighlightList {
    var styleExpressions = for (style <- bundle.styles.keySet) yield style
    highlightList.listData = styleExpressions.toSeq
  }
}