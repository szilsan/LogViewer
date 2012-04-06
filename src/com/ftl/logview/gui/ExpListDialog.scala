package com.ftl.logview.gui
import java.awt.Dimension

import scala.annotation.implicitNotFound
import scala.swing.event.ButtonClicked
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.Dialog
import scala.swing.FlowPanel
import scala.swing.Label
import scala.swing.ListView

import com.ftl.logview.LogViewBundle

/**
 * Show skipped expressions.
 * Possibility to add and delete new expression
 *
 */
class ExpListDialog(dialogTitle: String = "Expressions", bundle: LogViewBundle,
  refresh: LogViewBundle => Seq[Label],
  doOnNewExpression: LogViewBundle => Unit,
  doOnDeleteExpression: (LogViewBundle, String) => Unit,
  doOnEdit: (LogViewBundle, String) => Unit,
  listViewRenderer: Option[ListView.Renderer[Label]]) extends Dialog {

  require(bundle != null)

  modal = true
  preferredSize = new Dimension(280, 300)
  title = dialogTitle

  var expList = new ListView[Label] {
    selection.intervalMode = ListView.IntervalMode.Single

    listViewRenderer match {
      case None => renderer = ListView.Renderer(_.text)
      case Some(r) => renderer = r
    }
  }
  refreshData(refresh(bundle))

  val btnOk = new Button("Ok") {
    reactions += {
      case ButtonClicked(b) => {
        ExpListDialog.this.close()
      }
    }
  }

  val btnAdd = new Button("Add") {
    reactions += {
      case ButtonClicked(b) => {
        doOnNewExpression(bundle)
        refreshData(refresh(bundle))
      }
    }
  }

  val btnDel = new Button("Delete") {
    reactions += {
      case ButtonClicked(b) => {
        doOnButtonClicked(doOnDeleteExpression)
      }
    }
  }

  val btnEdit = new Button("Edit") {
    reactions += {
      case ButtonClicked(b) => {
        doOnButtonClicked(doOnEdit)
      }
    }
  }

  contents = new BorderPanel {
    layout(expList) = BorderPanel.Position.Center

    layout(new FlowPanel() {
      contents += btnAdd
      contents += btnEdit
      contents += btnDel
      contents += btnOk
    }) = BorderPanel.Position.South
  }

  centerOnScreen

  visible = true

  def refreshData(data: Seq[Label]) {
    expList.listData = data
  }

  def doOnButtonClicked(action: (LogViewBundle, String) => Unit) {
    if (expList.selection.items.isEmpty) {
      Dialog.showMessage(null, "No expressions is selected", "Warning", Dialog.Message.Warning)
    } else {
      action(bundle, expList.selection.items.first.text)
      refreshData(refresh(bundle))
    }
  }
}