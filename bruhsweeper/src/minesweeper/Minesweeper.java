package minesweeper;

import javax.swing.JOptionPane;

public class Minesweeper {

    
    public void proceed(int s) {
    s=20;
        int toughness = 1;
        Object[] options = {"Nem", "Mellem", "Svær"};
        toughness = JOptionPane.showOptionDialog(null,
                "Vælg din sværhedsgrad", "Sværhedsgrad",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);
        	
        if(toughness == -1)
            System.exit(0);
        newGame = new game(s, toughness);
        newGame.main(newGame, s);

    }
    
    public static void main(String[] args) {
    	  int size = 20;
    	 

    	  
        minesweeper = new Minesweeper();
		minesweeper.proceed(size);
    }
    
    private static Minesweeper minesweeper;
    private static game newGame;
}

