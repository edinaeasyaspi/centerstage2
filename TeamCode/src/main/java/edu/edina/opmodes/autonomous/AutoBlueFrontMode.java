package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.util.Position;

@Autonomous
public class AutoBlueFrontMode extends AutoFrontMode {
    public AutoBlueFrontMode() {
        super(false, new Position(7, 36, -90));
    }

    @Override
    protected int backboardX() {
        if (position == SelectedSpike.AUDIENCE)
            return 40;
        else if (position == SelectedSpike.MIDDLE)
            return 36;
        else
            return 32;
    }
}
