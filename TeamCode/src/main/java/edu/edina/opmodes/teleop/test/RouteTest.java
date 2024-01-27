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
    private PiBot piBot;

    @Override
    public void runOpMode() {
        RobotHardware hw = new RobotHardware(hardwareMap, telemetry);
        piBot = new PiBot(hw);

        waitForStart();

        Point startLocation = new Point(72, 72);

        drive(new Point(72, 84), DriveDirection.Axial);
        drive(new Point(84, 84), DriveDirection.Lateral);
        rotate(startLocation);
        sleep(3000);
        drive(startLocation, DriveDirection.Axial);
    }

    private void drive(Point to, DriveDirection direction) {
        piBot.planDriveToClosestPoint(to, direction);
        while (opModeIsActive()) {
            if (piBot.runDrive() == DriveStatus.Done) break;
        }

        showPos();
    }

    private void rotate(Point to) {
        piBot.planRotateToPoint(to);
        while (opModeIsActive()) {
            if (piBot.rotateToHeading() == DriveStatus.Done) break;
        }

        showPos();
    }

    private void showPos() {
        telemetry.addData("pos", piBot.getPositioning().getCurrPos());
        telemetry.update();
    }
}
