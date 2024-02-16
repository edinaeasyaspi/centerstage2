package edu.edina.opmodes.teleop.test;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.vision.VisionPortal;

import edu.edina.library.util.PiBot;
import edu.edina.library.util.Position;

import edu.edina.library.util.Positioning;
import edu.edina.library.util.RobotHardware;

@Autonomous
public class AprilTagTest extends LinearOpMode {

    public void runOpMode() {
        RobotHardware hw = new RobotHardware(hardwareMap, telemetry);
        PiBot piBot = new PiBot(hw);
        Positioning posn = piBot.getPositioning();
        posn.setCurrPos(new Position(72, 72, 180));

        while (opModeInInit()) {
            piBot.setup(false, false, true);
            posn.readHeading(true);
            Position p = posn.getCurrPos();
            telemetry.addData("position", p);
            telemetry.addData("position diag ", p.diagString);
            telemetry.update();
        }

        while (opModeIsActive()) {
            posn.readHeading(true);
            posn.readAprilTagPosition(true);
            Position p = posn.getCurrPos();
            telemetry.addData("position", p);
            telemetry.addData("position diag ", p.diagString);
            telemetry.update();
        }
    }
}
