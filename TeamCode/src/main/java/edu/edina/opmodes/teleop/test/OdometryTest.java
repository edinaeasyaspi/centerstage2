package edu.edina.opmodes.teleop.test;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import edu.edina.library.util.RobotHardware;

@TeleOp
public class OdometryTest extends LinearOpMode {
    private RobotHardware hw;

    @Override
    public void runOpMode() {
        hw = new RobotHardware(hardwareMap);
        DcMotor[] motors = new DcMotor[]{hw.frontLeftMotor, hw.backLeftMotor, hw.frontRightMotor, hw.backRightMotor};

        for (int i = 0; i < 4; i++) {
            motors[i].setZeroPowerBehavior(FLOAT);
            motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
