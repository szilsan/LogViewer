package com.ftl.logview
import org.scalatest.FunSuite
import com.ftl.logview.logic.TextUtil

object TextUtilTest extends FunSuite {

  test("collect positions") {
    val positions = TextUtil.collectPositions("almat eszik a babam, de eszik, de eszik", "eszik")
    assert(!positions.isEmpty)
    
    assert(positions.first(0) == 6)
    assert(positions.first(1) == 5)
    
    assert(positions(1)(0) == 24)
    assert(positions(1)(1) == 5)

    assert(positions(2)(0) == 34)
    assert(positions(2)(1) == 5)
  }
  
  test("delete skipped text") {
//    val generatedText = TextUtil.deleteSkippedTexts("megy a gozos, megy a gozos kanizsara", "gozos" :: "a" :: Nil)
//    assert(generatedText.indexOf("gozos") == -1)
//    assert(generatedText.indexOf("a") == -1)
//    assert(generatedText.length() != 0)
  }

  def main(args: Array[String]) {
    TextUtilTest.execute()
  }

}