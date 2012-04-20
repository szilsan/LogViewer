package com.ftl.logview.gui.menu
import java.awt.Color
import scala.annotation.implicitNotFound
import scala.swing.event.ButtonClicked
import scala.swing.Dialog
import scala.swing.Label
import scala.swing.Menu
import scala.swing.MenuItem
import com.ftl.logview.gui.ExpListDialog
import com.ftl.logview.gui.LogViewMainFrame
import com.ftl.logview.gui.StyleInputPanel
import com.ftl.logview.LogViewBundle
import javax.swing.text.StyleConstants
import scala.swing.Alignment
import scala.swing.ListView
import scala.swing.Component
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder
import scala.swing.Action
import java.awt.event.KeyEvent
import javax.swing.KeyStroke
import java.awt.event.InputEvent
import com.ftl.logview.gui.Shortcuts
import scala.swing.event.Key
import com.ftl.logview.model.Skipped

object HighlightMenu extends Menu("Highlight") {
  
  mnemonic = Key.L
  
  val highlightedMenuText = "Highlighted expressions"
   val highlightMenuItem = new MenuItem(new Action(highlightedMenuText) {
    mnemonic = KeyEvent.VK_I
    accelerator = Shortcuts.highlightListMenuItem
    def apply() {}
  }) {
    reactions += {
      case ButtonClicked(b) =>
        doOnMenuSelect(
          (bundle: LogViewBundle) => {
            require(bundle != null)
            new ExpListDialog(highlightedMenuText,
              bundle, highlightList,
              doOnAddHighlightExpression, doOnDeleteHighlightExpression, doOnEditHighlightExpression,
              Option(new StyleListViewRenderer(bundle)))
          })
    }
  }
  contents += highlightMenuItem

  val skippedMenuText = "Skiped expressions"
  contents += new MenuItem(new Action(skippedMenuText) {
    mnemonic = KeyEvent.VK_K
    accelerator = Shortcuts.skippedListMenuItem
    def apply() {}
  }) {
    reactions += {
      case ButtonClicked(b) => {
        doOnMenuSelect(
          (bundle: LogViewBundle) => {
            require(bundle != null)
            new ExpListDialog(skippedMenuText,
              bundle, skippedList,
              doOnAddSkipExpression, doOnDeleteSkipExpression, doOnEditSkipExpression,
              None)
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
  private def skippedList(bundle: LogViewBundle): Seq[Label] = {
    for (skipped <- bundle.skippedList) yield new Label(skipped.exp) { opaque = true; horizontalAlignment = Alignment.Left }
  }

  private def doOnAddSkipExpression(bundle: LogViewBundle) {
    require(bundle != null)

    val skipExpression = Dialog.showInput[String](null, "Skip text expression", "Skipped text", Dialog.Message.Question, null, Seq.empty, null)
    skipExpression match {
      case None => None
      case Some(exp) =>
        bundle.skippedList += Skipped.createSkipped(exp)
        bundle.logViewFrame.reloadData
    }
  }

  private def doOnEditSkipExpression(bundle: LogViewBundle, expression: String) {
    require(bundle != null && expression != null)

    val skipExpression = Dialog.showInput[String](null, "Skip text expression", "Skipped text", Dialog.Message.Question, null, Seq.empty, expression)
    skipExpression match {
      case None => None
      case Some(exp) =>
        bundle.skippedList -= bundle.skippedList.filter(_.exp == expression)(0)
        bundle.skippedList += Skipped.createSkipped(exp)
        bundle.logViewFrame.reloadData
    }
  }

  private def doOnDeleteSkipExpression(bundle: LogViewBundle, expression: String) {
    bundle.skippedList -= bundle.skippedList.filter(_.exp == expression)(0)
    bundle.logViewFrame.reloadData
  }

  // Styles
  private def highlightList(bundle: LogViewBundle): Seq[Label] = {
    (for (style <- bundle.styles.keySet)
      yield new Label(style) {
      background = bundle.sc.getStyle(style).getAttribute(StyleConstants.Background).asInstanceOf[Color]
      foreground = bundle.sc.getStyle(style).getAttribute(StyleConstants.Foreground).asInstanceOf[Color]
      opaque = true
      horizontalAlignment = Alignment.Left
    }).toSeq
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

  class StyleListViewRenderer(bundle: LogViewBundle) extends ListView.Renderer[Label] {
    override def componentFor(list: ListView[_], isSelected: Boolean, focused: Boolean, a: Label, index: Int): Component = {
      val style = bundle.sc.getStyle(a.text)
      if (isSelected) {
        if (style != null) {
          a.foreground = bundle.sc.getStyle(a.text).getAttribute(StyleConstants.Background).asInstanceOf[Color]
          a.background = bundle.sc.getStyle(a.text).getAttribute(StyleConstants.Foreground).asInstanceOf[Color]
          a.horizontalAlignment = Alignment.Right
        } else {
          a.background = Color.BLUE
          a.foreground = Color.WHITE
        }
      } else {
        if (style != null) {
          a.background = bundle.sc.getStyle(a.text).getAttribute(StyleConstants.Background).asInstanceOf[Color]
          a.foreground = bundle.sc.getStyle(a.text).getAttribute(StyleConstants.Foreground).asInstanceOf[Color]
          a.horizontalAlignment = Alignment.Left
        } else {
          a.background = Color.WHITE
          a.foreground = Color.BLACK
        }
      }
      a
    }
  }

}