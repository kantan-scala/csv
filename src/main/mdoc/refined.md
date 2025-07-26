---
layout: tutorial
title: "Refined module"
section: tutorial
sort_order: 28
---
kantan.csv comes with a [refined](https://github.com/fthomas/refined) module that can be used
by adding the following dependency to your `build.sbt`:

```scala
libraryDependencies += "io.github.kantan-scala" %% "kantan.csv-refined" % "@VERSION@"
```

You then need to import the corresponding package:

```scala mdoc:silent
import kantan.csv.refined.*
```

And that's pretty much it. You can now encode and decode refined types directly.

Let's first set our types up:

```scala mdoc:silent
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import kantan.csv.*
import kantan.csv.ops.*

type PositiveInt = Int Refined Positive
```

We can then simply write the following:

```scala mdoc
"1,2".readCsv[List, List[PositiveInt]](rfc)

"1,-2".readCsv[List, List[PositiveInt]](rfc)
```
