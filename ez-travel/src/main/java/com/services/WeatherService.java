package com.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.api.WeatherApi;
import com.exceptions.WeatherApiException;

@Path("weather")
public class WeatherService {

	private WeatherApi weatherApi = new WeatherApi();

	// GET
	@Path("getweather")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getWeather(@QueryParam("lon") double lon, @QueryParam("lat") double lat) {
		String weather = null;
		try {
			weather = weatherApi.getWeatherAndSave(lon, lat);
		} catch (WeatherApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return weather;
	}
}
