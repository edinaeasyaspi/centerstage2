package edu.edina.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.edina.library.util.PiBot;
import edu.edina.library.util.RobotHardware;

@Autonomous
public class PixelDetectTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        RobotHardware hw = new RobotHardware(hardwareMap, telemetry);
        PiBot piBot = new PiBot(hw);

        while (opModeInInit()) {
            piBot.setupVision(false, true);
            piBot.detectPixel();
            telemetry.update();
        }

        while (opModeIsActive()) {
            sleep(1);
        }
    }
}
