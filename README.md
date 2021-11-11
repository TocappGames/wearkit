WearKit - WearOS & Android GameDev
==================================
[WearKit](https://wearkit.dev) is a project to help developers to create
WearOS (and Android) games, leveraging [Dyn4J](https://dyn4j.org) java
library as a underlying physics engine.

## Installation (using gradle)
WearKit can be installed from the official
[WearKit Maven Repository](https://maven.wearkit.dev) (recommended),
or using [GitHub Packages](https://github.com/TocappGames/wearkit/packages).

### Add WearKit Maven repository to project

```gradle
// File: MyProject/build.gradle
    
allprojects {
    repositories {
        // ...
        maven {
            name = "WearKit"
            url = uri("https://maven.wearkit.dev")
        }
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
    implementation 'dev.wearkit:core:1.0.0-rc5' // mandatory
    implementation 'org.dyn4j:dyn4j:4.2.0' // recommended
}
```