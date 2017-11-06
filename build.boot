(set-env!
  :resource-paths #{"src" "resources"}
  :target-path "target"
  :dependencies '[[org.clojure/clojure       "1.9.0-beta2"]
                  [org.clojure/clojurescript "1.9.908"]
                  [adzerk/boot-cljs          "2.1.4"]
                  [adzerk/boot-cljs-repl     "0.3.3"]
                  [adzerk/boot-reload        "0.4.5"]
                  [com.cemerick/piggieback   "0.2.1"  :scope "test"]
                  [weasel                    "0.7.0"  :scope "test"]
                  [org.clojure/tools.nrepl   "0.2.12" :scope "test"]
                  [org.clojure/java.jdbc     "0.7.3"]
                  [org.clojure/tools.reader  "1.0.5"]
                  [com.oracle/ojdbc7         "12.1.0.1"]
                  [honeysql                  "0.9.1"]
                  [compojure                 "1.6.0"]
                  [hiccup                    "1.0.5"]
                  [hiccups                   "0.3.0"]
                  [com.cognitect/transit-clj "0.8.300"]
                  [pandeiro/boot-http        "0.8.3"]])

;(require '[panderio.boot-http :refer :all])
(require '[adzerk.boot-cljs            :refer [cljs]]
         '[adzerk.boot-cljs-repl       :refer [cljs-repl start-repl]]
         ;'[adzerk.boot-reload          :refer [reload]]
         ;'[crisptrutski.boot-cljs-test :refer [exit! test-cljs]]
         '[pandeiro.boot-http          :refer [serve]])

(task-options!
  aot {:all true}
  jar {:file "hilbert.jar"
       :main 'hilbert.service})

(deftask dev []
  (comp (serve :dir "target/")
        (watch)
        (notify)
        ;(reload :on-jsload 'hilbert.core/main)
        (cljs-repl)
        (cljs :source-map true :optimizations :none)))

(deftask build
  "Build hilbert locally as a JAR."
  []
  (comp (with-cp) (aot) (uber) (jar) (target)))

(deftask build-client
  "Build client-side runtime"
  []
  (cljs) (target))

(deftask run
  "Run the project."
  [a args ARG [str]]
  (require '[hilbert.service :as app])
  (apply (resolve 'app/-main) args))
