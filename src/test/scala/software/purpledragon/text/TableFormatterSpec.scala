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

import java.io.{ByteArrayOutputStream, PrintStream}
import java.nio.charset.StandardCharsets

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TableFormatterSpec extends AnyFlatSpec with Matchers {

  "TableFormatter()" should "create formatter without header" in {
    val formatter = TableFormatter()
    formatter.headers shouldBe None
  }

  "TableFormatter(header-1, header-2)" should "create formatter with header" in {
    val formatter = TableFormatter("header-1", "header-2")
    formatter.headers shouldBe Some(Seq("header-1", "header-2"))
  }

  "addRow" should "add a row to a table" in {
    val formatter = TableFormatter()
    val result = formatter.addRow("col-1", "col-2")

    result shouldBe formatter
    result.rows shouldBe Seq(Seq("col-1", "col-2"))
  }

  "+=" should "add a row to a table" in {
    val formatter = TableFormatter()
    val result = formatter += Seq("col-1", "col-2")

    result shouldBe formatter
    result.rows shouldBe Seq(Seq("col-1", "col-2"))
  }

  "toString" should "output empty table" in {
    val formatter = TableFormatter()
    formatter.toString shouldBe "\n"
  }

  it should "output table with single row" in {
    val formatter = TableFormatter()
      .addRow("col-1", "col-2", "col-3")

    formatter.toString shouldBe
      """col-1  col-2  col-3
        |""".stripMargin
  }

  it should "output table with multiple rows" in {
    val formatter = TableFormatter()
      .addRow("col-1", "col-2", "col-3")
      .addRow("row-2-col-1", "row-2-col-2", "row-2-col-3")

    formatter.toString shouldBe
      """col-1        col-2        col-3
        |row-2-col-1  row-2-col-2  row-2-col-3
        |""".stripMargin
  }

  it should "omit trailing whitespace on rows" in {
    val formatter = TableFormatter()
      .addRow("col-1", "col-2", "")
      .addRow("row-2-col-1", "row-2-col-2", "row-2-col-3")

    formatter.toString shouldBe
      """col-1        col-2
        |row-2-col-1  row-2-col-2  row-2-col-3
        |""".stripMargin
  }

  it should "output table with header and no rows" in {
    val formatter = TableFormatter("header-1", "header-2", "header-3")

    formatter.toString shouldBe
      """|| header-1 | header-2 | header-3 |
         |----------------------------------
         |""".stripMargin
  }

  it should "output table with header and single row" in {
    val formatter = TableFormatter("header-1", "header-2", "header-3")
      .addRow("col-1", "col-2", "col-3")

    formatter.toString shouldBe
      """|| header-1 | header-2 | header-3 |
         |----------------------------------
         || col-1    | col-2    | col-3    |
         |""".stripMargin
  }

  it should "output table with header and multiple rows" in {
    val formatter = TableFormatter("header-1", "header-2", "header-3")
      .addRow("col-1", "col-2", "col-3")
      .addRow("row-2-col-1", "row-2-col-2", "row-2-col-3")

    formatter.toString shouldBe
      """|| header-1    | header-2    | header-3    |
         |-------------------------------------------
         || col-1       | col-2       | col-3       |
         || row-2-col-1 | row-2-col-2 | row-2-col-3 |
         |""".stripMargin
  }

  it should "strip trailing newline for empty table" in {
    val formatter = TableFormatter().withStripTrailingNewline
    formatter.toString shouldBe ""
  }

  it should "strip trailing newline for non-empty table" in {
    val formatter = TableFormatter("header-1", "header-2", "header-3").withStripTrailingNewline
      .addRow("col-1", "col-2", "col-3")
      .addRow("row-2-col-1", "row-2-col-2", "row-2-col-3")

    formatter.toString shouldBe
      """|| header-1    | header-2    | header-3    |
         |-------------------------------------------
         || col-1       | col-2       | col-3       |
         || row-2-col-1 | row-2-col-2 | row-2-col-3 |""".stripMargin
  }

  "print(out)" should "print a table to a stream" in {
    val boas = new ByteArrayOutputStream()
    val out = new PrintStream(boas)

    val formatter = TableFormatter("header-1", "header-2", "header-3")
      .addRow("col-1", "col-2", "col-3")
      .addRow("row-2-col-1", "row-2-col-2", "row-2-col-3")

    formatter.print(out)

    boas.toString(StandardCharsets.UTF_8) shouldBe
      """|| header-1    | header-2    | header-3    |
         |-------------------------------------------
         || col-1       | col-2       | col-3       |
         || row-2-col-1 | row-2-col-2 | row-2-col-3 |
         |""".stripMargin
  }

  "print()" should "print a table to stdout" in {
    val boas = new ByteArrayOutputStream()
    val out = new PrintStream(boas)

    val formatter = TableFormatter("header-1", "header-2", "header-3")
      .addRow("col-1", "col-2", "col-3")
      .addRow("row-2-col-1", "row-2-col-2", "row-2-col-3")

    Console.withOut(out) {
      formatter.print()
    }

    boas.toString(StandardCharsets.UTF_8) shouldBe
      """|| header-1    | header-2    | header-3    |
         |-------------------------------------------
         || col-1       | col-2       | col-3       |
         || row-2-col-1 | row-2-col-2 | row-2-col-3 |
         |""".stripMargin

  }
}
