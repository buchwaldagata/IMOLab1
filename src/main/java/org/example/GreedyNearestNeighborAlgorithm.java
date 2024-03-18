package org.example;

import java.util.HashMap;
import java.util.Map;

class GreedyNearestNeighborAlgorithm {
    public Long runAlgorithm(int firstVertex, int[][] intCoordinateList, Long[][] distanceMatrix2 ){
        // key - visit number
        // value - vertex index
        Map<Integer, Integer> firstCycle = new HashMap<>();
        Map<Integer, Integer> secondCycle = new HashMap<>();

        String nameOfFile = "Greedy neighbour first "+ firstVertex;
        String secondNameOfFile = "Greedy neighbour second " + firstVertex;

        firstCycle.put(0, firstVertex);
//        System.out.println(Arrays.toString(distanceMatrix2[firstVertex]));

        Long maxDistance = 0L;
        int maxDistanceNumber = firstVertex;
        for (int j = 0; j < distanceMatrix2[firstVertex].length; j++) {
            if (distanceMatrix2[firstVertex][j] > maxDistance){
                maxDistance = distanceMatrix2[firstVertex][j];
                maxDistanceNumber = j;
            }
//            System.out.print(distanceMatrix2[firstVertex][j] + " ");
        }
//        System.out.println();
//        System.out.println(maxDistance);
//        System.out.println(maxDistanceNumber);

        secondCycle.put(0, maxDistanceNumber);
        int[] table = createTable();

        // delete first and last vertex from table and distanceMatrix
        int[] newTable = deleteIndexFromTable(firstVertex, table);
        int[] secondNewTable = deleteIndexFromTable(searchIndex(maxDistanceNumber, newTable), newTable);


        // wybbor nowego wierzcholka i nowej tabeli
        Object[] object = selectNextVertex(distanceMatrix2,firstVertex, secondNewTable);

        int firstNewVertex = (int) object[0];
        int[] newTable2 = deleteVertexFromTable(firstNewVertex, secondNewTable);
        firstCycle.put(1, firstNewVertex);

        Object[] object2 = selectNextVertex(distanceMatrix2,maxDistanceNumber, newTable2);
        int secondNewVertex = (int) object2[0];

        int[] newTableBySecondVertex = deleteVertexFromTable(secondNewVertex, newTable2);
        secondCycle.put(1, secondNewVertex);

        fillingFiles(firstVertex,firstNewVertex,maxDistanceNumber,secondNewVertex,newTableBySecondVertex,
                distanceMatrix2, firstCycle, secondCycle);

        firstCycle.put(firstCycle.keySet().size(), firstCycle.get(0));
        secondCycle.put(secondCycle.keySet().size(), secondCycle.get(0));

        // save cycles to files
        Main.saveCycle(intCoordinateList, firstCycle, nameOfFile);
        Main.saveCycle(intCoordinateList, secondCycle, secondNameOfFile);

        return Main.getLengthFromCycle(firstCycle, distanceMatrix2) + Main.getLengthFromCycle(secondCycle,distanceMatrix2);
    }

    private static int[] createTable(){
        int[] table = new int[100]; // TODO: zmienić wszystkie 100 na długość tablicy
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
    (Long[][] matrix, int vertex, int[] table){
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
    private static int[] deleteVertexFromTable(int firstNextVertex, int[] table){
        return deleteIndexFromTable(searchIndex(firstNextVertex, table), table);
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

    private static void fillingFiles(int firstVertex, int firstNewVertex, int maxDistanceNumber, int secondNewVertex,
                                     int[] newTableBySecondVertex, Long[][] distanceMatrix2,
                                     Map<Integer, Integer> firstCycle, Map<Integer, Integer> secondCycle){
        int leftVertexFirst = firstVertex;
        int rightVertexFirst = firstNewVertex;
        int leftVertexSecond = maxDistanceNumber;
        int rightVertexSecond = secondNewVertex;
//        distanceMatrix2.length/2 - 4
        int[] tableOfficial = newTableBySecondVertex;

        for (int i =0; i< distanceMatrix2.length/2 - 2; i++){
            //left
            Object[] objectTmpFirstLeft = selectNextVertex(distanceMatrix2,leftVertexFirst, tableOfficial);
            int distanceTmpFirstLeft = (int) objectTmpFirstLeft[1];
            int newVertexTmpFirstLeft = (int) objectTmpFirstLeft[0];

            //right
            Object[] objectTmpFirstRight = selectNextVertex(distanceMatrix2,rightVertexFirst, tableOfficial);
            int distanceTmpFirstRight = (int) objectTmpFirstRight[1];
            int newVertexTmpFirstRight = (int) objectTmpFirstRight[0];


            if(distanceTmpFirstLeft <= distanceTmpFirstRight ){
                tableOfficial = deleteVertexFromTable(newVertexTmpFirstLeft, tableOfficial);
                leftVertexFirst = newVertexTmpFirstLeft;
                for(int index=firstCycle.keySet().size()-1; index>=0; index--) {
                    firstCycle.put(index+1, firstCycle.get(index));
                }
                firstCycle.put(0, newVertexTmpFirstLeft);
            }
            else {
                tableOfficial = deleteVertexFromTable(newVertexTmpFirstRight, tableOfficial);
                rightVertexFirst = newVertexTmpFirstRight;
                firstCycle.put(firstCycle.keySet().size(), newVertexTmpFirstRight);
            }

            //second left
            Object[] objectTmpSecondLeft = selectNextVertex(distanceMatrix2,leftVertexSecond, tableOfficial);
            int distanceTmpSecondLeft = (int) objectTmpSecondLeft[1];
            int newVertexTmpSecondLeft = (int) objectTmpSecondLeft[0];

            //second right
            Object[] objectTmpSecondRight = selectNextVertex(distanceMatrix2,rightVertexSecond, tableOfficial);
            int distanceTmpSecondRight = (int) objectTmpSecondRight[1];
            int newVertexTmpSecondRight = (int) objectTmpSecondRight[0];

            if(distanceTmpSecondLeft <= distanceTmpSecondRight ){
                tableOfficial = deleteVertexFromTable(newVertexTmpSecondLeft, tableOfficial);
                leftVertexSecond = newVertexTmpSecondLeft;
                for(int index=secondCycle.keySet().size()-1; index>=0; index--) {
                    secondCycle.put(index+1, secondCycle.get(index));
                }
                secondCycle.put(0, newVertexTmpSecondLeft);
            }
            else {
                tableOfficial = deleteVertexFromTable(newVertexTmpSecondRight, tableOfficial);
                rightVertexSecond = newVertexTmpSecondRight;
                secondCycle.put(secondCycle.keySet().size(), newVertexTmpSecondRight);
            }
        }
    }
}
