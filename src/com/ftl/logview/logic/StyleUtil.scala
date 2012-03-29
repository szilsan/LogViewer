package com.ftl.logview.logic
import javax.swing.text.StyleConstants
import javax.swing.text.Style
import java.awt.Color

/*
 * Util methods for style handling
 */
object StyleUtil {

  /*
   * Configure a style. Set only background and text(foreground) color
   */
  def configureStyle(style: Style, bg: Option[Color], fg: Option[Color]) {
    require(style != null)

    StyleConstants.setForeground(style, fg.getOrElse(Color.BLACK))
    StyleConstants.setBackground(style, bg.getOrElse(Color.WHITE))
  }

}