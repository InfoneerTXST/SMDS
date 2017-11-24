package smds;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

public class Window extends JFrame {
	
	private static final String UNSELECTED = " --";
	private static JFileChooser fc;
	private static int result;
	private static Window frame;
	private static boolean loading;
	private static JsonNode rn;
	private int screenWidth = 800;
	private int screenHeight = 500;
	private Node system;
	private Node part;
	private ArrayList<JsonNodeWrapper> symptoms;
	private ArrayList<CheckboxListItem> check_list;
	private Color highlight_color = new Color(235, 235, 235);
	private Color etched_color1 = Color.LIGHT_GRAY;
	private Color etched_color2 = new Color(242, 242, 242);
	private Color tab_color1 = new Color(235, 235, 235);
	private Color tab_color2 = new Color(205, 205, 205);
	private JTable table;
	private static ArrayList<Node> system_list;
	private static ArrayList<Node> part_list;
	String textfieldUPDATE = "";
	String textfield1UPDATE = "";
	String textfield2UPDATE = "";

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					loading = true;
					frame = new Window();
					frame.addWindowListener(new WindowAdapter() {
			            @Override
			            public void windowClosing(WindowEvent e) {
			            	if(loading)
			            		System.exit(0);
			            }
			            @Override
			            public void windowClosed(WindowEvent e) {
			            	if(loading)
			            		System.exit(0);
			            }
			        });
					frame.setResizable(false);
					frame.setUndecorated(true);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Window() {
		super();
		setLocationRelativeTo(null);
		loadPage();
	}
	
	public Window(String frame_title) {
		super(frame_title);
		
		system = null;
		part = null;
		symptoms = new ArrayList<JsonNodeWrapper>();
		check_list = new ArrayList<CheckboxListItem>();
		
		Icon folder_icon = new ImageIcon(getClass().getResource("/images/folder.png"));
		Icon file_icon = new ImageIcon(getClass().getResource("/images/file.png"));
		Icon folder_up_icon = new ImageIcon(getClass().getResource("/images/folder_up.png"));
		Icon home_icon = new ImageIcon(getClass().getResource("/images/home.png"));
		
		try {
			List<Image> icons = new ArrayList<Image>();
			//icons.add(new ImageIcon(getClass().getResource("/images/logo20.png")).getImage());
			//icons.add(new ImageIcon(getClass().getResource("/images/logo32.png")).getImage());
			super.setIconImages(icons);
		} catch (NullPointerException e) {}
		
		try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch(Exception e) { System.out.println("Error 1 setting Java LAF: " + e); }
		
		UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 12));
		UIManager.put("Label.foreground", Color.DARK_GRAY);
		UIManager.put("Label.disabledForeground", Color.DARK_GRAY);
		UIManager.put("Button.foreground", Color.DARK_GRAY);
		UIManager.put("ButtonUI", ButtonUI.class.getName());
        UIManager.put("List.foreground", Color.DARK_GRAY);
        UIManager.put("List.selectionForeground", Color.DARK_GRAY);
        UIManager.put("List.selectionBackground", highlight_color);
        UIManager.put("List.focusCellHighlightBorder", highlight_color);
        UIManager.put("ScrollBar.shadow", new Color(235, 235, 235));
        UIManager.put("ScrollBar.darkShadow", Color.GRAY);
        UIManager.put("TextField.border", BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        UIManager.put("TextField.selectionBackground", tab_color1);
        UIManager.put("TextField.foreground", Color.DARK_GRAY);
        UIManager.put("ComboBox.foreground", Color.DARK_GRAY);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.selectionForeground", Color.DARK_GRAY);
        UIManager.put("ComboBox.selectionBackground", new Color(235, 235, 235));
        UIManager.put("ComboBox.disabledForeground", Color.DARK_GRAY);
        UIManager.put("ComboBox.disabledBackground", tab_color1);
		UIManager.put("PopupMenuUI", PopupUI.class.getName());
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 12));
		UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 12));
		UIManager.put("Table.gridColor", tab_color2);
		UIManager.put("Table.foreground", Color.DARK_GRAY);
		UIManager.put("TableHeader.foreground", Color.DARK_GRAY);
		UIManager.put("TableHeader.cellBorder", BorderFactory.createMatteBorder(0, 0, 1, 1, tab_color2));
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
		UIManager.put("FileView.directoryIcon", folder_icon);
		UIManager.put("FileView.computerIcon", folder_icon);
		UIManager.put("FileView.floppyDriveIcon", folder_icon);
		UIManager.put("FileView.hardDriveIcon", folder_icon);
		UIManager.put("FileView.fileIcon", file_icon);
		UIManager.put("FileChooser.upFolderIcon", folder_up_icon);
		UIManager.put("FileChooser.homeFolderIcon", home_icon);
		
		try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch(Exception e) { System.out.println("Error 2 setting Java LAF: " + e); }
		
		ToolTipManager.sharedInstance().setEnabled(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		loading = false;
		contextPage();
	}
	
	private void configureFileChooserUI(Component[] components) {
		for(int i = 0; i < components.length; i++)
		{
			if(components[i] instanceof Container)
				configureFileChooserUI(((Container)components[i]).getComponents());
			if(components[i] instanceof JButton)
			{
				JButton jbutton = (JButton)components[i];
				jbutton.setFont(new Font("Arial", Font.PLAIN, 12));
			}
			if(components[i] instanceof JScrollPane)
			{
				JScrollPane jscroll = (JScrollPane)components[i];
				jscroll.setBorder(new LineBorder(Color.LIGHT_GRAY));
				jscroll.getVerticalScrollBar().setUnitIncrement(10);
				jscroll.getHorizontalScrollBar().setUnitIncrement(10);
				jscroll.getVerticalScrollBar().setUI(new ScrollUI(Color.WHITE));
				jscroll.getHorizontalScrollBar().setUI(new ScrollUI(Color.WHITE));
				jscroll.setOpaque(false);
			}
			if(components[i] instanceof JComboBox)
			{
				JComboBox jcombo = (JComboBox)components[i];
				jcombo.setFont(new Font("Arial", Font.PLAIN, 12));
				jcombo.setUI(new ComboUI(jcombo, false));
				jcombo.setBorder(new LineBorder(Color.LIGHT_GRAY));
				jcombo.setBackground(tab_color1);
				jcombo.setFocusable(false);
				jcombo.setEnabled(false);
			}
			if(components[i] instanceof JLabel)
				((JLabel)components[i]).setFont(new Font("Arial", Font.PLAIN, 12));
			if(components[i] instanceof JToggleButton)
				((JToggleButton)components[i]).setVisible(false);
			if(components[i] instanceof JList)
			{
				JList jlist = (JList)components[i];
				jlist.setFocusable(false);
				jlist.setFont(new Font("Arial", Font.PLAIN, 12));
			}
		}
	}
	
	public String getBroadestID(JsonNode rootNode, JsonNode j) {
		if(j.get("http://www.w3.org/2004/02/skos/core#broader") != null)
			return getBroadestID(rootNode, rootNode.get(j.get("http://www.w3.org/2004/02/skos/core#broader").get(0).get("value").asText()));
		else if(j.get("http://www.w3.org/2004/02/skos/core#topConceptOf") != null)
			return j.get("http://www.w3.org/2004/02/skos/core#topConceptOf").get(0).get("value").asText();
		return "";
	}
	
	public ArrayList<JsonNodeWrapper> getNarrowerList(ArrayList<JsonNodeWrapper> list, boolean recur_flag) {
		ArrayList<JsonNodeWrapper> list2 = new ArrayList<JsonNodeWrapper>();
		for(int x = 0; x < list.size(); x++) {
			if(list.get(x).getJsonNode().get("http://www.w3.org/2004/02/skos/core#narrower") != null) {
				ArrayList<JsonNodeWrapper> recur_arg = new ArrayList<JsonNodeWrapper>();
				for(int y = 0; y < list.get(x).getJsonNode().get("http://www.w3.org/2004/02/skos/core#narrower").size(); y++)
					recur_arg.add(new JsonNodeWrapper(rn.get(list.get(x).getJsonNode().get("http://www.w3.org/2004/02/skos/core#narrower").get(y).get("value").asText())));
				ArrayList<JsonNodeWrapper> recur_list = getNarrowerList(recur_arg, true);
        		for(int y = 0; y < recur_list.size(); y++)
        			list2.add(recur_list.get(y));
			}
			list2.add(list.get(x));
		}
		return list2;
	}
	
	public ArrayList<Node> createList(JsonNode rootNode, String categoryID, boolean recur_flag) {
		ArrayList<Node> list = new ArrayList<Node>();
		JsonNode categoryNode = rootNode.get(categoryID);
		for(int x = 0; x < categoryNode.get("http://www.w3.org/2004/02/skos/core#narrower").size(); x++) {
        	JsonNode n = categoryNode.get("http://www.w3.org/2004/02/skos/core#narrower").get(x);
        	String id = n.get("value").asText();
        	JsonNode sn = rootNode.get(id);
        	if(sn == null)
        		throw new NullPointerException();
        	if(sn.get("http://www.w3.org/2004/02/skos/core#narrower") != null) {
        		ArrayList<Node> recur_list = createList(rootNode, id, true);
        		for(int y = 0; y < recur_list.size(); y++)
        			list.add(recur_list.get(y));
        	}
        	else {
        		if(sn.get("http://www.w3.org/2004/02/skos/core#prefLabel") != null) {
        			String pref_label = sn.get("http://www.w3.org/2004/02/skos/core#prefLabel").get(0).get("value").asText();
        			ArrayList<String> alt_labels = new ArrayList<String>();
                	if(sn.get("http://www.w3.org/2004/02/skos/core#altLabel") != null) {
    	            	for(int y = 0; y < sn.get("http://www.w3.org/2004/02/skos/core#altLabel").size(); y++)
    	                	alt_labels.add(sn.get("http://www.w3.org/2004/02/skos/core#altLabel").get(y).get("value").asText());
                	}
                	ArrayList<JsonNodeWrapper> problems = new ArrayList<JsonNodeWrapper>();
                	ArrayList<JsonNodeWrapper> treatments = new ArrayList<JsonNodeWrapper>();
                	JsonNode pr = sn.get("http://www.w3.org/2004/02/skos/core#related");
                	if(pr != null) {
    	            	for(int z = 0; z < pr.size(); z++) {
    	            		String broadestID = getBroadestID(rootNode, rootNode.get(pr.get(z).get("value").asText()));
    	            		if(broadestID.equals("https://infoneer.poolparty.biz/MaintenanceDiagnosisThesaurus/1"))
    	            			problems.add(new JsonNodeWrapper(rootNode.get(pr.get(z).get("value").asText())));
    	            		else if(broadestID.equals("https://infoneer.poolparty.biz/MaintenanceDiagnosisThesaurus/25"))
    	            			treatments.add(new JsonNodeWrapper(rootNode.get(pr.get(z).get("value").asText())));
    	            		else
    	            			System.out.println("Something went wrong.");
    	            	}
    	            	problems = getNarrowerList(problems, false);
    	            	treatments = getNarrowerList(treatments, false);
    	            	Collections.sort(problems);
    	            	Collections.sort(treatments);
                	}
                	list.add(new Node(categoryID, id, pref_label, alt_labels, problems, treatments));
        		}
        	}
        }	
		return list;
	}
	
	public void loadPage() {
		SwingWorker<?,?> worker = new SwingWorker<Void,Void>() {
			@Override
			protected Void doInBackground() {
				try {
					ObjectMapper mapper = new ObjectMapper();
		            //mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);                 
		            rn = mapper.readTree(getClass().getResource("/information/thesaurus.rj"));
		            system_list = new ArrayList<Node>();
		            system_list = createList(rn, "https://infoneer.poolparty.biz/MaintenanceDiagnosisThesaurus/7", false);
		            Collections.sort(system_list);
		            part_list = new ArrayList<Node>();
		            part_list = createList(rn, "https://infoneer.poolparty.biz/MaintenanceDiagnosisThesaurus/6", false);
		            Collections.sort(part_list);
					Thread.sleep(1500);
				} catch(Exception e1) {
					System.out.println(e1);
					System.exit(0);
				}
				return null;
			}
			
			@Override
			protected void done() {
				frame.dispose();
				frame = new Window("Smart Manufacturing Diagnosis System");
				frame.setResizable(false);
				frame.setUndecorated(false);
				frame.setVisible(true);
	        }
		};
	    worker.execute();

		getContentPane().removeAll();
		int loadWidth = 383, loadHeight = 279;
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel label = new JLabel(new ImageIcon(getClass().getResource("/images/loading.gif")));
		panel.add(label);
		setBounds(this.getX() + (this.getWidth() / 2) - (loadWidth / 2), this.getY() + (this.getHeight() / 2) - (loadHeight / 2), loadWidth, loadHeight);
		validate();
	}
	
	public void contextPage() {
		getContentPane().removeAll();
		symptoms.clear();
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{130, 400, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(5, 35, 5, 0);
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.fill = GridBagConstraints.VERTICAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		
		JLabel lblDefineDiagnosisContext = new JLabel("Define Diagnosis Context");
		lblDefineDiagnosisContext.setFont(new Font("Arial", Font.BOLD, 16));
		panel_1.add(lblDefineDiagnosisContext);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(etched_color2);
		separator.setForeground(etched_color1);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		panel.add(separator, gbc_separator);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.WEST;
		gbc_panel_2.insets = new Insets(15, 35, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.VERTICAL;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		panel.add(panel_2, gbc_panel_2);
		
		JLabel lblSelectSystem = new JLabel("Select system:");
		lblSelectSystem.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_2.add(lblSelectSystem);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(15, 0, 5, 5);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 2;
		panel.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{630, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0};
		gbl_panel_4.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setUI(new ComboUI(comboBox, true));
		comboBox.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBox.setForeground(Color.DARK_GRAY);
		comboBox.setEditable(true);
		comboBox.setBackground(Color.WHITE);
		comboBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		comboBox.setRenderer(new PromptComboBoxRenderer(UNSELECTED));
		for(int x = 0; x < system_list.size(); x++)
			comboBox.insertItemAt(system_list.get(x), x);
		comboBox.setMaximumRowCount(8);
		comboBox.setSelectedItem(system);
		final JTextField textfield = (JTextField)comboBox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if(!textfield.getText().isEmpty()) {
                        	String enteredText = textfield.getText();
                        	textfieldUPDATE = enteredText;
                        	comboBox.hidePopup();
                            List<Node> filterArray = new ArrayList<Node>();
                            for(int i = 0; i < system_list.size(); i++) {
                            	boolean matched = false;
                                if(system_list.get(i).toString().toLowerCase().contains(enteredText.toLowerCase())) {
                                	filterArray.add(system_list.get(i));
                                	matched = true;
                                	if(system_list.get(i).toString().toLowerCase().equals(enteredText.toLowerCase())) {
                                		comboBox.setSelectedItem(system_list.get(i));
                                		comboBox.removeAllItems();
                                    	for(int x = 0; x < system_list.size(); x++)
                                			comboBox.insertItemAt(system_list.get(x), x);
                                    	textfield.setText(enteredText);
                                		return;
                                	}
                                }
                                if(!matched) {
                                	for(int j = 0; j < system_list.get(i).getAltLabels().size(); j++) {
                                		if(system_list.get(i).getAltLabels().get(j).toString().toLowerCase().contains(enteredText.toLowerCase())) {
                                        	filterArray.add(system_list.get(i));
                                        	if(system_list.get(i).getAltLabels().get(j).toString().toLowerCase().equals(enteredText.toLowerCase())) {
                                        		comboBox.setSelectedItem(system_list.get(i));
                                        		comboBox.removeAllItems();
                                            	for(int x = 0; x < system_list.size(); x++)
                                        			comboBox.insertItemAt(system_list.get(x), x);
                                            	textfield.setText(system_list.get(i).getPrefLabel());
                                        		return;
                                        	}
                                        }
                                	}
                                }
                            }
                            comboBox.removeAllItems();
                            if(filterArray.size() > 0)
                                for(int i = 0; i < filterArray.size(); i++)
                                    comboBox.insertItemAt(filterArray.get(i), i);
                            textfield.setText(enteredText);
                        }
                        else {
                        	comboBox.hidePopup();
                        	comboBox.removeAllItems();
                        	for(int x = 0; x < system_list.size(); x++)
                    			comboBox.insertItemAt(system_list.get(x), x);
                        	textfieldUPDATE = "";
                        }
                        
                        if(textfieldUPDATE.length() < 2) {
                        	comboBox.removeAllItems();
                            for(int i = 0; i < system_list.size(); i++)
                            	comboBox.insertItemAt(system_list.get(i), i);
                            textfield.setText(textfieldUPDATE);
                        }
                        else
                        	comboBox.showPopup();
                    }
                });
            }
        });
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					system = (Node)comboBox.getSelectedItem();
					system.toString();
					comboBox.removeAllItems();
                	for(int x = 0; x < system_list.size(); x++)
            			comboBox.insertItemAt(system_list.get(x), x);
                	comboBox.setSelectedItem(system);
				} catch(Exception e) {
					system = null;
					if(textfieldUPDATE.isEmpty())
	                	comboBox.removeAllItems();
					if(!textfield.hasFocus()) {
						comboBox.removeAllItems();
						for(int x = 0; x < system_list.size(); x++)
	            			comboBox.insertItemAt(system_list.get(x), x);
					}
				}
			}
		});
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 0, 75);
		gbc_comboBox.fill = GridBagConstraints.BOTH;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		panel_4.add(comboBox, gbc_comboBox);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.anchor = GridBagConstraints.WEST;
		gbc_panel_3.insets = new Insets(15, 35, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.VERTICAL;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 3;
		panel.add(panel_3, gbc_panel_3);
		
		JLabel lblSelectPart = new JLabel("Select part:");
		lblSelectPart.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_3.add(lblSelectPart);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.insets = new Insets(15, 0, 5, 5);
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 1;
		gbc_panel_5.gridy = 3;
		panel.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setUI(new ComboUI(comboBox_1, true));
		comboBox_1.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBox_1.setEditable(true);
		comboBox_1.setForeground(Color.DARK_GRAY);
		comboBox_1.setBackground(Color.WHITE);
		comboBox_1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		comboBox_1.setRenderer(new PromptComboBoxRenderer(UNSELECTED));
		for(int x = 0; x < part_list.size(); x++)
			comboBox_1.insertItemAt(part_list.get(x), x);
		comboBox_1.setMaximumRowCount(8);
		comboBox_1.setSelectedItem(part);
		final JTextField textfield1 = (JTextField)comboBox_1.getEditor().getEditorComponent();
        textfield1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if(!textfield1.getText().isEmpty()) {
                        	String enteredText = textfield1.getText();
                        	textfield1UPDATE = enteredText;
                        	comboBox_1.hidePopup();
                            List<Node> filterArray = new ArrayList<Node>();
                            for(int i = 0; i < part_list.size(); i++) {
                            	boolean matched = false;
                                if(part_list.get(i).toString().toLowerCase().contains(enteredText.toLowerCase())) {
                                	filterArray.add(part_list.get(i));
                                	matched = true;
                                	if(part_list.get(i).toString().toLowerCase().equals(enteredText.toLowerCase())) {
                                		comboBox_1.setSelectedItem(part_list.get(i));
                                		comboBox_1.removeAllItems();
                                    	for(int x = 0; x < part_list.size(); x++)
                                			comboBox_1.insertItemAt(part_list.get(x), x);
                                    	textfield1.setText(enteredText);
                                		return;
                                	}
                                }
                                if(!matched) {
                                	for(int j = 0; j < part_list.get(i).getAltLabels().size(); j++) {
                                		if(part_list.get(i).getAltLabels().get(j).toString().toLowerCase().contains(enteredText.toLowerCase())) {
                                        	filterArray.add(part_list.get(i));
                                        	if(part_list.get(i).getAltLabels().get(j).toString().toLowerCase().equals(enteredText.toLowerCase())) {
                                        		comboBox_1.setSelectedItem(part_list.get(i));
                                        		comboBox_1.removeAllItems();
                                            	for(int x = 0; x < part_list.size(); x++)
                                        			comboBox_1.insertItemAt(part_list.get(x), x);
                                            	textfield1.setText(part_list.get(i).getPrefLabel());
                                        		return;
                                        	}
                                        }
                                	}
                                }
                            }
                            comboBox_1.removeAllItems();
                            if(filterArray.size() > 0)
                            	for(int i = 0; i < filterArray.size(); i++)
                            		comboBox_1.insertItemAt(filterArray.get(i), i);
                            textfield1.setText(enteredText);
                        }
                        else {
                        	comboBox_1.hidePopup();
                        	comboBox_1.removeAllItems();
                        	for(int x = 0; x < part_list.size(); x++)
                    			comboBox_1.insertItemAt(part_list.get(x), x);
                        	textfield1UPDATE = "";
                        }
                        
                        if(textfield1UPDATE.length() < 2) {
                        	comboBox_1.removeAllItems();
                            for(int i = 0; i < part_list.size(); i++)
                            	comboBox_1.insertItemAt(part_list.get(i), i);
                            textfield1.setText(textfield1UPDATE);
                        }
                        else
                        	comboBox_1.showPopup();
                    }
                });
            }
        });
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					part = (Node)comboBox_1.getSelectedItem();
					part.toString();
					comboBox_1.removeAllItems();
                	for(int x = 0; x < part_list.size(); x++)
            			comboBox_1.insertItemAt(part_list.get(x), x);
                	comboBox_1.setSelectedItem(part);
				} catch(Exception e) {
					part = null;
					if(textfield1UPDATE.isEmpty())
	                	comboBox_1.removeAllItems();
					if(!textfield1.hasFocus()) {
						comboBox_1.removeAllItems();
						for(int x = 0; x < part_list.size(); x++)
	            			comboBox_1.insertItemAt(part_list.get(x), x);
					}
				}
			}
		});
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 0, 75);
		gbc_comboBox_1.fill = GridBagConstraints.BOTH;
		gbc_comboBox_1.gridx = 0;
		gbc_comboBox_1.gridy = 0;
		panel_5.add(comboBox_1, gbc_comboBox_1);
		
		JPanel panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.gridwidth = 2;
		gbc_panel_6.insets = new Insets(15, 0, 0, 0);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 4;
		panel.add(panel_6, gbc_panel_6);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
				return;
			}
		});
		panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		btnExit.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_6.add(btnExit);
		
		JButton btnSwitch = new JButton("Change Thesaurus");
		btnSwitch.setPreferredSize(new Dimension(150, 27));
		btnSwitch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
						
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						fc = new JFileChooser("Desktop") {
							@Override
							public void approveSelection() {
								File f = getSelectedFile();
									if(f.exists()) {
										super.approveSelection();
								    }
								}
							};
								
							FileNameExtensionFilter RJ_filter = new FileNameExtensionFilter("RDF/JSON (*.rj)", "rj");
							fc.setFileFilter(RJ_filter);
							fc.setFocusable(false);
							fc.setDialogTitle("Select Thesaurus");
							fc.setMultiSelectionEnabled(false);
							fc.setCurrentDirectory(new File("\\"));
							fc.changeToParentDirectory();
							JComboBox open_combo = (JComboBox)(fc.getComponent(0).getAccessibleContext().getAccessibleChild(2));
							open_combo.setFont(new Font("Arial", Font.PLAIN, 12));
							open_combo.setUI(new ComboUI(open_combo, true));
							open_combo.setBorder(new LineBorder(Color.GRAY));
							open_combo.setBackground(Color.WHITE);
							configureFileChooserUI(fc.getComponents());
								
							result = fc.showOpenDialog(frame);
							if(result == 0)
							{
								File selected_file = fc.getSelectedFile();
								if(selected_file.exists())
								{
									ObjectMapper mapper = new ObjectMapper();
									ArrayList<Node> s_l = new ArrayList<Node>();
									ArrayList<Node> p_l = new ArrayList<Node>();
									comboBox.setSelectedIndex(0);
						            comboBox.setSelectedItem(-1);
						            comboBox_1.setSelectedIndex(0);
						            comboBox_1.setSelectedItem(-1);
									try {
										rn = mapper.readTree(selected_file);
										s_l = createList(rn, "https://infoneer.poolparty.biz/MaintenanceDiagnosisThesaurus/7", false);
										Collections.sort(s_l);
										p_l = createList(rn, "https://infoneer.poolparty.biz/MaintenanceDiagnosisThesaurus/6", false);
										Collections.sort(p_l);
									} catch (Exception e) {
										JOptionPane.showMessageDialog(frame, new JLabel("Error changing thesaurus.", JLabel.CENTER), "Notice", JOptionPane.PLAIN_MESSAGE, null);
										return;
									}
									system = null;
									part = null;
									system_list = new ArrayList<Node>();
									part_list = new ArrayList<Node>();
									for(Node n : s_l)
										system_list.add(n);
									for(Node n : p_l)
										part_list.add(n);
									comboBox.setSelectedIndex(0);
						            comboBox.setSelectedItem(-1);
						            comboBox_1.setSelectedIndex(0);
						            comboBox_1.setSelectedItem(-1);
						            JOptionPane.showMessageDialog(frame, new JLabel("Thesaurus changed successfully.", JLabel.CENTER), "Notice", JOptionPane.PLAIN_MESSAGE, null);
								}
							}
						}
					});	
				
			}
		});
		btnSwitch.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_6.add(btnSwitch);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(system != null && part != null)
					symptomsPage();
				else
					JOptionPane.showMessageDialog(frame, new JLabel("Please select a system and a part.", JLabel.CENTER), "Notice", JOptionPane.PLAIN_MESSAGE, null);
				return;
			}
		});
		btnNext.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_6.add(btnNext);
		
		setBounds(this.getX() + (this.getWidth() / 2) - (screenWidth / 2), this.getY() + (this.getHeight() / 2) - (screenHeight / 2), screenWidth, screenHeight);
		validate();
	}
	
	public void symptomsPage() {
		getContentPane().removeAll();
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{170, 400, 0};
		gbl_panel.rowHeights = new int[]{0, 7, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(5, 35, 5, 0);
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.fill = GridBagConstraints.VERTICAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		
		JLabel lblDefineDiagnosisContext = new JLabel("Symptoms");
		lblDefineDiagnosisContext.setFont(new Font("Arial", Font.BOLD, 16));
		panel_1.add(lblDefineDiagnosisContext);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(etched_color2);
		separator.setForeground(etched_color1);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.gridwidth = 2;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		panel.add(separator, gbc_separator);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.WEST;
		gbc_panel_2.insets = new Insets(15, 35, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.VERTICAL;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		panel.add(panel_2, gbc_panel_2);
		
		JLabel lblSelectSystem = new JLabel("Select symptoms:");
		lblSelectSystem.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_2.add(lblSelectSystem);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(15, 0, 5, 0);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 2;
		panel.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{469, 0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0};
		gbl_panel_4.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.insets = new Insets(0, 0, 0, 5);
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 0;
		panel_4.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{450, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.BOTH;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		panel_5.add(comboBox, gbc_comboBox);
		comboBox.setUI(new ComboUI(comboBox, true));
		comboBox.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBox.setForeground(Color.DARK_GRAY);
		comboBox.setBackground(Color.WHITE);
		comboBox.setEditable(true);
		comboBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		comboBox.setRenderer(new PromptComboBoxRenderer(UNSELECTED));
		ArrayList<JsonNodeWrapper> probs = part.getProblems();
		for(int x = 0; x < probs.size(); x++)
			comboBox.insertItemAt(probs.get(x), x);
		comboBox.setMaximumRowCount(8);
		comboBox.setSelectedIndex(-1);
		final JTextField textfield2 = (JTextField)comboBox.getEditor().getEditorComponent();
        textfield2.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if(!textfield2.getText().isEmpty()) {
                        	String enteredText = textfield2.getText();
                        	textfield2UPDATE = enteredText;
                        	comboBox.hidePopup();
                            List<JsonNodeWrapper> filterArray = new ArrayList<JsonNodeWrapper>();
                            for(int i = 0; i < probs.size(); i++) {
                            	boolean matched = false;
                                if(probs.get(i).toString().toLowerCase().contains(enteredText.toLowerCase())) {
                                	filterArray.add(probs.get(i));
                                	matched = true;
                                	if(probs.get(i).toString().toLowerCase().equals(enteredText.toLowerCase())) {
                                		comboBox.setSelectedItem(probs.get(i));
                                		comboBox.removeAllItems();
                                    	for(int x = 0; x < probs.size(); x++)
                                			comboBox.insertItemAt(probs.get(x), x);
                                    	textfield2.setText(enteredText);
                                		return;
                                	}
                                }
                                if(!matched && probs.get(i).getJsonNode().get("http://www.w3.org/2004/02/skos/core#altLabel") != null) {
                                	for(int j = 0; j < probs.get(i).getJsonNode().get("http://www.w3.org/2004/02/skos/core#altLabel").size(); j++) {
                                		if(probs.get(i).getJsonNode().get("http://www.w3.org/2004/02/skos/core#altLabel").get(j).get("value").asText().toLowerCase().contains(enteredText.toLowerCase())) {
                                        	filterArray.add(probs.get(i));
                                        	if(probs.get(i).getJsonNode().get("http://www.w3.org/2004/02/skos/core#altLabel").get(j).get("value").asText().toLowerCase().equals(enteredText.toLowerCase())) {
                                        		comboBox.setSelectedItem(probs.get(i));
                                        		comboBox.removeAllItems();
                                            	for(int x = 0; x < probs.size(); x++)
                                        			comboBox.insertItemAt(probs.get(x), x);
                                            	textfield2.setText(probs.get(i).toString());
                                        		return;
                                        	}
                                        }
                                	}
                                	
                                }
                            }
                            comboBox.removeAllItems();
                            if(filterArray.size() > 0)
                                for(int i = 0; i < filterArray.size(); i++)
                                    comboBox.insertItemAt(filterArray.get(i), i);
                            textfield2.setText(enteredText);
                        }
                        else {
                        	comboBox.hidePopup();
                        	comboBox.removeAllItems();
                        	for(int x = 0; x < probs.size(); x++)
                    			comboBox.insertItemAt(probs.get(x), x);
                        	textfield2UPDATE = "";
                        }
                        
                        if(textfield2UPDATE.length() < 2) {
                        	comboBox.removeAllItems();
                            for(int i = 0; i < probs.size(); i++)
                            	comboBox.insertItemAt(probs.get(i), i);
                            textfield2.setText(textfield2UPDATE);
                        }
                        else
                        	comboBox.showPopup();
                    }
                });
            }
        });
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JsonNodeWrapper j = (JsonNodeWrapper)comboBox.getSelectedItem();
					j.toString();
					comboBox.removeAllItems();
                	for(int x = 0; x < probs.size(); x++)
            			comboBox.insertItemAt(probs.get(x), x);
                	comboBox.setSelectedItem(j);
				} catch(Exception e) {
					//j = null;
					if(textfield2UPDATE.isEmpty())
	                	comboBox.removeAllItems();
					if(!textfield2.hasFocus()) {
						comboBox.removeAllItems();
						for(int x = 0; x < probs.size(); x++)
	            			comboBox.insertItemAt(probs.get(x), x);
					}
				}
			}
		});
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 0;
		panel_4.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Arial", Font.PLAIN, 14));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.BOTH;
		gbc_btnAdd.insets = new Insets(0, 10, 0, 75);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 0;
		panel_3.add(btnAdd, gbc_btnAdd);
		
		JPanel panel_9 = new JPanel();
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.anchor = GridBagConstraints.WEST;
		gbc_panel_9.insets = new Insets(15, 35, 5, 5);
		gbc_panel_9.fill = GridBagConstraints.VERTICAL;
		gbc_panel_9.gridx = 0;
		gbc_panel_9.gridy = 3;
		panel.add(panel_9, gbc_panel_9);
		
		JLabel lblAddedSymptoms = new JLabel("Added symptoms:");
		lblAddedSymptoms.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_9.add(lblAddedSymptoms);
		
		JPanel panel_10 = new JPanel();
		GridBagConstraints gbc_panel_10 = new GridBagConstraints();
		gbc_panel_10.insets = new Insets(15, 0, 5, 0);
		gbc_panel_10.fill = GridBagConstraints.BOTH;
		gbc_panel_10.gridx = 1;
		gbc_panel_10.gridy = 3;
		panel.add(panel_10, gbc_panel_10);
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[]{273, 117, 0};
		gbl_panel_10.rowHeights = new int[]{128, 0};
		gbl_panel_10.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_10.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_10.setLayout(gbl_panel_10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane.getHorizontalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane.setOpaque(false);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_10.add(scrollPane, gbc_scrollPane);
		
		DefaultListModel<JsonNodeWrapper> list_model = new DefaultListModel<JsonNodeWrapper>();
		for(int x = 0; x < symptoms.size(); x++)
			list_model.addElement(symptoms.get(x));
		JList<JsonNodeWrapper> list = new JList<JsonNodeWrapper>(list_model);
		list.setFont(new Font("Arial", Font.PLAIN, 12));
		list.setVisibleRowCount(6);
		list.setFocusable(false);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedItem() == null)
					return;
				JsonNodeWrapper selected_symptom = (JsonNodeWrapper)comboBox.getSelectedItem();
				if(!list_model.contains(selected_symptom)) {
					list_model.addElement(selected_symptom);
					symptoms.add(selected_symptom);
					comboBox.setSelectedItem(-1);
				}
				scrollPane.validate();
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
			}
		});
		
		JPanel panel_11 = new JPanel();
		GridBagConstraints gbc_panel_11 = new GridBagConstraints();
		gbc_panel_11.fill = GridBagConstraints.BOTH;
		gbc_panel_11.gridx = 1;
		gbc_panel_11.gridy = 0;
		panel_10.add(panel_11, gbc_panel_11);
		GridBagLayout gbl_panel_11 = new GridBagLayout();
		gbl_panel_11.columnWidths = new int[]{71, 0};
		gbl_panel_11.rowHeights = new int[]{23, 0};
		gbl_panel_11.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_11.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_11.setLayout(gbl_panel_11);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.setFont(new Font("Arial", Font.PLAIN, 14));
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.insets = new Insets(0, 10, 0, 75);
		gbc_btnRemove.fill = GridBagConstraints.BOTH;
		gbc_btnRemove.gridx = 0;
		gbc_btnRemove.gridy = 0;
		panel_11.add(btnRemove, gbc_btnRemove);
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected_index = list.getSelectedIndex();
				if(selected_index != -1) {
					list_model.remove(selected_index);
					symptoms.remove(selected_index);
				}
			}
		});
		
		JPanel panel_7 = new JPanel();
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.anchor = GridBagConstraints.WEST;
		gbc_panel_7.insets = new Insets(0, 0, 5, 0);
		gbc_panel_7.fill = GridBagConstraints.VERTICAL;
		gbc_panel_7.gridx = 1;
		gbc_panel_7.gridy = 4;
		panel.add(panel_7, gbc_panel_7);
		
		JLabel lblAffectSystemExample = new JLabel("Affected system: " + system);
		lblAffectSystemExample.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_7.add(lblAffectSystemExample);
		
		JPanel panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.anchor = GridBagConstraints.WEST;
		gbc_panel_8.insets = new Insets(0, 0, 5, 0);
		gbc_panel_8.fill = GridBagConstraints.VERTICAL;
		gbc_panel_8.gridx = 1;
		gbc_panel_8.gridy = 5;
		panel.add(panel_8, gbc_panel_8);
		
		JLabel lblAffectedPartExample = new JLabel("Affected part: " + part);
		lblAffectedPartExample.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_8.add(lblAffectedPartExample);
		
		JPanel panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.gridwidth = 2;
		gbc_panel_6.insets = new Insets(15, 0, 0, 0);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 6;
		panel.add(panel_6, gbc_panel_6);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				check_list.clear();
				for(int x = 0; x < symptoms.size(); x++) {
					if(symptoms.get(x).getJsonNode().get("http://www.w3.org/2004/02/skos/core#related") != null) {
						for(int y = 0; y < symptoms.get(x).getJsonNode().get("http://www.w3.org/2004/02/skos/core#related").size(); y++) {
							JsonNodeWrapper rel = new JsonNodeWrapper(rn.get(symptoms.get(x).getJsonNode().get("http://www.w3.org/2004/02/skos/core#related").get(y).get("value").asText()));
							boolean exists = false;
							for(int z = 0; z < check_list.size(); z++) { // check if already in cause list
								if(check_list.get(z).toString().equals(rel.toString())) {
									exists = true;
									z = check_list.size();
								}
							}
							if(!exists) {
								if(getBroadestID(rn, rel.getJsonNode()).equals("https://infoneer.poolparty.biz/MaintenanceDiagnosisThesaurus/1")) { // check if related concept is a problem (as opposed to say another part)
									boolean exists2 = false; // check if not one of the selected symptoms
									for(int a = 0; a < symptoms.size(); a++) {
										if(symptoms.get(a).getJsonNode().toString().equals(rel.getJsonNode().toString())) {
											exists2 = true;
											a = symptoms.size();
										}
									}
									if(!exists2)
										check_list.add(new CheckboxListItem(rel));
								}
							}
						}
					}
				}
				if(!list_model.isEmpty() && check_list.size() != 0)
					causesPage(check_list);
				else if(!list_model.isEmpty()) {
					Object[] opt = {"Cancel", "Skip"};
					result = JOptionPane.showOptionDialog(frame, new JLabel("No causes could be found.", JLabel.CENTER), "Notice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, opt, opt[0]);
					if(result == 1) {
						for(JsonNodeWrapper n : symptoms)
							check_list.add(new CheckboxListItem(n));
						ArrayList<Object[]> treatments_table = new ArrayList<Object[]>();
						for(int r = 0; r < check_list.size(); r++) {
							if(check_list.get(r).getJsonNodeWrapper().getJsonNode().get("http://www.w3.org/2004/02/skos/core#related") != null) {
								ArrayList<String> added_treatments = new ArrayList<String>();
								for(int y = 0; y < check_list.get(r).getJsonNodeWrapper().getJsonNode().get("http://www.w3.org/2004/02/skos/core#related").size(); y++) {
									JsonNodeWrapper rel = new JsonNodeWrapper(rn.get(check_list.get(r).getJsonNodeWrapper().getJsonNode().get("http://www.w3.org/2004/02/skos/core#related").get(y).get("value").asText()));
									boolean exists = false;
									for(int z = 0; z < added_treatments.size(); z++) {
										if(added_treatments.get(z).equals(rel.toString())) {
											exists = true;
											z = check_list.size();
										}
									}
									if(!exists) {
										if(getBroadestID(rn, rel.getJsonNode()).equals("https://infoneer.poolparty.biz/MaintenanceDiagnosisThesaurus/25")) {
											String definition = "";
											if(rel.getJsonNode().get("http://www.w3.org/2004/02/skos/core#definition") != null)
												definition = rel.getJsonNode().get("http://www.w3.org/2004/02/skos/core#definition").get(0).get("value").asText();
											treatments_table.add(new Object[]{check_list.get(r).toString(), rel.toString(), definition});
										}
									}
								}
							}
						}
						if(treatments_table.size() == 0)
							JOptionPane.showMessageDialog(frame, new JLabel("No treatments could be found.", JLabel.CENTER), "Notice", JOptionPane.PLAIN_MESSAGE, null);
						else
							treatmentsPage(check_list, treatments_table, true);
					}
				}
				else
					JOptionPane.showMessageDialog(frame, new JLabel("Please add at least one symptom.", JLabel.CENTER), "Notice", JOptionPane.PLAIN_MESSAGE, null);
				return;
			}
		});
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				contextPage();
				return;
			}
		});
		btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_6.add(btnBack);
		btnNext.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_6.add(btnNext);
		
		setBounds(this.getX() + (this.getWidth() / 2) - (screenWidth / 2), this.getY() + (this.getHeight() / 2) - (screenHeight / 2), screenWidth, screenHeight);
		validate();
	}
	
	public void causesPage(ArrayList<CheckboxListItem> check_list) {
		getContentPane().removeAll();
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{400, 530, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 259, 47, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(5, 35, 5, 0);
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.fill = GridBagConstraints.VERTICAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		
		JLabel lblDefineDiagnosisContext = new JLabel("Causes");
		lblDefineDiagnosisContext.setFont(new Font("Arial", Font.BOLD, 16));
		panel_1.add(lblDefineDiagnosisContext);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(etched_color2);
		separator.setForeground(etched_color1);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		panel.add(separator, gbc_separator);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		panel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(Color.LIGHT_GRAY));
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(15, 20, 0, 0);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 0;
		panel_2.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0};
		gbl_panel_4.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.insets = new Insets(20, 16, 20, 20);
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 0;
		panel_4.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		JPanel panel_10 = new JPanel();
		GridBagConstraints gbc_panel_10 = new GridBagConstraints();
		gbc_panel_10.anchor = GridBagConstraints.WEST;
		gbc_panel_10.fill = GridBagConstraints.VERTICAL;
		gbc_panel_10.gridx = 0;
		gbc_panel_10.gridy = 0;
		panel_5.add(panel_10, gbc_panel_10);
		
		JLabel lblSymptoms = new JLabel("Symptoms:");
		lblSymptoms.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_10.add(lblSymptoms);
		
		JPanel panel_9 = new JPanel();
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.insets = new Insets(0, 4, 5, 0);
		gbc_panel_9.fill = GridBagConstraints.BOTH;
		gbc_panel_9.gridx = 0;
		gbc_panel_9.gridy = 1;
		panel_5.add(panel_9, gbc_panel_9);
		GridBagLayout gbl_panel_9 = new GridBagLayout();
		gbl_panel_9.columnWidths = new int[]{82, 0};
		gbl_panel_9.rowHeights = new int[]{98, 0};
		gbl_panel_9.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_9.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_9.setLayout(gbl_panel_9);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane.getHorizontalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane.setOpaque(false);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridy = 0;
		gbc_scrollPane.gridx = 0;
		panel_9.add(scrollPane, gbc_scrollPane);
		DefaultListModel<JsonNodeWrapper> list_model = new DefaultListModel<JsonNodeWrapper>();
		for(int x = 0; x < symptoms.size(); x++)
			list_model.addElement(symptoms.get(x));
		JList<JsonNodeWrapper> list = new JList<JsonNodeWrapper>(list_model);
		list.setFont(new Font("Arial", Font.PLAIN, 12));
		list.setVisibleRowCount(6);
		list.setFocusable(false);
		list.setEnabled(false);
		scrollPane.setViewportView(list);
		
		JPanel panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.anchor = GridBagConstraints.WEST;
		gbc_panel_8.insets = new Insets(0, 0, 5, 0);
		gbc_panel_8.fill = GridBagConstraints.VERTICAL;
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 2;
		panel_5.add(panel_8, gbc_panel_8);
		
		JLabel lblAffectedSystemExample = new JLabel("Affected system: " + system);
		lblAffectedSystemExample.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_8.add(lblAffectedSystemExample);
		
		JPanel panel_7 = new JPanel();
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.anchor = GridBagConstraints.WEST;
		gbc_panel_7.fill = GridBagConstraints.VERTICAL;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 3;
		panel_5.add(panel_7, gbc_panel_7);
		
		JLabel lblAffectedPartExample_1 = new JLabel("Affected part: " + part);
		lblAffectedPartExample_1.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_7.add(lblAffectedPartExample_1);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(10, 10, 5, 20);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 2;
		panel.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JPanel panel_12 = new JPanel();
		GridBagConstraints gbc_panel_12 = new GridBagConstraints();
		gbc_panel_12.anchor = GridBagConstraints.WEST;
		gbc_panel_12.insets = new Insets(0, 0, 5, 0);
		gbc_panel_12.fill = GridBagConstraints.VERTICAL;
		gbc_panel_12.gridx = 0;
		gbc_panel_12.gridy = 0;
		panel_3.add(panel_12, gbc_panel_12);
		
		JLabel lblPotentialCauses = new JLabel("Potential causes:");
		lblPotentialCauses.setFont(new Font("Arial", Font.PLAIN, 15));
		panel_12.add(lblPotentialCauses);
		
		JPanel panel_11 = new JPanel();
		GridBagConstraints gbc_panel_11 = new GridBagConstraints();
		gbc_panel_11.fill = GridBagConstraints.BOTH;
		gbc_panel_11.gridx = 0;
		gbc_panel_11.gridy = 1;
		panel_3.add(panel_11, gbc_panel_11);
		GridBagLayout gbl_panel_11 = new GridBagLayout();
		gbl_panel_11.columnWidths = new int[]{0, 0};
		gbl_panel_11.rowHeights = new int[]{0, 0};
		gbl_panel_11.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_11.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_11.setLayout(gbl_panel_11);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		scrollPane_1.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane_1.getHorizontalScrollBar().setUnitIncrement(10);
		scrollPane_1.getVerticalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane_1.getHorizontalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane_1.setOpaque(false);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		panel_11.add(scrollPane_1, gbc_scrollPane_1);
		DefaultListModel<CheckboxListItem> list_model_1 = new DefaultListModel<CheckboxListItem>();
		for(int x = 0; x < check_list.size(); x++)
			list_model_1.addElement(check_list.get(x));
		JList<CheckboxListItem> list_1 = new JList<CheckboxListItem>(list_model_1);
		list_1.setCellRenderer(new CheckboxListRenderer());
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_1.setFont(new Font("Arial", Font.PLAIN, 12));
		list_1.setVisibleRowCount(6);
		list_1.setFocusable(false);
		list_1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				JList<CheckboxListItem> list_1a = (JList<CheckboxListItem>)event.getSource();
				int index = list_1a.locationToIndex(event.getPoint());
		        CheckboxListItem item = (CheckboxListItem)list_1a.getModel().getElementAt(index);
	            item.setSelected(!item.isSelected());
	            list_1a.repaint(list_1a.getCellBounds(index, index));
			}
		});
		scrollPane_1.setViewportView(list_1);
		
		JPanel panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.gridwidth = 2;
		gbc_panel_6.insets = new Insets(16, 0, 0, 0);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 3;
		panel.add(panel_6, gbc_panel_6);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				symptomsPage();
				return;
			}
		});
		btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_6.add(btnBack);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean one_check = false;
				for(int x = 0; x < list_model_1.getSize(); x++)
					if(list_model_1.get(x).isSelected()) {
						one_check = true;
						x = list_model_1.getSize();
					}
				if(one_check) {
					ArrayList<Object[]> treatments_table = new ArrayList<Object[]>();
					for(int r = 0; r < check_list.size(); r++) {
						if(check_list.get(r).getJsonNodeWrapper().getJsonNode().get("http://www.w3.org/2004/02/skos/core#related") != null && check_list.get(r).isSelected()) {
							ArrayList<String> added_treatments = new ArrayList<String>();
							for(int y = 0; y < check_list.get(r).getJsonNodeWrapper().getJsonNode().get("http://www.w3.org/2004/02/skos/core#related").size(); y++) {
								JsonNodeWrapper rel = new JsonNodeWrapper(rn.get(check_list.get(r).getJsonNodeWrapper().getJsonNode().get("http://www.w3.org/2004/02/skos/core#related").get(y).get("value").asText()));
								boolean exists = false;
								for(int z = 0; z < added_treatments.size(); z++) {
									if(added_treatments.get(z).equals(rel.toString())) {
										exists = true;
										z = check_list.size();
									}
								}
								if(!exists) {
									if(getBroadestID(rn, rel.getJsonNode()).equals("https://infoneer.poolparty.biz/MaintenanceDiagnosisThesaurus/25")) {
										String definition = "";
										if(rel.getJsonNode().get("http://www.w3.org/2004/02/skos/core#definition") != null)
											definition = rel.getJsonNode().get("http://www.w3.org/2004/02/skos/core#definition").get(0).get("value").asText();
										treatments_table.add(new Object[]{check_list.get(r).toString(), rel.toString(), definition});
									}
								}
							}
						}
					}
					if(treatments_table.size() == 0)
						JOptionPane.showMessageDialog(frame, new JLabel("No treatments could be found.", JLabel.CENTER), "Notice", JOptionPane.PLAIN_MESSAGE, null);
					else
						treatmentsPage(check_list, treatments_table, false);
				}
				else
					JOptionPane.showMessageDialog(frame, new JLabel("At least one cause must be checked.", JLabel.CENTER), "Notice", JOptionPane.PLAIN_MESSAGE, null);
				return;
			}
		});
		btnNext.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_6.add(btnNext);
		
		setBounds(this.getX() + (this.getWidth() / 2) - (screenWidth / 2), this.getY() + (this.getHeight() / 2) - (screenHeight / 2), screenWidth, screenHeight);
		validate();
	}
	
	public void treatmentsPage(ArrayList<CheckboxListItem> check_list, ArrayList<Object[]> treatments_table, boolean skipped) {
		getContentPane().removeAll();
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{400, 530, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 259, 47, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(5, 35, 5, 0);
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.fill = GridBagConstraints.VERTICAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		
		JLabel lblDefineDiagnosisContext = new JLabel("Treatments");
		lblDefineDiagnosisContext.setFont(new Font("Arial", Font.BOLD, 16));
		panel_1.add(lblDefineDiagnosisContext);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(etched_color2);
		separator.setForeground(etched_color1);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		panel.add(separator, gbc_separator);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		panel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(Color.LIGHT_GRAY));
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(15, 20, 0, 0);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 0;
		panel_2.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0};
		gbl_panel_4.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.insets = new Insets(20, 16, 20, 20);
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 0;
		panel_4.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		JPanel panel_10 = new JPanel();
		GridBagConstraints gbc_panel_10 = new GridBagConstraints();
		gbc_panel_10.anchor = GridBagConstraints.WEST;
		gbc_panel_10.fill = GridBagConstraints.VERTICAL;
		gbc_panel_10.gridx = 0;
		gbc_panel_10.gridy = 0;
		panel_5.add(panel_10, gbc_panel_10);
		
		JLabel lblSymptoms = new JLabel("Symptoms:");
		lblSymptoms.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_10.add(lblSymptoms);
		
		JPanel panel_9 = new JPanel();
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.insets = new Insets(0, 4, 5, 0);
		gbc_panel_9.fill = GridBagConstraints.BOTH;
		gbc_panel_9.gridx = 0;
		gbc_panel_9.gridy = 1;
		panel_5.add(panel_9, gbc_panel_9);
		GridBagLayout gbl_panel_9 = new GridBagLayout();
		gbl_panel_9.columnWidths = new int[]{82, 0};
		gbl_panel_9.rowHeights = new int[]{98, 0};
		gbl_panel_9.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_9.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_9.setLayout(gbl_panel_9);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane.getHorizontalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane.setOpaque(false);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridy = 0;
		gbc_scrollPane.gridx = 0;
		panel_9.add(scrollPane, gbc_scrollPane);
		DefaultListModel<JsonNodeWrapper> list_model = new DefaultListModel<JsonNodeWrapper>();
		for(int x = 0; x < symptoms.size(); x++)
			list_model.addElement(symptoms.get(x));
		JList<JsonNodeWrapper> list = new JList<JsonNodeWrapper>(list_model);
		list.setFont(new Font("Arial", Font.PLAIN, 12));
		list.setVisibleRowCount(6);
		list.setFocusable(false);
		list.setEnabled(false);
		scrollPane.setViewportView(list);
		
		JPanel panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.anchor = GridBagConstraints.WEST;
		gbc_panel_8.insets = new Insets(0, 0, 5, 0);
		gbc_panel_8.fill = GridBagConstraints.VERTICAL;
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 2;
		panel_5.add(panel_8, gbc_panel_8);
		
		JLabel lblAffectedSystemExample = new JLabel("Affected system: " + system);
		lblAffectedSystemExample.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_8.add(lblAffectedSystemExample);
		
		JPanel panel_7 = new JPanel();
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.anchor = GridBagConstraints.WEST;
		gbc_panel_7.fill = GridBagConstraints.VERTICAL;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 3;
		panel_5.add(panel_7, gbc_panel_7);
		
		JLabel lblAffectedPartExample_1 = new JLabel("Affected part: " + part);
		lblAffectedPartExample_1.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_7.add(lblAffectedPartExample_1);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(10, 10, 5, 20);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 2;
		panel.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JPanel panel_12 = new JPanel();
		GridBagConstraints gbc_panel_12 = new GridBagConstraints();
		gbc_panel_12.anchor = GridBagConstraints.WEST;
		gbc_panel_12.insets = new Insets(0, 0, 5, 0);
		gbc_panel_12.fill = GridBagConstraints.VERTICAL;
		gbc_panel_12.gridx = 0;
		gbc_panel_12.gridy = 0;
		panel_3.add(panel_12, gbc_panel_12);
		
		JLabel lblPotentialCauses = new JLabel("Treatments:");
		lblPotentialCauses.setFont(new Font("Arial", Font.PLAIN, 15));
		panel_12.add(lblPotentialCauses);
		
		JPanel panel_11 = new JPanel();
		GridBagConstraints gbc_panel_11 = new GridBagConstraints();
		gbc_panel_11.fill = GridBagConstraints.BOTH;
		gbc_panel_11.gridx = 0;
		gbc_panel_11.gridy = 1;
		panel_3.add(panel_11, gbc_panel_11);
		GridBagLayout gbl_panel_11 = new GridBagLayout();
		gbl_panel_11.columnWidths = new int[]{0, 0};
		gbl_panel_11.rowHeights = new int[]{0, 0};
		gbl_panel_11.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_11.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_11.setLayout(gbl_panel_11);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		scrollPane_1.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane_1.getHorizontalScrollBar().setUnitIncrement(10);
		scrollPane_1.getVerticalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane_1.getHorizontalScrollBar().setUI(new ScrollUI(Color.WHITE));
		scrollPane_1.setOpaque(false);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		panel_11.add(scrollPane_1, gbc_scrollPane_1);
		table = new JTable();
		table.setBorder(new EmptyBorder(0, 0, 0, 0));
		table.setFillsViewportHeight(true);
		table.setDragEnabled(false);
		table.setEnabled(false);
		table.setOpaque(true);
		table.getTableHeader().setReorderingAllowed(false);
		DefaultTableModel table_model = new DefaultTableModel();
		table_model.addColumn("Problem"); 
		table_model.addColumn("Treatment Label");
		table_model.addColumn("Description");
		for(int r = 0; r < treatments_table.size(); r++)
			table_model.addRow(treatments_table.get(r));
		table.setModel(table_model);
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		for(int col = 0; col < table.getColumnCount(); col++)
			table.getColumnModel().getColumn(col).setCellRenderer(new WordWrapCellRenderer());
		table.setAutoCreateColumnsFromModel(false);
		int def_row_height = 30;
		table.getTableHeader().setPreferredSize(new Dimension(0, def_row_height));
		table.setRowHeight(def_row_height);
		scrollPane_1.setViewportView(table);
		
		JPanel panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.gridwidth = 2;
		gbc_panel_6.insets = new Insets(16, 0, 0, 0);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 3;
		panel.add(panel_6, gbc_panel_6);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(skipped)
					symptomsPage();
				else
					causesPage(check_list);
				return;
			}
		});
		btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_6.add(btnBack);
		
		JButton btnNewDiagnosis = new JButton("Finish");
		btnNewDiagnosis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				system = null;
				part = null;
				contextPage();
				return;
			}
		});
		btnNewDiagnosis.setFont(new Font("Arial", Font.PLAIN, 14));
		panel_6.add(btnNewDiagnosis);
		
		setBounds(this.getX() + (this.getWidth() / 2) - (screenWidth / 2), this.getY() + (this.getHeight() / 2) - (screenHeight / 2), screenWidth, screenHeight);
		validate();
	}

}
