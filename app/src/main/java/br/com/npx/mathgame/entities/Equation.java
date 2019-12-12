package br.com.npx.mathgame.entities;

import java.util.List;

public class Equation {

    private int result;
    private List<Integer> numbers;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }
}
