MY_ORACLE_HOME=$(HOME)/Oracle/Middleware/Oracle_Home
#MY_JAVA_HOME=$(MY_ORACLE_HOME)/oracle_common_jdk
FORM2XML=$(MY_ORACLE_HOME)/forms/templates/scripts/frmf2xml.sh

CLJ=java -cp resources/cljs.jar:src clojure.main

target:
	boot cljs target

resources/public/js/main.js:
	$(CLJ) scripts/build-client.clj

.PHONY: dev-client
dev-client:
	$(CLJ) scripts/autobuild-client.clj

.PHONY: clean
clean:
	rm -f resources/public/js/main.js
	rm -rf resources/public/js/out
