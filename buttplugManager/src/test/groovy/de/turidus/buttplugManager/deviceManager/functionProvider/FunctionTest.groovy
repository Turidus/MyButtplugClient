package de.turidus.buttplugManager.deviceManager.functionProvider

import spock.lang.Specification

class FunctionTest extends Specification {

    UserDefinedFunction functionProvider

    def setup() {
        functionProvider = new UserDefinedFunction()
    }

    def "A UserDefinedFunction can take multiple Functions which joined to one continues function."() {
        UserDefinedFunction udf = new UserDefinedFunction()
        when:
        udf.addFunction(new ConstantFunction(0.5, 10))
        udf.addFunction(new ConstantFunction(0.1, 10))
        then:
        udf.getValueAtTime(5) == 0.5d
        udf.getValueAtTime(15) == 0.1d
        udf.getValueAtTime(20) == 0d
    }

    def """A UserDefinedFunction can take insert a function between other functions by time.
            The method looks for the closest insertion point greater than the given time."""() {
        UserDefinedFunction udf = new UserDefinedFunction()
        UserDefinedFunction udf2 = new UserDefinedFunction()
        UserDefinedFunction udf3 = new UserDefinedFunction()
        when:
        udf.addFunction(new ConstantFunction(0.1, 10))
        udf.addFunction(new ConstantFunction(0.2, 10))
        udf.insertFunction(9d, new ConstantFunction(0.3, 10))
        then:
        udf.getValueAtTime(5) == 0.1d
        udf.getValueAtTime(15) == 0.3d
        udf.getValueAtTime(25) == 0.2d
        when:
        udf2.addFunction(new ConstantFunction(0.1, 10))
        udf2.addFunction(new ConstantFunction(0.2, 10))
        udf2.insertFunction(19d, new ConstantFunction(0.3, 10))
        then:
        udf2.getValueAtTime(5) == 0.1d
        udf2.getValueAtTime(15) == 0.2d
        udf2.getValueAtTime(25) == 0.3d
        when:
        udf3.addFunction(new ConstantFunction(0.1, 10))
        udf3.addFunction(new ConstantFunction(0.2, 10))
        udf3.insertFunction(0, new ConstantFunction(0.3, 10))
        then:
        udf3.getValueAtTime(5) == 0.3d
        udf3.getValueAtTime(15) == 0.1d
        udf3.getValueAtTime(25) == 0.2d
    }

    def "UserDefinedFunction can insert a function before an other function."() {
        UserDefinedFunction udf = new UserDefinedFunction()
        UserDefinedFunction udf2 = new UserDefinedFunction()
        ConstantFunction pivotFunction = new ConstantFunction(0.2, 10)
        when:
        udf.addFunction(new ConstantFunction(0.1, 10))
        udf.addFunction(pivotFunction)
        udf.insertBefore(pivotFunction, new ConstantFunction(0.3, 10))
        then:
        udf.getValueAtTime(5) == 0.1d
        udf.getValueAtTime(15) == 0.3d
        udf.getValueAtTime(25) == 0.2d
        when:
        udf2.addFunction(pivotFunction)
        udf2.addFunction(new ConstantFunction(0.1, 10))
        udf2.insertBefore(pivotFunction, new ConstantFunction(0.3, 10))
        then:
        udf2.getValueAtTime(5) == 0.3d
        udf2.getValueAtTime(15) == 0.2d
        udf2.getValueAtTime(25) == 0.1d
    }

    def "UserDefinedFunction can insert a function after an other function."() {
        UserDefinedFunction udf = new UserDefinedFunction()
        UserDefinedFunction udf2 = new UserDefinedFunction()
        ConstantFunction pivotFunction = new ConstantFunction(0.2, 10)
        when:
        udf.addFunction(pivotFunction)
        udf.addFunction(new ConstantFunction(0.1, 10))
        udf.insertAfter(pivotFunction, new ConstantFunction(0.3, 10))
        then:
        udf.getValueAtTime(5) == 0.2d
        udf.getValueAtTime(15) == 0.3d
        udf.getValueAtTime(25) == 0.1d
        when:
        udf2.addFunction(new ConstantFunction(0.1, 10))
        udf2.addFunction(pivotFunction)
        udf2.insertAfter(pivotFunction, new ConstantFunction(0.3, 10))
        then:
        udf2.getValueAtTime(5) == 0.1d
        udf2.getValueAtTime(15) == 0.2d
        udf2.getValueAtTime(25) == 0.3d

    }

    def "UserDefinedFunction can remove a function."() {
        UserDefinedFunction udf = new UserDefinedFunction()
        ConstantFunction functionToBeRemoved = new ConstantFunction(0.2, 10)
        when:
        udf.addFunction(new ConstantFunction(0.1, 10))
        udf.addFunction(functionToBeRemoved)
        udf.addFunction(new ConstantFunction(0.3, 10))
        then:
        udf.getValueAtTime(5) == 0.1d
        udf.getValueAtTime(15) == 0.2d
        udf.getValueAtTime(25) == 0.3d
        when:
        udf.removeFunction(functionToBeRemoved)
        then:
        udf.getValueAtTime(5) == 0.1d
        udf.getValueAtTime(15) == 0.3d
    }

    def "A UserDefinedFunction can take a function and be queried"() {
        UserDefinedFunction udf = new UserDefinedFunction()
        expect:
        udf.getValueAtTime(5) == 0
        when:
        udf.addFunction(new ConstantFunction(0.5, 10))
        then:
        udf.getValueAtTime(5) == 0.5d
        when:
        udf.addFunction(new ConstantFunction(0.1, 10))
        then:
        udf.getValueAtTime(15) == 0.1d
    }

    def "A UserDefinedFunction can be queried by time steps. When the sum of all time steps becomes greater than the range of the UDF, the values roll over"() {
        UserDefinedFunction udf = new UserDefinedFunction()
        udf.addFunction(new ConstantFunction(0.1, 10))
        udf.addFunction(new ConstantFunction(0.9, 10))
        expect:
        udf.getNextValue(0d) == 0.1d
        udf.getNextValue(7d) == 0.1d
        udf.getNextValue(7d) == 0.9d
        udf.getNextValue(7d) == 0.1d
        udf.getNextValue(18d) == 0.9d
        udf.getNextValue(0.5d) == 0.9d
        udf.getNextValue(1d) == 0.1d
    }


    //Single Function Tests

    def "A ConstantFunction has a fixed speed value from 0 to a given time value (exclusive) after which it is zero"() {
        given:
        ConstantFunction func = new ConstantFunction(0.5, 5)
        expect:
        func.apply(a) == b
        where:
        a    | b
        0d   | 0.5d
        2d   | 0.5d
        4.9d | 0.5d
        5d   | 0d
        6d   | 0d
    }

    def "A LinearFunction has a linear rising or falling speed value from 0 to a given time value (exclusive) after which it is zero"() {
        given:
        LinearFunction func = new LinearFunction(0, 1, 10)
        LinearFunction func2 = new LinearFunction(0.9, 0.1, 10)
        expect:
        (b - 0.01) <= func.apply(a) && func.apply(a) <= (b + 0.01)
        (c - 0.01) <= func2.apply(a) && func2.apply(a) <= (c + 0.01)
        where:
        a    | b     | c
        0d   | 0d    | 0.9d
        2d   | 0.2d  | 0.74d
        4.9d | 0.49d | 0.508d
        5d   | 0.5d  | 0.5d
        6d   | 0.6d  | 0.42d
        9d   | 0.9d  | 0.18d
        11d  | 0d    | 0d
    }

    def "A SinusFunction follows a sinus curve from 0 to a given time value (exclusive) after which it is zero"() {
        given:
        SinusFunction func = new SinusFunction(10)
        expect:
        (b - 0.01) <= func.apply(a) && func.apply(a) <= (b + 0.01)
        where:
        a   | b
        0d  | 0.5
        1d  | 0.92
        2d  | 0.95
        3d  | 0.57
        4d  | 0.12
        5d  | 0.02
        6d  | 0.36
        7d  | 0.82
        8d  | 0.99
        9d  | 0.71
        10d | 0

    }

}
