/*****************************************************************************
 * 
 * Copyright 2012 See AUTHORS file.
 * 
 * This file is part of Escape-IR.
 * 
 * Escape-IR is free software: you can redistribute it and/or modify
 * it under the terms of the zlib license. See the COPYING file.
 * 
 *****************************************************************************/

package fr.escape.resources.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.escape.Objects;
import fr.escape.game.entity.ships.Ship;

/**
 * <p>
 * An object which represent a Scenario Configuration in {@link ScenarioFactory}.
 * 
 */
final class ScenarioConfiguration {
	
	private int id;
	private int time;
	private int duration;
	private int bossID;
	private String backgroundID;
	
	private HashMap<Integer, Ship> ships;
	private List<String> script;
	
	/**
	 * Default Constructor
	 */
	ScenarioConfiguration() {
		id = 0;
		time = 0;
		duration = 0;
		ships = new HashMap<Integer, Ship>();
		script = new ArrayList<String>();
	}
	
	/**
	 * Get Scenario ID
	 * 
	 * @return Scenario ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Get Scenario Time
	 * 
	 * @return Scenario Time
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * Get Stage Duration
	 * 
	 * @return Stage Duration
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Get Scenario Boss ID
	 * 
	 * @return Boss ID
	 */
	public int getBossID() {
		return bossID;
	}
	
	public String getBackgroundID() {
		return backgroundID;
	}
	
	/**
	 * Set the Scenario Time
	 * 
	 * @param time Scenario Time
	 */
	public void setTime(int time) {
		this.time = time;
	}
	
	/**
	 * Set the Scenario ID
	 * 
	 * @param id Scenario ID
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Set the Stage Duration
	 * 
	 * @param duration Stage Duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * Set Scenario Boss ID
	 * 
	 * @param bossID : Boss ID
	 * <p>
	 * 0 : Jupiter Boss<br />
	 * 1 : Moon Boss<br />
	 * 2 : Jupiter Boss
	 * </p>
	 */
	public void setBossID(int bossID) {
		this.bossID = bossID;
	}
	
	public void setBackgroundID(String backgroundID) {
		this.backgroundID = backgroundID;
	}
	
	/**
	 * Add a Ship in Scenario
	 * 
	 * @param shipID Ship Key
	 * @param ship Ship Value
	 */
	public void addShip(int shipID, Ship ship) {
		ships.put(Integer.valueOf(shipID), Objects.requireNonNull(ship));
	}
	
	/**
	 * Add a Script/Command in Scenario
	 * 
	 * @param line Script/Command to add
	 */
	public void addScript(String line) {
		script.add(Objects.requireNonNull(line));
	}
	
	/**
	 * Get the list of Script/Command in Scenario
	 * 
	 * @return List of Script/Command
	 */
	public String[] getScript() {
		return script.toArray(new String[0]);
	}

	/**
	 * Get a Map of ShipID -> Ship used in the Scenario
	 * 
	 * @return Map of ShipID -> Ship
	 */
	public HashMap<Integer, Ship> getShip() {
		HashMap<Integer, Ship> map = ships;
		ships = null;
		return map;
	}
	
}