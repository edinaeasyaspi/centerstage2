package edu.edina.opmodes.teleop.test;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp
public class OdometryTest extends LinearOpMode {
    private double inches;

    @Override
    public void runOpMode() {
        double degMult = 1114.0 / 360.0;
        DcMotor[] motors = new DcMotor[]{
                hardwareMap.get(DcMotorEx.class, "frontLeftMotor"),
                hardwareMap.get(DcMotorEx.class, "backLeftMotor"),
                hardwareMap.get(DcMotorEx.class, "backRightMotor"),
                hardwareMap.get(DcMotorEx.class, "frontRightMotor")
        };
        String[] positions = new String[]{"FL", "BL", "FR", "BR"};
        int[] mult = new int[]{1, 1, -1, -1};
        for (int i = 0; i < 4; i++) {
            motors[i].setZeroPowerBehavior(FLOAT);
            motors[i].setMode(STOP_AND_RESET_ENCODER);
            motors[i].setMode(RUN_USING_ENCODER);
        }

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        waitForStart();

        imu.resetYaw();

        while (opModeIsActive()) {
            for (int i = 0; i < 4; i++) {
                double inches = motors[i].getCurrentPosition() * mult[i] / degMult / 37.5;
                telemetry.addData(positions[i], "%.2fin", inches);
            }
            YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
            telemetry.addData("IMU", robotOrientation.getYaw(DEGREES));
            telemetry.update();
        }
    }
}