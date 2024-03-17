package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Instance kroA100 = new Instance("src/resources/kroA100.tsp");
//        Instance kroB100 = new Instance("src/resources/kroB100.tsp");

        List<RegretHeuristic> solutions = new ArrayList<RegretHeuristic>();
        for (int i = 0; i < kroA100.getDistanceMatrix().size(); i++) {
            List<Integer> distance = (kroA100.getDistanceMatrix()).get(i);
            int j = distance.indexOf(Collections.max(distance));
            solutions.add(new RegretHeuristic(kroA100, i, j));
        }
        int best = 0;
        double cost = 999999999;
        for (int i = 0; i < solutions.size(); i++) {
            if (solutions.get(i).achievedValue < cost) {
                best = i;
                cost = solutions.get(i).achievedValue;
            }
        }
        List<Double> criteriumValues=new ArrayList<>();
        for (RegretHeuristic solution: solutions) {
            criteriumValues.add(solution.getSolutionValue());
        }
        System.out.println(criteriumValues.stream().max(Double::compareTo));
        System.out.println(criteriumValues.stream().min(Double::compareTo));
        if(criteriumValues.stream().reduce(Double::sum).isPresent()){
            System.out.println(criteriumValues.stream().reduce(Double::sum).get()/Double.parseDouble(String.valueOf(criteriumValues.size())));
        }

        solutions.get(best).solutionToCsv("best.csv",kroA100);
//        List<List<Integer>> sol = new getSolution();
    }
}