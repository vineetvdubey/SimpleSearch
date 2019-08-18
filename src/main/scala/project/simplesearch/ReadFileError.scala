package project.simplesearch

sealed trait ReadFileError

case object MissingPathArg extends ReadFileError {
  override def toString = "Missing argument folder path"
}

case class FileNotFound(t: Throwable) extends ReadFileError

case class NotDirectory(error: String) extends ReadFileError {
  override def toString = error
}