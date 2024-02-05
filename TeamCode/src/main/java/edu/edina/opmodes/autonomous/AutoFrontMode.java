package edu.edina.opmodes.autonomous;

import static edu.edina.library.util.drivecontrol.DriveDirection.Axial;
import static edu.edina.library.util.drivecontrol.DriveDirection.Lateral;
import static edu.edina.opmodes.teleop.test.PropDetectingVisionProcessor.Selected.LEFT;
import static edu.edina.opmodes.teleop.test.PropDetectingVisionProcessor.Selected.MIDDLE;
import static edu.edina.opmodes.teleop.test.PropDetectingVisionProcessor.Selected.RIGHT;

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
            driveToClosestPoint(24, 46, Lateral);
            piBot.drop(GrabberSide.Left);
            driveToClosestPoint(16, 46, Axial);
        } else if (position == MIDDLE) {
            driveToClosestPoint(33, 36, Axial);
            piBot.drop(GrabberSide.Right);
            driveToClosestPoint(16, 36, Axial);
        } else if (position == RIGHT) {
            driveToClosestPoint(24, 36, Axial);
            driveToClosestPoint(24, 30, Lateral);
            piBot.drop(GrabberSide.Right);
            driveToClosestPoint(16, 30, Axial);
        } else {
            driveToClosestPoint(24, 36, Axial);
            driveToClosestPoint(24, 46, Lateral);
            piBot.drop(GrabberSide.Right);
            driveToClosestPoint(16, 46, Axial);
        }
//
//        rotateToPoint(24, 18);
//        driveToClosestPoint(24, 18, Axial);
//        rotateToHeading(180);
//        driveToClosestPoint(36, 18, Lateral);
//        driveToClosestPoint(36, 13, Axial);
//        piBot.grab(GrabberSide.Right);
//        rotateToPoint(58, 21);
//        driveToClosestPoint(58, 21, Axial);
//        rotateToPoint(66, 50);
//        driveToClosestPoint(66, 50, Axial);
//        rotateToHeading(0);
//        driveToClosestPoint(66, 110, Axial);
//        rotateToHeading(180);
//        driveToClosestPoint(42, 110, Lateral);
//        //drop pixel
//        driveToClosestPoint(10, 110, Lateral);
    }
}
