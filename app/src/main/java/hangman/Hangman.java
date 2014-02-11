package hangman;

import android.content.SharedPreferences;
import android.widget.EditText;

/**
 * Created by hroosterhuis on 21/01/14.
 */
public class Hangman {

    public final static String SECRETWORD   = "SECRET WORD"   ;
    public final static String GUESSES      = "GUESSES"       ;
    public final static String STARTGUESSES = "START GUESSES" ;
    public final static String GUESSED      = "GUESSED"       ;

    private String secretWord, guessed, display, alphabet ;
    private int    guesses;
    private final int startGuesses ;

    public Hangman(String secretWord, String guessed, int guesses, int startGuesses){
        this.secretWord = secretWord ;
        this.display = "" ;
        for(int i = 0 ; i < secretWord.length() ;i++){
            display += "_" ;
        }
        this.guesses = guesses;
        this.guessed = guessed;
        this.startGuesses = startGuesses;
        updateDisplay(guessed);
    }

    public Hangman(String secretWord, String guessed, int guesses){
        this(secretWord,guessed,guesses,guesses);
    }

    public Hangman(String secretWord, int guesses){
        this(secretWord,"",guesses);
    }

    public static Hangman load(SharedPreferences sharedPref){
        String secretWord, guessed ;
        int    guesses, startGuesses ;
        secretWord   = sharedPref.getString(SECRETWORD, "");
        guessed      = sharedPref.getString(GUESSED,    "");
        guesses      = sharedPref.getInt(GUESSES,0);
        startGuesses = sharedPref.getInt(STARTGUESSES,8);
        if(guesses <= 0 || secretWord.equals(""))
            return null ;
        Hangman game = new Hangman(secretWord, guessed, guesses, startGuesses);
        if(game.solved())
            return null ;
        return game;
     }

    public void save(SharedPreferences sharedPref){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SECRETWORD,    this.secretWord);
        editor.putString(GUESSED,       this.guessed);
        editor.putInt(GUESSES, this.guesses);
        editor.putInt(STARTGUESSES,     this.startGuesses);
        editor.commit();
    }

    public String display(){
        String screenDisplay = "";
        for(int i = 0 ; i < display.length() ; i++){
            screenDisplay += display.charAt(i) + " ";
        }
        return screenDisplay ;
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

    public boolean guessesLeft(){
        return guesses != 0 ;
    }

    public String getSecretword(){
        return secretWord ;
    }

    public boolean solved(){
        return this.secretWord.equals(this.display);
    }

    public boolean guessWord(String word){
        if(secretWord.equals(word.toLowerCase())){
            display = word ;
            return true ;
        }
        guesses--;
        return false;
    }

    public boolean hasGuessed(char letter){
        return guessed.indexOf(letter) >= 0 ;
    }
    public boolean guess(char letter){
        Character.toLowerCase(letter);
        if(guesses <= 0 || guessed.indexOf(letter) >= 0 || solved())
            return false ;
        if(!secretWord.contains(String.valueOf(letter)))
            guesses--;
        guessed = guessed + letter ;
        return updateDisplay(letter);
    }

    private void updateDisplay(String multipleGuess){
        for(int i = 0 ; i < multipleGuess.length() ; i++){
            updateDisplay(multipleGuess.charAt(i));
        }
    }
    private boolean updateDisplay(char guess){
        boolean found = false ;
        for(int i = 0; i < secretWord.length(); i++){
            if(secretWord.charAt(i) == guess){
                display = display.substring(0,i) + guess + display.substring(i+1);
                found = true ;
            }
        }
        return found ;
    }

}
