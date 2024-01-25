package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.util.drivecontrol.DriveDirection;

@Autonomous
public class DiagonalTestMode extends DriveTestMode {
    public DiagonalTestMode() {
        dir = DriveDirection.Diagonal;
    }
}
