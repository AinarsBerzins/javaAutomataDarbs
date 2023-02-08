package kursaDarbs;

import java.util.ArrayList;

public class TandemStatus {
	String status;
	ArrayList<Tandem> tandem;
	
	public TandemStatus(String[] data) {
		this.status = data[2].strip().toLowerCase();
		this.tandem = new ArrayList<>();
	}
}
