  resolvers += Resolver.url("artifactory",
    url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases")
  )(Resolver.ivyStylePatterns)

  resolvers += Resolver.url("sbt-plugin-releases",
    new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
  )(Resolver.ivyStylePatterns)

  resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"


  addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.0.0")

  addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.1.2")

  addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.1")

  addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.0.0")
