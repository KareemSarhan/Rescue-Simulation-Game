package view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")

public class RescueSim extends JFrame {

	public JPanel RescuePanel = new JPanel();
	public JPanel UnitPanel = new JPanel();
	public JPanel AvilableUnitPanel = new JPanel();
	public JPanel RespondingUnitPanel = new JPanel();
	public JPanel TreatingUnitPanel = new JPanel();
	public JPanel InfoPanel = new JPanel();
	public JPanel CitANDBilPanel = new JPanel();
	public JPanel GenralInfoPanel = new JPanel();
	public JScrollPane scroll;
	static ImageIcon Cit2;
	static ImageIcon Cit1;
	BufferedImage Main;
	static BufferedImage BilCit;
	BufferedImage outputImage;
	static ImageIcon Temp;

	public JTextArea AvilableUnitText = new JTextArea();
	public JTextArea RespondingUnitText = new JTextArea();
	public JTextArea title1 = new JTextArea();
	public JTextArea title2 = new JTextArea();
	public JTextArea TreatingUnitText = new JTextArea();
	public TextArea LogsPanel = new TextArea();
	public TextArea TextInfoPanel = new TextArea();
	public JTextPane Casualties = new JTextPane();
	public JTextArea CC = new JTextArea();
	public JTextArea JarvisLOG = new JTextArea();
	public JButton HINT = new JButton();
	public void RescuePanelMaker(JPanel rescuePanel) {
		// RescuePanel.setPreferredSize(new Dimension(100, getHeight()));
		// for (int i = 0;i!=100;i++)
		// {
		// JButton l= new JButton();
		// RescuePanel.add(l);
		// }

		RescuePanel.setLayout(new GridLayout(10, 10, 10, 10));
		// RescuePanel.setPreferredSize(new Dimension(1000,1000));
		// RescuePanel.setBounds(0, 0, 1000, 1000);
	}

	public void AvilableUnitPanelMaker(JPanel AvilableUnitPanel) {
		int X = 6;
		int Y = 2;
		AvilableUnitPanel.setLayout(new GridLayout(X, Y));
		// for (int i = 0;i!=5;i++)
		// {
		// JButton l= new JButton();
		// AvilableUnitPanel.add(l);
		// }
		AvilableUnitPanel.setPreferredSize(new Dimension(300, (getHeight() / 3) - 25));
	}

	public void RespondingUnitPanelMaker(JPanel RespondingUnitPanel) {
		int X = 6;
		int Y = 2;
		RespondingUnitPanel.setLayout(new GridLayout(X, Y));

		RespondingUnitPanel.setPreferredSize(new Dimension(300, (getHeight() / 3) - 25));
	}

	public void TreatingUnitPanelMaker(JPanel TreatingUnitPanel) {
		int X = 6;
		int Y = 2;
		TreatingUnitPanel.setLayout(new GridLayout(X, Y));

		TreatingUnitPanel.setPreferredSize(new Dimension(300, (getHeight() / 3) - 25));
	}

	public void UnitPanelMaker(JPanel UnitPanel) {
		UnitPanel.setPreferredSize(new Dimension(300, getHeight()));
		UnitPanel.setLayout(new BoxLayout(UnitPanel, BoxLayout.Y_AXIS));
	}

	public void InfoPanelMaker(JPanel InfoPanel) {
		InfoPanel.setPreferredSize(new Dimension(300, getHeight()));
		InfoPanel.setLayout(new BoxLayout(InfoPanel, BoxLayout.Y_AXIS));
	}

	public void LogsPanelMaker(TextArea logsPanel2, String l) {
		logsPanel2.setPreferredSize(new Dimension(300, l.length() + 500));
	}

	public void CitANDBilPanelMaker(JPanel CitANDBilPanel) {
		CitANDBilPanel.setPreferredSize(new Dimension(300, getHeight()));
	}

	public static String BilCitNumAdder(int num) throws IOException {
		int F = Random(1, 100000000);
		int offset = 3;
		BufferedImage big = ImageIO.read(new File("c:/Java_Dev/GridShit/BilCit.png"));
		String N = "" + num;
		int width = Math.max(
				(ImageIO.read(new File("c:/Java_Dev/GridShit/Numbers/" + N.charAt(0) + ".png"))).getWidth(),
				big.getWidth()) + offset;
		int height = Math.max(
				(ImageIO.read(new File("c:/Java_Dev/GridShit/Numbers/" + N.charAt(0) + ".png"))).getHeight(),
				big.getHeight()) + offset;
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color oldColor = g2.getColor();
		g2.setPaint(Color.RED);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(big, null, 0, 0);
		for (int j = 0; j < N.length(); j++) {
			g2.drawImage((ImageIO.read(new File("c:/Java_Dev/GridShit/Numbers/" + N.charAt(j) + ".png"))),
					58 + j * 18 + offset * j, 12, 18, 18, null);
			// g2.drawImage( , null, 20, 93);
		}
		g2.dispose();
		ImageIO.write(newImage, "png", new File("c:/Java_Dev/joined" + F + ".png"));
		return "c:/Java_Dev/joined" + F + ".png";
	}

	public static Icon CitAdder() throws IOException {
		if (Random(1, 200) > 100) {
			Temp = Cit1;
		} else {
			Temp = Cit2;
		}
		return Cit2;
	}

	public static int Random(int X, int Y) {
		int R;
		R = (int) (Math.random() * (Y - X + 1) + X);
		return R;
	}

	public static String rebuildbil(int num, String disastername) throws IOException {

		int offset = 0;

		BufferedImage big = ImageIO.read(new File(BilCitNumAdder(num)));
		int width = Math.max(big.getWidth(), big.getWidth()) + offset;
		int height = Math.max(big.getHeight(), big.getHeight()) + offset;
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		//g2.setPaint(Color.RED);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawImage(big, null, 0, 0);
		// for (int j = 0; j < N.length(); j++) {
		g2.drawImage((ImageIO.read(new File("c:/Java_Dev/GridShit/Disasters/" + disastername + ".png"))),
				58 + 18 + offset, 12, 18, 18, null);
		// g2.drawImage( , null, 20, 93);
		g2.dispose();
		int F = Random(1, 1000000);
		ImageIO.write(newImage, "png", new File("c:/Java_Dev/joined" + disastername + F + ".png"));
		return "c:/Java_Dev/joined" + disastername + F + ".png";

	}

	// public static String swrunitspanel(String unitname) throws IOException{
	//
	// int offset = 3;
	//
	//
	// BufferedImage big= ImageIO.read(new
	// File(("c:/Java_Dev/GridShit/humans/CIT1.png")));
	// int width = Math.max(big.getWidth() , big.getWidth()) + offset;
	// int height = Math.max(big.getHeight(), big.getHeight()) + offset;
	// BufferedImage newImage = new BufferedImage(width, height,
	// BufferedImage.TYPE_INT_ARGB);
	// Graphics2D g2 = newImage.createGraphics();
	// Color oldColor = g2.getColor();
	// g2.setPaint(Color.WHITE);
	// g2.fillRect(0, 0, width, height);
	// g2.setColor(oldColor);
	// g2.drawImage(big, null, 0, 0);
	// //for (int j = 0; j < N.length(); j++) {
	// g2.drawImage((ImageIO.read(new
	// File("c:/Java_Dev/GridShit/Disasters/"+disastername+".png"))),40, 40, 60, 60,
	// null);
	// //g2.drawImage( , null, 20, 93);
	// g2.dispose();
	// int F=Random(1, 1000000);
	// ImageIO.write(newImage, "png", new
	// File("c:/Java_Dev/joined"+disastername+F+".png"));
	// return "c:/Java_Dev/joined"+disastername+F+".png";
	//
	//
	// }

	public static String rebuildcit(String disastername) throws IOException {

		int offset = 0;

		BufferedImage big = ImageIO.read(new File(("c:/Java_Dev/GridShit/humans/CIT1.png")));
		int width = Math.max(big.getWidth(), big.getWidth()) + offset;
		int height = Math.max(big.getHeight(), big.getHeight()) + offset;
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		//g2.setPaint(Color.WHITE);
		//g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(big, null, 0, 0);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// for (int j = 0; j < N.length(); j++) {
		g2.drawImage((ImageIO.read(new File("c:/Java_Dev/GridShit/Disasters/" + disastername + ".png"))), 40, 40, 60,
				60, null);
		// g2.drawImage( , null, 20, 93);
		g2.dispose();
		int F = Random(1, 1000000);
		ImageIO.write(newImage, "png", new File("c:/Java_Dev/joined" + disastername + F + ".png"));
		return "c:/Java_Dev/joined" + disastername + F + ".png";

	}

	public static void resizeImage() throws IOException {

		String fileName = "c:/Java_Dev/casacsacs.png";
		int width = 50;
		int height = 50;
		BufferedImage image = ImageIO.read(new File("c:/Java_Dev/Units/casualties.png"));
		Image originalImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);

		int type = ((image.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : image.getType());
		BufferedImage resizedImage = new BufferedImage(width, height, type);

		Graphics2D g2d = resizedImage.createGraphics();
		g2d.drawImage(originalImage, 0, 0, width, height, null);
		g2d.dispose();
		g2d.setComposite(AlphaComposite.Src);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ImageIO.write(resizedImage, fileName.split("\\.")[1], new File("c:/Java_Dev/casualties.png"));

	}

	public RescueSim() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		Cit1 = new ImageIcon("c:/Java_Dev/GridShit/humans/CIT2.png");
		Cit2 = new ImageIcon("c:/Java_Dev/GridShit/humans/CIT1.png");
		Main = ImageIO.read(new File("c:/Java_Dev/GridShit/Main.png"));
		BilCit = ImageIO.read(new File("c:/Java_Dev/GridShit/BilCit.png"));
		setTitle("RescueSimulation");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(50, 50, 1000, 1000);
		setIconImage(ImageIO.read(new File("c:/Java_Dev/Units/casualties.png")));

		GenralInfoPanel.add(HINT);
		RescuePanelMaker(RescuePanel);
		add(RescuePanel, BorderLayout.CENTER);

		UnitPanelMaker(UnitPanel);
		add(UnitPanel, BorderLayout.EAST);

		AvilableUnitPanelMaker(AvilableUnitPanel);
		RespondingUnitPanelMaker(RespondingUnitPanel);
		TreatingUnitPanelMaker(TreatingUnitPanel);

		AvilableUnitText.setText("AvilableUnitPanel");
		RespondingUnitText.setText("RespondingUnit");
		TreatingUnitText.setText("TreatingUnit");
		AvilableUnitText.setFont(new Font(Font.MONOSPACED, Font.BOLD, 21));
		RespondingUnitText.setFont(new Font(Font.MONOSPACED, Font.BOLD, 21));
		TreatingUnitText.setFont(new Font(Font.MONOSPACED, Font.BOLD, 21));

		title1.setEditable(false);
		title2.setEditable(false);
		title1.setText("         InfoPanel");
		title2.setText("           LOGS");
		title1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 19));
		title2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 19));
		AvilableUnitText.setEditable(false);
		RespondingUnitText.setEditable(false);
		TreatingUnitText.setEditable(false);
		Casualties.setEditable(false);
		Casualties.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));

		LogsPanel.setEditable(false);
		TextInfoPanel.setEditable(false);
		UnitPanel.add(AvilableUnitText);
		UnitPanel.add(AvilableUnitPanel);
		UnitPanel.add(RespondingUnitText);
		UnitPanel.add(RespondingUnitPanel);
		UnitPanel.add(TreatingUnitText);
		UnitPanel.add(TreatingUnitPanel);

		
		
		
		StyledDocument doc = Casualties.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		// JButton NC= new JButton("NEXT CYCLE");
		resizeImage();
		JLabel KKKK = new JLabel(new ImageIcon("c:/Java_Dev/casualties.png"));
		// KKKK.set
		GenralInfoPanel.add(KKKK);
		GenralInfoPanel.add(Casualties);
		
		// GenralInfoPanel.add(CC);
		// GenralInfoPanel.add(NC);
		InfoPanel.add(GenralInfoPanel);

		CitANDBilPanelMaker(CitANDBilPanel);
		CitANDBilPanel.setLayout(new BoxLayout(CitANDBilPanel, BoxLayout.X_AXIS));
		CitANDBilPanel.add(TextInfoPanel);
		InfoPanel.add(title1);
		InfoPanel.add(CitANDBilPanel);

		InfoPanelMaker(InfoPanel);
		// LogsPanelMaker(LogsPanel);
		scroll = new JScrollPane(LogsPanel);
		scroll.setPreferredSize(new Dimension(300, getHeight() / 2));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setMaximumSize(LogsPanel.getMaximumSize());
		scroll.setAutoscrolls(true);
		InfoPanel.add(title2);
		InfoPanel.add(scroll);

		add(InfoPanel, BorderLayout.WEST);
		GenralInfoPanel.setPreferredSize(new Dimension(300, getHeight() / 20));
		GenralInfoPanel.setLayout(new BoxLayout(GenralInfoPanel, BoxLayout.X_AXIS));

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);

	}

}