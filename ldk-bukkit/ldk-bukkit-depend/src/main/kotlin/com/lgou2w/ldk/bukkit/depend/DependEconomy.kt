/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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

class DependEconomy : DependBase<Vault>(getPlugin(NAME)) {

    private val economy : Economy
        get() {
            checkReference() // check ref
            return field
        }

    init {
        val registered = Bukkit.getServicesManager().getRegistration(Economy::class.java)
        if (registered == null || registered.provider == null)
            throw DependCannotException("The Vault does not have a registered Economy service.")
        economy = registered.provider
    }

    val economyName : String
        get() = economy.name

    fun fractionalDigits(): Int
            = economy.fractionalDigits()

    fun currencyNamePlural(): String
            = economy.currencyNamePlural()

    fun currencyNameSingular(): String
            = economy.currencyNameSingular()

    fun format(value: Double): String
            = economy.format(value)

    @JvmOverloads
    fun hasAccount(player: OfflinePlayer, world: String? = null): Boolean
            = economy.hasAccount(player, world)

    @JvmOverloads
    fun createAccount(player: OfflinePlayer, world: String? = null): Boolean
            = economy.createPlayerAccount(player, world)

    @JvmOverloads
    fun getBalance(player: OfflinePlayer, world: String? = null): Double
            = economy.getBalance(player, world)

    @JvmOverloads
    fun hasBalance(player: OfflinePlayer, value: Double, world: String? = null): Boolean
            = economy.has(player, world, value)

    @JvmOverloads
    fun withdraw(player: OfflinePlayer, value: Double, world: String? = null): Response
            = economy.withdrawPlayer(player, world, value).toAdapter()

    @JvmOverloads
    fun deposit(player: OfflinePlayer, value: Double, world: String? = null): Response
            = economy.depositPlayer(player, world, value).toAdapter()

    fun hasBankSupport(): Boolean
            = economy.hasBankSupport()

    fun createBank(name: String, owner: OfflinePlayer): Response
            = economy.createBank(name, owner).toAdapter()

    fun deleteBank(name: String): Response
            = economy.deleteBank(name).toAdapter()

    fun getBankBalance(name: String): Response
            = economy.bankBalance(name).toAdapter()

    fun hasBankBalance(name: String, value: Double): Response
            = economy.bankHas(name, value).toAdapter()

    fun bankWithdraw(name: String, value: Double): Response
            = economy.bankWithdraw(name, value).toAdapter()

    fun bankDeposit(name: String, value: Double): Response
            = economy.bankDeposit(name, value).toAdapter()

    fun isBankOwner(name: String, player: OfflinePlayer): Response
            = economy.isBankOwner(name, player).toAdapter()

    fun isBankMember(name: String, player: OfflinePlayer): Response
            = economy.isBankMember(name, player).toAdapter()

    val banks: List<String>
        get() = economy.banks

    companion object {
        const val NAME = "Vault"
        @JvmField val NULL = Response(.0, .0, Response.Type.NULL, null)
    }

    private fun net.milkbowl.vault.economy.EconomyResponse?.toAdapter() : Response {
        return  if (this?.type == null) {
            NULL
        } else {
            val adapterType = when (type) {
                net.milkbowl.vault.economy.EconomyResponse.ResponseType.FAILURE -> Response.Type.FAILURE
                net.milkbowl.vault.economy.EconomyResponse.ResponseType.SUCCESS -> Response.Type.SUCCESS
                EconomyResponse.ResponseType.NOT_IMPLEMENTED -> Response.Type.NOT_IMPLEMENTED
                else -> Response.Type.NULL
            }
            Response(amount, balance, adapterType, errorMessage)
        }
    }

    data class Response(
            val amount: Double,
            val balance: Double,
            val type: Type,
            val errorMessage: String? = null) {

        fun transactionSuccess(): Boolean
                = type == Type.SUCCESS

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
