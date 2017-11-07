MY_ORACLE_HOME=$(HOME)/Oracle/Middleware/Oracle_Home
#MY_JAVA_HOME=$(MY_ORACLE_HOME)/oracle_common_jdk
FORM2XML=$(MY_ORACLE_HOME)/forms/templates/scripts/frmf2xml.sh

CP=$(shell lein classpath)
CLJ=java -cp 'resources/cljs.jar:$(CP):src'

target:
	boot cljs target

deps:
	lein deps

resources/public/js/main.js:
	$(CLJ) clojure.main scripts/build-client.clj

.PHONY: dev-client
dev-client:
	$(CLJ) clojure.main scripts/autobuild-client.clj

repl:
	$(CLJ) clojure.main

server:
	$(CLJ) clojure.main -m hilbert.service

.PHONY: clean
clean:
	rm -f resources/public/js/main.js
	rm -rf resources/public/js/out
