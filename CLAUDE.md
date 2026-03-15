# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

`http4s-play-json` provides `EntityEncoder` and `EntityDecoder` instances for Play JSON in http4s applications. It bridges http4s and Play JSON so types with Play `Reads`/`Writes` instances can be automatically serialized/deserialized in http4s routes and clients.

## Setup

This project uses [mise](https://mise.jdx.dev) for tool management. Run `mise install` to get the correct Java (Temurin 21), SBT, and Scala versions.

## Commands

### Build and Test
- `sbt test` - Run all tests
- `sbt +test` - Run tests across all Scala versions (2.13, 3)
- `sbt "++ 2.13" test` - Run tests for a specific Scala version
- `sbt "play-json/testOnly org.http4s.play.test.PlaySuite"` - Run a specific test class
- `sbt "play-json/testOnly org.http4s.play.test.PlaySuite -- --tests=*decodeJson*"` - Run a single test by name pattern

### Code Quality
- `sbt scalafmtAll` - Format all code
- `sbt scalafmtCheckAll` - Check formatting
- `sbt headerCheckAll` - Check license headers
- `sbt scalafixAll` - Apply scalafix rules (Scala 2.x only)
- `sbt 'scalafixAll --check'` - Check scalafix lints (Scala 2.x only)
- `sbt mimaReportBinaryIssues` - Check binary compatibility

### CI Checks
- `sbt githubWorkflowCheck` - Verify GitHub workflows are up to date
- `sbt githubWorkflowGenerate` - Regenerate GitHub workflows
- `sbt coverage test coverageReport` - Generate test coverage report

## Architecture

All source lives under `play-json/src/main/scala/org/http4s/play/`.

### Implicit Instance Layering

The library has a deliberate layered design for how implicit instances are provided:

1. **`PlayInstances`** (trait) - Core trait providing `jsonOf`, `jsonEncoderOf`, `jsonDecoder`, `jsonEncoder`, plus `Reads[Uri]`/`Writes[Uri]` and `Message[F].decodeJson[A]` syntax. These require explicit calls (e.g. `jsonOf[F, Foo]`).

2. **`package object play`** extends `PlayInstances` - importing `org.http4s.play._` gives you the core instances.

3. **`PlayEntityDecoder`** / **`PlayEntityEncoder`** - Provide fully implicit derivation: if a `Reads[A]` or `Writes[A]` is in scope, an `EntityDecoder`/`EntityEncoder` is derived automatically without calling `jsonOf`/`jsonEncoderOf`. These are opt-in imports (`import org.http4s.play.PlayEntityDecoder._`).

4. **`PlayEntityCodec`** - Combines both `PlayEntityDecoder` and `PlayEntityEncoder` for convenience.

5. **`PlayInstancesBuilder`** - Builder pattern (via `PlayInstances.builder`) to customize error handling, producing a custom `PlayInstances` with a different `jsonDecodeError` handler.

### JSON Parsing

**`Parser`** - Private jawn `SupportParser[JsValue]` providing a `Facade[JsValue]` that bridges jawn's streaming parser to Play JSON AST types. This is what powers `jsonDecoder` via `jawn.jawnDecoder`.

### Error Types

- **`PlayJsonDecodingFailure`** - Single path+error pair from a failed `Reads` validation
- **`PlayJsonDecodingFailures`** - `NonEmptyList[PlayJsonDecodingFailure]`, constructed from Play's `JsResult` errors

### Test Structure

The test suite (`PlaySuite`) lives in package `org.http4s.play.test` (not `org.http4s.play`) so it can import from the play package object without conflicts.

## Cross-Compilation

- Scala 2.13 and 3 (see `build.sbt` for exact versions)
- Java 11, 17, and 21 in CI (`tlJdkRelease := Some(11)` sets minimum bytecode target)
- JVM-only (play-json uses Jackson, no ScalaJS/Native)
- Scalafix rules only apply to Scala 2.x

## Build Configuration

Uses `sbt-http4s-org` plugin which bundles scalafmt, scalafix, MiMa, license headers, and GitHub Actions workflow generation. The `.scalafmt.conf` enforces 100-char line limit, no vertical alignment, and trailing commas.
