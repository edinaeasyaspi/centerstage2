package edu.edina.opmodes.autonomous;

import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.edina.library.util.DriveStatus;
import edu.edina.library.util.GrabberSide;
import edu.edina.library.util.PiBot;
import edu.edina.library.util.Point;
import edu.edina.library.util.Position;
import edu.edina.library.util.Positioning;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.drivecontrol.DriveDirection;

public abstract class AutoMode extends LinearOpMode {
    protected static final boolean testMode = true;
    private final boolean invert;
    protected PiBot piBot;

    protected Selected position;
    private final Position startingPos;

    protected AutoMode(boolean invert, Position startingPos) {
        this.invert = invert;
        this.startingPos = startingPos;
    }

    public void runOpMode() {
        RobotHardware hw = new RobotHardware(hardwareMap);
        piBot = new PiBot(hw);

        while (opModeInInit()) {
            position = piBot.getSelection();
        }

        Positioning posn = piBot.getPositioning();
        posn.setCurrPos(startingPos);

        waitForStart();

        while (opModeIsActive()) {
            piBot.grab(GrabberSide.Both);
            runMainPath();
            // todo place on backboard
        }
    }

    protected abstract void runMainPath();

    protected void driveToClosestPoint(double x, double y, DriveDirection driveDirection) {
        if (invert)
            x = 144 - x;

        piBot.planDriveToClosestPoint(new Point(x, y), driveDirection);
        while (opModeIsActive()) {
            if (piBot.runDrive() == DriveStatus.Done) break;
        }

        pauseOnTest();
    }

    protected void rotateToHeading(double targetHeading) {
        if (invert)
            targetHeading = -targetHeading;

        piBot.planRotateToHeading(targetHeading);
        while (opModeIsActive()) {
            if (piBot.runRotate() == DriveStatus.Done) break;
        }

        pauseOnTest();
    }

    protected void rotateToPoint(double x, double y) {
        if (invert)
            x = 144 - x;

        piBot.planRotateToPoint(new Point(x, y));
        while (opModeIsActive()) {
            if ((piBot.runRotate() == DriveStatus.Done)) break;
        }

        pauseOnTest();
    }

    private void pauseOnTest() {
        if (opModeIsActive() && testMode)
            sleep(1000);
    }
}
