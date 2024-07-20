package edu.edina.opmodes.teleop.test;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

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

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        waitForStart();

        imu.resetYaw();

        while (opModeIsActive()) {
            for (int i = 0; i < 4; i++) {
                if (i <= 1) {telemetry.addData(positions[i], motors[i].getCurrentPosition());}
                else {telemetry.addData(positions[i], (motors[i].getCurrentPosition() * -1));}
            }
            YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
            telemetry.addData("IMU", robotOrientation.getYaw(DEGREES));
            telemetry.update();
        }
    }
}