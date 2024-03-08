package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        countEuclideanDistanceFromFile("src/resources/kroA100.tsp");
        countEuclideanDistanceFromFile("src/resources/kroB100.tsp");
    }

    private static String countEuclideanDistanceFromFile(String path){
        List<List<String>> preparedLinesFromFile = prepareLinesFromFile(path);
        String euclideanDistance = getEuclideanDistance(preparedLinesFromFile);
        return euclideanDistance;
    }


    private static List<List<String>> prepareLinesFromFile(String path) {
        List<List<String>> correctsLines = new ArrayList<>();
        Integer count = -1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                List<String> correctLine = new ArrayList<>();
                correctLine.add(line);
                count++;
                if (count > 5 && count < 106) {
                    correctsLines.add(correctLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return correctsLines;
    }

    private static String getEuclideanDistance(List<List<String>> preparedLinesFromFile){
        if (preparedLinesFromFile.size() > 1) {
            for (int i = 0; i < preparedLinesFromFile.size(); i++) {
                for (int j = 0; j < preparedLinesFromFile.size(); j++){
                    String[] firstList = preparedLinesFromFile.get(i).get(0).split("\\s+");
                    String numberFirst = firstList[0];
                    int first = Integer.parseInt(numberFirst);
                    int x1 = Integer.parseInt(firstList[1]);
                    int y1 = Integer.parseInt(firstList[2]);


                    String[] secondList = preparedLinesFromFile.get(j).get(0).split("\\s+");
                    String numberSecond = secondList[0];
                    int dwa = Integer.parseInt(numberSecond);
                    int x2 = Integer.parseInt(secondList[1]);
                    int y2 = Integer.parseInt(secondList[2]);

                    Long countEuclideanDistance = countEuclideanDistance(x1, y1, x2, y2);
                    System.out.print(countEuclideanDistance + " ");
                }
                System.out.println();
            }
        }
        return "hej";
    }

    private static Long countEuclideanDistance(Integer x1, Integer y1, Integer x2, Integer y2){
        Integer squareFirstSubstraction = (x2-x1)*(x2-x1);
        Integer squareSecondSubstraction = (y2-y1)*(y2-y1);
        Double distance =Math.sqrt(squareFirstSubstraction+squareSecondSubstraction);
        Long roundDistance = Math.round(distance);
        return roundDistance;
    }


}