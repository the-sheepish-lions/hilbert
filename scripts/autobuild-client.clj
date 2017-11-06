(ns hilbert.client-build
  (:require [cljs.build.api :as cljs]))

(cljs/watch "src"
  {:main 'hilbert.client
   :asset-path "/js/out"
   :output-dir "resources/public/js/out"
   :output-to "resources/public/js/main.js"})
