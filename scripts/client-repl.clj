(require 'cljs.repl)
(require 'cljs.build.api)
(require 'cljs.repl.browser)

(cljs.build.api/build "src"
  {:main 'hilbert.client
   :asset-path "/js/out"
   :output-to "resources/public/js/main.js"
   :output-dir "resources/public/js/out"
   :browser-repl true
   :verbose true})

(cljs.repl/repl (cljs.repl.browser/repl-env)
  :watch "src"
  :output-dir "resources/public/js/out")
