package edu.edina.opmodes.autonomous;

import static edu.edina.library.util.GrabberSide.Both;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.edina.library.util.PiBot;
import edu.edina.library.util.RobotHardware;

@Autonomous
public class AutoSetup extends LinearOpMode {
    public PiBot piBot;

    public RobotHardware hw;

    @Override
    public void runOpMode() {
        hw = new RobotHardware(hardwareMap);
        piBot = new PiBot(hw);

        waitForStart();

        while (opModeIsActive()) {
            piBot.positionGrabber(0, true);
        }
    }
}
