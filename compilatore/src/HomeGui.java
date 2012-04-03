import inputParser.InputParser;

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
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import parserProgram.HistoryElement;
import parserProgram.St;
import parserProgram.ParserProgram;

import com.thoughtworks.xstream.XStream;

import contextFree.grammar.IGrammar;
import contextFree.scanner.IScanner;
import contextFree.scanner.ScannerFactory;

import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SpringLayout;
import java.awt.FlowLayout;

/**
 * The gui access.
 * @author Paolo Pino
 *
 */
public class HomeGui extends JFrame{
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(HomeGui.class.getName());
	private JFrame frame;
	private Panel rightPanel;
	private String filePath;
	private JButton startParsing;
	private TextArea leftPanel,automaText;
	private JTree tree;
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
	 * Initialize GUI component and start the process.
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 * @author Paolo Pino
	 */
	public HomeGui() throws FileNotFoundException, Exception {
		filePath="esempioLibro.4l";
		initialize();
		startProcess(filePath);
	}
	
	private JFileChooser apriFileChooser;
//  private JFileChooser salvaFileChooser;

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

            JOptionPane.showMessageDialog(this, "Il file \"" + fileSelezionato + "\" verra' analizzato...\n...i risultati saranno scritti nel file \"Result.txt\"");
            this.filePath=fileSelezionato.getPath();
            startProcess(filePath);
        }
    }

    /**
     * Start the parsing of grammar file and create result file.
     * @param path the path of grammar file (ex. grammar.4l)
     * @throws FileNotFoundException
     * @throws Exception
     * @author Paolo Pino
     */
    private void startProcess(String path) throws FileNotFoundException, Exception {
        long startTime = System.currentTimeMillis();
    	IGrammar grammar= (IGrammar) new InputParser(path).parse();
//		IParsing l = p.createParsing();
		IScanner lalr1 = ScannerFactory.createParser(grammar);
		
		if(lalr1!=null){
			leftPanel.setText(lalr1.toString());
			if(!lalr1.isAmbiguos()){
				automaText.setText(lalr1.getAutoma().toString());
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
        long endTime = System.currentTimeMillis();
		logger.info("Total elapsed time in execution of grammar parsing and \n\t\t\t\tAction e Goto table is :"+ (endTime-startTime));
	}

//    /**
//     * Save operation.
//     */
//	private void operazioneSalvaConNome() {
//        if (salvaFileChooser == null) {
//            salvaFileChooser = new JFileChooser();
//
//            salvaFileChooser.addChoosableFileFilter(txtFileFilter);
//            salvaFileChooser.addChoosableFileFilter(onelineFileFilter);
//            salvaFileChooser.addChoosableFileFilter(fourlineFileFilter);
//        }
//        
//        int stato = salvaFileChooser.showSaveDialog(this);
//
//        if (stato == JFileChooser.APPROVE_OPTION) {
//            File fileSelezionato = salvaFileChooser.getSelectedFile();
//          
//            JOptionPane.showMessageDialog(this, "Hai selezionato per il salvataggio:\n\n" + fileSelezionato);
//        }
//    }
    

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 899, 629);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		tree = new JTree();
		tree.setVisible(false);
		input = new JTextField();

		input.setFont(new Font("Tahoma", Font.PLAIN, 11));

		input.setBounds(577, 120, 139, 20);

		frame.getContentPane().add(input);
		input.setColumns(10);
		
		startParsing = new JButton("PARSE");
		springLayout.putConstraint(SpringLayout.WEST, input, -133, SpringLayout.WEST, startParsing);
		springLayout.putConstraint(SpringLayout.EAST, input, -6, SpringLayout.WEST, startParsing);
		springLayout.putConstraint(SpringLayout.WEST, startParsing, -99, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, startParsing, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(startParsing);
		
		JTextPane txtpnDss = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, input, 4, SpringLayout.SOUTH, txtpnDss);
		springLayout.putConstraint(SpringLayout.SOUTH, input, 26, SpringLayout.SOUTH, txtpnDss);
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
//        JMenuItem salvaConNomeMenuItem = new JMenuItem("Salva con nome...");

        menu.add(apriMenuItem);
//        menu.add(salvaConNomeMenuItem);

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

//        salvaConNomeMenuItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                operazioneSalvaConNome();
//            }
//        });
        
        /**
         * When clicked parse "Result.txt" file and create the ST.
         * @author Paolo Pino
         */
		startParsing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				long startTime = System.currentTimeMillis();
				try {
					ParserProgram parserProgram = (ParserProgram) new InputParser("Result.txt").parse();
		        	DefaultMutableTreeNode root = stMethod(parserProgram);
		        	if(root!=null){
		        		tree.setModel(new JTree(root).getModel());
		        		tree.expandPath(new TreePath(root.getPath()));
		        		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		        	    renderer.setOpenIcon(null);
		        	    renderer.setClosedIcon(null);
		        	    renderer.setLeafIcon(null);
		        	    tree.setCellRenderer(renderer);
		        		for (int i = 0; i < tree.getRowCount(); i++) {
		        			tree.expandRow(i);
		        		}
		        		tree.setVisible(true);
			            tree.updateUI();
		        	}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame.getContentPane(), "Insert input");
					e.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame.getContentPane(), "Uno o più caratteri tra quelli inseriti\nnon sono ammessi dalla grammatica corrente");
					e.printStackTrace();
//					logger.error("Parsing Result.txt fallito",e);
				}
		        long endTime = System.currentTimeMillis();
				logger.info("Total elapsed time in parsing grammar and table\n\t\t\t\tand fot creation of AST is :"+ (endTime-startTime));
			}
		});

        //-- Filtro "cablato" per .txt --
        txtFileFilter = new TxtFileFilter();

        //-- Filtro per .rtf con la implementazione piï¿½ generica --
        fourlineFileFilter= new GenericFileFilter("File context-free-gramar format (*.4l)", "4l");

        //onelineFileFilter = new GenericFileFilter("File context-free-gramar format (*.1l)", "1l");
		
		JTextPane txtpnFraseDiInput = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, txtpnFraseDiInput, 4, SpringLayout.SOUTH, txtpnDss);
		springLayout.putConstraint(SpringLayout.WEST, txtpnFraseDiInput, -324, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtpnFraseDiInput, -239, SpringLayout.EAST, frame.getContentPane());
		txtpnFraseDiInput.setBackground(SystemColor.menu);
		txtpnFraseDiInput.setEditable(false);
		txtpnFraseDiInput.setText("Frase di input:");

		txtpnFraseDiInput.setBounds(495, 120, 85, 20);

		frame.getContentPane().add(txtpnFraseDiInput);
		
		leftPanel = new TextArea();
		springLayout.putConstraint(SpringLayout.NORTH, leftPanel, 32, SpringLayout.SOUTH, txtpnDss);
		springLayout.putConstraint(SpringLayout.WEST, leftPanel, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, leftPanel, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, leftPanel, 349, SpringLayout.WEST, frame.getContentPane());
		leftPanel.setEditable(false);
		frame.getContentPane().add(leftPanel);
		
		rightPanel = new Panel();
		springLayout.putConstraint(SpringLayout.NORTH, rightPanel, 6, SpringLayout.SOUTH, txtpnFraseDiInput);
		springLayout.putConstraint(SpringLayout.WEST, rightPanel, 6, SpringLayout.EAST, leftPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, rightPanel, 0, SpringLayout.SOUTH, leftPanel);
		springLayout.putConstraint(SpringLayout.EAST, rightPanel, -324, SpringLayout.EAST, frame.getContentPane());
		FlowLayout flowLayout = (FlowLayout) rightPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		rightPanel.setBackground(SystemColor.text);
        rightPanel.add(tree);
		frame.getContentPane().add(rightPanel);
		
		automaText = new TextArea();
		springLayout.putConstraint(SpringLayout.NORTH, automaText, 6, SpringLayout.SOUTH, input);
		springLayout.putConstraint(SpringLayout.WEST, automaText, 10, SpringLayout.EAST, rightPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, automaText, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, automaText, -10, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, txtpnFraseDiInput, -6, SpringLayout.NORTH, automaText);
		automaText.setBackground(SystemColor.menu);
		automaText.setEditable(false);
		frame.getContentPane().add(automaText);
	}

	/**
	 * Create the ST.
	 * @param parser the object that rappresent "Result.txt"
	 * @return DefaultMutableTreeNode root of ST
	 * @throws Exception
	 * @author Paolo Pino
	 */
	private DefaultMutableTreeNode stMethod(ParserProgram parserProgram) throws Exception {
		//BufferedReader leggi = new BufferedReader(new InputStreamReader(System.in));
		String in = this.input.getText();
		if(in.length()==0) throw new IOException("Nessuna stringa in ingresso");
		char end = in.charAt(in.length()-1);
		if(end!='$') {
			logger.info("Carattere di terminazione $ mancante...");
			in+="$";
			logger.info("Carattere $ aggiunto automaticamente ");
		}
		parserProgram.setInput(in);                  
		switch(parserProgram.parse()){
			case ACCEPT:
				logger.info("ACCEPT");		
				St st = new St(parserProgram.getHistory());
				st.initFromHistory();
//				this.tree.setModel(ast.getRoot());	
				for(HistoryElement e : parserProgram.getHistory())
				System.out.println(e.toString());
				writeToXml(st.getRoot());
				return st.getRoot();
			case ERROR:
				logger.info("ERROR");
				JOptionPane.showMessageDialog(frame.getContentPane(), "La frase non appartiene alla grammatica");
				return null;
			case INVALID_IN:
				logger.warn("Uno o piu' caratteri tra quelli inseriti non sono ammessi dalla grammatica corrente");
        		JOptionPane.showMessageDialog(frame.getContentPane(), "Uno o piu' caratteri tra quelli inseriti non sono ammessi dalla grammatica corrente");
        		break;
			default:
				throw new Exception("Invalid result from parse() method");
		}
		return null;	
	}

	/**
	 * Store an ST into xml file named "ST.xml"
	 * @param root the root of AST
	 * @throws FileNotFoundException
	 * @author Paolo Pino
	 */
	private void writeToXml(DefaultMutableTreeNode root) throws FileNotFoundException { 
		XStream xstream = new XStream();
		String xml = xstream.toXML(root);
		PrintStream output = new PrintStream(new FileOutputStream("ST.xml"));
		String [] temp = xml.split("\\n");
		for(String o : temp)
			output.println(o);
		output.close();
	}
}
