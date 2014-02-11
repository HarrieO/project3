package hangman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by hroosterhuis on 23/01/14.
 */
public class HangmanCanvas extends View {
    Paint paint = new Paint();

    private Hangman game ;

    public HangmanCanvas(Context context, Hangman game) {
        super(context);
        paint.setColor(Color.BLACK);
        this.game = game ;
    }

    public void setGame(Hangman game){
        this.game = game ;
        super.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        int height = canvas.getHeight();
        int width  = canvas.getWidth() ;
        paint.setColor(Color.BLACK);

        int step = (int) Math.round(game.incorrectGuesses() / (double) game.startGuesses() * 12);
        if(step == 12 && game.guessesLeft())
            step = 11 ;

        if(step < 5)
            paint.setStrokeWidth(12);
        else
            paint.setStrokeWidth(6);

        switch (step){
            case 12:
                canvas.drawLine(width/3*2-15,height/5*4-20,width/3*2-70,height/5*4+50,paint);
            case 11:
                canvas.drawLine(width/3*2-5,height/5*4-20,width/3*2+50,height/5*4+50,paint);
            case 10:
                canvas.drawLine(width/3*2-20,height/3+50,width/3*2-90,height/3+70,paint);
            case 9:
                canvas.drawLine(width/3*2,height/3+50,width/3*2+70,height/3+70,paint);
            case 8:
                canvas.drawRect(width/3*2-20,height/3,width/3*2,height/5*4-10,paint);
            case 7:
                canvas.drawCircle(width/3*2-10,height/3,50,paint);
            case 6:
                paint.setStrokeWidth(5);
                canvas.drawLine(width / 3 * 2 - 10, 0, width / 3 * 2 - 10, height / 2 - 45, paint);
            case 5:
                paint.setStrokeWidth(12);
                canvas.drawRect(0,0,width/3*2,15,paint);
            case 4:
                canvas.drawLine(0, 85, 85, 0, paint);
            case 3:
                canvas.drawRect(0, 0, 15, height, paint);
            case 2:
                canvas.drawLine(0,height-85,85,height,paint);
            case 1:
                canvas.drawRect(0,height-15,width/3,height,paint);
        }
    }
}
