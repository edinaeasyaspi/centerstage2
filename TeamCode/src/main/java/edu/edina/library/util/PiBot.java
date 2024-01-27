package edu.edina.library.util;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import edu.edina.library.util.drivecontrol.DriveDirection;

public class PiBot {
    private final RobotHardware hw;
    private final Positioning posn;

    //rotation
    private double targetHeading;
    private DcMotor[] motors;
    private DcMotor.RunMode preRotateRunMode;

    public PiBot(RobotHardware hw) {
        this.hw = hw;
        this.posn = new Positioning(hw);
        motors = new DcMotor[]{hw.frontLeftMotor, hw.backLeftMotor, hw.frontRightMotor, hw.backRightMotor};
    }

    public void planDriveToClosestPoint(Point target, DriveDirection direction) {

    }

    public boolean runDrive() {
        throw new RuntimeException();
    }

    public void planRotate(double targetHeading) {
        preRotateRunMode = hw.frontLeftMotor.getMode();
        this.targetHeading = targetHeading;
    }

    public void planRotateToPoint(Point p) {
        Point c = posn.getCurrPos();

        double x = p.x - c.x;
        double y = p.y - c.y;

        double angle = Math.atan2(-x, y);

        planRotate(Math.toDegrees(angle));
    }

    public boolean rotateToHeading() {
        double ppd = 537.0 / 63.15;

        double targetAngle = targetHeading - posn.getHeading();

        if (targetAngle < -180) {
            targetAngle = targetAngle + 360;
        }

        if (targetAngle > 180) {
            targetAngle = targetAngle - 360;
        }

        if (Math.abs(targetAngle) > 2) {
            int targetPos = (int) (targetAngle * ppd);
            double power = 0.5;

            for (DcMotor m : motors) {
                int p = m.getCurrentPosition();
                if (m.getDirection() == DcMotorSimple.Direction.FORWARD) {
                    m.setTargetPosition(p - targetPos);
                } else {
                    m.setTargetPosition(p + targetPos);
                }

                m.setPower(power);
                m.setMode(RUN_TO_POSITION);
            }
        }

        if (areIdle()) {
            for (DcMotor m : motors) {
                m.setMode(preRotateRunMode);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean areIdle() {
        for (DcMotor m : motors) {
            if (m.isBusy()) {
                return false;
            }
        }
        return true;
    }

    public void positionGrabber(/*GrabberPosition*/) {
        //happy halloween
    }

    public boolean grabberInPosition() {
        throw new RuntimeException();
    }

    public void grab(/*GrabberSide*/) {
    }

    public void drop(/*GrabberSide*/) {
    }

    public void positionHooks() {
        //merry summer
    }

    public boolean hooksInPosition() {
        throw new RuntimeException();
    }

    public void planLatchOnBackboard() {
        //happy hanukkah
    }

    public boolean latchOn() {
        //happy MLKJ day
        throw new RuntimeException();
    }

    public void dropPixelOnBackground() {
        //enum LEFTMIDDLERIGHT
    }

    public void intakePixel() {
        //happy memorial day
    }

    public void launch() {
        //happy presidents day weekend
    }
}