package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.edina.library.util.PiBot;
import edu.edina.library.util.Position;
import edu.edina.library.util.Positioning;
import edu.edina.library.util.RobotHardware;

@Autonomous
public class AutoSetup extends LinearOpMode {
    public PiBot piBot;

    public RobotHardware hw;

    @Override
    public void runOpMode() {
        hw = new RobotHardware(hardwareMap);
        piBot = new PiBot(hw);
        Positioning posn = piBot.getPositioning();

        while (opModeInInit()) {
            piBot.setup(false, false, true);
        }

        while (opModeIsActive()) {
            posn.readHeading(true);
            Position p = posn.readAprilTagPosition(true);
            telemetry.addData("position", p);
            telemetry.update();

            piBot.positionGrabber(0, true);
        }
    }
}
