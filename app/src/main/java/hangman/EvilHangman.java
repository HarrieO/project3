package hangman;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import nl.mprog.apps.Hangman10196129.database.WordDatabase.WordCursor;

import nl.mprog.apps.Hangman10196129.database.WordDatabase;

/**
 * Created by hroosterhuis on 24/02/14.
 */
public class EvilHangman extends Hangman {

    private String state ;
    private int length;
    private ArrayList<String> impossibles ;

    private ArrayList<String> possibles ;

    private HashMap<Character, Boolean> guessed ;

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
        fill(db);
    }

    private void init(WordDatabase db){
        String startState = repeat("_",length);
        init(db, startState);
        fill(db);
    }

    private void init(WordDatabase db, String startState){
        this.state = startState;
        this.guessed = new HashMap<Character, Boolean>(26);

        // Creates like syntax for the sql query to ignore already guessed letters
        impossibles = new ArrayList<String>();
        HashMap<Character,Integer> map = new HashMap<Character, Integer>(26);
        for(int i = 0; i < state.length() ; i++){
            char c = state.charAt(i);
            if(!map.containsKey(c)){
                map.put(c,1);
            } else {
                map.put(c,map.get(c)+1);
            }
        }
        for(Entry<Character,Integer> e : map.entrySet()){
            if(e.getKey() != '_')
                impossibles.add(addWildCards(e.getKey(),e.getValue()+1));
        }

    }

    /**
     * Fills possible with possibilities.
     * @param db
     */
    private void fill(WordDatabase db){
        WordCursor c = db.getWordCursor(state,impossibles);
        possibles = new ArrayList<String>(c.count());
        c.first();
        possibles.add(c.getWord().toLowerCase());
        while(c.hasNext()){
            possibles.add(c.getWord().toLowerCase());
            c.next();
        }
    }

    @Override
    public String state() {
        return state;
    }

    private boolean isGuessed(char c){
        return guessed.containsKey(c);
    }

    /**
     * Returns hangman pattern of given word.
     * @param word
     * @return
     */
    private String getPattern(String word){
        String pattern = "";
        for(int i = 0;i<length;i++){
            char c = word.charAt(i);
            if(isGuessed(c))
                pattern += c ;
            else
                pattern += '_';
        }
        return pattern;
    }

    /**
     * Returns HashMap with pattern as Key and Set of matching words as value
     * @return
     */
    private HashMap<String,ArrayList<String>> splitWords(){
        HashMap<String,ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        for(String word : possibles){
            String pattern = getPattern(word);
            if(map.containsKey(pattern)){
                map.get(pattern).add(word);
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                map.put(pattern, list);
            }
        }
        return map ;
    }

    /**
     * Sets state to most evil pattern.
     */
    private void pickBestPattern(){
        String old = this.state ;
        int best = 0 ;
        HashMap<String,ArrayList<String>> split = splitWords();
        for(Entry<String, ArrayList<String>> e : split.entrySet()){
            // Swap if greater size, if equal take the one that makes the guess wrong (more evil :D).
            if(e.getValue().size() > best ||
                    (e.getValue().size() == best && old.equals(e.getKey()))){
                this.state = e.getKey();
                this.possibles = e.getValue();
                best = this.possibles.size();
            }
        }
    }
    /**
     * Returns game state, if out of guesses random possibility is chosen.
     * @return State of the game, can be an unfinished game state.
     */
    @Override
    public String getSecretWord() {
        if(!guessesLeft())
            return possibles.get((int) Math.floor(Math.random()*possibles.size()));
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
        ArrayList<Character> guessList = new ArrayList<Character>(26);
        for(int i = 0; i < multipleGuess.length();i++)
            if(!guessList.contains(multipleGuess.charAt(i))){
                guessList.add(multipleGuess.charAt(i));
                guessed.put(multipleGuess.charAt(i), true);
            }
        for(char c : guessList)
            if(state.indexOf(c) < 0 && c != '_')
                impossibles.add(addWildCards(c,1));
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
        guessed.put(letter,true);
        String old = this.state;
        pickBestPattern();
        return !old.equals(this.state);
    }

    @Override
    public boolean solved() {
        return state.indexOf("_") < 0 ;
    }


}
