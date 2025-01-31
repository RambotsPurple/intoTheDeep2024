package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

/*
 * This OpMode illustrates the concept of driving a path based on encoder counts.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forward, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backward for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This method assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@Autonomous(name="Main", group="Robot")
public class AutoOpMode extends LinearOpMode {

    /* Declare OpMode members. */
    private DcMotor frontLeftMotor = null, backLeftMotor = null;
    private DcMotor frontRightMotor = null, backRightMotor = null;
    private DcMotor slideExtension = null;
    private DcMotor slideAbduction = null;
    private DcMotor slideAbduction2 = null;
    private Servo  wrist1 = null;
    private Servo leftIntake = null;
    private Servo rightIntake = null;
    private double intakePower = 1;

    private ElapsedTime runtime = new ElapsedTime();

    // Calculate the COUNTS_PER_INCH for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double COUNTS_PER_MOTOR_REV = 134.4; // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0; // No External Gearing.
    static final double WHEEL_DIAMETER_INCHES = 4.0; // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    private OpenCvCamera webcam;

    @Override
    public void runOpMode() {
        // Drive Train motor
        frontLeftMotor = hardwareMap.get(DcMotor.class, "leftFront");
        frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
        backLeftMotor = hardwareMap.get(DcMotor.class, "leftBack");
        backRightMotor = hardwareMap.get(DcMotor.class, "rightBack");

        // DcMotors for Linear slide
        slideExtension = hardwareMap.get(DcMotor.class, "slideExtend");
        wrist1 = hardwareMap.get(Servo.class, "wrist1");
        slideAbduction = hardwareMap.get(DcMotor.class, "slideAbd");
        slideAbduction2 = hardwareMap.get(DcMotor.class, "slideAbd2");

        slideExtension.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        slideAbduction.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        slideAbduction2.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);



        // Takers
        leftIntake = hardwareMap.get(Servo.class, "l_intake");
        rightIntake = hardwareMap.get(Servo.class, "r_intake");

        // MaybeIntake = hardwareMap.get(DcMotor.class, "intake");
        // Setting the direction for the motor on where to rotate

        // Orientation for drivetrain
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // intake
        boolean intakeReleased = true;

        // linear slide
        slideExtension.setDirection(DcMotor.Direction.FORWARD);
        slideAbduction.setDirection(DcMotor.Direction.FORWARD);
        slideAbduction2.setDirection(DcMotor.Direction.REVERSE);

        // normalize motor positions
        double normalizedAbdPos1 = (double)slideAbduction.getCurrentPosition() / COUNTS_PER_MOTOR_REV; // TODO test

        // ENCODERS
        slideAbduction2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideAbduction2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideAbduction.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideAbduction.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//
//        // Initialize webcam
//        webcam = hardwareMap.get(OpenCvCamera.class, "Webcam 1");
//        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
//            @Override
//            public void onOpened() {
//                // Start streaming to the phone's display
//                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
//            }
//
//            @Override
//            public void onError(int errorCode) {
//                telemetry.addData("Webcam Error", "Error code: " + errorCode);
//                telemetry.update();
//            }
//        });

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg dzrives may require direction flips
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

        // stupid ass sliders
        slideExtension.setDirection(DcMotor.Direction.FORWARD);
        slideAbduction.setDirection(DcMotor.Direction.FORWARD);
        slideAbduction2.setDirection(DcMotor.Direction.FORWARD);


        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        // sigma
        slideExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideAbduction.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideAbduction2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        // this is why oop should be used
        slideExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideAbduction.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideAbduction2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set zero power behaviour
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // i hate this
        slideExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideAbduction.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideAbduction2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Starting at", "%7d :%7d",
                frontLeftMotor.getCurrentPosition(),
                frontRightMotor.getCurrentPosition(),
                backLeftMotor.getCurrentPosition(),
                backRightMotor.getCurrentPosition()
        );
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

//        // Step through each leg of the path,
//        // Note: Reverse movement is obtained by setting a negative distance (not speed)
////        encoderDrive(DRIVE_SPEED,  48,  48, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
////        encoderDrive(TURN_SPEED,   12, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
////        encoderDrive(DRIVE_SPEED, -24, -24, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout
        // move to submersible from start

        wrist1.setPosition(1);
        encoderraise(1, 24, 24);
//        // pick up scoring element from spike mark 1
//        encoderDrive(1, 32, 44); // approx 38 inch movement
//        // pushing 3 elements to the specimen station
//        encoderDrive(1, -12, -12, 1); // move back
//        encoderDrive(1, 30, 15, 1); // turn right
//            encoderDrive(1, 32, 32, 1); // move forward
//            encoderDrive(1, 16, 32, 1); // turn left
//            encoderDrive(1, 10, 10, 1); // move forward
//            encoderDrive(1, 30, 15, 1); // turn right
//            encoderDrive(1, 3, 3, 1); // move forward
//            encoderDrive(1, 30, 15, 1); // turn right
//            encoderDrive(1, 40, 40, 1); // move forwared
//            encoderDrive(1, -20, 20, 1); // 180 turn
//
//            encoderDrive(1, 40, 40, 1); // move forward
//            encoderDrive(1, 30, 15, 1); // turn right
//            encoderDrive(1, 3, 3, 1); // move forward
//            encoderDrive(1, 15, 30, 1); // turn right
//            encoderDrive(1, 40, 40, 1); // move forward
//            encoderDrive(1, -20, 20, 1); // 180 turn
//            encoderDrive(1, 40, 40, 1); // move forward
//            encoderDrive(1, 15, 30, 1); // turn right
//            encoderDrive(1, 3, 3, 1); // move forward
//            encoderDrive(1, 30, 15, 1); // turn right
//            encoderDrive(1, 40, 40, 1); // move forward
//
//            // hang the specimens
//            encoderDrive(1, 12, 12, 1); // turn right
//            encoderDrive(1, 12, 12, 1); // move forward



//        park hopefully
        sleep(2200);
//        encoderDrive(1, 35, 15, 1);
//        encoderDrive(1, 30, 30, 1);
        // park hehehe
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);  // pause to display final telemetry message.
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     */
    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = frontLeftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newLeftBackTarget = backLeftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightFrontTarget = frontRightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newRightBackTarget = backRightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);


            frontLeftMotor.setTargetPosition(newLeftFrontTarget);
            backLeftMotor.setTargetPosition(newLeftBackTarget);
            frontRightMotor.setTargetPosition(newRightFrontTarget);
            backRightMotor.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            frontLeftMotor.setPower(Math.abs(speed));
            backLeftMotor.setPower(Math.abs(speed));
            frontRightMotor.setPower(Math.abs(speed));
            backRightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (frontLeftMotor.isBusy() && backLeftMotor.isBusy() && frontRightMotor.isBusy() && backRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d :%7d", newLeftFrontTarget, newLeftBackTarget, newRightFrontTarget, newRightBackTarget);
                telemetry.addData("Currently at",  " at %7d :%7d",
                        frontLeftMotor.getCurrentPosition(), backLeftMotor.getCurrentPosition(), frontRightMotor.getCurrentPosition(),backRightMotor.getCurrentPosition() );
                telemetry.update();
            }

            // Stop all motion;
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



            sleep(250);   // optional pause after each move.
        }
    }

    // overloaded method for NO TIMEOUT CUZ ITS USELESS
    public void encoderraise(double speed, double leftInches, double rightInches) {
        encoderDrive(speed, leftInches, rightInches, 60);

        slideExtension.setPower(0);
        slideAbduction.setPower(0);
        slideAbduction2.setPower(0);

        slideAbduction.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideAbduction2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}