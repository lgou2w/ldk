- [English](README.md)
- **简体中文**

<p align="center">
<img src=".images/ldk-logo.png" alt="ldk" />
</p>

<p align="center">
<a href="https://java.com"><img src="https://img.shields.io/badge/Java-8-blue.svg?style=flat-square&logo=java"></a>
<a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-1.3.41-blue.svg?style=flat-square&logo=kotlin"></a>
<a href="https://travis-ci.org/lgou2w/ldk"><img src="https://img.shields.io/travis/lgou2w/ldk/develop.svg?style=flat-square&logo=travis" /></a>
<a href="https://search.maven.org/search?q=g:com.lgou2w%20AND%20a:ldk*"><img src="https://img.shields.io/maven-central/v/com.lgou2w/ldk.svg?style=flat-square&color=%231081c2&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAAOwgAADsIBFShKgAAAABh0RVh0U29mdHdhcmUAcGFpbnQubmV0IDQuMS41ZEdYUgAAAgNJREFUOE9jQAf29vtZMp0/mGa5vd2d6ftmW3LEO736+v9MUGnsIMvynXy23ds52S5vv2Z4v/2ZFvTmZ0rUm08JSW+mpOS+kYEqQ4AsrZc8BWZvS3Kt377Odn7zL9Pr7el0//cOSeEv3RITXl+OT3/9Lzbv9cuY8tfZ8fX/OcCask2fCOcbvz2XZ/XmX47j22eZbm9ycz3+sYMlgSAm5jl3TM7rsuiSNy8jq1//DW96dSR+PlBznvE7uTyLt59y7d+ezHZ5LQVVjwFiy9/JhTe+vhDa+fpdaMddfoY863dyOXZvPwH9tRqqBisIXfWfObTj9dbgPphGl3dy2c5vP2V6EtYI1LQ1cPLrdy4gjWlAjUBNn9L8CWsEatoaMPPVO5eZII0+7+Qy/N9+Sg15TVBjwMyXW/3mwzSGvpNLC337KTn6zbmkpGfyUHUYIGDGS2W/ea+u+CyBakwO+SCUHPXmaFLi6z8JGa/fxxS8Lomuf8MHVc8Q0vtByH/aq1r/Oa8++ix6+dtr5avd9vuhcZmW9pQrLvVVSmzemzvRpa//Rta+vhza+iospO9FNNBft/xnvvznu+DlTa9lr2M8tt2CxzEcxFZ8FI6qetka1vTqY0jnqz+BE1798Z/x6g3QiTXeSx8KQpXhAv8ZQzpfqwODfkfg1Neb/ee/VwCJQSWhgIEBAOwJCv4iGYRqAAAAAElFTkSuQmCC" /></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat-square&logo=apache" /></a>
<br />
<a href="https://codecov.io/gh/lgou2w/ldk/branch/develop"><img src="https://img.shields.io/codecov/c/github/lgou2w/ldk/develop.svg?style=flat-square&label=codecov&logo=codecov" /></a>
<a href="https://github.com/lgou2w/ldk"><img src="https://img.shields.io/github/repo-size/lgou2w/ldk.svg?style=flat-square&label=repo&logo=codesandbox" /></a>
<a href="https://github.com/lgou2w/ldk/commits"><img src="https://img.shields.io/github/last-commit/lgou2w/ldk/develop.svg?style=flat-square&label=commit&logo=git" /></a>
<a href="https://github.com/lgou2w/ldk/pulls"><img src="https://img.shields.io/badge/contributing-welcome-FF69B4.svg?style=flat-square&logo=github" /></a>
<a href="https://github.com/lgou2w/ldk/issues"><img src="https://img.shields.io/badge/issues-report-E74C3C.svg?style=flat-square&logo=github"></a>
</p>

# 介绍

* 一个多平台的 lgou2w 开发套件.

多模块，多平台，低耦合，依赖库，Minecraft 的聊天和 NBT 库，协程，工具，实用程序，包装，
JavaFx，Rxjava，Retrofit，ASM，Bukkit 的协程、扩展和运行时等。快速开发，Lambda，函数编程。 
集成的多模块多平台 lgou2w 开发套件。`LDK`

* 对于 Minecraft 服务器的兼容性.

    <p>
    <a href="https://minecraft.net"><img src="https://img.shields.io/badge/Minecraft-Bukkit%20|%20Spigot%20|%20Paper%20%3E%3D%201.8-brightgreen.svg?style=flat-square&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAACXBIWXMAABJ0AAASdAHeZh94AAAAGHRFWHRTb2Z0d2FyZQBwYWludC5uZXQgNC4xLjVkR1hSAAAChklEQVQ4T22RXUxSYRzGT5ldedGdrss2CwUUAUFQz0G+FMya5gmWHwdQkADZ2qym0Y66/E6pZSRqH2o4s1rlXV7VauuDD5W66MKt1bpta+siurCnc4At13y2d++79//+9nu2l/g/4Zgrd2mjS/Pog/9tJOl9OL/lPsay7P7seO9Ekj5BKMqszMTsv+bjDixvebCS7P6+vOkffZbszc8++5cnn9lDs3Hn4Fy848e9DSd4aGnThduJToTe28Dd/1lNdn+9k3Ay7Ef6YBoaW7cennpz5tP1dy0Ix22YjrYhHLNjcdONuwln+nwzymA2bsdComtn8LFljV6lc4jeSJPgwlJ9auB5MxbibszEeIMjbeMqI7J1Fve5NbHeCmu/FuVNxdsymSyXYBmj4HynIeUd1cA3Z8DwSwtCURtXtwNcNYRe29E6roXeVwW1UQylpDADhn3HBVfaa1IBOwl3mwKBp43oWWtA8FU7Ag9oUA4pynSFkJJHoZIKYagQb7t4cIihBGMOXSrob0bPqQoOVsLbTcEeJKE6LYG6TgSF8AgoaREP8XvGeNVlFAy0UKkRmxaXLFUYtevBttbAc7IcpEIMvVoKjbwIOoUQRlUJdCppBuSN/RwY8pkx3qHHEFODwTYNRhx6ziBCrVqCytKjqKuUgCwT4AQlz1Sd9pgLhhldfITR7kx1mTDprkfQZUSflYRGVgQTB/AQJRfx5t8auXCZpomc9F+usp68CafRd9lm+jLu0KGPqxxg6qAtL4ZeKYJOKd6pr5YmDJUlDRRFHUhDu3PNb82fdNVODdu0P294zWmjQSn+piOrPDRF5WWf7R0QxL5b5xpLL1rIF6ZqySJtpgqyo10hiL83Y0UXYFGT7wAAAABJRU5ErkJggg=="></a>
    <a href="https://bstats.org/plugin/bukkit/LDK"><img src="https://img.shields.io/badge/bStats-deployed-3366FF.svg?style=flat-square&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAJLSURBVDhPLVI7bxNBEP52z/eIz28c23GCCRBwJASIBiHRkgoKJAoaCoSgh4qaCgkhOqhoEA0/gw6EoEBCckFA4ARkbPls353vfbfMnrPSaGdnZ+abmW/Yxe2yEADSDOBg4IwhylJkZE2TlH4AxjkKnP7ozp3pcHnnQpaMFBnA5Qcl0Ap8JQrP39LnKA7sXI8QyZBIyKOjqiuEiiZTMGSUahkm8KKUUFc+rN8timatgqsXdqHkVg5d03Odp5GMg2AcnufD9zx8/jGE5bpgZ7trot/bwsPbN6AqKvkxGIYJxhT4S5uCKJYCA9J9x8ab9x/xczQBe/bgpjANHb32MVnAqkdFz4dgOwvqUUGrXoc1HWNmTTB1PQQ0NL6zuYGtVgt6sQI/FpjMHCorQBRFiJIEfhhjarsICVozS2g3aug165SccFonTmPv0VN88Uu4/+ItDg5HqNEkT220YTk+bj15hWG1jzvP3+H4+SvQSw3wJCRol+ofH6JM5Gw3a8RpggUNYOG4ub7TqcPIfCxHv7C05/B9D+z1vT2h6ToajXWoWhlcNfB1f4A5JZOkNaolXN49A8uSPY7xd+4iiBOwl3eviYKioEQDmgUCi0igXSsSJbIJICQni3rUWAydxHJDxAkxG9G6BHEMh8a9/+cAnwYDmEUdJzc7uZhrBj58+47f/yzEGXEDJaeKPb5+Kd8iJjJYfoJ5kGC9WoauFqQ5R5wsHHRrJjpUiRdGSGnLuBy3R+KSxJIf2snJ3MZwbOUyJl3iyOwp7XxGFQoh8B/lvSHNL742VAAAAABJRU5ErkJggg=="></a>
    </p>

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
        * [`ldk-bukkit-anvil`](/ldk-bukkit/ldk-bukkit-anvil)
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

# 版本

## 0.x

- `0.1.8-beta1` - 0.1.8 版本的第一个测试版.
- `0.1.8-rc2` - 0.1.8 版本的第二个发布候选版.
- `0.1.8-rc2-hotfix1` - 0.1.8 版本的第二个发布候选版热修1.
- `0.1.8-rc2-SNAPSHOT` - 0.1.8 快照版本的第二个发布候选版本.
- `0.1.8-SNAPSHOT` - 0.1.8 快照版本.
- `0.1.8` - 0.1.8 正式版.

## 1.x Or higher

- `1.0` - 1.0 正式版.
- `1.1-SNAPSHOT` - 1.1 快照版本.

# 构建

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
* [nihuge](https://github.com/nihuge)

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
