package br.com.npx.mathgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.com.npx.mathgame.business.MathGameBus;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        findViewById(R.id.btPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, GamePlayActivity.class);
                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

        TextView tvScore = findViewById(R.id.tvScore);

        MathGameBus bus = new MathGameBus(this);
        int maxScore = bus.getMaxScore();
        tvScore.setText(String.valueOf(maxScore));

    }

}
