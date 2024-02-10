package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.util.Position;

@Autonomous
public class AutoBlueFrontMode extends AutoFrontMode {
    public AutoBlueFrontMode() {
        super(false, new Position(7, 36, -90));
    }
}
