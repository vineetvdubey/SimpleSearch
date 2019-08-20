package project.simplesearch

import java.io.File

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class IndexSpec extends FlatSpec {

  "indexLine method in Index object" should "return an empty HashSet if input string is empty" in {
    val output = Index.indexLine("")
    assert(output.isInstanceOf[Set[String]] && output.isEmpty)
  }
  it should "return all the words in lower case as a set" in {
    val output = Index.indexLine("HAHAHA")
    assert(output.contains("hahaha"))
  }
  it should "return all words delimited by space as a set of strings" in {
    val input = "old is gold"
    val output = Index.indexLine(input)
    val expected = Set("is", "old", "gold")
    output should contain theSameElementsAs expected
  }
  it should "remove all non alphanumeric characters and return all words delimited by space as a set of strings" in {
    val input = "\"My name is Bond\", he said, \"Agent:007-James Bond!\"."
    val output = Index.indexLine(input)
    val expected = Set("my", "name", "is", "bond", "he", "said", "agent", "007", "james")
    output should contain theSameElementsAs expected
  }

  "naiveIndexer method in Index object" should "take in a file and return all words in it as a set" in {
    val file = new File("src/test/scala/project/simplesearch/testFolder/testFile.txt")
    val output = Index.naiveIndexer(file)
    val expected = Set("67890", "är", "detta", "exempel", "932", "12345", "dokument", "på", "ett", "n")
    output should contain theSameElementsAs expected
  }

  "indexSearchScoreResult method" should "return an empty sequence if no matching words are found" in {
    val testIndex = Index(new File("src/test/scala/project/simplesearch/testFolder"))
    val searchString = "qwerty!@#$%^"
    assert(testIndex.indexSearchScoreResult(searchString).isEmpty)
  }
  it should "return 100 score if all words match" in {
    val testIndex = Index(new File("src/test/scala/project/simplesearch/testFolder"))
    val searchString = "12345 dokument"
    val output = testIndex.indexSearchScoreResult(searchString)
    assert(output.head.score == 100)
  }
  it should "return 50 score if half the words match" in {
    val testIndex = Index(new File("src/test/scala/project/simplesearch/testFolder"))
    val searchString = "12345 document"
    val output = testIndex.indexSearchScoreResult(searchString)
    assert(output.head.score == 50)
  }

}