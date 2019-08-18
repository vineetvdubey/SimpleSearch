package project.simplesearch

import java.io.File

import project.simplesearch.Index.IndexSearchScore

import scala.util.Try

object SimpleSearch {

  def readFile(args: Array[String]): Either[ReadFileError, File] = {
    for {
      path <- args.headOption.toRight(MissingPathArg)
      file <- Try(new java.io.File(path))
        .fold(throwable => Left(FileNotFound(throwable)),
          file =>
            if (file.isDirectory) Right(file)
            else Left(NotDirectory(s"Path [$path] is not a directory "))
        )
    } yield file
  }

  def index(folder: File): Index = {
    Index(folder)
  }

  def iterate(index: Index): Unit = {
    print(s"Enter search query> ")
    scala.io.StdIn.readLine match {
      case specialCmd if specialCmd.trim.startsWith(":quit") =>
        println("Exiting search..")
      case searchQuery: String =>
        topNSearchResult(index.indexSearchScoreResult(searchQuery), n = 10)
          .fold(
            println,
            res => res.map(printIndexSearchScore)
          )
        println()
        iterate(index)
    }
  }

  private def topNSearchResult(resultScores: Seq[IndexSearchScore], n: Int): Either[String, Seq[IndexSearchScore]] = {
    if (resultScores.isEmpty) {
      Left("No result found !!")
    } else {
      Right(
        resultScores
          .sortBy(elem => (-elem.score, elem.file.getName)) // Sort desc order of score, then asc order of name
          .take(math.min(n, resultScores.size))
      )
    }

  }

  private def printIndexSearchScore(indexSearchScore: IndexSearchScore) = {
    println(f"${indexSearchScore.file.getName}%-15s: ${indexSearchScore.score}%%")
  }

}
