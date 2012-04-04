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
class SkippedExpListDialog(bundle: LogViewBundle) extends Dialog {

  require(bundle != null)
  
  modal = true
  preferredSize = new Dimension(220, 300)

  var skippedList = new ListView[String] 
  skippedList.selection.intervalMode  = ListView.IntervalMode.Single
  refreshSkippedList

  val btnOk = new Button("Ok") {
    reactions += {
      case ButtonClicked(b) => {
        SkippedExpListDialog.this.close()
      }
    }
  }

  var btnAdd = new Button("Add") {
    reactions += {
      case ButtonClicked(b) => {
        doOnSkipExpression(bundle)
        refreshSkippedList
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
    add(skippedList, BorderPanel.Position.Center)
    add(new FlowPanel() {
      contents += btnAdd
      contents += btnDel
      contents += btnOk
    }, BorderPanel.Position.South)
  }

  visible = true

  private def refreshSkippedList {
    var skippedExpressions = for (skipped <- bundle.skippedList) yield skipped
    skippedList.listData = skippedExpressions
  }
  
  private def doOnSkipExpression(bundle: LogViewBundle) {

    require(bundle != null)

    val skipExpression = Dialog.showInput[String](null, "Skip text expression", "Skipped text", Dialog.Message.Question, null, Seq.empty, null)
    skipExpression match {
      case None => None
      case Some(exp) =>
        bundle.skippedList += exp
        bundle.logViewFrame.reloadData
    }
  }
}