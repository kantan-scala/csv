---
layout: index
---

[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.kantan-scala/kantan.csv_3?style=flat)](https://central.sonatype.com/artifact/io.github.kantan-scala/kantan.csv_3)

kantan.csv is a library for CSV parsing and serialization written in the
[Scala programming language](https://www.scala-lang.org).

## Getting started

kantan.csv is currently available for Scala 2.13 and 3.

The current version is `@VERSION@`, which can be added to your project with one or more of the following line(s)
in your SBT build file:

```scala
// Core library, included automatically if any other module is imported.
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv" % "@VERSION@"

// Java 8 date and time instances.
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-java8" % "@VERSION@"

// Provides scalaz type class instances for kantan.csv, and vice versa.
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-scalaz" % "@VERSION@"

// Provides cats type class instances for kantan.csv, and vice versa.
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-cats" % "@VERSION@"

// Automatic type class instances derivation.
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-generic" % "@VERSION@"

// Provides instances for refined types.
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-refined" % "@VERSION@"

// Provides instances for enumeratum types.
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-enumeratum" % "@VERSION@"
```

Additionally, while kantan.csv comes with a default parser / serializer (that has
[pretty good]({{ site.baseurl }}/benchmarks.html) performances), some people might prefer to use older, more
reputable implementations. The following engines are currently supported:

```scala
// commons-csv engine.
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-commons" % "@VERSION@"

// jackson-csv engine.
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-jackson" % "@VERSION@"
```


## Motivation

CSV is an unreasonably popular data exchange format. It suffers from poor (or at the very least late) standardisation,
and is often a nightmare to work with when it contains more complex data than just lists of numerical values.

I started writing kantan.csv when I realised I was spending more time dealing with the data _container_ than the
data itself. My goal is to abstract CSV away as much as possible and allow developers to describe their data and where
it comes from, and then just work with it.

kantan.csv is meant to be [RFC](https://tools.ietf.org/html/rfc4180) compliant, but flexible enough that it should
parse any sane variation on the format. Should you find CSV files that don't parse, please file an issue and I'll look
into it.

While I'm pretty happy with kantan.csv, or at least the direction it's headed, I do not pretend that it will fit
all use cases. It fits mine, but might not work for everyone. I'm happy to hear suggestions on how this can be
addressed, though.
