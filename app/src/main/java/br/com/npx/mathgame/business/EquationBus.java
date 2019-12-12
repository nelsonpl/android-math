package br.com.npx.mathgame.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import br.com.npx.mathgame.common.RandomCustom;
import br.com.npx.mathgame.entities.Equation;

public class EquationBus {

    private int start = 1;
    private int count = 10;
    private List<Equation> equations = new ArrayList<>();
    private Integer[] options;
    private Integer level = 1;

    public Integer[] getOptions() {
        return options;

    }

    public Integer[] getResults() {

        ArrayList<Integer> results = new ArrayList<>();

        Random random = new Random();
        boolean flag;

        for (int j = 0; j < 9; j++) {
            do {
                Equation equation = new Equation();
                List<Integer> numbers = new ArrayList<>();
                Integer result = 0;
                ArrayList<Integer> optionsAux = new ArrayList<>(Arrays.asList(options));
                for (int i = 0; i < 2; i++) {
                    Integer index = random.nextInt(optionsAux.size());
                    Integer option = optionsAux.get(index);
                    numbers.add(option);
                    result += option;
                    optionsAux.remove(option);
                }
                if (results.contains(result) || optionsAux.contains(result)) {
                    flag = true;
                } else {
                    equation.setResult(result);
                    equation.setNumbers(numbers);
                    equations.add(equation);
                    results.add(result);
                    flag = false;
                }
            }
            while (flag);
        }

        return results.toArray(new Integer[0]);
    }

    public Integer[] getHint() {
        Equation e = equations.get(0);
        return e.getNumbers().toArray(new Integer[0]);
    }

    public void removeHint(Integer sum) {
        for (int i = 0; i < equations.size(); i++) {
            Equation e = equations.get(i);
            if (e.getResult() == sum) {
                equations.remove(i);
                break;
            }
        }
    }

    public void newGame() {
        RandomCustom randomCustom = new RandomCustom(start, count);
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) list.add(randomCustom.get());
        options = list.toArray(new Integer[0]);
        level++;
        if ((level % 2) == 0)
        {
           count+=5;
        }
        else
        {
           start-=5;
        }
    }
}
