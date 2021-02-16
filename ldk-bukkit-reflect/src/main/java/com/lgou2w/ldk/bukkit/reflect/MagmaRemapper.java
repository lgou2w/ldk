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

final class MagmaRemapper extends Remapper {
  final Class<?> classMagmaJarMapping;
  final Class<?> classClassMappings;
  final Class<?> classRemappingUtils;
  final FieldAccessor<Object, Map<String, Object>> fieldMagmaJarMappingByNMSName;
  final FieldAccessor<Object, String> fieldClassMappingsMcpName;
  final FieldAccessor<Object, Object> fieldRemappingUtilsJarMapping;
  final MethodAccessor<Object, String> methodMapMethod;
  final MethodAccessor<Object, String> methodMapFieldName;

  final static String NAME_MAGMA_JAR_MAPPING = "org.magmafoundation.magma.remapper.remappers.MagmaJarMapping";
  final static String NAME_CLASS_MAPPINGS = "org.magmafoundation.magma.remapper.mappingsModel.ClassMappings";
  final static String NAME_REMAPPING_UTILS = "org.magmafoundation.magma.remapper.utils.RemappingUtils";
  final static String ID = "Magma";

  MagmaRemapper(@Nullable ClassLoader loader) {
    super(loader);
    try {
      classMagmaJarMapping = Class.forName(NAME_MAGMA_JAR_MAPPING);
      classClassMappings = Class.forName(NAME_CLASS_MAPPINGS);
      classRemappingUtils = Class.forName(NAME_REMAPPING_UTILS);

      fieldMagmaJarMappingByNMSName = FuzzyReflection
        .of(classMagmaJarMapping, true)
        .useFieldMatcher()
        .withType(Map.class)
        .withName("byNMSName")
        .resultAccessorAs();
      fieldClassMappingsMcpName = FuzzyReflection
        .of(classClassMappings, true)
        .useFieldMatcher()
        .withType(String.class)
        .withName("mcpName")
        .resultAccessorAs();

      FuzzyReflection remapUtilsReflection = FuzzyReflection.of(classRemappingUtils, true);
      fieldRemappingUtilsJarMapping = remapUtilsReflection
        .useFieldMatcher()
        .withType(classMagmaJarMapping)
        .withName("jarMapping")
        .resultAccessor();
      methodMapMethod = remapUtilsReflection
        .useMethodMatcher()
        .withType(String.class)
        .withArgs(Class.class, String.class, Class[].class)
        .withName("mapMethodName")
        .resultAccessorAs();
      methodMapFieldName = remapUtilsReflection
        .useMethodMatcher()
        .withType(String.class)
        .withArgs(Class.class, String.class)
        .withName("mapFieldName")
        .resultAccessorAs();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Classes does not exist, ensure that the server is Magma?", e);
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
    Object magmaJarMapping = fieldRemappingUtilsJarMapping.get(null);
    Map<String, Object> byNMSName = fieldMagmaJarMappingByNMSName.get(magmaJarMapping);
    Object classMappings = byNMSName != null ? byNMSName.get(name) : null;
    String mappedName = classMappings != null ? fieldClassMappingsMcpName.get(classMappings) : null;
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
    return "MagmaRemapper";
  }
}
