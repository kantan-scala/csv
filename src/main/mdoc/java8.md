---
layout: tutorial
title: "Java 8 dates and times"
section: tutorial
sort_order: 27
---
Java 8 comes with a better thought out dates and times API. Unfortunately, it cannot be supported as part of the core
kantan.csv API - we still support Java 7. There is, however, a dedicated optional module that you can include by
adding the following line to your `build.sbt` file:

```scala
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-java8" % "@VERSION@"
```

You then need to import the corresponding package:

```scala mdoc:silent
import kantan.csv.java8._
```

kantan.csv has default, ISO 8601 compliant [`CellDecoder`] and [`CellEncoder`] instances for the following types:

* [`Instant`]
* [`LocalDateTime`]
* [`ZonedDateTime`]
* [`OffsetDateTime`]
* [`LocalDate`]
* [`LocalTime`]

Let's imagine for example that we want to extract dates from the following string:

```scala mdoc:silent
import java.time._
import kantan.csv._
import kantan.csv.ops._

val plainInput = "1,1978-12-10\n2,2015-01-09"
```

This is directly supported:

```scala mdoc
val res = plainInput.unsafeReadCsv[List, (Int, LocalDate)](rfc)

res.asCsv(rfc)
```

It's also possible to declare your own [`CellDecoder`] and [`CellEncoder`] instances. Let's take, for example,
the following custom format:

```scala mdoc:reset:silent
import java.time.format.DateTimeFormatter
import java.time._
import kantan.csv._
import kantan.csv.java8._
import kantan.csv.ops._

val input = "1,10-12-1978\n2,09-01-2015"

val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
```

We then need to build a decoder for it and stick it in the implicit scope:

```scala mdoc:silent
implicit val decoder: CellDecoder[LocalDate] = localDateDecoder(format)
```

And we're done:

```scala mdoc
val result = input.unsafeReadCsv[List, (Int, LocalDate)](rfc)
```

Similarly, this is how you create and encoder:

```scala mdoc:silent
implicit val encoder: CellEncoder[LocalDate] = localDateEncoder(format)
```

And you can now easily encode data that contains instances of [`LocalDate`]:

```scala mdoc
result.asCsv(rfc)
```

Note that if you're going to both encode and decode dates, you can create a [`CellCodec`] in a single call instead:

```scala mdoc:silent
val codec: CellCodec[LocalDate] = localDateCodec(format)
```

Note that while you can pass a [`DateTimeFormatter`] directly, the preferred way of dealing with pattern strings is to
use the literal syntax provided by kantan.csv:

```scala mdoc:silent
localDateDecoder(fmt"dd-MM-yyyy")
```

The advantage is that this is checked at compile time - invalid pattern strings will cause a compilation error:

```scala mdoc:fail
localDateDecoder(fmt"FOOBAR")
```

[`GroupDecoder`]:{{ site.baseurl }}/api/kantan/regex/package$$GroupDecoder.html
[`Instant`]:https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html
[`LocalDateTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
[`OffsetDateTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/OffsetDateTime.html
[`ZonedDateTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html
[`LocalDate`]:https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
[`LocalTime`]:https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html
[`DateTimeFormatter`]:http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormatter.html
[`CellDecoder`]:{{ site.baseurl }}/api/kantan/csv/package$$CellDecoder.html
[`CellEncoder`]:{{ site.baseurl }}/api/kantan/csv/package$$CellEncoder.html
[`CellCodec`]:{{ site.baseurl }}/api/kantan/csv/package$$CellCodec.html
