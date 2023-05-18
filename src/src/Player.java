package src;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ability.ChargeAble;
import ability.WalkAble;

public class Player implements WalkAble,ChargeAble{
//    private Node player;
    private HashMap<KeyCode, Boolean> keys;
    private int levelWidth;
	private ArrayList<Node> platforms = new ArrayList<>();
	private Point2D playerVelocity = new Point2D(0, 0);
	private ImageView player =  new ImageView(new Image(getClass().getResourceAsStream("/res/player_currentRight.png")));
	private ArrayList<Node> coins = new ArrayList<Node>();
	private boolean dialogEvent = false;
	
	public String pastDirection = "right";
	public String direction;
	
	boolean collideLeft ;
	boolean collideRight;
	
	public boolean isJump = false;
	public boolean isCharge = false;
	public  boolean isWalk = true;
	public boolean isOnGround = true;
	public boolean isFalling = false;
	
	protected int chargeTime = 0;
	protected final int maxChargeTime = 50;
	protected final double gravity = 0.9;
    private final double xVelocity = 6; // fixed // when they jump and in the air
    protected double yVelocity; // up 
    
    private double walkSpeed = 3; //when walk only
	
	Timeline walkRightAnimation = new Timeline();
    Timeline walkLeftAnimation = new Timeline();
    
    KeyFrame frameRight1 = new KeyFrame(Duration.millis(10), e -> {
    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_currentRight.png")));
    });
    KeyFrame frameRight2 = new KeyFrame(Duration.millis(10), e -> {
    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_walk1.png")));
    });
    KeyFrame frameRight3 = new KeyFrame(Duration.millis(110), e -> {
    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_walkRight1.5.png")));
    });
    KeyFrame frameRight4 = new KeyFrame(Duration.millis(210), e -> {
    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_walk2.png")));
    });
    
    KeyFrame frameLeft1 = new KeyFrame(Duration.millis(10), e -> {
    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_currentLeft.png")));
    });
    KeyFrame frameLeft2 = new KeyFrame(Duration.millis(10), e -> {
    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_walkLeft1.png")));
    });
    KeyFrame frameLeft3 = new KeyFrame(Duration.millis(110), e -> {
    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_walkLeft1.5.png")));
    });
    KeyFrame frameLeft4 = new KeyFrame(Duration.millis(210), e -> {
    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_walkLeft2.png")));
    });
    
    
	public Player(Pane gameRoot, HashMap<KeyCode, Boolean> keys) {
        this.keys = keys;
        levelWidth = LevelData.LEVEL1[0].length() * 60;
        player = new ImageView(new Image(getClass().getResourceAsStream("/res/player_currentRight.png")));
        player.setFitWidth(40);
        player.setFitHeight(40);
        player.setTranslateX(60);
        player.setTranslateY(45);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
            }
        });
//        player.translateYProperty().addListener((obs, old, newValuey) -> {
//            int offsety = newValuey.intValue();
//
//            if (offsety > 360 && offsety < levelWidth - 360) {
//                gameRoot.setLayoutX(-(offsety - 360));
//            }
//        });

        for (int i = 0; i < LevelData.LEVEL1.length; i++) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j * 60, i * 60, 60, 60, "/res/wall.png");
                        platforms.add(platform);
                        break;
                    case '2':
                        Node coin = createEntity(j*60, i*60, 60, 60, "/res/treasure.png");
                        coins.add(coin);
                        break;
                }
            }
        }
        
        gameRoot.getChildren().add(player);
        gameRoot.getChildren().addAll(platforms);
        gameRoot.getChildren().addAll(coins);
    }

    public void update(Pane gameRoot) {
    	if (!this.isJump && !this.isFalling) { //when player jumping and falling, player cann't walk
    		if ((isPressed(KeyCode.A) || isPressed(KeyCode.D)) && !isPressed(KeyCode.SPACE)) { // if it press spacebar,will not walk
    			if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
    				walkLeft();
//    				walkRightAnimation.stop();
    				this.isWalk = true;
    				direction = "left";
    				pastDirection = "left";
    		    } else if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) {
    		    	walkRight();
//    		    	walkLeftAnimation.stop();
    		    	this.isWalk = true;
    		    	direction = "right";
    		    	pastDirection = "right";
    		    }
        	} else if ((isPressed(KeyCode.SPACE) || 
    				((isPressed(KeyCode.A) || isPressed(KeyCode.D)) && isPressed(KeyCode.SPACE))) && player.getTranslateY() >= 5) { // when you press a spacebar, player is goning to charge
    		    if (!this.isJump) {
//    		    	walkLeftAnimation.stop();
//    				walkRightAnimation.stop();
    		    	chargeTime++;
    		        isCharge = true;
    		        isWalk = false;
    		        walkRightAnimation.stop();
    		        walkLeftAnimation.stop();
    		        player.setImage(new Image(getClass().getResourceAsStream("/res/player_jump.png")));
    	            if (isPressed(KeyCode.A) && isCharge) { 
    	            	direction = "chargeLeft";
    	            } else if (isPressed(KeyCode.D) && isCharge) {
    	            	direction = "chargeRight";
    	            } else {
    	            	direction = "charge";
    	            }
    		    }
    		} else if (!isPressed(KeyCode.A) && !isPressed(KeyCode.D)) { // set image when not walk
//    			walkLeftAnimation.stop();
//    			walkRightAnimation.stop();
    		    if (pastDirection.equals("right")) {
    		    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_currentRight.png")));
    		    } else if (pastDirection.equals("left")){
    		    	player.setImage(new Image(getClass().getResourceAsStream("/res/player_currentLeft.png")));
    		    }
    		    isCharge = false;
    		    isWalk = false;
    		}
    	}
    	
    	if (this.isCharge) {
    		charge();
    	}
    	
    	
    	if (this.isJump) {
    		if (direction.equals("chargeLeft") || direction.equals("left") || direction.equals("jumpLeft")) { //jump go to left
    			direction = "jumpLeft";
    			//this is check collide like walkLeft
    			for (int i = 0; i < 5; i++) {
    	            boolean collided = false;
    	            player.setTranslateX(player.getTranslateX() - 1);
    	            for (Node platform : platforms) {
    	                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
    	                    if (player.getTranslateX() == platform.getTranslateX() + 60) {
    	                        collided = true;
    	                        break;
    	                    }
    	                }
    	            }
    	            if (collided) {
    	            	direction = "jumpRight"; // change direction when collide
    	            	player.setTranslateX(player.getTranslateX() - 1); // Move the player back to the previous position
    	                break;
    	            }
    			}
    	    	if (playerVelocity.getY() > 0) {
    	    		direction = "downLeft";
    	    		this.isJump = false;
    	    		this.isFalling = true; // go to Falling
    	    		player.setImage(new Image(getClass().getResourceAsStream("/res/player_downLeft.png")));
    	    	} else {
    	    		player.setImage(new Image(getClass().getResourceAsStream("/res/player_up2.png.png")));   
    	    	}
    	    } else if (direction.equals("chargeRight") || direction.equals("right") ||  direction.equals("jumpRight")) { //jump go to right
    	    	direction = "jumpRight";
    	    	//this is check collide like walkRight
    	    	for (int i = 0; i < 5; i++) {
    	            boolean collided = false;
    	            player.setTranslateX(player.getTranslateX() + 1);
    	            for (Node platform : platforms) {
    	                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
    	                    if (player.getTranslateX() + 40 == platform.getTranslateX()) {
    	                        collided = true;
    	                        break;
    	                    }
    	                }
    	            }
    	            if (collided) {
    	            	direction = "jumpLeft"; // change direction when collide
    	            	player.setTranslateX(player.getTranslateX() + 1); // Move the player back to the previous position
    	                break;
    	            }
    			}
    	    	if (playerVelocity.getY() > 0) {
    	    		this.isFalling = true; // go to Falling
    	    		this.isJump = false; 
    	    		direction = "downRight"; 
    	    		player.setImage(new Image(getClass().getResourceAsStream("/res/player_down.png")));
    	    	} else {
    	    		player.setImage(new Image(getClass().getResourceAsStream("/res/player_up.png")));
    	    	}
    	    } else { // jump in Y-Axis only
    	    	direction = "jump";
    	    	if (pastDirection.equals("left")) {
    	    		if (playerVelocity.getY() > 0) {
    	    			this.isFalling = true;
        	    		this.isJump = false;
        	    		direction = "down";
    	    			player.setImage(new Image(getClass().getResourceAsStream("/res/player_downLeft.png")));
        	    	} else {
        	    		player.setImage(new Image(getClass().getResourceAsStream("/res/player_up2.png.png")));
        	    	}
    	    	} else {
    	    		if (playerVelocity.getY() > 0) {
    	    			this.isFalling = true;
        	    		this.isJump = false;
        	    		direction = "down";
    	    			player.setImage(new Image(getClass().getResourceAsStream("/res/player_down.png")));
        	    	} else {
        	    		player.setImage(new Image(getClass().getResourceAsStream("/res/player_up.png")));
        	    	}
    	    	}
    	    }
    		
    	}
    		
    	if (isFalling) { 
	        if (direction.equals("jumpRight") || direction.equals("downRight")) {
	        	direction = "downRight";
	        	for (int i = 0; i < 5; i++) {
    	            boolean collided = false;
    	            player.setTranslateX(player.getTranslateX() + 1);
    	            for (Node platform : platforms) {
    	                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
    	                    if (player.getTranslateX() + 40 == platform.getTranslateX()) {
    	                        collided = true;
    	                        break;
    	                    }
    	                }
    	            }
    	            if (collided) {
    	            	direction = "downLeft";
    	            	player.setTranslateX(player.getTranslateX() + 1); // Move the player back to the previous position
    	                break;
    	            }
    			}
	        	
//	        	player.setTranslateX(player.getTranslateX() + 6);
	            player.setImage(new Image(getClass().getResourceAsStream("/res/player_down.png")));
			} else if (direction.equals("jumpLeft") || direction.equals("downLeft")) {
				direction = "downLeft";
				for (int i = 0; i < 5; i++) {
    	            boolean collided = false;
    	            player.setTranslateX(player.getTranslateX() - 1);
    	            for (Node platform : platforms) {
    	                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
    	                    if (player.getTranslateX() == platform.getTranslateX() + 60) {
    	                        collided = true;
    	                        break;
    	                    }
    	                }
    	            }
    	            if (collided) {
    	            	direction = "downRight";
    	            	player.setTranslateX(player.getTranslateX() - 1); // Move the player back to the previous position
    	                break;
    	            }
    			}
				
				
//				player.setTranslateX(player.getTranslateX() - 6);
	            player.setImage(new Image(getClass().getResourceAsStream("/res/player_downLeft.png")));
//				direction = "downLeft";
			} 
			else if (direction.equals("jump") || direction.equals("down")) {
				direction = "down";
				if (pastDirection.equals("left")) {
					player.setImage(new Image(getClass().getResourceAsStream("/res/player_downLeft.png")));
				} else if (pastDirection.equals("right")) {
					player.setImage(new Image(getClass().getResourceAsStream("/res/player_down.png")));
				}
			}	
		}
   
    	
        if (playerVelocity.getY() < 10) {
            playerVelocity = playerVelocity.add(0, 1);
        }

        movePlayerY((int)playerVelocity.getY()); // like gravity
        
        
        for (Node coin : coins) {
            if (player.getBoundsInParent().intersects(coin.getBoundsInParent())) {
            	coin.getProperties().put("alive", false);
                dialogEvent = true;
                System.out.println("congrat");
            }
        }
        
        for (Iterator<Node> it = coins.iterator(); it.hasNext(); ) {
            Node coin = it.next();
            if (!(Boolean)coin.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(coin);
            }
        }
    }
    
    public void charge() {
    	if (chargeTime >= maxChargeTime) {
		    yVelocity = 7 + Math.pow(chargeTime/5, 1.5);
		    chargeTime = 0;// clear chargeTime
		    isJump = true;
		    isCharge = false;
		    isWalk = false;
		    playerVelocity = playerVelocity.add(0, -yVelocity);
		} else if (chargeTime > 0 && !isPressed(KeyCode.SPACE)) {
			yVelocity = 7 + Math.pow(chargeTime/5, 1.5);//1.13
			chargeTime = 0;// clear chargeTime
		    this.isJump = true; 
		    isCharge = false;
		    isWalk = false;	
		    playerVelocity = playerVelocity.add(0, -yVelocity);
		}
    }

    public void walkLeft() {
//        this.walkLeftAnimation.setCycleCount(Timeline.INDEFINITE); // it will repeat indefinitely.
//        this.walkLeftAnimation.getKeyFrames().addAll(frameLeft2, frameLeft3, frameLeft4); //These key frames define the animation frames to be displayed during the animation.
        for (int i = 0; i < 5; i++) { //to simulate the movement of the player to the left in small increments.
            boolean collided = false;
            player.setTranslateX(player.getTranslateX() - 1); //moves the player one unit to the left
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (player.getTranslateX() == platform.getTranslateX() + 60) { //the player has reached the left edge of the platform
                        collided = true;
                        break;
                    }
                }
            }
            if (collided) {
                player.setTranslateX(player.getTranslateX() + 1); // Move the player back to the previous position
                break;
            }
//            walkLeftAnimation.play();//playing the animation
        }
    }

    public void walkRight() {
//        this.walkRightAnimation.setCycleCount(Timeline.INDEFINITE); // it will repeat indefinitely.
//        this.walkRightAnimation.getKeyFrames().addAll(frameRight2, frameRight3, frameRight4); //These key frames define the animation frames to be displayed during the animation.
        for (int i = 0; i < 5; i++) { //to simulate the movement of the player to the left in small increments.
            boolean collided = false;
            player.setTranslateX(player.getTranslateX() + 1); //moves the player one unit to the right
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (player.getTranslateX() + 40 == platform.getTranslateX()) { //the player has reached the right edge of the platform
                        collided = true;
                        break;
                    }
                }
            }
            if (collided) {
                player.setTranslateX(player.getTranslateX() - 1); // Move the player back to the previous position
                break;
            }
//            walkRightAnimation.play(); //playing the animation
        }
    }

    private void movePlayerY(int value) { //responsible for handling the player's vertical movement	
        boolean movingDown = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {          	
            	 if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                    	this.isJump = false;
                        if (player.getTranslateY() + 40 == platform.getTranslateY()) { // check collide Bottom
                            player.setTranslateY(player.getTranslateY() - 1); //This helps the player ascend vertically on the screen
                            this.isJump = false; 
                            this.isFalling = false;
                            return;
                        } 
                    }
                    else {//moving up
                        if (player.getTranslateY() == platform.getTranslateY() + 60) { //check collide top
                        	this.isFalling = true; // go to falling
                        	playerVelocity = new Point2D(0, 0); 
                            return;
                        } 
                    }
                } 
            }	            
            if (movingDown) {
                player.setTranslateY(player.getTranslateY() + 1);
            } else {
                player.setTranslateY(player.getTranslateY() - 1);
            }
        }
    }


    private Node createEntity(double x, double y, double w, double h, String path) {
		Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        Image image = new Image(path);
        entity.setFill(new ImagePattern(image));
        entity.getProperties().put("alive", true);
        return entity;
	}

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

	public boolean isDialogEvent() {
		return dialogEvent;
	}

	public void setDialogEvent(boolean dialogEvent) {
		this.dialogEvent = dialogEvent;
	}
    
    
    
    
    
}
