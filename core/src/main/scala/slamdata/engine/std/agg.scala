/*
 * Copyright 2014 - 2015 SlamData Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package slamdata.engine.std

import scalaz._

import slamdata.engine._
import slamdata.engine.fp._

import Validation.{success, failure}
import NonEmptyList.nel

trait AggLib extends Library {
  private def reflexiveUnary(exp: Type): Func.Untyper = x => Type.typecheck(exp, x) map κ(x :: Nil)
  private val NumericUnary: Func.Untyper = reflexiveUnary(Type.Numeric)

  val Count = Reduction("COUNT", "Counts the values in a set", Type.Top :: Nil, constTyper(Type.Int), κ(success(Type.Top :: Nil)))

  val Sum = Reduction("SUM", "Sums the values in a set", Type.Numeric :: Nil, reflexiveTyper, NumericUnary)

  val Min = Reduction("MIN", "Finds the minimum in a set of values", Type.Comparable :: Nil,
    reflexiveTyper,
    reflexiveUnary(Type.Comparable))

  val Max = Reduction("MAX", "Finds the maximum in a set of values", Type.Comparable :: Nil,
    reflexiveTyper,
    reflexiveUnary(Type.Comparable))

  val Avg = Reduction("AVG", "Finds the average in a set of numeric values", Type.Numeric :: Nil, constTyper(Type.Dec), NumericUnary)

  def functions = Count :: Sum :: Min :: Max :: Avg :: Nil
}
object AggLib extends AggLib
