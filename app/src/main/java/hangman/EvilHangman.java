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

    public EvilHangman(WordDatabase db, int length, String guessed, int guesses, int startGuesses){
        super(guessed, guesses, startGuesses);
        this.length = length;
        init(db);
    }

    public EvilHangman(WordDatabase db, int length, String guessed, int guesses){
        super(guessed, guesses);
        this.length = length;
        init(db);
    }

    public EvilHangman(WordDatabase db, int length, int guesses){
        super(guesses);
        this.length = length;
        init(db);
    }

    public EvilHangman(WordDatabase db, String state, String guessed, int guesses, int startGuesses){
        super(guessed, guesses, startGuesses);
        this.length = state.length();
        init(db);
    }

    public EvilHangman(WordDatabase db, String state, String guessed, int guesses){
        super(guessed, guesses);
        this.length = state.length();
        init(db);
    }

    public EvilHangman(WordDatabase db, String state, int guesses){
        super(guesses);
        this.length = state.length();
        init(db);
    }

    private void init(WordDatabase db){
        this.db = db ;
        state = "" ;
        for(int i = 0 ; i < length ;i++){
            state += "_" ;
        }
        impossibles = new ArrayList<String>();
    }

    @Override
    public String display() {
        return state;
    }

    @Override
    public String getSecretWord() {
        return state;
    }

    @Override
    public boolean evil() {
        return true;
    }

    @Override
    public void updateDisplay(String multipleGuess) {

    }

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

    public Pair best(char guess){
        ArrayList<String> next = new ArrayList<String>();
        long both = getFullCount(state,next);
        next.add("%" + String.valueOf(guess) + "%");
        long without = getFullCount(state,next);
        long with    = both - without;
        Log.d("Hangman", state + " total " + both);
        Log.d("Hangman", state + " with " + with);
        Log.d("Hangman", state + " without " + without);
        if(with <= without)
            return new Pair(state,without,0) ;
        else
            return bestAdd(state,guess,0, new Pair(state,without,0));
    }

    public Pair bestAdd(String state, char guess, int start, Pair best){
        return  bestAdd(state, guess, start, best, new ArrayList<String>(), 0);
    }
    public Pair bestAdd(String state, char guess, int start, Pair best, ArrayList<String> unlike, int added){
        if(start >= length){
            Long count = getFullCount(state,unlike);
            if(count > best.count){
                Log.d("Hangman", "Newbest " + state + ": " + count);
                best.set(state, count, added);
            }
        } else if(state.charAt(start) != '_')
            return  bestAdd(state, guess, start+1, best, unlike, added);
        else {
            long both = getFullCount(state,unlike);
            String cur = state.substring(0,start) + String.valueOf(guess) + state.substring(start+1);
            long with = getFullCount(cur,unlike);
            long without = both - with;
            Log.d("Hangman", state + " total " + both);
            Log.d("Hangman", cur + " with " + with);
            Log.d("Hangman", state + " without " + without);
            if(without > best.count){
                unlike.add(cur);
                bestAdd(state,guess,start+1,best,unlike,added);
                unlike.remove(unlike.size()-1);
            }
            if(with > best.count){
                unlike.add(addWildCards(guess,added+1));
                long limit = getFullCount(cur, unlike);
                long expand = with - limit ;
                Log.d("Hangman", cur + " limit " + limit);
                Log.d("Hangman", cur + " expand " + expand);
                unlike.remove(unlike.size()-1);
                if(expand > limit){
                    bestAdd(cur,guess,start+1,best,unlike,added+1);
                } else {
                    bestAdd(cur,guess,length,best,unlike,added+1);
                }
            }
        }
        return best;
    }

    public void addConfigs(ArrayList<String> configs, String current, int start, char letter){
        if(start < length){
            String with = current.substring(0,start) + String.valueOf(letter) +
                    current.substring(start+1);
            configs.add(with);
            addConfigs(configs, current, start+1, letter);
            addConfigs(configs, with, start+1, letter);
        }
    }
    public String addWildCards(char letter, int times){
        return "%" + repeat(String.valueOf(letter) + "%",times);
    }
    public String repeat(String txt, int times){
        String temp = "" ;
        for(int i = 0; i<times;i++){
            temp += txt;
        }
        return temp ;
    }

    @Override
    public synchronized boolean guessLetter(char letter) {

        Pair best = best(letter);
        if(!this.state.equals(best.state)){
            this.state = best.state;
            String imp = "%" + String.valueOf(letter) + "%";
            for(int i = 0; i<best.added;i++){
                imp += String.valueOf(letter) + "%";
            }
            impossibles.add(imp);
            Log.d("Hangman", "Impossible " + imp);
            return true ;
        } else {
            impossibles.add("%" + String.valueOf(letter) + "%");
        }
        return false;
    }

    @Override
    public boolean solved() {
        return state.indexOf("_") < 0 ;
    }

    public long getFullCount(String state, ArrayList<String> unlike){
        long count = db.count(state, unlike, impossibles);
        return count ;
    }


}
