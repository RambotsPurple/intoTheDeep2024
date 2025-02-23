package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@Autonomous(name = "TempSampleAuto better version ", group = "Autonomous")
public class ParkAutoSub extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    int targetPos = 0;

    public class wrist{
        public class WristUp implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.wrist1.setPosition(0.55);
                return false;
            }
        }
        public  Action wristUp() {
            return new WristUp();
        }

        public class WristDown implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.wrist1.setPosition(1);
                return false;
            }
        }
        public Action wristDown() {
            return new WristDown();
        }

    }

    public class Lift {

        // component
        public Lift() {
            RobotConfig.arm1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            RobotConfig.arm1.setDirection(DcMotorSimple.Direction.FORWARD);

            RobotConfig.arm2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            RobotConfig.arm2.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        // raises lift
        public class LiftUp implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    targetPos = RobotConfig.ABD_SPEC;
                    RobotConfig.arm1.setTargetPosition(targetPos);
                    RobotConfig.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RobotConfig.arm1.setVelocity(1000);
                    RobotConfig.arm2.setTargetPosition(targetPos);
                    RobotConfig.arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RobotConfig.arm2.setVelocity(1000);
                    initialized = true;
                }

                double pos = RobotConfig.arm1.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos < RobotConfig.ABD_SPEC) {
                    return true;
                } else {
                    targetPos = 0;
                    RobotConfig.arm1.setPower(targetPos);
                    RobotConfig.arm2.setPower(targetPos);
                    return false;
                }
            }
        }

        // function for raising lift
        public Action liftUp() {
            return new LiftUp();
        }


        // pickup
        public class LiftDown implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    targetPos = RobotConfig.ABD_PICKUP;
                    RobotConfig.arm1.setTargetPosition(targetPos);
                    RobotConfig.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RobotConfig.arm1.setVelocity(1000);
                    RobotConfig.arm2.setTargetPosition(targetPos);
                    RobotConfig.arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    RobotConfig.arm2.setVelocity(1000);
                    initialized = true;
                }

                double pos = RobotConfig.arm1.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos > RobotConfig.ABD_PICKUP) {
                    return true;
                } else {
                    targetPos = 0;
                    RobotConfig.arm1.setPower(targetPos);
                    RobotConfig.arm2.setPower(targetPos);
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
                RobotConfig.intake.setPosition(0.55);
                return false;
            }
        }
        public Action closeClaw() {
            return new CloseClaw();
        }

        public class OpenClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.intake.setPosition(1.0);
                return false;
            }
        }
        public Action openClaw() {
            return new OpenClaw();
        }
    }
    public  class extend {
        public class ExtendForward implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                targetPos = RobotConfig.EXT_BASKET;//make it full slide extend
                RobotConfig.slideExtension.setTargetPosition(targetPos);
                RobotConfig.slideExtension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RobotConfig.slideExtension.setVelocity(3000);

                return false;
            }


        }
        public Action extendForward() {
            return new extend.ExtendForward();
        }

        public class ExtendBackwards implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                targetPos = RobotConfig.EXT_REG;//make it full slide extend
                RobotConfig.slideExtension.setTargetPosition(targetPos);
                RobotConfig.slideExtension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                RobotConfig.slideExtension.setVelocity(3000);
                return false;
            }


        }
        public Action extendBackwards() {
            return new extend.ExtendBackwards();
        }

    }

    @Override
    public void runOpMode() {
        RobotConfig.initialize(hardwareMap);


        Pose2d initialPose = new Pose2d(11, 60, Math.toRadians(-  90));
        SparkFunOTOSDrive drive = new SparkFunOTOSDrive(hardwareMap, initialPose);
//        instances
        Claw claw = new Claw();
        Lift lift = new Lift();
        wrist wrist = new wrist();
        extend extend = new extend();
//        @TODO trajectorty


        //drive to basket
        TrajectoryActionBuilder DriveToBaseket = drive.actionBuilder(initialPose)
                .setTangent(0)
                .lineToXSplineHeading(40, 5*Math.PI / 4)
                .strafeTo(new Vector2d(58, 58))
                .waitSeconds(4);

        //go back after grabbing the sample
        TrajectoryActionBuilder MoveBackToBasket = drive.actionBuilder(new Pose2d(48, 45,90))
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(56, 56), Math.PI / 2);

        TrajectoryActionBuilder FirstSample = drive.actionBuilder(new Pose2d(56, 56,225))
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(48, 38), Math.PI / 2)
                .turn(Math.toRadians(45));
//
//        TrajectoryActionBuilder SecondSample = drive.actionBuilder(new Pose2d(56, 56,225))
//                .setTangent(0)
//                .splineToConstantHeading(new Vector2d(48, 38), Math.PI / 2)
//                .turn(Math.toRadians(45));
//
//        TrajectoryActionBuilder ThridSample = drive.actionBuilder(new Pose2d(56, 56,225))
//                .setTangent(0)
//                .splineToConstantHeading(new Vector2d(48, 38), Math.PI / 2)
//                .turn(Math.toRadians(45));

        //park at rung
        Action trajectoryActionCloseOut = DriveToBaseket.endTrajectory().fresh()
                 .setTangent(Math.PI/2)
                .lineToYSplineHeading(40, Math.toRadians(270))
                .setTangent(0)
                .lineToX(54)
                .splineToConstantHeading(new Vector2d(30, 5), Math.PI / 2)
                .build();
        // actions that need to happen on init; for instance, a claw tightening.
        Actions.runBlocking(claw.closeClaw());
        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("We're the goats don't worry drivers, WE ARE THEM!");
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;



        Action driveToBaseket = DriveToBaseket.build();
        Action moveBackToBasket = MoveBackToBasket.build();
        Action firstSample = FirstSample.build();
        Actions.runBlocking(
                new SequentialAction(
//                        drops preplaced sample after arriving to basket
                        lift.liftUp(),
                        extend.extendForward(),
                        wrist.wristUp(),
                        driveToBaseket,
                        claw.openClaw(),
//                        retracts
                        lift.liftDown(),
                        extend.extendBackwards(),
                        wrist.wristDown(),
//                        goes to teh fiurst sample and picks up
//                        claw.closeClaw(),
//                        lift.liftUp(),
//                        goes back to basket and score
//                        moveBackToBasket,
//                        claw.openClaw(),
//                      @TODO more add second and third possibly a fourth
                        trajectoryActionCloseOut
                )
        );
    }
}