package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.list;
import static java.util.EnumSet.range;

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
        String nameOfFile = "Greedy neighbour first";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(nameOfFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        saveToFile(fileWriter,bufferedWriter,firstVertex,x1,y1,nameOfFile);
        saveToFile(fileWriter,bufferedWriter,2,3,4,nameOfFile);

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

        String secondNameOfFile = "Greedy neighbour second";
        FileWriter secondFileWriter = null;
        try {
            secondFileWriter = new FileWriter(secondNameOfFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter secondBufferedWriter = new BufferedWriter(secondFileWriter);

        saveToFile(secondFileWriter,secondBufferedWriter,maxDistanceNumber,x2,y2,secondNameOfFile);


        //tworzenie tablicy
        int[] table = createTable();

        // usuniecie z niej wierzcholka pierwszego i ostatniego

        int indexToDelete = firstVertex;
        int[] newTable = deleteIndexFromTable(indexToDelete, table);
        int[] secondNewTable = deleteIndexFromTable(maxDistanceNumber, newTable);


        for (int element : secondNewTable) {
            System.out.print(element + " ");
        }

        try {
            bufferedWriter.close();
            secondBufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static void writePointToCsv(BufferedWriter bufferedWriter, int vertex, int x, int y) throws IOException {
        bufferedWriter.write(vertex + "," + x + "," + y + "\n");
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
    
    private static void saveToFile(FileWriter fileWriter, BufferedWriter bufferedWriter, int firstVertex, int x, int y, String nameOfFile){
        try {
            writePointToCsv(bufferedWriter, firstVertex, x, y);
            System.out.println("Liczby zostały zapisane do pliku " + nameOfFile);

        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania do pliku: " + e.getMessage());
        }
    }
    private static int[] createTable(){
        int[] table = new int[100];
        for (int i = 0; i < 100; i++) {
            table[i] = i;
        }
        return table;
    }
    private static int[] deleteIndexFromTable(int indexToDelete, int[] table){

        for (int i = indexToDelete; i < table.length - 1; i++) {
            table[i] = table[i + 1];
        }

        int[] newTable = new int[table.length - 1];
        System.arraycopy(table, 0, newTable, 0, newTable.length);

        return newTable;
    }

}