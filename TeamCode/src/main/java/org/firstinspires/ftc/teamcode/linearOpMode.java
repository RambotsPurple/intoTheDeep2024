package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "RambotsPurpleTeleOp")
public class linearOpMode extends LinearOpMode {

    private double intakePos = 0;
    private boolean runningPreset = false;

    // wrist
    private double wristPos = 0.16;
    private double wrist2Pos = 0;
    private double targetDir = 0;
    double dir;
    int targetPos = 0;
    int armCorrectionFactor = 0;

    @Override
    public void runOpMode() {

        // initializing hardware
        RobotConfig.initialize(hardwareMap);

        waitForStart();

        boolean intakeReleased = true;
        boolean correctPath = false;
        boolean fieldCentric = false;

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            dir = RobotConfig.getAngle();

      /*
        GamePad Map
        GamePad 1 (Driver)
          Left JoyStick = lateral, diagonal, forwards and backwards movements
          Right JoyStick = Rotation of drive train
        GamePad 2 (Operator)
          Button A = toggle position of claw to open or closed (We start closed)
          left stick x = slide extension
          right stick y = slide abduction
       */

            // tune correction factor
            if (gamepad2.right_bumper) {
                armCorrectionFactor += 1;
            } else if (gamepad2.right_trigger > 0) {
                armCorrectionFactor -= 1;
            }
            if (gamepad2.right_stick_y < 0) {
                armCorrectionFactor *= -1;
            }

            // toggle field centric drive
            if (gamepad1.left_trigger > 0) {
                fieldCentric = true;
            } else if (gamepad1.right_trigger > 0) {
                fieldCentric = false;
            }

            // linear slide controls
            double slideExtendPower = gamepad2.left_stick_y;
            double slideAbdPower = gamepad2.right_stick_y;
            if (gamepad2.right_stick_y != 0) {
                RobotConfig.arm1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                targetPos = RobotConfig.arm1.getCurrentPosition() + armCorrectionFactor;
                RobotConfig.arm2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                targetPos = RobotConfig.arm2.getCurrentPosition() + armCorrectionFactor;
            } else {
                RobotConfig.arm1.setTargetPosition(targetPos);
                RobotConfig.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RobotConfig.arm1.setVelocity(3000);
                RobotConfig.arm2.setTargetPosition(targetPos);
                RobotConfig.arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RobotConfig.arm2.setVelocity(3000);
            }

            if (gamepad1.right_stick_x != 0) {
                targetDir = dir ; // TODO tune
            }

            // turning + toggle autocorrect
            double turn;
            if (correctPath) {
                turn = (dir - targetDir) / 30 + gamepad1.right_stick_x;
                turn = Math.min(1, turn);
                turn = Math.max(-1, turn);
            } else {
                turn = gamepad1.right_stick_x;
            }
            if (gamepad1.left_bumper) {
                correctPath = true;
            } else if (gamepad1.right_bumper) {
                correctPath = false;
            }

            if (fieldCentric) {
                RobotConfig.drive(gamepad1.left_stick_x * 1.1, -gamepad1.left_stick_y, turn, dir);
            } else {
                RobotConfig.drive(gamepad1.left_stick_x * 1.1, -gamepad1.left_stick_y, turn, 0);
            }

            // intake
            if (gamepad2.a && intakeReleased) {
                intakePos = 1 - intakePos;
                intakeReleased = false;
            }
            if (gamepad2.b && intakeReleased) {
                intakePos = Math.max(0, intakePos - 0.1);
                intakeReleased = false;
            } else if (gamepad2.y && intakeReleased) {
                intakePos = Math.min(1, intakePos + 0.1);
                intakeReleased = false;
            }

            if (!gamepad2.a && !gamepad2.b && !gamepad2.y) {
                intakeReleased = true;
            }

            // presets
            if (gamepad2.dpad_up) {
                runningPreset = true;
                targetPos = RobotConfig.ABD_SPEC; // sets target position for arm1 encoders
                wristPos = 0;

            } else if (gamepad2.dpad_down) {
                runningPreset = true;
                targetPos = RobotConfig.ABD_PICKUP;
                wristPos = RobotConfig.WRIST_PICKUP;
                intakePos = 1;
            } // if

            // STOP ALL PRESETS
            if (gamepad2.dpad_left) {
                runningPreset = false;
            }

            // Power to the arm
            RobotConfig.arm1.setPower(slideAbdPower);
            RobotConfig.arm2.setPower(slideAbdPower);

            RobotConfig.slideExtension.setPower(slideExtendPower);

            // Wrist power
            if (gamepad2.left_trigger > 0) {
                wristPos = Math.min(1, wristPos + 0.025);
            } else if (gamepad2.left_bumper) {
                wristPos = Math.max(0, wristPos - 0.025);
            }

            RobotConfig.wrist1.setPosition(wristPos);


            // Power to the intake
            RobotConfig.intake.setPosition(intakePos);

            // Telemetry

            telemetry.addData("RUNNING PRESET:", runningPreset);
            telemetry.addData("RUNMODE:", RobotConfig.arm1.getMode());
            telemetry.addData("Abd 1 position:", RobotConfig.arm1.getCurrentPosition());
            telemetry.addData("intake pos BETTER VERSION:", RobotConfig.intake.getPosition());

            telemetry.addData("ENCODER:", RobotConfig.slideExtension.getCurrentPosition());
            telemetry.addData("wrist pos:", RobotConfig.wrist1);

            telemetry.addData("Intake pos (right is inverse): ", intakePos);
            telemetry.addData("Slide extension power: ", slideExtendPower);
            telemetry.addData("Slide abduction power: ", slideAbdPower);
            telemetry.addData("Target abduction pos: ", targetPos);
            telemetry.addData("Slide abduction pos: ", RobotConfig.arm1.getCurrentPosition());
            telemetry.addData("Slide extension pos: ", RobotConfig.slideExtension.getCurrentPosition());

            telemetry.addData("heading", dir);

            telemetry.addData("Wrist pos: ", wristPos);
            telemetry.addData("arm correction factor: ", armCorrectionFactor);
            telemetry.update();

        }
    }

}
