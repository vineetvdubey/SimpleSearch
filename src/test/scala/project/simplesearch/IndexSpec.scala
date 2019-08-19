package project.simplesearch

import org.scalatest.Matchers._
import org.scalatest.{FlatSpec, PrivateMethodTester}

class IndexSpec extends FlatSpec with PrivateMethodTester {

  "Testing indexLine helper method in Index object" should "return an empty HashSet if input string is empty" in {
    val output = Index.indexLine("")
    assert(output.isInstanceOf[Set[String]] && output.isEmpty)
  }
  it should "return all the words in lower case as a set" in {
    assert(Index.indexLine("HAHAHA").contains("hahaha"))
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

}
