name := "seed"

version := "0.1"

scalaVersion := "2.12.7"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, JavaAppPackaging)
  .settings(
    libraryDependencies ++= Seq(
      guice,
      evolutions,
      "net.codingwell" %% "scala-guice" % "4.1.0",
      "com.typesafe.play" %% "play-slick" % "3.0.1",
      "com.typesafe.play" %% "play-slick-evolutions" % "3.0.1",
      "com.byteslounge" %% "slick-repo" % "1.4.3",
      "org.sangria-graphql" %% "sangria" % "1.4.2",
      "org.sangria-graphql" %% "sangria-play-json" % "1.0.4",
      "org.postgresql" % "postgresql" % "9.4.1212",
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
      "org.mockito" % "mockito-core" % "2.13.0" % Test
    )
  )
