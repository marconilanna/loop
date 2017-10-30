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
import scala.collection.mutable
import scala.util.parsing.combinator.RegexParsers

object Loop extends RegexParsers {
  type Expr = () => Unit

  private val terms = mutable.Map.empty[String, Int]

  private val intRegex = """[1-9][0-9]*""".r
  private val stringRegex = """[a-zA-Z_]+""".r

  private val assignment = "="

  private def run(expr: List[Expr]) = expr foreach (_.apply)

  // scalastyle:off multiple.string.literals // doesn't play well with string interpolation
  private def validate(term: String) = {
    if (!terms.contains(term)) throw new Exception(s"Variable not found: '$term'")
  }

  def number: Parser[Int] = intRegex ^^ { _.toInt }

  def term: Parser[String] = stringRegex

  def expr: Parser[List[Expr]] = (init | inc | loop).+

  def init: Parser[Expr] = term ~ assignment ~ "0" ^^ {
    case term ~ _ ~ _ =>
      () => terms(term) = 0
  }

  def inc: Parser[Expr] = term ~ assignment ~ term ~ "+" ~ "1" ^^ {
    case t1 ~ _ ~ t2 ~ _ ~ _ =>
      () => if (t1 != t2) throw new Exception(s"Mismatched increment. Expected '$t1', got '$t2'")
      validate(t1)
      terms(t1) += 1
  }

  def loop: Parser[Expr] = "loop" ~ term ~ "{" ~ expr ~ "}" ^^ {
    case _ ~ term ~ _ ~ expr ~ _ =>
      () =>
      validate(term)
      for { _ <- 1 to terms(term) } run(expr)
  }

  def input: Parser[Any] = (inputTerm ~ ",".?).*

  def inputTerm: Parser[Unit] = term ~ assignment ~ number ^^ {
    case term ~ _ ~ number =>
      if (terms.contains(term)) println(s"Warning: reassignment to var '$term'")
      terms(term) = number
  }

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      // scalastyle:off regex
      println("Usage: scala Loop <source> <input>")
      println("  <source>  a Loop source file")
      println("  <input>   constant declarations in the format var1=n,var2=m,...")
      // scalastyle:on
    } else {
      parseAll[Any](input, args(1))

      val source = io.Source.fromFile(args(0))

      try {
        parseAll(expr, source.bufferedReader) match {
          case Success(expr, _) => run(expr)
          case failure: NoSuccess => sys.error(failure.msg)
        }
      } finally {
        source.close
      }

      terms.toSeq sortBy (_._1) foreach {
        case (k, v) => println(s"$k = $v")
      }
    }
  }
}
