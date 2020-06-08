[![Build Status](https://img.shields.io/travis/stringbean/text-utils/master.svg)](https://travis-ci.org/stringbean/text-utils)
[![Codacy Grade](https://img.shields.io/codacy/grade/7b07263adca449ee8e51133b87af5030.svg?label=codacy)](https://www.codacy.com/app/stringbean/text-utils)
[![Test Coverage](https://img.shields.io/codecov/c/github/stringbean/text-utils/master.svg)](https://codecov.io/gh/stringbean/text-utils)
[![Maven Central - Scala 2.11](https://img.shields.io/maven-central/v/software.purpledragon/text-utils_2.11.svg?label=scala%202.11)](https://search.maven.org/search?q=g:software.purpledragon%20a:text-utils_2.11)
[![Maven Central - Scala 2.12](https://img.shields.io/maven-central/v/software.purpledragon/text-utils_2.12.svg?label=scala%202.12)](https://search.maven.org/search?q=g:software.purpledragon%20a:text-utils_2.12)
[![Maven Central - Scala 2.13](https://img.shields.io/maven-central/v/software.purpledragon/text-utils_2.13.svg?label=scala%202.13)](https://search.maven.org/search?q=g:software.purpledragon%20a:text-utils_2.13)

# Simple Text Utils for Scala

`text-utils` is a collection of useful utilities for formatting text.

## Quickstart

Add the following to your `build.sbt`:

```scala
libraryDependencies += "software.purpledragon" %% "text-utils" % "<version>"
```

## Table Formatting

`TableFormatter` and `SortedTableFormatter` provide mechanisms for building and printing tabular data.

The simplest case is outputting a bare table without headers:

```scala
TableFormatter()
  .addRow("Apples", "25")
  .addRow("Pears", "10")
  .addRow("Bananas", "4")
  .print()
```

Will output:

```text
Apples   25
Pears    10
Bananas  4
```

Tables can also be sorted and have a header:

```scala
SortedTableFormatter("Produce", "Remaining")
  .addRow("Apples", "25")
  .addRow("Pears", "10")
  .addRow("Bananas", "4")
  .print()
```

Will output:

```text
| Produce | Remaining |
-----------------------
| Apples  | 25        |
| Bananas | 4         |
| Pears   | 10        |
```

Full details can be found in the [documentation](https://stringbean.github.io/text-utils/tables.html).