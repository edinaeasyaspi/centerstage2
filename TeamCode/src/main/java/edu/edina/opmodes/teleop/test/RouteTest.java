package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.edina.library.util.DriveStatus;
import edu.edina.library.util.PiBot;
import edu.edina.library.util.Point;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.drivecontrol.DriveDirection;

@Autonomous
public class RouteTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        RobotHardware hw = new RobotHardware(hardwareMap, telemetry);
        PiBot piBot = new PiBot(hw);

        waitForStart();

        Point startLocation = new Point(72, 72);

        telemetry.addData("pos", piBot.getPositioning().getCurrPos());
        piBot.planDriveToClosestPoint(new Point(72, 102), DriveDirection.Axial);
        sleep(10000);

        while (opModeIsActive()) {
            if (piBot.runDrive() == DriveStatus.Done) break;
        }

        piBot.planDriveToClosestPoint(new Point(102, 102), DriveDirection.Lateral);

        while (opModeIsActive()) {
            if (piBot.runDrive() == DriveStatus.Done) break;
        }

        piBot.planRotateToPoint(startLocation);

        while (opModeIsActive()) {
            if (piBot.rotateToHeading() == DriveStatus.Done) break;
        }

        piBot.planDriveToClosestPoint(new Point(72, 72), DriveDirection.Axial);

        while (opModeIsActive()) {
            if (piBot.runDrive() == DriveStatus.Done) break;
        }
    }
}
