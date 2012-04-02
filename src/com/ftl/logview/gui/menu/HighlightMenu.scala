package com.ftl.logview.gui.menu
import scala.swing.event.ButtonClicked
import scala.swing.Dialog
import scala.swing.Menu
import scala.swing.MenuItem
import com.ftl.logview.gui.LogViewMainFrame
import com.ftl.logview.gui.StyleInputPanel

object HighlightMenu extends Menu("HighLight") {
  contents += new MenuItem("Highlighted expressions") {
    reactions += {
      case ButtonClicked(b) =>
        doOnMenuSelect({new StyleInputPanel()})
    }
  }

  contents += new MenuItem("Skiped expressions") {
    reactions += {
      case ButtonClicked(b) => {
        doOnMenuSelect(doOnSkipExpression)
      }
    }
  }

  private def doOnSkipExpression: Unit = {
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
  }

  def doOnMenuSelect(doFunc: => Unit) {
    println(LogViewMainFrame.tabAndBundle.size)
    if (LogViewMainFrame.tabAndBundle.nonEmpty) {
      doFunc
    } else {
      Dialog.showMessage(null, "There is no tab opened", "Warning", Dialog.Message.Warning)
    }
  }
}