package edu.edina.library.subsystems;

import edu.edina.library.enums.DroneLauncherState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class DroneLauncher extends Subsystem {
    private Robot robot;


    public DroneLauncher(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (robot.Started) {
            switch (state.droneLauncherState) {
                case Idle:
                    hardware.droneLauncherServo.setPower(0);
                    break;
                case Expel:
                    hardware.droneLauncherServo.setPower(-1);
                    break;
            }
        }
    }

    public void setProperties(boolean b, boolean a) {
        RobotState state = RobotState.getInstance();

        if (b && a) {
            if (state.droneLauncherState == DroneLauncherState.Expel) {
            } else {
                state.droneLauncherState = DroneLauncherState.Idle;
            }
        }
    }
}
