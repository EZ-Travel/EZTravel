package com.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.cache.Cache;
import com.exceptions.WeatherApiException;
import com.pojos.Event;

public class WeatherApi {

	private static final String Url = "http://api.openweathermap.org/data/2.5/weather";
	private static final String Key = "9242d768b6d28de00eafba6e380ed05f";
	// private static final String PREFIX = "openWeather_";
	private static final long expirationDateInMillis = 3600000;
	private static final Collection<String> wordsDescribingSunny = getCollectionOfWordsDescribingSunny();
	private static final Collection<String> wordsDescribingRainy = getCollectionOfWordsDescribingRainy();

	private Client client;
	private WebTarget target;
	private Invocation.Builder builder;

	public WeatherApi() {
		client = ClientBuilder.newClient();
		target = client.target(Url);
	}

	public String getWeatherAndSave(double lon, double lat) throws WeatherApiException {
		Cache cache = Cache.getInstance();
		// search for the weather in cache
		String key = lon + "!" + lat;
		String weather = (String) cache.getValue(key);

		if (weather != null) {
			return weather;
		}
		// if not available in cache get it from 3rd party and save in cache
		else {
			builder = target.queryParam("APPID", Key).queryParam("lon", lon).queryParam("lat", lat)
					.request(MediaType.APPLICATION_JSON);
			String res = builder.get().readEntity(String.class);
			JSONObject obj = new JSONObject(res);
			// System.out.println(res);
			// if error:not found city
			if (obj.get("cod").equals("404")) {
				throw new WeatherApiException("No weather found for lon=" + lon + " ,lat=" + lat, null);
			} else {
				JSONObject arr = obj.getJSONArray("weather").getJSONObject(0);

				String value = generalizeWeather(arr.get("main").toString());
				Date expirationDate = new Date();
				expirationDate.setTime(expirationDate.getTime() + expirationDateInMillis);
				// save it in cache
				cache.addValue(key, value, expirationDate);

				return value;
			}
		}
	}

	private static String generalizeWeather(String weather) throws WeatherApiException {

		switch (weather.toLowerCase()) {

		// all cases for sunny
		case "clear":
		case "clouds":
		case "mist":
		case "fog":
			return "sunny";

		// all cases for rainy
		case "rain":
		case "drizzle":
		case "thunderstorm":
			return "rainy";

		default:
			throw new WeatherApiException(weather.toLowerCase() + " - is not recognized as an option!", null);
		}

	}

	private static Collection<String> getCollectionOfWordsDescribingSunny() {
		Collection<String> wordsToReturn;
		String wordsForSunny = "youthful warm verdantunforgettable tropical tan sweltering sweet "
				+ "sweaty sweating sunny sunburnt sun-sational sun-kissed sun-filled sun-drenched sun-baked "
				+ "summery sultry stifling sticky steamy starry sizzling shaded sensational seasonal scorching "
				+ "roasting ripe relaxing refreshing hot red poolside perfect patriotic outdoor oppressive "
				+ "light lazy leisurely lakeside humid hot heavenly hazy happy growing grilled green fresh "
				+ "free fragrant endless easy dreamy delightful damp clear cloudless clammy natural muggy "
				+ "moist ablaze abloom active air-conditioned alive allergic aquaholic backyard balmy barefoot "
				+ "beautiful blazing blistering boiling breezy bright burning cheerful light out lovely lush magical";
		wordsToReturn = new ArrayList<String>(Arrays.asList(wordsForSunny.split(" ")));
		return wordsToReturn;
	}

	private static Collection<String> getCollectionOfWordsDescribingRainy() {
		Collection<String> wordsToReturn;
		String wordsForRainy = "abundant amber autumnal back-to-school blustery bountiful breezy bright brilliant"
				+ " brisk brown changing chilly cold colder colored colorful comfortable cooling cozy crackling crisp"
				+ " crunchy deciduous earthy enchanting enjoyable fallen fireside flannel foggy foraging "
				+ "fresh frosty gold golden gray gusty harvested hibernating howling inspirational leaf-strewn magnificent"
				+ " moonlit orange overgrown pumpkin-flavored pumpkin-spiced rainy"
				+ "raked red relaxing ripe roaring rust-colored rustling scary seasonal soggy spectacular spooky turning "
				+ "unpredictable vibrant visual vivid wilted wilting windy wondrous woodland yellow";

		wordsToReturn = new ArrayList<String>(Arrays.asList(wordsForRainy.split(" ")));
		return wordsToReturn;
	}

	public String getRelevantWeather(Event event) {

		String weatherSunny = "sunny";
		String weatherRainy = "rainy";
		// to be implemented
		String description = event.getDescription().toLowerCase();
		for (String wordForSunny : wordsDescribingSunny) {
			if (description.contains(wordForSunny)) {
				return weatherSunny;
			}
		}
		for (String wordForRainy : wordsDescribingRainy) {
			if (description.contains(wordForRainy)) {
				return weatherRainy;
			}
		}

		return "No Appropriate weather found";
	}

	public static void main(String[] args) {

		WeatherApi api = new WeatherApi();

		try {
			System.out.println(api.getWeatherAndSave(30, 30));
			// going to cache
			// System.out.println(api.getWeatherAndSave(30, 30));
			// System.out.println(api.getWeatherAndSave(30, 30));
			System.out.println(getCollectionOfWordsDescribingSunny());
		} catch (WeatherApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
