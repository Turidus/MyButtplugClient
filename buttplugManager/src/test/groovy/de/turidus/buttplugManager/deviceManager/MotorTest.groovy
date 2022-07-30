package de.turidus.buttplugManager.deviceManager

import de.turidus.buttplugClient.devices.DeviceData
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BiFunction

class MotorTest extends Specification {

    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_vibrateCmd
    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_rotateCmd
    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_linearCmd
    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_stopCmd

    AtomicInteger atomicInteger;


    def setupSpec() {
        deviceMessageAttribute_vibrateCmd = new DeviceData.DeviceMessageAttribute(2, new int[]{10, 20})
        deviceMessageAttribute_rotateCmd = new DeviceData.DeviceMessageAttribute(1, new int[]{10})
        deviceMessageAttribute_linearCmd = new DeviceData.DeviceMessageAttribute(1, new int[]{2})
        deviceMessageAttribute_stopCmd = null
    }

    def setup(){
        atomicInteger = new AtomicInteger();
    }

    def "Linear Motor - Building"() {
        LinearMotor motor = new LinearMotor(0, deviceMessageAttribute_linearCmd.StepCount()[0], atomicInteger.incrementAndGet())
        expect:
        motor.getStepCount() == 2
        motor.isManual()
        motor.groupID == 1
        motor.groupLeader
    }

    def "Linear motor - Set target and calculate next step"() {
        LinearMotor motor = new LinearMotor(0, deviceMessageAttribute_linearCmd.StepCount()[0], atomicInteger.get())
        when:
        motor.setNextTargetAndDurationOfMovement(1d, 1000d)
        motor.calculateNextStep(200)
        then:
        motor.getCurrentStep() == 0
        when:
        motor.calculateNextStep(200)
        then:
        motor.getCurrentStep() == 0.5d
        when:
        motor.calculateNextStep(200)
        then:
        motor.getCurrentStep() == 0.5d
        when:
        motor.calculateNextStep(200)
        then:
        motor.getCurrentStep() == 1d
    }

    def "Linear motor - Set target and calculate next step with less convenient values"() {
        LinearMotor motor = new LinearMotor(0, deviceMessageAttribute_linearCmd.StepCount()[0], atomicInteger.get())
        motor.setNextTargetAndDurationOfMovement(0.733d, 733)
        when:
        motor.calculateNextStep(200)
        then:
        motor.getCurrentStep() == 0
        when:
        motor.calculateNextStep(400)
        then:
        motor.getCurrentStep() == 0.5d
        when:
        motor.calculateNextStep(200)
        then:
        motor.getCurrentStep() == 0.5d
        when:
        motor.calculateNextStep(400)
        then:
        motor.getCurrentStep() == 0.5d
    }

    def "Linear motor - set a speed function and execute it"() {
        LinearMotor motor = new LinearMotor(0, deviceMessageAttribute_linearCmd.StepCount()[0], atomicInteger.get())
        BiFunction function = setFunction()
        motor.setSpeedFunction(function)
        motor.setManual(false)
        when:
        motor.calculateNextStep(10)
        then:
        motor.getCurrentStep() == 0
        when:
        motor.calculateNextStep(90)
        then:
        motor.getCurrentStep() == 0.5d
        when:
        motor.calculateNextStep(10)
        then:
        motor.getCurrentStep() == 0.5d
        when:
        motor.calculateNextStep(90)
        then:
        motor.getCurrentStep() == 1d
        when:
        motor.calculateNextStep(90)
        then:
        motor.getCurrentStep() == 1d
    }

    def "Rotation Motor - Building"() {
        RotationMotor motor = new RotationMotor(0, deviceMessageAttribute_rotateCmd.StepCount()[0], atomicInteger.get())
        when:
        motor.setNextTarget(0.5)
        motor.calculateNextStep(10)
        then:
        motor.getCurrentStep() == 0.5d
        motor.isClockwise()
        when:
        motor.setNextTarget(-0.6)
        motor.calculateNextStep(10)
        then:
        motor.getCurrentStep() == 0.6d
        !motor.isClockwise()
    }

    def "Rotation Motor - Speed function"() {
        RotationMotor motor = new RotationMotor(0, deviceMessageAttribute_rotateCmd.StepCount()[0], atomicInteger.get())
        motor.setManual(false)
        motor.setSpeedFunction(setFunction())
        when:
        motor.calculateNextStep(1)
        then:
        motor.getCurrentStep() == 0
        when:
        motor.calculateNextStep(10)
        then:
        motor.getCurrentStep() == 0.1d
        when:
        motor.calculateNextStep(24)
        then:
        motor.getCurrentStep() == 0.2d
        when:
        motor.calculateNextStep(200)
        then:
        motor.getCurrentStep() == 1d
    }

    def "Vibrate Motor - Building"() {
        VibrationMotor motor = new VibrationMotor(0, deviceMessageAttribute_vibrateCmd.StepCount()[0], atomicInteger.get())
        when:
        motor.setNextTarget(0.5)
        motor.calculateNextStep(10)
        then:
        motor.getCurrentStep() == 0.5d
        when:
        motor.setNextTarget(0.6)
        motor.calculateNextStep(10)
        then:
        motor.getCurrentStep() == 0.6d
    }

    def "Vibration Motor - Speed function"() {
        VibrationMotor motor = new VibrationMotor(0, deviceMessageAttribute_vibrateCmd.StepCount()[0], atomicInteger.get())
        motor.setManual(false)
        motor.setSpeedFunction(setFunction())
        when:
        motor.calculateNextStep(1)
        then:
        motor.getCurrentStep() == 0
        when:
        motor.calculateNextStep(10)
        then:
        motor.getCurrentStep() == 0.1d
        when:
        motor.calculateNextStep(24)
        then:
        motor.getCurrentStep() == 0.2d
        when:
        motor.calculateNextStep(200)
        then:
        motor.getCurrentStep() == 1d
    }

    BiFunction setFunction() {
        return (double deltaT, double currentStep) -> {
            0.005d
        }
    }
}
