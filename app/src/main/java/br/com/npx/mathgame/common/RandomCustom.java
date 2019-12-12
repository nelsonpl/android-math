package br.com.npx.mathgame.common;

import java.util.ArrayList;
import java.util.Random;

public class RandomCustom {
    private Random random = new Random();
    private ArrayList<Integer> used = new ArrayList<>();
    private ArrayList<Integer> numbers = new ArrayList<>();

    public RandomCustom(Integer start, Integer count) {
        for (int i = start; i <= count; i++) {
            numbers.add(i);
        }
        numbers.remove(Integer.valueOf(0));
    }

    public Integer get() {
        Integer result;
        Boolean isEqual;

        do {
            isEqual = false;
            int index = random.nextInt(numbers.size());
            result = numbers.get(index);

            if (result == 0) {
                isEqual = true;
            } else {
                for (int i = 0; i < used.size(); i++) {
                    if (result.equals(used.get(i)) || result.equals(used.get(i) * -1)) {
                        isEqual = true;
                        break;
                    }

                }
            }

        } while (isEqual);

        used.add(result);

        return result;
    }

}
