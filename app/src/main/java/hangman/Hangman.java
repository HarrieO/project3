package hangman;

import android.content.SharedPreferences;

import nl.mprog.apps.Hangman10196129.database.WordDatabase;

/**
 * Created by hroosterhuis on 21/01/14.
 */
public abstract class Hangman {

    public final static String SECRETWORD   = "SECRET WORD"   ;
    public final static String GUESSES      = "GUESSES"       ;
    public final static String STARTGUESSES = "START GUESSES" ;
    public final static String GUESSED      = "GUESSED"       ;
    public final static String EVIL         = "EVIL"       ;

    private String guessed, incorrect, alphabet ;
    private int    guesses;
    private final int startGuesses ;

    public Hangman(String guessed, int guesses, int startGuesses){
        this.guesses = guesses;
        this.guessed = guessed;
        this.startGuesses = startGuesses;
        this.incorrect = "";
        updateDisplay(guessed);
    }

    public Hangman(String guessed, int guesses){
        this(guessed,guesses,guesses);
    }

    public Hangman(int guesses){
        this("",guesses);
    }

    public static Hangman load(SharedPreferences sharedPref, WordDatabase db){
        String secretWord, guessed ;
        int    guesses, startGuesses ;
        boolean evil ;
        secretWord   = sharedPref.getString(SECRETWORD, "");
        guessed      = sharedPref.getString(GUESSED,    "");
        guesses      = sharedPref.getInt(GUESSES,0);
        startGuesses = sharedPref.getInt(STARTGUESSES,8);
        evil         = sharedPref.getBoolean(EVIL, false);
        if(guesses <= 0 || secretWord.equals(""))
            return null ;

        Hangman game ;
        /*if(evil)
            game = new Hangman(secretWord, guessed, guesses, startGuesses);
        else */
            //game = new FairHangman(secretWord, guessed, guesses, startGuesses);
        game = new EvilHangman(db, secretWord, guessed, guesses, startGuesses);
        if(game.solved())
            return null ;
        return game;
     }

    public void save(SharedPreferences sharedPref){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SECRETWORD,    this.getSecretWord());
        editor.putString(GUESSED,       this.guessed);
        editor.putInt(GUESSES,          this.guesses);
        editor.putInt(STARTGUESSES,     this.startGuesses);
        editor.putBoolean(EVIL, this.evil());
        editor.commit();
    }

    public abstract String display();
    public abstract String getSecretWord();
    public abstract boolean evil();
    public abstract void updateDisplay(String multipleGuess);
    public abstract boolean guessLetter(char letter);

    public String getIncorrect(){
        return  incorrect ;
    }
    public int guesses(){
        return guesses ;
    }
    
    public int startGuesses(){
        return startGuesses;
    }

    public int incorrectGuesses(){
        return startGuesses - guesses ;
    }

    public int score(){
        if(!solved())
            return 0 ;
        return (int) Math.round(0.7 * Math.pow(26-incorrectGuesses(),2) + (getSecretWord().length()));
    }

    public boolean guessesLeft(){
        return guesses != 0 ;
    }

    public abstract boolean solved();

    public boolean hasGuessed(char letter){
        return guessed.indexOf(letter) >= 0 ;
    }
    
    public boolean guess(char letter){
        letter = Character.toLowerCase(letter);
        if(guesses <= 0 || guessed.indexOf(letter) >= 0 || solved())
            return false ;
        guessed = guessed + letter ;
        boolean correct = guessLetter(letter);
        if(!correct){
            guesses--;
            incorrect = incorrect + String.valueOf(letter);
        }
        return correct;
    }

}
