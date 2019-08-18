package project.simplesearch

import java.io.{BufferedReader, File, FileReader}

import scala.annotation.tailrec
import scala.collection.immutable.HashSet
import scala.util.Try

case class Index(folder: File) {

  import Index._

  // Regex Reference - https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
  // Used the POSIX Character Class {Alnum} i.e Alphabets as well as numeric characters
  // Note: \p{prop} matches if the input has the property prop, while \P{prop} does not match if the input has that property
  // And enable UNICODE_CHARACTER_CLASS, using (?U)
  private val regexNonAlphaNumeric = "(?U)\\P{Alnum}+"

  /**
   * Takes in a search string and returns a search score against
   * each file's index if a match is found in the file
   *
   * @param searchString
   * @return Seq[IndexSearchScore]
   */
  def indexSearchScoreResult(searchString: String): Seq[IndexSearchScore] = {
    computeFolderSearchScore(searchString)
      .filter(_.score > 0)
  }

  private val fileIndexMap: Map[File, NaiveIndex] = {
    folder
      .listFiles()
      .foldLeft(Map[File, NaiveIndex]()) { (acc, f) => acc + (f -> naiveIndexer(f)) }
  }

  private def computeFolderSearchScore(searchString: String): Seq[IndexSearchScore] = {
    fileIndexMap
      .flatMap[IndexSearchScore](computeFileSearchScore(searchString, _))
      .toSeq
  }

  // Score is calculated as a percentage value of the matching tokens out the total search tokens
  // Score is approximately rounded down due to decimal truncation of the integer
  private def computeFileSearchScore(searchString: String, fileIndexEntry: (File, NaiveIndex)): Option[IndexSearchScore] = {
    val searchTokens = preProcessStringIndex(searchString)
    Try(IndexSearchScore(
      score = (100 * searchTokens.intersect(fileIndexEntry._2).size) / searchTokens.size,
      file = fileIndexEntry._1
    )).toOption
  }

  private def naiveIndexer(file: File): NaiveIndex = {
    @tailrec
    def recursiveIndexBuilder(acc: NaiveIndex, br: BufferedReader): NaiveIndex = {
      Option(br.readLine) match {
        case None => br.close(); acc
        case Some(str) => recursiveIndexBuilder(acc ++ preProcessStringIndex(str), br)
      }
    }

    recursiveIndexBuilder(
      Set.empty,
      new BufferedReader(new FileReader(file))
    )
  }

  private def preProcessStringIndex(line: String): HashSet[String] = {
    val temp = line.replaceAll(regexNonAlphaNumeric, " ").trim.toLowerCase
    if (temp.isEmpty) {
      HashSet.empty
    } else {
      temp.split("\\s+").to(HashSet)
    }
  }

}

object Index {
  type NaiveIndex = Set[String]

  case class IndexSearchScore(score: Int, file: File)

}