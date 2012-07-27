import sbt._
import Keys._

object HelloBuild extends Build {

  resolvers ++= Seq(
    "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "repo.novus rels" at "http://repo.novus.com/releases/",
    "repo.novus snaps" at "http://repo.novus.com/snapshots/")

  lazy val root = Project(id = "polyscalaz",
    base = file("."),
    settings = Project.defaultSettings ++
      Seq(libraryDependencies ++=
        Seq(
          "redis.clients" % "jedis" % "2.0.0",
          "org.scalaz" %% "scalaz-full" % "6.0.4"
        )
      )
  )

}
