(defproject bling-bling "0.1.0-SNAPSHOT"
  :description "A tiny UI library based on react.js and reagant"
  :url "https://github.com/ralf-o/bling-bling"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.107"]
                 [reagent "0.5.1-rc"]]
  :plugins [[lein-cljsbuild "1.1.0"]]
  ;:cljsbuild {:builds [{:source-paths ["src-cljs" "src-cljs/blingbling/core" "src-cljs/blingbling/demo/todomvc"]
                :cljsbuild {:builds [{:source-paths ["src", "demo"]
                            :compiler {:output-to "resources/demo/todomvc/build/todomvc-blingbling.js"
                                   ;;:source-map "app.js.map"
                                   :elide-asserts false
                                   :optimizations :whitespace
                                   :preamble ["reagent/react.js"]
                                   :pretty-print true}}]})
