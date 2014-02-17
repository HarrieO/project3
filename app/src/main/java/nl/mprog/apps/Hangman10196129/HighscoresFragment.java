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
public class HighscoresFragment extends Fragment {

    private int id ;
    public HighscoresFragment() {
        this.id = R.layout.fragment_highscores;
    }
    public HighscoresFragment(int id){
        this.id = id ;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(id, container, false);

        return rootView;
    }
}
