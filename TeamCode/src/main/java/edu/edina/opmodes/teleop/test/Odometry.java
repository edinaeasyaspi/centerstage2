package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import edu.edina.library.util.PiBot;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.drivecontrol.PiDrive;

public class Odometry extends LinearOpMode {
    private RobotHardware hw;
    private static final DcMotor.ZeroPowerBehavior FLOAT = DcMotor.ZeroPowerBehavior.FLOAT;

    @Override
    public void runOpMode() {
        DcMotor[] motors = new DcMotor[]{hw.frontLeftMotor, hw.backLeftMotor, hw.frontRightMotor, hw.backRightMotor};
        for (int i = 0; i < 4; i++) {
            motors[i].setZeroPowerBehavior(FLOAT);
        }

        waitForStart();

        while (opModeIsActive()) {
            for (int i = 0; i < 4; i++) {
                motors[i].getCurrentPosition();

            }

            telemetry.addData("a", 1);
            telemetry.update();
        }
    }
}
