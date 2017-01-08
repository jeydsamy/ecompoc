name := "ecompoc"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)


scalaVersion := "2.11.8"

libraryDependencies += javaJdbc
libraryDependencies += cache
libraryDependencies += javaWs


libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1212.jre7"

libraryDependencies ++= Seq(
  	javaJpa,
    javaWs,
  	"org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",// replace by your jpa implementation
    "com.amazonaws" % "aws-java-sdk" % "1.11.76",
     "javax.mail" % "mail" % "1.4.3"
)

javaOptions in Test ++= Seq(
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9998",
  "-Xms512M",
  "-Xmx1536M",
  "-Xss1M",
  "-XX:MaxPermSize=384M"
)