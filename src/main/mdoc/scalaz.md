---
layout: tutorial
title: "Scalaz module"
section: tutorial
sort_order: 24
---
Kantan.csv has a [scalaz](https://github.com/scalaz/scalaz) module that is, in its current incarnation, fairly bare
bones: it provides decoders for [`Maybe`] and [`\/`] as well as a few useful type class instances.

The `scalaz` module can be used by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-scalaz" % "@VERSION@"
```

You then need to import the corresponding package:

```scala mdoc:silent
import kantan.csv.scalaz.*
```

## `\/` codecs

For any two types `A` and `B` that each have a [`CellEncoder`], there exists a
`CellEncoder[A \/ B]`. If `A` and `B` each have a [`RowEncoder`], there exists a `RowEncoder[A \/ B]`.

By the same token, for any two types `A` and `B` that each have a [`CellDecoder`], there exists a
`CellDecoder[A \/ B]`. If `A` and `B` each have a [`RowDecoder`], there exists a `RowDecoder[A \/ B]`.

First, a few imports:

```scala mdoc:silent
import _root_.scalaz.*
import kantan.csv.*
import kantan.csv.ops.*
```

We can then simply write the following:

```scala mdoc
"1,2\n4,true".readCsv[List, (Int, Int \/ Boolean)](rfc)

"1,2\n4,true".readCsv[List, (Int, Int) \/ (Int, Boolean)](rfc)
```


## `Maybe` decoder

For any type `A` that has:

 * a [`CellDecoder`], there exists a `CellDecoder[Maybe[A]]`
 * a [`RowDecoder`], there exists a `RowDecoder[Maybe[A]]`
 * a [`CellEncoder`], there exists a `CellEncoder[Maybe[A]]`
 * a [`RowEncoder`], there exists a `RowEncoder[Maybe[A]]`

You can write, for example:

```scala mdoc
"1,2\n3,".readCsv[List, (Int, Maybe[Int])](rfc)
```

## Scalaz instances

The following instance for cats type classes are provided:

* [`MonadError`] and [`Plus`] for all decoders ([`CellDecoder`] and [`RowDecoder`]).
* [`Contravariant`] for all encoders ([`CellEncoder`] and [`RowEncoder`]).
* [`Show`] and [`Equal`] for all error types ([`ReadError`] and all its descendants).
* [`RowEncoder`] for any type that has a [`Foldable`].

[`MonadError`]:https://javadoc.io/doc/org.scalaz/scalaz_2.13/7.3.8/scalaz/MonadError.html
[`Plus`]:https://javadoc.io/doc/org.scalaz/scalaz_2.13/7.3.8/scalaz/Plus.html
[`Show`]:https://javadoc.io/doc/org.scalaz/scalaz_2.13/7.3.8/scalaz/Show.html
[`Equal`]:https://javadoc.io/doc/org.scalaz/scalaz_2.13/7.3.8/scalaz/Equal.html
[`Foldable`]:https://javadoc.io/doc/org.scalaz/scalaz_2.13/7.3.8/scalaz/Foldable.html
[`\/`]:https://javadoc.io/doc/org.scalaz/scalaz_2.13/7.3.8/scalaz/$bslash$div.html
[`Maybe`]:https://javadoc.io/doc/org.scalaz/scalaz_2.13/7.3.8/scalaz/Maybe.html
[`CellEncoder`]:{{ site.baseurl }}/api/kantan/csv/package$$CellEncoder.html
[`CellDecoder`]:{{ site.baseurl }}/api/kantan/csv/CellDecoder$.html
[`RowDecoder`]:{{ site.baseurl }}/api/kantan/csv/RowDecoder$.html
[`RowEncoder`]:{{ site.baseurl }}/api/kantan/csv/package$$RowEncoder.html
[`ReadError`]:{{ site.baseurl }}/api/kantan/csv/ReadError.html
[`Contravariant`]:https://javadoc.io/doc/org.scalaz/scalaz_2.13/7.3.8/scalaz/Contravariant.html
