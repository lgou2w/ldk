/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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

import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.LazyFuzzyReflect
import com.lgou2w.ldk.reflect.Reflection
import com.lgou2w.ldk.reflect.StructureModifier
import com.lgou2w.ldk.reflect.Visibility
import org.junit.Test

class Test {

    class MyClass(value: Int = 0) {
        private val member : Int = value
        fun get() : Int {
            return member
        }
    }

    val myClassMember = LazyFuzzyReflect(MyClass::class.java) {
        useForceAccess()
            .useFieldMatcher()
            .withType(Int::class.java)
            .resultAccessorAs<MyClass, Int>()
    }

    @Test
    fun test_LazyField() {
        println(myClassMember.isInitialized())
        println(myClassMember.value[MyClass(100)])
        println(myClassMember.isInitialized())
    }

    @Test
    fun test_ClassField() {
        val instance = MyClass()
        FuzzyReflect.of(MyClass::class.java)
            .useForceAccess()
            .useFieldMatcher()
            .withType(Int::class.java)
            .resultAccessorAs<MyClass, Int>()
            .set(instance, 100)
        println(instance.get())
    }

    @Test
    fun test_ClassMethod() {
        val instance = MyClass(100)
        val value = FuzzyReflect.of(MyClass::class.java)
            .useForceAccess()
            .useMethodMatcher()
            .withType(Int::class.java)
            .withVisibilities(Visibility.PUBLIC)
            .resultAccessorAs<MyClass, Int>()
            .invoke(instance)
        println(value)
    }

    @Test
    fun test_CallerClass() {
        Reflection.safe()
            .getCallerClasses()
            .forEach { println(it) }
    }

    @Test
    fun test_StructureModifier() {
        class Data {
            private var name: String = "My"
            private var age : Int = 0
            override fun toString(): String {
                return "Data(name=$name, age=$age)"
            }
        }
        val instance = Data()
        println(instance)
        val sm = StructureModifier.of<Data>().withTarget(instance)
        sm.withType(String::class.java).write(0, "Data Structure")
        sm.withType(Int::class.java).write(0, 23)
        println(instance)
    }
}
