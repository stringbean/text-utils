# Contributing

Welcome and thanks for taking the time to contribute to this project!

## Building & Preparing PRs

Before submitting a PR please ensure that you have run the following sbt tasks:

* `test`
* `scapegoat`
* `scalafmt`
* `headerCheck`
* `mimaFindBinaryIssues`

This should ensure that everything works and meets the styleguide.

## Documentation

The documentation site is built using [Paradox](https://github.com/lightbend/paradox) and lives in the `src/main/paradox` directory. You can preview the site by running:

```sh
sbt previewSite
```