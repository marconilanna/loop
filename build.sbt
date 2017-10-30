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
val lib = Dependencies
val utf8 = java.nio.charset.StandardCharsets.UTF_8.toString

/*
 * Project definition
 */

lazy val root = project.in(file(".")).settings(
  name := "Loop"
, description := "Minimalist toy programming language"
, commonSettings
)

lazy val commonSettings =
  projectMetadata ++
  projectLayout ++
  scalacConfiguration ++
  scaladocConfiguration ++
  dependencies ++
  sbtOptions ++
  staticAnalysis ++
  codeCoverage

/*
 * Project metadata
 */

val projectMetadata = Seq(
  version := "0.1"
, startYear := Option(2017)
, licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")
, developers := List(
    Developer("marconilanna", "Marconi Lanna", "@marconilanna", url("https://github.com/marconilanna"))
  )
)

/*
 * Project layout
 */

val projectLayout = Seq(
  scalaSource in Compile := baseDirectory.value / "src"
, scalaSource in Test := baseDirectory.value / "test"
, javaSource in Compile := scalaSource.in(Compile).value
, javaSource in Test := scalaSource.in(Test).value
, resourceDirectory in Compile := scalaSource.in(Compile).value / "resources"
, resourceDirectory in Test := scalaSource.in(Test).value / "resources"
, sourcesInBase := false
)

/*
 * scalac configuration
 */

val coreScalacOptions = Seq(
  "-encoding", utf8 // Specify character encoding used by source files
, "-target:jvm-" + lib.v.jvm // Target platform for object files
, "-Xexperimental" // Enable experimental extensions
, "-Xfuture" // Turn on future language features
)

val commonScalacOptions = Seq(
  "-deprecation" // Emit warning and location for usages of deprecated APIs
, "-feature" // Emit warning and location for usages of features that should be imported explicitly
, "-g:vars" // Set level of generated debugging info: none, source, line, vars, notailcalls
, "-opt:l:inline" // Enable optimizations (see list below)
, "-opt-inline-from:**" // Classfile names from which to allow inlining
, "-opt-warnings:at-inline-failed" // Enable optimizer warnings: detailed warning for each @inline method call that could not be inlined
, "-unchecked" // Enable additional warnings where generated code depends on assumptions
, "-Xfatal-warnings" // Fail the compilation if there are any warnings
, "-Xlint:_" // Enable or disable specific warnings (see list below)
, "-Yno-adapted-args" // Do not adapt an argument list to match the receiver
, "-Ywarn-dead-code" // Warn when dead code is identified
, "-Ywarn-extra-implicit" // Warn when more than one implicit parameter section is defined
, "-Ywarn-macros:before" // Enable lint warnings on macro expansions (see list below)
, "-Ywarn-numeric-widen" // Warn when numerics are widened
, "-Ywarn-unused:_" // Enable or disable specific (see list below)
)

val compileScalacOptions = Seq(
  "-Ywarn-value-discard" // Warn when non-Unit expression results are unused
)

val testScalacOptions = Seq(
  "-Xcheckinit" // Wrap field accessors to throw an exception on uninitialized access
)

val consoleScalacOptions = Seq(
  "-language:_" // Enable or disable language features (see list below)
, "-nowarn" // Generate no warnings
)

val scalacConfiguration = Seq(
  scalaVersion := lib.v.scala
, scalacOptions ++= coreScalacOptions ++ commonScalacOptions ++ compileScalacOptions
, scalacOptions in (Test, compile) := coreScalacOptions ++ commonScalacOptions ++ testScalacOptions
, scalacOptions in (Compile, console) := coreScalacOptions ++ consoleScalacOptions
, scalacOptions in (Test, console) := scalacOptions.in(Compile, console).value
, compileOrder := CompileOrder.JavaThenScala
)

/*
 * Scaladoc configuration
 */

val scaladocConfiguration = Seq(
  autoAPIMappings := true
, apiMappings += (
    scalaInstance.value.libraryJar -> url(s"http://www.scala-lang.org/api/${scalaVersion.value}/")
  )
, scalacOptions in (Compile, doc) := coreScalacOptions ++ Seq("-author", "-groups", "-implicits")
, scalacOptions in (Test, doc) := scalacOptions.in(Compile, doc).value
)

/*
 * Managed dependencies
 */

val dependencies = Seq(
  libraryDependencies ++= lib.commonDependencies ++ lib.testDependencies
, resolvers ++= lib.resolvers
)

/*
 * sbt options
 */

val consoleDefinitions = """
import
  scala.annotation.{switch, tailrec}
, scala.beans.{BeanProperty, BooleanBeanProperty}
, scala.collection.JavaConverters._
, scala.collection.{breakOut, mutable}
, scala.concurrent.{Await, ExecutionContext, Future}
, scala.concurrent.ExecutionContext.Implicits.global
, scala.concurrent.duration._
, scala.math._
, scala.util.{Failure, Random, Success, Try}
, scala.util.control.NonFatal
, java.io._
, java.net._
, java.nio.file._
, java.time.{Duration => jDuration, _}
, java.util.{Locale, UUID}
, java.util.regex.{Matcher, Pattern}
, System.{currentTimeMillis => now, nanoTime}

def time[T](f: => T): T = {
  val start = now
  try f finally {
    println("Elapsed: " + (now - start)/1000.0 + " s")
  }
}
"""

val desugarMacro = """
import
  scala.language.experimental.macros
, scala.reflect.macros.blackbox

def desugarImpl[T](c: blackbox.Context)(expr: c.Expr[T]): c.Expr[Unit] = {
  import c.universe._, scala.io.AnsiColor.{BOLD, GREEN, RESET}

  val exp = show(expr.tree)
  val typ = expr.actualType.toString takeWhile '('.!=

  println(s"$exp: $BOLD$GREEN$typ$RESET")
  reify { (): Unit }
}

def desugar[T](expr: T): Unit = macro desugarImpl[T]
"""

val sbtOptions = Seq(
  // Statements executed when starting the Scala REPL (sbt's `console` task)
  initialCommands := consoleDefinitions + desugarMacro
, initialCommands in consoleProject := consoleDefinitions
  // Improved incremental compilation
, incOptions := incOptions.value.withNameHashing(true)
  // Improved dependency management
, updateOptions := updateOptions.value.withCachedResolution(true)
  // Clean locally cached project artifacts
, publishLocal := publishLocal
    .dependsOn(cleanCache.toTask(""))
    .dependsOn(cleanLocal.toTask(""))
    .value
  // Share history among all projects instead of using a different history for each project
, historyPath := Option(target.in(LocalRootProject).value / ".history")
  // Do not exit sbt when Ctrl-C is used to stop a running app
, cancelable in Global := true
, logLevel in Global := Level.Info
, logBuffered in Test := false
, showSuccess := true
, showTiming := true
  // ScalaTest configuration
, testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest
    // F: show full stack traces
    // S: show short stack traces
    // D: show duration for each test
    // I: print "reminders" of failed and canceled tests at the end of the summary,
    //    eliminating the need to scroll and search to find failed or canceled tests.
    //    replace with G (or T) to show reminders with full (or short) stack traces
    // K: exclude canceled tests from reminder
  , "-oDI"
    // enforce chosen testing styles
  , "-y", "org.scalatest.FreeSpec"
  , "-y", "org.scalatest.AsyncFreeSpec"
    // Periodic notification of slowpokes (tests that have been running longer than 30s)
  , "-W", "30", "30"
  )
  // Enable colors in Scala console (2.11.4+)
, initialize ~= { _ =>
    val ansi = System.getProperty("sbt.log.noformat", "false") != "true"
    if (ansi) System.setProperty("scala.color", "true")
  }
  // Clear the console between triggered runs (e.g, ~test)
, triggeredMessage := Watched.clearWhenTriggered
, shellPrompt := { state =>
    import scala.Console.{BLUE, BOLD, RESET}
    s"$BLUE$BOLD${name.value}$RESET $BOLD\u25b6$RESET "
  }
)

addCommandAlias("cd", "project")
addCommandAlias("ls", "projects")
addCommandAlias("cr", ";clean ;reload")
addCommandAlias("cru", ";clean ;reload ;test:update")
addCommandAlias("du", "dependencyUpdates")
addCommandAlias("rdu", ";reload ;dependencyUpdates")
addCommandAlias("ru", ";reload ;test:update")
addCommandAlias("tc", "test:compile")

// Uncomment to enable offline mode
//offline in ThisBuild := true

/*
 * Scalastyle: http://www.scalastyle.org/
 */

val scalastyleConfiguration = Seq(
  scalastyleConfig := baseDirectory.in(LocalRootProject).value / "project" / "scalastyle-config.xml"
, scalastyleConfig in Test := baseDirectory.in(LocalRootProject).value / "project" / "scalastyle-test-config.xml"
, scalastyleFailOnError := true
, test in Test := test.in(Test)
    .dependsOn(scalastyle.in(Test).toTask(""))
    .dependsOn(scalastyle.in(Compile).toTask(""))
    .value
)

/*
 * WartRemover: http://github.com/wartremover/wartremover
 */

val wartremoverConfiguration = Seq(
  wartremoverErrors ++= Seq(
    Wart.Any
  , Wart.AnyVal
  , Wart.ArrayEquals
  , Wart.AsInstanceOf
  , Wart.EitherProjectionPartial
  , Wart.ExplicitImplicitTypes
  , Wart.FinalCaseClass
  , Wart.IsInstanceOf
  , Wart.JavaConversions
  , Wart.JavaSerializable
  , Wart.LeakingSealed
  , Wart.Nothing
  , Wart.Null
  , Wart.Option2Iterable
  , Wart.OptionPartial
  , Wart.Product
  , Wart.Return
  , Wart.Serializable
  , Wart.TraversableOps
  , Wart.TryPartial
  , Wart.Var
  , ContribWart.ExposedTuples
  , ContribWart.OldTime
  , ContribWart.SealedCaseClass
  , ContribWart.SomeApply
  , ExtraWart.EnumerationPartial
  , ExtraWart.FutureObject
  , ExtraWart.GenTraversableLikeOps
  , ExtraWart.GenTraversableOnceOps
  , ExtraWart.ScalaGlobalExecutionContext
  , ExtraWart.ThrowablePartial
  , ExtraWart.TraversableOnceOps
  )
)

/*
 * Scapegoat: http://github.com/sksamuel/scapegoat
 */

val scapegoatConfiguration = Seq(
  scapegoatVersion := lib.v.scapegoat
, scapegoatDisabledInspections := Seq.empty
, scapegoatIgnoredFiles := Seq.empty
, scapegoatReports := Seq("none")
, test in Test := test.in(Test)
    .dependsOn(scapegoat.in(Test))
    .dependsOn(scapegoat.in(Compile))
    .value
)

/*
 * Linter: http://github.com/HairyFotr/linter
 */

val linterConfiguration = Seq(
  addCompilerPlugin(lib.linter)
, scalacOptions += "-P:linter:enable-only:" +
    "AssigningOptionToNull+" +
    "AvoidOptionCollectionSize+" +
    "AvoidOptionMethod+" +
    "AvoidOptionStringSize+" +
    "BigDecimalNumberFormat+" +
    "BigDecimalPrecisionLoss+" +
    "CloseSourceFile+" +
    "ContainsTypeMismatch+" +
    "DecomposingEmptyCollection+" +
    "DivideByOne+" +
    "DivideByZero+" +
    "DuplicateIfBranches+" +
    "DuplicateKeyInMap+" +
    "EmptyStringInterpolator+" +
    "FilterFirstThenSort+" +
    "FloatingPointNumericRange+" +
    "FuncFirstThenMap+" +
    "IdenticalCaseBodies+" +
    "IdenticalCaseConditions+" +
    "IdenticalIfCondition+" +
    "IdenticalIfElseCondition+" +
    "IdenticalStatements+" +
    "IfDoWhile+" +
    "IndexingWithNegativeNumber+" +
    "InefficientUseOfListSize+" +
    "IntDivisionAssignedToFloat+" +
    "InvalidParamToRandomNextInt+" +
    "InvalidStringConversion+" +
    "InvalidStringFormat+" +
    "InvariantCondition+" +
    "InvariantExtrema+" +
    "InvariantReturn+" +
    "JavaConverters+" +
    "LikelyIndexOutOfBounds+" +
    "MalformedSwap+" +
    "MergeMaps+" +
    "MergeNestedIfs+" +
    "ModuloByOne+" +
    "NumberInstanceOf+" +
    "OnceEvaluatedStatementsInBlockReturningFunction+" +
    "OperationAlwaysProducesZero+" +
    "OptionOfOption+" +
    "PassPartialFunctionDirectly+" +
    "PatternMatchConstant+" +
    "PossibleLossOfPrecision+" +
    "PreferIfToBooleanMatch+" +
    "ProducesEmptyCollection+" +
    "ReflexiveAssignment+" +
    "ReflexiveComparison+" +
    "RegexWarning+" +
    "StringMultiplicationByNonPositive+" +
    "SuspiciousMatches+" +
    "SuspiciousPow+" +
    "TransformNotMap+" +
    "TypeToType+" +
    "UndesirableTypeInference+" +
    "UnextendedSealedTrait+" +
    "UnitImplicitOrdering+" +
    "UnlikelyEquality+" +
    "UnlikelyToString+" +
    "UnnecessaryMethodCall+" +
    "UnnecessaryReturn+" +
    "UnnecessaryStringIsEmpty+" +
    "UnnecessaryStringNonEmpty+" +
    "UnsafeAbs+" +
    "UnthrownException+" +
    "UnusedForLoopIteratorValue+" +
    "UnusedParameter+" +
    "UseAbsNotSqrtSquare+" +
    "UseCbrt+" +
    "UseConditionDirectly+" +
    "UseContainsNotExistsEquals+" +
    "UseCountNotFilterLength+" +
    "UseExistsNotCountCompare+" +
    "UseExistsNotFilterIsEmpty+" +
    "UseExistsNotFindIsDefined+" +
    "UseExp+" +
    "UseExpm1+" +
    "UseFilterNotFlatMap+" +
    "UseFindNotFilterHead+" +
    "UseFlattenNotFilterOption+" +
    "UseFuncNotFold+" +
    "UseFuncNotReduce+" +
    "UseFuncNotReverse+" +
    "UseGetOrElseNotPatMatch+" +
    "UseGetOrElseOnOption+" +
    "UseHeadNotApply+" +
    "UseHeadOptionNotIf+" +
    "UseHypot+" +
    "UseIfExpression+" +
    "UseInitNotReverseTailReverse+" +
    "UseIsNanNotNanComparison+" +
    "UseIsNanNotSelfComparison+" +
    "UseLastNotApply+" +
    "UseLastNotReverseHead+" +
    "UseLastOptionNotIf+" +
    "UseLog10+" +
    "UseLog1p+" +
    "UseMapNotFlatMap+" +
    "UseMinOrMaxNotSort+" +
    "UseOptionExistsNotPatMatch+" +
    "UseOptionFlatMapNotPatMatch+" +
    "UseOptionFlattenNotPatMatch+" +
    "UseOptionForallNotPatMatch+" +
    "UseOptionForeachNotPatMatch+" +
    "UseOptionGetOrElse+" +
    "UseOptionIsDefinedNotPatMatch+" +
    "UseOptionIsEmptyNotPatMatch+" +
    "UseOptionMapNotPatMatch+" +
    "UseOptionOrNull+" +
    "UseOrElseNotPatMatch+" +
    "UseQuantifierFuncNotFold+" +
    "UseSignum+" +
    "UseSqrt+" +
    "UseTakeRightNotReverseTakeReverse+" +
    "UseUntilNotToMinusOne+" +
    "UseZipWithIndexNotZipIndices+" +
    "VariableAssignedUnusedValue+" +
    "WrapNullWithOption+" +
    "YodaConditions+" +
    "ZeroDivideBy"
)

val staticAnalysis =
  scalastyleConfiguration ++
  wartremoverConfiguration ++
  scapegoatConfiguration ++
  linterConfiguration

/*
 * scoverage: http://github.com/scoverage/sbt-scoverage
 */

val codeCoverage = Seq(
  coverageMinimum := 90
, coverageFailOnMinimum := true
, coverageOutputCobertura := false
, coverageOutputHTML := true
, coverageOutputXML := false
)
