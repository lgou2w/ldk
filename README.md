- **English**
- [简体中文](README-CN.md)

<p align="center">
<img src=".images/ldk-logo.png" alt="ldk" />
</p>

<p align="center">
<a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-1.3%2b-brightgreen.svg"></a>
<a href="https://travis-ci.org/lgou2w/ldk"><img src="https://travis-ci.org/lgou2w/ldk.svg?branch=develop" /></a>
<a href="https://codebeat.co/projects/github-com-lgou2w-ldk-develop"><img alt="codebeat badge" src="https://codebeat.co/badges/7c8fccc7-6096-4f12-81e3-98b1f39a3875" /></a>
<a href="https://jitpack.io/#lgou2w/ldk"><img src="https://jitpack.io/v/lgou2w/ldk.svg" /></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.txt"><img src="https://img.shields.io/hexpm/l/plug.svg" /></a>
<a href="https://github.com/lgou2w/ldk/pulls"><img src="https://img.shields.io/badge/contributing-welcome-FF69B4.svg?style=flat" /></a>
<a href="https://github.com/lgou2w/ldk/issues"><img src="https://img.shields.io/badge/issues-report-E74C3C.svg?style=flat"></a>
</p>

# Information

* A multi-platform lgou2w development kit.
    
Multi-modules、Multi-platform、Low coupling、Dependency library、Minecraft's chat and nbt library、Coroutines、Tools、Utility、Package、
Rxjava、Retrofit、ASM、Bukkit's coroutines、extended and runtime etc. Rapid development、Lambda、Functional programming. 
Integrated multi-module multi-platform lgou2w development kit. `LDK`

* Compatibility with Minecraft server.

    <a href="https://github.com/lgou2w/ldk"><img src="https://img.shields.io/badge/Minecraft-Bukkit%20|%20Spigot%20|%20PaperSpigot%20%3E%3D%201.8-brightgreen.svg"></a>

# Modules

<details>
<summary>View Modules</summary>

* ldk
    * [`ldk-asm`](/ldk-asm)
    * [`ldk-common`](/ldk-common)
    * [`ldk-coroutines`](/ldk-coroutines)
    * [`ldk-i18n`](/ldk-i18n)
    * [`ldk-reflect`](/ldk-reflect)
    * [`ldk-retrofit`](/ldk-retrofit)
    * [`ldk-rx`](/ldk-rx)
    * [`ldk-fx`](/ldk-fx) (N/A)
    * [`ldk-rsa`](/ldk-rsa)
    * [`ldk-chat`](/ldk-chat)
    * [`ldk-nbt`](/ldk-nbt)
    * [`ldk-hikari`](/ldk-hikari)
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
        * [`ldk-bukkit-scheduler`](/ldk-bukkit/ldk-bukkit-scheduler)
        * [`ldk-bukkit-coroutines`](/ldk-bukkit/ldk-bukkit-coroutines)
        * [`ldk-bukkit-plugin`](/ldk-bukkit/ldk-bukkit-plugin)
        
</details>
        
# Binaries

## Jenkins CI

Many thanks to the `Jenkins CI` lifecycle provided by [@25](https://github.com/25).

* [Stable version](http://www.soulbound.me/job/ldk/) (master)
* [Development version](http://www.soulbound.me/job/ldk_Dev/) (develop)

## Dependencies

The `Maven` or `Gradle` dependency for the version tag can be found at https://jitpack.io/#lgou2w/ldk.

> The bukkit runtime of ldk-bukkit-plugin is distributed in [release](https://github.com/lgou2w/ldk/releases).

* Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
	
<dependencies>
    <dependency>
        <groupId>com.github.lgou2w.ldk</groupId>
        <artifactId>${module}</artifactId>
        <version>${version}</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

* Gradle
```gsp
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.lgou2w.ldk:${module}:${version}'
}
```

# Development

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

# License

```
Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)

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
