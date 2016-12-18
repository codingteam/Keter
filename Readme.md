Keter [![Build Status][badge-travis]][travis]
=====

Keter is a [roguelike][] game based on the [SCP Foundation][scp] universe. It
works in browser.

![Game screenshot][screenshot]

How to play
-----------

The currently released version is published online at [keter.fornever.me][].
[Game design documents](#game-design-documents) section could provide useful
information about the game.

If you want to build the game yourself, please read the following sections.

How to build
------------

The game is written in [Scala][scala] programming language and uses [sbt][] for
build process management. Build prerequisites:

- [sbt][]
- [Node.js][node-js] 6.9.1 or newer

To build the application, execute the following command in your terminal:

```console
$ sbt site
```

After that, open `target/site/index.html` file with your Web browser.

You may publish the `target/site` directory if you want to provide game access
to the other players.

How to run the unit tests
-------------------------

It's considered useful to continuously run the unit tests while changing the
game files. That could be achieved using the following command:

```console
$ sbt ~;test;site
```

Game design documents
---------------------

- [Game description][gdd-game-description]
- [General concepts][gdd-general-concepts]

### UI mockups

- [Inventory screen][mockup-inventory-screen]
- [Equipment screen][mockup-equipment-screen]

License
-------

Keter is licensed under the terms of MIT License. See [`License.md`][license]
file for details.

[gdd-game-description]: docs/SCP%20Roguelike
[gdd-general-concepts]: docs/General%20concepts
[license]: License.md
[mockup-equipment-screen]: docs/Equipment%20screen.svg
[mockup-inventory-screen]: docs/Inventory%20screen.svg
[screenshot]: docs/screenshot.png

[badge-travis]: https://travis-ci.org/codingteam/Keter.svg?branch=develop

[keter.fornever.me]: http://keter.fornever.me
[node-js]: https://nodejs.org/
[sbt]: http://www.scala-sbt.org/
[scala]: http://www.scala-lang.org/
[roguelike]: http://en.wikipedia.org/wiki/Roguelike
[scp]: http://www.scp-wiki.net/
[travis]: https://travis-ci.org/codingteam/Keter
