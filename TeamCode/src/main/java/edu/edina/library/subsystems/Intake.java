package edu.edina.library.subsystems;

import edu.edina.library.enums.IntakeState;
import edu.edina.library.enums.LiftState;
import edu.edina.library.enums.LiftTipState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Intake extends Subsystem {
    private Robot robot;

    public Intake(Robot robot) {
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
            switch (state.intakeState) {
                case Intake:
                    hardware.leftIntakeServo.setPower(1);
                 //   hardware.rightIntakeServo.setPosition(-1);
                    break;
                case Expel:
                    hardware.leftIntakeServo.setPower(-1);
                  //  hardware.rightIntakeServo.setPosition(1);
                    break;
                case Idle:
                    hardware.leftIntakeServo.setPower(0);
                //    hardware.rightIntakeServo.setPosition(0);
                    break;
            }
        }
    }

    public void setProperties(boolean leftBumper, boolean rightBumper) {
        RobotState state = RobotState.getInstance();

        if (leftBumper) {
            if (state.intakeState == IntakeState.Intake) {
                state.intakeState = IntakeState.Idle;
            } else {
                state.intakeState = IntakeState.Intake;
            }
        } else if (rightBumper) {
            if (state.intakeState == IntakeState.Expel) {
                state.intakeState = IntakeState.Idle;
            } else {
                state.intakeState = IntakeState.Expel;
            }
        }
    }
}
