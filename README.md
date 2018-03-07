# cljs-node-figwheel

Quick example setup for running Figwheel with a node project,
a node/cljs REPL just by itself, or various admixures.


## Just Node.js

To do auto-reload dev in a non-browser Node.js/ClojureScript REPL,
in one terminal run the following:

```
$ lein cljsbuild auto
```

Then, in a separate terminal:

```
$ rlwrap lein node-repl
```


## Figwheel and Node.js

Note: this will start up a web browser.

To get an interactive development environment run:

```
$ lein figwheel
```

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

```
$ lein clean
```

To create a production build run:

```
$ lein do clean, cljsbuild once min
```

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.


## License

Copyright © 2018, Duncan McGreggor
Copyright © 2016-2018, Clojure-Aided Enrichment Center

Apache License, Version 2.0
