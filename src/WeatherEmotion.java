import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import twitter4j.TwitterException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;

public class WeatherEmotion {
	private static final boolean TWEETING = true;
	private static final String TWITTER_ACCOUNT = "robot_ebooks";
	
	static final String inputFileName  = "weather.owl";
	static final String modelNameSpace = "http://www.semanticweb.org/tony/ontologies/2013/11/weather_emotion#";

	public static void main(String[] args) throws IOException, InterruptedException {
		// Initialize the model and currentWeather.
		OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		try (final FileInputStream in = new FileInputStream(inputFileName)) {
            model.read(in, null, "RDF/XML");
        }
        final Individual weather = model.createIndividual(
        		modelNameSpace + "currentWeather", OWL.Thing);
        Weather currentWeather = new Weather(model, modelNameSpace, weather);
        
        // Update currentWeather with weather data.
        currentWeather.addProperty("hasTemperature", currentWeather.getTemperature());
        currentWeather.addProperty("hasPrecipitationChance", currentWeather.getPrecipationChance());
        currentWeather.addProperty("hasPrecipitationType", currentWeather.getPrecipitationType());
        currentWeather.addProperty("hasWindSpeed", currentWeather.getWindSpeed());
        currentWeather.addProperty("hasCloudCoverage", currentWeather.getCloudCoverage());
		
        System.out.print("Weather condtions:");
        List<String> currentWeatherConditions = currentWeather.listConditions();
        for(final Iterator<String> i = currentWeatherConditions.iterator(); i.hasNext(); ) {
        	System.out.print(" " + i.next());
        }
        System.out.println();
        
        // Instantiate new model without a pellet reasoner.
        model = ModelFactory.createOntologyModel();
        InputStream in = FileManager.get().open( inputFileName );
		if (in == null) {
		    throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}
		model.read(in, null);
		// Print out sentences based on happy and sad emotions.
		OntClass emotionClass = model.getOntClass(modelNameSpace+"Emotion");
		ExtendedIterator<OntClass> emotionSubClasses = emotionClass.listSubClasses();
		ArrayList<String> emotions = new ArrayList<String>();
		while(emotionSubClasses.hasNext()) {
			String emotion = emotionSubClasses.next().getLocalName().toLowerCase();
			emotions.add(emotion);
			System.out.println(generateWeatherTweet(emotion, model, currentWeatherConditions));
		}
		String randomEmotion = emotions.get(new Random().nextInt(emotions.size()));
	    if(TWEETING) {
		    Tweeter tweeter = new Tweeter(TWITTER_ACCOUNT);
		    try {
				tweeter.tweet(generateWeatherTweet(randomEmotion, model, currentWeatherConditions));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
	    }
	}
	
	private static String generateWeatherTweet(String emotion, OntModel model,
			List<String> currentWeatherConditions) {
		String s = "";
		s += "I'm feeling " + emotion + (emotion.equals("sad")? ". :( " : ". :) ");
		OntClass emotionClass = model.getOntClass(modelNameSpace + capitalize(emotion));
		LinkedList<String> relevantWeather = new LinkedList<String>();
		for(final Iterator<String> i = currentWeatherConditions.iterator(); i.hasNext(); ) {
			Individual queryIndiv = model.getIndividual(modelNameSpace + i.next());
			if(queryIndiv.listRDFTypes(true).toSet().contains(emotionClass)) {
				relevantWeather.add(breakIntoWords(queryIndiv.getLocalName()));
			}
		}
		if(relevantWeather.isEmpty()) {
			s += "I am just ignoring the weather right now.";
		} else {
			s += "It is currently " + list(relevantWeather) + " outside.";
		}
		System.out.println("Generated tweet: " + s);
		return s;
	}
	
	private static String capitalize(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	private static String lowercase(String s, int i) {
		return s.substring(0, i) + Character.toLowerCase(s.charAt(i)) + s.substring(i+1);
	}
	
	private static String breakIntoWords(String s) {
		if(s == null) {
			return null;
		}
		s = s.replace("_", " ");
		// Break up camelCase.
		for(int i = 0; i < s.length(); i++) {
			if(Character.isUpperCase(s.charAt(i))) {
				s = lowercase(s, i);
				if(i > 0) {
					s = s.substring(0, i) + " " + s.substring(i);
					i++;
				}
			}
		}
		return s;
	}

	private static String list(LinkedList<String> relevantWeather) {
		String listedWeather = "";
		for(int i = 0; i < relevantWeather.size(); i++) {
			String s = breakIntoWords(relevantWeather.get(i));
			listedWeather += s;
			if(relevantWeather.size() >= 2 && i == relevantWeather.size() - 2) {
				listedWeather += " and ";
			}
			else if(i < relevantWeather.size() - 1) {
				listedWeather += ", ";
			}
		}
		return listedWeather;
	}
}
