package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.util.Position;

@Autonomous
public class AutoBlueBackMode extends AutoBackMode {
    public AutoBlueBackMode() {
        super(false, new Position(7, 84, -90));
    }

    @Override
    protected int backboardX() {
        if (position == AutoMode.SelectedSpike.AUDIENCE)
            return 40;
        else if (position == AutoMode.SelectedSpike.MIDDLE)
            return 36;
        else
            return 32;
    }
}


