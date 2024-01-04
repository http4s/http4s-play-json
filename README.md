# http4s-play-json [![CI](https://github.com/http4s/http4s-play-json/actions/workflows/ci.yml/badge.svg)](https://github.com/http4s/http4s-play-json/actions/workflows/ci.yml) [![Maven Central](https://img.shields.io/maven-central/v/org.http4s/http4s-play-json_2.13?versionPrefix=0.23)](https://img.shields.io/maven-central/v/org.http4s/http4s-play-json_2.13?versionPrefix=0.23) <a href="https://typelevel.org/cats/"><img src="https://typelevel.org/cats/img/cats-badge.svg" height="40px" align="right" alt="Cats friendly" /></a>

Provides `EntityEncoder` and `EntityDecoder` support for [play-json](https://github.com/playframework/play-json) JSON library.

## SBT Setup
```sbt
libraryDependencies += "org.http4s" %% "http4s-play-json" % "0.23.13"
```

## Usage
```scala
import org.http4s.play._
import play.api.libs.json._

// Lets fetch some JSON from a website and parse it 
// into a case class that has a play-json `Format` defined (Reads/Writes)

// Assumes that https://mydomain.invalid/sample.json responds:
// { "hello": "world" }

case class SampleResponse(hello: String)

object SampleResponse {
  // Play-json formatter for { "hello": "world" }
  implicit val format: Format[SampleResponse] = Json.format
}

// Fetch with an http4s client
val client: Client[F] = ???

val target = Uri.uri("http://mydomain.invalid/sample.json")

// The response will be deserialised to a `SampleResponse` 
// using the implicit play-json `Format`
val response: F[Sample] = httpClient.expect[SampleResponse](target)
```

## Community

The [Typelevel Discord](https://discord.gg/XF3CXcMzqD) has an #http4s channel. Please join us!

## See also

Other JSON compatibility modules for http4s include: http4s-circe (in the core repo) and [http4s-fabric](https://github.com/http4s/http4s-fabric).