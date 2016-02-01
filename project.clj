(defproject bling-cljs "0.1.0-SNAPSHOT"
  :description "A tiny UI library based on react.js and reagant"
  :url "https://github.com/ralf-o/bling-bling"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [reagent "0.6.0-alpha"]]

  :plugins [[lein-cljsbuild "1.1.2"]]
  :cljsbuild {:builds [{:source-paths ["src", "demo"]
                        :compiler     {:output-to     "target/todomvc-bling.js"
                                       ;;:source-map    "target/todomvc-bling.js.map"
                                       :elide-asserts false
                                       :optimizations :whitespace
                                       :pretty-print  true}}]})
