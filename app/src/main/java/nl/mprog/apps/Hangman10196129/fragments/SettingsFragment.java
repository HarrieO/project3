package nl.mprog.apps.Hangman10196129.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import nl.mprog.apps.Hangman10196129.MainActivity;
import nl.mprog.apps.Hangman10196129.R;
import nl.mprog.apps.Hangman10196129.database.WordDatabase;

/**
 * Created by hroosterhuis on 11/02/14.
 */
public class SettingsFragment extends Fragment {

    private int id ;
    public SettingsFragment() {
        this.id = R.layout.fragment_settings;
    }
    public SettingsFragment(int id){
        this.id = id ;

    }

    private boolean changed, evil ;
    private int length, guesses ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(id, container, false);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        changed = false;
        length  = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getInt(getString(R.string.word_length_key), 1);

        guesses = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getInt(getString(R.string.no_guesses_key), 1);

        evil    = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getBoolean(getString(R.string.evil_key), false);

        WordDatabase db = new WordDatabase(getActivity());

        SeekBar barLength = (SeekBar) getView().findViewById(R.id.word_length_bar);
        barLength.setOnSeekBarChangeListener(
                new ChangeListener(barLength, db.maxLength(), R.id.word_length, R.string.word_length,
                        R.string.word_length_key));

        SeekBar barGuess = (SeekBar) getView().findViewById(R.id.no_guesses_bar);
        barGuess.setOnSeekBarChangeListener(
                new ChangeListener(barGuess, 26, R.id.no_guesses, R.string.no_guesses,
                        R.string.no_guesses_key));

        Switch switchEvil = (Switch) getView().findViewById(R.id.evil);
        switchEvil.setOnCheckedChangeListener(
                new EvilListener(switchEvil, R.string.evil_key)
        );
    }

    public void updateTextView(int id, String text) {
        TextView view = (TextView) getView().findViewById(id);
        view.setText(text);
    }

    public void onPause(){
        super.onPause();
        MainActivity activity = (MainActivity) getActivity();
        if(changed){
            getActivity().getPreferences(Context.MODE_PRIVATE).edit()
                    .putInt(getString(R.string.word_length_key), length)
                    .putInt(getString(R.string.no_guesses_key), guesses)
                    .putBoolean(getString(R.string.evil_key), evil)
                    .commit();

            if(!activity.gameStarted())
               activity.newGame();
        }


    }

    public class ChangeListener implements SeekBar.OnSeekBarChangeListener {

        int max, textViewId, stringId, prefKey ;

        ChangeListener(SeekBar bar, int max, int textViewId, int stringId, int prefKey){
            this.max        = max        ;
            this.textViewId = textViewId ;
            this.stringId   = stringId   ;
            this.prefKey    = prefKey    ;
            updateTextView(textViewId,  getString(stringId)  + " " + loadSetting() );
            bar.setProgress((int) Math.round(loadSetting()/(max-1.0)*100)+1);
        }

        public void saveSetting(int progress){
            if(prefKey == R.string.word_length_key)
                length = value(progress);
            else if(prefKey == R.string.no_guesses_key)
                guesses = value(progress);
        }

        public int loadSetting(){
            return getActivity().getPreferences(Context.MODE_PRIVATE).getInt(getString(prefKey),1);
        }

        public int value(int progress){
            return (int) Math.round((progress/100.0)*(max-1)) + 1;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            updateTextView(textViewId,  getString(stringId)  + " " + value(progress) );
            saveSetting(progress);
            changed = true ;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
    public class EvilListener implements Switch.OnCheckedChangeListener {

        int prefKey ;

        EvilListener(Switch bar, int prefKey){
            this.prefKey = prefKey;
            bar.setChecked(loadSetting());
        }

        public boolean loadSetting(){
            return getActivity().getPreferences(Context.MODE_PRIVATE).getBoolean(getString(prefKey),false);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
            evil    = value;
            changed = true ;
        }
    }
}
