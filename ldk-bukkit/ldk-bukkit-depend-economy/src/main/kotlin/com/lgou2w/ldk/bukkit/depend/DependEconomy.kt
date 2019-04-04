/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.depend

import net.milkbowl.vault.Vault
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

/**
 * ## DependEconomy (Vault 插件经济依赖)
 *
 * @see [Depend]
 * @see [DependBase]
 * @author lgou2w
 */
class DependEconomy : DependBase<Vault>(getPlugin(NAME)) {

    private val economy : Economy
        get() {
            checkReference() // check ref
            return field
        }

    init {
        val registrations = Bukkit.getServicesManager().getRegistrations(Economy::class.java)
        if (registrations.size > 1)
            plugin.logger.info("[LDK] There are multiple economic services, using maximum priority.")
        val registered = registrations.sortedBy { it.priority }.lastOrNull() // Maximum priority
                         ?: throw DependCannotException("The Vault does not have a registered Economy service.")
        economy = registered.provider
        plugin.logger.info("[LDK] Uses ${economy.name} as an economy dependency.")
    }

    /**
     * * Get the economy name that this economy depend.
     * * 获取此经济依赖的经济名称.
     */
    val economyName : String
        get() = economy.name

    /**
     * * Get the economy fractional digits that this economy depend.
     * * 获取此经济依赖的经济分数位.
     */
    fun fractionalDigits(): Int
            = economy.fractionalDigits()

    /**
     * * Get the economy currency name plural that this economy depend.
     * * 获取此经济依赖的经济货币名称复数.
     */
    fun currencyNamePlural(): String
            = economy.currencyNamePlural()

    /**
     * * Get the economy currency name singular that this economy depend.
     * * 获取此经济依赖的经济货币名称单数.
     */
    fun currencyNameSingular(): String
            = economy.currencyNameSingular()

    /**
     * * Format the amount [value] as a human readable string.
     * * 将数量值 [value] 格式化为人类可读的字符串.
     */
    fun format(value: Double): String
            = economy.format(value)

    /**
     * * Get a given [player] Whether you have an economy account.
     * * 获取给定的玩家 [player] 是否拥有经济账户.
     */
    @JvmOverloads
    fun hasAccount(player: OfflinePlayer, world: String? = null): Boolean
            = economy.hasAccount(player, world)

    /**
     * * Get a given [playerName] Whether you have an economy account.
     * * 获取给定的玩家 [playerName] 是否拥有经济账户.
     *
     * @throws [DependCannotException] If the API function is not available.
     * @throws [DependCannotException] 如果 API 函数不可用.
     */
    @JvmOverloads
    @Throws(DependCannotException::class)
    @Deprecated("Not recommended method.", ReplaceWith("hasAccount(OfflinePlayer, String)"))
    fun hasAccount(playerName: String, world: String? = null): Boolean {
        return try {
            @Suppress("DEPRECATION")
            economy.hasAccount(playerName, world)
        } catch (e: NoSuchMethodException) {
            throw DependCannotException(EXCEPTION_METHOD_REMOVED)
        }
    }

    /**
     * * Create an economy account for a given [player].
     * * 将给定的玩家 [player] 创建一个经济账户.
     */
    @JvmOverloads
    fun createAccount(player: OfflinePlayer, world: String? = null): Boolean
            = economy.createPlayerAccount(player, world)

    /**
     * * Create an economy account for a given [playerName].
     * * 将给定的玩家 [playerName] 创建一个经济账户.
     *
     * @throws [DependCannotException] If the API function is not available.
     * @throws [DependCannotException] 如果 API 函数不可用.
     */
    @JvmOverloads
    @Throws(DependCannotException::class)
    @Deprecated("Not recommended method.", ReplaceWith("createAccount(OfflinePlayer, String)"))
    fun createAccount(playerName: String, world: String? = null): Boolean {
        return try {
            @Suppress("DEPRECATION")
            economy.createPlayerAccount(playerName, world)
        } catch (e: NoSuchMethodException) {
            throw DependCannotException(EXCEPTION_METHOD_REMOVED)
        }
    }

    /**
     * * Get the economy balance of a given [player].
     * * 获取给定的玩家 [player] 所拥有的经济余额.
     */
    @JvmOverloads
    fun getBalance(player: OfflinePlayer, world: String? = null): Double
            = economy.getBalance(player, world)

    /**
     * * Get the economy balance of a given [playerName].
     * * 获取给定的玩家 [playerName] 所拥有的经济余额.
     *
     * @throws [DependCannotException] If the API function is not available.
     * @throws [DependCannotException] 如果 API 函数不可用.
     */
    @JvmOverloads
    @Throws(DependCannotException::class)
    @Deprecated("Not recommended method.", ReplaceWith("getBalance(OfflinePlayer, String)"))
    fun getBalance(playerName: String, world: String? = null): Double {
        return try {
            @Suppress("DEPRECATION")
            economy.getBalance(playerName, world)
        } catch (e: NoSuchMethodException) {
            throw DependCannotException(EXCEPTION_METHOD_REMOVED)
        }
    }

    /**
     * * Gets the given [player] whether it has the economy balance of the given amount [value].
     * * 获取给定的玩家 [player] 是否拥有给定数量 [value] 的经济余额.
     */
    @JvmOverloads
    fun hasBalance(player: OfflinePlayer, value: Double, world: String? = null): Boolean
            = economy.has(player, world, value)

    /**
     * * Gets the given [playerName] whether it has the economy balance of the given amount [value].
     * * 获取给定的玩家 [playerName] 是否拥有给定数量 [value] 的经济余额.
     *
     * @throws [DependCannotException] If the API function is not available.
     * @throws [DependCannotException] 如果 API 函数不可用.
     */
    @JvmOverloads
    @Throws(DependCannotException::class)
    @Deprecated("Not recommended method.", ReplaceWith("hasBalance(OfflinePlayer, Double, String)"))
    fun hasBalance(playerName: String, value: Double, world: String? = null): Boolean {
        return try {
            @Suppress("DEPRECATION")
            economy.has(playerName, world, value)
        } catch (e: NoSuchMethodException) {
            throw DependCannotException(EXCEPTION_METHOD_REMOVED)
        }
    }

    /**
     * * Withdraw the economy balance of a given amount [value] from a given [player] economy account.
     * * 从给定的玩家 [player] 经济账户中提取给定数量 [value] 的经济余额.
     */
    @JvmOverloads
    fun withdraw(player: OfflinePlayer, value: Double, world: String? = null): Response
            = economy.withdrawPlayer(player, world, value).toAdapter()

    /**
     * * Withdraw the economy balance of a given amount [value] from a given [playerName] economy account.
     * * 从给定的玩家 [playerName] 经济账户中提取给定数量 [value] 的经济余额.
     *
     * @throws [DependCannotException] If the API function is not available.
     * @throws [DependCannotException] 如果 API 函数不可用.
     */
    @JvmOverloads
    @Throws(DependCannotException::class)
    @Deprecated("Not recommended method.", ReplaceWith("withdraw(OfflinePlayer, Double, String)"))
    fun withdraw(playerName: String, value: Double, world: String? = null): Response {
        return try {
            @Suppress("DEPRECATION")
            economy.withdrawPlayer(playerName, world, value).toAdapter()
        } catch (e: NoSuchMethodException) {
            throw DependCannotException(EXCEPTION_METHOD_REMOVED)
        }
    }

    /**
     * * Deposit the economy balance of a given amount [value] from a given [player] economy account.
     * * 从给定的玩家 [player] 经济账户中存入给定数量 [value] 的经济余额.
     */
    @JvmOverloads
    fun deposit(player: OfflinePlayer, value: Double, world: String? = null): Response
            = economy.depositPlayer(player, world, value).toAdapter()

    /**
     * * Deposit the economy balance of a given amount [value] from a given [playerName] economy account.
     * * 从给定的玩家 [playerName] 经济账户中存入给定数量 [value] 的经济余额.
     *
     * @throws [DependCannotException] If the API function is not available.
     * @throws [DependCannotException] 如果 API 函数不可用.
     */
    @JvmOverloads
    @Throws(DependCannotException::class)
    @Deprecated("Not recommended method.", ReplaceWith("deposit(OfflinePlayer, Double, String)"))
    fun deposit(playerName: String, value: Double, world: String? = null): Response {
        return try {
            @Suppress("DEPRECATION")
            economy.depositPlayer(playerName, world, value).toAdapter()
        } catch (e: NoSuchMethodException) {
            throw DependCannotException(EXCEPTION_METHOD_REMOVED)
        }
    }

    /**
     * * Get this economy depend to support bank feature.
     * * 获取此经济依赖是否支持银行功能.
     */
    fun hasBankSupport(): Boolean
            = economy.hasBankSupport()

    /**
     * * Create a bank account with the given [name] for the given [owner].
     * * 将给定的拥有者 [owner] 创建给定名称 [name] 的银行账户.
     */
    fun createBank(name: String, owner: OfflinePlayer): Response
            = economy.createBank(name, owner).toAdapter()

    /**
     * * Create a bank account with the given [name] for the given [owner].
     * * 将给定的拥有者 [owner] 创建给定名称 [name] 的银行账户.
     *
     * @throws [DependCannotException] If the API function is not available.
     * @throws [DependCannotException] 如果 API 函数不可用.
     */
    @Throws(DependCannotException::class)
    @Deprecated("Not recommended method.", ReplaceWith("createBank(String, OfflinePlayer)"))
    fun createBank(name: String, owner: String): Response {
        return try {
            @Suppress("DEPRECATION")
            economy.createBank(name, owner).toAdapter()
        } catch (e: NoSuchMethodException) {
            throw DependCannotException(EXCEPTION_METHOD_REMOVED)
        }
    }

    /**
     * * Delete the bank account with the given name [name].
     * * 删除给定名称 [name] 的银行账户.
     */
    fun deleteBank(name: String): Response
            = economy.deleteBank(name).toAdapter()

    /**
     * * Get the bank account balance for the given [name].
     * * 获取给定名称 [name] 的银行账户余额.
     */
    fun getBankBalance(name: String): Response
            = economy.bankBalance(name).toAdapter()

    /**
     * * Get the given [name] bank Whether has the balance of the given amount [value].
     * * 获取给定名称 [name] 的银行是否拥有给定数量 [value] 的余额.
     */
    fun hasBankBalance(name: String, value: Double): Response
            = economy.bankHas(name, value).toAdapter()

    /**
     * * Withdraw the balance of the given amount [value] from the bank of the given [name].
     * * 从给定名称 [name] 的银行提取给定数量 [value] 的余额.
     */
    fun bankWithdraw(name: String, value: Double): Response
            = economy.bankWithdraw(name, value).toAdapter()

    /**
     * * Deposit the balance of the given amount [value] from the bank of the given [name].
     * * 从给定名称 [name] 的银行存入给定数量 [value] 的余额.
     */
    fun bankDeposit(name: String, value: Double): Response
            = economy.bankDeposit(name, value).toAdapter()

    /**
     * * Get whether the given [player] is the bank owner of the given [name].
     * * 获取给定的玩家 [player] 是否为给定名称 [name] 的银行拥有者.
     */
    fun isBankOwner(name: String, player: OfflinePlayer): Response
            = economy.isBankOwner(name, player).toAdapter()

    /**
     * * Get whether the given [player] is the bank owner of the given [name].
     * * 获取给定的玩家 [player] 是否为给定名称 [name] 的银行拥有者.
     *
     * @throws [DependCannotException] If the API function is not available.
     * @throws [DependCannotException] 如果 API 函数不可用.
     */
    @Throws(DependCannotException::class)
    @Deprecated("Not recommended method.", ReplaceWith("isBankOwner(String, OfflinePlayer)"))
    fun isBankOwner(name: String, player: String): Response {
        return try {
            @Suppress("DEPRECATION")
            economy.isBankOwner(name, player).toAdapter()
        } catch (e: NoSuchMethodException) {
            throw DependCannotException(EXCEPTION_METHOD_REMOVED)
        }
    }

    /**
     * * Get whether the given [player] is the bank member of the given [name].
     * * 获取给定的玩家 [player] 是否为给定名称 [name] 的银行成员.
     */
    fun isBankMember(name: String, player: OfflinePlayer): Response
            = economy.isBankMember(name, player).toAdapter()

    /**
     * * Get whether the given [player] is the bank member of the given [name].
     * * 获取给定的玩家 [player] 是否为给定名称 [name] 的银行成员.
     *
     * @throws [DependCannotException] If the API function is not available.
     * @throws [DependCannotException] 如果 API 函数不可用.
     */
    @Throws(DependCannotException::class)
    @Deprecated("Not recommended method.", ReplaceWith("isBankMember(String, OfflinePlayer)"))
    fun isBankMember(name: String, player: String): Response {
        return try {
            @Suppress("DEPRECATION")
            economy.isBankMember(name, player).toAdapter()
        } catch (e: NoSuchMethodException) {
            throw DependCannotException(EXCEPTION_METHOD_REMOVED)
        }
    }

    /**
     * * Get a list of bank account names that this economy depend.
     * * 获取此经济依赖的银行账户名列表.
     */
    val banks : List<String>
        get() = economy.banks

    companion object {
        const val NAME = "Vault"
        const val EXCEPTION_METHOD_REMOVED = "Error, method has deprecated and removed."
        @JvmField val NULL = Response(.0, .0, Response.Type.NULL, null)
    }

    private fun EconomyResponse?.toAdapter(): Response {
        return  if (this?.type == null) {
            NULL
        } else {
            val adapterType = when (type) {
                EconomyResponse.ResponseType.FAILURE -> Response.Type.FAILURE
                EconomyResponse.ResponseType.SUCCESS -> Response.Type.SUCCESS
                EconomyResponse.ResponseType.NOT_IMPLEMENTED -> Response.Type.NOT_IMPLEMENTED
                else -> Response.Type.NULL
            }
            Response(amount, balance, adapterType, errorMessage)
        }
    }

    /**
     * ## Response (经济响应)
     *
     * @author lgou2w
     */
    data class Response(
            /**
             * * The amount of this response.
             * * 此响应的数量.
             */
            val amount: Double,
            /**
             * * The balance of this response.
             * * 此响应的余额.
             */
            val balance: Double,
            /**
             * * The type of this response.
             * * 此响应的类型.
             */
            val type: Type,
            /**
             * * The error message of this response.
             * * 此响应的错误消息.
             */
            val errorMessage: String? = null
    ) {

        /**
         * * Get this response if the transaction is successful.
         * * 获取此响应是否交易成功.
         */
        fun transactionSuccess(): Boolean
                = type == Type.SUCCESS

        /**
         * ## Type (经济响应类型)
         *
         * @author lgou2w
         */
        enum class Type {

            /**
             * * Response Type: Success
             * * 回应类型: 成功
             */
            SUCCESS,
            /**
             * * Response Type: Failure
             * * 回应类型: 失败
             */
            FAILURE,
            /**
             * * Response Type: Not Implemented
             * * 回应类型: 未实现
             */
            NOT_IMPLEMENTED,
            /**
             * * Economy Response Type: NULL
             * * 回应类型: NULL
             */
            NULL,
            ;
        }
    }
}
