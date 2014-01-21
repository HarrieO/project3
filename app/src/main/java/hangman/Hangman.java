package hangman;

/**
 * Created by hroosterhuis on 21/01/14.
 */
public class Hangman {

    private String secretWord ;
    private String guessed    ;
    private String display    ;
    private int guesses ;

    public Hangman(String secretWord, String guessed, int guesses){
        this.secretWord = secretWord ;
        this.display = "" ;
        for(int i = 0 ; i < secretWord.length() ;i++){
            display += "_" ;
        }
        this.guesses = guesses;
        this.guessed = guessed;
        updateDisplay(guessed);
    }

    public Hangman(String secretWord, int guesses){
        this(secretWord,"",guesses);
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

    public boolean guessesLeft(){
        return guesses == 0 ;
    }

    public boolean solved(){
        return this.secretWord.equals(this.display);
    }

    public boolean guess(char letter){
        if(guesses <= 0 || guessed.indexOf(letter) >= 0 || solved())
            return false ;
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
