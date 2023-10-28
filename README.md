# http4s-play-json
[![CI](https://github.com/http4s/http4s-play-json/actions/workflows/ci.yml/badge.svg)](https://github.com/http4s/http4s-play-json/actions/workflows/ci.yml)

Provides `EntityEncoder` and `EntityDecoder` support for [play-json](https://github.com/playframework/play-json) JSON library.

## SBT Setup
```sbt
libraryDependencies += "org.http4s" %% "http4s-play-json" % "0.23.11"
```

## Usage
```scala
import org.http4s.play._
import play.api.libs.json._

// Use implicits for any type with play-json `Reads` or `Writes`. One example:

// Assumes that https://mydomain.invalid/sample.json contains:
// { "hello": "world" }

// Play-json formatter for { "hello": "world" }
case class SampleResponse(hello: String)

object Sample {
  implicit val format: Format[SampleResponse] = Json.format
}

// Fetch with an http4s client
val client: Client[F] = ???

val target = Uri.uri("http://mydomain.invalid/sample.json")
val response: F[Sample] = httpClient.expect[Sample](target)
```

