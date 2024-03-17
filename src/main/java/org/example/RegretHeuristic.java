package org.example;

import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public  class RegretHeuristic {
    List<List<Integer>> solution;
    List<Integer> cycleA;
    List<Integer> cycleB;
    double achievedValue=0;
    double cycleALength=0;
    double cycleBLength=0;
    private List<Double> calcCost(List<Integer> current ,int index, List<List<Integer>> distanceMatrix){
        List<Double> cost=new ArrayList<>();
        for(Integer i:current){
            cost.add(new Double(distanceMatrix.get(index).get(i)));
        }
        return cost;
    }
    private Pair<Integer, Integer> findVertex(List<Integer> current,double currentLength,List<Integer> availableVert,List<List<Integer>> distanceMatrix){
        List<Pair<Pair<Integer,Integer>, Double>> bests = new ArrayList<>(); // pozycja/miastp, regret

        for(int choosedVertex:availableVert) {
            List<Pair<Integer, Double>>lengthOfPossibleInsertions=new ArrayList<>();
            for(int i=1;i<current.size();i++){
                lengthOfPossibleInsertions.add(new Pair<>(i,currentLength-
                        distanceMatrix.get(current.get(i - 1)).get(current.get(i))
                        +distanceMatrix.get(current.get(i - 1)).get(choosedVertex)
                        +distanceMatrix.get(choosedVertex).get(current.get(i))));
            }
            lengthOfPossibleInsertions.sort(new Comparator<Pair<Integer, Double>>() {
                @Override
                public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                    if (o1.getValue() > o2.getValue()) {
                        return 1;
                    } else if (o1.getValue().equals(o2.getValue())) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            double regret=0;
            if(lengthOfPossibleInsertions.size()>=3){
                regret= lengthOfPossibleInsertions.get(1).getValue() - lengthOfPossibleInsertions.get(0).getValue();
            }else{
                regret= - lengthOfPossibleInsertions.get(0).getValue();
            }
            bests.add(new Pair<>(new Pair<>(lengthOfPossibleInsertions.get(0).getKey(),choosedVertex),regret));
//            for (int choosedVertex : availableVert) {
//                Map.Entry<Integer, Double> firstEntry = calcRegret(current, choosedVertex, distanceMatrix).entrySet().iterator().next();
//                bestVertex.put(firstEntry.getKey(), firstEntry.getValue());
//                regret.add(firstEntry.getValue());
//            }
//            LinkedHashMap<Integer, Double> result = bestVertex.entrySet().stream()
//                    .sorted(Map.Entry.<Integer, Double>comparingByValue(byRegretDescending))
//                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//            Map.Entry<Integer, Double> firstEntry = result.entrySet().iterator().next();
//            int vertex = current.get(regret.indexOf(Collections.max(regret)));
//            int position = firstEntry.getKey();
//            HashMap<Integer, Integer> resultVertexPosition = new HashMap<>();
//            resultVertexPosition.put(position, vertex);
        }
        bests.sort(new Comparator<Pair<Pair<Integer,Integer>, Double>>() {
            @Override
            public int compare(Pair<Pair<Integer,Integer>, Double> o1, Pair<Pair<Integer,Integer>, Double> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return -1;
                } else if (o1.getValue().equals(o2.getValue())) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        return new Pair<>(bests.get(0).getKey().getKey(),bests.get(0).getKey().getValue());
    }

//    private HashMap<Integer,Double> calcRegret(List<Integer> current, Integer choosedVertex ,List<List<Integer>> distanceMatrix){
//        HashMap<Integer,Double> regret=new HashMap<>();
//        Comparator<Double> byCost = Double::compareTo;
//
//        for(int i = 0; i < current.size()-1; i++){
//            List<Integer> tempCycle = new ArrayList<>(current);
//            tempCycle.add(i,choosedVertex);
//            double lengthCycle = 0;
//            for(int j =0; j < tempCycle.size()-2; j++){
//                lengthCycle += distanceMatrix.get(tempCycle.get(j)).get(tempCycle.get(j+1));
//            }
//            lengthCycle+=distanceMatrix.get(tempCycle.get(tempCycle.size()-1)).get(tempCycle.get(0));
//            regret.put(i, lengthCycle);
//        }
//        LinkedHashMap<Integer, Double> result=regret.entrySet().stream()
//                .sorted(Map.Entry.<Integer, Double>comparingByValue(byCost))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//        Map.Entry<Integer, Double> firstEntry = result.entrySet().iterator().next();
//        Map.Entry<Integer, Double> secondEntry = result.entrySet().iterator().next();
//        HashMap<Integer,Double> positionRegret=new HashMap<>();
//        positionRegret.put(firstEntry.getKey(),(secondEntry.getValue()-firstEntry.getValue()));
//        return positionRegret;
//    }
    private void solve(Instance instance,int startingPoint1, int startingPoint2){
        List<List<Integer>> distanceMatrix = instance.getDistanceMatrix();
        Set<Integer> availableVert = IntStream.rangeClosed(0, distanceMatrix.size() - 1).boxed().collect(Collectors.toSet());
        availableVert.remove(startingPoint1);
        availableVert.remove(startingPoint2);
        Pair<Integer,Integer> choosedVertex ;
        for(int it=2;it<distanceMatrix.size();it++){
            if(it%2==0){
                choosedVertex = findVertex(cycleA,cycleALength, new ArrayList<>(availableVert),distanceMatrix);
                cycleA.add(choosedVertex.getKey(), choosedVertex.getValue());
                availableVert.remove(choosedVertex.getValue());
            }else {
                choosedVertex = findVertex(cycleB,cycleBLength, new ArrayList<>(availableVert),distanceMatrix);
                cycleB.add(choosedVertex.getKey(), choosedVertex.getValue());
                availableVert.remove(choosedVertex.getValue());
            }
        }

        solution.add(cycleA);
        solution.add(cycleB);
        cycleALength=0;
        for(int i =0; i < cycleA.size()-1; i++){
            cycleALength += distanceMatrix.get(cycleA.get(i)).get(cycleA.get(i+1));
        }
        cycleBLength=0;
        for(int i =0; i < cycleB.size()-1; i++){
            cycleBLength += distanceMatrix.get(cycleB.get(i)).get(cycleB.get(i+1));
        }
        achievedValue = getAchievedValue(cycleA,cycleB,distanceMatrix);
    }

    public List<List<Integer>> getSolution(){

        return solution;
    }
    private double getAchievedValue(List<Integer> cycleA, List<Integer> cycleB,List<List<Integer>> distanceMatrix){
        double solutionValue = 0.0;
        for(int i =0; i < cycleA.size()-2; i++){
            solutionValue += distanceMatrix.get(cycleA.get(i)).get(cycleA.get(i+1));
        }
        for(int i =0; i < cycleB.size()-2; i++){
            solutionValue += distanceMatrix.get(cycleB.get(i)).get(cycleB.get(i+1));
        }
        solutionValue+=distanceMatrix.get(cycleA.get(cycleA.size()-1)).get(cycleA.get(0));
        solutionValue+=distanceMatrix.get(cycleB.get(cycleB.size()-1)).get(cycleB.get(0));
        return solutionValue;
    }

    public double getSolutionValue(){
        return achievedValue;
    }
    RegretHeuristic(Instance instance,int startingPoint1,int startingPoint2){
            cycleA=new ArrayList<>();
            cycleB=new ArrayList<>();
            solution=new ArrayList<>();

            cycleA.add(startingPoint1);
            cycleA.add(startingPoint1);

            cycleB.add(startingPoint2);
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
