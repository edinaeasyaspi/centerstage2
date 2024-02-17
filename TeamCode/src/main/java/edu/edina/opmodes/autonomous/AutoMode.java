package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.edina.library.util.DriveStatus;
import edu.edina.library.util.GrabberSide;
import edu.edina.library.util.PiBot;
import edu.edina.library.util.Point;
import edu.edina.library.util.Position;
import edu.edina.library.util.Positioning;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.drivecontrol.DriveDirection;
import edu.edina.opmodes.teleop.test.PropDetectingVisionProcessor;

public abstract class AutoMode extends LinearOpMode {
    protected static final boolean testMode = false;
    private static final double parkX = 10;
    private final boolean invert;
    protected PiBot piBot;

    protected RobotHardware hw;

    protected SelectedSpike position;

    private final Position startingPos;

    private int liftDir;

    protected AutoMode(boolean invert, Position startingPos) {
        this.invert = invert;
        this.startingPos = startingPos;
    }

    @Override
    public void runOpMode() {
        hw = new RobotHardware(hardwareMap, telemetry);
        piBot = new PiBot(hw);

        while (opModeInInit()) {
            piBot.setup(true, false, true);

            PropDetectingVisionProcessor.Selected s = piBot.getSelection();
            if (s == PropDetectingVisionProcessor.Selected.LEFT) {
                position = invert ? SelectedSpike.AUDIENCE : SelectedSpike.BACKSTAGE;
            } else if (s == PropDetectingVisionProcessor.Selected.MIDDLE) {
                position = SelectedSpike.MIDDLE;
            } else if (s == PropDetectingVisionProcessor.Selected.RIGHT) {
                position = invert ? SelectedSpike.BACKSTAGE : SelectedSpike.AUDIENCE;
            }

            telemetry.addData("selected", position);
            telemetry.update();
        }

        Positioning posn = piBot.getPositioning();
        posn.setCurrPos(startingPos);

        waitForStart();

        piBot.grab(GrabberSide.Both);
        runMainPath();
        dropPixelOnBackboard();
        driveToClosestPoint(parkX, 120, DriveDirection.Lateral);
    }

    protected abstract void runMainPath();

    private int backboardX() {
        if (position == SelectedSpike.AUDIENCE) return 40;
        else if (position == SelectedSpike.MIDDLE) return 36;
        else return 32;
    }

    protected void dropPixelOnBackboard() {
        Positioning posn = piBot.getPositioning();

        double x = backboardX();
        rotateToHeading(180);
        driveToClosestPoint(x, 115, DriveDirection.Lateral);

        Position prePos = posn.getCurrPos();
        telemetry.addData("prior position", prePos);

        ElapsedTime t = new ElapsedTime();
        while (opModeIsActive()) {
            posn.readHeading(true);
            Position p = posn.readAprilTagPosition(true);

            if (p != null) {
                telemetry.addData("detected april tag", p);
                break;
            } else if (t.seconds() > 0.5) {
                telemetry.addData("detected april tag", "");
                break;
            }
        }

        pauseOnTest();

        driveToClosestPoint(x, 115, DriveDirection.Lateral);

        piBot.positionGrabber(PiBot.GrabberPosition.Backboard);

        t = new ElapsedTime();
        while (opModeIsActive()) {
            piBot.runGrabber();
            if (t.milliseconds() > 300) {
                break;
            }
        }

        liftDir = 1;
        while (opModeIsActive()) {
            piBot.runGrabber();
            int liftPos = runLift();
            if (liftPos > 1000) break;
        }

        liftDir = 0;

        driveToClosestPoint(x, 121, DriveDirection.Axial);

        piBot.drop(GrabberSide.Both);
        sleep(400);

        piBot.positionGrabber(PiBot.GrabberPosition.Ground);

        liftDir = -1;
        while (opModeIsActive()) {
            piBot.runGrabber();
            int liftPos = runLift();
            if (liftPos < 2) break;
        }
    }

    private int runLift() {
        int liftPos = piBot.runLift(liftDir);
        return liftPos;
    }

    protected void driveToClosestPoint(double x, double y, DriveDirection driveDirection) {
        if (invert) x = 144 - x;

        piBot.planDriveToClosestPoint(new Point(x, y), driveDirection);
        while (opModeIsActive()) {
            runLift();
            if (piBot.runDrive() == DriveStatus.Done) break;
            telemetry.update();
        }

        pauseOnTest();
    }

    protected void rotateToHeading(double targetHeading) {
        if (invert) targetHeading = -targetHeading;

        piBot.planRotateToHeading(targetHeading);
        while (opModeIsActive()) {
            if (piBot.runRotate() == DriveStatus.Done) break;
            telemetry.update();
        }

        pauseOnTest();
    }

    protected void rotateAndDriveToPoint(double x, double y, DriveDirection direction) {
        rotateToPoint(x, y);
        driveToClosestPoint(x, y, DriveDirection.Axial);
    }

    protected void rotateToPoint(double x, double y) {
        if (invert) x = 144 - x;

        piBot.planRotateToPoint(new Point(x, y));
        while (opModeIsActive()) {
            if ((piBot.runRotate() == DriveStatus.Done)) break;
            telemetry.update();
        }

        pauseOnTest();
    }

    private void pauseOnTest() {
        double delay;
        if (testMode) delay = 3;
        else delay = 0.0;

        ElapsedTime time = new ElapsedTime();

        while (opModeIsActive() && time.seconds() < delay) {
            telemetry.addData("gyro heading", "%.1f", piBot.getPositioning().readHeading(false));
            telemetry.addData("pos", piBot.getPositioning().getCurrPos());
            telemetry.update();
        }
    }

    protected enum SelectedSpike {
        AUDIENCE, MIDDLE, BACKSTAGE
    }
}
