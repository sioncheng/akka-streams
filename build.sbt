val AkkaVersion = "2.6.20"
val LogbackVersion = "1.2.3"
val ScalaVersion = "2.13.14"
val AkkaManagementVersion = "1.1.4"
val AkkaProjectionVersion = "1.2.2"
val ScalikeJdbcVersion = "3.5.0"
val AkkaHttpVersion = "10.2.9"
val AkkaGRPC = "2.0.0"
val ScalaTest = "3.1.4"
val JacksonVersion = "2.11.4" 
val AkkaStreamAlpakka = "6.0.1"
val AkkaStreamKafka = "3.0.1"
val SlickVersion = "3.4.1"


lazy val chapter03 = project
  .in(file("chapter03"))
  .settings(
    scalaVersion := ScalaVersion,
    //scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "org.scalatest" %% "scalatest" % ScalaTest % Test,
      "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
      )
    )

lazy val chapter04 = project
  .in(file("chapter04"))
  .settings(
    scalaVersion := ScalaVersion,
    //scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "org.scalatest" %% "scalatest" % ScalaTest % Test,
      "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
      )
    )

lazy val chapter05 = project
  .in(file("chapter05"))
  .settings(
    name := "like-api",
    version := "1.0",

    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "org.scalatest" %% "scalatest" % ScalaTest % Test,
      "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
      "com.typesafe.slick" %% "slick" % SlickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "mysql" % "mysql-connector-java" % "8.0.33",
    )
  )

lazy val chapter07 = project
  .in(file("chapter07"))
  .settings(
    name := "akka-streams",
    version := "1.0",

    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "org.scalatest" %% "scalatest" % ScalaTest % Test,
      "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
      "com.typesafe.slick" %% "slick" % SlickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "mysql" % "mysql-connector-java" % "8.0.33",
      "org.slf4j" % "slf4j-nop" % "1.7.26",
    )
  )


lazy val chapter08 = project
  .in(file("chapter08"))
  .settings(
    //scalafmtOnCompile := true,
    scalaVersion := ScalaVersion,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream-typed" % AkkaVersion,
      "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "6.0.1",
      "com.lightbend.akka" %% "akka-stream-alpakka-file" % "6.0.1",
      "com.lightbend.akka" %% "akka-stream-alpakka-s3" % "6.0.1",
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,//
      "com.typesafe.akka" %% "akka-http-xml" % AkkaHttpVersion,// this is for solving dependency version mismatches
      "com.typesafe.akka" %% "akka-stream-kafka" % AkkaStreamKafka,
      "com.fasterxml.jackson.core" % "jackson-databind" % JacksonVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "org.scalatest" %% "scalatest" % ScalaTest % Test,
    ))

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

ThisBuild / watchTriggeredMessage := Watch.clearScreenOnTrigger
