package kursaDarbs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Main {
	static String daysOfWeek = new String(); //Mainīgais dienu HashMapu veidošanai. Dienas atdalītas ar _.
	static ArrayList<String[]> fileList = new ArrayList<>();
	static ArrayList<String[]> badFileList = new ArrayList<>();
	static ArrayList<String> persNameList = new ArrayList<String>();
	static ArrayList<String> taskNameList = new ArrayList<String>();
	static ArrayList<Task> taskList = new ArrayList<Task>();
	static HashMap<String, Person> persMap = new HashMap<String, Person>();
	static HashMap<String, Status> statMap = new HashMap<String, Status>();
	static HashMap<String, Tandem> tandemMap = new HashMap<String, Tandem>();
	static HashMap<String, TandemStatus> tandemStatMap = new HashMap<String, TandemStatus>();

	public static void main(String[] args) {
		
		FileW.getListOfFiles("./");
		for (String[] filename : fileList) {
			FileW.readTasks(filename[1] + filename[0]);
		}
		new Chart();
		for (String[] row : badFileList) {
			System.out.println(row[1] + " " + row[2] + row[0]);
		}
	}
	//Saņem statusu un prasa sarēķināt katram DARBINIEKAM nostrādātās MINŪTES noteikta statusa darbos
	public static ArrayList<Comparator> getMinutesByStatus(String status){
		ArrayList<Comparator> data = new ArrayList<Comparator>();
		for (String key : persMap.keySet()) {
			int minutes = getClerkedMinutesByName(key, statMap.get(status).task);
			Comparator c = new Comparator(key, minutes);
			data.add(c);
		}
		Collections.sort(data);
		return data;
	}
	//Saņem statusu un prasa sarēķināt katram BOSAM nostrādātās REIZES noteikta statusa darbos
	public static ArrayList<Comparator> getTopOfBossesByStatus(String status){
		ArrayList<Comparator> data = new ArrayList<Comparator>();
		for (String key : persMap.keySet()) {
			int counter = getBossedCountOfTasksByName(key, statMap.get(status).task);
			Comparator dataToAdd = new Comparator (key, counter);
			data.add(dataToAdd);
		}
		Collections.sort(data);
		return data;
	}
	//Saņem statusu un prasa sarēķināt katram DARBINIEKAM nostrādātās REIZES noteikta statusa darbos
		public static ArrayList<Comparator> getTopOfClerksByStatus(String status){
			ArrayList<Comparator> data = new ArrayList<Comparator>();
			for (String key : persMap.keySet()) {
				int counter = getClerkedCountOfTasksByName(key, statMap.get(status).task);
				Comparator dataToAdd = new Comparator (key, counter);
				data.add(dataToAdd);
			}
			Collections.sort(data);
			return data;
		}
		//Saņem statusu un prasa sarēķināt katram PĀRIM nostrādātās REIZES noteikta statusa darbos
		public static ArrayList<Comparator> getTopOfTandemsByStatus(String status){
			ArrayList<Comparator> data = new ArrayList<Comparator>();
			for (String key : tandemMap.keySet()) {
				int counter = getTandemCountOfTasksByName(key, tandemStatMap.get(status).tandem);
				Comparator dataToAdd = new Comparator (key, counter);
				data.add(dataToAdd);
			}
			Collections.sort(data);
			return data;
		}
	//Saņem HashMapu ar nedēļas dienām un ArrayListu, kurā ir TotalTasks un Success tasks, lai izvilktu succees rate
	public static ArrayList<Comparator> getTaskSuccessRates(){
		HashMap<String, ArrayList<Double>> data = getTaskCountersByDays();
		ArrayList<Comparator> dataToReturn = new ArrayList<Comparator>();
		for (String key : data.keySet()) {
			Comparator c = new Comparator(key, data.get(key).get(0)/data.get(key).get(1));
//			System.out.println(c.value);
			dataToReturn.add(c);
		}
		Collections.sort(dataToReturn);
		return dataToReturn;
	}
	//Saņem HashMapu ar nedēļas dienām un nostrādātajām minūtēm katrā dienā, samet to visu objektu listē un sasortē
	public static ArrayList<Comparator> getMostPopularDaysInMinutes(){
		HashMap<String, ArrayList<Double>> data = getMinutesByDaysForAllTasks();
		ArrayList<Comparator> dataToReturn = new ArrayList<Comparator>();
		for (String key : data.keySet()) {
			Comparator c = new Comparator(key, data.get(key).get(0));
			dataToReturn.add(c);
		}
		Collections.sort(dataToReturn);
		return dataToReturn;
	}
	//Saliek visu nedēļas dienu TotalTasks un Done + Bossdidit HashMap ArrayListā, no kura cita metode izvilks success rate pa DIENĀM.
	public static HashMap<String, ArrayList<Double>> getTaskCountersByDays() {
		String oldDayOfWeek = "";
		double tasksThisDay = 0;
		double tasksSuccessThisDay = 0;
		HashMap<String, ArrayList<Double>> newOne = makeHashMapFromString();
		for (Task t : taskList) {
			//pārbauda vai pirmais cikls, kurā nevajadzētu pazaudēt vērtības
			if (oldDayOfWeek.length() == 0) {
				oldDayOfWeek = t.day; 
			}
			//ja nomainās dienas, tad tekošās dienas uzskaiti beidz un pieskaita jaunās vērtības nedēļas dienai un nonullē uzskaites mainīgos, kā arī iestata jauno dienu
			if (!t.day.equals(oldDayOfWeek)) { 
				newOne.get(oldDayOfWeek).set(0, newOne.get(t.day).get(0) + tasksSuccessThisDay / tasksThisDay); //izrēķina HashMap ArrayListā tekošās dienas success rate un pieskataita iepriekšējiem
				newOne.get(oldDayOfWeek).set(1, newOne.get(t.day).get(1) + 1); //pieskaita vēl vienu dienu pie uzskaitītajām nedēļas dienām.
				tasksThisDay = 0;
				tasksSuccessThisDay = 0;
				oldDayOfWeek = t.day;
			}
			//ja diena tā pati, tad uzskaita tekošās dienas kopējo un veiksmīgo darbu uzskaiti
			tasksThisDay += 1;
			if (t.status.equals("done") || t.status.equals("bossdidit")) {
				tasksSuccessThisDay += 1;
			}
		}
		return newOne;
	}
	//Saliek visu nedēļas dienu MINŪTES pa DIENĀM jaunizveidotā HashMap ArrayListā, kurā keys ir nedēļas DIENAS. Katrai dienai, cik ieraksti, tik liels ArrayLists.
	//Šis jocīgais penteris, lai nebūtu 6 reizes jātin cauri viss garais taskList.
	public static HashMap<String, ArrayList<Double>> getMinutesByDaysForAllTasks() {
		HashMap<String, ArrayList<Double>> newOne = makeHashMapFromString();
		for (Task t : taskList) {
			double newValue = newOne.get(t.day).get(0) + t.minutes;
			newOne.get(t.day).set(0, newValue);
		}
		return newOne;
	}
	//Sarēķina saņemtā STATUSA listā strādātās REIZES saņemtajam BOSAM
	public static int getBossedCountOfTasksByName(String name, ArrayList<Task> list) {
		int count = 0;
		for (Task t : list) {
			if (t.boss.equals(name)) count++;
		}
		return count;
	}
	//Sarēķina saņemtā STATUSA listā strādātās REIZES saņemtajam DARBINIEKAM
	public static int getClerkedCountOfTasksByName(String name, ArrayList<Task> list) {
		int count = 0;
		for (Task t : list) {
			if (t.clerk.equals(name)) count++;
		}
		return count;
	}
	//Sarēķina saņemtā STATUSA listā strādātās REIZES saņemtajam PĀRIM
		public static int getTandemCountOfTasksByName(String name, ArrayList<Tandem> list) {
			int count = 0;
			for (Tandem t : list) {
				if (t.tandem.equals(name)) count++;
			}
			return count;
		}
	//Sarēķina saņemtā STATUSA listā strādātās MINŪTES saņemtajam DARBINIEKAM
	public static int getClerkedMinutesByName(String name, ArrayList<Task> list){
		int count = 0;
		for (Task t : list) {
			if (t.clerk.equals(name)) count += t.minutes;
		}
		return count;
	}
	//Izskaita, cik katrs ir BOSĒJIS
	public static ArrayList<Comparator> getTopBosses() {
		ArrayList<Comparator> data = new ArrayList<Comparator>();
		for (String key : persMap.keySet()) {
			Comparator c = new Comparator(key, persMap.get(key).bossed.size());
			data.add(c);
		}
		Collections.sort(data);		
		return data;
	}
	//Izskaita, cik katrs ir DARBOJIES
	public static ArrayList<Comparator> getTopClerks() {
		ArrayList<Comparator> data = new ArrayList<Comparator>();
		for (String key : persMap.keySet()) {
			Comparator c = new Comparator(key, persMap.get(key).clerked.size());
			data.add(c);
		}
		Collections.sort(data);		
		return data;
	}
	//Savelk visu statistiku par STATUSIEM, izmantojot HashMap, kurā atslēgas ir STATUSI
	public static void printStatusStats() {
		for (String key : statMap.keySet()) {
			System.out.println(key + " tasks are " + statMap.get(key).task.size());
			for (String nameToCheck : persMap.keySet()) {
				int countTimeBoss = 0;
				int countTimeClerk = 0;
				int countBoss = 0;
				int countClerk = 0;
				for (int i = 0; i < statMap.get(key).task.size(); i++) {
					if (nameToCheck.equals(statMap.get(key).task.get(i).boss))
						countBoss++;
					if (nameToCheck.equals(statMap.get(key).task.get(i).clerk))
						countClerk++;
					if (nameToCheck.equals(statMap.get(key).task.get(i).boss))
						countTimeBoss += statMap.get(key).task.get(i).minutes;
					if (nameToCheck.equals(statMap.get(key).task.get(i).clerk))
						countTimeClerk += statMap.get(key).task.get(i).minutes;
				}
				System.out.println(nameToCheck + " gave " + key + " tasks " + countBoss + " times for " + countTimeBoss
						+ " minutes.");
				System.out.println(nameToCheck + " worked on " + key + " tasks " + countClerk + " times for "
						+ countTimeClerk + " minutes.");
			}
		}
	}
	//Izsauc metodi success rate pa DIENĀM atrašanai un izdrukā PIRMO
	public static void printSuccessRatesByDays() {
		ArrayList<Comparator> data = getTaskSuccessRates();
		double value = (Math.round(data.get(0).value * 100));
		value = value / 100;
		System.out.println("Best task success rate is " + value + " in " + data.get(0).name + "s.");
	}
	//Izsauc metodi success rate pa DIENĀM atrašanai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getSuccessRatesByDays() {
		ArrayList<Comparator> data = getTaskSuccessRates();
		return data;
	}
	//Izsauc metodi produktīvāko dienu atrašanai un izdrukā PIRMO
	public static void printMostProductiveWeekDay() {
		ArrayList<Comparator> data = getMostPopularDaysInMinutes();
		int hours = (int)data.get(0).value / 60;
		int minutes = (int)data.get(0).value % 60;
		System.out.println("Most productive day is " + data.get(0).name + " with " + hours + " hours and " + minutes + " minutes.");
	}
	//Izsauc metodi produktīvāko dienu atrašanai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostProductiveWeekDay() {
		ArrayList<Comparator> data = getMostPopularDaysInMinutes();
		return data;
	}
	//Izsauc metodi populārāko BOSU atrašanai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostPopularBoss() {
		ArrayList<Comparator> data = getTopBosses();
		return data;
	}
	//Izsauc metodi populārāko DARBINIEKA atrašanai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostPopularClerk() {
		ArrayList<Comparator> data = getTopClerks();
		return data;
	}
	//Izsauc metodi populārāko BOSU FAILED kategorijā atrašanai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostFailedByBoss() {
		ArrayList<Comparator> data = getTopOfBossesByStatus("failed");
		return data;
	}
	//Izsauc metodi populārāko DARBINIEKU FAILED kategorijā atrašanai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostFailedByClerk() {
		ArrayList<Comparator> data = getTopOfClerksByStatus("failed");
		return data;
	}
	//Izsauc metodi populārāko PĀRU FAILED kategorijā atrašanai un padod atpakaļ SARAKSTU
		public static ArrayList<Comparator> getMostFailedByTandem() {
			ArrayList<Comparator> data = getTopOfTandemsByStatus("failed");
			return data;
		}
	//Izsauc metodi populārāko BOSU DONE kategorijā atrašānai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostDoneByBoss() {
		ArrayList<Comparator> data = getTopOfBossesByStatus("done");
		return data;
	}
	//Izsauc metodi populārāko DARBINIEKU DONE kategorijā atrašānai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostDoneByClerk() {
		ArrayList<Comparator> data = getTopOfClerksByStatus("done");
		return data;
	}
	//Izsauc metodi populārāko DARBINIEKU DONE kategorijā atrašānai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostDoneByTandem() {
		ArrayList<Comparator> data = getTopOfTandemsByStatus("done");
		return data;
	}
	//Izsauc metodi populārāko DARBINIEKU DONE kategorijā atrašānai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostCancelledByTandem() {
		ArrayList<Comparator> data = getTopOfTandemsByStatus("cancelled");
		return data;
	}
	//Izsauc metodi ILGĀK strādājošo DARBINIEKU CANCELLED kategorijā atrašanai un padod atpakaļ SARAKSTU
	public static ArrayList<Comparator> getMostCanceledClerkByTime(){
		ArrayList<Comparator> data = getMinutesByStatus("cancelled");
		return data;
	}
	//No stringa, kurā ir nedēļas dienas uztaisa HashMap ar nedēļu dienām kā keys.
	public static HashMap<String, ArrayList<Double>> makeHashMapFromString(){
		String[] days = daysOfWeek.split("_");
		HashMap<String, ArrayList<Double>> newOne = new HashMap<String, ArrayList<Double>>();
		for (String day : days) {
			ArrayList<Double> arrayToAdd = new ArrayList<Double>();
			arrayToAdd.add((double) 0);
			arrayToAdd.add((double) 0);
			if (day.length() == 0) continue; //dēļ tā, ka Strings sākas ar _, pirmā vērtība ir tukšums pēc splitošanas
			newOne.put(day, arrayToAdd);
		}
		return newOne;
	}
}
