package edu.edina.library.util;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.util.Size;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

import edu.edina.library.util.drivecontrol.DriveDirection;
import edu.edina.library.util.drivecontrol.PiDrive;
import edu.edina.opmodes.teleop.test.ImageProcessor;

public class PiBot {
    private final RobotHardware hw;
    private final Positioning posn;
    private final ImageProcessor imageProcessor;

    //rotation
    private double targetHeading;
    private DcMotor[] motors;
    private DcMotor.RunMode preRotateRunMode;

    //Driving
    private Position stoppingPoint;
    public final PiDrive drive;
    private double predriveHeading;

    public PiBot(RobotHardware hw) {
        this.hw = hw;
        this.posn = new Positioning(hw);
        motors = new DcMotor[]{hw.frontLeftMotor, hw.backLeftMotor, hw.frontRightMotor, hw.backRightMotor};
        drive = new PiDrive(hw, posn);

        imageProcessor = new ImageProcessor(telemetry);

        VisionPortal.Builder visionPortalBuilder = new VisionPortal.Builder();
        visionPortalBuilder.enableLiveView(true)
                .addProcessor(imageProcessor)
                .addProcessor(posn.getMyAprilTagProc())
                .setCamera(hw.webcam)
                .setCameraResolution(new Size(640, 480))
                .build();
    }

    public ImageProcessor.Selected getSelection() {
        ImageProcessor.Selected position = imageProcessor.getSelection();

        if (hw.telemetry != null) {
            telemetry.addData("Selected value", position);
            telemetry.addData("diag", imageProcessor.getDiagString());
        }

        return position;
    }

    public Positioning getPositioning() {
        return posn;
    }

    public void planDriveToClosestPoint(Point target, DriveDirection direction) {
        double tgtDist = 0;

        this.predriveHeading = posn.readHeading(true);

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
            posn.readHeading(true);
            posn.setCurrPos(stoppingPoint);
            return DriveStatus.Done;
        } else {
            return DriveStatus.Driving;
        }
    }

    public void planRotateToHeading(double targetHeading) {
        preRotateRunMode = hw.frontLeftMotor.getMode();
        this.targetHeading = targetHeading;
    }

    public void planRotateToPoint(Point p) {
        Position c = posn.getCurrPos();

        double x = p.x - c.x;
        double y = p.y - c.y;

        double angle = Math.atan2(-x, y);

        planRotateToHeading(Math.toDegrees(angle));
    }

    int xz;

    public DriveStatus runRotate() {
        double ppd = 537.0 / 63.15;

        double targetAngle = targetHeading - posn.readHeading(true);

        while (targetAngle < -180) {
            targetAngle = targetAngle + 360;
        }

        while (targetAngle > 180) {
            targetAngle = targetAngle - 360;
        }

        if (hw.telemetry != null) {
            hw.telemetry.addData("heading", "%.1f", posn.readHeading(false));
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

    public void grab(GrabberSide grabberSide) {
        if (grabberSide == GrabberSide.Right)
            hw.intakeRight.setPosition(0.4);
        if (grabberSide == GrabberSide.Left)
            hw.intakeLeft.setPosition(0.6);
        if (grabberSide == GrabberSide.Both) {
            hw.intakeLeft.setPosition(0.6);
            hw.intakeRight.setPosition(0.4);
        }
    }

    public void drop(GrabberSide grabberSide) {
        if (grabberSide == GrabberSide.Right)
            hw.intakeRight.setPosition(0.6);
        if (grabberSide == GrabberSide.Left)
            hw.intakeLeft.setPosition(0.4);
        if (grabberSide == GrabberSide.Both) {
            hw.intakeLeft.setPosition(0.4);
            hw.intakeRight.setPosition(0.6);
        }
    }

    public void positionHooks() {
        hw.hangRight.setPosition(Servo.MAX_POSITION);
        hw.hangLeft.setPosition(Servo.MIN_POSITION);
        hw.hangLiftLeft.setPosition(0.8);
        hw.hangLiftRight.setPosition(0.2);
    }
    //merry summer


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
