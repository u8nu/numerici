// Copyright 2015 sumito3478 <sumito3478@gmail.com>
//
// This software is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by the
// Free Software Foundation; either version 3 of the License, or (at your
// option) any later version.
//
// This software is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
// for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this software. If not, see http://www.gnu.org/licenses/.

// Scalariform Settings
lazy val scalariformSettings = {
  import scalariform.formatter.preferences._
  import com.typesafe.sbt.SbtScalariform
  SbtScalariform.scalariformSettings ++ Seq(
    SbtScalariform.ScalariformKeys.preferences := FormattingPreferences()
      .setPreference(DoubleIndentClassDeclaration, true))
}

// sbt-release settings
lazy val releaseSettings = {
  import sbtrelease.ReleasePlugin.ReleaseKeys._
  sbtrelease.ReleasePlugin.releaseSettings ++ Seq(
    crossBuild := true,
    tagComment <<= (version in ThisBuild) map (v => s"Release $v"),
    commitMessage <<= (version in ThisBuild) map (v => s"Bump version number to $v"))
}

lazy val macroParadiseSettings = Seq(
  resolvers += Resolver.sonatypeRepo("releases"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)
)

lazy val buildInfoSettings = {
  import sbtbuildinfo.Plugin._
  sbtbuildinfo.Plugin.buildInfoSettings ++ Seq(
    sourceGenerators in Compile += buildInfo.taskValue,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "numerici"
  )
}

lazy val settings = Seq(
  organization := "nu.u8",
  scalaVersion := "2.11.7",
  scalaSource in Compile := baseDirectory.value / "src",
  scalaSource in Test := baseDirectory.value / "test",
  resourceDirectory in Compile := baseDirectory.value / "res",
  resourceDirectory in Test := baseDirectory.value / "res-test",
  javaSource in Compile := baseDirectory.value / "src",
  javaSource in Test := baseDirectory.value / "test",
  resolvers += Resolver.sonatypeRepo("releases"),
  licenses += ("LGPL-3.0", url("http://www.gnu.org/licenses/lgpl-3.0.txt")),
  javaOptions := Seq(
    "-Xms200m",
    "-Xmx2G",
    "-XX:ReservedCodeCacheSize=225m",
    "-ea",
    "-XX:+UseConcMarkSweepGC",
    "-XX:SoftRefLRUPolicyMSPerMB=50",
    "-XX:+UseCodeCacheFlushing",
    "-server",
    "-XX:+DoEscapeAnalysis",
    "-XX:+UseCompressedOOps"),
  crossScalaVersions := Seq("2.11.7"),
  fork := true,
  libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _),
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.3" % "test",
    "com.chuusai" %% "shapeless" % "2.2.5",
    "io.netty" % "netty-buffer" % "4.0.33.Final",
    "org.scalatest" %% "scalatest" % "2.2.5" % "test",
    "org.scalaz" %% "scalaz-core" % "7.1.5",
    "org.slf4j" % "slf4j-api" % "1.7.13"),
  scalacOptions ++= Seq(
    "-encoding", "utf-8",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xexperimental",
    "-Xcheckinit",
    "-Xlint")) ++ scalariformSettings ++ releaseSettings ++ macroParadiseSettings

lazy val unnamed = (project in file(".")).settings(settings: _*)
