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

import java.util.Map;
import java.util.NoSuchElementException;

final class CatServerRemapper extends Remapper {
  final Class<?> classJarMapping;
  final Class<?> classRemapUtils;
  final Class<?> classReflectionTransformer;
  final FieldAccessor<Object, Map<String, String>> fieldJarMappingClasses;
  final FieldAccessor<Object, Object> fieldReflectionTransformerJarMapping;
  final MethodAccessor<Object, String> methodMapMethod;
  final MethodAccessor<Object, String> methodMapFieldName;

  final static String NAME_JAR_MAPPING = "net.md_5.specialsource.JarMapping";
  final static String NAME_REMAP_UTILS = "catserver.server.remapper.RemapUtils";
  final static String NAME_REFLECTION_TRANSFORMER = "catserver.server.remapper.ReflectionTransformer";
  final static String ID = "CatServer";

  CatServerRemapper(@Nullable ClassLoader loader) {
    super(loader);
    try {
      classJarMapping = Class.forName(NAME_JAR_MAPPING);
      classRemapUtils = Class.forName(NAME_REMAP_UTILS);
      classReflectionTransformer = Class.forName(NAME_REFLECTION_TRANSFORMER);

      fieldJarMappingClasses = FuzzyReflection
        .of(classJarMapping, true)
        .useFieldMatcher()
        .withType(Map.class)
        .withName("classes")
        .resultAccessorAs();
      fieldReflectionTransformerJarMapping = FuzzyReflection
        .of(classReflectionTransformer, true)
        .useFieldMatcher()
        .withType(classJarMapping)
        .withName("jarMapping")
        .resultAccessor();

      FuzzyReflection remapUtilsReflection = FuzzyReflection.of(classRemapUtils, true);
      methodMapMethod = remapUtilsReflection
        .useMethodMatcher()
        .withType(String.class)
        .withArgs(Class.class, String.class, Class[].class)
        .withName("mapMethod")
        .resultAccessorAs();
      methodMapFieldName = remapUtilsReflection
        .useMethodMatcher()
        .withType(String.class)
        .withArgs(Class.class, String.class)
        .withName("mapFieldName")
        .resultAccessorAs();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Classes does not exist, ensure that the server is CatServer?", e);
    } catch (NoSuchElementException e) {
      throw new RuntimeException("Error while find structure members: ", e);
    } catch (Throwable t) {
      throw new RuntimeException("Internal error: ", t);
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
    Object jarMapping = fieldReflectionTransformerJarMapping.get(null);
    Map<String, String> classes = fieldJarMappingClasses.get(jarMapping);
    String mappedName = classes != null ? classes.get(name) : null;
    return mappedName != null
      ? mappedName.replace('/', '.')
      : name;
  }

  @Override
  @Nullable
  public String mapMethodName(Class<?> clazz, String name, Class<?>[] parameterTypes) {
    if (clazz == null) throw new NullPointerException("clazz");
    if (parameterTypes == null) throw new NullPointerException("parameterTypes");
    if (name == null) return null;
    String methodName = methodMapMethod.invoke(null, clazz, name, parameterTypes);
    return methodName != null ? methodName : name;
  }

  @Override
  @Nullable
  public String mapFieldName(Class<?> clazz, String name) {
    if (clazz == null) throw new NullPointerException("clazz");
    if (name == null) return null;
    String fieldName = methodMapFieldName.invoke(null, clazz, name);
    return fieldName != null ? fieldName : name;
  }

  @Override
  public String toString() {
    return "CatServerRemapper";
  }
}
