package edu.edina.opmodes.autonomous;

import static edu.edina.library.util.drivecontrol.DriveDirection.Axial;
import static edu.edina.library.util.drivecontrol.DriveDirection.Lateral;
import static edu.edina.opmodes.autonomous.AutoMode.SelectedSpike.BACKSTAGE;
import static edu.edina.opmodes.autonomous.AutoMode.SelectedSpike.MIDDLE;
import static edu.edina.opmodes.autonomous.AutoMode.SelectedSpike.AUDIENCE;

import edu.edina.library.util.GrabberSide;
import edu.edina.library.util.Position;

public abstract class AutoFrontMode extends AutoMode {
    protected AutoFrontMode(boolean invert, Position startingPos) {
        super(invert, startingPos);
    }

    @Override
    protected void runMainPath() {
        piBot.grab(GrabberSide.Both);
        if (position == BACKSTAGE) {
            driveToClosestPoint(31, 36, Axial);
            rotateToHeading(-45);
            driveToClosestPoint(37, 44.5, Axial);
            piBot.drop(GrabberSide.Left);
            piBot.grab(GrabberSide.Right);
            driveToClosestPoint(31, 36, Axial);
        } else if (position == MIDDLE) {
            driveToClosestPoint(40, 36, Axial);
            piBot.drop(GrabberSide.Left);
            piBot.grab(GrabberSide.Right);
            driveToClosestPoint(23, 36, Axial);
        } else if (position == AUDIENCE) {
            driveToClosestPoint(25, 36, Axial);
            rotateToHeading(-135);
            driveToClosestPoint(38, 38.5, Axial);
            piBot.drop(GrabberSide.Left);
            piBot.grab(GrabberSide.Right);
            driveToClosestPoint(25, 36, Axial);
            driveToClosestPoint(15, 33, Lateral);
        } else {
            driveToClosestPoint(31, 36, Axial);
            rotateToHeading(-45);
            driveToClosestPoint(37, 44.5, Axial);
            piBot.drop(GrabberSide.Left);
            piBot.grab(GrabberSide.Right);
        }

        rotateAndDriveToPoint(24, 18, Axial);
        rotateToHeading(180);
        driveToClosestPoint(47, 20, Lateral);
        rotateAndDriveToPoint(64, 48, Axial);
        rotateAndDriveToPoint(56, 100, Axial);
        rotateToHeading(0);
        driveToClosestPoint(68, 120, Axial);
        rotateToHeading(180);
        driveToClosestPoint(36, 120, Lateral);
        //drop pixel
        driveToClosestPoint(10, 120, Lateral);
    }
}
