package nl.mprog.apps.Hangman10196129.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import hangman.Hangman;
import hangman.HangmanCanvas;
import nl.mprog.apps.Hangman10196129.MainActivity;
import nl.mprog.apps.Hangman10196129.R;
import nl.mprog.apps.Hangman10196129.database.HighscoreDatabase;

/**
 * Created by hroosterhuis on 11/02/14.
 */
public class GameFragment extends Fragment {

    private int id ;
    public Hangman game ;
    public HangmanCanvas canvas ;

    public GameFragment() {
        this.id = R.layout.fragment_main_game;
    }
    public GameFragment(int id){
        this.id = id ;
    }

    @Override
    public void onStart(){
        super.onStart();
        loadGame(false);
        updateScreen();
        createKeyBoard();
        FrameLayout frame = (FrameLayout)getView().findViewById(R.id.canvasFrame);
        if(frame != null){
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frame.addView(canvas,params);
        }
    }

    public void onPause(){
        super.onPause();
        if(game.solved() || !game.guessesLeft())
            loadGame(true);
        game.save(getActivity().getPreferences(Context.MODE_PRIVATE));
        FrameLayout frame = (FrameLayout)getView().findViewById(R.id.canvasFrame);
        frame.removeView(canvas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(id, container, false);
        ((MainActivity) getActivity()).setActiveMenu(true);
        return rootView;
    }

    public void loadGame(){
        loadGame(false);
    }

    public void loadGame(boolean forceNew){
         game = ((MainActivity) getActivity()).loadGame();

        if(game == null || forceNew || game.solved() || !game.guessesLeft()){
            game = ((MainActivity) getActivity()).newGame();
            resetKeyBoard();
        }

        if(canvas == null)
            canvas = new HangmanCanvas(getActivity(), game) ;
        else
            canvas.setGame(game);
        updateScreen();
    }

    public void updateScreen(){

        updateTextView(R.id.secretWord, game.display());
        updateTextView(R.id.leftMessage, "You have " + game.guesses() + " guesses left.");
        canvas.invalidate();
    }


    public void updateTextView(int id, String text) {
        TextView view = (TextView) getView().findViewById(id);
        view.setText(text);
    }

    public void guess(char letter){
        game.guess(letter);
        if( game.solved() || !game.guessesLeft()){
            AfterGameDialogFragment dialog = new AfterGameDialogFragment();
            if(game.solved()){
                HighscoreDatabase db = new HighscoreDatabase(getActivity());
                db.put(game.getSecretWord(),game.incorrectGuesses(),game.score());
            }
            dialog.show(getActivity().getSupportFragmentManager(),"Finished game");
        }
        updateScreen();
    }

    public void createKeyBoard(){
        TableLayout table = (TableLayout) getView().findViewById(R.id.keyboard);
        if(table == null || table.getChildCount() > 0)
            return ;
        TableRow row = new TableRow(getActivity());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        String letters = getString(R.string.keyboard_letters);
        Button.OnClickListener KeyboardListener = new KeyboardListener();
        for(int i = 0; i < letters.length(); i++){
            char letter = letters.charAt(i);
            if(letter != '/'){
                Button button = new Button(getActivity());
                button.setText(String.valueOf(letter));
                button.setOnClickListener(KeyboardListener);
                if(game.hasGuessed(letter))
                    button.setEnabled(false);
                row.addView(button, buttonParams);
            } else {
                table.addView(row,params);
                row = new TableRow(getActivity());
            }
        }
        table.addView(row,params);

    }
    public void resetKeyBoard(){
        TableLayout table = (TableLayout) getView().findViewById(R.id.keyboard);
        for(int i = 0 ; i < table.getChildCount() ; i++ ){
            TableRow row = (TableRow) table.getChildAt(i);
            for(int j = 0 ; j < row.getChildCount() ; j++){
                Button button = (Button) row.getChildAt(j);
                button.setEnabled(true);
            }
        }
    }


    public class KeyboardListener implements View.OnClickListener {


        @Override
        public void onClick(View view) {
            if(game.guessesLeft()){
                Button button = (Button) view ;
                if(!game.solved() && game.guessesLeft()){
                    button.setEnabled(false);
                    guess(button.getText().charAt(0));
                }
            }
        }
    }

    public class AfterGameDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if(game.solved()){
                builder.setMessage("You scored " + game.score() + "!\n With " + game.incorrectGuesses() + " incorrect guesses.");
            } else {
                builder.setMessage("You failed to guess the word!\nThe correct word was: " + game.getSecretWord());
            }

            builder.setPositiveButton("New Puzzle", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    loadGame(true);
                    updateScreen();
                }
            }).setNegativeButton("Highscores", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    loadGame(true);
                    ((MainActivity) getActivity()).transition(new HighscoresFragment());
                }
            });
            builder.setCancelable(false);
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
