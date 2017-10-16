MY_ORACLE_HOME=$(HOME)/Oracle/Middleware/Oracle_Home
#MY_JAVA_HOME=$(MY_ORACLE_HOME)/oracle_common_jdk
FORM2XML=$(MY_ORACLE_HOME)/forms/templates/scripts/frmf2xml.sh

all: dist/hilbert.js

dist/hilbert.js:
	cat src/core.js src/runtime.js > dist/hilbert.js
