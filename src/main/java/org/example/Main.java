package org.example;

import java.io.*;

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

        int[] table = createTable();

        // delete first and last vertex from table and distanceMatrix
        int[] newTable = deleteIndexFromTable(firstVertex, table);
        int[] secondNewTable = deleteIndexFromTable(searchIndex(maxDistanceNumber, newTable), newTable);


        // wybbor nowego wierzcholka i nowej tabeli
        Object[] object = selectNextVertex(distanceMatrix2,firstVertex, intCoordinateList, fileWriter, bufferedWriter, nameOfFile,secondNewTable);
        secondNewTable = (int[]) object[1];
        int firstNewVertex = (int) object[0];
        int[] newTable2 = deleteIndexFromTable(firstNewVertex, secondNewTable);

        Object[] object2 = selectNextVertex(distanceMatrix2,maxDistanceNumber, intCoordinateList, secondFileWriter, secondBufferedWriter, secondNameOfFile,secondNewTable);
        secondNewTable = (int[]) object2[1];
        int secondNewVertex = (int) object2[0];
        int[] newTableBySecondVertex = deleteIndexFromTable(firstNewVertex, secondNewTable);


        int leftVertexFirst = firstVertex;
        int rightVertexFirst = firstNewVertex;
        int leftVertexSecond = maxDistanceNumber;
        int rightVertexSecond = secondNewVertex;

        for (int i =0; i< distanceMatrix2.length/2 - 4; i++){
            //obliczany lewy
            Object[] objectTmp = selectNextVertex(distanceMatrix2,leftVertexFirst, intCoordinateList, fileWriter, bufferedWriter, nameOfFile,secondNewTable);
            int[] secondNewTableTmp = (int[]) object[1];
            int firstNewVertexTmp = (int) object[0];


            //obliczany prawy
            Object[] objectTmp2 = selectNextVertex(distanceMatrix2,rightVertexFirst, intCoordinateList, fileWriter, bufferedWriter, nameOfFile,secondNewTable);
            int[] secondNewTableTmp2 = (int[]) object[1];
            int firstNewVertexTmp2 = (int) object[0];


            if(distanceMatrix2[leftVertexFirst][firstNewVertexTmp] <=distanceMatrix2[rightVertexFirst][firstNewVertexTmp2] ){

            }
            else {

            }
//            int[] newTableTmp = deleteIndexFromTable(firstNewVertexTmp, secondNewTableTmp);

//            Object[] object2 = selectNextVertex(distanceMatrix2,maxDistanceNumber, intCoordinateList, secondFileWriter, secondBufferedWriter, secondNameOfFile,secondNewTable);
//            secondNewTable = (int[]) object2[1];
//            int secondNewVertex = (int) object2[0];
//            int[] newTableBySecondVertex = deleteIndexFromTable(firstNewVertex, secondNewTable);

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

    private static void displayTable(int[] table){
        for (int element : table) {
            System.out.print(element + " ");
        }
    }

    private static Object[] selectNextVertex
            (Long[][] matrix, int vertex, int[][] coordinateList, FileWriter fileWriter, BufferedWriter bufferedWriter, String nameOfFile,int[] table){
        int firstDistance = 1000000;
        int firstNextVertex = -1;
        for (int j = 0; j < matrix.length; j++){
//            for (int element : tablica) {
//                if (element == szukanaLiczba) {
//                    liczbaZnaleziona = true;
//                    break;
//                }
//            }


            int distance = Math.toIntExact(matrix[vertex][j]);
            if (distance != 0 && distance < firstDistance){
                firstDistance = distance;
                firstNextVertex = j;
            }
        }

        deleteIndexFromTable(searchIndex(firstNextVertex, table), table);
        int x = coordinateList[firstNextVertex][1];
        int y = coordinateList[firstNextVertex][2];
        saveToFile(fileWriter,bufferedWriter,firstNextVertex,x,y,nameOfFile);
        Object[] result = {firstNextVertex, table};
        return result;
    }
    private static int searchIndex(int searchNumber, int[] table){
        int index = -1;
        for (int i = 0; i < table.length; i++) {
            if (table[i] == searchNumber) {
                index = i;
                break;
            }
        }
        return index;
    }

    private static Long[][] removeRowAndColumn(Long[][] matrix, int rowIndex) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        Long[][] modifiedMatrix = new Long[numRows - 1][numCols - 1];

        for (int i = 0, k = 0; i < numRows; i++) {
            if (i == rowIndex) {
                continue; // Skip the row to be removed
            }

            for (int j = 0, l = 0; j < numCols; j++) {
                if (j == rowIndex) {
                    continue; // Skip the column to be removed
                }

                modifiedMatrix[k][l] = (long) Math.toIntExact(matrix[i][j]);
                l++;
            }

            k++;
        }

        return modifiedMatrix;
    }


}