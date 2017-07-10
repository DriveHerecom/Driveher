package com.yukti.dataone.model;

import java.io.Serializable;

public class Efficiency implements Serializable{
	
	public String highway,fuel_grade,combined,city,transmission_id,fuel_type,engine_id;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCombined() {
		return combined;
	}

	public void setCombined(String combined) {
		this.combined = combined;
	}

	public String getEngine_id() {
		return engine_id;
	}

	public void setEngine_id(String engine_id) {
		this.engine_id = engine_id;
	}

	public String getFuel_grade() {
		return fuel_grade;
	}

	public void setFuel_grade(String fuel_grade) {
		this.fuel_grade = fuel_grade;
	}

	public String getFuel_type() {
		return fuel_type;
	}

	public void setFuel_type(String fuel_type) {
		this.fuel_type = fuel_type;
	}

	public String getHighway() {
		return highway;
	}

	public void setHighway(String highway) {
		this.highway = highway;
	}

	public String getTransmission_id() {
		return transmission_id;
	}

	public void setTransmission_id(String transmission_id) {
		this.transmission_id = transmission_id;
	}
}
