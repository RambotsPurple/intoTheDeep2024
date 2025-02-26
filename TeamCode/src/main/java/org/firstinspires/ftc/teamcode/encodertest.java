package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name = "encodertest")
public class encodertest extends LinearOpMode {
    private DcMotor frontLeftMotor = null, backLeftMotor = null;
    private DcMotor frontRightMotor = null, backRightMotor = null;

    private double armpow = 0;
    private double slidepow = 0;
    private DcMotor slideExtension = null;
    private DcMotor slideAbduction = null;
    private DcMotor slideAbduction2 = null;
    private Servo  wrist1 = null;
    private Servo leftIntake = null;
    private Servo rightIntake = null;
    private double intakePower = 1;


    @Override
    public void runOpMode() {
        RobotConfig.initialize(hardwareMap);


        //wrist
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
//            slides
//            RobotConfig.slideExtension.setPower(gamepad1.right_trigger > 0 ? 1 : gamepad1.dpad_up ? 0 : slideExtension.getPower());
//            RobotConfig.arm1.setPower(gamepad1.left_trigger > 0 ? 1 : gamepad1.dpad_up ? 0 : slideAbduction.getPower());
//            RobotConfig.arm2.setPower(gamepad1.left_trigger > 0 ? 1 : gamepad1.dpad_up ? 0 : slideAbduction2.getPower());
            armpow = gamepad1.left_stick_y;
            slidepow = gamepad1.right_stick_y;
            RobotConfig.slideExtension.setPower(slidepow);
            RobotConfig.arm1.setPower(armpow);
            RobotConfig.arm2.setPower(armpow);
//            motors
            RobotConfig.frontLeft.setPower(gamepad1.a ? 1 : gamepad1.dpad_up ? 0 : frontLeftMotor.getPower());

            RobotConfig.rearLeft.setPower(gamepad1.b ? 1 : gamepad1.dpad_up ? 0 : backLeftMotor.getPower());

            RobotConfig.frontRight.setPower(gamepad1.x ? 1 : gamepad1.dpad_up ? 0 : frontRightMotor.getPower());

            RobotConfig.rearRight.setPower(gamepad1.y ? 1 : gamepad1.dpad_up ? 0 : backRightMotor.getPower());



            telemetry.addData("encoder frontleft", RobotConfig.frontLeft.getCurrentPosition());
            telemetry.addData("encoder front right", RobotConfig.frontRight.getCurrentPosition());
            telemetry.addData("encoder backright", RobotConfig.rearRight.getCurrentPosition());
            telemetry.addData("encoder backleft", RobotConfig.rearLeft.getCurrentPosition());
            telemetry.addData("encoder abd", RobotConfig.arm1.getCurrentPosition());
            telemetry.addData("encoder extend", RobotConfig.slideExtension.getCurrentPosition());
            telemetry.addLine("A front left motor");
            telemetry.addLine("B back left motor");
            telemetry.addLine("X front right motor");
            telemetry.addLine("Y back left motor");
            telemetry.addLine("left stick slide abd");
            telemetry.addLine("right stick slide extend");


            telemetry.update();

        }
    }

}