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

package com.lgou2w.ldk.bukkit.event

import com.lgou2w.ldk.common.Applicator
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.Plugin

/**
 * ## EventScope (事件范围)
 *
 * @author lgou2w
 */
class EventScope(
        /**
         * * The plugin object for this event scope.
         * * 此事件范围的插件对象.
         */
        val plugin: Plugin
) {
    /**
     * * Register the given event type listener.
     * * 注册给定的事件类型监听器.
     */
    @JvmOverloads
    inline fun <reified T : Event> event(
            priority: EventPriority = EventPriority.NORMAL,
            ignoreCancelled: Boolean = false,
            noinline executor: Applicator<T>
    ) {
        plugin.server.pluginManager.registerEvent(
                T::class.java,
                object : EventListener {},
                priority,
                { _, event -> executor(event as T) },
                plugin,
                ignoreCancelled
        )
    }
}

/**
 * * Register the event scope listener from the given plugin.
 * * 从给定的插件注册事件范围监听器.
 *
 * @see [EventScope]
 */
inline fun Plugin.registerListeners(scope: Applicator<EventScope>) {
    val eventScope = EventScope(this)
    eventScope.scope()
}
