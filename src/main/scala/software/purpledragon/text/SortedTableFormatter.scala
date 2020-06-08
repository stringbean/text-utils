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

class SortedTableFormatter(
    headers: Option[Seq[String]],
    separator: String = "  ",
    prefix: String = "",
    suffix: String = "",
    stripTrailingNewline: Boolean = false,
    sortColumnIndex: Int = 0)
    extends TableFormatter(headers, separator, prefix, suffix, stripTrailingNewline) {

  override def rows: Seq[Seq[String]] = {
    if (contents.isEmpty) {
      Nil
    } else {
      if (sortColumnIndex >= contents.head.size) {
        throw new IllegalArgumentException(
          s"Sort index ($sortColumnIndex) is greater than last column (${contents.head.size - 1}"
        )
      }

      contents.sortBy(row => row(sortColumnIndex)).toSeq
    }
  }
}

object SortedTableFormatter {
  def apply(headers: String*): SortedTableFormatter = {
    if (headers.isEmpty) {
      new SortedTableFormatter(None)
    } else {
      new SortedTableFormatter(Some(headers), " | ", "| ", " |")
    }
  }
}
