(set-env!
  :resource-paths #{"src" "resources"}
  :target-path "target"
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [org.clojure/java.jdbc "0.7.3"]
                  [org.clojure/tools.reader "1.0.5"]
                  [com.oracle/ojdbc7 "12.1.0.1"]
                  [compojure "1.6.0"]
                  [hiccup "1.0.5"]])

(task-options!
  with-cp {:file "resources/ojdbc7.jar"}
  aot {:all true}
  jar {:file "hilbert.jar"
       :main 'hilbert.service})

(deftask build
  "Build hilbert locally as a JAR."
  []
  (comp (with-cp) (aot) (uber) (jar) (target)))

(deftask run
  "Run the project."
  [a args ARG [str]]
  (require '[hilbert.compiler :as app])
  (apply (resolve 'app/-main) args))

(deftask project-repl
  "Load repl with custom classpath."
  []
  (comp (with-cp) (repl)))
