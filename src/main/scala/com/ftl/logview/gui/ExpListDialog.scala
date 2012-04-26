package com.ftl.logview.gui

import java.awt.Color
import java.awt.Dimension

import scala.collection.mutable.ListBuffer
import scala.swing.event.ButtonClicked
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.Component
import scala.swing.Dialog
import scala.swing.FlowPanel
import scala.swing.ListView

import com.ftl.logview.model.Highlighted
import com.ftl.logview.LogViewBundle

/**
 * Show skipped expressions.
 * Possibility to add and delete new expression
 *
 */
class ExpListDialog(dialogTitle: String = "Expressions", bundle: LogViewBundle,
  refresh: LogViewBundle => Seq[ExpDialogPanel],
  doOnNewExpression: LogViewBundle => Unit,
  doOnDeleteExpression: (LogViewBundle, Highlighted) => Unit,
  doOnEdit: (LogViewBundle, Highlighted) => Unit) extends Dialog {

  require(bundle != null)

  modal = true
  preferredSize = new Dimension(280, 300)
  title = dialogTitle

  var expList = new ListView[ExpDialogPanel] {
    selection.intervalMode = ListView.IntervalMode.Single

    renderer = new StyleListViewRenderer(bundle)
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

  val btnUp = new Button("Up") {
    reactions += {
      case ButtonClicked(b) => {
        doOnUp
      }
    }
  }

  val btnDown = new Button("Down") {
    reactions += {
      case ButtonClicked(b) => {
        doOnDown
      }
    }
  }

  contents = new BorderPanel {
    layout(expList) = BorderPanel.Position.Center

    layout(new FlowPanel() {
      contents += btnUp
      contents += btnDown
      contents += btnAdd
      contents += btnEdit
      contents += btnDel
      contents += btnOk
    }) = BorderPanel.Position.South
  }

  centerOnScreen

  visible = true

  def refreshData(data: Seq[ExpDialogPanel]) {
    expList.listData = data
  }

  private def doOnUp() {
    doOnButtonClicked((bundle: LogViewBundle, style: Highlighted) => {
      moveUpByOne(expList.selection.items.head.style, bundle.styles)
    })
  }

  private def doOnDown() {
    moveDownByOne(expList.selection.items.head.style, bundle.styles)
  }
  
  private def moveDownByOne( movingStyle: Highlighted, list: ListBuffer[Highlighted]) = moveInList(-1, movingStyle, list)
  private def moveUpByOne( movingStyle: Highlighted, list: ListBuffer[Highlighted]) = moveInList(1, movingStyle, list)

  private def moveInList(index: Int, movingStyle: Highlighted, list: ListBuffer[Highlighted]) {
    require(index < 0 || list.size > index, movingStyle != null)
    val removePos = list.findIndexOf(_ == movingStyle)
    if ((removePos+index == list.size && index > 0) || (removePos == 0 && index < 0)) {}
    else {
      list.remove(removePos)
      list.insert(removePos + index, movingStyle)
    }
  }

  private def doOnButtonClicked(action: (LogViewBundle, Highlighted) => Unit) {
    if (expList.selection.items.isEmpty) {
      Dialog.showMessage(null, "No expressions is selected", "Warning", Dialog.Message.Warning)
    } else {
      val exp = bundle.styles.filter(_.exp == expList.selection.items.head.style.exp)(0)
      action(bundle, exp)
      refreshData(refresh(bundle))
    }
  }

  class StyleListViewRenderer(bundle: LogViewBundle) extends ListView.Renderer[ExpDialogPanel] {
    override def componentFor(list: ListView[_], isSelected: Boolean, focused: Boolean, a: ExpDialogPanel, index: Int): Component = {
      if (isSelected) {
        a.labelLeft.background = Color.BLACK
        a.labelLeft.foreground = Color.WHITE
      } else {
        a.labelLeft.background = Color.WHITE
        a.labelLeft.foreground = Color.BLACK
      }
      a
    }
  }
}