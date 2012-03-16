import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Panel;
import java.awt.TextArea;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import javax.swing.plaf.SliderUI;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.Parse.Ast;
import com.Parse.Parser;
import com.compilatore.inputParser.GrammarParser;
import com.compilatore.inputParser.InputParser;
import com.compilatore.inputParser.LRInputParser;
import com.compilatore.parser.IParsing;
import com.compilatore.parser.ParsingFactory;
import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SpringLayout;
import java.awt.FlowLayout;


public class HomeGui extends JFrame{
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(HomeGui.class.getName());
	private JFrame frame;
	private Panel rightPanel;
	private String filePath;
	private JButton startParsing;
	private TextArea leftPanel;
	private JTree tree;
	private TreeModel model;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		logger.debug("Start Application");
		PropertyConfigurator.configure("log4j.config");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeGui window = new HomeGui();
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
	public HomeGui() {
		filePath="esempioLibro";
		initialize();
	}
	
	private JFileChooser apriFileChooser;
    private JFileChooser salvaFileChooser;

    private FileFilter txtFileFilter;
    private FileFilter onelineFileFilter,fourlineFileFilter;
    private JTextField input;

    private void operazioneApri() throws FileNotFoundException, Exception {
        if (apriFileChooser == null) {
            apriFileChooser = new JFileChooser();

            apriFileChooser.addChoosableFileFilter(txtFileFilter);
            apriFileChooser.addChoosableFileFilter(onelineFileFilter);
            apriFileChooser.addChoosableFileFilter(fourlineFileFilter);
            
        }

        int stato = apriFileChooser.showOpenDialog(this);

        if (stato == JFileChooser.APPROVE_OPTION) {
            File fileSelezionato = apriFileChooser.getSelectedFile();

            JOptionPane.showMessageDialog(this, "Hai selezionato per l'apertura:\n\n" + fileSelezionato);
            this.filePath=fileSelezionato.getPath();
            startProcess(filePath);
        }
    }

    private void startProcess(String path) throws FileNotFoundException, Exception {
        InputParser parser = new GrammarParser(path);
        ParsingFactory p = new ParsingFactory();
//		IParsing l = p.createParsing();
		IParsing lalr1 = p.createParsing(parser);
		
		if(lalr1!=null){
			leftPanel.setText(lalr1.toString());
			if(!lalr1.isAmbiguos()){
				PrintStream output = new PrintStream(new FileOutputStream("Result.txt"));
				String table = lalr1.toString();
				String [] temp = table.split("\\n");
				for(String o : temp)
					output.println(o);
				output.println("\nGrammatica:");
				output.println(lalr1.getGrammar().toOneLineString());
				output.close();
			}
		}			
		
		//TODO->
        parser = new LRInputParser("Result.txt");
        JTree tree = new JTree(astMethod(parser));
        rightPanel.add(tree);
		//<-TODO
	}

	private void operazioneSalvaConNome() {
        if (salvaFileChooser == null) {
            salvaFileChooser = new JFileChooser();

            salvaFileChooser.addChoosableFileFilter(txtFileFilter);
            salvaFileChooser.addChoosableFileFilter(onelineFileFilter);
            salvaFileChooser.addChoosableFileFilter(fourlineFileFilter);
        }
        
        int stato = salvaFileChooser.showSaveDialog(this);

        if (stato == JFileChooser.APPROVE_OPTION) {
            File fileSelezionato = salvaFileChooser.getSelectedFile();
          
            JOptionPane.showMessageDialog(this, "Hai selezionato per il salvataggio:\n\n" + fileSelezionato);
        }
    }
    

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 825, 623);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		input = new JTextField();

		input.setFont(new Font("Tahoma", Font.PLAIN, 11));

		input.setBounds(577, 120, 139, 20);

		frame.getContentPane().add(input);
		input.setColumns(10);
		
		startParsing = new JButton("PARSE");
		springLayout.putConstraint(SpringLayout.EAST, input, -6, SpringLayout.WEST, startParsing);
		springLayout.putConstraint(SpringLayout.WEST, startParsing, -99, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, startParsing, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(startParsing);
		
		
		//TODO->
        InputParser parser = new LRInputParser("Result.txt");
		try {
			model = new DefaultTreeModel(astMethod(parser));
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		tree=new JTree(model);
		tree.setEditable(true);
		getContentPane().add(tree);
		
		startParsing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        InputParser parser = new LRInputParser("Result.txt");
		        try {
					tree.setVisible(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		//<-TODO
		
		
		JTextPane txtpnDss = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, input, 4, SpringLayout.SOUTH, txtpnDss);
		springLayout.putConstraint(SpringLayout.NORTH, startParsing, 3, SpringLayout.SOUTH, txtpnDss);
		springLayout.putConstraint(SpringLayout.SOUTH, txtpnDss, 106, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, txtpnDss, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, txtpnDss, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtpnDss, -10, SpringLayout.EAST, frame.getContentPane());
		txtpnDss.setBounds(0, 0, 726, 115);
		txtpnDss.setBackground(SystemColor.control);
		txtpnDss.setEditable(false);
		txtpnDss.setText("\t\tProgetto di compilatori e interpreti"+
			"\n\t\tRealizzato da Paolo Pino e Pierluigi Sottile "+
		"\nE' stato realizzato un programma che accetta in ingresso una grammatica context-free e produca in uscita" +
							   "le tabelle action e goto LALR(1), eventuali stati ambigui devono mostrare la situazione"+
							   "di ambiguita scriva i risultati in un file di testo Result.txt."
			+"un compilatore generico che preso in ingresso il file Result.txt traduca la generica " +
							   "frase del linguaggio generato dalla grammatica contenuta nel file Result.txt in un albero" +
							   "sintattico, nel caso in cui la frase appartenga alla grammatica scrivai risultati in " +
							   "un file di testo");
		frame.getContentPane().add(txtpnDss);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("File");

        JMenuItem apriMenuItem = new JMenuItem("Apri...");
        JMenuItem salvaConNomeMenuItem = new JMenuItem("Salva con nome...");

        menu.add(apriMenuItem);
        menu.add(salvaConNomeMenuItem);

        menuBar.add(menu);

        frame.setJMenuBar(menuBar);

        apriMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					operazioneApri();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        salvaConNomeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                operazioneSalvaConNome();
            }
        });

        //-- Filtro "cablato" per .txt --
        txtFileFilter = new TxtFileFilter();

        //-- Filtro per .rtf con la implementazione più generica --
        fourlineFileFilter= new GenericFileFilter("File context-free-gramar format (*.4l)", "4l");

        onelineFileFilter = new GenericFileFilter("File context-free-gramar format (*.1l)", "1l");
		
		JTextPane txtpnFraseDiInput = new JTextPane();
		springLayout.putConstraint(SpringLayout.WEST, input, 7, SpringLayout.EAST, txtpnFraseDiInput);
		springLayout.putConstraint(SpringLayout.WEST, txtpnFraseDiInput, -324, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtpnFraseDiInput, -239, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, txtpnFraseDiInput, -22, SpringLayout.SOUTH, input);
		springLayout.putConstraint(SpringLayout.SOUTH, txtpnFraseDiInput, 0, SpringLayout.SOUTH, input);
		txtpnFraseDiInput.setBackground(SystemColor.menu);
		txtpnFraseDiInput.setEditable(false);
		txtpnFraseDiInput.setText("Frase di input:");

		txtpnFraseDiInput.setBounds(495, 120, 85, 20);

		frame.getContentPane().add(txtpnFraseDiInput);
		
		leftPanel = new TextArea();
		springLayout.putConstraint(SpringLayout.NORTH, leftPanel, 32, SpringLayout.SOUTH, txtpnDss);
		springLayout.putConstraint(SpringLayout.SOUTH, leftPanel, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, leftPanel, 380, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, leftPanel, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(leftPanel);
		
		rightPanel = new Panel();
		FlowLayout flowLayout = (FlowLayout) rightPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		springLayout.putConstraint(SpringLayout.NORTH, rightPanel, 138, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, input, -6, SpringLayout.NORTH, rightPanel);
		springLayout.putConstraint(SpringLayout.WEST, rightPanel, 6, SpringLayout.EAST, leftPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, rightPanel, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, rightPanel, -6, SpringLayout.EAST, frame.getContentPane());
		rightPanel.setBackground(SystemColor.text);
		frame.getContentPane().add(rightPanel);

		try {
			startProcess("esempioLibro.4l");    //TODO:
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private DefaultMutableTreeNode astMethod(InputParser parser) throws Exception {
		Parser parserProgram = (Parser)parser.parse();
		//BufferedReader leggi = new BufferedReader(new InputStreamReader(System.in));
//		parserProgram.setInput(input.getText());                     //TODO: da rimuovere	
		parserProgram.setInput("d=d$)");			/////////ATTENZIONE!!!!  scrivi qui la stringa di input (es. sul libro id*id+id$)
		switch(parserProgram.parse()){
			case ACCEPT:
				logger.debug("ACCEPT");		
				Ast ast = new Ast(parserProgram.getHistory());
				ast.initFromHistory();
//				this.tree.setModel(ast.getRoot());			
				return ast.getRoot();			
		}	
		return null;
	}
}
