import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


public class Tweeter {
	
	public enum TwitterAccount {
		BENDER, MARVIN, CALCULON, CLAMPS, ROBERTO, ROBOT_EBOOKS
	}

	private Twitter twitter;
	private TwitterAccount activeAccount;
	
	/**
	 * Constructs a Tweeter that will tweet to the given account.
	 * @param account The robot's TwitterAccount (i.e. TwitterAccount.MARVIN).
	 */
	public Tweeter(TwitterAccount account) {
		setActiveAccount(account);
	}
	
	/**
	 * You can use this method to change the account this Tweeter tweets to
	 * after constructing it.
	 * @param account The robot's TwitterAcount (i.e. MARVIN).
	 */
	public void setActiveAccount(TwitterAccount account) {
		// Don't do anything if this is already the active account.
		if (account == activeAccount) return;
		
		String consumerKey;
		String consumerSecret;
		String accessToken;
		String accessTokenSecret;
		switch (account) {
		case BENDER:
			consumerKey = "2joSTjEbkN1V7LOgoEey3w";
			consumerSecret = "Mp7aDJHctQOhGzsscguLJl2UvOF5QdaEnKgKEy0";
			accessToken = "1244852696-CpOXSTl2FRn7zoubPtfTHnWXYJ5LO807vEdhZJy";
			accessTokenSecret = "HaqRSnPN4b7BoNvkeX9ZU1IkCaulcE4GCFZF5nySus";
			break;
		case MARVIN:
			consumerKey = "mkMJbXaja0Qi2BJqG555iw";
			consumerSecret = "W9lJolkTimxXUtENpYymJNAez7Tw6DFqMf1K71XlX4";
			accessToken = "1244844690-SdBJz4lyo47C0k2gAN94wWihaskGCO6A9bJb5Xm";
			accessTokenSecret = "HPo0KtKgDIvNBuR2vHGjBIG1ShqOGaFxRvNpg9YF4";
			break;
		case CALCULON:
			consumerKey = "aiAqKeLXIsYOngINOy8g";
			consumerSecret = "Wnf2bRwo3JrViHaVvOpqY5TCxp0eZWB3GsDEK8TYR5o";
			accessToken = "1244839182-FUE7QlVi4fnBG0pIVU1yRVOKznKnugZ4s76p6f2";
			accessTokenSecret = "92X6OUAvbureRinH0sfV3hxjREiYxEsftUSd1QZYY4";
			break;
		case CLAMPS:
			consumerKey = "LXJh6f2VHO2ohJ1RLgJW1g";
			consumerSecret = "jOpNt4rfmVo0InowmR0o5dWQBeXNmO8HLd8wiaCesI";
			accessToken = "1244858930-Z3e9uUCy8CNJ8H3EJX24gP7rulcPDALPDGfNSda";
			accessTokenSecret = "3aSlH9xsQTedJFqkBYZpKFNc1Qe8xueJlD0D6qHIuI";
			break;
		case ROBERTO:
			consumerKey = "P1BjUBcgMrLGqAbhYuAuQ";
			consumerSecret = "kyhCbaQqRXbIJg21SV1GjDMioItNPLYhYxWtVy0";
			accessToken = "1368256272-FCdBZNeFfZQxFSsivSiV1lRVAXwpjsBWn39BX90";
			accessTokenSecret = "dGEe0Xqk88F5O56UHoXIlwwDxE1rwgYQdqiFUZXHYtc";
			break;
		case ROBOT_EBOOKS:
			consumerKey = "B9mwrhOkQ65oE50JrEuw";
			consumerSecret = "607fnxIrc0dpOAx0jfxk53ac0VaH03mHzIMcuOaCddI";
			accessToken = "1244870965-ExsbSasa6wxROZ5DgHFVJTy33GxG25hPBgDhIE";
			accessTokenSecret = "9mEtUX5c6NhC0ZSbxQFxKHduYnRJfS5zq36aCijN54";
			break;
		default:
			throw new IllegalArgumentException("Please pass a robot's name to the constructor.");
		}
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(consumerKey)
			.setOAuthConsumerSecret(consumerSecret)
			.setOAuthAccessToken(accessToken)
			.setOAuthAccessTokenSecret(accessTokenSecret);
		twitter = new TwitterFactory(cb.build()).getInstance();
		activeAccount = account;
	}

	/**
	 * @return the active account that will be tweeted to by default (i.e. "marvin").
	 */
	public TwitterAccount getActiveAccount() {
		return activeAccount;
	}
	
	/**
	 * Tweets to the active account.
	 * @param status The message to tweet.
	 * @throws TwitterException
	 */
	public void tweet(String status) throws TwitterException {
		twitter.updateStatus(status);
	}
	
	/**
	 * Tweets to the given account.
	 * @param status The message to tweet.
	 * @param account The account to tweet to.
	 * @throws TwitterException
	 */
	public void tweet(String status, TwitterAccount account) throws TwitterException {
		setActiveAccount(account);
		twitter.updateStatus(status);
	}

}
