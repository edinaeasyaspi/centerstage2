package edu.edina.opmodes.teleop.test;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import edu.edina.library.util.drivecontrol.MotorEncoderOdometry;

/////// 2 other comments - then test, test, test

@TeleOp
public class OdometryTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        double robotWidth = 14.5;
        double hw = robotWidth / 2;
        int[] mult = new int[]{1, 1, -1, -1};
        DcMotorEx[] motors = new DcMotorEx[]{
                    hardwareMap.get(DcMotorEx.class, "frontLeftMotor"),
                    hardwareMap.get(DcMotorEx.class, "backLeftMotor"),
                    hardwareMap.get(DcMotorEx.class, "frontRightMotor"),
                    hardwareMap.get(DcMotorEx.class, "backRightMotor")
        };
        MotorEncoderOdometry[] motor = new MotorEncoderOdometry[]{
                    new MotorEncoderOdometry(motors[0], mult[0], hw),
                    new MotorEncoderOdometry(motors[1], mult[1], hw),
                    new MotorEncoderOdometry(motors[2], mult[2], hw),
                    new MotorEncoderOdometry(motors[3], mult[3], hw)
        };
        String[] positions = new String[]{"FL", "BL", "FR", "BR"};
        for (int i = 0; i < 4; i++) {
            motors[i].setZeroPowerBehavior(FLOAT);
            ////// I think this is unneeded    motors[i].setMode(STOP_AND_RESET_ENCODER);
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
            YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();

            double yaw = robotOrientation.getYaw(RADIANS);

            for (int i = 0; i < 4; i++) {
                telemetry.addData(positions[i], motor[i].motorInches());
                telemetry.addData("radius", "%d: %.2f", i, motor[i].estRadius(yaw));
                telemetry.addData("arc length", "%d: %.2f", i, motor[i].estRadius(yaw) * yaw); //angle * radius = arc length////// is there an extra multiply here?
            }

            telemetry.addData("IMU", robotOrientation.getYaw(DEGREES));

            telemetry.update();

            //robotodometry
        }
    }
}