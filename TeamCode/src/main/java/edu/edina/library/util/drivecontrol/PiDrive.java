package edu.edina.library.util.drivecontrol;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import java.util.Arrays;

import edu.edina.library.util.RobotHardware;

@SuppressLint("DefaultLocale")
public class PiDrive {
    private final PiMotor[] motors;

    //for 20:1
    private static final double shutdownTol = 30;
    private static final MoveCal driveCal = new MoveCal(546.0 / 39.0, -500);
    private static final MoveCal strafeCal = new MoveCal(746.2 / 40.5, -1200);
    private static final MoveCal diagonalCal = new MoveCal(749.0 / 40.5, -1200);
    private static final double[] accelVolts = new double[]{0.9619 * 0.25, 0.9748 * 0.25, 1.0273 * 0.25, 1.0405 * 0.25};

    private double[] move;
    private MoveCal amc;
    private double tgtDeg;
    private boolean driving;

    public PiDrive(RobotHardware hw) {
        DcMotor[] motors = new DcMotor[]{hw.frontLeftMotor, hw.backLeftMotor, hw.frontRightMotor, hw.backRightMotor};
        VoltageSensor vs = hw.voltageSensor;

        this.motors = new PiMotor[4];
        for (int i = 0; i < 4; i++) {
            if (i < 2) motors[i].setDirection(FORWARD);
            else motors[i].setDirection(REVERSE);

            double accelTf = accelVolts[i];

            this.motors[i] = new PiMotor(motors[i], MotorConfig.driveMotor, vs, accelTf);
        }
    }

    public PiMotor get(int i) {
        return motors[i];
    }

    public void resetPos() { // prepMove, reset deg, read imu
        for (int i = 0; i < 4; i++)
            get(i).resetDeg();
    }

    public MoveCal getActiveMoveCal() {
        return amc;
    }

    public double getDegSpeed() {
        double[] d = new double[4];
        for (int i = 0; i < 4; i++)
            d[i] = get(i).getDegSpeed() * move[i]; // mult/divide?

        Arrays.sort(d);
        return (d[1] + d[2]) / 2;
    }

    public double getDeg() {
        double[] d = new double[4];
        for (int i = 0; i < 4; i++)
            d[i] = get(i).getDeg() * move[i]; // mult/divide?

        Arrays.sort(d);
        return (d[1] + d[2]) / 2;
    }

    public double getPos() {
        double deg = getDeg();
        return deg / amc.dpi;
    }

    public String moveString() {
        return String.format("tgtDeg: %f, drv: %s, move: %f, %f, %f, %f",
                tgtDeg, driving, move[0], move[1], move[2], move[3]);
    }

    public void preRun(double targetPos, DriveDirection d) {
        if (d == DriveDirection.Lateral) {
            move = new double[]{1, -1, -1, 1};
            amc = strafeCal;
        } else if (d == DriveDirection.Axial) {
            move = new double[]{1, 1, 1, 1};
            amc = driveCal;
        } else if (d == DriveDirection.Diagonal) {
            move = new double[]{2, 0, 0, 2};
            amc = diagonalCal;
        } else if (d == DriveDirection.CrossDiagonal) {
            move = new double[]{0, 2, 2, 0};
            amc = diagonalCal;
        } else {
            throw new RuntimeException("unknown direction");
        }

        tgtDeg = amc.dpi * targetPos;

        setDriving(true, 1);
    }

    public boolean run() {
        for (int i = 0; i < 4; i++)
            get(i).sample();

        double s = getDegSpeed();
        double d = getDeg();

        double dist = tgtDeg - d;

        double vSign = signum(s, 5);
        double dSign = signum(dist, 30);

        if (vSign == 0) { //stopped
            if (dSign == 0) { //at target
                shutdown();
                return true;
            } else {
                setDriving(true, 1);
            }
        }

        double requiredDeccel = -s * s / (2 * dist);

        if (vSign * dSign == -1) {
            setDriving(true, 1);
        } else if (Math.abs(requiredDeccel) > Math.abs(amc.deccel)) {
            setDriving(false, 1);
        }

        if (!driving) {
            double torqueLimit = Math.abs(requiredDeccel) / Math.abs(amc.deccel);
            setDriving(false, torqueLimit);
        }

        for (int i = 0; i < 4; i++)
            get(i).run(s, move[i], dSign);

        return false;
    }

    private double signum(double x, double tol) {
        if (Math.abs(x) < tol) {
            return 0;
        } else if (x > 0) {
            return 1;
        } else if (x < 0) {
            return -1;
        } else {
            throw new RuntimeException("invalid float");
        }
    }

    private void setDriving(boolean t, double torqueLimit) {
        this.driving = t;

        for (int i = 0; i < 4; i++)
            get(i).setDriving(t, torqueLimit);
    }

    private void shutdown() {
        for (int i = 0; i < 4; i++)
            get(i).shutdown();
    }
}
