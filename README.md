Keter
=====

Keter is a [roguelike](http://en.wikipedia.org/wiki/Roguelike) game based on the
[SCP Foundation](http://www.scp-wiki.net/) universe.

It uses the [rot.js](http://ondras.github.io/rot.js/) library and is written in [Scala](http://www.scala-lang.org/)
language using the [Scala.js](http://www.scala-js.org/) compiler.

Running the game
----------------

Install the actual version of [`sbt` tool](http://www.scala-sbt.org/). Run the following from the terminal:

    $ sbt fastOptJS
    
Then open the file `src/main/html/index.html` from its original location on your local drive. 