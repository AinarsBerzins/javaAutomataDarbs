package kursaDarbs;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class Tandem {
	String tandem;
	String boss;
	
	public Tandem(String[] data) throws ParseException, UnsupportedEncodingException {
		this.tandem = getTandemName(data);
		this.boss = data[0].strip();
	}
	
	public String getTandemName(String[] data) throws UnsupportedEncodingException {
//		String[] first = data[0].split("");
//		String[] second = data[1].split("");
		byte[] first = data[0].getBytes("US-ASCII");
		byte[] second = data[1].getBytes("US-ASCII");
		if (data[0].length() == data[1].length()) {
			for (int i = 0; i < first.length; i++) {
				if (first[i] < second[i]) return data[0] + "AND" + data[1];
				if (second[i] < first[i]) return data[1] + "AND" + data[0];
			}
		}
		if (first.length < second.length) return data[0] + "AND" + data[1];
		return data[1] + "AND" + data[0];
	}
}
