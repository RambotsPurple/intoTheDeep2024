package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.ParallelAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotConfig;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@Autonomous(name = "Real Auton ", group = "Autonomous")
public class SparkfunAuto extends LinearOpMode {
    public class Lift {

        // component
//        public Lift(HardwareMap hardwareMap) {
//            arm1 = hardwareMap.get(DcMotorEx.class, "arm1");
//            arm1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            arm1.setDirection(DcMotorSimple.Direction.FORWARD);
//
//            arm2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            arm2.setDirection(DcMotorSimple.Direction.FORWARD);
//        }

        // raises lift
        public class LiftUp implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    RobotConfig.arm1.setPower(0.8);
                    RobotConfig.arm2.setPower(0.8);
                    initialized = true;
                }

                double pos = RobotConfig.arm1.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos < 3000.0) {
                    return true;
                } else {
                    RobotConfig.arm1.setPower(0);
                    RobotConfig.arm2.setPower(0);
                    return false;
                }
            }
        }

        // function for raising lift
        public Action liftUp() {
            return new LiftUp();
        }


        // lower lift
        public class LiftDown implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    RobotConfig.arm1.setPower(-0.8);
                    RobotConfig.arm2.setPower(-0.8);
                    initialized = true;
                }

                double pos = RobotConfig.arm1.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos > 100.0) {
                    return true;
                } else {
                    RobotConfig.arm1.setPower(0);
                    RobotConfig.arm2.setPower(0);
                    return false;
                }
            }
        }

        // lower lift
        public Action liftDown(){
            return new LiftDown();
        }
    }

    // Claw component
    public class Claw {


        public class CloseClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.wrist1.setPosition(0.55);
                return false;
            }
        }
        public Action closeClaw() {
            return new CloseClaw();
        }

        public class OpenClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.wrist1.setPosition(1.0);
                return false;
            }
        }
        public Action openClaw() {
            return new OpenClaw();
        }
    }

    @Override
    public void runOpMode() {
        RobotConfig.initialize(hardwareMap);

        Pose2d initialPose = new Pose2d(11.8, 61.7, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Claw claw = new Claw();
        // make a Lift instance
        Lift lift = new Lift();
        // vision here that outputs position

//        @TODO trajectorty

        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5)
                .waitSeconds(3);
//        TrajectoryActionBuilder tab2 = drive.actionBuilder(initialPose)
//                .lineToY(37)
//                .setTangent(Math.toRadians(0))
//                .lineToX(18)
//                .waitSeconds(3)
//                .setTangent(Math.toRadians(0))
//                .lineToXSplineHeading(46, Math.toRadians(180))
//                .waitSeconds(3);
//        TrajectoryActionBuilder tab3 = drive.actionBuilder(initialPose)
//                .lineToYSplineHeading(33, Math.toRadians(180))
//                .waitSeconds(2)
//                .strafeTo(new Vector2d(46, 30))
//                .waitSeconds(3);
        Action trajectoryActionCloseOut = tab1.endTrajectory().fresh()
                .strafeTo(new Vector2d(48, 12))
                .build();

        // actions that need to happen on init; for instance, a claw tightening.
        Actions.runBlocking(claw.closeClaw());




        waitForStart();

        if (isStopRequested()) return;

        Action trajectoryActionChosen;

        trajectoryActionChosen = tab1.build();
        Actions.runBlocking(
                new SequentialAction(
                        trajectoryActionChosen,
                        lift.liftUp(),
                        claw.openClaw(),
                        lift.liftDown(),
                        trajectoryActionCloseOut
                )
        );
    }
}