bwi-twitter-java
================

Java interface for the UT Austin Building-Wide Intelligence Twitter functionality.

To generate and publish a tweet, change the first line of /src/tweet.sh to cd to
the /src directory, then run the script with
	./src/tweet.sh

To tweet periodically, update a cron job with
	crontab -e

To see logs from the cron job, look in tweet.log.
