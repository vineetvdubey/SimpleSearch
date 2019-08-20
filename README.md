# SimpleSearch

A naive directory indexing program. It indexes all the files in the directory and also provides a search function.

At the current state, the indexing is very basic and limited to maintaining a Set of all the alphanumeric words encountered in the file. A search is made against this index set to determine a search score that indicates the number of search tokens that are matched.


###Steps to run

Inside the sbt console, use

```
sbt
> runMain project.driver.Main folder
 ```
 
 Alternatively,
 
```
sbt
> run folder
```

Here, *folder* represent the directory path that contains the text files to be indexed and searched.


Included some basic unit tests for the core Indexing mechanism.
Try:

```
sbt
> test
```
