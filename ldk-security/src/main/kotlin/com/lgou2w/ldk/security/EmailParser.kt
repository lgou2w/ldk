/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lgou2w.ldk.security

import java.util.regex.Pattern

/**
 * ## EmailParser (电子邮箱解析器）
 *
 * @since LDK 0.2.1
 */
interface EmailParser {

  @Throws(IllegalArgumentException::class)
  fun parse(fullyQualifiedEmail: String): Email

  fun parseSafely(fullyQualifiedEmail: String?): Email? {
    return if (fullyQualifiedEmail.isNullOrBlank())
      null
    else try {
      parse(fullyQualifiedEmail)
    } catch (e: IllegalArgumentException) {
      null
    }
  }
}

/**
 * ## SimpleEmailParser (简单电子邮箱解析器）
 *
 * @since LDK 0.2.1
 */
object SimpleEmailParser : EmailParser {

  private val pattern = Pattern.compile("^(?<id>.+)@(?<secondaryDomain>.+)\\.(?<topDomain>.+)$")

  override fun parse(fullyQualifiedEmail: String): Email {
    val matcher = pattern.matcher(fullyQualifiedEmail)
    if (!matcher.matches())
      throw IllegalArgumentException("Illegal email format: $fullyQualifiedEmail")
    return Email(
      matcher.group("id"),
      matcher.group("secondaryDomain"),
      matcher.group("topDomain")
    )
  }
}

/**
 * ## StandardEmailParser (标准电子邮箱解析器）
 *
 * @since LDK 0.2.1
 */
object StandardEmailParser : EmailParser {

  private val pattern = Pattern.compile("^(?<id>[A-Za-z0-9_.\\-]{3,})@((?<thirdLevelDomain>[A-Za-z0-9_\\-]+)\\.(?<secondaryDomain>[A-Za-z]{2,})(\\.(?<topDomain>[A-Za-z]{2,}))?)$")

  override fun parse(fullyQualifiedEmail: String): Email {
    val matcher = pattern.matcher(fullyQualifiedEmail)
    if (!matcher.matches())
      throw IllegalArgumentException("Illegal email format: $fullyQualifiedEmail")
    val id = matcher.group("id")
    val thirdLevelDomain = matcher.group("thirdLevelDomain")
    val secondaryDomain = matcher.group("secondaryDomain")
    val topDomain: String? = matcher.group("topDomain")
    return if (topDomain != null)
      Email(id, "$thirdLevelDomain.$secondaryDomain", topDomain)
    else
      Email(id, thirdLevelDomain, secondaryDomain)
  }
}
