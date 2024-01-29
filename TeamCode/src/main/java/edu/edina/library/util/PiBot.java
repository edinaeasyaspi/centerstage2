package edu.edina.library.util;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import edu.edina.library.util.drivecontrol.DriveDirection;
import edu.edina.library.util.drivecontrol.PiDrive;

public class PiBot {
    private final RobotHardware hw;
    private final Positioning posn;

    //rotation
    private double targetHeading;
    private DcMotor[] motors;
    private DcMotor.RunMode preRotateRunMode;

    //Driving
    private Position stoppingPoint;
    private PiDrive drive;

    public PiBot(RobotHardware hw) {
        this.hw = hw;
        this.posn = new Positioning(hw);
        motors = new DcMotor[]{hw.frontLeftMotor, hw.backLeftMotor, hw.frontRightMotor, hw.backRightMotor};
        drive = new PiDrive(hw, posn);
    }

    public Positioning getPositioning() {
        return posn;
    }

    public void planDriveToClosestPoint(Point target, DriveDirection direction) {
        double tgtDist = 0;

        Position currentPos = posn.getCurrPos();

        if (direction == DriveDirection.Lateral) {
            tgtDist = currentPos.toRobotRel(target).x;

            stoppingPoint = currentPos.addRobotRel(new Point(tgtDist, 0));
        } else if (direction == DriveDirection.Axial) {
            tgtDist = currentPos.toRobotRel(target).y;

            stoppingPoint = currentPos.addRobotRel(new Point(0, tgtDist));
        }

        if (Math.abs(tgtDist) < 0.1) return;

        drive.preRun(tgtDist, direction);
        if (hw.telemetry != null) {
            hw.telemetry.addData("tgtDist", "%.1fin", tgtDist);
            hw.telemetry.update();
        }
    }

    public DriveStatus runDrive() {
        if (drive.run()) {
            posn.setCurrPos(stoppingPoint);
            return DriveStatus.Done;
        } else {
            return DriveStatus.Driving;
        }
    }

    public void planRotate(double targetHeading) {
        preRotateRunMode = hw.frontLeftMotor.getMode();
        this.targetHeading = targetHeading;
    }

    public void planRotateToPoint(Point p) {
        Position c = posn.getCurrPos();

        double x = p.x - c.x;
        double y = p.y - c.y;

        double angle = Math.atan2(-x, y);

        planRotate(Math.toDegrees(angle));
    }

    int xz;

    public DriveStatus rotateToHeading() {
        double ppd = 537.0 / 63.15;

        double targetAngle = targetHeading - posn.readImuHeading(true);

        while (targetAngle < -180) {
            targetAngle = targetAngle + 360;
        }

        while (targetAngle > 180) {
            targetAngle = targetAngle - 360;
        }

        if (hw.telemetry != null) {
            hw.telemetry.addData("heading", "%.1f", posn.readImuHeading(false));
            hw.telemetry.addData("target", "%.1f", targetAngle);
            hw.telemetry.addData("xz", xz++);
            hw.telemetry.update();
        }

        if (Math.abs(targetAngle) > 1) {
            int targetPos = (int) (targetAngle * ppd);
            double power = 0.5;

            for (DcMotor m : motors) {
                int p = m.getCurrentPosition();
                if (m.getDirection() == DcMotorSimple.Direction.REVERSE) {
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
                m.setPower(0);
                m.setMode(preRotateRunMode);
            }

            return DriveStatus.Done;
        } else {
            return DriveStatus.Driving;
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
