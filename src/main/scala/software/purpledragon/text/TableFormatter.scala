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

class TableFormatter(
    val headers: Option[Seq[String]],
    val separator: String = "  ",
    val prefix: String = "",
    val suffix: String = "",
    val stripTrailingNewline: Boolean = false) {

  protected val contents: mutable.Buffer[Seq[String]] = mutable.Buffer()

  // TODO handle contents
  def withSeparator(separator: String): TableFormatter = {
    new TableFormatter(headers, separator, prefix, suffix, stripTrailingNewline)
  }

  def withPrefix(prefix: String): TableFormatter = {
    new TableFormatter(headers, separator, prefix, suffix, stripTrailingNewline)
  }

  def withSuffix(suffix: String): TableFormatter = {
    new TableFormatter(headers, separator, prefix, suffix, stripTrailingNewline)
  }

  def withStripTrailingNewline: TableFormatter = {
    new TableFormatter(headers, separator, prefix, suffix, true)
  }

  def rows: Seq[Seq[String]] = this.contents.toSeq

  def addRow(columns: String*): TableFormatter = +=(columns)

  def +=(columns: Seq[String]): TableFormatter = {
    // TODO validate column count?
    contents += columns
    this
  }

  def print(): Unit = print(Console.out)
  def print(out: PrintStream): Unit = {
    out.print(toString)
  }

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
  def apply(headers: String*): TableFormatter = {
    if (headers.isEmpty) {
      new TableFormatter(None)
    } else {
      new TableFormatter(Some(headers), " | ", "| ", " |")
    }
  }
}
