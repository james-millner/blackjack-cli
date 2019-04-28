PACKAGE  = blackjack-cli

default: build

build: clean package run

clean:
	mvn clean

package:
	mvn package

run:
	java -jar target/blackjack-app-1.0-jar-with-dependencies.jar
