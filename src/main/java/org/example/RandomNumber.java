package org.example;

import java.util.Random;

class RandomNumber {
    public int drawNumber(int number){
        Random randomNumber = new Random();
        return randomNumber.nextInt(number);

    }
}
