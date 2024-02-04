package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.util.Position;

@Autonomous
public class AutoBlueFront extends AutoFrontMode {
    public AutoBlueFront() {
        super(false, new Position(0, 36, -90));
    }
}
