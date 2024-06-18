/*
 * Copyright 2018 http4s.org
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

package org.http4s.play

import cats.data.NonEmptyList
import cats.syntax.all._
import play.api.libs.json.JsPath
import play.api.libs.json.JsonValidationError

final case class PlayJsonDecodingFailures(failures: NonEmptyList[PlayJsonDecodingFailure])
    extends Exception {
  override def getMessage: String = failures.iterator.map(_.show).mkString("\n")
}

object PlayJsonDecodingFailures {
  def fromJsResult(
      errors: scala.collection.Seq[(JsPath, scala.collection.Seq[JsonValidationError])]
  ): PlayJsonDecodingFailures =
    PlayJsonDecodingFailures(
      errors.toList
        .flatMap {
          case (path, Nil) =>
            List(PlayJsonDecodingFailure(path, JsonValidationError("error.path.missing")))
          case (path, errors) => errors.toList.map(PlayJsonDecodingFailure(path, _))
        }
        .toNel
        .getOrElse(
          NonEmptyList.one(
            PlayJsonDecodingFailure(JsPath, JsonValidationError("error.path.missing"))
          )
        )
    )
}
