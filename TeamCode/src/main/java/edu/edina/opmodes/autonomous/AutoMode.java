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
    private final boolean invert;
    protected PiBot piBot;

    protected RobotHardware hw;

    protected SelectedSpike position;

    private final Position startingPos;

    protected AutoMode(boolean invert, Position startingPos) {
        this.invert = invert;
        this.startingPos = startingPos;
    }

    @Override
    public void runOpMode() {
        hw = new RobotHardware(hardwareMap, telemetry);
        piBot = new PiBot(hw);

        while (opModeInInit()) {
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
        // todo place on backboard
    }

    protected abstract void runMainPath();

    protected void driveToClosestPoint(double x, double y, DriveDirection driveDirection) {
        if (invert) x = 144 - x;

      //   double initHeading = piBot.getPositioning().readHeading(true);

        piBot.planDriveToClosestPoint(new Point(x, y), driveDirection);
        while (opModeIsActive()) {
            if (piBot.runDrive() == DriveStatus.Done) break;
            telemetry.update();
        }

        pauseOnTest();

//       rotateToHeading(initHeading);
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
        if (testMode)
            delay = 3;
        else
            delay = 0.1;

        ElapsedTime time = new ElapsedTime();

        while (opModeIsActive() && time.seconds() < delay) {
            telemetry.addData("gyro heading", piBot.getPositioning().readHeading(false));
            telemetry.addData("pos", piBot.getPositioning().getCurrPos());
            telemetry.update();
        }
    }

    protected enum SelectedSpike {
        AUDIENCE, MIDDLE, BACKSTAGE
    }
}
