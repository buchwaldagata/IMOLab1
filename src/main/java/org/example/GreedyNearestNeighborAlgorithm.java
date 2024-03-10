package org.example;

class GreedyNearestNeighborAlgorithm {
    public int runAlgorithm(){
        RandomNumber randomNumber = new RandomNumber();
        int firstVertex = randomNumber.drawNumber(100);
        System.out.println(firstVertex);

        return firstVertex;
    }

}
