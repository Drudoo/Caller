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

public class Caller extends JPanel implements ItemListener,ActionListener, MouseListener,ClipboardOwner {

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
	System.out.println("createAndShowGUI");

	//Set the title of the jframe. 
	frame = new JFrame(configList.get(15));
	frame.getContentPane().setBackground(Color.BLACK);
	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	//When the window closes write all the statistics to a log.
	frame.addWindowListener( new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent we) {
			frame.setVisible(false);
			PrintWriter pw = null; 
		try {
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file, false);
			pw = new PrintWriter(fw);

			pw.println(stats_calls_made);
			pw.println(stats_calls_answered);
			pw.println(stats_calls_transfered_s);
			pw.println(stats_calls_transfered_a);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
			  pw.close();
			}
		}
		frame.dispose();
		}
	});

	//Array of last two digits in phone number. 
	Object[] options = {configList.get(16),configList.get(17), configList.get(18),configList.get(19),configList.get(20)};
    
    int n = JOptionPane.showOptionDialog(frame, configList.get(21), "Phone Setup",
    JOptionPane.YES_NO_CANCEL_OPTION,
    JOptionPane.WARNING_MESSAGE,
    null,
    options,
    options[3]);

    //Save what role is selected by the user
    role = n;
    switch (role) {
		case 4: phoneNumber=50; IPaddr=configList.get(22); break;
		case 3: phoneNumber=51; IPaddr=configList.get(23); break;
		case 2: phoneNumber=52; IPaddr=configList.get(24); break;
		case 1: phoneNumber=53; IPaddr=configList.get(25); break;
		case 0: phoneNumber=54; IPaddr=configList.get(26); break;
		default: phoneNumber=0; IPaddr="xx.xxx.xxx.xx"; break;
	}

	//Print phone number and IP address (for debug purposes)
	System.out.println(phoneNumber + " " + IPaddr);

	//Create the new content pane and populate it with the jframe.
    JComponent newContentPane = new Caller(frame.getContentPane());
    newContentPane.setBackground(Color.BLACK);
    newContentPane.setOpaque(true);
    frame.setContentPane(newContentPane);
    frame.setPreferredSize(new Dimension(300, 530));
    frame.pack();
    frame.setVisible(true);


	public static void main(String[] args) {

		//Try and use windows native look and feel. 
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException e) {
			System.out.println("Error: " + e);
		}
		catch (ClassNotFoundException e) {
			System.out.println("Error: " + e);
		}
		catch (InstantiationException e) {
			System.out.println("Error: " + e);
		}
		catch (IllegalAccessException e) {
			System.out.println("Error: " + e);
		}

		//Create and show the GUI defined earlier.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
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

	//Call phone using IP and phone number and increment the stats for calls made. 
	private void callPhone(String number) {
		urlRequest("http://" + IPaddr + "/command.htm?number=" + number);
		stats_calls_made++;
	}

	//Hang up the phone 
	private void hangUpPhone() {
		urlRequest("http://" + IPaddr + "/command.htm?key=CANCEL");
	}

	//Redial last called number
	private void redialPhone() {
		urlRequest("http://" + IPaddr + "/command.htm?key=REDIAL");
		answerPhone();
	}

	//How to send an urlRequest to the phone. 
	private void urlRequest(String url) {
		try {
			URL _url = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(_url.openStream()));
			String inputLine;
			in.close();
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

	//Answer the phone 
	private void answerPhone() {
		urlRequest("http://" + IPaddr + "/command.htm?key=ENTER");
		stats_calls_answered++;
	}

	//Answer another phone on the local network, using *8 enter (STAR-EIGHT-ENTER)
	private void answerOtherPhone() {
		urlRequest("http://" + IPaddr + "/command.htm?key=%2A");
		urlRequest("http://" + IPaddr + "/command.htm?key=8");
		urlRequest("http://" + IPaddr + "/command.htm?key=ENTER");
		stats_calls_answered++;
	}

	//Transfer a call using HOLD-TRANSFER-NUMBER-ENTER
	private void transfer(String number) {
		urlRequest("http://" + IPaddr + "/command.htm?key=F_HOLD");
		urlRequest("http://" + IPaddr + "/command.htm?key=TRANSFER");

		//The phone get a full eight digit number at a time when using transfer, 
		//so we type one digit at a time, but we need a small delay between each 
		//key in order to make sure the correct order is maintained. 
		for (char x : number.toCharArray()) {
			urlRequest("http://"+IPaddr+"/command.htm?key=" + x);
			try {
				Thread.sleep(500);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		//Confirm and transfer call
		urlRequest("http://"+IPaddr+"/command.htm?key=ENTER");
	}

	//What happens when the phone book button is pressed.
	public void openPhoneBook() {
		phoneBookFrame = new JFrame("Phone Book");
		phoneBookFrame.getContentPane().setBackground(Color.BLACK);
		phoneBookIsVisible=true;

		JComponent newContentPane = new PhoneBook(IPaddr);
		newContentPane.setBackground(Color.BLACK);

		phoneBookFrame.add(newContentPane);
		phoneBookFrame.setPreferredSize(new Dimension(300, 400));
		phoneBookFrame.pack();
		phoneBookFrame.setVisible(true);

		p_windowToFront = new JCheckBox("Always on top");
		p_windowToFront.addItemListener(this);
		p_windowToFront.setBackground(Color.BLACK);
		p_windowToFront.setForeground(ASColor);
		phoneOpen = true;
		phoneBookFrame.add(p_windowToFront,BorderLayout.SOUTH);

		phoneBookFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		phoneBookFrame.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				phoneBookIsVisible=false;
				phoneBookFrame.setVisible(false);
				phoneBookFrame.dispose();
				phoneOpen=false;
			}
		});
	}

	//Do the same for the airline book.
	public void openAirlineBook() {
		airlineBookFrame = new JFrame("Airline Book");
		airlineBookFrame.getContentPane().setBackground(Color.BLACK);
		airlineBookIsVisible=true;

		JComponent newContentPane = new AirlineBook();
		newContentPane.setBackground(Color.BLACK);

		airlineBookFrame.add(newContentPane);
		airlineBookFrame.setPreferredSize(new Dimension(300, 400));
		airlineBookFrame.pack();
		airlineBookFrame.setVisible(true);

		a_windowToFront = new JCheckBox("Always on top");
		a_windowToFront.addItemListener(this);
		a_windowToFront.setBackground(Color.BLACK);
		a_windowToFront.setForeground(ASColor);
		airlineOpen = true;
		airlineBookFrame.add(a_windowToFront,BorderLayout.SOUTH);
		airlineBookFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		airlineBookFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				airlineBookIsVisible=false;
				airlineBookFrame.setVisible(false);
				airlineBookFrame.dispose();
				airlineOpen=false;
			}
		});
	}

	//Get the content of the clipboard
	public String getClipboardContents() {
		//Store the result
		String result = "";

		//Get the clipbaord
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		
		if (hasTransferableText) {
			try {
				result = (String)contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}

	//Check if mouse is clicked inside the window
	public void mouseClicked(MouseEvent e) {
		//If right click is clicked, then replace callNumber with the content of the clipboard
		if (e.getButton() == 3) {
			callNumber.setText(getClipboardContents());
		}
	}

	//Methods needed in order to implement the mouseEvent and other events from line 47
	public void lostOwnership(Clipboard aClipboard, Transferable aContents){}
	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}

	//Check what button has been pressed. 
	public void actionPerformed(ActionEvent e) {
		//Get the number what we are trying to call
		String inputNumber = callNumber.getText();
		//Redial phone
		if (e.getActionCommand().equals(configList.get(7))) {
			redialPhone();
		//Open Phone book
		} else if (e.getActionCommand().equals(configList.get(13))) {
			//If the phonebook isnt visible, then open it
			if (!phoneBookIsVisible) {
				openPhoneBook();
			} else {
				//Else bring phone book to front
				java.awt.EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						phoneBookFrame.toFront();
						phoneBookFrame.repaint();
					}
				});
			}
		//Open airline book and do the same as phone book.
		} else if (e.getActionCommand().equals(configList.get(14))) {
			if (!airlineBookIsVisible) {
				openAirlineBook();
			} else {
				java.awt.EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						airlineBookFrame.toFront();
						airlineBookFrame.repaint();
					}
				});
			}
		//Transfer to Agent 2
		} else if (e.getActionCommand().equals(configList.get(6))) {
			stats_calls_transfered_a++;
			transfer(configList.get(27));
		//Transfer to Agent 1
		} else if (e.getActionCommand().equals(configList.get(5))) {
			stats_calls_transfered_s++;
			transfer(configList.get(28));
		//Answer other phone on the local network
		} else if (e.getActionCommand().equals(configList.get(4))) {
			answerOtherPhone();
		//Answer your own phone
		} else if (e.getActionCommand().equals(configList.get(3))) {
			answerPhone();
		//Hang up your phone
		} else if (e.getActionCommand().equals(configList.get(12))) {
			hangUpPhone();
		//Call number
		} else if(e.getActionCommand().equals(configList.get(2))) {
			callNumber.selectAll();
			callNumber.setText("");
			if (!inputNumber.equals("")) {
				callPhone(inputNumber);
			}
		//Default action, call number
		} else {
			callNumber.selectAll();
			callNumber.setText("");
			if (!inputNumber.equals("")) {
				callPhone(inputNumber);
			}
		}
	}

	//Bad hack to get foreground and background to work with each window. Not recommended to use. 
	public void itemStateChanged(ItemEvent e) {
		try {
			boolean selected = windowToFront.isSelected();
			if (selected) {
				isOnTop = true;
				System.out.println("Foreground");
				frame.setAlwaysOnTop(true);
			} else {
				isOnTop = false;
				System.out.println("Background");
				frame.setAlwaysOnTop(false);
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex);
		}

		try {
			boolean a_selected = a_windowToFront.isSelected();
			if (a_selected) {
				a_isOnTop = true;
				System.out.println("a_Foreground");
				airlineBookFrame.setAlwaysOnTop(true);
			} else {
				a_isOnTop = false;
				System.out.println("a_Background");
				airlineBookFrame.setAlwaysOnTop(false);
			}
		} catch (Exception ex) {
		//System.out.println("Error: " + ex);
		}

		try {
			boolean p_selected = p_windowToFront.isSelected();
			if (p_selected) {
				p_isOnTop = true;
				System.out.println("p_Foreground");
				phoneBookFrame.setAlwaysOnTop(true);
			} else {
				p_isOnTop = false;
				System.out.println("p_Background");
				phoneBookFrame.setAlwaysOnTop(false);
			}
		} catch (Exception ex) {
		//System.out.println("Error: " + ex);
		}
	}
}















