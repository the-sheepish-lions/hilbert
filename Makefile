.PHONY: deps autobuild-client build-client repl server clean

MY_ORACLE_HOME=$(HOME)/Oracle/Middleware/Oracle_Home
#MY_JAVA_HOME=$(MY_ORACLE_HOME)/oracle_common_jdk
FORM2XML=$(MY_ORACLE_HOME)/forms/templates/scripts/frmf2xml.sh

CP=$(shell scripts/lein classpath)
CLJ=java -cp 'resources/cljs.jar:$(CP):src'

deps:
	scripts/lein deps

resources/public/js/main.js:
	$(CLJ) clojure.main scripts/build-client.clj

autobuild-client:
	$(CLJ) clojure.main scripts/autobuild-client.clj

build-client: resources/public/js/main.js

repl:
	$(CLJ) clojure.main

client-repl:
	$(CLJ) clojure.main scripts/client-repl.clj

server:
	$(CLJ) clojure.main -m hilbert.service

clean:
	rm -f resources/public/js/main.js
	rm -rf resources/public/js/out
