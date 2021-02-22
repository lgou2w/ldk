/*
 * Copyright (C) 2021 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.reflect;

import com.lgou2w.ldk.reflect.FieldAccessor;
import com.lgou2w.ldk.reflect.FuzzyReflection;
import com.lgou2w.ldk.reflect.MethodAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.NoSuchElementException;

final class ArclightRemapper extends Remapper {
  final Object remapper;
  final Class<?> classLoaderRemapper;
  final FieldAccessor<ClassLoader, Object> fieldRemapper;
  final MethodAccessor<Object, String> methodMap;
  final MethodAccessor<Object, Method> methodTryMapMethodToSrg;
  final MethodAccessor<Object, String> methodTryMapFieldToSrg;

  final static String NAME = "io.izzel.arclight.common.mod.util.remapper.ClassLoaderRemapper";
  final static String ID = "Arclight";

  ArclightRemapper(@Nullable ClassLoader loader) {
    super(loader);
    try {
      classLoaderRemapper = Class.forName(NAME);
      fieldRemapper = FuzzyReflection.of(classLoader, true)
        .useFieldMatcher()
        .withType(classLoaderRemapper)
        .withName("remapper")
        .resultAccessorAs("Missing match: " + classLoader.getClass().getSimpleName() + " -> Field: ClassLoaderRemapper remapper");
      remapper = fieldRemapper.get(classLoader);

      FuzzyReflection remapperReflection = FuzzyReflection.of(remapper, true);
      methodMap = remapperReflection
        .useMethodMatcher()
        .withName("map")
        .withType(String.class)
        .withArgs(String.class)
        .resultAccessorAs("Missing match: ClassLoaderRemapper -> Method: String map(String)");
      methodTryMapMethodToSrg = remapperReflection
        .useMethodMatcher()
        .withName("tryMapMethodToSrg")
        .withType(Method.class)
        .withArgs(Class.class, String.class, Class[].class)
        .resultAccessorAs("Missing match: ClassLoaderRemapper -> Method: String tryMapMethodToSrg(Class, String, Class[])");
      methodTryMapFieldToSrg = remapperReflection
        .useMethodMatcher()
        .withName("tryMapFieldToSrg")
        .withType(String.class)
        .withArgs(Class.class, String.class)
        .resultAccessorAs("Missing match: ClassLoaderRemapper -> Method: String tryMapFieldToSrg(Class, String)");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("ClassLoaderRemapper class does not exist, ensure that the server is Arclight?", e);
    } catch (NoSuchElementException e) {
      throw new RuntimeException("Error while find structure members of ClassLoaderRemapper class:", e);
    } catch (Throwable t) {
      throw new RuntimeException("Internal error:", t);
    }
  }

  @Override
  @NotNull
  public String getServerName() {
    return ID;
  }

  @Override
  @Nullable
  public String mapClassName(String name) {
    if (name == null) return null;
    String internalName = name.replace('.', '/');
    String mappedName = methodMap.invoke(remapper, internalName);
    return mappedName != null && !mappedName.equals(internalName)
      ? mappedName.replace('/', '.')
      : mappedName != null
      ? mappedName
      : name;
  }

  @Override
  @Nullable
  public String mapMethodName(Class<?> clazz, String name, Class<?>[] parameterTypes) {
    if (clazz == null) throw new NullPointerException("clazz");
    if (parameterTypes == null) throw new NullPointerException("parameterTypes");
    if (name == null) return null;
    Method method = methodTryMapMethodToSrg.invoke(remapper, clazz, name, parameterTypes);
    return method != null ? method.getName() : name;
  }

  @Override
  @Nullable
  public String mapFieldName(Class<?> clazz, String name) {
    if (clazz == null) throw new NullPointerException("clazz");
    String fieldName = methodTryMapFieldToSrg.invoke(remapper, clazz, name);
    return fieldName != null ? fieldName : name;
  }

  @Override
  public String toString() {
    return "ArclightRemapper";
  }
}
