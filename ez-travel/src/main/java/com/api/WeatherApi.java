package com.api;

import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.cache.Cache;
import com.exceptions.WeatherApiException;

public class WeatherApi {

	private static final String Url = "http://api.openweathermap.org/data/2.5/weather";
	private static final String Key = "9242d768b6d28de00eafba6e380ed05f";
	private static final long expirationDateInMillis = 3600000;
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
		String weather = (String) cache.getValue(lon + "!" + lat);
		if (weather != null) {
			// if available in cache return it
			// System.out.println("from cache");
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
				String key = lon + "!" + lat;
				String value = generalizeWeather(arr.get("main").toString());
				Date expirationDate = new Date();
				expirationDate.setTime(expirationDate.getTime() + expirationDateInMillis);
				// save it in cache
				cache.addValue(key, value, expirationDate);

				return value;
			}
		}
	}

	private static String generalizeWeather(String weather) {

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
			System.err.println("-------------------------------------------------------------------------");
			System.err.println("ATTENTION");
			System.err.println("-------------------------------------------------------------------------");
			return weather.toLowerCase() + " - is not recognized as an option!";
		}

	}

	public static void main(String[] args) {

		WeatherApi api = new WeatherApi();

		try {
			System.out.println(api.getWeatherAndSave(30, 30));
			// going to cache
			System.out.println(api.getWeatherAndSave(30, 30));
			System.out.println(api.getWeatherAndSave(30, 30));

		} catch (WeatherApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
