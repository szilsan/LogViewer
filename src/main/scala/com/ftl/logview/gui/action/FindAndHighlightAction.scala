package com.ftl.logview.gui.action
import java.awt.event.ActionEvent

import scala.collection.immutable.Seq
import scala.swing.Dialog

import javax.swing.AbstractAction
import javax.swing.JEditorPane

class FindAndHighlightAction(editorPane: JEditorPane) extends AbstractAction {

  var lastPosition = 0
  var lastSearchText = ""

  def actionPerformed(actionEvent: ActionEvent) {
    val textToSearch = Dialog.showInput[String](null, "Text to search", "", Dialog.Message.Question, null, Seq.empty, lastSearchText)
    textToSearch match {
      case None => None
      case Some(t) =>
        if (lastSearchText != t) {
          lastSearchText = t
          lastPosition = 0
        }

        findNext()
    }
  }

  def findNext() {
    val pos = editorPane.getDocument.getText(0, editorPane.getDocument().getLength()).indexOf(lastSearchText, lastPosition)
    if (pos != -1) {
      editorPane.setCaretPosition(editorPane.getDocument.getText(0, editorPane.getDocument().getLength()).indexOf(lastSearchText, lastPosition))
      lastPosition += (pos - lastPosition + lastSearchText.length())
    } else {
      Dialog.showMessage(null, "Not found: " + lastSearchText, "Info", Dialog.Message.Info)
    }
  }
}