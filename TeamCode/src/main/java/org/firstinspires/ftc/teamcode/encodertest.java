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

    private DcMotor slideExtension = null;
    private DcMotor slideAbduction = null;
    private DcMotor slideAbduction2 = null;
    private Servo  wrist1 = null;
    private Servo leftIntake = null;
    private Servo rightIntake = null;
    private double intakePower = 1;


    @Override
    public void runOpMode() {

        // initializing hardware

        // Drive Train motor
        frontLeftMotor = hardwareMap.get(DcMotor.class, "leftFront");
        frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
        backLeftMotor = hardwareMap.get(DcMotor.class, "leftBack");
        backRightMotor = hardwareMap.get(DcMotor.class, "rightBack");

        slideExtension = hardwareMap.get(DcMotor.class, "slideExtend");
        wrist1 = hardwareMap.get(Servo.class, "wrist1");
        slideAbduction = hardwareMap.get(DcMotor.class, "slideAbd");
        slideAbduction2 = hardwareMap.get(DcMotor.class, "slideAbd2");



        // MaybeIntake = hardwareMap.get(DcMotor.class, "intake");
        // Setting the direction for the motor on where to rotate


        slideExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideAbduction.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideAbduction2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        slideExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideAbduction.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideAbduction2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Orientation for drivetrain
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // intake
        boolean intakeReleased = true;






//    leftIntake.setPosition(0);
//    rightIntake.setPosition(0);
        //wrist
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
//            slides
            slideExtension.setPower(gamepad1.right_trigger > 0 ? 1 : gamepad1.dpad_up ? 0 : slideExtension.getPower());
            slideAbduction.setPower(gamepad1.left_trigger > 0 ? 1 : gamepad1.dpad_up ? 0 : slideAbduction.getPower());
            slideAbduction2.setPower(gamepad1.left_trigger > 0 ? 1 : gamepad1.dpad_up ? 0 : slideAbduction2.getPower());

//            motors
            frontLeftMotor.setPower(gamepad1.a ? 1 : gamepad1.dpad_up ? 0 : frontLeftMotor.getPower());

            backLeftMotor.setPower(gamepad1.b ? 1 : gamepad1.dpad_up ? 0 : backLeftMotor.getPower());

            frontRightMotor.setPower(gamepad1.x ? 1 : gamepad1.dpad_up ? 0 : frontRightMotor.getPower());

            backRightMotor.setPower(gamepad1.y ? 1 : gamepad1.dpad_up ? 0 : backRightMotor.getPower());



            telemetry.addData("encoder frontleft", frontLeftMotor.getCurrentPosition());
            telemetry.addData("encoder front right", frontRightMotor.getCurrentPosition());
            telemetry.addData("encoder backright", backRightMotor.getCurrentPosition());
            telemetry.addData("encoder backleft", backLeftMotor.getCurrentPosition());
            telemetry.addData("encoder abd", slideAbduction.getCurrentPosition());
            telemetry.addData("encoder extend", slideExtension.getCurrentPosition());
            telemetry.addLine("A front left motor");
            telemetry.addLine("B back left motor");
            telemetry.addLine("X front right motor");
            telemetry.addLine("Y back left motor");
            telemetry.addLine("left trig slide abd");
            telemetry.addLine("right trig slide extend");


            telemetry.update();

        }
    }

}