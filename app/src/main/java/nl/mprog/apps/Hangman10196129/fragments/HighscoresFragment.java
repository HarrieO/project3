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

    private final static TableRow.LayoutParams WORDPARAMS = new TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1.3f);
    {WORDPARAMS.setMargins(4,4,4,4);}
    private final static TableRow.LayoutParams SCOREPARAMS = new TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
    {SCOREPARAMS.setMargins(4,4,4,4);}

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
            while(cursor.hasNext()){
                cursor.next();
                addRow(cursor);
            }
        }
    }




    public void addRow(HighscoreCursor cursor){

        TableRow row = new TableRow(getActivity());



        TextView word = new TextView(getActivity());
        word.setText(cursor.getWord());
        word.setTextSize(18);
        word.setWidth(0);
        word.setPadding(30, 10, 30, 10);
        row.addView(word, WORDPARAMS);
        

        TextView incorrect = new TextView(getActivity());
        incorrect.setText(String.valueOf(cursor.getIncorrect()));
        incorrect.setTextSize(18);
        incorrect.setWidth(0);
        incorrect.setPadding(40,10,40,10);
        row.addView(incorrect, SCOREPARAMS);

        TextView score = new TextView(getActivity());
        score.setText(String.valueOf(cursor.getScore()));
        score.setTextSize(18);
        score.setWidth(0);
        score.setPadding(40, 10, 40, 10);
        row.addView(score, SCOREPARAMS);

        ((TableLayout) getView().findViewById(R.id.highscores_table)).addView(row);
    }
}
