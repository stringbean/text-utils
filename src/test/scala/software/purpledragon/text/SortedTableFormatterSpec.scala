/*
 * Copyright 2020 Michael Stringer
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

package software.purpledragon.text

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SortedTableFormatterSpec extends AnyFlatSpec with Matchers {

  "SortedTableFormatter()" should "create sorted formatter without header" in {
    val formatter = SortedTableFormatter()
    formatter.headers shouldBe None
  }

  "SortedTableFormatter(header-1, header-2)" should "create sorted formatter with header" in {
    val formatter = SortedTableFormatter("header-1", "header-2")
    formatter.headers shouldBe Some(Seq("header-1", "header-2"))
  }

  "toString" should "output empty table" in {
    val formatter = SortedTableFormatter()
    formatter.toString shouldBe "\n"
  }

  it should "output table with single row" in {
    val formatter = SortedTableFormatter()
      .addRow("col-1", "col-2", "col-3")

    formatter.toString shouldBe
      """col-1  col-2  col-3
        |""".stripMargin
  }

  it should "output table with multiple rows" in {
    val formatter = SortedTableFormatter()
      .addRow("col-1", "col-2", "col-3")
      .addRow("row-2-col-1", "row-2-col-2", "row-2-col-3")

    formatter.toString shouldBe
      """col-1        col-2        col-3
        |row-2-col-1  row-2-col-2  row-2-col-3
        |""".stripMargin
  }

  it should "sort rows before outputting" in {
    val formatter = SortedTableFormatter()
      .addRow("bravo", "row 2")
      .addRow("alpha", "row 1")
      .addRow("charlie", "row 3")
      .addRow("delta", "row 4")

    formatter.toString shouldBe
      """alpha    row 1
        |bravo    row 2
        |charlie  row 3
        |delta    row 4
        |""".stripMargin
  }

  it should "sort rows by column" in {
    val formatter = new SortedTableFormatter(None, sortColumnIndex = 1)
      .addRow("banana", "row 1")
      .addRow("pear", "row 3")
      .addRow("apple", "row 5")
      .addRow("peach", "row 4")
      .addRow("zebra", "row 2")

    formatter.toString shouldBe
      """banana  row 1
        |zebra   row 2
        |pear    row 3
        |peach   row 4
        |apple   row 5
        |""".stripMargin
  }

  it should "throw an IllegalArgumentException if sort column is invalid" in {
    val formatter = new SortedTableFormatter(None, sortColumnIndex = 2)
      .addRow("banana", "row 1")
      .addRow("pear", "row 3")

    an[IllegalArgumentException] shouldBe thrownBy {
      formatter.toString
    }
  }
}
