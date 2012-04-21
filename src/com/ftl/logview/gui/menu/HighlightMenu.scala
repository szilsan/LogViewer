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
import com.ftl.logview.model.Highlighted
import scala.collection.mutable.ListBuffer

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

  // Styles
  private def highlightList(bundle: LogViewBundle): Seq[Label] = {
    val labels = ListBuffer.empty[Label]
    for (e <- bundle.styles) {
      labels += new Label(e.exp) {
        background = e.bgColor
        foreground = e.fgColor
        opaque = true
        horizontalAlignment = Alignment.Left
      }
    }

    labels.toSeq
  }

  private def doOnAddHighlightExpression(bundle: LogViewBundle) {
    new StyleInputPanel(bundle, new Highlighted(""))
    bundle.logViewFrame.reloadData
  }

  private def doOnEditHighlightExpression(bundle: LogViewBundle, expression: Highlighted) {
    new StyleInputPanel(bundle, expression)
    bundle.logViewFrame.reloadData
  }

  private def doOnDeleteHighlightExpression(bundle: LogViewBundle, expression: Highlighted) {
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