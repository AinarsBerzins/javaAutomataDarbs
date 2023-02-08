package kursaDarbs;

import java.util.ArrayList;

public class Status {
	String status;
	ArrayList<Task> task;
	
	public Status(String[] data) {
		this.status = data[2].strip().toLowerCase();
		this.task = new ArrayList<>();
	}
}
