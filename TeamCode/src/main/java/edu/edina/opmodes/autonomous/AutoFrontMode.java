package edu.edina.opmodes.autonomous;

import static edu.edina.library.util.drivecontrol.DriveDirection.Axial;
import static edu.edina.library.util.drivecontrol.DriveDirection.Lateral;
import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected.LEFT;
import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected.MIDDLE;
import static edu.edina.opmodes.teleop.test.ImageProcessor.Selected.RIGHT;

import edu.edina.library.util.GrabberSide;
import edu.edina.library.util.Position;

public abstract class AutoFrontMode extends AutoMode {
    protected AutoFrontMode(boolean invert, Position startingPos) {
        super(invert, startingPos);
    }

    @Override
    protected void runMainPath() {
        if (position == LEFT) {
            driveToClosestPoint(24, 36, Axial);
            driveToClosestPoint(24, 26, Lateral);
            piBot.drop(GrabberSide.Right);
            driveToClosestPoint(20, 26, Axial);
            //close to pick up white pixel
        } else if (position == MIDDLE) {
            driveToClosestPoint(33, 36, Axial);
            piBot.drop(GrabberSide.Right);
            driveToClosestPoint(24, 36, Axial);
        } else if (position == RIGHT) {
            driveToClosestPoint(24, 36, Axial);
            driveToClosestPoint(24, 42, Lateral);
            piBot.drop(GrabberSide.Right);
            driveToClosestPoint(20, 42, Axial);
            //close to pick up white pixel
        }

        rotateToPoint(24, 18);
        driveToClosestPoint(24, 18, Axial);
        rotateToHeading(180);
        driveToClosestPoint(36, 18, Lateral);
        driveToClosestPoint(36, 15, Axial);
        rotateToPoint(58, 21);
        driveToClosestPoint(58, 21, Lateral);
        rotateToPoint(66, 50);
        driveToClosestPoint(66, 50, Lateral);
        rotateToHeading(0);
        driveToClosestPoint(66, 110, Lateral);
        rotateToHeading(180);
        driveToClosestPoint(42, 110, Axial);
        //drop pixel
        driveToClosestPoint(10, 110, Axial);
    }
}
