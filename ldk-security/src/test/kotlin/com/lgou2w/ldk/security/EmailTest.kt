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

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class EmailTest {

  companion object {
    const val ID = "lgou2w"
    const val SECONDARY_DOMAIN = "hotmail"
    const val TOP_DOMAIN = "com"
    const val AT = '@'
    const val DOT = '.'
  }

  @Test fun `Email - validation`() {
    val email = Email(ID, SECONDARY_DOMAIN, TOP_DOMAIN)
    email.id shouldBeEqualTo ID
    email.secondaryDomain shouldBeEqualTo SECONDARY_DOMAIN
    email.topDomain shouldBeEqualTo TOP_DOMAIN
    email.fullyQualifiedEmail shouldBeEqualTo (ID + AT + SECONDARY_DOMAIN + DOT + TOP_DOMAIN)
  }

  @Test fun `Email - compare`() {
    val e1 = Email("aaa", SECONDARY_DOMAIN, TOP_DOMAIN)
    val e2 = Email("bbb", SECONDARY_DOMAIN, TOP_DOMAIN)
    (e1 < e2) shouldBeEqualTo true
    (e2 > e1) shouldBeEqualTo true
  }
}
