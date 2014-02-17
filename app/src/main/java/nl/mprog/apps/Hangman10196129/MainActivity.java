package nl.mprog.apps.Hangman10196129;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
        return true ;
    }

    public void setActiveMenu(boolean active){

        this.menuActive = active ;
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        return menuActive ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        menuActive = false ;

        Fragment fragment = null;
        if (id == R.id.action_settings)
            fragment = new SettingsFragment();
        else if (id == R.id.action_highscore)
            fragment = new HighscoresFragment();

        if (fragment != null) {
            return transition(fragment);
        }
        return super.onOptionsItemSelected(item);
    }

}
