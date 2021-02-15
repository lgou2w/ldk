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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class Remapper {
  protected final ClassLoader classLoader;

  private Remapper(@Nullable ClassLoader classLoader) {
    this.classLoader = classLoader == null
      ? this.getClass().getClassLoader()
      : classLoader;
  }

  @Nullable
  @Contract("null -> null; !null -> !null")
  public abstract String mapClassName(String name);

  @Nullable
  @Contract("null, _, _ -> fail; _, _, null -> fail; _, null, _ -> null; _, !null, _ -> !null")
  public abstract String mapMethodName(Class<?> clazz, String name, Class<?>[] parameterTypes);

  @Nullable
  @Contract("null, _ -> fail; _, null -> null; _, !null -> !null")
  public abstract String mapFieldName(Class<?> clazz, String name);

  /// Remapper - Arclight

  final static class ArclightRemapper extends Remapper {
    final Object remapper;
    final Class<?> classLoaderRemapper;
    final FieldAccessor<ClassLoader, Object> fieldRemapper;
    final MethodAccessor<Object, String> methodMap;
    final MethodAccessor<Object, Method> methodTryMapMethodToSrg;
    final MethodAccessor<Object, String> methodTryMapFieldToSrg;
    final static String NAME = "io.izzel.arclight.common.mod.util.remapper.ClassLoaderRemapper";

    ArclightRemapper(@Nullable ClassLoader loader) {
      super(loader);
      try {
        classLoaderRemapper = Class.forName(NAME);
        fieldRemapper = FuzzyReflection.of(classLoader, true)
          .useFieldMatcher()
          .withType(classLoaderRemapper)
          .withName("remapper")
          .resultAccessorAs();
        remapper = fieldRemapper.get(classLoader);

        FuzzyReflection remapperReflection = FuzzyReflection.of(remapper, true);
        methodMap = remapperReflection
          .useMethodMatcher()
          .withName("map")
          .withType(String.class)
          .withArgs(String.class)
          .resultAccessorAs();
        methodTryMapMethodToSrg = remapperReflection
          .useMethodMatcher()
          .withName("tryMapMethodToSrg")
          .withType(Method.class)
          .withArgs(Class.class, String.class, Class[].class)
          .resultAccessorAs();
        methodTryMapFieldToSrg = remapperReflection
          .useMethodMatcher()
          .withName("tryMapFieldToSrg")
          .withType(String.class)
          .withArgs(Class.class, String.class)
          .resultAccessorAs();
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("ClassLoaderRemapper class does not exist, ensure that the server is Arclight?", e);
      } catch (NoSuchElementException e) {
        throw new RuntimeException("Error while find structure members of ClassLoaderRemapper class: ", e);
      } catch (Throwable t) {
        throw new RuntimeException("Internal error: ", t);
      }
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

  /// Remapper - CatServer

  final static class CatServerRemapper extends Remapper {
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
}