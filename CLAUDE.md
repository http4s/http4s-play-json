# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is `http4s-play-json`, a library that provides `EntityEncoder` and `EntityDecoder` support for Play JSON in http4s applications. The library enables seamless JSON serialization/deserialization using Play Framework's JSON library within http4s servers and clients.

## Commands

### Build and Test
- `sbt test` - Run all tests
- `sbt "++ 2.13" test` - Run tests for specific Scala version (2.12, 2.13, 3)
- `sbt +test` - Run tests across all Scala versions

### Code Quality
- `sbt scalafmtAll` - Format all code
- `sbt scalafmtCheckAll` - Check code formatting
- `sbt headerCheckAll` - Check license headers
- `sbt 'scalafixAll --check'` - Check scalafix lints (Scala 2.x only)
- `sbt scalafixAll` - Apply scalafix rules
- `sbt mimaReportBinaryIssues` - Check binary compatibility

### Documentation
- `sbt doc` - Generate API documentation

### Dependencies
- `sbt unusedCompileDependenciesTest` - Check for unused dependencies
- `sbt +update` - Update dependencies across all Scala versions

### Workflow
- `sbt githubWorkflowCheck` - Verify GitHub workflows are up to date
- `sbt githubWorkflowGenerate` - Regenerate GitHub workflows

## Architecture

### Core Components

The library provides a bridge between http4s and Play JSON through several key components:

1. **PlayInstances** (`play-json/src/main/scala/org/http4s/play/PlayInstances.scala`) - The main trait providing implicit instances for JSON encoding/decoding
2. **Package Object** (`play-json/src/main/scala/org/http4s/play/package.scala`) - Extends PlayInstances to provide default instances
3. **Error Handling** - Custom failure types for JSON decoding errors:
   - `PlayJsonDecodingFailure.scala`
   - `PlayJsonDecodingFailures.scala`

### Key Features

- **EntityDecoder[F, A]** - Decode JSON to case classes with Play JSON `Reads[A]`
- **EntityEncoder[F, A]** - Encode case classes to JSON with Play JSON `Writes[A]`
- **Custom Error Handling** - Configurable JSON decode error handling via `PlayInstancesBuilder`
- **URI Support** - Built-in `Reads[Uri]` and `Writes[Uri]` instances
- **Message Syntax** - Extension methods for `Message[F]` to decode JSON directly

### Usage Pattern

```scala
import org.http4s.play._
import play.api.libs.json._

// Case class with Play JSON Format
case class User(name: String, age: Int)
object User {
  implicit val format: Format[User] = Json.format[User]
}

// Automatic JSON encoding/decoding in http4s routes
val service = HttpRoutes.of[F] {
  case req @ POST -> Root / "user" =>
    req.decodeJson[User].flatMap { user =>
      Ok(Json.toJson(user))
    }
}
```

## Development Guidelines

### Code Style
- Uses Scalafmt 3.9.7 with 100 character line limit
- No vertical alignment to minimize diffs
- Trailing commas in multiple parameter lists
- Specific dialects for different Scala versions

### Scalafix Rules
- Http4s-specific linters (Http4sFs2Linters, Http4sGeneralLinters, Http4sUseLiteralsSyntax)
- LeakingImplicitClassVal detection
- ExplicitResultTypes enforcement
- OrganizeImports for consistent import ordering

### Cross-Compilation
- Supports Scala 2.12.20, 2.13.16, and 3.3.6
- Scala 3 support introduced in version 0.23.12
- Java 11, 17, and 21 compatibility (with some version restrictions)

### Dependencies
- http4s 0.23.32
- Play JSON 3.0.5
- MUnit for testing with Cats Effect support

The project uses sbt-http4s-org plugin for standardized build configuration and follows http4s ecosystem conventions.