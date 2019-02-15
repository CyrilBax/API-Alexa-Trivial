package com.game.model;

public class Player {
	public String name;
	public int age;
	public int points;
	public int position;
	
	public Player() {}
	
	public Player(String name, int age) {
		this.name = name;
		this.age = age;
		this.points = 0;
		this.position = 0;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public int getAge() { return age; }
	public void setAge(int age) { this.age = age; }

	public int getPoints() { return points; }
	public void setPoints(int points) { this.points = points; }
	
	public int getPosition() {return position;}
	public void setPosition(int position) {this.position = position;}
}
