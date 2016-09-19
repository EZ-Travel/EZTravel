package com.pojos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table
public class Event implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String description;
	private String vendor_id;
	private Date exp_date;
	private double lon;
	private double lat;
	private double radius;
	private String image;
	private String weather;

	public Event() {

	}

	public Event(int id, String name, String description, String vendor_id, Date exp_date, double lon, double lat,
			double radius, String image, String weather) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.vendor_id = vendor_id;
		this.exp_date = exp_date;
		this.lon = lon;
		this.lat = lat;
		this.radius = radius;
		this.image = image;
		this.weather = weather;
	}

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double rad) {
		this.radius = rad;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String relevent_weather) {
		this.weather = relevent_weather;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", description=" + description + ", vendor_id=" + vendor_id
				+ ", exp_date=" + exp_date + ", lon=" + lon + ", lat=" + lat + ", radius=" + radius + ", image=" + image
				+ ", weather=" + weather + "]";
	}

}
