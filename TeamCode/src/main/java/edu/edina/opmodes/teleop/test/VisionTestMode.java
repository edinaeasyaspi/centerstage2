package edu.edina.opmodes.teleop.test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

import edu.edina.library.util.PiBot;
import edu.edina.library.util.RobotHardware;

@Autonomous
public class VisionTestMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        PiBot bot = new PiBot(new RobotHardware(hardwareMap, telemetry));

        while (opModeInInit()) {
            bot.getSelection();
            telemetry.update();
        }

        waitForStart();

        while (opModeIsActive()) {
            sleep(1);
        }
    }
}
