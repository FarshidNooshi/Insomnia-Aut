// In The Name Of GOD
package Phase1;

// * the tray part is used from the code from stackOverFlow.com

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static java.awt.event.InputEvent.ALT_MASK;

public class View extends JFrame {
    private static final int NUMBER_OF_MENUS = 3; // number of menus of the program
    private static final int NUMBER_OF_SUBMENUS = 2; // its the number of submenus of each menu
    private static int[] numberOfHeaders = {1, 1, 1}; // an index holder for the headers that are visible at the moment
    private static ArrayList<JPanel> centerPanels, rightPanels;
    private static JPanel jsonPanel;
    private static JPanel formDataPanel;
    private final Options options; // options panel that should be opened in another frame
    private JButton selectItem, resetItem;
    private JLabel fileSelected;
    private JComboBox<Requests> comboBox;
    private boolean fullScreen = false; // say if it's in the fullscreen mode or not
    private JMenuBar menuBar; // for the menu of the frame
    private ArrayList<JMenu> menus; // menus of the program
    private ArrayList<ArrayList<JMenuItem>> submenus; // submenus of the program
    private JPanel center, left, right; // three panels for left center and right side of the program
    private JTabbedPane tabbedPane; // used for center panel (4 possibilities)
    private JTextField urlTextField; // url textField for writing a url
    private JButton sendURL; // a button for saving the url
    private JList<Object> list; // for making folders in the left panel
    private TrayIcon trayIcon; // this two are for tray closing system
    private SystemTray tray;
    private JButton createNode = new JButton("create"), copy;
    private ArrayList<String> folders = new ArrayList<>();
    private JTabbedPane bodyTabbedPane;
    private JPanel status;
    private JTextArea raw, json;
    private JEditorPane preview;
    private JLabel bufferedPic = new JLabel();


    /**
     * the constructor of the frame that build the things
     *
     * @param options is the option frame
     * @throws ClassNotFoundException          if the class didn't exist
     * @throws UnsupportedLookAndFeelException if the look and feel wasn't available in the system
     * @throws InstantiationException          if the exception occurs.
     * @throws IllegalAccessException          if we wanted an illegal access to something in the system
     */
    View(Options options) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        this.options = options;
        setTitle("Farshid Nooshi Midterm project-term2(98-99)");
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        setLayout(new BorderLayout());
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(screen);
        setResizable(false);
        center = new JPanel();
        left = new JPanel();
        right = new JPanel();
        menuBar = new JMenuBar();
        menus = new ArrayList<>();
        submenus = new ArrayList<>();
        urlTextField = new JTextField();
        rightPanels = new ArrayList<>();
        comboBox = new JComboBox<>(Requests.values());
        sendURL = new JButton("Send");
        tabbedPane = new JTabbedPane();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initMenuBar();
        setJMenuBar(menuBar);
        add(center, BorderLayout.CENTER);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
        initLeftPanel();
        initCenterPanel();
        initRightPanel();
        //***********************************************************************************************TRAY
        tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("OrbitProject/Data/Insomnia.png");
        PopupMenu popup = new PopupMenu();
        MenuItem defaultItem = new MenuItem("Exit");
        defaultItem.addActionListener(e -> System.exit(0));
        popup.add(defaultItem);
        defaultItem = new MenuItem("Open");
        defaultItem.addActionListener(e -> {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                    tray.remove(trayIcon);
                }
        );
        popup.add(defaultItem);
        trayIcon = new TrayIcon(image, "", popup);
        trayIcon.setImageAutoSize(true);
        //***********************************************************************************************TRAY
    }

    public static ArrayList<JPanel> getCenterPanels() {
        return centerPanels;
    }

    public static ArrayList<JPanel> getRightPanels() {
        return rightPanels;
    }

    public static int[] getNumberOfHeaders() {
        return numberOfHeaders;
    }

    public static JPanel getFormDataPanel() {
        return formDataPanel;
    }

    public static JPanel getJsonPanel() {
        return jsonPanel;
    }

    public JButton getResetItem() {
        return resetItem;
    }

    public JButton getSelectItem() {
        return selectItem;
    }

    public JComboBox<Requests> getComboBox() {
        return comboBox;
    }

    public JPanel getStatus() {
        return status;
    }

    /**
     * this method initializes the center panel for our GUI
     */
    private void initCenterPanel() {
        center.setLayout(new BorderLayout());
        comboBox.setPreferredSize(new Dimension(80, 41));
        JPanel temporary = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        urlTextField.setPreferredSize(new Dimension(602, 41));
        sendURL.setPreferredSize(new Dimension(70, 41));
        temporary.add(comboBox);
        temporary.add(urlTextField);
        temporary.add(sendURL);
        temporary.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        center.add(temporary, BorderLayout.NORTH);
        center.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
        initTabs();
    }

    /**
     * this method initializes the right panel for our GUI
     * also i separated parts of the code for better readability
     * each separated part is used for one panel
     */
    private void initRightPanel() {
        right.setLayout(new BorderLayout(5, 0));
        JTabbedPane jTabbedPane = new JTabbedPane();
        GridLayout layout = new GridLayout(1, 3);
        status = new JPanel(layout);
        status.setPreferredSize(new Dimension(400, 45));
        right.add(status, BorderLayout.NORTH);
        status.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        jTabbedPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        try {
            createSpecificLabelRightPanel("200 OK", new Color(83, 255, 119), status);
            createSpecificLabelRightPanel("1.03 S", new Color(10, 84, 255), status);
            createSpecificLabelRightPanel("11.6 KB", new Color(10, 84, 255), status);
        } catch (Exception e) {
            e.printStackTrace();
            dispose();
        }
        for (int i = 0; i < 2; i++)
            rightPanels.add(new JPanel());
        rightPanels.get(0).setName("Body");
        rightPanels.get(1).setName("Header");
        for (JPanel panel : rightPanels)
            jTabbedPane.add(panel.getName(), panel);
        right.add(jTabbedPane, BorderLayout.CENTER);
//**********************************************************************************************************************
        JPanel reference = rightPanels.get(1);// HEADER
        reference.setLayout(new GridLayout(16, 3));
        reference.setMaximumSize(new Dimension(400, 45));
        for (int i = 0; i < 15; i++) {
            JTextArea key = new JTextArea(), value = new JTextArea();
            reference.add(new JLabel(new ImageIcon("OrbitProject/Data/menu_32px.png")));
            key.setEditable(false);
            value.setEditable(false);
            value.setLineWrap(true);
            key.setLineWrap(true);
            reference.add(key);
            reference.add(value);
        }
        copy = new JButton("Copy to Clipboard");
        reference.add(copy);
//**********************************************************************************************************************
        reference = rightPanels.get(0);//Body
        JTabbedPane jTabbedPane1 = new JTabbedPane();
        reference.setMaximumSize(new Dimension(400, 45));
        raw = new JTextArea("");
        preview = new JEditorPane();
        json = new JTextArea("");
        JScrollPane scroll = new JScrollPane(raw);
        JScrollPane previewScroll = new JScrollPane(preview);
        JScrollPane jsonScroll = new JScrollPane(json);
        previewScroll.setPreferredSize(new Dimension(400, 45));
        raw.setLineWrap(true);
        json.setLineWrap(true);
        reference.setLayout(new BorderLayout());
        reference.add(jTabbedPane1, BorderLayout.CENTER);
        ArrayList<JPanel> rightBodyPanels = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            rightBodyPanels.add(new JPanel(new BorderLayout())); // default in bashe ta too phase 2&3 avaz beshe
        rightBodyPanels.get(0).setName("Raw");
        rightBodyPanels.get(1).setName("Preview");
        rightBodyPanels.get(2).setName("JSON");
        rightBodyPanels.get(3).setName("Picture");
        rightBodyPanels.get(0).add(scroll, BorderLayout.CENTER);
        rightBodyPanels.get(1).add(previewScroll, BorderLayout.CENTER);
        rightBodyPanels.get(2).add(jsonScroll, BorderLayout.CENTER);
        rightBodyPanels.get(3).add(bufferedPic, BorderLayout.CENTER);
        raw.setEditable(false);
        json.setEditable(false);
        preview.setEditable(false);
        preview.setPreferredSize(new Dimension(400, 45));
        for (JPanel panel : rightBodyPanels)
            jTabbedPane1.add(panel.getName(), panel);
    }

    public JTextArea getRaw() {
        return raw;
    }

    public JTextArea getJson() {
        return json;
    }

    public JLabel getBufferedPic() {
        return bufferedPic;
    }

    /**
     * @param text  is a text string
     * @param color is the color of the label
     * @param help  is a panel to adding to
     * @throws NullPointerException checking for not being null in help
     */
    private void createSpecificLabelRightPanel(String text, Color color, JPanel help) throws NullPointerException {
        JLabel memory = new JLabel(text, SwingConstants.CENTER);
        memory.setPreferredSize(new Dimension(70, 30));
        memory.setBackground(color);
        memory.setForeground(Color.WHITE);
        memory.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        memory.setOpaque(true);
        help.add(memory);
    }

    /**
     * this method initializes the left panel of the program
     * we have a list for creating folders
     */
    private void initLeftPanel() {
        JLabel label = new JLabel("Insomnia", new ImageIcon("OrbitProject/Data/Insomnia.png"), SwingConstants.LEFT);
        label.setBackground(new Color(182, 1, 255));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        folders.add("Requests");
        try {
            File fileToCreate = new File("OrbitProject\\src");
            for (String name : Objects.requireNonNull(fileToCreate.list())) {
                if (name.contains("Phase") || name.equalsIgnoreCase("OutputFolder"))
                    continue;
                folders.add(folders.size(), name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        list = new JList<>(folders.toArray());
        list.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        left.setLayout(new BorderLayout());
        left.add(label, BorderLayout.NORTH);
        left.add(createNode, BorderLayout.SOUTH);
        left.add(list, BorderLayout.CENTER);
    }

    /**
     * showing the frame in a window
     */
    void showGUI() {
        pack();
        setVisible(true);
    }

    /**
     * initializes the menu bar of our program
     * it's extendable also for adding multiple futures to the program
     */
    @SuppressWarnings("deprecation")
    private void initMenuBar() { // Action listener should add
        setIconImage(new ImageIcon("OrbitProject/Data/Insomnia.png").getImage());
        menus.add(new JMenu("Application"));
        menus.add(new JMenu("View"));
        menus.add(new JMenu("Help"));
        menus.get(0).setMnemonic(KeyEvent.VK_ASTERISK);
        menus.get(0).setMnemonic(KeyEvent.VK_1);
        menus.get(0).setMnemonic(KeyEvent.VK_2);
        menus.get(0).setMnemonic(KeyEvent.VK_3);
        for (int i = 0; i < NUMBER_OF_MENUS; i++)
            menuBar.add(menus.get(i));
        for (int i = 0; i < NUMBER_OF_MENUS; i++)
            submenus.add(new ArrayList<>());
        submenus.get(0).add(new JMenuItem("Options", KeyEvent.VK_O));
        submenus.get(0).add(new JMenuItem("Exit", KeyEvent.VK_E));
        submenus.get(1).add(new JMenuItem("Toggle Full Screen", KeyEvent.VK_T));
        submenus.get(1).add(new JMenuItem("Toggle sidebar", KeyEvent.VK_S));
        submenus.get(2).add(new JMenuItem("About", KeyEvent.VK_A));
        submenus.get(2).add(new JMenuItem("Help", KeyEvent.VK_H));
        submenus.get(0).get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ALT_MASK));
        submenus.get(0).get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ALT_MASK));
        submenus.get(1).get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ALT_MASK));
        submenus.get(1).get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ALT_MASK));
        submenus.get(2).get(0).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ALT_MASK));
        submenus.get(2).get(1).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ALT_MASK));
        for (int i = 0; i < NUMBER_OF_MENUS; i++)
            for (int j = 0; j < NUMBER_OF_SUBMENUS; j++) {
                menus.get(i).add(submenus.get(i).get(j));
                if (j == 0)
                    menus.get(i).addSeparator();
            }
        submenus.get(0).get(0).addActionListener(e -> options.showGUI());
        submenus.get(0).get(1).addActionListener(e -> dispose());
        submenus.get(1).get(0).addActionListener(e -> {
            fullScreen = !fullScreen;
            if (fullScreen)
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            else
                setExtendedState(JFrame.NORMAL);
        });
        submenus.get(1).get(1).addActionListener(e -> {
            if (left.isVisible()) {
                urlTextField.setPreferredSize(new Dimension(707, 41));
                left.setVisible(false);
            } else {
                urlTextField.setPreferredSize(new Dimension(602, 41));
                left.setVisible(true);
            }
        });
        submenus.get(2).get(0).addActionListener(e -> JOptionPane.showMessageDialog(null, "Farshid Nooshi\nStudent ID: 9831068\nEmail: FarshidNooshi726@aut.ac.ir", "About", JOptionPane.INFORMATION_MESSAGE));
        submenus.get(2).get(1).addActionListener(e -> JOptionPane.showMessageDialog(null, "see the help folder in phase 3", "Help", JOptionPane.INFORMATION_MESSAGE));
    }

    /**
     * it'll be done in the next phases it's purpose is to set the frame to dark mode!
     */
    void setToDark() {
        System.out.println("seen");
    }

    /**
     * the purpose of this method is to setting back everything to it's default mode
     * i mean putting back the changes that setToDark() has made.
     */
    void setToLight() {
        System.out.println("!seen");
    }

    /**
     * initializes the tabs of the center panel
     * I separated the parts of the code for better readability
     */
    private void initTabs() {
        centerPanels = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            centerPanels.add(new JPanel());
        centerPanels.get(0).setName("Header");
        centerPanels.get(1).setName("Auth");
        centerPanels.get(2).setName("Query");
        centerPanels.get(3).setName("Body");
        for (JPanel j : centerPanels)
            tabbedPane.add(j.getName(), j);
//**********************************************************************************************************************
        JPanel reference = centerPanels.get(0);// HEADER
        buildTab(reference, 0);
//**********************************************************************************************************************
        reference = centerPanels.get(2);//Query
        buildTab(reference, 1);
//**********************************************************************************************************************
        reference = centerPanels.get(1);// Auth
        reference.setLayout(new GridLayout(5, 2));
        JTextField token = new JTextField();
        JTextField prefix = new JTextField();
        JCheckBox isEnabled = new JCheckBox();
        JLabel token1 = new JLabel("TOKEN");
        JLabel prefix1 = new JLabel("PREFIX");
        JLabel enabled = new JLabel("ENABLED");
        isEnabled.addActionListener(e -> System.out.println("Auth checkbox got action event."));
        reference.add(token1);
        reference.add(token);
        reference.add(prefix1);
        reference.add(prefix);
        reference.add(enabled);
        reference.add(isEnabled);
//**********************************************************************************************************************
        reference = centerPanels.get(3);// Body
        reference.setLayout(new BorderLayout());
        initBody();
    }

    private void buildTab(JPanel reference, int id) {
        MyFocus focus = new MyFocus(id);
        reference.setLayout(new GridLayout(20, 5, 5, 5));
        for (int i = 0; i < 20; i++) {
            JTextField header = new JTextField("New Header"), value = new JTextField("New Value");
            JButton trash = new JButton(new ImageIcon("OrbitProject/Data/trash_32px.png"));
            JCheckBox active = new JCheckBox("activate Header");
            header.addFocusListener(focus);
            header.setName(header.getText());
            value.setName(value.getText());
            value.addFocusListener(focus);
            reference.add(new JLabel(new ImageIcon("OrbitProject/Data/menu_32px.png")));
            reference.add(header);
            reference.add(value);
            reference.add(active);
            reference.add(trash);
            active.setName("" + i);
            trash.setName("" + i);
            active.setSelected(false);
        }
        for (int i = numberOfHeaders[id]; i < 20; i++)
            for (int j = 0; j < 5; j++)
                reference.getComponent(i * 5 + j).setVisible(false);
    }

    /**
     * because of the body of the center panel is too long i separated the code to another method for dividing it to improving the readability
     */
    private void initBody() {
        JPanel reference = centerPanels.get(3);
        formDataPanel = new JPanel();
        jsonPanel = new JPanel();
        JPanel binaryDataPanel = new JPanel();
        jsonPanel.setName("jsonPanel");
        formDataPanel.setName("formDataPanel");
        binaryDataPanel.setName("binaryDataPanel");
        bodyTabbedPane = new JTabbedPane();
        bodyTabbedPane.add(jsonPanel.getName(), jsonPanel);
        bodyTabbedPane.add(formDataPanel.getName(), formDataPanel);
        bodyTabbedPane.add(binaryDataPanel.getName(), binaryDataPanel);
        reference.add(bodyTabbedPane, BorderLayout.CENTER);
//**********************************************************************************************************************
        binaryDataPanel.setLayout(new BorderLayout()); // Binary Data
        JLabel label = new JLabel("Please select a file ", new ImageIcon("OrbitProject/Data/resume_100px.png"), SwingConstants.LEFT);
        binaryDataPanel.add(label, BorderLayout.NORTH);
        selectItem = new JButton("Choose File");
        resetItem = new JButton("Reset File");
        fileSelected = new JLabel("Selected File: Nothing", new ImageIcon("OrbitProject/Data/file_100px.png"), SwingUtilities.LEFT);
        JPanel tmp = new JPanel(new FlowLayout());
        tmp.add(resetItem);
        tmp.add(selectItem);
        binaryDataPanel.add(fileSelected, BorderLayout.CENTER);
        binaryDataPanel.add(tmp, BorderLayout.SOUTH);
//**********************************************************************************************************************
        jsonPanel.setLayout(new BorderLayout());// JSON panel
        JTextField textField1 = new JTextField("");
        textField1.setName("json");
        textField1.addFocusListener(new MyFocus(3));
        jsonPanel.add(textField1, BorderLayout.CENTER);
//**********************************************************************************************************************
        buildTab(formDataPanel, 2);// FormData Panel
        formDataPanel.setName("formData");
//**********************************************************************************************************************
        binaryDataPanel.setVisible(false);
        jsonPanel.setVisible(false);
        formDataPanel.setVisible(false);
    }

    void setToTray() {
        if (options.getExitBox().isSelected()) {
            try {
                tray.add(trayIcon);
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
            setVisible(false);
        }
    }

    public JButton getCopy() {
        return copy;
    }

    public JButton getSendURL() {
        return sendURL;
    }

    public JTabbedPane getBodyTabbedPane() {
        return bodyTabbedPane;
    }

    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    public JLabel getFileSelected() {
        return fileSelected;
    }

    public JList<Object> getList() {
        return list;
    }

    public JEditorPane getPreview() {
        return preview;
    }

    public JTextField getUrlTextField() {
        return urlTextField;
    }

    public Options getOptions() {
        return options;
    }

    public JButton getCreateNode() {
        return createNode;
    }

    public ArrayList<String> getFolders() {
        return folders;
    }

    /**
     * this class is built for listening to focuses in the program for textAreas
     */
    private static class MyFocus extends FocusAdapter {
        private final int ID;

        MyFocus(int id) {
            this.ID = id;
        }

        private boolean valid(JTextField... texts) {
            return (!texts[0].getText().equals("New Header") && !texts[1].getText().equals("New Value"));
        }

        /**
         * clearing the initial text of the textArea is the responsibility
         * and opening a new row of textFields
         *
         * @param e is the focus even
         */
        @Override
        public void focusGained(FocusEvent e) {
            JTextField jTextField = (JTextField) e.getSource();
            jTextField.setText("");
            if (ID > 2)
                return;
            JTextField[] texts = new JTextField[2];
            if (ID == 0) {
                texts[0] = (JTextField) centerPanels.get(0).getComponent((numberOfHeaders[ID] - 1) * 5 + 1);
                texts[1] = (JTextField) centerPanels.get(0).getComponent((numberOfHeaders[ID] - 1) * 5 + 2);
            } else if (ID == 1) {
                texts[0] = (JTextField) centerPanels.get(2).getComponent((numberOfHeaders[ID] - 1) * 5 + 1);
                texts[1] = (JTextField) centerPanels.get(2).getComponent((numberOfHeaders[ID] - 1) * 5 + 2);
            } else {
                texts[0] = (JTextField) formDataPanel.getComponent((numberOfHeaders[ID] - 1) * 5 + 1);
                texts[1] = (JTextField) formDataPanel.getComponent((numberOfHeaders[ID] - 1) * 5 + 2);
            }
            if (valid(texts)) {
                for (int j = 0; j < 5 && numberOfHeaders[ID] <= 19; j++) {
                    if (ID == 0)
                        centerPanels.get(0).getComponent(numberOfHeaders[ID] * 5 + j).setVisible(true);
                    else if (ID == 1)
                        centerPanels.get(2).getComponent(numberOfHeaders[ID] * 5 + j).setVisible(true);
                    else
                        formDataPanel.getComponent(numberOfHeaders[ID] * 5 + j).setVisible(true);
                }
                numberOfHeaders[ID]++;
            }
        }

        /**
         * setting back the text to its initial text is the responsibility
         *
         * @param e is focus Event
         */
        @Override
        public void focusLost(FocusEvent e) {
            JTextField jTextField = (JTextField) e.getSource();
            if (jTextField.getText().trim().equals(""))
                jTextField.setText(jTextField.getName());
        }
    }
}
