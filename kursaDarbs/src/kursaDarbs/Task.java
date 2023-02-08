package kursaDarbs;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Task {
	String boss;
	String clerk;
	String status;
	int minutes;
	String day;

	public Task(String[] data, String date) throws ParseException {
		this.boss = data[0].strip();
		this.clerk = data[1].strip();
		this.status = data[2].strip().toLowerCase();
		this.minutes = getMinutesFromString(data[3].strip());
		this.day = getDayFromString(date);
	}
	//Pārveido tabulas formātu minūtēs
	public int getMinutesFromString(String time) {
		String[] temp = time.split(":");
		int minutes = (Integer.parseInt(temp[0].trim()) * 60) + (Integer.parseInt(temp[1].trim()));
		return minutes;
	}
	//Atgūst nedēļas dienu angliski
	public String getDayFromString(String fullPath) throws ParseException {
		String[] fullPathSplited = fullPath.split("/"); //faila nosaukums ir ar visu direktoriju ceļu
		String filename = fullPathSplited[(fullPathSplited.length)-1]; //faila nosaukums ir pēdējais elements pilnajā direktoriju ceļā
		String dateSplited = filename.split("\\.")[0]; //noņem paplašinājumu
		int y = Integer.parseInt(dateSplited.split("_")[2]);
		int m = Integer.parseInt(dateSplited.split("_")[1]) - 1; //jocīgais mēnešus skaita no nulles.
		int d = Integer.parseInt(dateSplited.split("_")[0]);
		GregorianCalendar cal = new GregorianCalendar(y, m, d);
		String dayOfWeek = cal.getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.getDefault()); //iegūst nedēļas dienu vārdiskā formātā angļu valodā
		if (!Main.daysOfWeek.contains(dayOfWeek)) Main.daysOfWeek = Main.daysOfWeek + "_" + dayOfWeek; //saraksta stringu, kurā ir SEŠAS (???) dienas, kas atdalītas ar _. !!!Strings sākas ar _ !!!
		return dayOfWeek;
	}


}