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
class LoopSpec extends Spec {
  "Loop should:" - {
    "Multiply" in {
      val file = getClass.getResource("/mult.loop").getPath
      val result = new java.io.ByteArrayOutputStream

      Console.withOut(result) {
        Loop.main(Array(file, "a=3,b=2"))
      }
      assert(result.toString contains "r = 6")
    }

    "Decrement" in {
      val file = getClass.getResource("/dec.loop").getPath
      val result = new java.io.ByteArrayOutputStream

      Console.withOut(result) {
        Loop.main(Array(file, "a=5"))
      }
      assert(result.toString contains "r = 4")
    }
  }
}
