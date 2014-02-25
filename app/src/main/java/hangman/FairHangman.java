package hangman;

import android.util.Log;

/**
 * Created by hroosterhuis on 23/02/14.
 */
public class FairHangman extends Hangman {

    String word, display ;

    public FairHangman(String word, String guessed, int guesses, int startGuesses){
        super(guessed, guesses, startGuesses);
        init(word);
        updateDisplay(guessed);
    }

    public FairHangman(String word, String guessed, int guesses){
        super(guessed, guesses);
        init(word);
        updateDisplay(guessed);
    }

    public FairHangman(String word, int guesses){
        super(guesses);
        init(word);
    }

    private void init(String word){
        this.word = word.toLowerCase();
        this.display = "" ;
        for(int i = 0 ; i < word.length() ;i++){
            display += "_" ;
        }

    }

    @Override
    public String display() {
        return display ;
    }

    public boolean solved(){
        return this.getSecretWord().equals(this.display());
    }

    @Override
    public String getSecretWord() {
        return word ;
    }

    @Override
    public boolean evil() {
        return false;
    }

    @Override
    public void updateDisplay(String multipleGuess) {
        for(int i = 0 ; i < multipleGuess.length() ; i++){
            updateDisplay(multipleGuess.charAt(i));
        }
    }

    public boolean updateDisplay(char guess){
        boolean found = false ;
        for(int i = 0; i < word.length(); i++){
            if(word.charAt(i) == guess){
                display = display.substring(0,i) + guess + display.substring(i+1);
                found = true ;
            }
        }
        return found ;
    }

    @Override
    public boolean guessLetter(char letter) {
        Log.d("Hangman", "Guessed: " + String.valueOf(letter));
        return updateDisplay(letter);
    }
}
