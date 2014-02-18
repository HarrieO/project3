package nl.mprog.apps.Hangman10196129.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import nl.mprog.apps.Hangman10196129.database.HighscoreDatabase.HighscoreCursor ;

import nl.mprog.apps.Hangman10196129.R;
import nl.mprog.apps.Hangman10196129.database.HighscoreDatabase;

/**
 * Created by hroosterhuis on 11/02/14.
 */
public class HighscoresFragment extends Fragment {

    private final static LinearLayout.LayoutParams WORDPARAMS = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    {WORDPARAMS.gravity = Gravity.CENTER_HORIZONTAL;}

    private final static ViewGroup.LayoutParams ROWPARAMS = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

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



    public void onStart(){
        super.onStart();
        HighscoreDatabase db = new HighscoreDatabase(getActivity());
        HighscoreCursor cursor = db.get();
        if(cursor.count() == 0){
            TextView txt = new TextView(getActivity());
            txt.setText("There are no highscores.");
            txt.setGravity(Gravity.CENTER);
            ((LinearLayout) getView().findViewById(R.id.highscores_list)).addView(txt, ROWPARAMS);
        } else {
            addRow(cursor);
            while(cursor.hasNext())
            {
                cursor.next();
                addRow(cursor);
            }
        }
    }




    public void addRow(HighscoreCursor cursor){

        TextView word = new TextView(getActivity());
        word.setText(cursor.getWord());
        ((LinearLayout) getView().findViewById(R.id.highscore_word_list)).addView(word, WORDPARAMS);
        

        TextView incorrect = new TextView(getActivity());
        incorrect.setText(String.valueOf(cursor.getIncorrect()));
        ((LinearLayout) getView().findViewById(R.id.highscore_incorrect_list)).addView(incorrect, WORDPARAMS);

        TextView score = new TextView(getActivity());
        score.setText(String.valueOf(cursor.getScore()));
        ((LinearLayout) getView().findViewById(R.id.highscore_score_list)).addView(score, WORDPARAMS);

    }
}
