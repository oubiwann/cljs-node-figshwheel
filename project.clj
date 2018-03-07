(defn get-banner
  []
  (try
    (str
      (slurp "dev-resources/text/banner.txt")
      (slurp "dev-resources/text/loading.txt"))
    ;; If another project can't find the banner, just skip it;
    ;; this function is really only meant to be used by Dragon itself.
    (catch Exception _ "")))

(defn get-prompt
  [ns]
  (str "\u001B[35m[\u001B[34m"
       ns
       "\u001B[35m]\u001B[33m λ\u001B[m=> "))

(defproject cljs-node-figwheel "0.1.0-SNAPSHOT"
  :description "Example project for using Node.js, Figwheel, and a combination of both"
  :url "http://github.com/oubiwann/cljs-node-figwheel"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :min-lein-version "2.7.1"
  :exclusions [
    [org.clojure/clojure]]
  :dependencies [
    [org.clojure/clojure "1.9.0"]
    [org.clojure/clojurescript "1.10.126"]
    [org.clojure/core.async  "0.4.474"]]
  :plugins [
    [lein-figwheel "0.5.15"]
    [lein-cljsbuild "1.1.7"]]
  :source-paths ["src"]
  :clean-targets
    ^{:protect false} ["resources/public/js" :target]
  :cljsbuild {
    :builds [
     {:id "dev"
      :source-paths ["src"]
      ;; The presence of a :figwheel configuration here
      ;; will cause figwheel to inject the figwheel client
      ;; into your build
      :figwheel {
        :on-jsload "cnf.core/on-js-reload"
        ;; :open-urls will pop open your application
        ;; in the default browser once Figwheel has
        ;; started and compiled your application.
        ;; Comment this out once it no longer serves you.
        :open-urls ["http://localhost:3449/index.html"]}
      :compiler {
        :main cnf.core
        :asset-path "js/compiled/out"
        :output-to "target/node/cnf.js"
        :output-dir "target/node/out"
        :target :nodejs
        :source-map-timestamp true
        ;; To console.log CLJS data-structures make sure you enable devtools in Chrome
        ;; https://github.com/binaryage/cljs-devtools
        :preloads [devtools.preload]}}
      ;; This next build is a compressed minified build for
      ;; production. You can build this with:
      ;; lein cljsbuild once min
      {:id "min"
       :source-paths ["src"]
       :compiler {
         :output-to "resources/public/js/compiled/cnf.js"
         :main cnf.core
         :optimizations :advanced
         :pretty-print false}}]}
  :figwheel {
    ;; :http-server-root "public" ;; default and assumes "resources"
    ;; :server-port 3449 ;; default
    ;; :server-ip "127.0.0.1"
    :css-dirs ["resources/public/css"] ;; watch and update CSS

    ;; Start an nREPL server into the running figwheel process
    ;; :nrepl-port 7888

    ;; Server Ring Handler (optional)
    ;; if you want to embed a ring handler into the figwheel http-kit
    ;; server, this is for simple ring servers, if this

    ;; doesn't work for you just run your own server :) (see lein-ring)

    ;; :ring-handler hello_world.server/handler

    ;; To be able to open files in your editor from the heads up display
    ;; you will need to put a script on your path.
    ;; that script will have to take a file path and a line number
    ;; ie. in  ~/bin/myfile-opener
    ;; #! /bin/sh
    ;; emacsclient -n +$2 $1
    ;;
    ;; :open-file-command "myfile-opener"

    ;; if you are using emacsclient you can just use
    ;; :open-file-command "emacsclient"

    ;; if you want to disable the REPL
    ;; :repl false

    ;; to configure a different figwheel logfile path
    ;; :server-logfile "tmp/logs/figwheel-logfile.log"

    ;; to pipe all the output to the repl
    :server-logfile false
    }

  ;; Setting up nREPL for Figwheel and ClojureScript dev
  ;; Please see:
  ;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl
  :profiles {
    :dev {
      :dependencies [
        [binaryage/devtools "0.9.9"]
        [figwheel-sidecar "0.5.15"]
        [com.cemerick/piggieback "0.2.2"]]
      ;; need to add dev source path here to get user.clj loaded
      :source-paths ["src" "dev-resources/src"]
      ;; for CIDER
      ;; :plugins [[cider/cider-nrepl "0.12.0"]]
      :repl-options {
        :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
        :init-ns cnf.dev
        :prompt ~get-prompt
        :init ~(println (get-banner))}
      ;; need to add the compliled assets to the :clean-targets
      :clean-targets
        ^{:protect false} ["resources/public/js/compiled"
        :target-path]}
    :lint {
      :source-paths ^:replace ["src"]
      :test-paths ^:replace []
      :plugins [
        [jonase/eastwood "0.2.5"]
        [lein-ancient "0.6.15"]
        [lein-bikeshed "0.5.1"]
        [lein-kibit "0.1.6"]
        [venantius/yagni "0.1.4"]]}}
  :aliases {
    ;; Linting aliases
    "kibit" ["do"
      ["with-profile" "lint" "shell" "echo" "== Kibit =="]
      ["with-profile" "lint" "kibit"]]
    "eastwood"
      ["with-profile" "lint" "eastwood" "{:namespaces [:source-paths]}"]
    "bikeshed"
      ["with-profile" "lint" "bikeshed" "--max-line-length=100"]
    "yagni"
      ["with-profile" "lint" "yagni"]
    "check-vers"
      ["with-profile" "lint" "ancient" ":all"]
    "lint" ["do"
      ["check"]
      ["kibit"]
      ["eastwood"]]
    ;; Aliases for working with Node.js; for readline support, call the
    ;; following as `rlwrap lein node-repl`
    "node-repl"
      ^{:doc "Start a Node.js-based Clojurescript REPL"}
      ["trampoline" "run" "-m" "clojure.main"
      "dev-resources/scripts/node-repl.clj"]})
