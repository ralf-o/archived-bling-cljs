(defproject bling-bling "0.1.0-SNAPSHOT"
  :description "A tiny UI library based on react.js and reagant"
  :url "https://github.com/ralf-o/bling-bling"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0-alpha2"]
                 [org.clojure/clojurescript "1.7.122"]
                 [reagent "0.5.1"]]

  :plugins [[lein-cljsbuild "1.1.0"]]
  :cljsbuild {:builds [{:source-paths ["src", "demo"]
                        :compiler     {:output-to     "target/todomvc-blingbling.js"
                                       :source-map    "target/todomvc-blingbling.js.map"
                                       :elide-asserts false
                                       :optimizations :whitespace
                                       :pretty-print  true}}]})
