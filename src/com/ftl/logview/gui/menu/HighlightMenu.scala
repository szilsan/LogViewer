package com.ftl.logview.gui.menu
import scala.swing.event.ButtonClicked
import scala.swing.ListView
import scala.swing.Dialog
import scala.swing.Menu
import scala.swing.MenuItem

import com.ftl.logview.gui.ExpListDialog
import com.ftl.logview.gui.LogViewMainFrame
import com.ftl.logview.gui.StyleInputPanel
import com.ftl.logview.LogViewBundle

object HighlightMenu extends Menu("HighLight") {
  val highlightedMenuText = "Highlighted expressions"
  contents += new MenuItem(highlightedMenuText) {
    reactions += {
      case ButtonClicked(b) =>
        doOnMenuSelect(
          (bundle: LogViewBundle) => {
            require(bundle != null)
            new ExpListDialog(highlightedMenuText, bundle, highlightList, doOnAddHighlightExpression, doOnDeleteHighlightExpression, doOnEditHighlightExpression)
          })
    }
  }

  val skippedMenuText = "Skiped expressions"
  contents += new MenuItem(skippedMenuText) {
    reactions += {
      case ButtonClicked(b) => {
        doOnMenuSelect(
          (bundle: LogViewBundle) => {
            require(bundle != null)
            new ExpListDialog(skippedMenuText, bundle, skippedList, doOnAddSkipExpression, doOnDeleteSkipExpression, doOnEditSkipExpression)
          })
      }
    }
  }

  def doOnMenuSelect(doFunc: LogViewBundle => Unit) {
    if (LogViewMainFrame.tabAndBundle.nonEmpty) {
      var page = LogViewMainFrame.tabbedPane.selection
      val bundle = LogViewMainFrame.tabAndBundle.get(page.page)
      bundle match {
        case None => None
        case Some(b) => doFunc(b)
      }
    } else {
      Dialog.showMessage(null, "There is no tab opened", "Warning", Dialog.Message.Warning)
    }
  }

  //Skipped expressions handling
  private def skippedList(bundle: LogViewBundle): Seq[String] = {
    for (skipped <- bundle.skippedList) yield skipped
  }

  private def doOnAddSkipExpression(bundle: LogViewBundle) {
    require(bundle != null)

    val skipExpression = Dialog.showInput[String](null, "Skip text expression", "Skipped text", Dialog.Message.Question, null, Seq.empty, null)
    skipExpression match {
      case None => None
      case Some(exp) =>
        bundle.skippedList += exp
        bundle.logViewFrame.reloadData
    }
  }

  private def doOnEditSkipExpression(bundle: LogViewBundle, expression: String) {
    require(bundle != null && expression!=null)

    val skipExpression = Dialog.showInput[String](null, "Skip text expression", "Skipped text", Dialog.Message.Question, null, Seq.empty, expression)
    skipExpression match {
      case None => None
      case Some(exp) =>
        bundle.skippedList -= expression
        bundle.skippedList += exp
        bundle.logViewFrame.reloadData
    }
  }

  private def doOnDeleteSkipExpression(bundle: LogViewBundle, expression: String) {
    bundle.skippedList -= expression
    bundle.logViewFrame.reloadData
  }

  // Styles
  private def highlightList(bundle: LogViewBundle): Seq[String] = {
    (for (style <- bundle.styles.keySet) yield style).toSeq
  }

  private def doOnAddHighlightExpression(bundle: LogViewBundle) {
    new StyleInputPanel(bundle)
    bundle.logViewFrame.reloadData
  }

  private def doOnEditHighlightExpression(bundle: LogViewBundle, expression: String = "") {
    new StyleInputPanel(bundle, expression)
    bundle.logViewFrame.reloadData
  }

  private def doOnDeleteHighlightExpression(bundle: LogViewBundle, expression: String) {
    bundle.styles -= expression
    bundle.logViewFrame.reloadData
  }

}