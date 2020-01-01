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

package com.lgou2w.ldk.nbt

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NBTStreamsTest {

  companion object {

    private lateinit var testResourceFolder : File
    private lateinit var writeFile : File
    private lateinit var writeFileNoCompound : File

    @JvmStatic
    @BeforeClass
    fun init() {
      testResourceFolder = File(System.getProperty("user.dir"), "/target/test-classes/")
      writeFile = File(testResourceFolder, "NBTStreams - writeFile.dat")
      writeFileNoCompound = File(testResourceFolder, "NBTStreams - writeFileNoCompound.dat")
    }

    @JvmStatic
    @AfterClass
    fun clean() {
      if (writeFile.exists())
        writeFile.delete()
      if (writeFileNoCompound.exists())
        writeFileNoCompound.delete()
    }
  }

  @Test fun `NBTStreams - 00 - writeBase64`() {
    val nbt = ofCompound { putString("id", "diamond") }
    val base64 = NBTStreams.writeBase64(nbt)
    base64 shouldNotEqual null
    base64 shouldNotEqual ""
    base64.length shouldBeGreaterThan 1 // length > 1
  }

  @Test fun `NBTStreams - 01 - readBase64`() {
    val base64 = "CgAACAACaWQAB2RpYW1vbmQA" // From above nbt
    val nbt = NBTStreams.readBase64(base64)
    val compound = NBTStreams.readBase64Compound(base64)
    compound shouldEqual nbt
    compound.getString("id") shouldEqual "diamond"
    compound.size shouldEqual 1
  }

  @Test fun `NBTStreams - 02 - readBase64 - Illegal base64 value should throw exception`() {
    val base64 = "CgAACAACaWQAB2RpYW1vbmQA"
    val illegalBase64 = "illegal base64 value"
    NBTStreams.readBase64(base64) shouldNotEqual null
    invoking { NBTStreams.readBase64(illegalBase64) } shouldThrow IllegalArgumentException::class
  }

  @Test fun `NBTStreams - 03 - write - TAG_END should only have one byte zero`() {
    val baos = ByteArrayOutputStream()
    NBTStreams.write(baos, NBTTagEnd.INSTANCE)
    val bytes = baos.toByteArray()
    bytes.size shouldEqual 1
    bytes[0] shouldEqual 0
  }

  @Test fun `NBTStreams - 04 - readBase64 - Read compound then base64 must be of type TAG_COMPOUND`() {
    val base64 = "CAAGYmFzZTY0AAV2YWx1ZQ==" // NBTTagString
    val inst = NBTStreams.readBase64(base64)
    inst shouldBeInstanceOf NBTTagString::class
    (inst as NBTTagString).name shouldEqual "base64"
    invoking { NBTStreams.readBase64Compound(base64) } shouldThrow IllegalArgumentException::class
  }

  @Test fun `NBTStreams - 05 - writeFile`() {
    if (!testResourceFolder.exists()) return
    val compound = ofCompound { putInt("id", 1); putByte("Count", 10) }
    NBTStreams.writeFile(compound, writeFile) // compress
    writeFile.exists() && writeFile.isFile shouldEqual true
    writeFile.length() shouldBeGreaterThan 1L
    val nbt = NBTTagString("name", "str")
    NBTStreams.writeFile(nbt, writeFileNoCompound, false) // no compress
  }

  @Test fun `NBTStreams - 06 - readFile`() {
    if (!writeFile.exists()) return
    val compound = NBTStreams.readFileCompound(writeFile) // decompress
    compound.getInt("id") shouldEqual 1
    compound.getByte("Count") shouldEqual 10
    compound.size shouldEqual 2
  }

  @Test fun `NBTStreams - 07 - readFile - If do not decompress should throw exception`() {
    if (!writeFile.exists()) return  // not decompress
    invoking { NBTStreams.readFileCompound(writeFile, false) } shouldThrow IllegalArgumentException::class
    NBTStreams.readFileInfer(writeFile) shouldNotEqual null
    NBTStreams.readFileInferCompound(writeFile).size shouldEqual 2
    NBTStreams.readFileInfer(writeFileNoCompound) shouldNotEqual null
    invoking { NBTStreams.readFileInferCompound(writeFileNoCompound) } shouldThrow IllegalArgumentException::class
  }

  @Test fun `NBTStreams - 08 - readFile - If the file does not exist or is directory, should throw exception`() {
    val notFoundFile = File(testResourceFolder, "404 Not Found.dat")
    invoking { NBTStreams.readFile(notFoundFile) } shouldThrow FileNotFoundException::class
    invoking { NBTStreams.readFile(notFoundFile, true) } shouldThrow FileNotFoundException::class
    invoking { NBTStreams.readFile(testResourceFolder, false) } shouldThrow FileNotFoundException::class
  }

  @Test fun `NBTStreams - 09 - writeBase - all tag type`() {
    val compound = ofCompound {
      putByte("byte", 1)
      putShort("short", 1)
      putInt("int", 1)
      putLong("long", 1L)
      putFloat("float", 1f)
      putDouble("double", 1.0)
      putByteArray("byte[]", byteArrayOf(1))
      putString("string", "1")
      put(ofList("list") { addList(ofList { addByte(1) }) })
      put(ofCompound("compound") { putByte("byte", 1) })
      putIntArray("int[]", intArrayOf(1))
      putLongArray("long[]", longArrayOf(1L))
    }
    val base64 = NBTStreams.writeBase64(compound)
    base64 shouldNotEqual null
    base64 shouldNotEqual ""
    base64.length shouldBeGreaterThan 1
    val compoundRead = NBTStreams.readBase64Compound(base64)
    compound shouldEqual compoundRead
    compound.size shouldEqual compoundRead.size
  }
}
