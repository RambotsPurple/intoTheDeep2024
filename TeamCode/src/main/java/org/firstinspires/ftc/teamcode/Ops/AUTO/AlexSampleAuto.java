package org.firstinspires.ftc.teamcode.Ops.AUTO;

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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.RR.SparkFunOTOSDrive;

@Config
@Autonomous(name = "Alex's Sample Auto ", group = "Autonomous")
public class AlexSampleAuto extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    int targetPos = 0;
    int targetPos1 = -1;

    public class wrist{
        public class WristUp implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.wrist1.setPosition(0.25);
                return false;
            }
        }
        public  Action wristUp() {
            return new WristUp();
        }

        public class WristDown implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.wrist1.setPosition(0.8);
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
                telemetry.addLine("running lift");
                telemetry.update();
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

        public class extendForward implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                telemetry.addLine("running ext");
                telemetry.update();
                if (!initialized) {
//                    targetPos = RobotConfig.EXT_BASKET;
//                    RobotConfig.slideExtension.setTargetPosition(targetPos);
//                    RobotConfig.slideExtension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                    RobotConfig.slideExtension.setVelocity(1000);

                    targetPos = RobotConfig.EXT_BASKET; //make it full slide extend
                    RobotConfig.slideExtension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    runtime.reset();
                    while (RobotConfig.slideExtension.getCurrentPosition() > targetPos) {
                        RobotConfig.slideExtension.setPower(-1);
                        telemetry.addData("ext pos:", RobotConfig.slideExtension.getCurrentPosition());
                        telemetry.update();
                        if (runtime.seconds() > 2.8) {
                            break;

                        }
                    }
                    initialized = true;
//                    RobotConfig.slideExtension.setPower(0);
                }

                double pos = RobotConfig.slideExtension.getCurrentPosition();
                packet.put("liftPos", pos);
                if (pos < RobotConfig.EXT_BASKET) {
                    return true;
                } else {
                    targetPos1 = 0;
                    RobotConfig.slideExtension.setPower(targetPos);
                    RobotConfig.slideExtension.setPower(targetPos);
                    return false;
                }
            }
        }

        public class ExtendForward implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                RobotConfig.slideExtension.setPower(0.7);
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 2.0)) {
                    telemetry.addData("Path", "Extend", runtime.seconds());
                    telemetry.update();
                }

                        targetPos = RobotConfig.EXT_BASKET; //make it full slide extend
                        RobotConfig.slideExtension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                        while (RobotConfig.slideExtension.getCurrentPosition() > targetPos) {
                            RobotConfig.slideExtension.setPower(-1);
                            telemetry.addData("ext pos:", RobotConfig.slideExtension.getCurrentPosition());
                            telemetry.update();
                        }
        //                RobotConfig.slideExtension.setPower(0);

                        targetPos = RobotConfig.EXT_BASKET;
                        RobotConfig.slideExtension.setTargetPosition(targetPos);
                        RobotConfig.slideExtension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        RobotConfig.slideExtension.setVelocity(1000);
                return false;
            }


        }
        public Action extendForward() {
            return new extend.extendForward();
        }

        public class ExtendBackwards implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
//                RobotConfig.slideExtension.setPower(-0.7);
//                runtime.reset();
//                while (opModeIsActive() && (runtime.seconds() < 2.0)) {
//                    telemetry.addData("Path", "Extend", runtime.seconds());
//                    telemetry.update();
//                }
                targetPos = 0; //make it full slide extend
                RobotConfig.slideExtension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                runtime.reset();
                while (RobotConfig.slideExtension.getCurrentPosition() < 0) {
                    RobotConfig.slideExtension.setPower(1);
                    if (runtime.seconds() > 2.8) {
                        break;
                    }
                }
                RobotConfig.slideExtension.setPower(0);


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


        Pose2d initialPose = new Pose2d(-11.5, -60, Math.toRadians(90));
        Pose2d basketPose = new Pose2d(-55, -54, Math.toRadians(225));
        SparkFunOTOSDrive drive = new SparkFunOTOSDrive(hardwareMap, initialPose);
//        instances
        Claw claw = new Claw();
        Lift lift = new Lift();
        wrist wrist = new wrist();
        extend extend = new extend();
//        @TODO FIX THE CLAW/ARM


        //drive to basket
        TrajectoryActionBuilder ToBasket = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-55, -54), Math.toRadians(225))
                .waitSeconds(2);


        TrajectoryActionBuilder FirstSample = drive.actionBuilder(basketPose)
                .strafeToLinearHeading(new Vector2d(-48, -38), Math.toRadians(90))
                .waitSeconds(2);




        Action park = ToBasket.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-55, -54), Math.toRadians(225))

                .build();

        // actions that need to happen on init; for instance, a claw tightening.
        Actions.runBlocking(claw.closeClaw());
        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("We're the goats don't worry drivers, WE ARE THEM!");
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;

        Action toBasket = ToBasket.build();
        Action firstSample = FirstSample.build();
        Actions.runBlocking(
                new SequentialAction(
                        lift.liftUp(),
                        wrist.wristDown(),
                        toBasket,
                        extend.extendForward(),
                        wrist.wristUp(),
                        firstSample,
                        claw.openClaw(),
                        park
                )
        );
    }
}