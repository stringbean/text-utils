# Tables

The @scaladoc[TableFormatter](software.purpledragon.text.TableFormatter) and
@scaladoc[SortedTableFormatter](software.purpledragon.text.SortedTableFormatter) classes provide mechanisms for building
and printing tabular data.

@@@ note
The outputted tables rely on monospaced fonts to render correctly. YMMV if you are not outputting to a terminal or plain
text file.
@@@

## Basic Tables

### Examples

#### Without Headers

```scala
TableFormatter()
  .addRow("Apples", "25")
  .addRow("Pears", "10")
  .addRow("Bananas", "4")
  .print()
```

Will output:

```text
Apples   25
Pears    10
Bananas  4
```

#### With Headers

```scala
TableFormatter("Produce", "Remaining")
  .addRow("Apples", "25")
  .addRow("Pears", "10")
  .addRow("Bananas", "4")
  .print()
```

Will output:

```text
| Produce | Remaining |
-----------------------
| Apples  | 25        |
| Pears   | 10        |
| Bananas | 4         |
```

## Sorting Table Contents

@scaladoc[SortedTableFormatter](software.purpledragon.text.SortedTableFormatter) extends the basic `TableFormatter` to
sort the contents by a column. The default sort ordering is by the first column.

@@@ note
Currently the only sorting available is ascending alpha sort.
@@@

@@@ warning
The sorted column must be present in all rows otherwise an error will occur.
@@@

### Examples

```scala
SortedTableFormatter()
  .addRow("Apples", "25")
  .addRow("Pears", "10")
  .addRow("Bananas", "4")
  .print()
```

Will output:

```text
Apples   25
Bananas  4
Pears    10
```

Or with a different sort column:

```scala
SortedTableFormatter()
  .withSortColumnIndex(1)
  .addRow("Apples", "25")
  .addRow("Pears", "10")
  .addRow("Bananas", "4")
  .print()
```

Will output:

```text
Pears    10
Apples   25
Bananas  4
```

## Configuration

### Prefix, Separator & Suffix

The prefix before the first column, separator between columns and the suffix after the last column can be configured:

| Setting   | Heading Default | No-heading Default |
|-----------|-----------------|--------------------|
| Prefix    | `"| "`          | `""`               |
| Seperator | `" | "`         | `"  "`             |
| Suffix    | `" |"`          | `""`               |

### Stripping Trailing Newlines

In some scenarios the newline at the end of the final row may interfere with other formatting. This can be fixed by
setting `stripTrailingNewline`:

```scala
TableFormatter()
  .withStripTrailingNewline
  .addRow("Foo", "Bar")
  .print()
```