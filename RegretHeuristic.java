package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public  class RegretHeuristic {
    private List<List<Integer>> solution;
    private List<Integer> cycleA;
    private List<Integer> cycleB;
    private double achievedValue=0;
    private Double calcCost(List<Integer> current ,int index, List<List<Integer>> distanceMatrix){
        Double cost=0.0;
        for(Integer i:current){
            cost+= distanceMatrix.get(index).get(i);
        }
        return cost;
    }
    private Integer findVertex(List<Integer> current,List<Integer> availableVert,List<List<Integer>> distanceMatrix){
        HashMap<Integer, Double> costs=new HashMap<>();
        Comparator<Double> byCost = Double::compareTo;
        if(availableVert.size()<=1)
            return availableVert.get(0);
        for(Integer vertIndex:availableVert){
            costs.put(vertIndex,calcCost(current,vertIndex,distanceMatrix));
        }
        LinkedHashMap<Integer, Double> result=costs.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue(byCost))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Iterator iterator = result.entrySet().iterator();
        iterator.next();

        return (Integer) ((Map.Entry)iterator.next()).getKey();
    }
    private void solve(Instance instance,int startingPoint1, int startingPoint2){
        List<List<Integer>> distanceMatrix = instance.getDistanceMatrix();
        Set<Integer> availableVert=new HashSet<Integer>();
        availableVert.addAll(IntStream.rangeClosed(0, distanceMatrix.size()-1).boxed().collect(Collectors.toList()));
        availableVert.remove(startingPoint1);
        availableVert.remove(startingPoint2);
        Integer choosedVertex;
        for(int it=2;it<distanceMatrix.size();it++){
            if(it%2==0){
                choosedVertex = findVertex(cycleA, new ArrayList<Integer>(availableVert),distanceMatrix);
                availableVert.remove(choosedVertex);
                achievedValue+=distanceMatrix.get(cycleA.get(cycleA.size()-1)).get(choosedVertex);
                cycleA.add(choosedVertex);
            }else {
                choosedVertex = findVertex(cycleB, new ArrayList<Integer>(availableVert),distanceMatrix);
                availableVert.remove(choosedVertex);
                achievedValue+=distanceMatrix.get(cycleB.get(cycleB.size()-1)).get(choosedVertex);
                cycleB.add(choosedVertex);
            }
        }
        achievedValue+=distanceMatrix.get(cycleB.get(cycleB.size()-1)).get(0);
        achievedValue+=distanceMatrix.get(cycleA.get(cycleA.size()-1)).get(0);
        solution.add(cycleA);
        solution.add(cycleB);
    }

    public List<List<Integer>> getSolution(){
        return solution;
    }
    public double getAchievedValue(){
        return achievedValue;
    }
//    RegretHeuristic(Instance instance){
//        cycleA=new ArrayList<>();
//        cycleB=new ArrayList<>();
//        solution=new ArrayList<>();
//
//        cycleA.add(0);
//        cycleB.add(1);
//        solve(instance, 0,1);
//    }
    RegretHeuristic(Instance instance,int startingPoint1,int startingPoint2){
            cycleA=new ArrayList<>();
            cycleB=new ArrayList<>();
            solution=new ArrayList<>();

            cycleA.add(startingPoint1);
            cycleB.add(startingPoint2);
            solve(instance, startingPoint1,startingPoint2 );
    }
    public void solutionToCsv(String path,Instance instance) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("cycle,x,y\n");
        for (Integer a : cycleA) {
            printWriter.printf("%s,%d,%d\n","a", instance.coordinates.get(a).getKey(), instance.coordinates.get(a).getValue());
        }
        for (Integer a : cycleB) {
            printWriter.printf("%s,%d,%d\n","b", instance.coordinates.get(a).getKey(), instance.coordinates.get(a).getValue());
        }
        printWriter.close();
    }
}
