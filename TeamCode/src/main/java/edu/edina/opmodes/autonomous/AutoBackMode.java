package edu.edina.opmodes.autonomous;

import static edu.edina.library.util.drivecontrol.DriveDirection.Axial;
import static edu.edina.library.util.drivecontrol.DriveDirection.Lateral;
import static edu.edina.opmodes.autonomous.AutoMode.SelectedSpike.AUDIENCE;
import static edu.edina.opmodes.autonomous.AutoMode.SelectedSpike.BACKSTAGE;
import static edu.edina.opmodes.autonomous.AutoMode.SelectedSpike.MIDDLE;

import edu.edina.library.util.GrabberSide;
import edu.edina.library.util.Position;

public abstract class AutoBackMode extends AutoMode {
    private static final int delayMilliseconds = 0;

    protected AutoBackMode(boolean invert, Position startingPos) {
        super(invert, startingPos);
    }

    @Override
    protected void runMainPath() {
        if (position == BACKSTAGE) {
            driveToClosestPoint(28, 84, Axial);
            rotateToHeading(-45);
            driveToClosestPoint(32, 87.5, Axial);
            piBot.drop(GrabberSide.Left);
            sleep(100);
            driveToClosestPoint(24, 84, Axial);
            this.position = BACKSTAGE;
        } else if (position == MIDDLE) {
            driveToClosestPoint(36, 84, Axial);
            driveToClosestPoint(39, 84, Axial);
            piBot.drop(GrabberSide.Left);
            sleep(100);
            driveToClosestPoint(16, 84, Axial);
            this.position = MIDDLE;
        } else if (position == AUDIENCE) {
            driveToClosestPoint(27, 84, Axial);
            rotateToHeading(-135);
            driveToClosestPoint(33, 78, Axial);
            piBot.drop(GrabberSide.Left);
            sleep(100);
            driveToClosestPoint(27, 84, Axial);
            driveToClosestPoint(21, 90, Lateral);
            this.position = AUDIENCE;
        } else {
            driveToClosestPoint(24, 84, Axial);
            rotateToHeading(-45);
            driveToClosestPoint(32, 87.5, Axial);
            piBot.drop(GrabberSide.Left);
            sleep(100);
            driveToClosestPoint(24, 84, Axial);
            this.position = BACKSTAGE;
        }

        rotateAndDriveToPoint(20, 118, Axial);

        sleep(delayMilliseconds);
    }
}
