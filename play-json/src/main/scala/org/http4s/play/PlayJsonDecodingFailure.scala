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

import cats.Show
import play.api.libs.json.JsPath
import play.api.libs.json.JsonValidationError

final case class PlayJsonDecodingFailure(path: JsPath, error: JsonValidationError)
    extends Exception {
  override def fillInStackTrace(): Throwable = this

  def message: Option[String] =
    error match {
      case JsonValidationError.Detailed(msg, arg) => Some(s"$msg ($arg)")
      case JsonValidationError.Message(msg) => Some(msg)
      case _ => None
    }

  def pathToRootString: String = path.path.foldLeft("")((acc, p) => acc + p.toJsonString)

  override def toString: String =
    message.fold(s"PlayJsonDecodingFailure at $pathToRootString")(msg =>
      s"PlayJsonDecodingFailure at $pathToRootString: $msg"
    )
}

object PlayJsonDecodingFailure {

  /** Creates compact, human readable string representations for PlayJsonDecodingFailure
    * Cursor history is represented as JS style selections, i.e. ".foo.bar[3]"
    */
  implicit final val showDecodingFailure: Show[PlayJsonDecodingFailure] =
    Show.fromToString
}
