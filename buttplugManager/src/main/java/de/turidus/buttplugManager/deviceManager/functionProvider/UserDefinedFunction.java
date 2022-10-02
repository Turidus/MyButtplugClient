package de.turidus.buttplugManager.deviceManager.functionProvider;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class UserDefinedFunction {

    private final TreeMap<Double, AbstractFunction> functionMap = new TreeMap<>();
    private       double                            currentTimeStep;

    public double getNextValue(double deltaT) {
        return getValueAtTime(getNextTime(deltaT));
    }

    public double getValueAtTime(double time) {
        if(functionMap.isEmpty() || time < 0) {return 0;}
        Map.Entry<Double, AbstractFunction> entry = functionMap.floorEntry(time);
        return entry.getValue().apply(time - entry.getKey());
    }

    /**
     * Inserts a function at the end of the last function.
     *
     * @param abstractFunction
     *         The function to be inserted.
     */
    public void addFunction(AbstractFunction abstractFunction) {
        functionMap.put(calculateKey(), abstractFunction);
    }

    /**
     * Inserts a function into the underlying map between the function with a lower starting point then {@code time} and
     * the function with a greater or equal starting point.
     *
     * @param time
     *         The time used to find the insertion point.
     * @param abstractFunction
     *         The function to be inserted.
     */
    public void insertFunction(Double time, AbstractFunction abstractFunction) {
        if(time > functionMap.lastKey()) {
            addFunction(abstractFunction);
            return;
        }
        insertByTime(time, abstractFunction);
    }

    /**
     * Inserts a function before the pivot function
     *
     * @param pivotFunction
     *         The function used to find the insert point.
     * @param functionToBeInserted
     *         The function to be inserted.
     */
    public void insertBefore(AbstractFunction pivotFunction, AbstractFunction functionToBeInserted) {
        if(!functionMap.containsValue(pivotFunction)) {throw new RuntimeException("Could not find pivotFunction.");}
        insertByTime(findKeyOfFunction(pivotFunction), functionToBeInserted);
    }

    /**
     * Inserts a function after the pivot function
     *
     * @param pivotFunction
     *         The function used to find the insert point.
     * @param functionToBeInserted
     *         The function to be inserted.
     */
    public void insertAfter(AbstractFunction pivotFunction, AbstractFunction functionToBeInserted) {
        if(!functionMap.containsValue(pivotFunction)) {throw new RuntimeException("Could not find pivotFunction.");}
        double pivotKey = findKeyOfFunction(pivotFunction);
        if(pivotKey == functionMap.lastKey()) {
            addFunction(functionToBeInserted);
        }
        else {
            insertByTime(functionMap.higherKey(pivotKey), functionToBeInserted);
        }
    }

    public void removeFunction(AbstractFunction abstractFunction) {
        if(!functionMap.containsValue(abstractFunction)) {return;}
        double keyToBeRemoved = findKeyOfFunction(abstractFunction);
        double offset         = -functionMap.remove(keyToBeRemoved).maxRange;
        getMovedFunctions(offset, functionMap.tailMap(keyToBeRemoved)).forEach(function -> functionMap.put(function.key, function.value));
    }

    private double getNextTime(double deltaT) {
        currentTimeStep += deltaT;
        double maxXValue = functionMap.lastKey() + functionMap.lastEntry().getValue().maxRange;
        return maxXValue == 0 ? 0 : currentTimeStep % maxXValue;
    }

    private double findKeyOfFunction(AbstractFunction pivotFunction) {
        return functionMap.entrySet().stream().filter(entry -> entry.getValue().equals(pivotFunction)).findFirst().orElseThrow().getKey();
    }

    private void insertByTime(Double time, AbstractFunction abstractFunction) {
        double insertTime = functionMap.ceilingKey(time);
        getMovedFunctions(abstractFunction.maxRange, functionMap.tailMap(time)).forEach(function -> functionMap.put(function.key, function.value));
        functionMap.put(insertTime, abstractFunction);
    }

    private ArrayList<MovedFunctions> getMovedFunctions(double offset, Map<Double, AbstractFunction> tailMap) {
        ArrayList<MovedFunctions> newMovedFunctions = new ArrayList<>();
        for(Map.Entry<Double, AbstractFunction> entry : Set.copyOf(tailMap.entrySet())) {
            tailMap.remove(entry.getKey());
            newMovedFunctions.add(new MovedFunctions(entry.getKey() + offset, entry.getValue()));
        }
        return newMovedFunctions;
    }

    private Double calculateKey() {
        if(functionMap.isEmpty()) {return 0d;}
        Map.Entry<Double, AbstractFunction> entry = functionMap.lastEntry();
        return entry.getKey() + entry.getValue().maxRange;
    }


    private record MovedFunctions(double key, AbstractFunction value) {}

}
