//For layout/GUI
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import javax.swing.*;
import javax.swing.JOptionPane;

//For mouse+keyboard clicks
import java.awt.event.*;

//For clipboard (copy+paste)
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

//For images/icons
import javax.imageio.ImageIO;

//For file usage
import java.io.*;
import java.io.File;
import java.io.File;

//For scroll panes
import java.awt.Toolkit;

//For Native look and feel
//import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
//import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

//For date, lists, arrays and other
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

//For URL / URI
import java.net.*;
import java.net.URI;
import java.net.URL;

public class Caller extends JPanel  {

	//Generally 'a' is airline windows and 'p' is phone book window

	//Checkboxes to keep windows on top of all other windows
	private static JCheckBox windowToFront;
	private static JCheckBox a_windowToFront;
	private static JCheckBox p_windowToFront;

	//Text fields for phone number to call and for searching a manifest number
	private JTextField callNumber;
	private JTextField maniNumber;

	//Booleans to check if window is on top or not.
	private static boolean isOnTop;
	private static boolean a_isOnTop;
	private static boolean p_isOnTop;

	//JFrame for our main window.
	private static JFrame frame;

	//String for filename to use with the log.
	private static String fileName;

	//Static integers to keep stats over all instances of the app
	private static int stats_calls_made = 0;
	private static int stats_calls_answered = 0;
	private static int stats_calls_transfered_s = 0;
	private static int stats_calls_transfered_a = 0;

	//Frames for the two 'books' 
	private static JFrame phoneBookFrame;
	private static JFrame airlineBookFrame;

	//Cehck if each 'book' is open or not
	private static boolean airlineOpen = false;
	private static boolean phoneOpen = false;

	//Speficy what role / phone number to use
	private static int role;

	//Last two digits of phonenumber
	private static int phoneNumber;
	
	//Full phonenumber
	private static String fullPhoneNumber;

	//IP address of phone.
	private static String IPaddr;

	//Specify layout mode
	private GridLayout grid;

	//Check if 'books' are visible
	private static boolean phoneBookIsVisible = false;
	private static boolean airlineBookIsVisible = false;

	//Set text color.
	private Color ASColor = Color.decode("#3399FF");

	//Specify newline character
	private final static String newline = "\n";

	private static List<String> configList = new ArrayList<String>();

	//Create constructor
	public Caller(Container panel) {
		
		//Load external configs from Config.txt
		loadConfig();

		//Define how the grid should be
		grid = new GridLayout(0,1);

		//Create checkbox and make it clickable. 
		windowToFront = new JCheckBox("Always on top");
		windowToFront.addItemListener(this);
		windowToFront.setSelected(false);
		windowToFront.setOpaque(true);
		windowToFront.setBackground(Color.BLACK);

		//Create title and subtitle and set the window color.
		JLabel title = new JLabel(configList.get(0));
		title.setForeground(ASColor);
		JLabel subTitle = new JLabel(configList.get(1));
		subTitle.setForeground(ASColor);
		windowToFront.setForeground(ASColor);

		//Create buttons and set the labels of the 
		JButton call = new JButton(configList.get(2));
		JButton answer = new JButton(configList.get(3));
		JButton answerOther = new JButton(configList.get(4));
		JButton transferToA1 = new JButton(configList.get(5));
		JButton transferToA2 = new JButton(configList.get(6));
		JButton redial = new JButton(configList.get(7));

		//Set colors of each button
		redial.setBackground(Color.BLACK);
		call.setBackground(Color.BLACK);
		answer.setBackground(Color.BLACK);
		answerOther.setBackground(Color.BLACK);
		transferToA1.setBackground(Color.BLACK);
		transferToA2.setBackground(Color.BLACK);

		//Setup phone number, IP and transfer labels
		JLabel phoneN = new JLabel(configList.get(8)+phoneNumber);
		phoneN.setForeground(ASColor);
		JLabel ipLabel = new JLabel(configList.get(9)+IPaddr);
		ipLabel.setForeground(ASColor);
		JLabel transferCall = new JLabel(configList.get(10));
		transferCall.setForeground(ASColor);
		JLabel callOut = new JLabel(configList.get(11));
		callOut.setForeground(ASColor);

		//Setup phone number input field
		callNumber = new JTextField();
		
		//Add listeder to the input field, so we can right click 
		callNumber.addMouseListener(this);

		//Hangup button 
		JButton hangUp = new JButton(configList.get(12));
		hangUp.setBackground(Color.BLACK);

		//Try and read images from the 'resources' folder and add them to the buttons
		try {
			Image img = ImageIO.read(getClass().getResource("resources/incoming.png"));
			answer.setIcon(new ImageIcon(img));
			img = ImageIO.read(getClass().getResource("resources/traffic.png"));
			answerOther.setIcon(new ImageIcon(img));
			img = ImageIO.read(getClass().getResource("resources/outgoing.png"));
			call.setIcon(new ImageIcon(img));
			img = ImageIO.read(getClass().getResource("resources/record.png"));
			hangUp.setIcon(new ImageIcon(img));
			img = ImageIO.read(getClass().getResource("resources/transfer.png"));
			transferToA2.setIcon(new ImageIcon(img));
			transferToA1.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.out.println("Error: " + ex);
		}

		//Code to seach for manifest and docklet based on Manifest number, this only works if default browser is Internet Explorer.
/*
		JButton mani = new JButton("Manifest");
		JButton docket = new JButton("Docklet");
		maniNumber = new JTextField();
		JPanel searchMani = new JPanel(new GridLayout(1,3));
		searchMani.add(maniNumber);
		searchMani.add(mani);
		searchMani.add(docket);
		searchMani.setBackground(Color.BLACK);
		searchMani.setOpaque(true);
		mani.setBackground(Color.BLACK);
		mani.setOpaque(true);
		docket.setBackground(Color.BLACK);
		docket.setOpaque(true);
		mani.addActionListener(this);
		docket.addActionListener(this);
*/

		//Make a new panel for the two answer buttons and setup the grid and color. 
		JPanel callAnswer = new JPanel(new GridLayout(1,2));
		callAnswer.add(answer);
		callAnswer.add(answerOther);
		callAnswer.setBackground(Color.BLACK);
		callAnswer.setOpaque(true);

	    //Make a new panel for the two transfer buttons 
	    JPanel sasAvi = new JPanel(new GridLayout(1,2));
	    sasAvi.add(transferToA1);
	    sasAvi.add(transferToA2);
	    sasAvi.setBackground(Color.BLACK);

	    //Setup listeners for most buttons
	    transferToA1.addActionListener(this);
	    transferToA2.addActionListener(this);
	    answerOther.addActionListener(this);
	    callNumber.addActionListener(this);
	    call.addActionListener(this);
	    hangUp.addActionListener(this);
	    answer.addActionListener(this);
	    redial.addActionListener(this);

	    //Setup the the two buttons to open the 'books'.
	    JButton phoneBook = new JButton(configList.get(13));
	    JButton airlineBook = new JButton(configList.get(14));
	    JPanel books = new JPanel(new GridLayout(1,2));
	    books.add(phoneBook);
	    books.add(airlineBook);
	    phoneBook.addActionListener(this);
	    airlineBook.addActionListener(this);
	    phoneBook.setBackground(Color.BLACK);
	    airlineBook.setBackground(Color.BLACK);
	    books.setBackground(Color.BLACK);

	    //Add all the items to the frame in order.
	    frame.setLayout(grid);
	    frame.add(title);
	    frame.add(subTitle);
	    frame.add(phoneN);
	    frame.add(callNumber);
	    frame.add(call);
	    frame.add(callAnswer);
	    frame.add(hangUp);
	    frame.add(redial);
	    frame.add(transferCall);
	    frame.add(sasAvi);
	    frame.add(windowToFront);
	    frame.add(books);
	    //frame.add(searchMani);

	    //Setup the border
	    setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

	    //Add the panel to out window
	    add(panel, BorderLayout.PAGE_START);
	}

	//Create and show the GUI
	public static void createAndShowGUI() {
		
	}

	public static void main(String[] args) {
		
	}

	private static void loadConfig() {
		String fileName = "Config.txt";

		File f = new File(fileName);
		if(f.exists() && !f.isDirectory()) { 

			BufferedReader reader = null;
			BufferedReader tempReader = null;
			try {
				reader = new BufferedReader(new FileReader(f));
				tempReader = reader;
				String text = null;
				String tempText = null;


				while ((text = reader.readLine()) != null) {
					configList.add(text);
				}

				for (int i = 0; i<2; i++) {
					System.out.println(configList.get(i));
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			
				try {
					if (reader != null) {
					reader.close();
					}
				} catch (IOException e) {
				}
			}
		} else {
			System.out.println("File Does not Exists");
		}
	}
}















