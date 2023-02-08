package kursaDarbs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileW {
//	public Date getDateFromString(String stringDate) {
//	
//	return date;
//}
//Rokas cauri visām apakšdirektorijām, meklējot failus, kas atbilst parametriem un saglabājot rezultātu globālos mainīgajos
public static List<String[]> getListOfFiles(String path) { //saņem direktoriju, kurā meklēt
	List<String[]> directoryList = new ArrayList<>();
	File actual = new File(path); //izmanto saņemto direktoriju
	for (File f : actual.listFiles()) { //rokas cauri visam direktorijas saturam
		String[] add = new String[2]; //nākotnē, ir doma, būs vajadzīgi divi lauki, piemēram, String apakšdirektorija, kurā fails atrasts
		add[0] = f.getName();
		add[1] = path;
		if (isValidFileName(add[0], path)) {
			Main.fileList.add(add); //pievieno faila nosaukumu globālajam
			makeMaps(path + add[0]);
		}
		if (!add[0].contains(".")) { //te vēl !!!JĀPIELABO!!! Ja fails būs bez punkta, tiks uzskatīts par direktoriju. Varētu izraisīt !!!ERROR!!!
			directoryList.add(add);
		}
	}
	if (directoryList.size() > 0) {
		for (String[] dir : directoryList) {
			getListOfFiles(path + dir[0] + "/"); //ja ir atrasta(s) direktorija(s), izsauc šo pašu metodi, nododot jauno direktoriju, kurā meklēt. Ceļš netiek saglabāts, nav vajadzīgs atpakaļ solis.
		}
	}
	return directoryList;
}

//pēc vairākiem parametriem pārbauda, vai fails atbilst meklēto failu kritērijiem
public static boolean isValidFileName(String fileName, String path){
	String[] add = new String[3];
	add[0] = fileName;
	add[2] = path;
	
	if(!fileName.contains(".")) {
		add[1] = "Warning: Its probably a directory";
		Main.badFileList.add(add);
		return false;
	}
	if(!fileName.contains("csv") && fileName.contains(".")) {
		add[1] = "Warning: File has invalid type";
		Main.badFileList.add(add);
		return false; //ja fails nav .csv, tad nav derīgs
	}
	String[] temp = fileName.split("\\."); //atmet paplašinājumu
	String[] splited = temp[0].split("_");
	if (splited.length != 3) {
		add[1] = "Warning: File has invalid name";
		Main.badFileList.add(add);
		return false; //vai derīgs faila nosaukums
	}
	if (!isInt(splited[0]) || !isInt(splited[1]) || !isInt(splited[2])) {
		add[1] = "Warning: File has invalid name";
		Main.badFileList.add(add);
		return false; //vai ir inti
	}
	if (!isValidLengthOfString(splited[0], 1, 2) || !isValidLengthOfString(splited[1], 1, 2) || !isValidLengthOfStringYear(splited[2])) {
		add[1] = "Warning: Filename has invalid dateformat in name";
		Main.badFileList.add(add);
		return false; //vai pareiza garuma inti
	}
	//Pārbauda, vai nav dublikāts, kas nedrīkstētu notikt, jo katrai dienai viens fails
	if (Main.fileList.size() > 0) {
		for (String[] fileNameToCheck : Main.fileList) {
			if (fileName.equals(fileNameToCheck[0])) {
				add[1] = "Error: Found dublicate file";
				add[2] = "Ignored in " + add[2] + " and red from " + fileNameToCheck[1]; //ja ir atrasts dublikāts, tiek saglabātas abas direktorijas, kuros abi dublikāti atrodas.
				Main.badFileList.add(add);
				return false;
			}
		}
	}
	//System.out.println("good filename");
	return true;
}

public static boolean isInt(String s) {
	try {
		Integer.parseInt(s);
	} catch (NumberFormatException e) {
		return false;
	} catch (NullPointerException e) {
		return false;
	}
	return true;
}
public static boolean isValidLengthOfString(String s, int lowLimit, int highLimit){
	if (s.length() < lowLimit || s.length() > highLimit) {
		return false;
	}
	return true;
}
public static boolean isValidLengthOfStringYear(String s){
	if (s.length() != 4) {
		return false;
	}
	return true;
}
static void makeMaps(String filename) {
	try {
		File file = new File(filename);
		@SuppressWarnings("resource")
		Scanner read = new Scanner(file);
		while (read.hasNextLine()) {
			String row = read.nextLine();
			String[] data = row.split(";");
			if (data.length == 4) {
				Task t = new Task(data, filename);
				Person p = new Person(data);
				Status s = new Status(data);
				Tandem td = new Tandem(data);
				TandemStatus ts = new TandemStatus(data);
				Main.taskList.add(t);
				Main.persMap.put(p.name, p);
				Main.statMap.put(s.status, s);
				Main.tandemMap.put(td.tandem, td);
				Main.tandemStatMap.put(ts.status, ts);
				
			}
		}
		
	} catch (Exception e) {
		System.err.println("Not good at all");
		e.printStackTrace();
	}
}

//Savukārt, cikli vajadzīgi divi, lai vispirms izveidotu hashMapus, kuros likt tās vērtības.
static void readTasks(String filename) {
	try {
		File file = new File(filename);
		Scanner read1 = new Scanner(file);
		while (read1.hasNextLine()) {
			String row1 = read1.nextLine();
			String[] data1 = row1.split(";");
			if (data1.length == 4) {
				Task te = new Task(data1, filename);
				Tandem tan = new Tandem(data1);
				Main.persMap.get(te.boss).bossed.add(te);
				Main.persMap.get(te.clerk).clerked.add(te);
				Main.statMap.get(te.status.strip().toLowerCase()).task.add(te);
				Main.tandemStatMap.get(te.status).tandem.add(tan);
				int count = 0;
				if (Main.persNameList != null) {
					for (String name : Main.persNameList) {
						if (name.equals(data1[0])) count++; 
					}
					if (count == 0) Main.persNameList.add(data1[0]);
				}
				count = 0;
				if (Main.taskNameList != null) {
					for (String name : Main.taskNameList) {
						if (name.equals(data1[2])) count++; 
					}
					if (count == 0) Main.taskNameList.add(data1[2]);
				}
			}
		}
		read1.close();
	} catch (Exception e) {
		System.err.println("Not good at all");
		e.printStackTrace();
	}
}

}
