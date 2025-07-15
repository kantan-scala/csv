scalaVersion := "2.13.16"

enablePlugins(DocumentationPlugin)

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.14.0",
  "io.github.kantan-scala" %% "kantan.csv-java8" % kantanCsvVersion,
  "io.github.kantan-scala" %% "kantan.csv-scalaz" % kantanCsvVersion,
  "io.github.kantan-scala" %% "kantan.csv-cats" % kantanCsvVersion,
  "io.github.kantan-scala" %% "kantan.csv-generic" % kantanCsvVersion,
  "io.github.kantan-scala" %% "kantan.csv-refined" % kantanCsvVersion,
  "io.github.kantan-scala" %% "kantan.csv-enumeratum" % kantanCsvVersion,
  "io.github.kantan-scala" %% "kantan.csv-commons" % kantanCsvVersion,
  "io.github.kantan-scala" %% "kantan.csv-jackson" % kantanCsvVersion,
  "io.github.kantan-scala" %% "kantan.csv-java8" % kantanCsvVersion
)
