- [English](README.md)
- **简体中文**

<p align="center">
<img src="images/ldk-logo.jpg" alt="ldk" />
</p>

<p align="center">
<a href="https://travis-ci.org/lgou2w/ldk"><img src="https://travis-ci.org/lgou2w/ldk.svg?branch=develop" /></a>
<a href="https://codebeat.co/projects/github-com-lgou2w-ldk-develop"><img alt="codebeat badge" src="https://codebeat.co/badges/7c8fccc7-6096-4f12-81e3-98b1f39a3875" /></a>
<a href="https://jitpack.io/#lgou2w/ldk"><img src="https://jitpack.io/v/lgou2w/ldk.svg" /></a>
<a href="https://github.com/lgou2w/ldk"><img src="https://badges.frapsoft.com/os/v1/open-source.svg?v=102" alt="OpenSource" /></a>
</p>

# 介绍
* 一个多平台的 lgou2w 开发套件.

# 模块

* ldk
    * ldk-asm
    * ldk-common
    * ldk-i18n
    * ldk-reflect
    * ldk-retrofit
    * ldk-rx
    * ldk-fx
    * ldk-chat
    * ldk-nbt
    * ldk-bukkit
        * ldk-bukkit-common
        * ldk-bukkit-i18n
        * ldk-bukkit-scheduler
        * ldk-bukkit-plugin

        
# 构建

可以在 https://jitpack.io/#lgou2w/ldk 找到版本标记的 `Maven` 或 `Gradle` 依赖项.

> 模块 ldk-bukkit-plugin 的 Bukkit 运行时分发在 [releases](https://github.com/lgou2w/ldk/releases) 中.

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

# 开发

* Intellij IDEA
* JDK 8
* Kotlin 1.2+
* Maven 3.3+
* Git

```bash
git clone https://github.com/lgou2w/ldk.git
cd ldk
mvn clean install
```

# 协议

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
