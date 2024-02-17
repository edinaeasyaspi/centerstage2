package edu.edina.opmodes.autonomous;

import static edu.edina.library.util.drivecontrol.DriveDirection.Axial;
import static edu.edina.library.util.drivecontrol.DriveDirection.Lateral;
import static edu.edina.opmodes.autonomous.AutoMode.SelectedSpike.BACKSTAGE;
import static edu.edina.opmodes.autonomous.AutoMode.SelectedSpike.MIDDLE;
import static edu.edina.opmodes.autonomous.AutoMode.SelectedSpike.AUDIENCE;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.edina.library.util.GrabberSide;
import edu.edina.library.util.Position;

public abstract class AutoFrontMode extends AutoMode {
    private static final int delayMilliseconds = 0;

    protected AutoFrontMode(boolean invert, Position startingPos) {
        super(invert, startingPos);
    }

    @Override
    protected void runMainPath() {
        hw.liftMotor.setTargetPosition(hw.liftMotor.getCurrentPosition());
        hw.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        piBot.grab(GrabberSide.Both);
        if (position == BACKSTAGE) {
            driveToClosestPoint(31, 36, Axial);
            rotateToHeading(-45);
            driveToClosestPoint(36, 43.5, Axial);
            piBot.drop(GrabberSide.Left);
            sleep(100);
            piBot.grab(GrabberSide.Right);
            driveToClosestPoint(31, 36, Axial);
        } else if (position == MIDDLE) {
            driveToClosestPoint(36, 36, Axial);
            driveToClosestPoint(39, 36, Axial);
            piBot.drop(GrabberSide.Left);
            sleep(100);
            piBot.grab(GrabberSide.Right);
            driveToClosestPoint(23, 36, Axial);
        } else if (position == AUDIENCE) {
            driveToClosestPoint(25, 36, Axial);
            rotateToHeading(-135);
            driveToClosestPoint(38, 38.5, Axial);
            piBot.drop(GrabberSide.Left);
            sleep(100);
            piBot.grab(GrabberSide.Right);
            driveToClosestPoint(25, 36, Axial);
            driveToClosestPoint(15, 33, Lateral);
        } else {
            driveToClosestPoint(31, 36, Axial);
            rotateToHeading(-45);
            driveToClosestPoint(37, 44.5, Axial);
            piBot.drop(GrabberSide.Left);
            sleep(100);
            piBot.grab(GrabberSide.Right);
        } //purple pixel. it works well

        rotateAndDriveToPoint(24, 18, Axial);
        rotateToHeading(180);

        sleep(delayMilliseconds);

        driveToClosestPoint(61, 18, Lateral);
        driveToClosestPoint(61, 118, Axial);

//        if (position == AUDIENCE) {
//            aprilTagPosX = 42;
//            driveToClosestPoint(aprilTagPosX, 118, Lateral);
//        } else if (position == MIDDLE) {
//            aprilTagPosX = 36;
//            driveToClosestPoint(aprilTagPosX, 118, Lateral);
//        } else if(position == BACKSTAGE) {
//            aprilTagPosX = 30;
//            driveToClosestPoint(aprilTagPosX, 118, Lateral);
//        }
//
//        piBot.positionGrabber(0.9, false);
//        hw.liftMotor.setTargetPosition(500);
//
//        rotateToHeading(180);
//        driveToClosestPoint(aprilTagPosX, 122, Axial);
//        hw.liftMotor.setTargetPosition(0);
//        piBot.positionGrabber(0, true);
//
//        driveToClosestPoint(10, 120, Lateral);
    }
}
