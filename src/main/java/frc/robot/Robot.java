// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final RomiDrivetrain m_drivetrain = new RomiDrivetrain();
  private final RomiGyro m_gyro = new RomiGyro();

  // This line creates a new controller object, which we can use to get inputs
  // from said controller/joystick.
  private GenericHID controller = new GenericHID(0);
  private Servo servo1 = new Servo(2);
  public double adjConstant = 0;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    SmartDashboard.putNumber("Adjustmant Constant", adjConstant);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    m_drivetrain.resetEncoders();
  }

  public void arcadeDrive(double xaxisSpeed, double zaxisRotate) {
  }

  /** This function is called periodically during autonomous. */

  // initial state of the robot (each state will have a specicfic function like
  // state 1 will drive forward)
  float state = 0;
  //scanning for blue block
  public void turnaround(){
    if (m_drivetrain.getLeftDistanceInch() - m_drivetrain.getRightDistanceInch()<
    13.5/2){
      m_drivetrain.tankDrive(0.5, -0.5);
    }
    if (m_drivetrain.getLeftDistanceInch() - m_drivetrain.getRightDistanceInch()<
      13.5){
      m_drivetrain.tankDrive(-0.5, 0.5);

    }
  }


  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here

        break;
      case kDefaultAuto:
      default:
        // Put defult auto code here
 // driving straight (you can fix the drift of the romi by adjusting the left or
          // right wheel wheel speed)
        if (state == 0 && m_drivetrain.getLeftDistanceInch() <= 72) {
          m_drivetrain.tankDrive(1, 1);
        }

      //turning 180
         if (state == 1 && m_drivetrain.getLeftDistanceInch() - m_drivetrain.getRightDistanceInch()<
         13.5){
         m_drivetrain.tankDrive(0.5, -0.5);
         }
//scanning for blue block
//(state ===2 && vision does not detect blue block){...}
         if (state ==2){
           //calibrate gyro
           turnaround();

         }
        
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */

  double first_press_angle;

  @Override
  public void teleopPeriodic() {
    // WARN: The controller code is written based on my controller, and as such may need to be changed

    // The getRawAxis method allows one to get the value an axis is on
    // We use axis to get stuff from the joysticks, as it is easy to represent a joystick
    // like a coordinate grid, which allows us to just extract the x or y axis information from it.
    double forwardSpeed = controller.getRawAxis(1);
    double turnSpeed = controller.getRawAxis(0);
    m_drivetrain.tankDrive(forwardSpeed, turnSpeed);
    // boolean servo_button_pressed = controller. (1);
    // if (servo_button_pressed) {  
    //   servo1.set(1);
    // } else {
    //   servo1.set(0);
    // }
    // boolean drive_straight_button_pressed_down = controller.getRawButtonPressed(2);
    // boolean drive_straight_button_pressed = controller.getRawButton(2);

    // if (drive_straight_button_pressed_down) {
    //   first_press_angle = m_gyro.getAngleZ();
    //   System.out.println(1);
    // }

    // if (drive_straight_button_pressed) {
    //   double change_in_turn = first_press_angle * SmartDashboard.getNumber("Adjustmant Constant", 1);
    //   double current_angle = m_gyro.getAngleZ() - change_in_turn;
    //   m_drivetrain.arcadeDrive(forwardSpeed, current_angle);
    //   //Angle that you are at - the angle that you want to be at 
    //   //multiply this by one constant 
    // } else {
    //   m_drivetrain.arcadeDrive(-forwardSpeed, turnSpeed);
    // }
  
    
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
}
