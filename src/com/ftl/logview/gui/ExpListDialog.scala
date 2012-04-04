package com.ftl.logview.gui
import java.awt.Dimension

import scala.swing.event.ButtonClicked
import scala.swing.ListView
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.Dialog
import scala.swing.FlowPanel

import com.ftl.logview.LogViewBundle

/**
 * Show skipped expressions.
 * Possibility to add and delete new expression
 *
 */
class ExpListDialog(bundle: LogViewBundle, refresh: LogViewBundle => Seq[String], doOnNewExpression: LogViewBundle => Unit) extends Dialog {

  require(bundle != null)
  
  modal = true
  preferredSize = new Dimension(220, 300)

  var expList = new ListView[String] 
  expList.selection.intervalMode  = ListView.IntervalMode.Single
  refreshData(refresh(bundle))

  val btnOk = new Button("Ok") {
    reactions += {
      case ButtonClicked(b) => {
        ExpListDialog.this.close()
      }
    }
  }

  var btnAdd = new Button("Add") {
    reactions += {
      case ButtonClicked(b) => {
        doOnNewExpression(bundle)
        refreshData(refresh(bundle))
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
    add(expList, BorderPanel.Position.Center)
    add(new FlowPanel() {
      contents += btnAdd
      contents += btnDel
      contents += btnOk
    }, BorderPanel.Position.South)
  }

  visible = true
  
  def refreshData(data: Seq[String]) {
    expList.listData = data
  }
}