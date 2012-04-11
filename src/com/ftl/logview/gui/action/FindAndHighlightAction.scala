package com.ftl.logview.gui.action
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JEditorPane
import scala.swing.Dialog
import scala.collection.immutable.Seq

class FindAndHighlightAction(editorPane: JEditorPane) extends AbstractAction {

  var lastPosition = 0
  var lastSearchText = ""

  def actionPerformed(actionEvent: ActionEvent) {
    val textToSearch = Dialog.showInput[String](null, "Text to search", "", Dialog.Message.Question, null, Seq.empty, "")
    textToSearch match {
      case None => None
      case Some(t) =>
        if (lastSearchText != t) {
          lastSearchText = t
          lastPosition = 0
        }

        val pos = editorPane.getDocument.getText(0, editorPane.getDocument().getLength()).indexOf(t, lastPosition)
        if (pos != -1) {
          editorPane.setCaretPosition(editorPane.getDocument.getText(0, editorPane.getDocument().getLength()).indexOf(t, lastPosition))
          lastPosition += (pos - lastPosition + t.length())
        }
    }
  }
}