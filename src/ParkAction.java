

public class ParkAction extends Action {
	private int parkSteps, direction;
	private int forwardAmount = Driver.MOTOR_PARK;
	private int stepsTaken = 0, lastIncident = 0;
	private boolean isSlow, isRound;
	
	public ParkAction(Driver d, int dir, int t) {
		super(d, Action.LEFT_PARK_ACTION);
		direction = dir;
		parkSteps = t;
		isSlow = driver.getSlow(driver.getCurrentPath());
	}
	
	public void act(int leftColor, int rightColor) {
		if (step==0) {
			System.out.println(stepsTaken);
			if (leftColor==ColorIdentifier.BLACK && rightColor==ColorIdentifier.BLACK) {
				leftMotor = rightMotor = (isSlow) ? 0.5f : 1f;
				nextAction = this;
			}
			else if (leftColor==ColorIdentifier.WHITE
					|| leftColor==ColorIdentifier.YELLOW
					|| leftColor==ColorIdentifier.GRAY
					|| leftColor==ColorIdentifier.BLUE
					|| leftColor==ColorIdentifier.RED) {
				System.out.println("CL");
				nextAction = new CorrectionAction(driver,this,lastIncident,-1,isRound);
				lastIncident = 0;
			}
			else if (rightColor==ColorIdentifier.WHITE
					|| rightColor==ColorIdentifier.YELLOW
					|| rightColor==ColorIdentifier.GRAY
					|| rightColor==ColorIdentifier.BLUE
					|| rightColor==ColorIdentifier.RED) {
				System.out.println("CR");
				nextAction = new CorrectionAction(driver,this,lastIncident,1,isRound);
				lastIncident = 0;
			}
			
			lastIncident++;
			stepsTaken++;
			
			if (stepsTaken>parkSteps) {
				pivot = false;
				rotate = 0;
				leftMotor = 0;
				rightMotor = 0;
				step++;
			}
		}
		else if (step==1) {
			pivot = true;
			rotate = (direction<0) ? 90 : -90;
			leftMotor = 1;
			rightMotor = 1;
			step++;
		}
		else if (step==2) { // Blue line
			pivot = false;
			rotate = 0;
			leftMotor = 1;
			rightMotor = 1;
			
			if (leftColor==ColorIdentifier.BLUE && rightColor==ColorIdentifier.BLUE) {
				step+=2;
			}
			else if (leftColor==ColorIdentifier.BLUE || rightColor==ColorIdentifier.BLUE) {
				step+=2;
			}
		}
		else if (step==3) { // Align
			pivot = false;
			rotate = 0;
			leftMotor = (rightColor==ColorIdentifier.BLUE || rightColor==ColorIdentifier.WHITE) ? 0 : 0.5f;
			rightMotor = (leftColor==ColorIdentifier.BLUE || leftColor==ColorIdentifier.WHITE) ? 0 : 0.5f;
			
			if ((leftColor==ColorIdentifier.BLUE || leftColor==ColorIdentifier.WHITE)
					&& (rightColor==ColorIdentifier.BLUE || rightColor==ColorIdentifier.WHITE)) {
				step++;
			}
		}
		else if (step==4) { // White line
			pivot = false;
			rotate = 0;
			leftMotor = 1;
			rightMotor = 1;

			if (leftColor==ColorIdentifier.WHITE || rightColor==ColorIdentifier.WHITE) {
				leftMotor = 0;
				rightMotor = 0;
				step++;
			}
		}
		else if (step==5) { // Park
			pivot = false;
			rotate = -50;
			leftMotor = 1;
			rightMotor = 1;
			step++;
		}
		else if (step==6) {
			System.out.println("park");
			driver.leftMotor.stop();
			driver.rightMotor.stop();
			pivot = false;
			rotate = 0;
			leftMotor = 0;
			rightMotor = 0;
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			step++;
		}
		else if (step==7) { // Blue line
			pivot = false;
			rotate = 0;
			leftMotor = -1;
			rightMotor = -1;

			if ((leftColor==ColorIdentifier.BLUE) || (rightColor==ColorIdentifier.BLUE)) {
				step++;
			}
		}
		else if (step==8) {
			pivot = false;
			rotate = -forwardAmount;
			leftMotor = 1;
			rightMotor = 1;
			step++;
		}
		else if (step==9) { // Rotate
			pivot = true;
			rotate = (direction<0) ? -90 : 90;
			leftMotor = 1;
			rightMotor = 1;
			step++;
		}
		else {
			driver.notifyPathCompletion();
		}
	}

}
