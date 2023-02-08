package kursaDarbs;

public class Comparator implements Comparable<Comparator>{
	String name;
	double value;
	
	public Comparator(String name, double d) {
		this.name = name;
		this.value = d;
	}

	@Override
	public int compareTo(Comparator o) {
		if (this.value < o.value) return 1;
		if (this.value > o.value) return -1;
		return 0;
	}
}