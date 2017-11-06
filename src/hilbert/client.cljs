(ns hilbert.client)

(defn main []
  (let [c (.. js/document (createElement "DIV"))]
    (aset c "innerHTML" "<p>This is a test</p>")
    (.. js/document (getElementById "main") (appendChild c))))

(main)
