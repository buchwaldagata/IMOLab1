package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Instance kroA100 = new Instance("src/resources/kroA100.tsp");
//        Instance kroB100 = new Instance("src/resources/kroB100.tsp");
        List<RegretHeuristic> solutions = new ArrayList<RegretHeuristic>();
        for (int i = 0; i < kroA100.getDistanceMatrix().size(); i++) {
            for (int j = i + 1; i < kroA100.getDistanceMatrix().size(); j++) {
                if (j >= kroA100.getDistanceMatrix().size())
                    break;
                solutions.add(new RegretHeuristic(kroA100, i, j));
            }
        }
//        int best = 0;
//        double cost = 999999999;
        List<Double> criteriumValues=new ArrayList<>();
        for (RegretHeuristic solution: solutions) {
            criteriumValues.add(solution.getAchievedValue());
//            if (solutions.get(i).achievedValue < cost) {
////                best = i;
////                cost = solutions.get(i).achievedValue;
//            }
        }
        System.out.println(criteriumValues.stream().max(Double::compareTo));
        System.out.println(criteriumValues.stream().min(Double::compareTo));
        if(criteriumValues.stream().reduce(Double::sum).isPresent()){
            System.out.println(criteriumValues.stream().reduce(Double::sum).get()/Double.parseDouble(String.valueOf(criteriumValues.size())));
        }

//        solutions.get(best).solutionToCsv("best.csv",kroB100);
//        List<List<Integer>> sol = new getSolution();
    }
}