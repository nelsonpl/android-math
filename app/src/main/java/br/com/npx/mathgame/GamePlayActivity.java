package br.com.npx.mathgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.com.npx.mathgame.adapters.NumberAdapter;
import br.com.npx.mathgame.business.EquationBus;
import br.com.npx.mathgame.business.MathGameBus;

public class GamePlayActivity extends Activity implements NumberAdapter.ItemClickListener {

    NumberAdapter resultAdapter;
    NumberAdapter optionAdapter;
    ProgressBar progressBar;
    CountDownTimer mCountDownTimer;
    Boolean isProgressBarReset = false;
    Boolean isAlternative = true;
    int score = 0;
    TextView tvScore;
    int[] progressCount = {0};
    MathGameBus bus;
    TextView tvNumberA;
    TextView tvNumberB;
    TextView tvNumberC;
    TextView tvNumberD;
    EquationBus equationBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        tvScore = findViewById(R.id.tvScore);
        tvNumberA = findViewById(R.id.tvNumberA);
        tvNumberB = findViewById(R.id.tvNumberB);
        tvNumberC = findViewById(R.id.tvNumberC);
        tvNumberD = findViewById(R.id.tvNumberD);

        equationBus = new EquationBus();
        bus = new MathGameBus(this);

        equationBus.newGame();
        createRvOptions(true);
        createRvResults(true);
        createProgressBar();

        findViewById(R.id.btNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownTimer.cancel();
                //equationBus.newGame();
                createRvOptions(false);
                createRvResults(false);
                mCountDownTimer.start();
                progressCount[0] = 0;
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Button bt = (Button) view;
        Integer optionNumber = optionAdapter.getNumber(position);
        boolean isNew = true;

        if (optionAdapter.getStatus(position)) {

            if (!tvNumberA.getText().equals("") && !tvNumberB.getText().equals("") && !tvNumberC.getText().equals("") && !tvNumberD.getText().equals("")) {
                return;
            }

            bt.setBackgroundColor(getResources().getColor(R.color.option_inactive));
            optionAdapter.setStatus(position, false);
            optionAdapter.setAlternative(position, false);
        } else {
            isNew = false;
            bt.setBackgroundColor(getResources().getColor(R.color.option_active));
            optionAdapter.setStatus(position, true);
        }

        Integer sum = 0;
        Integer optionsCount = 0;
        for (int i = 0; i < optionAdapter.getItemCount(); i++) {

            if (optionAdapter.getStatus(i)) {
                continue;
            }

            sum += optionAdapter.getNumber(i);
            optionsCount++;
        }

        Integer indexResult = resultAdapter.findResult(sum);

        if (indexResult >= 0) {
            findResultSuccess(indexResult);

            equationBus.removeHint(sum);

            if (resultAdapter.allStatus()) {
                mCountDownTimer.cancel();
                equationBus.newGame();
                createRvOptions(false);
                createRvResults(false);
                mCountDownTimer.start();
                progressCount[0] = 0;
            }

            calcScore(optionsCount, sum);
            maxScore();

        } else {
            writeNumber(optionNumber, isNew);
        }

    }

    private void writeNumber(Integer optionNumber, boolean isNew) {
        if (isNew) {
            if (tvNumberA.getText().equals("")) {
                tvNumberA.setText(String.valueOf(optionNumber));
                return;
            }
            if (tvNumberB.getText().equals("")) {
                tvNumberB.setText(String.valueOf(optionNumber));
                return;
            }
            if (tvNumberC.getText().equals("")) {
                tvNumberC.setText(String.valueOf(optionNumber));
                return;
            }
            tvNumberD.setText(String.valueOf(optionNumber));
        } else {
            if (tvNumberA.getText().equals(String.valueOf(optionNumber))) {
                tvNumberA.setText((""));
                return;
            }
            if (tvNumberB.getText().equals(String.valueOf(optionNumber))) {
                tvNumberB.setText((""));
                return;
            }
            if (tvNumberC.getText().equals(String.valueOf(optionNumber))) {
                tvNumberC.setText((""));
                return;
            }
            tvNumberD.setText((""));
        }
    }

    private void maxScore() {
        Integer maxScore = bus.getMaxScore();

        if (score > maxScore) {
            bus.saveMaxScore(score);
        }
    }

    private void calcScore(Integer optionsCount, Integer sum) {

        if (!isAlternative) {
            return;
        }

        if (optionsCount - 2 > 2)
            sum += (optionsCount - 2) * 10;

        Integer progress = progressCount[0] * 100 / (30000 / 100);

        if (progress <= 10) {
            sum += 10;
        }
        if (progress <= 20) {
            sum += 10;
        }
        if (progress <= 30) {
            sum += 10;
        }

        score += sum;
        tvScore.setText(String.valueOf(score));
    }

    private void createProgressBar() {
        progressBar = findViewById(R.id.progressBar);

        progressBar.setProgress(progressCount[0]);

        mCountDownTimer = new CountDownTimer(30000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isProgressBarReset) {
                    progressCount[0] = 0;
                    isProgressBarReset = false;
                } else {
                    progressCount[0]++;
                }

                Integer progress = progressCount[0] * 100 / (30000 / 100);
                progressBar.setProgress(progress);

                if (progress >= 85) {
                    if (isAlternative) {
                        Integer[] number = equationBus.getHint();
                        for (int aNumber : number) {
                            int index = optionAdapter.setAlternative(aNumber);
                            optionAdapter.notifyItemChanged(index);
                        }
                        isAlternative = false;
                    }
                } else {
                    isAlternative = true;
                }
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                Intent i = new Intent(GamePlayActivity.this, HomeActivity.class);
                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        };
        mCountDownTimer.start();
    }

    private void createRvResults(boolean isNew) {
        RecyclerView recyclerView = findViewById(R.id.rvResults);
        if (isNew) {
            int numberOfColumns = 3;
            GridLayoutManager grid = new GridLayoutManager(this, numberOfColumns);
            recyclerView.setLayoutManager(grid);
            resultAdapter = new NumberAdapter(this, equationBus.getResults(), true);
            recyclerView.setAdapter(resultAdapter);
        } else {
            resultAdapter = (NumberAdapter) recyclerView.getAdapter();
            resultAdapter.setData(equationBus.getResults());
            resultAdapter.notifyDataSetChanged();
        }
    }

    private void createRvOptions(boolean isNew) {

        Integer[] data = equationBus.getOptions();

        RecyclerView recyclerView = findViewById(R.id.rvOption);
        if (isNew) {
            int numberOfColumns = 3;
            GridLayoutManager grid = new GridLayoutManager(this, numberOfColumns);
            recyclerView.setLayoutManager(grid);
            optionAdapter = new NumberAdapter(this, data, false);
            recyclerView.setAdapter(optionAdapter);
        } else {
            optionAdapter.setData(data);
            optionAdapter.notifyDataSetChanged();

        }

        optionAdapter.setClickListener(this);
    }

    private void findResultSuccess(Integer indexResult) {

        for (int i = 0; i < optionAdapter.getItemCount(); i++) {
            optionAdapter.activeItem(i);
            optionAdapter.notifyItemChanged(i);
        }
        resultAdapter.setStatus(indexResult, false);
        resultAdapter.notifyItemChanged(indexResult);

        tvNumberA.setText("");
        tvNumberB.setText("");
        tvNumberC.setText("");
        tvNumberD.setText("");

        mCountDownTimer.cancel();
        mCountDownTimer.start();
        isProgressBarReset = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        mCountDownTimer.cancel();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        mCountDownTimer.start();
    }

}
