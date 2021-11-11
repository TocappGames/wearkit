WearKit - WearOS & Android GameDev
==================================
[WearKit](https://wearkit.dev) is a project to help developers to create  
WearOS (and Android) games, leveraging [Dyn4J](https://dyn4j.org) java  
library as a underlying physics engine.

## Installation (using gradle)
WearKit can be installed from the official
[WearKit Maven Repository](https://maven.wearkit.dev) (recommended),
or using [GitHub Packages](https://github.com/TocappGames/wearkit/packages).

- Add WearKit Maven repository to project
    ```gradle
    // File: MyProject/build.gradle
        
    allprojects {
        repositories {
            google()
            mavenCentral()
            maven {
                name = "WearKit"
                url = uri("https://maven.wearkit.dev")
            }
        }
    }
    ```
- Add dependency to module
    ```gradle
    // File: MyProject/MyModule/build.gradle
    dependencies {
        implementation 'dev.wearkit:core:1.0.0-rc2'
    }
    ```
## Usage
to be completed...