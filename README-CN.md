- [English](README.md)
- **简体中文**

<p align="center">
<img src=".images/ldk-logo.png" alt="ldk" />
</p>

<p align="center">
<a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-1.3.30-blue.svg"></a>
<a href="https://travis-ci.org/lgou2w/ldk"><img src="https://travis-ci.org/lgou2w/ldk.svg?branch=develop" /></a>
<a href="https://codebeat.co/projects/github-com-lgou2w-ldk-develop"><img alt="codebeat badge" src="https://codebeat.co/badges/7c8fccc7-6096-4f12-81e3-98b1f39a3875" /></a>
<a href="https://search.maven.org/search?q=g:com.lgou2w%20AND%20a:ldk*"><img src="https://img.shields.io/maven-central/v/com.lgou2w/ldk.svg?color=%231081c2" /></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.txt"><img src="https://img.shields.io/hexpm/l/plug.svg" /></a>
<a href="https://github.com/lgou2w/ldk/pulls"><img src="https://img.shields.io/badge/contributing-welcome-FF69B4.svg?style=flat" /></a>
<a href="https://github.com/lgou2w/ldk/issues"><img src="https://img.shields.io/badge/issues-report-E74C3C.svg?style=flat"></a>
</p>

# 介绍

* 一个多平台的 lgou2w 开发套件.

多模块，多平台，低耦合，依赖库，Minecraft 的聊天和 NBT 库，协程，工具，实用程序，包装，
JavaFx，Rxjava，Retrofit，ASM，Bukkit 的协程、扩展和运行时等。快速开发，Lambda，函数编程。 
集成的多模块多平台 lgou2w 开发套件。`LDK`

* 对于 Minecraft 服务器的兼容性.

    <a href="https://github.com/lgou2w/ldk"><img src="https://img.shields.io/badge/Minecraft-Bukkit%20|%20Spigot%20|%20PaperSpigot%20%3E%3D%201.8-brightgreen.svg"></a>

* Wiki

    有关各种模块使用和问题请查看 [维基](https://github.com/lgou2w/ldk/wiki) 内容.

# 模块

<details>
<summary>查看所有模块</summary>

* ldk
    * [`ldk-asm`](/ldk-asm)
    * [`ldk-common`](/ldk-common)
    * [`ldk-coroutines`](/ldk-coroutines)
    * [`ldk-i18n`](/ldk-i18n)
    * [`ldk-reflect`](/ldk-reflect)
    * [`ldk-retrofit`](/ldk-retrofit)
    * [`ldk-rx`](/ldk-rx)
    * [`ldk-fx`](/ldk-fx)
        * [`ldk-fx-common`](/ldk-fx/ldk-fx-common)
        * [`ldk-fx-coroutines`](/ldk-fx/ldk-fx-coroutines)
        * [`ldk-fx-fontawesomefx`](/ldk-fx/ldk-fx-fontawesomefx)
        * [`ldk-fx-jfoenix`](/ldk-fx/ldk-fx-jfoenix)
        * [`ldk-fx-tornadofx`](/ldk-fx/ldk-fx-tornadofx)
        * [`ldk-fx-rx`](/ldk-fx/ldk-fx-rx)
    * [`ldk-rsa`](/ldk-rsa)
    * [`ldk-chat`](/ldk-chat)
    * [`ldk-nbt`](/ldk-nbt)
    * [`ldk-sql`](/ldk-sql)
        * [`ldk-sql-api`](/ldk-sql/ldk-sql-api)
        * [`ldk-sql-hikari`](/ldk-sql/ldk-sql-hikari)
        * [`ldk-sql-sqlite`](/ldk-sql/ldk-sql-sqlite)
        * [`ldk-sql-h2`](/ldk-sql/ldk-sql-h2)
    * [`ldk-exposed`](/ldk-exposed)
    * [`ldk-bukkit`](/ldk-bukkit)
        * [`ldk-bukkit-version`](/ldk-bukkit/ldk-bukkit-version)
        * [`ldk-bukkit-reflect`](/ldk-bukkit/ldk-bukkit-reflect)
        * [`ldk-bukkit-nbt`](/ldk-bukkit/ldk-bukkit-nbt)
        * [`ldk-bukkit-common`](/ldk-bukkit/ldk-bukkit-common)
        * [`ldk-bukkit-compatibility`](/ldk-bukkit/ldk-bukkit-compatibility)
        * [`ldk-bukkit-i18n`](/ldk-bukkit/ldk-bukkit-i18n)
        * [`ldk-bukkit-cmd`](/ldk-bukkit/ldk-bukkit-cmd)
        * [`ldk-bukkit-gui`](/ldk-bukkit/ldk-bukkit-gui)
        * [`ldk-bukkit-region`](/ldk-bukkit/ldk-bukkit-region)
        * [`ldk-bukkit-depend`](/ldk-bukkit/ldk-bukkit-depend)
        * [`ldk-bukkit-depend-economy`](/ldk-bukkit/ldk-bukkit-depend-economy)
        * [`ldk-bukkit-depend-placeholderapi`](/ldk-bukkit/ldk-bukkit-depend-placeholderapi)
        * [`ldk-bukkit-depend-worldedit`](/ldk-bukkit/ldk-bukkit-depend-worldedit)
        * [`ldk-bukkit-rx`](/ldk-bukkit/ldk-bukkit-rx)
        * [`ldk-bukkit-coroutines`](/ldk-bukkit/ldk-bukkit-coroutines)
        * [`ldk-bukkit-plugin`](/ldk-bukkit/ldk-bukkit-plugin)
        
</details>
        
# 构建

## 持续集成

非常感谢 [@25](https://github.com/25) 提供的 `Jenkins CI` 生命周期.

* [稳定版](http://www.soulbound.me/job/ldk/) (master)
* [开发版](http://www.soulbound.me/job/ldk_Dev/) (develop)

## 依赖

可以在 [https://search.maven.org](https://search.maven.org/search?q=g:com.lgou2w%20AND%20a:ldk*) 找到版本标记的 `Maven` 或 `Gradle` 依赖项.

> 模块 ldk-bukkit-plugin 的 Bukkit 运行时分发在 [releases](https://github.com/lgou2w/ldk/releases) 中.

* Maven
```xml
<dependency>
    <groupId>com.lgou2w</groupId>
    <artifactId>${module}</artifactId>
    <version>${version}</version>
    <scope>compile</scope>
</dependency>
```

* Gradle
```groovy
dependencies {
    compile 'com.lgou2w:${module}:${version}'
}
```

# 开发

* Intellij IDEA
* JDK 8
* Kotlin 1.3+
* Maven 3.3+
* Git

```bash
git clone https://github.com/lgou2w/ldk.git
cd ldk
mvn clean package
```

# 捐赠

非常感谢你们的捐赠

* [LywLover](https://github.com/LywLover)
* [EnTIv](https://github.com/EnTIv)

# 协议

```
Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
