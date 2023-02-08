package kursaDarbs;

import java.util.ArrayList;

public class Person {
	String name;
	ArrayList<Task> bossed;
	ArrayList<Task> clerked;
	
	
	public Person(String[] data) {
		this.name = data[0].strip();
		this.bossed = new ArrayList<>();
		this.clerked = new ArrayList<>();
		
	}

}