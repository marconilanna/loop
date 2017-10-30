/*
 * Copyright 2017 Marconi Lanna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import sbt.Test
import sbt.toGroupID

object Dependencies extends Dependencies

trait Dependencies {
  object v {
    val jvm = "1.8"
    val scala = "2.12.4"
    val scapegoat = "1.3.3"
  }

  // Resolvers
  val resolvers = Seq(
  )

  // Java

  // Scala
  val parserCombinators = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"

  // Test
  val scalatest         = "org.scalatest"          %% "scalatest"                % "3.0.4" % Test

  // Compiler plug-ins
  val linter            = "org.psywerx.hairyfotr"  %% "linter"                   % "0.1.17"

  val commonDependencies = Seq(
    parserCombinators
  )

  val testDependencies = Seq(
    scalatest
  )
}
