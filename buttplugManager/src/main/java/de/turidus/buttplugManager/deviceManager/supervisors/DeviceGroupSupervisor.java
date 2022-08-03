package de.turidus.buttplugManager.deviceManager.supervisors;

import de.turidus.buttplugManager.deviceManager.Device;
import de.turidus.buttplugManager.deviceManager.motors.Motor;
import de.turidus.buttplugManager.events.ClockEvent;
import de.turidus.buttplugManager.events.DeviceAddedEvent;
import de.turidus.buttplugManager.events.DeviceRemovedEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DeviceGroupSupervisor {

    public final  Map<Integer, DeviceGroup> deviceGroupMap = new HashMap<>();
    private final EventBus                  eventBus;

    public DeviceGroupSupervisor(@Qualifier("managerEventBus") EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onClockEvent(ClockEvent clockEvent) {
        sortMotorsIntoGroups();
        applyAllLeaderValueToFollowers();
    }

    private void applyAllLeaderValueToFollowers() {
        deviceGroupMap.values().forEach(DeviceGroup::applyLeaderValueToFollowers);
    }

    @Subscribe
    public void onDeviceAddedEvent(DeviceAddedEvent dae) {
        addDeviceToMap(dae.device());
    }

    @Subscribe
    public void onDeviceRemovedEvent(DeviceRemovedEvent dre) {
        removedDeviceFromMap(dre.device());
    }

    private void sortMotorsIntoGroups() {
        List<Motor> motorList = getUnfitMotors();
        motorList.forEach(this::addMotorToMap);
    }

    private List<Motor> getUnfitMotors() {
        List<Motor> motorList = new ArrayList<>();
        deviceGroupMap.values().forEach(dg -> motorList.addAll(dg.removeAllMotorsWithUnfitGroupID()));
        return motorList;
    }

    private void removedDeviceFromMap(Device device) {
        device.getAllMotors().forEach(this::removeMotorFromMap);
    }

    private void removeMotorFromMap(Motor motor) {
        if(!deviceGroupMap.get(motor.groupID).removeMotor(motor)) {
            for(DeviceGroup deviceGroup : deviceGroupMap.values()) {
                if(deviceGroup.removeMotor(motor)) {break;}
            }
        }
    }

    private void addDeviceToMap(Device device) {
        device.getAllMotors().forEach(this::addMotorToMap);
    }

    private void addMotorToMap(Motor motor) {
        if(deviceGroupMap.containsKey(motor.groupID)) {
            deviceGroupMap.get(motor.groupID).addMotor(motor);
        }
        else {
            deviceGroupMap.put(motor.groupID, new DeviceGroup(motor));
        }
    }

    public static class DeviceGroup {

        final int groupID;
        boolean     empty;
        Motor       leadingMotor;
        List<Motor> followerList = new ArrayList<>();

        public DeviceGroup(Motor leadingMotor) {
            this(leadingMotor.groupID, leadingMotor);
        }

        public DeviceGroup(int groupID, Motor leadingMotor) {
            this(groupID, leadingMotor, new ArrayList<>());
        }

        public DeviceGroup(int groupID, Motor leadingMotor, List<Motor> followerList) {
            this.groupID = groupID;
            this.leadingMotor = leadingMotor;
            this.followerList.addAll(followerList);
            this.leadingMotor.groupLeader = true;
            empty = false;
        }

        public void addMotor(Motor motor) {
            if(empty) {
                leadingMotor = motor;
                motor.groupLeader = true;
                empty = false;
            }
            else {
                followerList.add(motor);
                motor.groupLeader = false;
            }
        }

        public boolean removeMotor(Motor motor) {
            if(empty) {return false;}
            if(!Objects.equals(leadingMotor, motor)) {
                return followerList.remove(motor);
            }
            else if(!followerList.isEmpty()) {
                leadingMotor = followerList.remove(0);
                leadingMotor.groupLeader = true;
                return true;
            }
            else {
                leadingMotor = null;
                empty = true;
                return true;
            }
        }

        public boolean contains(Motor motor) {
            return !empty || Objects.equals(leadingMotor, motor) || followerList.contains(motor);
        }

        public List<Motor> removeAllMotorsWithUnfitGroupID() {
            List<Motor> motorList = searchForUnfitGroupIDs();
            motorList.forEach(this::removeMotor);
            return motorList;
        }

        public void applyLeaderValueToFollowers() {
            if(empty) {return;}
            followerList.forEach(motor -> motor.setNextTarget(leadingMotor.getCurrentStep()));
        }

        private List<Motor> searchForUnfitGroupIDs() {
            List<Motor> motorList = new ArrayList<>();
            if(empty) {return motorList;}

            if(leadingMotor.groupID != groupID) {motorList.add(leadingMotor);}
            motorList.addAll(followerList.stream().filter(motor -> motor.groupID != groupID).toList());
            return motorList;
        }

    }

}
