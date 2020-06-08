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

import java.io.PrintStream

import scala.collection.mutable

/**
  * Simple textual formatter for tabular data. This is intended to be used in text based user interfaces such as CLIs.
  *
 * == Example without headers ==
  * {{{
  * TableFormatter()
  *   .addRow("Apples", "25")
  *   .addRow("Pears", "10")
  *   .addRow("Bananas", "4")
  *   .print()
  * }}}
  *
 * Would output:
  *
 * {{{
  * Apples   25
  * Pears    10
  * Bananas  4
  * }}}
  *
 * == Example with headers ==
  * {{{
  * TableFormatter("Produce", "Remaining")
  *   .addRow("Apples", "25")
  *   .addRow("Pears", "10")
  *   .addRow("Bananas", "4")
  *   .print()
  * }}}
  *
 * Would output:
  * {{{
  * | Produce | Remaining |
  * -----------------------
  * | Apples  | 25        |
  * | Pears   | 10        |
  * | Bananas | 4         |
  * }}}
  *
 *
  * @param headers optional column headers.
  * @param separator separator to use between columns.
  * @param prefix prefix to use before first column.
  * @param suffix suffix to use after last column.
  * @param stripTrailingNewline if `true` then no newline will be output after the last row.
  */
class TableFormatter(
    val headers: Option[Seq[String]],
    val separator: String = "  ",
    val prefix: String = "",
    val suffix: String = "",
    val stripTrailingNewline: Boolean = false) {

  protected val contents: mutable.Buffer[Seq[String]] = mutable.Buffer()

  /**
    * Creates a new `TableFormatter`, copying the settings from this and with the supplied separator.
    *
   * The rows in this table will ''not'' be copied to the new table.
    *
   * @param separator separator to use between columns.
    * @return An empty table with the updated settings.
    */
  def withSeparator(separator: String): TableFormatter = {
    new TableFormatter(headers, separator, prefix, suffix, stripTrailingNewline)
  }

  /**
    * Creates a new `TableFormatter`, copying the settings from this and with the supplied prefix.
    *
   * The rows in this table will ''not'' be copied to the new table.
    *
   * @param prefix prefix to use before first column.
    * @return An empty table with the updated settings.
    */
  def withPrefix(prefix: String): TableFormatter = {
    new TableFormatter(headers, separator, prefix, suffix, stripTrailingNewline)
  }

  /**
    * Creates a new `TableFormatter`, copying the settings from this and with the supplied suffix.
    *
   * The rows in this table will ''not'' be copied to the new table.
    *
   * @param suffix suffix to use after last column.
    * @return An empty table with the updated settings.
    */
  def withSuffix(suffix: String): TableFormatter = {
    new TableFormatter(headers, separator, prefix, suffix, stripTrailingNewline)
  }

  /**
    * Creates a new `TableFormatter`, copying the settings from this and with `stripTrailingNewline` enabled.
    *
   * The rows in this table will ''not'' be copied to the new table.
    *
   * @return An empty table with the updated settings.
    */
  def withStripTrailingNewline: TableFormatter = {
    new TableFormatter(headers, separator, prefix, suffix, true)
  }

  /**
    * Current contents of this table.
    */
  def rows: Seq[Seq[String]] = this.contents.toSeq

  /** Add a row to this table. */
  def addRow(columns: String*): TableFormatter = +=(columns)

  /** Add a row to this table. */
  def +=(columns: Seq[String]): TableFormatter = {
    // TODO validate column count?
    contents += columns
    this
  }

  /**
    * Prints this table to stdout.
    */
  def print(): Unit = print(Console.out)

  /**
    * Prints this table to the specified stream.
    * @param out stream to print to.
    */
  def print(out: PrintStream): Unit = {
    out.print(toString)
  }

  /**
    * Formats the contents of this table and returns them as a string.
    */
  override def toString: String = {
    val res = if (headers.isEmpty && rows.isEmpty) {
      "\n"
    } else {
      val sb = new mutable.StringBuilder()

      val widths = columnWidths

      def appendRow(row: Seq[String]): Unit = {
        row.zipWithIndex foreach {
          case (text, i) =>
            if (i == 0) {
              sb ++= prefix
            } else {
              sb ++= separator
            }

            if (suffix.isEmpty && i >= widths.length - 1) {
              sb.append(text)
            } else {
              val colWidth = widths(i)
              sb.append(text.padTo(colWidth, ' '))
            }

            if (i >= widths.length - 1) {
              sb ++= suffix
            }

        }
        sb += '\n'
      }

      headers foreach { header =>
        appendRow(header)

        // add separator
        val separatorLength = columnWidths
          .map(w => w + separator.length)
          .sum + prefix.length + suffix.length - separator.length
        (0 until separatorLength).foreach(_ => sb += '-')
        sb += '\n'
      }

      rows.foreach(appendRow)

      sb.toString()
    }

    if (stripTrailingNewline) {
      res
        .replaceAll("\n$", "")
        .replaceAll("(?m) *$", "")
    } else {
      res.replaceAll("(?m) *$", "")
    }
  }

  private def columnWidths: Seq[Int] = {
    val everything: Seq[Seq[String]] = headers.getOrElse(Nil) +: rows
    everything.foldLeft(Seq.empty[Int]) { (acc, row) =>
      row.zipWithIndex.foldLeft(acc) {
        case (rowAcc, (col, colIndex)) =>
          if (rowAcc.size < colIndex + 1) {
            rowAcc :+ col.length
          } else {
            rowAcc.updated(colIndex, Math.max(rowAcc(colIndex), col.length))
          }
      }
    }
  }
}

object TableFormatter {

  /**
    * Creates `TableFormatter` with the specified headers.
    *
   * If headers are specified then the prefix, separator and suffix will be set to `"| "`, `" | "` and `" |"`
    * respectively. If no headers are specified then the defaults will be used.
    *
   * @param headers column header names.
    */
  def apply(headers: String*): TableFormatter = {
    if (headers.isEmpty) {
      new TableFormatter(None)
    } else {
      new TableFormatter(Some(headers), " | ", "| ", " |")
    }
  }
}
