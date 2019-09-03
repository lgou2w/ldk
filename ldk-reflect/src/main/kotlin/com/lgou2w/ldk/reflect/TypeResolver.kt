/*
 * Copyright 2002-2017 the TypeTools original author or authors.
 *
 *      https://github.com/jhalterman/typetools
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (C) 2019 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.reflect

import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Constructor
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Member
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType
import java.security.AccessController
import java.security.PrivilegedExceptionAction
import java.util.Arrays
import java.util.Collections
import java.util.WeakHashMap

/*
 *  Modify: TypeTools TypeResolver Java -> Kotlin
 *  by lgou2w on 03/09/2019
 */

/**
 * Enhanced type resolution utilities.
 *
 * @author Jonathan Halterman
 */
object TypeResolver {

    /** Cache of type variable/argument pairs */
    @JvmStatic
    private val TYPE_VARIABLE_CACHE = Collections.synchronizedMap(
            WeakHashMap<Class<*>, Reference<MutableMap<TypeVariable<*>, Type>>>())

    @JvmStatic @Volatile private var CACHE_ENABLE = true
    @JvmStatic private var RESOLVES_LAMBDAS : Boolean = false
    @JvmStatic private var GET_CONSTANT_POOL : Method? = null
    @JvmStatic private var GET_CONSTANT_POOL_SIZE : Method? = null
    @JvmStatic private var GET_CONSTANT_POOL_METHOD_AT : Method? = null
    @JvmStatic private val OBJECT_METHODS = HashMap<String, Method>()
    @JvmStatic private val PRIMITIVE_WRAPPERS : MutableMap<Class<*>, Class<*>>
    @JvmStatic private val JAVA_VERSION = java.lang.Double.parseDouble(System.getProperty("java.specification.version", "0"))

    init {
        try {
            val unsafe = AccessController.doPrivileged(PrivilegedExceptionAction<sun.misc.Unsafe> {
                val f = sun.misc.Unsafe::class.java.getDeclaredField("theUnsafe")
                f.isAccessible = true
                f.get(null) as sun.misc.Unsafe
            })

            GET_CONSTANT_POOL = Class::class.java.getDeclaredMethod("getConstantPool")
            val constantPoolName = if (JAVA_VERSION < 9) "sun.reflect.ConstantPool" else "jdk.internal.reflect.ConstantPool"
            val constantPoolClass = Class.forName(constantPoolName)
            GET_CONSTANT_POOL_SIZE = constantPoolClass.getDeclaredMethod("getSize")
            GET_CONSTANT_POOL_METHOD_AT = constantPoolClass.getDeclaredMethod("getMethodAt", Integer.TYPE)

            // setting the methods as accessible
            val overrideField = AccessibleObject::class.java.getDeclaredField("override")
            val overrideFieldOffset = unsafe.objectFieldOffset(overrideField)
            unsafe.putBoolean(GET_CONSTANT_POOL, overrideFieldOffset, true)
            unsafe.putBoolean(GET_CONSTANT_POOL_SIZE, overrideFieldOffset, true)
            unsafe.putBoolean(GET_CONSTANT_POOL_METHOD_AT, overrideFieldOffset, true)

            // additional checks - make sure we get a result when invoking the Class::getConstantPool and
            // ConstantPool::getSize on a class
            val constantPool = GET_CONSTANT_POOL?.invoke(Object::class.java)
            GET_CONSTANT_POOL_SIZE?.invoke(constantPool)

            for (method in Object::class.java.declaredMethods)
                OBJECT_METHODS[method.name] = method

            RESOLVES_LAMBDAS = true
        } catch (e: Exception) {
        }

        val types = HashMap<Class<*>, Class<*>>()
        types[java.lang.Boolean.TYPE] = java.lang.Boolean::class.java
        types[java.lang.Byte.TYPE] = java.lang.Byte::class.java
        types[java.lang.Character.TYPE] = java.lang.Character::class.java
        types[java.lang.Double.TYPE] = java.lang.Double::class.java
        types[java.lang.Float.TYPE] = java.lang.Float::class.java
        types[java.lang.Integer.TYPE] = java.lang.Integer::class.java
        types[java.lang.Long.TYPE] = java.lang.Long::class.java
        types[java.lang.Short.TYPE] = java.lang.Short::class.java
        types[java.lang.Void.TYPE] = java.lang.Void::class.java
        PRIMITIVE_WRAPPERS = Collections.unmodifiableMap(types)
    }

    /** An unknown type. */
    class Unknown private constructor()

    @JvmStatic fun enableCache() { CACHE_ENABLE = true }
    @JvmStatic fun disableCache() {
        TYPE_VARIABLE_CACHE.clear()
        CACHE_ENABLE = false
    }

    @JvmStatic fun <T, S : T> resolveRawArgument(type: Class<T>, subType: Class<S>): Class<*>?
            = resolveRawArgument(resolveGenericType(type, subType), subType)

    @JvmStatic fun resolveRawArgument(genericType: Type?, subType: Class<*>): Class<*>? {
        val arguments = resolveRawArguments(genericType, subType) ?: return Unknown::class.java
        if (arguments.size != 1)
            throw IllegalArgumentException(
                    "Expected 1 argument for generic type " + genericType + " but found " + arguments.size)
        return arguments[0]
    }

    @JvmStatic fun <T, S : T> resolveRawArguments(type: Class<T>, subType: Class<S>): Array<Class<*>?>?
            = resolveRawArguments(resolveGenericType(type, subType), subType)

    @JvmStatic fun <T, S : T> reify(type: Class<T>, context: Class<S>): Type?
            = reify(resolveGenericType(type, context), getTypeVariableMap(context, null))

    @JvmStatic fun reify(type: Type, context: Class<*>): Type?
            = reify(type, getTypeVariableMap(context, null))

    @JvmStatic fun reify(type: Type): Type?
            = reify(type, HashMap())

    @JvmStatic fun resolveRawArguments(genericType: Type?, subType: Class<*>): Array<Class<*>?>? {
        var result : Array<Class<*>?>? = null
        var functionalInterface : Class<*>? = null

        // Handle lambdas
        if (RESOLVES_LAMBDAS && subType.isSynthetic) {
            val fi = if (genericType is ParameterizedType && genericType.rawType is Class<*>) genericType.rawType as Class<*>
            else if (genericType is Class<*>) genericType else null
            if (fi != null && fi.isInterface)
                functionalInterface = fi
        }

        when (genericType) {
            is ParameterizedType -> {
                val arguments = genericType.actualTypeArguments
                result = Array(arguments.size) { i ->
                    resolveRawClass(arguments[i], subType, functionalInterface)
                }
            }
            is TypeVariable<*> -> {
                result = Array(1) { resolveRawClass(genericType, subType, functionalInterface) }
            }
            is Class<*> -> {
                val typeParams = genericType.typeParameters
                result = Array(typeParams.size) { i ->
                    resolveRawClass(typeParams[i], subType, functionalInterface)
                }
            }
        }

        return result
    }

    @JvmStatic fun resolveGenericType(type: Class<*>, subType: Type): Type? {
        val rawType = if (subType is ParameterizedType)
            subType.rawType as Class<*>
        else
            subType as Class<*>

        if (type == rawType)
            return subType

        var result : Type?
        if (type.isInterface) {
            for (superInterface in rawType.genericInterfaces)
                if (superInterface != null && superInterface != java.lang.Object::class.java)
                    if (resolveGenericType(type, superInterface).apply { result = this } != null)
                        return result
        }

        val superClass : Type? = rawType.genericSuperclass
        if (superClass != null && superClass != java.lang.Object::class.java)
            if (resolveGenericType(type, superClass).apply { result = this } != null)
                return result

        return null
    }

    @JvmStatic fun resolveRawClass(genericType: Type, subType: Class<*>): Class<*>
            = resolveRawClass(genericType, subType, null)

    @JvmStatic private fun resolveRawClass(genericType: Type, subType: Class<*>, functionalInterface: Class<*>?): Class<*> {
        var genericType0 : Type? = genericType
        return when (genericType0) {
            is Class<*> -> genericType0
            is ParameterizedType -> resolveRawClass(genericType0.rawType, subType, functionalInterface)
            is GenericArrayType -> {
                val component = resolveRawClass(genericType0.genericComponentType, subType, functionalInterface)
                java.lang.reflect.Array.newInstance(component, 0).javaClass
            }
            is TypeVariable<*> -> {
                genericType0 = getTypeVariableMap(subType, functionalInterface)[genericType]
                genericType0 = if (genericType0 == null) resolveBound(genericType as TypeVariable<*>) else
                    resolveRawClass(genericType0, subType, functionalInterface)
                if (genericType0 is Class<*>) genericType0 else Unknown::class.java
            }
            else -> {
                if (genericType0 is Class<*>) genericType0 else Unknown::class.java
            }
        }
    }

    @JvmStatic private fun reify(genericType: Type?, typeVariableMap: Map<TypeVariable<*>, Type>): Type? {
        // Check for terminal cases to avoid allocating HashMap and trivial call.
        return when (genericType) {
            null -> null
            is Class<*> -> genericType
            else -> reify(genericType, typeVariableMap, HashMap())
        }
    }

    @JvmStatic private fun reify(
            genericType: Type,
            typeVariableMap: Map<TypeVariable<*>, Type>,
            cache: MutableMap<Type, Type>
    ): Type {
        var genericType0 : Type? = genericType
        // Terminal case.
        if (genericType0 is Class<*>)
            return genericType0

        // For cycles of length larger than one, find its last element by chasing through cache.
        while (cache.containsKey(genericType0))
            genericType0 = cache[genericType0]

        // Recursive cases.
        when (genericType0) {
            is ParameterizedType -> {
                val parameterizedType = genericType0
                val genericTypeArguments = genericType0.actualTypeArguments
                val reifiedTypeArguments = Array<Type?>(genericTypeArguments.size) { null }
                val result = ReifiedParameterizedType(parameterizedType)
                cache[parameterizedType] = result

                var changed = false
                var i = 0
                while (i < genericTypeArguments.size) {
                    // Cycle detection. In case a genericTypeArgument is null, it is currently being resolved,
                    // thus there's a cycle in the type's structure.
                    if (genericTypeArguments[i] == null)
                        return parameterizedType
                    reifiedTypeArguments[i] = reify(genericTypeArguments[i], typeVariableMap, cache)
                    changed = changed || (reifiedTypeArguments[i] != genericTypeArguments[i])
                    i++
                }

                if (!changed)
                    return parameterizedType

                result.setReifiedTypeArguments(reifiedTypeArguments)
                return result
            }
            is GenericArrayType -> {
                val genericArrayType = genericType0
                val genericComponentType = genericArrayType.genericComponentType
                val reifiedComponentType = reify(genericArrayType.genericComponentType, typeVariableMap, cache)

                if (genericComponentType == reifiedComponentType)
                    return genericComponentType

                if (reifiedComponentType is Class<*>)
                    return java.lang.reflect.Array.newInstance(reifiedComponentType, 0).javaClass

                throw UnsupportedOperationException(
                        "Attempted to reify generic array type, whose generic component type " +
                        "could not be reified to some Class<?>. Handling for this case is not implemented")
            }
            is TypeVariable<*> -> {
                val typeVariable = genericType0
                val mapping = typeVariableMap[typeVariable]
                if (mapping != null) {
                    cache[typeVariable] = mapping
                    return reify(mapping, typeVariableMap, cache)
                }

                val upperBounds = typeVariable.bounds

                // Copy cache in case the bound is mutually recursive on the variable. This is to avoid sharing of
                // cache in different branches of the call-graph of reify.
                val cache2 = HashMap(cache)

                // NOTE: According to https://docs.oracle.com/javase/tutorial/java/generics/bounded.html
                // if there are multiple upper bounds where one bound is a class, then this must be the
                // leftmost/first bound. Therefore we blindly take this one, hoping is the most relevant.
                // Hibernate does the same when erasing types, see also
                // https://github.com/hibernate/hibernate-validator/blob/6.0/engine/src/main/java/org/hibernate/validator/internal/util/TypeHelper.java#L181-L186
                cache2[typeVariable] = upperBounds[0]
                return reify(upperBounds[0], typeVariableMap, cache2)
            }
            is WildcardType -> {
                val wildcardType = genericType0
                val upperBounds = wildcardType.upperBounds
                val lowerBounds = wildcardType.lowerBounds
                if (upperBounds.size == 1 && lowerBounds.isEmpty())
                    return reify(upperBounds[0], typeVariableMap, cache)

                throw UnsupportedOperationException(
                        "Attempted to reify wildcard type with name '" + wildcardType + "' which has " +
                        upperBounds.size + " upper bounds and " + lowerBounds.size + " lower bounds. " +
                        "Reification of wildcard types is only supported for " +
                        "the trivial case of exactly one upper bound and no lower bounds.")
            }
            else -> throw UnsupportedOperationException(
                    "Reification of type with name '" + genericType.typeName + "' and " +
                    "class name '" + genericType.javaClass.name + "' is not implemented.")
        }
    }

    @JvmStatic private fun getTypeVariableMap(
            targetType: Class<*>,
            functionalInterface: Class<*>?
    ): MutableMap<TypeVariable<*>, Type> {
        val ref = TYPE_VARIABLE_CACHE[targetType]
        var map = ref?.get()

        if (map == null) {
            map = HashMap()

            // Populate lambdas
            if (functionalInterface != null)
                populateLambdaArgs(functionalInterface, targetType, map)

            // Populate interfaces
            populateSuperTypeArgs(targetType.genericInterfaces, map, functionalInterface != null)

            // Populate super classes and interfaces
            var genericType = targetType.genericSuperclass
            var type : Class<*>? = targetType.superclass
            while (type != null && java.lang.Object::class.java != type) {
                if (genericType is ParameterizedType)
                    populateTypeArgs(genericType, map, false)
                populateSuperTypeArgs(type.genericInterfaces, map, false)

                genericType = type.genericSuperclass
                type = type.superclass
            }

            // Populate enclosing classes
            type = targetType
            while (type != null && type.isMemberClass) {
                genericType = type.genericSuperclass
                if (genericType is ParameterizedType)
                    populateTypeArgs(genericType, map, functionalInterface != null)

                type = type.enclosingClass
            }

            if (CACHE_ENABLE)
                TYPE_VARIABLE_CACHE[targetType] = WeakReference<MutableMap<TypeVariable<*>, Type>>(map)
        }
        return map
    }

    @JvmStatic private fun populateSuperTypeArgs(
            types: Array<Type>,
            map: MutableMap<TypeVariable<*>, Type>,
            depthFirst: Boolean
    ) {
        for (type in types) {
            if (type is ParameterizedType) {
                if (!depthFirst)
                    populateTypeArgs(type, map, depthFirst)
                val rawType = type.rawType
                if (rawType is Class<*>)
                    populateSuperTypeArgs(rawType.genericInterfaces, map, depthFirst)
                if (depthFirst)
                    populateTypeArgs(type, map, depthFirst)
            } else if (type is Class<*>) {
                populateSuperTypeArgs(type.genericInterfaces, map, depthFirst)
            }
        }
    }

    @JvmStatic private fun populateTypeArgs(
            type: ParameterizedType,
            map: MutableMap<TypeVariable<*>, Type>,
            depthFirst: Boolean
    ) {
        if (type.rawType is Class<*>) {
            val typeVariables = (type.rawType as Class<*>).typeParameters
            val typeArguments = type.actualTypeArguments

            if (type.ownerType != null) {
                val owner = type.ownerType
                if (owner is ParameterizedType)
                    populateTypeArgs(owner, map, depthFirst)
            }

            var i = 0
            loop@while (i < typeArguments.size) {
                val variable = typeVariables[i]
                when (val typeArgument = typeArguments[i]) {
                    is Class<*>,
                    is GenericArrayType,
                    is ParameterizedType -> map[variable] = typeArgument
                    is TypeVariable<*> -> {
                        if (depthFirst) {
                            val existingType = map[variable]
                            if (existingType != null) {
                                map[typeArgument] = existingType
                                continue@loop
                            }
                        }

                        var resolvedType = map[typeArgument]
                        if (resolvedType == null)
                            resolvedType = resolveBound(typeArgument)
                        map[variable] = resolvedType
                    }
                }

                i++
            }
        }
    }

    @JvmStatic fun resolveBound(typeVariable: TypeVariable<*>): Type {
        val bounds = typeVariable.bounds
        if (bounds.isEmpty())
            return Unknown::class.java

        var bound = bounds[0]
        if (bound is TypeVariable<*>)
            bound = resolveBound(bound)

        return if (bound == Object::class.java) Unknown::class.java else bound
    }

    @JvmStatic private fun populateLambdaArgs(
            functionalInterface: Class<*>,
            lambdaType: Class<*>,
            map: MutableMap<TypeVariable<*>, Type>
    ) {
        if (RESOLVES_LAMBDAS) {
            // Find SAM
            for (m in functionalInterface.methods) {
                if (!isDefaultMethod(m) && !Modifier.isStatic(m.modifiers) && !m.isBridge) {
                    // Skip methods that override Object.class
                    val objectMethod = OBJECT_METHODS[m.name]
                    if (objectMethod != null && Arrays.equals(m.typeParameters, objectMethod.typeParameters))
                        continue

                    // Get functional interface's type params
                    val returnTypeVar = m.genericReturnType
                    val paramTypeVars = m.genericParameterTypes

                    val member = getMemberRef(lambdaType) ?: return

                    // Populate return type argument
                    if (returnTypeVar is TypeVariable<*>) {
                        var returnType = if (member is Method) member.returnType else member.declaringClass
                        returnType = wrapPrimitives(returnType)
                        if (returnType != java.lang.Void::class.java)
                            map[returnTypeVar] = returnType
                    }

                    val arguments = if (member is Method) member.parameterTypes else (member as Constructor<*>).parameterTypes

                    // Populate object type from arbitrary object method reference
                    var paramOffset = 0
                    if (paramTypeVars.isNotEmpty() && paramTypeVars[0] is TypeVariable<*> && paramTypeVars.size == arguments.size + 1) {
                        val instanceType = member.declaringClass
                        map[paramTypeVars[0] as TypeVariable<*>] = instanceType
                        paramOffset = 1
                    }

                    // Handle additional arguments that are captured from the lambda's enclosing scope
                    var argOffset = 0
                    if (paramTypeVars.size < arguments.size)
                        argOffset = arguments.size - paramTypeVars.size

                    // Populate type arguments
                    var i = 0
                    while (i + argOffset < arguments.size) {
                        if (paramTypeVars[i] is TypeVariable<*>)
                            map[paramTypeVars[i + paramOffset] as TypeVariable<*>] = wrapPrimitives(arguments[i + argOffset])
                        i++
                    }

                    return
                }
            }
        }
    }

    @JvmStatic private fun isDefaultMethod(method: Method): Boolean
            = JAVA_VERSION >= 1.8 && method.isDefault

    @JvmStatic private fun getMemberRef(type: Class<*>): Member? {
        val constantPool : Any?
        try {
            constantPool = GET_CONSTANT_POOL?.invoke(type)
        } catch (e: Exception) {
            return null
        }

        var result : Member? = null
        var i = getConstantPoolSize(constantPool) - 1
        while (i >= 0) {
            val member = getConstantPoolMethodAt(constantPool, i)
            // Skip SerializedLambda constructors and members of the "type" class
            if (member == null
                || (member is Constructor<*>
                    && member.declaringClass.name == "java.lang.invoke.SerializedLambda")
                || member.declaringClass.isAssignableFrom(type)
            )
                continue

            result = member

            // Return if not valueOf method
            if (member !is Method || !isAutoBoxingMethod(member))
                break

            i--
        }

        return result
    }

    @JvmStatic private fun isAutoBoxingMethod(method: Method): Boolean {
        val parameters = method.parameterTypes
        return method.name == "valueOf" && parameters.size == 1 && parameters[0].isPrimitive
               && wrapPrimitives(parameters[0]) == method.declaringClass
    }

    @JvmStatic private fun wrapPrimitives(clazz: Class<*>): Class<*>
            = if (clazz.isPrimitive) PRIMITIVE_WRAPPERS[clazz] ?: clazz else clazz

    @JvmStatic private fun getConstantPoolSize(constantPool: Any?): Int {
        return try {
            GET_CONSTANT_POOL_SIZE?.invoke(constantPool) as Int
        } catch (e: Exception) {
            0
        }
    }

    @JvmStatic private fun getConstantPoolMethodAt(constantPool: Any?, i: Int): Member? {
        return try {
            GET_CONSTANT_POOL_METHOD_AT?.invoke(constantPool, i) as? Member
        } catch (e: Exception) {
            null
        }
    }
}

internal class ReifiedParameterizedType internal constructor(
        private val original: ParameterizedType
) : ParameterizedType {

    private val reifiedTypeArguments : Array<Type?>
            = Array(original.actualTypeArguments.size) { null }

    override fun getActualTypeArguments(): Array<Type?>
            = reifiedTypeArguments

    override fun getRawType(): Type
            = original.rawType

    override fun getOwnerType(): Type?
            = original.ownerType

    /**
     * NOTE: This method should only be called once per instance.
     */
    internal fun setReifiedTypeArguments(reifiedTypeArguments: Array<Type?>)
            = System.arraycopy(reifiedTypeArguments, 0, this.reifiedTypeArguments, 0, this.reifiedTypeArguments.size)

    override fun hashCode(): Int {
        var result = original.hashCode()
        result = 31 * result + reifiedTypeArguments.contentHashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReifiedParameterizedType

        if (original != other.original) return false
        if (!reifiedTypeArguments.contentEquals(other.reifiedTypeArguments)) return false

        return true
    }

    /** Keep this consistent with {@link sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl#toString} */
    override fun toString(): String {
        val ownerType : Type? = ownerType
        val rawType = rawType
        val actualTypeArguments = actualTypeArguments
        return buildString {
            if (ownerType != null) {
                if (ownerType is Class<*>)
                    append(ownerType.name)
                else
                    append(ownerType.toString())
                append(".")
                if (ownerType is ParameterizedType) {
                    // Find simple name of nested type by removing the
                    // shared prefix with owner.
                    append(rawType.typeName.replace(ownerType.typeName + "$", ""))
                } else {
                    append(rawType.typeName)
                }
            } else {
                append(rawType.typeName)
            }

            if (actualTypeArguments.isNotEmpty()) {
                append("<")
                var first = true
                for (t in actualTypeArguments) {
                    if (!first)
                        append(", ")
                    append(if (t == null) "null" else t.typeName)
                    first = false
                }
                append(">")
            }
        }
    }
}
