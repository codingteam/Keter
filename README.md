Keter
=====

Keter is a [roguelike](http://en.wikipedia.org/wiki/Roguelike) game based on the
[SCP Foundation](http://www.scp-wiki.net/) universe.

It uses the [rot.js](http://ondras.github.io/rot.js/) library and is written in [Scala](http://www.scala-lang.org/)
language using the [Scala.js](http://www.scala-js.org/) compiler.

Running the game
----------------

Install the actual version of the [`sbt` tool](http://www.scala-sbt.org/). Run the following from the terminal:

    $ sbt site
    
Then open the file `target/site/index.html` with your Web browser.

You may publish the `site` directory through Web server if you wish to provide the game for the other players.