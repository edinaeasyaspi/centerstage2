package edu.edina.opmodes.teleop.test;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import edu.edina.library.util.RobotHardware;

@TeleOp
public class OdometryTest extends LinearOpMode {


    @Override
    public void runOpMode() {
        DcMotorEx frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        DcMotorEx backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        DcMotorEx backRightMotor = hardwareMap.get(DcMotorEx.class, "backRightMotor");
        DcMotorEx frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        DcMotor[] motors = new DcMotor[]{frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor};
        String[] positions = new String[]{"FL", "BL", "FR", "BR"};
        for (int i = 0; i < 4; i++) {
            motors[i].setZeroPowerBehavior(FLOAT);
            motors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        waitForStart();

        while (opModeIsActive()) {
            for (int i = 0; i < 4; i++) {
                telemetry.addData(positions[i], motors[i].getCurrentPosition());
            }

            telemetry.update();
        }
    }
}