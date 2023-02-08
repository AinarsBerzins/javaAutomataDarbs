package kursaDarbs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Chart extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	static DefaultCategoryDataset barDataset;
	JLabel label1 = new JLabel("");
	JLabel label2 = new JLabel("");
	JLabel label3 = new JLabel("");
	JLabel label4 = new JLabel("");
	JLabel label5 = new JLabel("");
	JLabel label6 = new JLabel("");
	JPanel buttonBasicPanel = new JPanel(new GridLayout(12, 1));
	JPanel bossPanel = new JPanel();
	JPanel clerkPanel = new JPanel();
	JPanel tandemPanel = new JPanel();
	ChartPanel chartPanel = new ChartPanel(null);

	public Chart() {
		super ("Lets chart this!");
		this.setSize(800, 600);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(1, 1));
		
		JMenuBar bar = new JMenuBar();
		JMenu options = new JMenu("Options");
		JMenuItem basic = new JMenuItem("Basic");
		JMenuItem boss = new JMenuItem("Bosses");
		JMenuItem clerk = new JMenuItem("Clerks");
		JMenuItem tandem = new JMenuItem("Tandems");
		JMenuItem close = new JMenuItem("Close");
		
		JButton button1 = new JButton ("Print Most Popular Clerk");
		JButton button2 = new JButton ("Print Most Popular Boss");
		JButton button3 = new JButton ("Print Most Productive Weekday");
		JButton button4 = new JButton ("Print Most incompetent Boss");
		JButton button5 = new JButton ("Print Time Spent on Cancelled");
		JButton button6 = new JButton ("Print Most Successfull Weekday");
		JButton button7 = new JButton ("Popular");
		button7.setActionCommand("bossPopular");
		JButton button8 = new JButton ("Successive");
		button8.setActionCommand("bossSuccess");
		JButton button9 = new JButton ("Unsuccessive");
		button9.setActionCommand("bossUnsuccess");
		JButton button10 = new JButton ("Popular");
		button10.setActionCommand("clerkPopular");
		JButton button11 = new JButton ("Successive");
		button11.setActionCommand("clerkSuccess");
		JButton button12 = new JButton ("Unsuccessive");
		button12.setActionCommand("clerkUnsuccess");
		JButton button13 = new JButton ("Successive");
		button13.setActionCommand("tandemSuccess");
		JButton button14 = new JButton ("Unsuccessive");
		button14.setActionCommand("tandemUnsuccess");
		JButton button15 = new JButton ("Canceled");
		button15.setActionCommand("tandemCanceled");
		
		options.add(basic);
		options.add(boss);
		options.add(clerk);
		options.add(tandem);
		options.addSeparator();
		options.add(close);
		bar.add(options);
		setJMenuBar(bar);
		
		basic.addActionListener(this);
		boss.addActionListener(this);
		clerk.addActionListener(this);
		tandem.addActionListener(this);
		close.addActionListener(this);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		button7.addActionListener(this);
		button8.addActionListener(this);
		button9.addActionListener(this);
		button10.addActionListener(this);
		button11.addActionListener(this);
		button12.addActionListener(this);
		button13.addActionListener(this);
		button14.addActionListener(this);
		button15.addActionListener(this);
		
		buttonBasicPanel.add(button1);
		buttonBasicPanel.add(button2);
		buttonBasicPanel.add(button3);
		buttonBasicPanel.add(button4);
		buttonBasicPanel.add(button5);
		buttonBasicPanel.add(button6);
		buttonBasicPanel.add(label1);
		buttonBasicPanel.add(label2);
		buttonBasicPanel.add(label3);
		buttonBasicPanel.add(label4);
		buttonBasicPanel.add(label5);
		buttonBasicPanel.add(label6);
		
		bossPanel.add(button7);
		bossPanel.add(button8);
		bossPanel.add(button9);
		
		clerkPanel.add(button10);
		clerkPanel.add(button11);
		clerkPanel.add(button12);
		
		tandemPanel.add(button13);
		tandemPanel.add(button14);
		tandemPanel.add(button15);
		
		this.add(buttonBasicPanel);
		this.setVisible(true);
		label1.setVisible(false);
		label2.setVisible(false);
		label3.setVisible(false);
		label4.setVisible(false);
		label5.setVisible(false);
		label6.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		String name = e.getActionCommand();
		if (label1.getText().length() == 0) setLabels();
		switch(name) 
        { 
            case "Print Most Popular Clerk":
            	label1.setVisible(changeStatus(label1.isVisible()));
                break; 
            case "Print Most Popular Boss": 
            	label2.setVisible(changeStatus(label2.isVisible()));
                break; 
            case "Print Most Productive Weekday":
        		label3.setVisible(changeStatus(label3.isVisible()));
                break;
            case "Print Most incompetent Boss":
            	label4.setVisible(changeStatus(label4.isVisible()));
            	break;
            case "Print Time Spent on Cancelled":
        		label5.setVisible(changeStatus(label5.isVisible()));
            	break;
            case "Print Most Successfull Weekday": 
            	label6.setVisible(changeStatus(label6.isVisible()));
                break;
                
            case "bossPopular":
            	setDataset(Main.getMostPopularBoss(), "Top 5 bosses by popularity");
            	break;
            case "bossSuccess":
            	setDataset(Main.getMostDoneByBoss(), "Top 5 bosses by failed tasks");
            	break;
            case "bossUnsuccess":
            	setDataset(Main.getMostFailedByBoss(), "Top 5 bosses by failed tasks");
            	break;
            	
            case "clerkPopular":
            	setDataset(Main.getMostPopularClerk(), "Top 5 clerks by popularity");
            	break;
            case "clerkSuccess":
            	setDataset(Main.getMostDoneByClerk(), "Top 5 clerks by failed tasks");
            	break;
            case "clerkUnsuccess":
            	setDataset(Main.getMostFailedByClerk(), "Top 5 clerks by failed tasks");
            	break;
            	
            case "tandemSuccess":
            	setTandemDataset(Main.getMostDoneByTandem(), "Top 5 Best matched emploees");
            	break;
            case "tandemUnsuccess":
            	setTandemDataset(Main.getMostFailedByTandem(), "Top 5 Employees that should never work together");
            	break;
            case "tandemCanceled":
            	setTandemDataset(Main.getMostCancelledByTandem(), "Top 5 Most unfortunate tandems");
            	break;
            	
            case "Basic":
            	this.dispose();
            	new Chart();
            	break;
            case "Bosses":
            	clearDataset();
            	createBarChart();
            	clerkPanel.setVisible(false);
            	tandemPanel.setVisible(false);
            	bossPanel.setVisible(true);
            	break;
            case "Clerks":
            	clearDataset();
            	createBarChart();
            	bossPanel.setVisible(false);
            	tandemPanel.setVisible(false);
            	clerkPanel.setVisible(true);
            	break;
            case "Tandems":
            	clearDataset();
            	createBarChart();
            	bossPanel.setVisible(false);
            	clerkPanel.setVisible(false);
            	tandemPanel.setVisible(true);
            	break;
            case "Close":
            	System.exit(0);
        } 
	}
	void setLabels() {
		ArrayList<Comparator> data1 = Main.getMostPopularClerk();
    	label1.setText("Most popular clerk is: " + data1.get(0).name + " with " + (int)data1.get(0).value + " times clerked.");
    	
    	ArrayList<Comparator> data2 = Main.getMostPopularBoss();
    	label2.setText("Most popular boss is: " + data2.get(0).name + " with " + (int)data2.get(0).value + " times bossed.");
    	
    	ArrayList<Comparator> data3 = Main.getMostProductiveWeekDay();
    	int hours3 = (int)data3.get(0).value / 60;
		int minutes3 = (int)data3.get(0).value % 60;
		label3.setText("Most productive day is " + data3.get(0).name + " with " + hours3 + " hours and " + minutes3 + " minutes.");
		
		ArrayList<Comparator> data4 = Main.getMostFailedByBoss();
    	label4.setText("Most incompetent boss is " + data4.get(0).name + " with " + (int)data4.get(0).value + " given FAILED tasks.");
    	
    	ArrayList<Comparator> data5 = Main.getMostCanceledClerkByTime();
    	int hours5 = (int)data5.get(0).value / 60;
		int minutes5 = (int)data5.get(0).value % 60;
		label5.setText("Most time spent on CANCELLED tasks is " + data5.get(0).name + ". " + hours5 + "h " + minutes5 + "m.");
		
		ArrayList<Comparator> data6 = Main.getSuccessRatesByDays();
    	double value = (Math.round(data6.get(0).value * 100));
		value = value / 100;
    	label6.setText("Best task success rate is " + value + " in " + data6.get(0).name + "s.");
	}
	
	void createBarChart() {
		if (chartPanel.getComponentCount() == 0) {
			barDataset = new DefaultCategoryDataset();
			JFreeChart barChart = ChartFactory.createBarChart(
					"", "", "", this.barDataset, PlotOrientation.VERTICAL, true, false, true);
			chartPanel = new ChartPanel(barChart);
			this.setContentPane(chartPanel);
			this.add(bossPanel);
			this.add(clerkPanel);
			this.add(tandemPanel);
		}
	}
	
	void setDataset(ArrayList<Comparator> data, String req) {
		clearDataset();
		for (int i = 0; i < data.size() && i < 5; i++) {
			barDataset.addValue(data.get(i).value, data.get(i).name, req);
		}
	}
	
	void setTandemDataset(ArrayList<Comparator> data, String req) {
		clearDataset();
		for (int i = 0; i < data.size() && i < 5; i++) {
			barDataset.addValue(data.get(i).value, data.get(i).name.split("AND")[0] + " and " + data.get(i).name.split("AND")[1], req);
		}
	}
	
	private boolean changeStatus(boolean s) {
		if (s == true) return false;
		return true;
	}
	
	void clearDataset() {
		if (barDataset != null) {
			barDataset.clear();
		}
	}
}
