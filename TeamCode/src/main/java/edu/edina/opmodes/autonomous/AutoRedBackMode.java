package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.util.Position;

@Autonomous(name = "red back")
public class AutoRedBackMode extends AutoBackMode {
    public AutoRedBackMode() {
        super(true, new Position(137, 84, 90));
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
