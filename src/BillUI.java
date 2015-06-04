import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;


public class BillUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BillUI window = new BillUI();
					window.frame.setVisible(true);
					//設定frame參數
					window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					//window.frame.getContentPane().setBackground(Color.BLUE);
					window.frame.setSize(500,300);
					window.frame.setLocationRelativeTo(null);
					window.frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BillUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		final String type[] = {"0","Null"};
		
		
		frame = new JFrame();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{65, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 28, 0, 18, 0, 0, 85, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		//將RadioButton群組化
		ButtonGroup BG = new ButtonGroup();
		
		final JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
        chooser.setCurrentDirectory(new File("./"));
        chooser.setDialogTitle("Choose file location:");
        chooser.setFileHidingEnabled(false);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setApproveButtonText("Choose");
		
		final JFileChooser chooser2 = new JFileChooser();
		chooser2.setCurrentDirectory(new java.io.File("."));
		chooser2.setDialogTitle("choosertitle");
		chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser2.setAcceptAllFileFilterUsed(false);
		
		final JRadioButton rdbtnNewRadioButton = new JRadioButton("S2T");
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 0;
		gbc_rdbtnNewRadioButton.gridy = 0;
		frame.getContentPane().add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
		BG.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.addMouseListener(new MouseAdapter(){
			 @Override
	            public void mouseReleased(MouseEvent ev)
	            {
				 type[0]="1";		 
				 type[1]=rdbtnNewRadioButton.getText();
	            }
		});
		
		final JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("NTT");
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 1;
		gbc_rdbtnNewRadioButton_1.gridy = 0;
		frame.getContentPane().add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
		BG.add(rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_1.addMouseListener(new MouseAdapter(){
			 @Override
	            public void mouseReleased(MouseEvent ev)
	            {
				 type[0]="2";		 
				 type[1]=rdbtnNewRadioButton_1.getText();
	            }
		});
		
		final JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("FET");
		GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_2.gridx = 2;
		gbc_rdbtnNewRadioButton_2.gridy = 0;
		frame.getContentPane().add(rdbtnNewRadioButton_2, gbc_rdbtnNewRadioButton_2);
		BG.add(rdbtnNewRadioButton_2);

		rdbtnNewRadioButton_2.addMouseListener(new MouseAdapter(){
			 @Override
	            public void mouseReleased(MouseEvent ev)
	            {
				 type[0]="3";
				 type[1]=rdbtnNewRadioButton_2.getText();
	            }
		});
		
		final JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("IGlomo");
		GridBagConstraints gbc_rdbtnNewRadioButton_3 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_3.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton_3.gridx = 3;
		gbc_rdbtnNewRadioButton_3.gridy = 0;
		frame.getContentPane().add(rdbtnNewRadioButton_3, gbc_rdbtnNewRadioButton_3);
		BG.add(rdbtnNewRadioButton_3);
		
		rdbtnNewRadioButton_3.addMouseListener(new MouseAdapter(){
			 @Override
	            public void mouseReleased(MouseEvent ev)
	            {
				 type[0]="4";		 
				 type[1]=rdbtnNewRadioButton_3.getText();
	            }
		});
		
		JLabel lblInputFile = new JLabel("Input File:");

		GridBagConstraints gbc_lblInputFile = new GridBagConstraints();
		gbc_lblInputFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblInputFile.gridx = 0;
		gbc_lblInputFile.gridy = 1;
		frame.getContentPane().add(lblInputFile, gbc_lblInputFile);
		
		final JLabel lblNewLabel = new JLabel("     ");
		lblNewLabel.setSize(500, 15);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 3;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		JButton btnNewButton = new JButton("Sorce file");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridwidth = 4;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 2;
		frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		//Button的Action
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent ev)
            {
                int confirm = chooser.showOpenDialog(null);
                if (confirm == JFileChooser.APPROVE_OPTION)
                {
                    //JOptionPane.showMessageDialog(null, "Your file: " + chooser.getSelectedFile() + "\n,in " + chooser.getCurrentDirectory() + ".", "Message", JOptionPane.INFORMATION_MESSAGE);
                    lblNewLabel.setText(chooser.getSelectedFile().toString()+"");
                }
            }
        });
		
		JLabel lblOutputFolder = new JLabel("Output Folder:");
		GridBagConstraints gbc_lblOutputFolder = new GridBagConstraints();
		gbc_lblOutputFolder.insets = new Insets(0, 0, 5, 5);
		gbc_lblOutputFolder.gridx = 0;
		gbc_lblOutputFolder.gridy = 3;
		frame.getContentPane().add(lblOutputFolder, gbc_lblOutputFolder);
		
		final JLabel lblNewLabel_1 = new JLabel("  ");
		lblNewLabel_1.setSize(500, 15);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.gridwidth = 3;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 3;
		frame.getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JButton btnNewButton_1 = new JButton("OutputDir");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridwidth = 4;
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 4;
		frame.getContentPane().add(btnNewButton_1, gbc_btnNewButton_1);
		
		final JButton btnNewButton_2 = new JButton("\u958B\u59CB\u8F49\u63DB");
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridwidth = 4;
		gbc_btnNewButton_2.gridx = 0;
		gbc_btnNewButton_2.gridy = 5;
		frame.getContentPane().add(btnNewButton_2, gbc_btnNewButton_2);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 6;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		
		btnNewButton_1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent ev)
            {
                int confirm = chooser2.showOpenDialog(null);
                if (confirm == JFileChooser.APPROVE_OPTION)
                {
                    //JOptionPane.showMessageDialog(null, "Your file: " + chooser2.getSelectedFile() + "\n,in " + chooser2.getCurrentDirectory() + ".", "Message", JOptionPane.INFORMATION_MESSAGE);
                    lblNewLabel_1.setText(chooser2.getSelectedFile().toString());
                }
            }
        });
		
		btnNewButton_2.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent ev)
            {
            	boolean volid=true;

            	String msg="";
            	if(chooser.getSelectedFile()==null||
            			"".equals(chooser.getSelectedFile().toString()))
            		msg+="Please choose input file."+"\n";
            	
            	if(chooser2.getSelectedFile()==null||
            			"".equals(chooser2.getSelectedFile().toString()))
            		msg+="Please choose output folder."+"\n";
            	
            	if(type.length==0||type[0]==null||"".equals(type[0])||"0".equals(type[0]))
            		msg+="Please choose transfer type."+"\n";
            	
            	if("".equals(msg)){
            		int value=JOptionPane.showConfirmDialog(null, "Do you want to output bill type "+type[0]+" !", "Comfirm Check!", 0);
            		
            		if(value==0){
            			btnNewButton_2.setEnabled(false);
            			btnNewButton_2.setVisible(false);
            			textArea.setText("Converting... Please wait...");
            			//XXX
            			new BillReport(textArea,chooser.getSelectedFile().toString(),chooser2.getSelectedFile().toString(),type[0],"1");
            			JOptionPane.showConfirmDialog(null, "Converting End");
            			btnNewButton_2.setEnabled(true);
            			btnNewButton_2.setVisible(true);
            		}
            		
            		
            		/*JOptionPane.showMessageDialog(null,""
                			+ "Input file : "+chooser.getSelectedFile()+"\n"
                					+ "Output dir : "+chooser2.getSelectedFile()+"\n"
                							+ "Convert type : "+type[0]+"\n"
                									+ "Confirm value : "+value+"\n"
                											+ "Error msg : "+msg);*/
            	}
            }
        });
		
		

	}
}
