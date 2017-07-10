package com.yukti.dataone.model;

import java.io.Serializable;

public class SafetyEquipment implements Serializable {
	
	public String rollover_stability_control,tire_pressure_monitoring_system,daytime_running_lights,abs_two_wheel,
	airbags_front_passenger,airbags_front_driver,electronic_stability_control,airbags_side_impact,abs_four_wheel,
	brake_assist,airbags_side_curtain,electronic_traction_control;

	public String getAbs_four_wheel() {
		return abs_four_wheel;
	}

	public void setAbs_four_wheel(String abs_four_wheel) {
		this.abs_four_wheel = abs_four_wheel;
	}

	public String getAbs_two_wheel() {
		return abs_two_wheel;
	}

	public void setAbs_two_wheel(String abs_two_wheel) {
		this.abs_two_wheel = abs_two_wheel;
	}

	public String getAirbags_front_driver() {
		return airbags_front_driver;
	}

	public void setAirbags_front_driver(String airbags_front_driver) {
		this.airbags_front_driver = airbags_front_driver;
	}

	public String getAirbags_front_passenger() {
		return airbags_front_passenger;
	}

	public void setAirbags_front_passenger(String airbags_front_passenger) {
		this.airbags_front_passenger = airbags_front_passenger;
	}

	public String getAirbags_side_curtain() {
		return airbags_side_curtain;
	}

	public void setAirbags_side_curtain(String airbags_side_curtain) {
		this.airbags_side_curtain = airbags_side_curtain;
	}

	public String getAirbags_side_impact() {
		return airbags_side_impact;
	}

	public void setAirbags_side_impact(String airbags_side_impact) {
		this.airbags_side_impact = airbags_side_impact;
	}

	public String getBrake_assist() {
		return brake_assist;
	}

	public void setBrake_assist(String brake_assist) {
		this.brake_assist = brake_assist;
	}

	public String getDaytime_running_lights() {
		return daytime_running_lights;
	}

	public void setDaytime_running_lights(String daytime_running_lights) {
		this.daytime_running_lights = daytime_running_lights;
	}

	public String getElectronic_stability_control() {
		return electronic_stability_control;
	}

	public void setElectronic_stability_control(String electronic_stability_control) {
		this.electronic_stability_control = electronic_stability_control;
	}

	public String getElectronic_traction_control() {
		return electronic_traction_control;
	}

	public void setElectronic_traction_control(String electronic_traction_control) {
		this.electronic_traction_control = electronic_traction_control;
	}

	public String getRollover_stability_control() {
		return rollover_stability_control;
	}

	public void setRollover_stability_control(String rollover_stability_control) {
		this.rollover_stability_control = rollover_stability_control;
	}

	public String getTire_pressure_monitoring_system() {
		return tire_pressure_monitoring_system;
	}

	public void setTire_pressure_monitoring_system(String tire_pressure_monitoring_system) {
		this.tire_pressure_monitoring_system = tire_pressure_monitoring_system;
	}
}
