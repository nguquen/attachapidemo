name := "attachapidemo"

version := "1.0"

scalaVersion := "2.11.8"

// Add tools.jar to classpath
// https://blogs.oracle.com/CoreJavaTechTips/entry/the_attach_api
//unmanagedJars in Compile := (file(System.getProperty("java.home")) / ".." / "lib" * "tools.jar").classpath
unmanagedJars in Compile ~= {uj =>
  Seq(Attributed.blank(file(System.getProperty("java.home").dropRight(3)+"lib/tools.jar"))) ++ uj
}

packageOptions in (Compile, packageBin) += Package.ManifestAttributes(
  "Main-Class"  -> "demo.AttachMain",
  "Agent-Class" -> "demo.Agent"
)

