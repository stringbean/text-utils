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

/**
 * Extension of [[TableFormatter]] that sorts the rows by a column before outputting.
 *
 * @param headers optional column headers.
 * @param separator separator to use between columns.
 * @param prefix prefix to use before first column.
 * @param suffix suffix to use after last column.
 * @param sortColumnIndex index of the column (zero-based) to sort by.
 */
class SortedTableFormatter(
    headers: Option[Seq[String]],
    separator: String = "  ",
    prefix: String = "",
    suffix: String = "",
    stripTrailingNewline: Boolean = false,
    val sortColumnIndex: Int = 0)
    extends TableFormatter(headers, separator, prefix, suffix, stripTrailingNewline) {

  /**
   * Creates a new `SortedTableFormatter`, copying the settings from this and with the supplied sort column index.
   *
   * The rows in this table will ''not'' be copied to the new table.
   *
   * @param newSortColumnIndex index of the column (zero-based) to sort by.
   * @return An empty table with the updated settings.
   */
  def withSortColumnIndex(newSortColumnIndex: Int): SortedTableFormatter = {
    new SortedTableFormatter(headers, separator, prefix, suffix, stripTrailingNewline, newSortColumnIndex)
  }

  @SuppressWarnings(Array("UnnecessaryConversion"))
  override def rows: Seq[Seq[String]] = {
    contents.headOption foreach { first =>
      if (sortColumnIndex >= first.size) {
        throw new IllegalArgumentException(
          s"Sort index ($sortColumnIndex) is greater than last column (${first.size - 1}")
      }
    }

    contents.sortBy(row => row.toIndexedSeq(sortColumnIndex)).toSeq
  }
}

object SortedTableFormatter {

  /**
   * Creates `SortedTableFormatter` with the specified headers.
   *
   * If headers are specified then the prefix, separator and suffix will be set to `"| "`, `" | "` and `" |"`
   * respectively. If no headers are specified then the defaults will be used.
   *
   * The table will be sorted by the first column.
   *
   * @param headers column header names.
   */
  def apply(headers: String*): SortedTableFormatter = {
    if (headers.isEmpty) {
      new SortedTableFormatter(None)
    } else {
      new SortedTableFormatter(Some(headers), " | ", "| ", " |")
    }
  }
}
