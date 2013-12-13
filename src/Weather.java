import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import dme.forecastiolib.FIOCurrently;
import dme.forecastiolib.FIODataPoint;
import dme.forecastiolib.ForecastIO;

public class Weather {
	private final OntModel model;
	private final String modelNameSpace;
	private final Individual weather;
	private final ForecastIO fio;
	private FIODataPoint currentWeatherDataPoint;
	
	private final String FORECAST_IO_API_STRING = "af9c9f81d64b659c5f2303bc861373e7";
	private final String LATITUDE = "30.28685", LONGITUDE = "-97.73659"; // For UT Austin.

	public Weather(OntModel model, String modelNameSpace, Individual weather) {
		this.model = model;
		this.modelNameSpace = modelNameSpace;
		this.weather = weather;
		
		fio = new ForecastIO(LATITUDE, LONGITUDE, ForecastIO.UNITS_US, FORECAST_IO_API_STRING);
		currentWeatherDataPoint = new FIOCurrently(fio).get();
	}
	
	/**
	 * @param condition A weather condition, i.e. "Cold" or "Sunny"
	 * @return Whether this weather instance (Individual) has the specified condition.
	 */
	public boolean hasWeatherCondition(String condition) {
		return weather.hasProperty(model.getProperty(modelNameSpace+"hasWeatherCondition"),
				model.getIndividual(modelNameSpace+condition));
	}
	
	/**
	 * Adds a property with the specified value to this weather instance.
	 * @param property A property that has a range of double, i.e. "hasChancePrecipitation".
	 * @param value The value to set the property, i.e. 0.25.
	 */
	public void addProperty(String property, Double value) {
		weather.addLiteral(model.createOntProperty(modelNameSpace+property),
				model.createTypedLiteral(value, XSDDatatype.XSDdouble));
	}
	
	/**
	 * Adds a property with the specified value to this weather instance.
	 * @param property A property that has a range of string, i.e. "hasPrecipitationType".
	 * @param value The value to set the property, i.e. "rain".
	 */
	public void addProperty(String property, String value) {
		weather.addLiteral(model.createOntProperty(modelNameSpace+property),
				model.createTypedLiteral(value, XSDDatatype.XSDstring));
	}
	
	/**
	 * List the weather conditions of this weather instance.
	 * @return A list of conditions, i.e. "Hot, Sunny, NotRainy".
	 */
	public List<String> listConditions() {
		List<String> conditions = new LinkedList<>();
		final Property p = model.getProperty(modelNameSpace+"hasWeatherCondition");
		for(final StmtIterator i = weather.listProperties(p); i.hasNext(); ) {
			conditions.add(i.next().asTriple().getObject().getLocalName());
		}
		return conditions;
	}
	
	/**
	 * Refreshes the currentWeather data point.
	 */
	public void refresh() {
		if(fio.update()) {
			currentWeatherDataPoint = new FIOCurrently(fio).get();
		}
	}
	
	public Double getTemperature() {
		return currentWeatherDataPoint.temperature();
	}
	
	public Double getPrecipationChance() {
		return currentWeatherDataPoint.precipProbability();
	}
	
	public String getPrecipitationType() {
		return currentWeatherDataPoint.precipType();
	}
	
	public Double getCloudCoverage() {
		return currentWeatherDataPoint.cloudCover();
	}
	
	public Double getWindSpeed() {
		return currentWeatherDataPoint.windSpeed();
	}
}
