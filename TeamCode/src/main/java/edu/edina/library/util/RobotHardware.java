package edu.edina.library.util;

import com.acmerobotics.roadrunner.ftc.LynxFirmware;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class RobotHardware {
    //public final DistanceSensor distanceSensor;
    public final DcMotorEx frontLeftMotor, frontRightMotor, backRightMotor, backLeftMotor;

    public final VoltageSensor voltageSensor;

    public final IMU imu;

    public final Servo rightClaw, leftClaw;

    public final Servo  wrist;
    public final CRServo droneLauncherServo;

    public final DcMotorEx liftMotor;

    public final WebcamName webcam;



    public final int cameraMonitorViewId;
    public WebcamName LogitechC270_8034PI;

    public RobotHardware(HardwareMap hardwareMap) {
        LynxFirmware.throwIfModulesAreOutdated(hardwareMap);

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "backRightMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");


        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        droneLauncherServo = hardwareMap.get(CRServo.class, "droneLauncherServo");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        wrist = hardwareMap.get(Servo.class, "wrist");


        liftMotor = hardwareMap.get(DcMotorEx.class, "liftMotor");


        webcam = hardwareMap.get(WebcamName.class, "LogitechC270_8034PI");
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

    }
}
