package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.util.Position;

@Autonomous(name = "red front")
public class AutoRedFrontMode extends AutoFrontMode {
    public AutoRedFrontMode() {
        super(true, new Position(137, 36, 90));
    }

//    @Override
//    protected int backboardX() {
//        if (position == SelectedSpike.AUDIENCE)
//            return 104;
//        else if (position == SelectedSpike.MIDDLE)
//            return 108;
//        else
//            return 112;
//    }
}

