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
import org.scalatest.DiagrammedAssertions
import org.scalatest.EitherValues
import org.scalatest.FreeSpec
import org.scalatest.OptionValues
import org.scalatest.TryValues

// scalastyle:off line.size.limit
/**
 * Base trait for unit tests.
 *
 * Use [[Spec `Spec`]] for general tests and
 * [[AsyncSpec `AsyncSpec`]] for non-blocking asynchronous tests.
 * In both tests are nested inside text clauses denoted with the dash operator (`-`).
 *
 * Mixed-in traits include:
 *
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.DiagrammedAssertions `DiagrammedAssertions`]]:
 *  show diagram of expression values when the assertion fails
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.EitherValues `EitherValues`]]:
 *  `left.value` and `right.value` methods for `Either`
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.OptionValues `OptionValues`]]:
 *  `value` method for `Option`
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.TryValues `TryValues`]]:
 *  `success` and `failure` methods for `Try`
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.mockito.MockitoSugar `MockitoSugar`]]:
 *  syntax sugar for Mockito
 *
 * Also consider mixing:
 *
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.Inside `Inside`]]:
 *  make assertions about nested object graphs using pattern matching
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.Inspectors `Inspectors`]]:
 *  enable assertions to be made about collections
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.PartialFunctionValues `PartialFunctionValues`]]:
 *  `valueAt` method for `PartialFunction`
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.PrivateMethodTester `PrivateMethodTester`]]:
 *  testing of private methods
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.prop.TableDrivenPropertyChecks `TableDrivenPropertyChecks`]]:
 *  property checks against tables of data
 *  - [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.selenium.WebBrowser `WebBrowser`]]:
 *  domain specific language for browser-based tests
 *
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.DiagrammedAssertions `DiagrammedAssertions`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.EitherValues `EitherValues`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.OptionValues `OptionValues`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.TryValues `TryValues`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.mockito.MockitoSugar `MockitoSugar`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.Inside `Inside`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.Inspectors `Inspectors`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.PartialFunctionValues `PartialFunctionValues`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.PrivateMethodTester `PrivateMethodTester`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.prop.TableDrivenPropertyChecks `TableDrivenPropertyChecks`]]
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.selenium.WebBrowser `WebBrowser`]]
 */
trait SpecLike
  extends DiagrammedAssertions
  with EitherValues
  with OptionValues
  with TryValues

/**
 * Base class for general tests.
 *
 * Based on [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.FreeSpec `FreeSpec`]]:
 * nested tests are written inside text clauses denoted with the dash operator (-).
 *
 * @see [[http://doc.scalatest.org/3.0.0/index.html#org.scalatest.FreeSpec `FreeSpec`]]
 * @see [[AsyncSpec `AsyncSpec`]] for non-blocking asynchronous tests
 * @see [[SpecLike `SpecLike`]] base trait
 */
abstract class Spec
  extends FreeSpec
  with SpecLike
