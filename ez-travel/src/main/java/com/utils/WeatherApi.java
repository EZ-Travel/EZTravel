package com.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

public class WeatherApi {

	private static final String Url = "http://api.openweathermap.org/data/2.5/weather";
	private static final String Key = "9242d768b6d28de00eafba6e380ed05f";
	private Client client;
	private WebTarget target;
	private Invocation.Builder builder;

	public WeatherApi() {
		client = ClientBuilder.newClient();
		target = client.target(Url);
	}

	public String getWeather(double lon, double lat) {
		builder = target.queryParam("APPID", Key).queryParam("lon", lon).queryParam("lat", lat)
				.request(MediaType.APPLICATION_JSON);
		String res = builder.get().readEntity(String.class);
		System.out.println(res);
		JSONObject obj = new JSONObject(res);
		// System.out.println(res);
		// if error:not found city
		if (obj.get("cod").equals("404")) {
			return "Error - invalid location";
		} else {
			JSONObject arr = obj.getJSONArray("weather").getJSONObject(0);
			return Util.generalizeWeather(arr.get("main").toString());
		}
	}

	public static void main(String[] args) {

		WeatherApi api = new WeatherApi();

		System.out.println(api.getWeather(30, 30));
	}
}
