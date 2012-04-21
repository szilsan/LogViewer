package com.ftl.logview.gui

import java.io.IOException
import java.io.RandomAccessFile
import scala.swing.BorderPanel
import scala.swing.Component
import scala.swing.Dialog
import scala.swing.ScrollPane
import scala.swing.Swing
import com.ftl.logview.logic.DocumentUtil
import com.ftl.logview.logic.TextUtil
import com.ftl.logview.LogViewBundle
import javax.swing.text.DefaultStyledDocument
import javax.swing.JEditorPane
import javax.swing.JTextPane
import javax.swing.AbstractAction
import javax.swing.KeyStroke
import com.ftl.logview.gui.action.FindAndHighlightAction
import action.FindNextAction
import javax.swing.JComponent
import com.ftl.logview.ShortcutType

class LogViewPanel(bundle: LogViewBundle) extends BorderPanel {

  // file handling
  var filePosition: Long = 0

  // for highlighting
  var doc: DefaultStyledDocument = new DefaultStyledDocument(bundle.sc);
  val editorPane: JTextPane = createTextPane

  createGui
  reloadData

  // find
  val findAction = new FindAndHighlightAction(editorPane)

  // refind
  val refindAction = new FindNextAction(findAction)

  def reloadData {
    filePosition = 0
    editorPane.setText("")
    refreshData
  }

  def refreshData {
    try {
      val raf: RandomAccessFile = new RandomAccessFile(bundle.logFile.getAbsoluteFile(), "r")
      raf.seek(filePosition)

      var line: String = null
      do {
        line = raf.readLine()
        if (line != null) {
          filePosition = raf.getFilePointer()
          doc.insertString(doc.getLength(), line + "\n", null)
        }
      } while (line != null)

      raf.close()
    } catch {
      case ioex: IOException => {
        Dialog.showMessage(null, "Error: " + ioex.getMessage(), "Error", Dialog.Message.Error)
        return
      }
    }
    editorPane.setText(TextUtil.deleteSkippedTexts(editorPane.getText(), bundle.styles.toList))
    DocumentUtil.highlightText(doc, bundle.sc, bundle.styles.toList, editorPane.getText())
    refreshShortcutsOnEditorpane
  }

  private def refreshShortcutsOnEditorpane() {
    Shortcuts.removeKeyBinding(editorPane, ShortcutType.FIND.toString)
    Shortcuts.removeKeyBinding(editorPane, ShortcutType.FIND_NEXT.toString)

    Shortcuts.addKeyBinding(editorPane, Shortcuts.findShortCut, findAction, ShortcutType.FIND.toString)
    Shortcuts.addKeyBinding(editorPane, Shortcuts.findNextShortcut, refindAction, ShortcutType.FIND_NEXT.toString)
  }

  // GUI
  private def createGui {
    add(new ScrollPane(createTextPanel), BorderPanel.Position.Center)
  }

  private def createTextPanel: Component = new Component() {
    override lazy val peer: JEditorPane = editorPane
  }

  private def createTextPane: JTextPane = new JTextPane(doc) {
    setEditable(false)
    setBorder(Swing.EmptyBorder(5))
  }
}