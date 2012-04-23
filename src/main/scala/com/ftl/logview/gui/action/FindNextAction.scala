package com.ftl.logview.gui.action
import java.awt.event.ActionEvent

import javax.swing.AbstractAction

class FindNextAction(finder: FindAndHighlightAction) extends AbstractAction {
  def actionPerformed(actionEvent: ActionEvent) {
    finder.findNext()
  }
}