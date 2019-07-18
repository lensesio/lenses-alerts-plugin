addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.9")

resolvers += Resolver.bintrayIvyRepo("rallyhealth", "sbt-plugins")
addSbtPlugin("com.rallyhealth.sbt" % "sbt-git-versioning" % "1.2.1")

resolvers += "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com"
addSbtPlugin("ohnosequences" % "sbt-github-release" % "0.7.1")

