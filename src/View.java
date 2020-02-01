import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

/**
 * This class represent the a User Interface for picture processing program. It has menu for load
 * and save files, processing image and generating specific image. It also have buttons that undo,
 * redo the process, or exit the program. It has a text area for user to input batch- scrip and
 * buttons to save or execute the scrip. The processing result image will shown in this interface.
 * This class contains all the methods required by the IView interface.
 */
public class View extends JFrame implements IView {

  private JMenuItem loadMenu;
  private JMenuItem saveMenu;

  private JMenuItem blurMenu;
  private JMenuItem sharpMenu;
  private JMenuItem greyMenu;
  private JMenuItem sepiaMenu;
  private JMenuItem ditherMenu;
  private JMenuItem mosaicMenu;
  private JMenuItem rainbowMenu;
  private JMenuItem checkerBoardMenu;
  private JButton exitButton;
  private JButton redoButton;
  private JButton undoButton;
  private JButton executeButton;
  private JButton saveScripButton;

  private JTextArea textArea;
  private JLabel imageLabel;

  /**
   * Constructor that set up the UI. It contains menu items to process the image, and buttons to
   * redo, undo, execute the script and exit the program. It has image label to show the image and
   * a text area to type the batch script.
   */
  public View() {
    super();
    setTitle("Image Processing Program");
    setSize(400, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //--------main panel--------------
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    add(mainPanel);


    //-------------create menu-------------------------
    JMenuBar menuBar = new JMenuBar();
    //file menu(load and save)
    JMenu file = new JMenu("File");
    menuBar.add(file);

    //load
    loadMenu = new JMenuItem("Open");
    file.add(loadMenu);

    //save
    saveMenu = new JMenuItem("Save");
    file.add(saveMenu);


    //Edit menu with image processing method
    JMenu edit = new JMenu("Edit");
    menuBar.add(edit);

    //blur
    blurMenu = new JMenuItem("Blur");
    edit.add(blurMenu);

    //sharpen
    sharpMenu = new JMenuItem("Sharpen");
    edit.add(sharpMenu);

    //greyscale
    greyMenu = new JMenuItem("Greyscale");
    edit.add(greyMenu);

    //Sepia
    sepiaMenu = new JMenuItem("Sepia tone");
    edit.add(sepiaMenu);

    //Dithering
    ditherMenu = new JMenuItem("Dithering");
    edit.add(ditherMenu);

    //Mosaicing
    mosaicMenu = new JMenuItem("Mosaic");
    edit.add(mosaicMenu);

    //Edit menu with image generator:
    JMenu generate = new JMenu("Generate");
    menuBar.add(generate);

    //rainbow flag
    rainbowMenu = new JMenuItem("Rainbow flag");
    generate.add(rainbowMenu);

    //checkerBoard
    checkerBoardMenu = new JMenuItem("Checkerboard");
    generate.add(checkerBoardMenu);

    this.setJMenuBar(menuBar);


    //----------Image panel--------------
    imageLabel = new JLabel();
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    imageScrollPane.setPreferredSize(new Dimension(400, 400));
    mainPanel.add(imageScrollPane);


    //---------------button panel-------------
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    mainPanel.add(buttonPanel);

    //Redo button
    redoButton = new JButton("Redo");
    buttonPanel.add(redoButton);

    //undo button
    undoButton = new JButton("Undo");
    buttonPanel.add(undoButton);

    //exit button
    exitButton = new JButton("Exit");
    buttonPanel.add(exitButton);


    //-----------batch scrip text------------
    textArea = new JTextArea(5, 40);
    JScrollPane scrollPane = new JScrollPane(textArea);
    textArea.setLineWrap(true);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Type your batch scrip here: "));
    mainPanel.add(scrollPane);

    //-----execute button-----
    JPanel executeButtonPanel = new JPanel();
    executeButtonPanel.setLayout(new FlowLayout());
    mainPanel.add(executeButtonPanel, BorderLayout.PAGE_END);

    executeButton = new JButton("Execute");
    executeButtonPanel.add(executeButton);

    //-----------save scrip file button---------
    saveScripButton = new JButton("Save Script");
    executeButtonPanel.add(saveScripButton);


    setVisible(true);
  }

  @Override
  public void setFeatures(Feature c) {

    loadMenu.addActionListener(l -> {
      try {
        c.loadImage();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    saveMenu.addActionListener(l -> {
      try {
        c.saveImage();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    blurMenu.addActionListener(l -> c.processCommand("blur"));
    sharpMenu.addActionListener(l -> c.processCommand("sharpen"));
    greyMenu.addActionListener(l -> c.processCommand("greyscale"));
    sepiaMenu.addActionListener(l -> c.processCommand("sepia"));
    ditherMenu.addActionListener(l -> c.processCommand("dithering"));
    mosaicMenu.addActionListener(l -> c.processCommand("mosaicing"));
    rainbowMenu.addActionListener(l -> c.processCommand("generate rainbowflag"));
    checkerBoardMenu.addActionListener(l -> c.processCommand("generate checkerboard"));

    exitButton.addActionListener(l -> c.exitProgram());
    undoButton.addActionListener(l -> c.undo());
    redoButton.addActionListener(l -> c.redo());

    executeButton.addActionListener(l -> c.executeScrip());
    saveScripButton.addActionListener(l -> c.saveScrip());
  }

  @Override
  public String getInputScrip() {
    return textArea.getText();
  }

  @Override
  public void updateImage(Image i) {
    imageLabel.setIcon(new ImageIcon(i));
  }

  @Override
  public String getFilePath() {
    final JFileChooser fileChooser = new JFileChooser(".");
    int retvalue = fileChooser.showOpenDialog(View.this);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();
      return f.getAbsolutePath();
    }
    return "";
  }

  @Override
  public String getInputFromOption(Object[] options, String message) {
    return (String) JOptionPane.showInputDialog(View.this, message,
            "Input", JOptionPane.PLAIN_MESSAGE, null, options,
            options[0]);
  }

  @Override
  public String getInput(String message) {
    return JOptionPane.showInputDialog(View.this, message, "Input",
            JOptionPane.PLAIN_MESSAGE);
  }

  @Override
  public void showErrorMessage(String errorMessage) {
    JOptionPane.showMessageDialog(View.this, errorMessage, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

}











