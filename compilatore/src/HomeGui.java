import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.TextArea;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.filechooser.FileFilter;
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


public class HomeGui extends JFrame{
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(HomeGui.class.getName());
	private JFrame frame;
	private JTree tree;
	private String filePath;
	private JButton startParsing;
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
    JTextPane leftPanel;
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
		frame.setBounds(100, 100, 742, 541);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		input = new JTextField();
		input.setBounds(577, 120, 139, 20);
		frame.getContentPane().add(input);
		input.setColumns(10);
		
		JTextPane txtpnDss = new JTextPane();
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
		
		
		Panel fileChooserPanel = new Panel();
		fileChooserPanel.setBounds(0, 462, 726, 20);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setVisible(true);
		fileChooserPanel.add(fileChooser);
		frame.getContentPane().add(fileChooserPanel);
		
		leftPanel = new JTextPane();
		leftPanel.setBounds(0, 115, 490, 372);
		leftPanel.setEditable(false);
		frame.getContentPane().add(leftPanel);
		JTextPane txtpnFraseDiInput = new JTextPane();
		txtpnFraseDiInput.setBackground(SystemColor.menu);
		txtpnFraseDiInput.setEditable(false);
		txtpnFraseDiInput.setText("Frase di input:");
		txtpnFraseDiInput.setBounds(495, 120, 85, 20);
		frame.getContentPane().add(txtpnFraseDiInput);
		
		startParsing = new JButton("PARSE");
		startParsing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        InputParser parser = new LRInputParser("Result.txt");
		        try {
					astMethod(parser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		startParsing.setBounds(627, 150, 89, 23);
		frame.getContentPane().add(startParsing);
	}

	private void astMethod(InputParser parser) throws Exception {
		Parser parserProgram = (Parser)parser.parse();
		System.out.println("\nDigitare la stringa in input: "); 
		//BufferedReader leggi = new BufferedReader(new InputStreamReader(System.in));
		parserProgram.setInput(input.getText());                     //TODO: da rimuovere	
//		parserProgram.setInput(leggi.readLine());                     /////////ATTENZIONE!!!!  scrivi qui la stringa di input (es. sul libro id*id+id$)
		switch(parserProgram.parse()){
			case ACCEPT:
				logger.debug("ACCEPT");		
				Ast ast = new Ast(parserProgram.getHistory());
				ast.initFromHistory();
				this.tree = new JTree(ast.getRoot());
				this.tree.setSize(275, 300);
				this.tree.setVisible(true);
				this.frame.getContentPane().add(tree, BorderLayout.CENTER);
		}	
	}
}
