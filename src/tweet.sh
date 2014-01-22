#!/bin/sh
cd /nishome/andrew/git/bwi-twitter-java/src
echo "Compiling libraries and source code..."
javac -target 1.6 -cp ".:../external-libraries/*:../external-libraries/Forecastio/*:../external-libraries/Jena/*:../external-libraries/Pellet/*:../external-libraries/twitter4j-core/*" *.java
echo "Generating tweet..."
java -cp ".:../external-libraries/*:../external-libraries/Forecastio/*:../external-libraries/Jena/*:../external-libraries/Pellet/*:../external-libraries/twitter4j-core/*" WeatherEmotion
echo "Tweet published."
