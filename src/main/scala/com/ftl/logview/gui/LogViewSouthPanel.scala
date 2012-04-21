package com.ftl.logview.gui

import scala.swing.event.ButtonClicked
import scala.swing.Button
import scala.swing.CheckBox
import scala.swing.FlowPanel
import scala.collection.JavaConversions._

import com.ftl.logview.LogViewBundle

import javax.swing.Timer

/**
 * South panel of LogViewFrame
 */
class LogViewSouthPanel(bundle: LogViewBundle) extends FlowPanel {

  val refreshText = "Automatic refresh [tail] "
  val refreshRate = 5

  val automaticRefreshCheckbox: CheckBox = new CheckBox(refreshText)
  val forceRefreshButton: Button = new Button("Force refresh")

  val refreshTimer: Timer = new Timer(1000, new java.awt.event.ActionListener {
    var counter = 0
    def actionPerformed(e: java.awt.event.ActionEvent) {

      if (counter / refreshRate == 1) {
        counter = 0
        bundle.logViewFrame.refreshData
      } else {
        counter += 1
      }
      automaticRefreshCheckbox.text = refreshText + " in " + (refreshRate - counter)
    }
  })

  contents += automaticRefreshCheckbox
  contents += forceRefreshButton

  listenTo(automaticRefreshCheckbox, forceRefreshButton)

  reactions += {
    case ButtonClicked(`automaticRefreshCheckbox`) =>
      if (automaticRefreshCheckbox.selected) {
        refreshTimer.start()
      } else {
        automaticRefreshCheckbox.text = refreshText
        refreshTimer.stop()
      }
    case ButtonClicked(`forceRefreshButton`) =>
      forceRefreshButton.enabled = false
      val isRunning = refreshTimer.isRunning()
      refreshTimer.stop()
      bundle.logViewFrame.reloadData
      if (isRunning) {
        refreshTimer.start()
      }
      forceRefreshButton.enabled = true
  }
}