package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String distanceMatrixKroAString = countEuclideanDistanceFromFile("src/resources/kroA100.tsp");
        String distanceMatrixKroBString = countEuclideanDistanceFromFile("src/resources/kroB100.tsp");
        int[][] distanceMatrixKroA = parseStringToDistanceMatrix(distanceMatrixKroAString);
        int [][] distanceMatrixKroB = parseStringToDistanceMatrix(distanceMatrixKroBString);
        displayDistanceMatrix(distanceMatrixKroA);
        System.out.println();
        displayDistanceMatrix(distanceMatrixKroB);

    }

    private static String countEuclideanDistanceFromFile(String path){
        List<List<String>> preparedLinesFromFile = prepareLinesFromFile(path);
        return getEuclideanDistance(preparedLinesFromFile);
    }


    private static List<List<String>> prepareLinesFromFile(String path) {
        List<List<String>> correctsLines = new ArrayList<>();
        int count = -1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> correctLine = new ArrayList<>();
                correctLine.add(line);
                count++;
                if (count > 5 && count < 106) { //todo: change 5 and 106
                    correctsLines.add(correctLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return correctsLines;
    }

    private static String getEuclideanDistance(List<List<String>> preparedLinesFromFile){
        StringBuilder distanceMatrix = new StringBuilder();
        if (preparedLinesFromFile.size() > 1) {
            for (int i = 0; i < preparedLinesFromFile.size(); i++) {
                for (int j = 0; j < preparedLinesFromFile.size(); j++){
                    String[] firstList = preparedLinesFromFile.get(i).get(0).split("\\s+");
                    int x1 = Integer.parseInt(firstList[1]);
                    int y1 = Integer.parseInt(firstList[2]);

                    String[] secondList = preparedLinesFromFile.get(j).get(0).split("\\s+");
                    int x2 = Integer.parseInt(secondList[1]);
                    int y2 = Integer.parseInt(secondList[2]);

                    Long countEuclideanDistance = countEuclideanDistance(x1, y1, x2, y2);
                    distanceMatrix.append(countEuclideanDistance).append(" ");
                }
                distanceMatrix.append("\n");
            }
        }
        return distanceMatrix.toString();
    }

    private static Long countEuclideanDistance(Integer x1, Integer y1, Integer x2, Integer y2){
        Integer squareFirstSubtraction = subtractionSquared(x1, x2);
        Integer squareSecondSubtraction = subtractionSquared(y1, y2);
        double distance = Math.sqrt(squareFirstSubtraction+squareSecondSubtraction);
        return Math.round(distance);
    }

    private static Integer subtractionSquared(Integer a, Integer b){
        return ((b-a)*(b-a));
    }

    private static int[][] parseStringToDistanceMatrix(String stringDistanceMatrix){
        String[] rows = stringDistanceMatrix.split("\n");
        int rowCount = rows.length;
        int[][] matrix = new int[rowCount][];

        for (int i = 0; i < rowCount; i++) {
            String[] elements = rows[i].split(" ");
            int columnCount = elements.length;
            matrix[i] = new int[columnCount];

            for (int j = 0; j < columnCount; j++) {
                matrix[i][j] = Integer.parseInt(elements[j]);
            }
        }
        return matrix;
    }

    private static void displayDistanceMatrix(int[][] distanceMatrix){
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                System.out.print(distanceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}