---
layout: tutorial
title: "External CSV libraries"
section: tutorial
sort_order: 21
---
kantan.csv comes with a default implementation of CSV parsing and serializing. This implementation is
[relatively fast](benchmarks.html) and robust, but might not satisfy all use cases - some of the more outlandish CSV
mutations are not implemented (yet), for instance. For these cases, it's possible to use other CSV libraries under the
hood.

## Supported libraries

### Jackson CSV

The [jackson csv] parser and serializer can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-jackson" % "@VERSION@"
```

You then need to bring the right implicits in scope through:

```scala mdoc:silent
import kantan.csv.engine.jackson.*
```

You can tweak the behaviour of the underlying parsers and serializers by creating them through
[`readerEngineFrom`]({{ site.baseurl }}/api/kantan/csv/engine/jackson/index.html#readerEngineFrom(f:kantan.csv.engine.jackson.CSVSchemaBuilder):kantan.csv.engine.ReaderEngine)
and [`writerEngineFrom`]({{ site.baseurl }}/api/kantan/csv/engine/jackson/index.html#writerEngineFrom(f:kantan.csv.engine.jackson.CSVSchemaBuilder):kantan.csv.engine.WriterEngine).


### Apache Commons CSV

The [commons csv] parser and serializer can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-commons" % "@VERSION@"
```

You then need to bring the right implicits in scope through:

```scala mdoc:silent
import kantan.csv.engine.commons.*
```

You can tweak the behaviour of the underlying parsers and serializers by creating them through
[`readerEngineFrom`]({{ site.baseurl }}/api/kantan/csv/engine/commons/index.html#readerEngineFrom(f:kantan.csv.engine.commons.package.CSVFormatBuilder):kantan.csv.engine.ReaderEngine)
and [`writerEngineFrom`]({{ site.baseurl }}/api/kantan/csv/engine/commons/index.html#writerEngineFrom(f:kantan.csv.engine.commons.package.CSVFormatBuilder):kantan.csv.engine.WriterEngine).


## Supporting a new library

For the purpose of this tutorial, let's make up an hypothetical CSV library, EasyCSV, that provides the following:

```scala mdoc:silent
import java.io.*

object EasyCSV {
  trait EasyWriter {
    def write(row: Array[String]): Unit
    def close(): Unit
  }

  def read(
    reader: Reader,
    sep: Char
  ): java.util.Iterator[Array[String]] with Closeable = ???
  def write(writer: Writer, sep: Char): EasyWriter = ???
}
```


### Parsing

Support for parsing with external libraries is handled through the [`ReaderEngine`] trait: all functions that need
to create an instance of [`CsvReader`] rely on an implicit instance of [`ReaderEngine`] to do so.

Creating a new instance of [`ReaderEngine`] is meant to be fairly straightforward: there's a helper
[`ReaderEngine.from`] method that takes care of this. It still means we need to be able to write a
`(Reader, Char) => CsvReader[Seq[String]]`, but the most common scenario is already covered: if you can write a
`(Reader, Char) => Iterator[Seq[String]]`, you can simply use [`ResourceIterator.fromIterator`]:

```scala mdoc:silent
import kantan.csv.*
import kantan.csv.engine.*

implicit val readerEngine: ReaderEngine =
  ReaderEngine.from { (in: Reader, conf: CsvConfiguration) =>
    kantan.codecs.resource.ResourceIterator.fromIterator(
      javaIterator(
        EasyCSV.read(in, conf.cellSeparator)
      )
    )
  }
```

### Serializing

Serializing is very similar to parsing, except that instead of providing a [`ReaderEngine`], you need to provide a
[`WriterEngine`]. This is achieved through [`WriterEngine.from`], the argument to which you most likely want to create
through [`CsvWriter.apply`]:

```scala mdoc:silent
implicit val writerEngine: WriterEngine = WriterEngine.from {
  (writer: Writer, conf: CsvConfiguration) =>
    CsvWriter(EasyCSV.write(writer, conf.cellSeparator))(_ write _.toArray)(
      _.close()
    )
}
```

[commons csv]:https://commons.apache.org/proper/commons-csv/
[jackson csv]:https://github.com/FasterXML/jackson-dataformats-text
[`ReaderEngine`]:{{ site.baseurl }}/api/kantan/csv/engine/ReaderEngine.html
[`WriterEngine`]:{{ site.baseurl }}/api/kantan/csv/engine/WriterEngine.html
[`CsvReader`]:{{ site.baseurl }}/api/kantan/csv/package$$CsvReader.html
[`ReaderEngine.from`]:{{ site.baseurl }}/api/kantan/csv/engine/ReaderEngine$.html#from(f:(java.io.Reader,Char)=>kantan.csv.CsvReader[Seq[String]]):kantan.csv.engine.ReaderEngine
[`CsvReader.fromResource`]:{{ site.baseurl }}/api/#kantan.csv.CsvReader$@fromResource[I,R](in:I)(open:I=>Iterator[R])(release:I=>Unit):kantan.csv.CsvReader[kantan.csv.ParseResult[R]]
[`WriterEngine.from`]:{{ site.baseurl }}/api/kantan/csv/engine/WriterEngine$.html#from(f:(java.io.Writer,Char)=>kantan.csv.CsvWriter[Seq[String]]):kantan.csv.engine.WriterEngine
[`CsvWriter.apply`]:{{ site.baseurl }}/api/kantan/csv/CsvWriter$.html#apply[A](out:A)(w:(A,Seq[String])=>Unit)(r:A=>Unit):kantan.csv.CsvWriter[Seq[String]]
[`ResourceIterator.fromIterator`]:https://javadoc.io/doc/io.github.kantan-scala/kantan.codecs_3/latest/kantan/codecs/resource/ResourceIterator$.html
