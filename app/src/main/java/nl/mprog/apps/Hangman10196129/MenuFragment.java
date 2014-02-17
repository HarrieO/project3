package nl.mprog.apps.Hangman10196129;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import hangman.Hangman;

/**
 * Created by hroosterhuis on 11/02/14.
 */
public class MenuFragment extends Fragment {

    private int id ;
    public MenuFragment() {
        this.id = R.layout.fragment_menu;
    }
    public MenuFragment(int id){
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
        ((MainActivity) getActivity()).setActiveMenu(false);
        Button.OnClickListener listener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                Fragment fragment = null ;
                switch (id){
                    case R.id.play_button:
                        fragment = new GameFragment();
                        break;
                    case R.id.settings_button:
                        fragment = new SettingsFragment();
                        break;
                    case R.id.high_scores_button:
                        fragment = new HighscoresFragment();
                }
                ((MainActivity) getActivity()).transition(fragment);
            }
        };

        ((Button) getView().findViewById(R.id.play_button)       ).setOnClickListener(listener);
        ((Button) getView().findViewById(R.id.settings_button)   ).setOnClickListener(listener);
        ((Button) getView().findViewById(R.id.high_scores_button)).setOnClickListener(listener);

        if( getActivity().getPreferences(Context.MODE_PRIVATE).getString(Hangman.GUESSED,"").length()
             > 0 ){
            ((Button) getView().findViewById(R.id.play_button)).setText("Continue");
        }
    }



}
