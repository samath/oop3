import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {

	 private static final long serialVersionUID = 1L;
	 
	 protected JTextArea input;
	 protected JTextArea output;
	 protected JButton solve;
	 protected JCheckBox autoCheck;
	 protected JComboBox gridChoices;
	 protected JButton load;
	 
	 private static final int EMPTY = 0;
	 private static final int EASY = 1;
	 private static final int MEDIUM = 2;
	 private static final int HARD = 3;
	
	public SudokuFrame() {
		super("Sudoku Solver");
		
		JPanel panel = createPanel();
		add(panel);
		
		addListeners();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(4,4));
		
		input = new JTextArea(15, 20);
		input.setBorder(new TitledBorder("Puzzle"));
		output = new JTextArea(15, 20);
		output.setBorder(new TitledBorder("Solution"));
		output.setEditable(false);
		
		solve = new JButton("Check");
		autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);
		gridChoices = new JComboBox(new String[] {"none", "easy", "medium", "hard"});
		load = new JButton("Load Grid");
		
		JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
		row.add(solve);
		row.add(autoCheck);
		row.add(load);
		row.add(gridChoices);
		
		
		panel.add(input, BorderLayout.WEST);
		panel.add(output, BorderLayout.EAST);
		panel.add(row, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private void addListeners() {
		input.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) check();
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(autoCheck.isSelected()) check();
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(autoCheck.isSelected()) check();
			}
		});
		solve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				check();
			}
		});
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switch(gridChoices.getSelectedIndex()) {
				case EMPTY:
					input.setText("");
					break;
				case EASY:
					input.setText(Sudoku.gridToText(Sudoku.easyGrid));
					break;
				case MEDIUM:
					input.setText(Sudoku.gridToText(Sudoku.mediumGrid));
					break;
				case HARD:
					input.setText(Sudoku.gridToText(Sudoku.hardGrid));
					break;
				default:
					//empty
				}
			}
		});
		autoCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(autoCheck.isSelected()) check();
			}
		});
	}
	
	private void check(){
		String text = input.getText();
		Sudoku s;
		try{
			s = new Sudoku(Sudoku.textToGrid(text));
		} catch(RuntimeException e) {
			output.setText("Parsing error.");
			return;
		}
		if(!s.validate()) {
			output.setText("Invalid grid.");
			return;
		}
		int numSolutions = s.solve();
		String solved = s.getSolutionText();
		long elapsed = s.getElapsed();
		
		StringBuilder sb = new StringBuilder();
		sb.append(solved + "\n");
		sb.append("solutions: " + numSolutions + "\n");
		sb.append("elapsed: " + elapsed + "ms");
		
		output.setText(sb.toString());
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {/* empty */ }
		
		@SuppressWarnings("unused")
		SudokuFrame frame = new SudokuFrame();
	}

}
