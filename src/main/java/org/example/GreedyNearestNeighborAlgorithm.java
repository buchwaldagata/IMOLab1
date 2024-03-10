package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class GreedyNearestNeighborAlgorithm {
    public void runAlgorithm(int[][] intCoordinateList, Long[][] distanceMatrix2 ){

        int firstVertex = chooseRandomNumber(100);
        int x1 = intCoordinateList[firstVertex][1];
        int y1 = intCoordinateList[firstVertex][2];

        String nameOfFile = "Greedy neighbour first";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(nameOfFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        Main.saveToFile(fileWriter,bufferedWriter,firstVertex,x1,y1,nameOfFile);
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

        Main.saveToFile(secondFileWriter,secondBufferedWriter,maxDistanceNumber,x2,y2,secondNameOfFile);

        int[] table = createTable();

        // delete first and last vertex from table and distanceMatrix
        int[] newTable = deleteIndexFromTable(firstVertex, table);
        int[] secondNewTable = deleteIndexFromTable(searchIndex(maxDistanceNumber, newTable), newTable);


        // wybbor nowego wierzcholka i nowej tabeli
        Object[] object = selectNextVertex(distanceMatrix2,firstVertex, intCoordinateList, fileWriter, bufferedWriter, nameOfFile,secondNewTable);

        int distanceA = (int) object[1];
        int firstNewVertex = (int) object[0];
        int[] newTable2 = deleteFromTableAndSaveInFile(firstNewVertex,intCoordinateList, fileWriter, bufferedWriter, nameOfFile,secondNewTable);

        Object[] object2 = selectNextVertex(distanceMatrix2,maxDistanceNumber, intCoordinateList, secondFileWriter, secondBufferedWriter, secondNameOfFile,newTable2);
        int distanceB = (int) object2[1];
        int secondNewVertex = (int) object2[0];

        int[] newTableBySecondVertex = deleteFromTableAndSaveInFile(secondNewVertex, intCoordinateList, secondFileWriter, secondBufferedWriter, secondNameOfFile,newTable2);

        fillingFiles(firstVertex,firstNewVertex,maxDistanceNumber,secondNewVertex,newTableBySecondVertex,distanceMatrix2,intCoordinateList,fileWriter,bufferedWriter,nameOfFile,secondFileWriter,secondBufferedWriter,secondNameOfFile);
        closeBuffer(bufferedWriter, secondBufferedWriter);

    }


    private static int chooseRandomNumber(int number){
        RandomNumber randomNumber = new RandomNumber();
        int firstVertex = randomNumber.drawNumber(number);
        return firstVertex;
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

    private static Object[] selectNextVertex
            (Long[][] matrix, int vertex, int[][] coordinateList, FileWriter fileWriter, BufferedWriter bufferedWriter, String nameOfFile,int[] table){
        int firstDistance = 1000000;
        int firstNextVertex = -1;
        for (int j = 0; j < matrix.length; j++){

            int distance = Math.toIntExact(matrix[vertex][j]);
            if (distance != 0 && distance < firstDistance && isThere(table,j)){
                firstDistance = distance;
                firstNextVertex = j;
            }
        }
        Object[] result = {firstNextVertex, firstDistance};
        return result;
    }
    private static int[] deleteFromTableAndSaveInFile(int firstNextVertex, int[][] coordinateList, FileWriter fileWriter, BufferedWriter bufferedWriter, String nameOfFile,int[] table){
        int[] newTable = deleteIndexFromTable(searchIndex(firstNextVertex, table), table);
        int x = coordinateList[firstNextVertex][1];
        int y = coordinateList[firstNextVertex][2];
        Main.saveToFile(fileWriter,bufferedWriter,firstNextVertex,x,y,nameOfFile);

        return newTable;
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

    private static boolean isThere(int[] table, int value) {
        for (int element : table) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    private static void fillingFiles(int firstVertex, int firstNewVertex, int maxDistanceNumber, int secondNewVertex, int[] newTableBySecondVertex, Long[][] distanceMatrix2, int[][] intCoordinateList,
                                     FileWriter fileWriter, BufferedWriter bufferedWriter, String nameOfFile,
                                     FileWriter secondFileWriter, BufferedWriter secondBufferedWriter, String secondNameOfFile){
        int leftVertexFirst = firstVertex;
        int rightVertexFirst = firstNewVertex;
        int leftVertexSecond = maxDistanceNumber;
        int rightVertexSecond = secondNewVertex;
//        distanceMatrix2.length/2 - 4
        int[] tableOfficial = newTableBySecondVertex;

        for (int i =0; i< distanceMatrix2.length/2 - 2; i++){
            //left
            Object[] objectTmpFirstLeft = selectNextVertex(distanceMatrix2,leftVertexFirst, intCoordinateList, fileWriter, bufferedWriter, nameOfFile,tableOfficial);
            int distanceTmpFirstLeft = (int) objectTmpFirstLeft[1];
            int newVertexTmpFirstLeft = (int) objectTmpFirstLeft[0];

            //right
            Object[] objectTmpFirstRight = selectNextVertex(distanceMatrix2,rightVertexFirst, intCoordinateList, fileWriter, bufferedWriter, nameOfFile,tableOfficial);
            int distanceTmpFirstRight = (int) objectTmpFirstRight[1];
            int newVertexTmpFirstRight = (int) objectTmpFirstRight[0];


            if(distanceTmpFirstLeft <= distanceTmpFirstRight ){
                tableOfficial = deleteFromTableAndSaveInFile(newVertexTmpFirstLeft, intCoordinateList, fileWriter, bufferedWriter, nameOfFile, tableOfficial);
                leftVertexFirst = newVertexTmpFirstLeft;
            }
            else {
                tableOfficial = deleteFromTableAndSaveInFile(newVertexTmpFirstRight, intCoordinateList, fileWriter, bufferedWriter, nameOfFile, tableOfficial);
                rightVertexFirst = newVertexTmpFirstRight;
            }

            //second left
            Object[] objectTmpSecondLeft = selectNextVertex(distanceMatrix2,leftVertexSecond, intCoordinateList, secondFileWriter, secondBufferedWriter, secondNameOfFile,tableOfficial);
            int distanceTmpSecondLeft = (int) objectTmpSecondLeft[1];
            int newVertexTmpSecondLeft = (int) objectTmpSecondLeft[0];

            //second right
            Object[] objectTmpSecondRight = selectNextVertex(distanceMatrix2,rightVertexSecond, intCoordinateList, secondFileWriter, secondBufferedWriter, secondNameOfFile,tableOfficial);
            int distanceTmpSecondRight = (int) objectTmpSecondRight[1];
            int newVertexTmpSecondRight = (int) objectTmpSecondRight[0];

            if(distanceTmpSecondLeft <= distanceTmpSecondRight ){
                tableOfficial = deleteFromTableAndSaveInFile(newVertexTmpSecondLeft, intCoordinateList, secondFileWriter, secondBufferedWriter, secondNameOfFile, tableOfficial);
                leftVertexFirst = newVertexTmpSecondLeft;
            }
            else {
                tableOfficial = deleteFromTableAndSaveInFile(newVertexTmpSecondRight, intCoordinateList, secondFileWriter, secondBufferedWriter, secondNameOfFile, tableOfficial);
                rightVertexFirst = newVertexTmpSecondRight;
            }
        }
    }

    private static void closeBuffer(BufferedWriter bufferedWriter, BufferedWriter secondBufferedWriter){
        try {
            bufferedWriter.close();
            secondBufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
