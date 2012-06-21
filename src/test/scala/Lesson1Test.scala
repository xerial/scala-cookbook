package xerial.scb

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{WordSpec, FlatSpec}
import xerial.silk.util.Logger
import java.io.File

//--------------------------------------
//
// Lesson1Test.scala
// Since: 2012/06/13 15:18
// 
//--------------------------------------

/**
 * @author leo
 */
class Lesson1Test extends WordSpec with ShouldMatchers with Logger {

  import Lesson1._

  "Lesson1" should {
    "download file" in {
      val out = new File("target", "refFlat.gz")
      download(HG19_REFFLAT, out)
    }

    "parse UCSCGene" in {
      val sampleLine = "C17orf76-AS1\tNR_027160\tchr17\t+\t16342300\t16345340\t16345340\t16345340\t5\t16342300,16342894,16343498,16344387,16344681,\t16342728,16343017,16343567,16344444,16345340,"
      val g = UCSCGene.parse(sampleLine)

      g should be ('defined)

      g.map { gene =>
        gene.name should be ("C17orf76-AS1")
        gene.refSeqName should be ("NR_027160")
        gene.start should be (16342300)

        // Add tests for the other paremeters ...
      }
    }

    "load UCSCGene" in {
      val genes = loadUCSCGene

      info("# of genes: %d", genes.length)
      genes.length should be (43145)

      val sorted = sortGenes(genes)
      for((gene, index) <- sorted.take(5).zipWithIndex) {
        debug("%d:%s", index, gene)
      }

    }

    "run lesson1" in {
      lesson1
    }


  }


}