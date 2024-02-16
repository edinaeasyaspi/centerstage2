package edu.edina.library.util;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;

import android.util.Size;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.vision.VisionPortal;

import edu.edina.library.util.drivecontrol.DriveDirection;
import edu.edina.library.util.drivecontrol.LinearFunc;
import edu.edina.library.util.drivecontrol.PiDrive;
import edu.edina.library.util.drivecontrol.ServoThrottle;
import edu.edina.library.util.projection.Vec;
import edu.edina.opmodes.teleop.test.PixelDetectProcessor;
import edu.edina.opmodes.teleop.test.PropDetectingVisionProcessor;

public class PiBot {
    //main fields
    private final RobotHardware hw;

    //vision
    private final Positioning posn;
    private final PropDetectingVisionProcessor propDetImageProc;
    private final PixelDetectProcessor pixDetProc;
    private final VisionPortal frontVisionPortal, rearVisionPortal;
    private static final double PIXEL_DET_SCALE = 0.5;
    private boolean isVisionPortalSetup;

    //rotation
    private double targetHeading;
    private DcMotor[] motors;
    private DcMotor.RunMode preRotateRunMode;
    private boolean firstRotate;

    //Driving
    private Position stoppingPoint;
    public final PiDrive drive;
    private double predriveHeading;
    private ServoThrottle armSwingRight, armSwingLeft;
    private DcMotor.ZeroPowerBehavior preRotateZeroPowerMode;

    //Lift Motor
    private int noLift, liftPosition, liftLimit;

    public PiBot(RobotHardware hw) {
        this.hw = hw;
        this.posn = new Positioning(hw, true);
        motors = new DcMotor[]{hw.frontLeftMotor, hw.backLeftMotor, hw.frontRightMotor, hw.backRightMotor};
        armSwingRight = new ServoThrottle(hw.intakeSwingRight, 0.7, 0);
        armSwingLeft = new ServoThrottle(hw.intakeSwingLeft, 0.7, 1);
        drive = new PiDrive(hw, posn);

        propDetImageProc = new PropDetectingVisionProcessor();
        pixDetProc = new PixelDetectProcessor(
                new Vec[]{
                        new Vec(-0.1677, -.4518, 2.8624),
                        new Vec(.8298, -.4965, 2.8070),
                        new Vec(.8922, -.3197, 3.7893),
                        new Vec(-.1053, -.2750, 3.8446),
                },
                -6,
                -32);

        int[] viewportIds = VisionPortal.makeMultiPortalView(2, VisionPortal.MultiPortalLayout.HORIZONTAL);

        VisionPortal.Builder visionPortalBuilder = new VisionPortal.Builder();
        frontVisionPortal = visionPortalBuilder
                .enableLiveView(true)
                .addProcessor(propDetImageProc)
                .addProcessor(pixDetProc)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCamera(hw.frontWebcam)
                .setCameraResolution(new Size(640, 480))
                .setLiveViewContainerId(viewportIds[0])
                .build();

        rearVisionPortal = visionPortalBuilder
                .enableLiveView(true)
                .addProcessor(posn.getMyAprilTagProc())
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCamera(hw.rearWebcam)
                .setCameraResolution(new Size(640, 480))
                .setLiveViewContainerId(viewportIds[1])
                .build();
    }

    public PropDetectingVisionProcessor.Selected getSelection() {
        PropDetectingVisionProcessor.Selected position = propDetImageProc.getSelection();

        if (hw.telemetry != null) {
            hw.telemetry.addData("Selected value", position);
            hw.telemetry.addData("diag", propDetImageProc.getDiagString());
        }

        return position;
    }

    public void setup(boolean enablePropDet, boolean enablePixDet, boolean enableAprilTags) {
        if (frontVisionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            if (!isVisionPortalSetup) {
                WhiteBalanceControl wb = frontVisionPortal.getCameraControl(WhiteBalanceControl.class);
                wb.setMode(WhiteBalanceControl.Mode.MANUAL);
                wb.setWhiteBalanceTemperature(3000);
                isVisionPortalSetup = true;
            }
        }

        frontVisionPortal.setProcessorEnabled(propDetImageProc, enablePropDet);
        frontVisionPortal.setProcessorEnabled(pixDetProc, enablePixDet);
        rearVisionPortal.setProcessorEnabled(getPositioning().getMyAprilTagProc(), enableAprilTags);

        noLift = hw.liftMotor.getCurrentPosition();
        liftPosition = noLift;
        liftLimit = 2500;
    }

    public PixelDetect.Result detectPixel() {
        if (hw.telemetry != null) {
            hw.telemetry.addData("diag", pixDetProc.getDiagString());
        }

        PixelDetect.Result r = pixDetProc.getLastResult();

        if (r == null) return null;
        if (r.count < 40) return null;

        r = new PixelDetect.Result(
                r.x0 * PIXEL_DET_SCALE,
                r.y0 * PIXEL_DET_SCALE,
                r.angleDeg,
                r.s * PIXEL_DET_SCALE,
                r.d * PIXEL_DET_SCALE,
                new LinearFunc(r.f.beta, r.f.alpha * PIXEL_DET_SCALE, r.f.R2),
                r.count
        );

        if (hw.telemetry != null) {
            hw.telemetry.addData("detect", "angleDeg=%.1f strafe=%.2f", r.angleDeg, r.s);
        }

        return r;
    }

    public Positioning getPositioning() {
        return posn;
    }

    public void planRelativeDrive(double dist, DriveDirection direction) {
        Point rel;
        if (direction == DriveDirection.Axial)
            rel = new Point(0, dist);
        else
            rel = new Point(dist, 0);

        posn.readHeading(true);

        Point tgt = getPositioning().getCurrPos().addRobotRel(rel).toPoint();
        planDriveToClosestPoint(tgt, direction);
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

    public void planRelativeRotate(double angle) {
        double h = posn.readHeading(true);
        planRotateToHeading(h + angle);
    }

    public void planRotateToPoint(Point p) {
        Position c = posn.getCurrPos();

        double x = p.x - c.x;
        double y = p.y - c.y;

        double angle = Math.atan2(-x, y);

        planRotateToHeading(Math.toDegrees(angle));
    }

    public void planRotateToHeading(double targetHeading) {
        this.targetHeading = targetHeading;
        preRotateRunMode = hw.frontLeftMotor.getMode();
        preRotateZeroPowerMode = hw.frontLeftMotor.getZeroPowerBehavior();
        firstRotate = true;
    }

    public DriveStatus runRotate() {
        double ppd = 537.0 / 63.15;

        double targetAngle = targetHeading - posn.readHeading(true);

        while (targetAngle < -180) {
            targetAngle = targetAngle + 360;
        }

        while (targetAngle > 180) {
            targetAngle = targetAngle - 360;
        }

        if (firstRotate && targetAngle == 0)
            return doneWithRotate();

        if (hw.telemetry != null) {
            hw.telemetry.addData("heading", "%.1f", posn.readHeading(false));
            hw.telemetry.addData("target", "%.1f", targetAngle);
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
            return doneWithRotate();
        } else {
            return DriveStatus.Driving;
        }
    }

    private DriveStatus doneWithRotate() {
        for (DcMotor m : motors) {
            m.setPower(0);
            m.setMode(preRotateRunMode);
            m.setZeroPowerBehavior(preRotateZeroPowerMode);
        }

        return DriveStatus.Done;
    }

    public void setZeroPowerBrake() {
        for (DcMotor m : motors) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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

    public enum GrabberPosition {
        Ground, Backboard
    }

    public void positionGrabber(GrabberPosition p) {
        if (p == GrabberPosition.Backboard) {
            positionGrabber(0.9, false);
        } else if (p == GrabberPosition.Ground) {
            positionGrabber(0, true);
        }
    }

    public void positionGrabber(double targetPosition, boolean fullClose) {
        armSwingRight.setTargetPos(targetPosition);
        armSwingLeft.setTargetPos(1 - targetPosition);

        if (fullClose) {
            hw.intakeLeft.setPosition(0);
            hw.intakeRight.setPosition(0.6);
        }
    }

    public void runGrabber() {
        armSwingLeft.run();
        armSwingRight.run();
    }

    public boolean grabberInPosition() {
        throw new RuntimeException();
    }

    public void grab(GrabberSide grabberSide) {
        if (grabberSide == GrabberSide.Right)
            hw.intakeRight.setPosition(0.5);
        if (grabberSide == GrabberSide.Left)
            hw.intakeLeft.setPosition(0.1);
        if (grabberSide == GrabberSide.Both) {
            hw.intakeLeft.setPosition(0.1);
            hw.intakeRight.setPosition(0.5);
        }
    }

    public void drop(GrabberSide grabberSide) {
        if (grabberSide == GrabberSide.Right)
            hw.intakeRight.setPosition(0.42);
        if (grabberSide == GrabberSide.Left)
            hw.intakeLeft.setPosition(0.15);
        if (grabberSide == GrabberSide.Both) {
            hw.intakeLeft.setPosition(0.15);
            hw.intakeRight.setPosition(0.42);
        }
    }

    public void positionHooks(Hanging hanging) {
        if (hanging == Hanging.Extend) ;
        hw.hangLeft.setPosition(Servo.MIN_POSITION + 0.05);
        hw.hangRight.setPosition(Servo.MAX_POSITION - 0.05);
        hw.hangLiftLeft.setPosition(0.8);
        hw.hangLiftRight.setPosition(0.2);
    }
    //merry summer

    public int runLift(int dir) {
        int currPos = hw.liftMotor.getCurrentPosition();
        if (dir > 0 && currPos < noLift + liftLimit) {
            hw.liftMotor.setPower(0.7);
            liftPosition = currPos;
        } else if (dir < 0 && currPos > noLift) {
            hw.liftMotor.setPower(-0.6);
            liftPosition = currPos;
        } else {
            double y = currPos - liftPosition;
            hw.liftMotor.setPower(-y / 100);
        }

        return currPos - noLift;
    }

    public void retractHooks(Hanging hanging) {
        if (hanging == Hanging.Retract) ;
        hw.hangLeft.setPosition(Servo.MIN_POSITION + 0.05);
        hw.hangRight.setPosition(Servo.MAX_POSITION - 0.05);
        hw.hangLiftLeft.setPosition(0.2);
        hw.hangLiftRight.setPosition(0.8);
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


    public void launch(DroneLauncher droneLauncher) {
        if (droneLauncher == DroneLauncher.Idle)
            hw.droneLauncher.setPower(0);
        if (droneLauncher == DroneLauncher.Expel) {
            hw.droneLauncher.setPower(-1);
        }
    }
}