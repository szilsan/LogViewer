package com.ftl.logview.gui.menu
import scala.swing.event.ButtonClicked
import scala.swing.Dialog
import scala.swing.Menu
import scala.swing.MenuItem

import com.ftl.logview.gui.LogViewMainFrame

object HighlightMenu extends Menu("HighLight") {
  contents += new MenuItem("Highlight") {

  }

  contents += new MenuItem("Skip") {
    reactions += {
      case ButtonClicked(b) => {
        if (LogViewMainFrame.tabAndBundle.nonEmpty) {
          val skipExpression = Dialog.showInput[String](null, "Skip text expression", "Skipped text", Dialog.Message.Question, null, Seq.empty, null)
          skipExpression match {
            case None => None
            case Some(exp) =>
              var page = LogViewMainFrame.tabbedPane.selection
              val bundle = LogViewMainFrame.tabAndBundle.get(page.page)
              bundle match {
                case None => None
                case Some(s) => 
                  s.skippedList += exp
                  s.logViewFrame.reloadData
              }
          }
        } else {
          Dialog.showMessage(null, "There is no tab opened", "Warning", Dialog.Message.Warning)
        }
      }
    }
  }
}