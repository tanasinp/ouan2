package entity;

public class Entity {

	public String pastDirection ;
	public String direction ;
	
	private final double walkSpeed = 2; //when walk only
    private final double jumpSpeedX = 3; //when jump only
    
	public double getWalkSpeed() {
		return walkSpeed;
	}
	public double getJumpSpeedX() {
		return jumpSpeedX;
	}
	
}
