package com.example.duckhunt.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duckhunt.R;
import com.example.duckhunt.common.Constantes;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView tvCounterDucks, tvTimer, tvNick;
    ImageView ivDuck;
    int counter = 0;
    int screenHeight;
    int screenWidth;
    Random random;
    boolean gameOver = false;
    String id, nick;
    FirebaseFirestore db;
    int timeGame = 5000;
    int frecuencyTimeGame = 1000;
    Button btnReinicar;
    CountDownTimer timerGameover;
    CountDownTimer timerGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        initViewComponents();
        events();
        initScreen();
        moveDuck();
        initCuentaAtras();
    }

    private void initCuentaAtras() {
        timerGame.start();
        timerGameover.start();
    }

    private void saveResultFirestore() {
        db.collection("users").document(id).update("ducks", counter);
    }

    private void mostrarDialogGameOver() {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setCancelable(false);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Has conseguido cazar " + counter + " patos")
                .setTitle("Games Over");

        // Add the buttons
        builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                resetGame();
            }
        });
        builder.setNegativeButton("Ver Ranking", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Intent i = new Intent(GameActivity.this, RankingActivity.class);
                startActivity(i);
            }
        });

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void resetGame() {
        btnReinicar.setVisibility(View.GONE);
        ivDuck.setVisibility(View.VISIBLE);
        counter = 0;
        tvCounterDucks.setText("0");
        gameOver = false;
        initCuentaAtras();
        moveDuck();
    }

    private void initScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        random = new Random();

    }

    private void initViewComponents() {
        tvCounterDucks = findViewById(R.id.textViewCounter);
        tvTimer = findViewById(R.id.textViewTimer);
        tvNick = findViewById(R.id.textViewNIck);
        ivDuck = findViewById(R.id.imageViewDuck);
        btnReinicar = findViewById(R.id.buttonReset);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        tvCounterDucks.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);

        Bundle extras = getIntent().getExtras();
        nick = extras.getString(Constantes.EXTRA_NICK);
        id = extras.getString(Constantes.EXTRA_ID);
        tvNick.setText(nick);

        btnReinicar.setVisibility(View.GONE);

        timerGameover = new CountDownTimer(timeGame, frecuencyTimeGame) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText((millisUntilFinished / 1000)+"s");
            }

            public void onFinish() {
                timerGame.cancel();
                timerGameover.cancel();
                tvTimer.setText("0s");
                gameOver = true;
                btnReinicar.setVisibility(View.VISIBLE);
                ivDuck.setVisibility(View.GONE);
                saveResultFirestore();
                mostrarDialogGameOver();
            }
        };

        timerGame = new CountDownTimer(timeGame, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                moveDuck();
            }

            @Override
            public void onFinish() {

            }
        };

        btnReinicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private void events() {
        ivDuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerGame.cancel();
                if(!gameOver) {
                    counter++;
                    tvCounterDucks.setText(String.valueOf(counter));
                    ivDuck.setImageResource(R.drawable.duck_clicked);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ivDuck.setImageResource(R.drawable.duck);
                            timerGame.start();
                        }
                    }, 500);
                }
            }
        });
    }

    private void moveDuck() {
        int min = 0;
        int maxX = screenWidth - ivDuck.getWidth();
        int maxY = screenHeight - ivDuck.getHeight();

        int positionNewX = random.nextInt(((maxX-min)+1));
        int positionNewY = random.nextInt(((maxY-min)+1));

        ivDuck.setX(positionNewX);
        ivDuck.setY(positionNewY);
    }
}
