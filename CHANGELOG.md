# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

## [1.0.0-beta7](https://github.com/TocappGames/wearkit/compare/v1.0.0-beta6...v1.0.0-beta7) (2020-04-21)


### Bug Fixes

* **performance:** fixed bitmap performance issues ([d0b18d9](https://github.com/TocappGames/wearkit/commit/d0b18d9)), closes [#30](https://github.com/TocappGames/wearkit/issues/30)
* **scaling:** fixed scaling bug ([b172e97](https://github.com/TocappGames/wearkit/commit/b172e97))



## [1.0.0-beta6](https://github.com/TocappGames/wearkit/compare/v1.0.0-beta5...v1.0.0-beta6) (2020-04-17)


### Features

* **engine:** added lifecycle callbacks to allow processing before and after world update and render ([7cef359](https://github.com/TocappGames/wearkit/commit/7cef359))



## [1.0.0-beta5](https://github.com/TocappGames/wearkit/compare/v1.0.0-beta4...v1.0.0-beta5) (2020-04-16)


### Bug Fixes

* **build:** compatiblity with java7 ([d01a589](https://github.com/TocappGames/wearkit/commit/d01a589))
* **compatiblity:** fixed compatibility with java 7 (removed java 8 code) ([06e25b8](https://github.com/TocappGames/wearkit/commit/06e25b8))
* **decoration:** reimplemented decoration management, now is easier to use ([518ebf9](https://github.com/TocappGames/wearkit/commit/518ebf9))
* **input:** fixed input ontouchevent function ([9ac92e8](https://github.com/TocappGames/wearkit/commit/9ac92e8))


### Features

* **camera:** implemented camera angle mode to allow follow a body and its angle ([4cbc2e7](https://github.com/TocappGames/wearkit/commit/4cbc2e7))
* **camera:** implemented camera to follow object and object angle ([da313c4](https://github.com/TocappGames/wearkit/commit/da313c4))
* **core:** added ability to scale bodies ([9c5493f](https://github.com/TocappGames/wearkit/commit/9c5493f))
* **data:** implemented unity loader to load the .meta exported from Unity Editor ([96d030e](https://github.com/TocappGames/wearkit/commit/96d030e))



## [1.0.0-beta4](https://github.com/TocappGames/wearkit/compare/v1.0.0-beta3...v1.0.0-beta4) (2020-04-13)


### Bug Fixes

* **camera:** fixes bug when body not starting in the middle of the world ([f904282](https://github.com/TocappGames/wearkit/commit/f904282))


### Features

* **render:** implemented text rendering in bodies and ornament ([1a3d57e](https://github.com/TocappGames/wearkit/commit/1a3d57e))



## [1.0.0-beta3](https://github.com/TocappGames/wearkit/compare/v1.0.0-beta2...v1.0.0-beta3) (2020-04-12)


### Bug Fixes

* fixed bug with body rendering if doesn't have fixtures (ornament with bitmap), fixes [#13](https://github.com/TocappGames/wearkit/issues/13) ([08ea564](https://github.com/TocappGames/wearkit/commit/08ea564))
* fixed gradle script to generate javadocs ([ef75ce9](https://github.com/TocappGames/wearkit/commit/ef75ce9))


### Features

* **sprite_loading:** improved body loading, fixes [#15](https://github.com/TocappGames/wearkit/issues/15), [#13](https://github.com/TocappGames/wearkit/issues/13) ([2cf1506](https://github.com/TocappGames/wearkit/commit/2cf1506))



## [1.0.0-beta2](https://github.com/TocappGames/wearkit/compare/v1.0.0-beta1...v1.0.0-beta2) (2020-04-11)


### Features

* **releasing:** added Pom.xml generator ([f892668](https://github.com/TocappGames/wearkit/commit/f892668))



## [1.0.0-beta1](https://github.com/TocappGames/wearkit/compare/v1.0.0-alpha0...v1.0.0-beta1) (2020-04-11)


### Bug Fixes

* changed colors to improve contrast ([2a3fa9a](https://github.com/TocappGames/wearkit/commit/2a3fa9a))
* moved assets to example module ([eb773e5](https://github.com/TocappGames/wearkit/commit/eb773e5))
* **engine:** fixes [#12](https://github.com/TocappGames/wearkit/issues/12) ([21e31b8](https://github.com/TocappGames/wearkit/commit/21e31b8))
* **naming:** renamed old packages ([0ab9ec3](https://github.com/TocappGames/wearkit/commit/0ab9ec3))
* **refactor:** removed unused zIndex in shapes (it is used only in bodies/ornament) ([4a5f7cb](https://github.com/TocappGames/wearkit/commit/4a5f7cb))


### Features

* **camera:** implemented camera setings/viewport, fixes [#11](https://github.com/TocappGames/wearkit/issues/11) ([b5d2319](https://github.com/TocappGames/wearkit/commit/b5d2319))
* **wear:** implemented wear activity, fixes [#8](https://github.com/TocappGames/wearkit/issues/8) ([5fa0613](https://github.com/TocappGames/wearkit/commit/5fa0613))
* added menu to launch different examples ([4efc823](https://github.com/TocappGames/wearkit/commit/4efc823))
* **polygons:** added test to load shapes from exported files (json), needs refactor ([1eb2b41](https://github.com/TocappGames/wearkit/commit/1eb2b41))
* **refactor:** renamed thing to ornament, and added World.addOrnament() method to ease understanding ([1bcfb8b](https://github.com/TocappGames/wearkit/commit/1bcfb8b))
* **sprite_loading:** implemented sprite rendering and loading assets ([2dc77d7](https://github.com/TocappGames/wearkit/commit/2dc77d7))



### 1.0.0-alpha0 (2020-03-30)


### Bug Fixes

* **coordinate_system:** fixes [#1](https://github.com/TocappGames/wearkit/issues/1) ([e428dd0](https://github.com/TocappGames/wearkit/commit/e428dd0))
