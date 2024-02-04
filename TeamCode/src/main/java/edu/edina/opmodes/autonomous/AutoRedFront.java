package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.util.Position;

@Autonomous
public class AutoRedFront extends AutoFrontMode {
    public AutoRedFront() {
        super(true, new Position(144, 36, 90));
    }
}
