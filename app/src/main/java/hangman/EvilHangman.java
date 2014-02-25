package hangman;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nl.mprog.apps.Hangman10196129.database.WordDatabase;

/**
 * Created by hroosterhuis on 24/02/14.
 */
public class EvilHangman extends Hangman {

    private String state ;
    private int length;
    private  WordDatabase db ;
    private ArrayList<String> impossibles ;

    public EvilHangman(WordDatabase db, int length, int guesses){
        super(guesses);
        this.length = length;
        init(db);
    }

    public EvilHangman(WordDatabase db, String state, String guessed, int guesses, int startGuesses){
        super(guessed, guesses, startGuesses);
        this.length = state.length();
        init(db,state);
        updateDisplay(guessed);
    }

    private void init(WordDatabase db){
        String startState = "" ;
        for(int i = 0 ; i < length ;i++){
            startState += "_" ;
        }
        init(db, startState);
    }

    private void init(WordDatabase db, String startState){
        this.db = db ;
        this.state = startState;
        impossibles = new ArrayList<String>();
    }

    @Override
    public String state() {
        return state;
    }

    /**
     * Returns game state, if out of guesses random possibility is chosen.
     * @return State of the game, can be an unfinished game state.
     */
    @Override
    public String getSecretWord() {
        if(!guessesLeft())
            return db.getRandom(state,new ArrayList<String>(),impossibles).toLowerCase();
        else
            return state ;
    }

    @Override
    public boolean evil() {
        return true;
    }

    /**
     * Updates display when game is loaded.
     * @param multipleGuess loaded guesses
     */
    @Override
    public void updateDisplay(String multipleGuess) {
        ArrayList<Character> guessed = new ArrayList<Character>(26);
        for(int i = 0; i < multipleGuess.length();i++)
            if(!guessed.contains(multipleGuess.charAt(i)))
                guessed.add(multipleGuess.charAt(i));
        for(char c : guessed)
            if(state.indexOf(c) < 0)
                impossibles.add(addWildCards(c,1));
    }

    /**
     * Data structure for selecting the best state.
     */
    private class Pair{
        public String state ;
        public Long   count ;
        public int added;
        public Pair(String state, Long count, int added){
            this.state = state;
            this.count = count;
            this.added = added;
        }
        public void set(String state, Long count, int added){
            this.state = state;
            this.count = count;
            this.added = added;
        }
    }

    /**
     * Finds best state to go to after guess.
     * Best state is the state with the most possible words.
     * @param guess Guessed letter.
     * @return Pair structure for the best state to select.
     */
    public Pair best(char guess){
        ArrayList<String> next = new ArrayList<String>();
        long both = getFullCount(state,next);
        next.add("%" + String.valueOf(guess) + "%");
        long without = getFullCount(state,next);
        long with    = both - without;
        Log.d("Hangman", "Guess " + guess);
        Log.d("Hangman", state + " both " + both);
        Log.d("Hangman", state + " with " + with);
        Log.d("Hangman", state + " without "+ without);
        if(with <= without)
            return new Pair(state,without,0) ;
        else
            return bestAdd(state,both,guess,0, new Pair(state,without,0));
    }

    /**
     * Returns the best state after placing the guess or not.
     */
    public Pair bestAdd(String state, long previous, char guess, int start, Pair best){
        return  bestAdd(state, previous, guess, start, best, new ArrayList<String>(), 0);
    }
    public Pair bestAdd(String state, long previous, char guess, int index, Pair best,
                        ArrayList<String> unlike, int added){
        if(index >= length){
            if(previous > best.count){
                best.set(state, previous, added);
            }
        } else if(state.charAt(index) != '_'){
            return  bestAdd(state,previous, guess, index+1, best, unlike, added);
        } else {
            long both = previous;
            String cur = state.substring(0,index) + String.valueOf(guess) + state.substring(index+1);
            // possibilities with a letter added on index
            long with = getFullCount(cur,unlike);
            // possibilities without a letter added on index
            long without = both - with;
            Log.d("Hangman", cur   + " with " + with);
            Log.d("Hangman", state + " without "+ without);
            if(without > best.count){
                unlike.add(cur);
                bestAdd(state,without,guess,index+1,best,unlike,added);
                unlike.remove(unlike.size()-1);
            }
            if(with > best.count){
                unlike.add(addWildCards(guess,added+1));
                // possibilities without more letters added
                long limit = getFullCount(cur, unlike);
                // possibilities with more letters added
                long expand = with - limit ;
                Log.d("Hangman", state + " limit " + limit);
                Log.d("Hangman", state + " expand " + expand);
                unlike.remove(unlike.size()-1);
                if(expand > limit)
                    //explore possibilities when another letter is added
                    bestAdd(cur,expand,guess,index+1,best,unlike,added+1);
                else
                    //stop searching and skip to comparing case
                    bestAdd(cur,limit,guess,length,best,unlike,added+1);
            }
        }
        return best;
    }

    public String addWildCards(char letter, int times){
        return "%" + repeat(String.valueOf(letter) + "%",times);
    }
    public String repeat(String txt, int times){
        String temp = "" ;
        for(int i = 0; i<times;i++)
            temp += txt;
        return temp ;
    }

    /**
     * Player guesses letter, Evil algorithm picks the state with most possibilities, to avoid the
     * player from guessing the word.
     * @param letter
     * @return
     */
    @Override
    public synchronized boolean guessLetter(char letter) {

        Pair best = best(letter);
        if(!this.state.equals(best.state)){
            this.state = best.state;
            impossibles.add(addWildCards(letter, best.added+1));
            return true ;
        } else
            impossibles.add("%" + String.valueOf(letter) + "%");
        return false;
    }

    @Override
    public boolean solved() {
        return state.indexOf("_") < 0 ;
    }

    /**
     * Get count of words for a state, without correction meaning (_a__ includes _a_a_)
     */
    public long getFullCount(String state, ArrayList<String> unlike){
        long count = db.count(state, unlike, impossibles);
        return count ;
    }


}
