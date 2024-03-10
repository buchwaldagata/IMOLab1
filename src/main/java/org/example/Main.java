package org.example;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        CoordinateList coordinateList = new CoordinateList("src/resources/kroA100.tsp");
        int[][] intCoordinateList = coordinateList.intCoordinateList;
        DistanceMatrix distanceMatrix = new DistanceMatrix(intCoordinateList);
        Long[][] distanceMatrix2 = distanceMatrix.distanceMatrix;



        GreedyNearestNeighborAlgorithm greedyNearestNeighborAlgorithm = new GreedyNearestNeighborAlgorithm();
        greedyNearestNeighborAlgorithm.runAlgorithm(intCoordinateList,distanceMatrix2);

        GreedyCycleAlgorithm greedyCycleAlgorithm = new GreedyCycleAlgorithm();
        greedyCycleAlgorithm.runAlgorithm(intCoordinateList,distanceMatrix2);
    }

    static void saveToFile(FileWriter fileWriter, BufferedWriter bufferedWriter, int firstVertex, int x, int y, String nameOfFile){
        try {
            writePointToCsv(bufferedWriter, firstVertex, x, y);
            System.out.println("Liczby zostały zapisane do pliku " + nameOfFile);

        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania do pliku: " + e.getMessage());
        }
    }

    private static void writePointToCsv(BufferedWriter bufferedWriter, int vertex, int x, int y) throws IOException {
        bufferedWriter.write(vertex + "," + x + "," + y + "\n");
    }
}