package edu.edina.library.util;

import com.qualcomm.hardware.bosch.BNO055IMUNew;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class Parts {
    public final WebcamName webcam;

    public final BNO055IMUNew imu;

    public Parts(HardwareMap hardwareMap) {
        webcam = hardwareMap.get(WebcamName.class, "LogitechC270_8034PI");
        imu = hardwareMap.get(BNO055IMUNew.class, "imu");
    }
}
