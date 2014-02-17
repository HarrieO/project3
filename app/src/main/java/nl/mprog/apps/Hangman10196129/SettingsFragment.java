package nl.mprog.apps.Hangman10196129;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(id, container, false);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        SeekBar barLength = (SeekBar) getView().findViewById(R.id.word_length_bar);

        barLength.setOnSeekBarChangeListener(
                new changeListener(barLength, 15, R.id.word_length, R.string.word_length,
                                   R.string.word_length_key));

        SeekBar barGuess = (SeekBar) getView().findViewById(R.id.no_guesses_bar);
        barGuess.setOnSeekBarChangeListener(
                new changeListener(barGuess, 26, R.id.no_guesses, R.string.no_guesses,
                                   R.string.no_guesses_key));
    }

    public void updateTextView(int id, String text) {
        TextView view = (TextView) getView().findViewById(id);
        view.setText(text);
    }

    public class changeListener implements SeekBar.OnSeekBarChangeListener {

        int max, textViewId, stringId, prefKey ;

        changeListener(SeekBar bar, int max, int textViewId, int stringId, int prefKey){
            this.max        = max        ;
            this.textViewId = textViewId ;
            this.stringId   = stringId   ;
            this.prefKey    = prefKey    ;
            updateTextView(textViewId,  getString(stringId)  + " " + loadSetting() );
            bar.setProgress((int) Math.round(loadSetting()/(max-1.0)*100)+1);
        }

        public void saveSetting(int progress){
            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(getString(prefKey),
                    value(progress)).commit();
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
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
