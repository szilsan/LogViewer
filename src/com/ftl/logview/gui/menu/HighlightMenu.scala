package com.ftl.logview.gui.menu
import scala.annotation.implicitNotFound
import scala.swing.event.ButtonClicked
import scala.swing.Dialog
import scala.swing.Menu
import scala.swing.MenuItem

import com.ftl.logview.gui.HighlightExpListDialog
import com.ftl.logview.gui.LogViewMainFrame
import com.ftl.logview.gui.SkippedExpListDialog
import com.ftl.logview.LogViewBundle

object HighlightMenu extends Menu("HighLight") {
  contents += new MenuItem("Highlighted expressions") {
    reactions += {
      case ButtonClicked(b) =>
        doOnMenuSelect(
          (bundle: LogViewBundle) =>
            {
              require(bundle != null)
              new HighlightExpListDialog(bundle)
            })
    }
  }

  contents += new MenuItem("Skiped expressions") {
    reactions += {
      case ButtonClicked(b) => {
        doOnMenuSelect(
          (bundle: LogViewBundle) =>
            {
              require(bundle != null)
              new SkippedExpListDialog(bundle)
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
}