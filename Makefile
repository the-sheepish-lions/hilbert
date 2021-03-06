.PHONY: deps autobuild-client build-client repl server clean help install-ojdbc setup

MY_ORACLE_HOME=$(HOME)/Oracle/Middleware/Oracle_Home
MY_JAVA_HOME=$(MY_ORACLE_HOME)/oracle_common_jdk
FORM2XML=$(MY_ORACLE_HOME)/forms/templates/scripts/frmf2xml.sh

CP=$(shell scripts/lein classpath)
CLJ=java -cp 'resources/cljs.jar:$(CP):src'

deps:
	scripts/lein deps

install-ojdbc:
	mvn install:install-file -Dfile=resources/ojdbc7.jar -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0.1 -Dpackaging=jar

setup: install-ojdbc deps

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

help:
	@echo -==================== Hilbert ========================-
	@echo "setup            - setup developent environment"
	@echo "deps             - download project dependecies from Maven"
	@echo "install-ojdbc    - install Oracle JDBC driver"
	@echo "autobuild-client - start autobuild daemon"
	@echo "build-client     - build client"
	@echo "repl             - start server-side REPL"
	@echo "client-repl      - start client-side REPL"
	@echo "server           - start hilbert service"
	@echo "clean            - clean up client side build"
	@echo "help             - display this message"
