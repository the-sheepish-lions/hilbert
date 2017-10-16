(set-env!
  :resource-paths #{"src"}
  :target-path "target"
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [org.clojure/java.jdbc "0.7.3"]
                  [org.clojure/tools.reader "1.0.5"]
                  [compojure "1.6.0"]
                  [hiccup "1.0.5"]])

(task-options!
  aot {:all true}
  jar {:file "hilbert.jar"
       :main 'hilbert.service})

(deftask build
  "Build a hilbert jar file"
  []
  (comp (aot) (uber) (jar) (target)))
