WearKit - WearOS 2D GameDev
=====================================
[WearKit](https://wearkit.dev) is a project to help developers to create
WearOS (and Android) 2D games, leveraging [Dyn4J](https://dyn4j.org) java
library as a underlying physics engine.

## Installation (using gradle)
WearKit can be installed from the official
[WearKit Maven Repository](https://maven.wearkit.dev) (recommended),
or using [GitHub Packages](https://github.com/TocappGames/wearkit/packages).

### Add WearKit Maven repository to project
If you configured gradle repositories in settings, add it to `gradle.settings`

```gradle
// File: MyProject/gradle.settings

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url "https://maven.tocappgames.com"
        }
    }
}
// ...
```

Otherwise add it to `build.gradle`
```gradle
// File: MyProject/build.gradle

// ...
repositories {
    // ...
    mavenCentral()
    maven {
        url "https://maven.tocpgamess.com"
    }
}
```

### Add WearKit and Dyn4J dependency to module
Dyn4J isn't necessary by default, but if you want to interact with the
physics engine or use the Dyn4J data structures, you need Dyn4J too.

```gradle
// File: MyProject/MyModule/build.gradle

dependencies {
    // ...
    implementation 'dev.wearkit:core:1.1.2' // mandatory
    implementation 'org.dyn4j:dyn4j:4.2.0' // recommended
}
```

## Usage
See some game examples in [example module](https://github.com/TocappGames/wearkit/tree/main/example/src/main/java/dev/wearkit/example)