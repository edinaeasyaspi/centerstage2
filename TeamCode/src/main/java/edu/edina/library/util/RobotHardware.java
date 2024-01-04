package edu.edina.library.util;

import com.acmerobotics.roadrunner.ftc.LynxFirmware;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import edu.edina.opmodes.teleop.HangingMotorTest;

public class RobotHardware {
    //public final DistanceSensor distanceSensor;
    public final DcMotorEx frontLeftMotor, frontRightMotor, backRightMotor, backLeftMotor;

    public final VoltageSensor voltageSensor;

    public final IMU imu;

    //public final Servo leftLiftServo, rightLiftServo;

    //public final CRServo leftIntakeServo, rightIntakeServo;

    //public final DcMotorEx liftMotor;

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
        //HangingMotor = hardwareMap.get(DcMotorEx.class, "hangingMotor");

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

//        leftLiftServo = hardwareMap.get(Servo.class, "S1");
//        rightLiftServo = hardwareMap.get(Servo.class, "S2");
//        //droneLauncherServo = hardwareMap.get(Servo.class, "droneLauncherServo");
//
//        leftIntakeServo = hardwareMap.get(CRServo.class, "F2");
//        rightIntakeServo = hardwareMap.get(CRServo.class, "F1");

        //liftMotor = hardwareMap.get(DcMotorEx.class, "liftMotor");
        //distanceSensor = hardwareMap.get(DistanceSensor.class, "distance_sensor");

        webcam = hardwareMap.get(WebcamName.class, "LogitechC270_8034PI");
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

    }
}
