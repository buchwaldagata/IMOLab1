package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Main {
    public static void main(String[] args) {

        CoordinateList coordinateList = new CoordinateList("src/resources/kroA100.tsp");
        int[][] intCoordinateList = coordinateList.intCoordinateList;
        DistanceMatrix distanceMatrix = new DistanceMatrix(intCoordinateList);
        Long[][] distanceMatrix2 = distanceMatrix.distanceMatrix;





        displayCoordinatesList(intCoordinateList);
        displayDistanceMatrix(distanceMatrix2);
        System.out.println();

        GreedyNearestNeighborAlgorithm greedyNearestNeighborAlgorithm = new GreedyNearestNeighborAlgorithm();
        int firstVertex = greedyNearestNeighborAlgorithm.runAlgorithm();
        System.out.println(firstVertex);

        System.out.println(intCoordinateList[firstVertex][0]);
        int x1 = intCoordinateList[firstVertex][1];
        int y1 = intCoordinateList[firstVertex][2];
        System.out.println(x1);
        System.out.println(y1);

        saveToFile(firstVertex,x1,y1,"Greedy Nearest Neighbor Algorithm - first");


        System.out.println(distanceMatrix2[firstVertex]);

        Long maxDistance = 0L;
        int maxDistanceNumber = firstVertex;
        for (int j = 0; j < distanceMatrix2[firstVertex].length; j++) {
            if (distanceMatrix2[firstVertex][j] > maxDistance){
                maxDistance = distanceMatrix2[firstVertex][j];
                maxDistanceNumber = j;
            }
            System.out.print(distanceMatrix2[firstVertex][j] + " ");
        }
        System.out.println();
        System.out.println(maxDistance);
        System.out.println(maxDistanceNumber);

        int x2 = intCoordinateList[maxDistanceNumber][1];
        int y2 = intCoordinateList[maxDistanceNumber][2];

//
        saveToFile(maxDistanceNumber,x2,y2,"Greedy Nearest Neighbor Algorithm - second");





    }

    private static void writePointToCsv(BufferedWriter bufferedWriter, int vertex, int x, int y) throws IOException {
        bufferedWriter.write(vertex + "," + x + "," + y);
    }

    private static void printListList(List<List<Integer>> listList) {
        for (List<Integer> row : listList) {
            for (Integer value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
    private static List<List<Integer>> convertStringToListMatrix(List<List<String>> stringMatrix){
        List<List<Integer>> intMatrix = new ArrayList<>();
        for (int i = 0; i < stringMatrix.size(); i++) {
            List<String> row = stringMatrix.get(i);
            List<Integer> row2 = splitAndConvertToNumbers(row.get(0));
            intMatrix.add(row2);
        }
        return intMatrix;
    }

    private static List<Integer> splitAndConvertToNumbers(String inputString) {
        String[] tokens = inputString.split("\\s+");
        return Arrays.asList(
                Integer.parseInt(tokens[0]),
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2])
        );
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

    private static String countEuclideanDistanceFromFile(List<List<String>> preparedLinesFromFile){
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

    private static void displayCoordinatesList(int[][] coordinatesList){
        for (int i = 0; i < coordinatesList.length; i++) {
            for (int j = 0; j < coordinatesList[i].length; j++) {
                System.out.print(coordinatesList[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void displayDistanceMatrix(Long[][] distanceMatrix){
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                System.out.print(distanceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    private static void saveToFile(int firstVertex, int x, int y, String nameOfFile){
        try {
            FileWriter fileWriter = new FileWriter(nameOfFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            writePointToCsv(bufferedWriter, firstVertex, x, y);
            bufferedWriter.close();

            System.out.println("Liczby zostały zapisane do pliku " + nameOfFile);

        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania do pliku: " + e.getMessage());
        }
    }
}