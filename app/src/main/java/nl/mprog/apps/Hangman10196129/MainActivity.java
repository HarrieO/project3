package nl.mprog.apps.Hangman10196129;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import hangman.Hangman;
import nl.mprog.apps.Hangman10196129.database.WordDatabase;
import nl.mprog.apps.Hangman10196129.fragments.GameFragment;
import nl.mprog.apps.Hangman10196129.fragments.HighscoresFragment;
import nl.mprog.apps.Hangman10196129.fragments.MenuFragment;
import nl.mprog.apps.Hangman10196129.fragments.SettingsFragment;

public class MainActivity extends ActionBarActivity {

    private boolean menuActive ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuActive = false ;
        setContentView(R.layout.activity_main_game);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MenuFragment())
                    .commit();
        }
    }



    protected void onStop(){
        super.onStop();

    }

    public boolean transition(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true ;
    }

    public void setActiveMenu(boolean active){
        this.menuActive = active ;
        super.supportInvalidateOptionsMenu();
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        menu.getItem(1).setVisible(menuActive);
        menu.getItem(2).setVisible(menuActive);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main_game, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        menuActive = false ;

        Fragment fragment = null;
        if (id == R.id.action_settings)
            fragment = new SettingsFragment();
        else if (id == R.id.action_highscore)
            fragment = new HighscoresFragment();
        else if (id == R.id.action_new_game){
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentById(R.id.container);
            if(!frag.getClass().equals(GameFragment.class)){
                newGame();
                fragment = new GameFragment();
            } else {
                ((GameFragment)frag).loadGame(true);
            }
        }

        if (fragment != null) {
            return transition(fragment);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

    public int getInitialGuesses(){
        return getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.no_guesses_key), 8) ;
    }

    public int getWordLength(){
        return getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.word_length_key), 7) ;
    }

    public Hangman newGame(){
        WordDatabase db = new WordDatabase(this);
        Hangman game = new Hangman(db.get(getWordLength()), getInitialGuesses());
        game.save(getPreferences(Context.MODE_PRIVATE));
        return game ;
    }
    public boolean gameStarted(){
        return getPreferences(Context.MODE_PRIVATE).getString(Hangman.GUESSED,"").length() > 0;
    }
}
