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

final class MohistRemapper extends Remapper {
  final Class<?> classMohistJarMapping;
  final Class<?> classClassMapping;
  final Class<?> classRemapUtils;
  final FieldAccessor<Object, Map<String, Object>> fieldMohistJarMappingByNMSName;
  final FieldAccessor<Object, String> fieldClassMappingMcpName;
  final FieldAccessor<Object, Object> fieldRemapUtilsJarMapping;
  final MethodAccessor<Object, String> methodMapMethod;
  final MethodAccessor<Object, String> methodMapFieldName;

  final static String NAME_MOHIST_JAR_MAPPING = "com.mohistmc.bukkit.nms.remappers.MohistJarMapping";
  final static String NAME_CLASS_MAPPING = "com.mohistmc.bukkit.nms.model.ClassMapping";
  final static String NAME_REMAP_UTILS = "com.mohistmc.bukkit.nms.utils.RemapUtils";
  final static String ID = "Mohist";

  MohistRemapper(@Nullable ClassLoader loader) {
    super(loader);
    try {
      classMohistJarMapping = Class.forName(NAME_MOHIST_JAR_MAPPING);
      classClassMapping = Class.forName(NAME_CLASS_MAPPING);
      classRemapUtils = Class.forName(NAME_REMAP_UTILS);

      fieldMohistJarMappingByNMSName = FuzzyReflection
        .of(classMohistJarMapping, true)
        .useFieldMatcher()
        .withType(Map.class)
        .withName("byNMSName")
        .resultAccessorAs("Missing match: MohistJarMapping -> Field: Map byNMSName");
      fieldClassMappingMcpName = FuzzyReflection
        .of(classClassMapping, true)
        .useFieldMatcher()
        .withType(String.class)
        .withName("mcpName")
        .resultAccessorAs("Missing match: ClassMapping -> Field: String mcpName");

      FuzzyReflection remapUtilsReflection = FuzzyReflection.of(classRemapUtils, true);
      fieldRemapUtilsJarMapping = remapUtilsReflection
        .useFieldMatcher()
        .withType(classMohistJarMapping)
        .withName("jarMapping")
        .resultAccessor("Missing match: RemapUtils -> Field: MohistJarMapping jarMapping");
      methodMapMethod = remapUtilsReflection
        .useMethodMatcher()
        .withType(String.class)
        .withArgs(Class.class, String.class, Class[].class)
        .withName("mapMethodName")
        .resultAccessorAs("Missing match: RemapUtils -> Method: String mapMethod(Class, String, Class[])");
      methodMapFieldName = remapUtilsReflection
        .useMethodMatcher()
        .withType(String.class)
        .withArgs(Class.class, String.class)
        .withName("mapFieldName")
        .resultAccessorAs("Missing match: RemapUtils -> Method: String mapFieldName(Class, String)");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Classes does not exist, ensure that the server is Mohist?", e);
    } catch (NoSuchElementException e) {
      throw new RuntimeException("Error while find structure members:", e);
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
    Object mohistJarMapping = fieldRemapUtilsJarMapping.get(null);
    Map<String, Object> byNMSName = fieldMohistJarMappingByNMSName.get(mohistJarMapping);
    Object classMapping = byNMSName != null ? byNMSName.get(name) : null;
    String mappedName = classMapping != null ? fieldClassMappingMcpName.get(classMapping) : null;
    return mappedName != null ? mappedName : name;
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
    return "MohistRemapper";
  }
}
