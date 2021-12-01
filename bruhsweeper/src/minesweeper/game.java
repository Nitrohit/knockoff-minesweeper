package minesweeper;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import javax.imageio.ImageIO;

public class game extends JFrame {

	int time0; // tid
	int time; //placeholder tid
	   private JButton[][] buttons;  // Felterne
	    private JPanel panel1;  // krofferknap panelet
	    private JPanel panel2;  // felterne panelet
	    private JLabel flagsLabel;  // tilbageværende flag
	    private JButton smileButton;  // krofferknappen
	    private JLabel timeLabel;  // label der viser tid

	    private int noOfMines = 0;  // antal miner
	    private int[][] playingField;  // int array der har informationen på hvert felt
	    private boolean[][] revealed;  //tjekker om man har klikket
	    private int noOfRevealed;  // hvor mange?
	    private boolean[][] flagged;  // eller er det blevet stenet?
	    
	    private Image smiley; //Import Image
	    private Image newSmiley; //Scaler den
	    private Image flag; //-||-
	    private Image newFlag;//-||-
	    private Image mine;//-||-
	    private Image newMine;//-||-
	    private Image tabt;//-||-
	    private Image newtabt;//-||-
	    private Image win; // ikke brugt
	    private Image newWin; // ikke brugt
	    
	    private boolean smiling;  // smiler jeg ? eller smiler jeg ikke ?

	    public static final int MAGIC_SIZE = 30; //brugt mindre end forventet
    
    public game(int size, int toughness) {
    	size = 20;
        noOfMines = size*(1 + toughness/2); // hvor mange bomber der skal vÃ¦re (mat A btw)
        this.setSize(size*MAGIC_SIZE, size*MAGIC_SIZE + 50); //størrelsen
        this.setTitle("SteenStryger"); //vores mega kreative navn
        setLocationRelativeTo(null);//idk google sagde jeg skulle skrive det her
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);//så den faktisk lukker
    }

    private void setMines(int size) { //minesætte metoden
        Random rand = new Random(); //så den sætter dem random i playingField
        playingField = new int[size][size]; //gør det til en intarray, siden det er en private 
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                playingField[i][j] = 0;
            }//vores 2 forloops der kører igennem alle tallende på spillefeltet
        }

        int count = 0; //placeholder variable til vores miner så der ikke kommer uendelige miner
        int xPoint; //xværdien	
        int yPoint; //y værdien
        while(count<noOfMines) { //et whileloop der kører mens count er mindre en noOfMines, og number of mines ændre sig, ift. sværhedsgraden man vælger.
            xPoint = rand.nextInt(size);
            yPoint = rand.nextInt(size);
            if (playingField[xPoint][yPoint]!=-1) {
                playingField[xPoint][yPoint]=-1;  // -1 er bomber
                count++;
            } // nu bliver den sat tilfældigt på playingField[][]
        }
        
        
        for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (playingField[i][j]==-1) {
                    for (int k = -1; k <= 1 ; k++) {
                    for (int l = -1; l <= 1; l++) {
                        
                        try {
                            if (playingField[i+k][j+l]!=-1) {
                                playingField[i+k][j+l] += 1;
                            }
                        }
                        catch (Exception e) {
                            // sÃ¥ den ikke spasser ud
                        }
                    }
                    }
            }
        }
        }
    }

    public void main(game frame, int size) {
  
        GameEngine gameEngine = new GameEngine(frame);
        MyMouseListener myMouseListener = new MyMouseListener(frame);
        JPanel mainPanel = new JPanel(); //hovedpanelet

        panel1 = new JPanel();
        panel2 = new JPanel();

        this.noOfRevealed = 0;//så du ikke starter med nogle åben lol

        revealed = new boolean[size][size];
        flagged = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }

        // billederne
        try {
            smiley = ImageIO.read(getClass().getResource("images/Smiley.jpg")); //kroffer :))
            newSmiley = smiley.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);

            tabt = ImageIO.read(getClass().getResource("images/tabt.jpg")); //sur kroffer :(((
            newtabt = tabt.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);

            flag = ImageIO.read(getClass().getResource("images/flag.png")); //steen aka -03 undvigeren
            newFlag = flag.getScaledInstance(MAGIC_SIZE, MAGIC_SIZE, java.awt.Image.SCALE_SMOOTH);

            mine = ImageIO.read(getClass().getResource("images/bombe.png")); //aaaah det er vores -03
            newMine = mine.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            
            win = ImageIO.read(getClass().getResource("images/win.jpg"));//mega seje kroffer (ikke brugt)
            newWin = win.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
        }
        catch (Exception e){ //haha ellers kan den ikke lide mig :))
        }

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //y akse knapper til vores spil

        BoxLayout g1 = new BoxLayout(panel1, BoxLayout.X_AXIS); //x aksen
        panel1.setLayout(g1); //adding that shit

        JLabel jLabel1 = new JLabel(" Flag = "); // så man kan se hvor mange flag / bomber der er tilbage
        jLabel1.setAlignmentX(Component.LEFT_ALIGNMENT); // så den er til venstre
        jLabel1.setHorizontalAlignment(JLabel.LEFT); // så den er til venstre lol.
        flagsLabel = new JLabel(""+this.noOfMines); // gør den afhængig af vores noOfMines

        smiling = true; // :)
        smileButton = new JButton(new ImageIcon(newSmiley)); //jeg smiler
        smileButton.setPreferredSize(new Dimension(50, 50));// jeg laver den så man kan se den
        smileButton.setMaximumSize(new Dimension(50, 50)); // idk det her skulle være der for at det virkede
        smileButton.setBorderPainted(true); // laver den der tynde sorte ting rundt om
        smileButton.setName("smileButton"); // navngiver knappen
        smileButton.addActionListener(gameEngine); // sætter den til vores game engine

        JLabel jLabel2 = new JLabel(" Tid :"); // tid
        timeLabel = new JLabel("0"); // så tiden starter på 0 lol
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT); //sætter den til højre
        timeLabel.setHorizontalAlignment(JLabel.RIGHT); //det jeg lige sagde
        
        panel1.add(jLabel1); 												//adding this shit to the panel
        panel1.add(flagsLabel);
        panel1.add(Box.createRigidArea(new Dimension((size-1)*15 - 80,50)));
        panel1.add(smileButton, BorderLayout.PAGE_START);
        panel1.add(Box.createRigidArea(new Dimension((size-1)*15 - 85,50)));
        panel1.add(jLabel2);
        panel1.add(timeLabel);
        
        GridLayout g2 = new GridLayout(size, size);		//laver en grid til vores knapper								
        panel2.setLayout(g2); 

        buttons = new JButton[size][size]; //laver en stabil knap

        for (int i=0; i<size; i++) {
            for (int j=0; j<size ; j++ ) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(12, 12)); // gør vores 
                buttons[i][j].setBorder(new LineBorder(Color.BLACK));
                buttons[i][j].setBorderPainted(true);
                buttons[i][j].setName(i + " " + j);
                buttons[i][j].addActionListener(gameEngine);
                buttons[i][j].addMouseListener(myMouseListener);
                panel2.add(buttons[i][j]);
            }
        } //vores mega kompliceret algorythme der ved hjælp af AI sætter knapper til størrelsen af boardet. Eller noget

        //begge panels done

        mainPanel.add(panel1);
        mainPanel.add(panel2);
        frame.setContentPane(mainPanel);
        this.setVisible(true);//adding this shit to the mainpanel gang
        
       
        setMines(size);// C-C-CRAZY ALGORYTHM BRO

       
        timeThread timer = new timeThread(this);
        timer.start(); // Timer, :))

    }

    // vitterligt en timer
    public void timer() {
        String[] time = this.timeLabel.getText().split(" ");
        time0 = Integer.parseInt(time[0]);
        ++time0;
        this.timeLabel.setText(Integer.toString(time0) + " s");

    } // logikken til timeren

    //logikken til smileknappen ;) den bedste feature
    public void changeSmile() {
        if (smiling) {
            smiling=false;
            smileButton.setIcon(new ImageIcon(newtabt));//is he smiling?
        } else {
            smiling=true;
            smileButton.setIcon(new ImageIcon(newSmiley));//he do be kinda smilin' doe
        }
    }

    // hvis man højreklikker en et felt
    public void buttonRightClicked(int x, int y) {
        if(!revealed[x][y]) {
            if (flagged[x][y]) {
                buttons[x][y].setIcon(null);// gør knappernes icon til, ja ingenting
                flagged[x][y] = false; // så de ikke starter med at have en steen på sig
                int steen = Integer.parseInt(this.flagsLabel.getText());
                ++steen; //hvis man fjerne sin steen
                this.flagsLabel.setText(""+steen);
            }
            else {
                if (Integer.parseInt(this.flagsLabel.getText())>0) {
                    buttons[x][y].setIcon(new ImageIcon(newFlag));
                    flagged[x][y] = true; //når man sætter en steen
                    int steen = Integer.parseInt(this.flagsLabel.getText());
                    --steen; //hvis man sætter en steen
                    this.flagsLabel.setText(""+steen);
                }
            }
        }
    }

    private boolean gameWon() {
        // noOfRevealed - noOfMines skal være lig med den samlede mængde af felter
        return (this.noOfRevealed) == (Math.pow(this.playingField.length, 2) - this.noOfMines);
    }

    // når man venstreklikker
    public void buttonClicked(int x, int y) { 
        if(!revealed[x][y] && !flagged[x][y]) { //hvis den ikke er revealed og den ikke er flagged så bliver den vist hvis man trykker på den arbitrærer knap
            revealed[x][y] = true; 

            switch (playingField[x][y]) {//den kører switch kommandoen 1 gang og evaluere om det felt du har trykket på har -1 i case (hvilket er en bombe) og hvis den har det taber du
                case -1:
                    try {
                        buttons[x][y].setIcon(new ImageIcon(newMine));
                    } catch (Exception e1) {
                    }
                    buttons[x][y].setBackground(Color.RED); //uh ohhh du tabte :(( hyg dig med dit -03
                    try {
                        smileButton.setIcon(new ImageIcon(newtabt));
                    } catch (Exception e2) {
                    }

                    JOptionPane.showMessageDialog(this, "Du fik -03 !", null,
                            JOptionPane.ERROR_MESSAGE);
                

                    System.exit(0);

                    break;

                case 0: //0 er et felt der ikke har en bombe, og hvis du klikker på den af dem bliver den vist. + hvis gameWon er = true så vinder du.
                    buttons[x][y].setBackground(Color.lightGray);
                    ++this.noOfRevealed;

                    if (gameWon()) {
                    	
                        JOptionPane.showMessageDialog(rootPane,
                            "Tillykke Du fik 12 ! På kun " + time0 + " sekunder");
                        
                    } 

  
                    for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        try {
                            buttonClicked(x + i, y + j);
                        }
                        catch (Exception e3) {
                            // sÃ¥ den ikke spasser
                        }
                    }
                    }

                    break;

                default: //den kører den her kode hvis ingen af de givne cases er lig med vores expression. altså hvis du trkker et "tomt" sted
                    buttons[x][y].setText(Integer.toString(playingField[x][y]));
                    buttons[x][y].setBackground(Color.LIGHT_GRAY);
                    ++this.noOfRevealed;
                    if (gameWon()) {
                        JOptionPane.showMessageDialog(rootPane, "Tillykke Du fik 12 ! På kun " + time0 +  " sekunder");
                        smileButton.setIcon(new ImageIcon(newWin)); //icon virker ikke, har ikke tid til at fikse det. den kommer med mange errors MEN DET VIRKER :))))

                        System.exit(0);
                    }

                    break;
            }
        }
        
    }

 

}

class GameEngine implements ActionListener {
    game parent;
    
    GameEngine(game parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object eventSource = e.getSource();
        JButton clickedButton = (JButton) eventSource;
        String name = clickedButton.getName();
        if (name.equals("smileButton")) {
            parent.changeSmile();
        }
        else {
            String[] xy = clickedButton.getName().split(" ", 2);
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            parent.buttonClicked(x, y);

        }
    }
}

class MyMouseListener implements MouseListener {
    game parent;

    MyMouseListener(game parent) {
        this.parent = parent;
    }

    public void mouseExited(MouseEvent arg0){
    }
    public void mouseEntered(MouseEvent arg0){
    }
    public void mousePressed(MouseEvent arg0){
    }
    public void mouseClicked(MouseEvent arg0){
    }

    @Override
    public void mouseReleased(MouseEvent arg0) { //højreklik
        if(SwingUtilities.isRightMouseButton(arg0)){
            Object eventSource = arg0.getSource();
            JButton clickedButton = (JButton) eventSource;
            String[] xy = clickedButton.getName().split(" ", 2);
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            parent.buttonRightClicked(x, y);
        }
    }
}

class timeThread implements Runnable {
    private Thread t;
    private game newGame;

    timeThread(game newGame) {
        this.newGame = newGame;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(1000); // venter 1000ms til at kører timer metoden
                newGame.timer();
            }
            catch (InterruptedException e) {
                System.exit(0);
            }
        }
    }

    public void start() {
        if (t==null) {
            t = new Thread(this);
            t.start();
        }
    }
}

