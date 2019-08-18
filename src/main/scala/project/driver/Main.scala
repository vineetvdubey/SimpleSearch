package project.driver

import project.simplesearch.SimpleSearch

object Main extends App {

  SimpleSearch
    .readFile(args)
    .fold(
      println,
      file => SimpleSearch.iterate(SimpleSearch.index(file))
    )

}