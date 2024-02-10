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
            driveToClosestPoint(24, 36, Axial);
            rotateToHeading(-45);
            driveToClosestPoint(32, 39.5, Axial);
            piBot.drop(GrabberSide.Left);
            driveToClosestPoint(30, 24, Axial);
        }

        rotateToPoint(24, 18);
        driveToClosestPoint(24, 12, Axial);
        rotateToHeading(180);
        driveToClosestPoint(47, 12, Lateral);
        driveToClosestPoint(47, 6, Axial);
        driveToClosestPoint(47, 12, Axial);
        piBot.grab(GrabberSide.Right);
        hw.intakeSwingRight.setPosition(0.1);
        hw.intakeSwingLeft.setPosition(0.9);
        rotateToPoint(64, 48);
        driveToClosestPoint(64, 48, Axial);
        rotateToPoint(68, 100);
        driveToClosestPoint(68, 100, Axial);
        rotateToHeading(0);
        driveToClosestPoint(68, 120, Axial);
        rotateToHeading(180);
        driveToClosestPoint(36, 120, Lateral);

        //drop pixel
        driveToClosestPoint(10, 120, Lateral);
    }
}
