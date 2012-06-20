//--------------------------------------
//
// Lesson1.scala
// Since: 2012/06/13 15:17
// 
//--------------------------------------

package xerial.scb

import io.Source
import java.net.{URI, URL}
import java.nio.channels.Channels
import java.io._
import xerial.silk.util.Logger
import java.util.zip.GZIPInputStream

/**
 * @author leo
 */
object Lesson1 extends Logger {

  val HG19_REFFLAT = "http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/refFlat.txt.gz"


  /**
   * Download the file from the given URL, then output the file contents to the file
   * @param url
   * @param outputFile
   */
  def download(url: String, outputFile: File) {

    if (outputFile.exists()) {
      info("%s alrady exists", outputFile)
      return
    }

    outputFile.getParentFile.mkdirs()

    val input = Channels.newChannel(new URL(url).openStream)
    val out = new FileOutputStream(outputFile).getChannel
    try {
      info("Downloading %s", url)
      out.transferFrom(input, 0, Integer.MAX_VALUE)
    }
    finally {
      input.close
      out.close
    }
  }

  def gunzipStream(file: File) = new BufferedInputStream(new GZIPInputStream(new FileInputStream(file)))

  def loadUCSCGene = {
    // Download data file from URL
    val refFlat = new File("target/refFlat.gz")
    download(HG19_REFFLAT, refFlat)

    // Create an array of genes
    val b = Array.newBuilder[UCSCGene]

    // Read the unzipped file line by line
    for (line <- Source.fromInputStream(gunzipStream(refFlat)).getLines;
         gene <- UCSCGene.parse(line)) {  // pattern match is used
      b += gene
    }
    b.result
  }

  def sortGenes(in:Array[UCSCGene]) : Array[UCSCGene] = {
    // Problem: Improve the ordering of genes to be aware of alnum values (e.g., chr1, chr10, etc.)
    in.sortBy(gene => (gene.chr, gene.start))
  }


  def lesson1 {

    val genes = loadUCSCGene
    
    // map
    info("map")
    val geneNames = genes.map(g => g.name)
    debug("gene names:%s, ...", geneNames.take(5).mkString(", "))

    // filter
    info("filter")
    val genesInChr21 = genes.filter(g => g.chr == "chr21")
    debug("genes in chr21:\n%s", genesInChr21.take(5).mkString("\n"))

    // reduce (Count the total number of exons)
    info("reduce")
    val exonCount = genes.map(_.exonCount).reduce(_ + _)
    val exonCount2 = genes.map(_.exonCount).reduce((a, b) => a + b)
    val exonCount3 = genes.map(_.exonCount).sum
    debug("exon count: %,d", exonCount)
    debug("exon count2: %,d", exonCount2)
    debug("exon count using sum: %,d", exonCount3)

    // fold (Count the average number of exons)
    info("fold")
    val (count, sum) = genes.foldLeft((0, 0))((s, gene) => (s._1 + 1, s._2 + gene.exonCount))
    debug("avg. exon count per gene: %.2f", sum.toFloat / count)
    
    // Group genes by their chromosome names
    info("groupBy")
    val geneTable = genes.groupBy(_.chr)
    debug("""geneTable("chr21")\n%s, ...""", geneTable("chr21").take(5).mkString("\n"))
    val geneCountInChr1 = geneTable("chr1").length
    debug("# of genes in chr1: %d", geneCountInChr1)


    // Map 
    // Create indexes from gene names to UCSCGenes
    info("Using Map")
    val geneIndex = {
      val b = Map.newBuilder[String, UCSCGene]
      for(g <- genes) {
        b += g.name -> g
        b += g.refSeqName -> g
      }
      b.result
    }
    val hox1 = geneIndex("HOXA1")
    debug("HOXA1 gene: %s", hox1)
    
    // Alternative way of creating indexes using flatMap and toMap
    val geneIndex2 = genes.flatMap(g => Seq(g.name -> g, g.refSeqName -> g)).toMap
    debug("Keys in geneIndex2:%s,...", geneIndex2.keys.take(5))
      
    // Create a set of chromosome names
    info("Set")
    val chrSet = {
      val b = Set.newBuilder[String]
      for(g <- genes) b += g.chr
      b.result
    }
    debug("chrSet:%s", chrSet)

    // Extract chr names
    val chrNamePattern = """chr([0-9]+|[XY])""".r.pattern
    val commonChrSet = chrSet.filter(chr => chrNamePattern.matcher(chr).matches())
    debug("common chr set:%s", commonChrSet)


    // Extract a subset of gene information
    info("Tuple")
    val tuples = genes.map(g => (g.name,  g.chr, g.strand, g.start, g.end))
    debug("tuples:%s, ..", tuples.take(5).mkString(", "))

    import xerial.silk.util.TimeMeasure._

    info("Parallel collection")
    time("gene report", repeat=3) {
      block("single core") {
        val geneReport = genes.map(_.toString)
      }
      block("parallel") {
        val geneReport = genes.par.map(_.toString)
      }
    }
  }


  /**
   * Gene information
   * @param name
   * @param refSeqName
   * @param chr
   * @param strand
   * @param start
   * @param end
   * @param cdsStart
   * @param cdsEnd
   * @param exonCount
   * @param exonStarts
   * @param exonEnds
   */
  class UCSCGene(val name: String,
                 val refSeqName: String,
                 val chr: String,
                 val strand: String,
                 val start: Int,
                 val end: Int,
                 val cdsStart: Int,
                 val cdsEnd: Int,
                 val exonCount: Int,
                 val exonStarts: Array[Int],
                 val exonEnds: Array[Int]
                  ) {
    override def toString = {
      "name:%s refSeqName:%s, %s, %s:%d-%d".format(name, refSeqName, strand, chr, start, end)
    }

  }


  object UCSCGene {

    def parse(line: String): Option[UCSCGene] = {

      def splitByComma(s: String): Array[Int] = {
        val ss = if (s.endsWith(",")) s.slice(0, s.length - 1) else s
        ss.split(",").map(x => x.toInt)
      }

      val c = line.split("\\t")
      if (c.length != 11) {
        error("Wrong number of columns:%d\n%s", c.length, line)
        // Report None instead of issuing an error
        None
      }
      else {
        Some(new UCSCGene(c(0), c(1), c(2), c(3), c(4).toInt, c(5).toInt, c(6).toInt, c(7).toInt, c(8).toInt,
          splitByComma(c(9)), splitByComma(c(10))))
      }
    }
  }


}