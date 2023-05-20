package entity;

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
import src.LevelData;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ability.ChargeAble;
import ability.WalkAble;

public class Player extends Entity implements WalkAble,ChargeAble{

	//    private Node player;
    private HashMap<KeyCode, Boolean> keys;
    private int levelWidth;
    private int levelHeight;
	private Point2D playerVelocity = new Point2D(0, 0);
	private ImageView player;// =  new ImageView(new Image(getClass().getResourceAsStream("/res/player_currentRight.png")));
	private ArrayList<Node> platforms = new ArrayList<>();
	private ArrayList<Node> coins = new ArrayList<Node>();
	private ArrayList<Node> spikes = new ArrayList<>();
	private boolean dialogEvent;
	private int count;
	private boolean isDeath ;
	
	public boolean isJump ;
	public boolean isCharge ;
	public  boolean isWalk ;
	public boolean isFalling ;
	
	private int chargeTime = 0;
	private final int maxChargeTime = 50;
	
	//protected final double gravity = 0.9;
    private double yVelocity; // up 
    private double maxGravity = 10;
    
    public int spriteCounter = 0;
	public int spriteNum = 1;
    
	public Player(Pane gameRoot, HashMap<KeyCode, Boolean> keys) {
        this.keys = keys;
        levelWidth = LevelData.LEVEL1[0].length() * 60;
        levelHeight = LevelData.LEVEL1.length * 60;
//        player = new ImageView(new Image(getClass().getResourceAsStream("/res/player_currentRight.png")));
//        player.setFitWidth(40);
//        player.setFitHeight(40);
//        player.setTranslateX(80);
//        player.setTranslateY(60);
        setDefaultValues(gameRoot);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offsetX = newValue.intValue();
            int offsetY = (int) player.getTranslateY(); // Get the current translateY value

            if (offsetX > 640 && offsetX < levelWidth - 640) {
                gameRoot.setLayoutX(-(offsetX - 640));
            }

            if (offsetY > 380 && offsetY < levelHeight - 380) {
                gameRoot.setLayoutY(-(offsetY - 380));
            }
        });




        for (int i = 0; i < LevelData.LEVEL1.length; i++) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j * 60, i * 60, 60, 60, "map/block.png");
                        platforms.add(platform);
                        break;
                    case '2':
                        Node coin = createEntity(j * 60, i * 60, 60, 60, "map/treasure.png");
                        coins.add(coin);
                        break;
                    case '3':
                    	Node spike = createEntity(j * 60, i * 60, 60, 60, "map/spike.png");
                    	spikes.add(spike);
                }
            }
        }
        
        gameRoot.getChildren().add(player);
        gameRoot.getChildren().addAll(platforms);
        gameRoot.getChildren().addAll(coins);
        gameRoot.getChildren().addAll(spikes);
    }
	
	public void setDefaultValues(Pane gameRoot) {
//		System.out.println(ClassLoader.getSystemResource("map/spike.png"));
		player = new ImageView(new Image(ClassLoader.getSystemResource("player/player2_currentRight.png").toString()));
        direction = "right";
        pastDirection = "right";
        player.setFitWidth(40);
        player.setFitHeight(40);
        player.setTranslateX(80);
        player.setTranslateY(23 * 60);
        gameRoot.setLayoutX(0);
        gameRoot.setLayoutY(-780);
        isWalk = true;
        isJump = false;
        isCharge = false;
        isFalling = false;
        isDeath = false;
        dialogEvent = false;
        
	}

    public void update(Pane gameRoot) {
    	
    	updateWalkImage();
    	
    	if (!this.isJump && !this.isFalling) { //when player jumping and falling, player cann't walk
    		if ((isPressed(KeyCode.A) || isPressed(KeyCode.D)) && !isPressed(KeyCode.SPACE)) { // if it press space bar,will not walk
    			if (isPressed(KeyCode.A) ) { //&& player.getTranslateX() >= 5
    				walkLeft();
    				this.isWalk = true;
    				direction = "left";
    				pastDirection = "left";
    		    } else if (isPressed(KeyCode.D) ) { //&& player.getTranslateX() + 40 <= levelWidth - 5
    		    	walkRight();
    		    	this.isWalk = true;
    		    	direction = "right";
    		    	pastDirection = "right";
    		    }
        	} 
    		else if ((isPressed(KeyCode.SPACE) || 
    				((isPressed(KeyCode.A) || isPressed(KeyCode.D)) && isPressed(KeyCode.SPACE))) ) { // when you press a space bar, player is going to charge
    		    if (!this.isJump ) {//&& player.getTranslateY() >= 5
    		    	chargeTime++;
    		        isCharge = true;
    		        isWalk = false;
    		        player.setImage(new Image(ClassLoader.getSystemResource("player/player2_charge.png").toString()));
    	            if (isPressed(KeyCode.A) && isCharge) { 
    	            	direction = "chargeLeft";
    	            } else if (isPressed(KeyCode.D) && isCharge) {
    	            	direction = "chargeRight";
    	            } else {
    	            	direction = "charge";
    	            }
    		    }
    		} else if (!isPressed(KeyCode.A) && !isPressed(KeyCode.D)) { // set image when not walk
    		    if (pastDirection.equals("right")) {
    		    	player.setImage(new Image(ClassLoader.getSystemResource("player/player2_currentRight.png").toString()));
    		    } else if (pastDirection.equals("left")){
    		    	player.setImage(new Image(ClassLoader.getSystemResource("player/player2_currentLeft.png").toString()));
    		    }
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
    			for (int i = 0; i < this.getJumpSpeedX(); i++) {
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
    	    		player.setImage(new Image(ClassLoader.getSystemResource("player/player2_downLeft.png").toString()));
    	    	} else {
    	    		player.setImage(new Image(ClassLoader.getSystemResource("player/player2_upLeft.png").toString()));   
    	    	}
    	    } else if (direction.equals("chargeRight") || direction.equals("right") ||  direction.equals("jumpRight")) { //jump go to right
    	    	direction = "jumpRight";
    	    	//this is check collide like walkRight
    	    	for (int i = 0; i < this.getJumpSpeedX(); i++) {
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
    	    		player.setImage(new Image(ClassLoader.getSystemResource("player/player2_downRight.png").toString()));
    	    	} else {
    	    		player.setImage(new Image(ClassLoader.getSystemResource("player/player2_upRight.png").toString()));
    	    	}
    	    } else { // jump in Y-Axis only
    	    	direction = "jump";
    	    	if (pastDirection.equals("left")) {
    	    		if (playerVelocity.getY() > 0) {
    	    			this.isFalling = true;
        	    		this.isJump = false;
        	    		direction = "down";
    	    			player.setImage(new Image(ClassLoader.getSystemResource("player/player2_downLeft.png").toString()));
        	    	} else {
        	    		player.setImage(new Image(ClassLoader.getSystemResource("player/player2_upLeft.png").toString()));
        	    	}
    	    	} else {
    	    		if (playerVelocity.getY() > 0) {
    	    			this.isFalling = true;
        	    		this.isJump = false;
        	    		direction = "down";
    	    			player.setImage(new Image(ClassLoader.getSystemResource("player/player2_downRight.png").toString()));
        	    	} else {
        	    		player.setImage(new Image(ClassLoader.getSystemResource("player/player2_upRight.png").toString()));
        	    	}
    	    	}
    	    }
    	}
    		
    	if (isFalling) { 
	        if (direction.equals("jumpRight") || direction.equals("downRight")) {
	        	direction = "downRight";
	        	for (int i = 0; i < this.getJumpSpeedX(); i++) {
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
	            player.setImage(new Image(ClassLoader.getSystemResource("player/player2_downRight.png").toString()));
			} else if (direction.equals("jumpLeft") || direction.equals("downLeft")) {
				direction = "downLeft";
				for (int i = 0; i < this.getJumpSpeedX(); i++) {
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
	            player.setImage(new Image(ClassLoader.getSystemResource("player/player2_downLeft.png").toString()));
			} 
			else if (direction.equals("jump") || direction.equals("down")) {
				direction = "down";
				if (pastDirection.equals("left")) {
					player.setImage(new Image(ClassLoader.getSystemResource("player/player2_downLeft.png").toString()));
				} else if (pastDirection.equals("right")) {
					player.setImage(new Image(ClassLoader.getSystemResource("player/player2_downRight.png").toString()));
				}
			}	
		}
    	
        if (playerVelocity.getY() < this.maxGravity) {
            playerVelocity = playerVelocity.add(0, 1);
        }

        movePlayerY((int)playerVelocity.getY()); // like gravity
        
        
        for (Node coin : coins) {
            if (player.getBoundsInParent().intersects(coin.getBoundsInParent())) {
            	coin.getProperties().put("alive", false);
                dialogEvent = true;
                //System.out.println("congrat");
            }
        }
        
        for (Iterator<Node> it = coins.iterator(); it.hasNext(); ) {
            Node coin = it.next();
            if (!(Boolean)coin.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(coin);
            }
        }
        
        for (Node spike : spikes) {
            if (player.getBoundsInParent().intersects(spike.getBoundsInParent())) {
                reset(player, gameRoot);
            }
        }
    }
    
    public void charge() {
    	if (chargeTime >= maxChargeTime || chargeTime > 0 && !isPressed(KeyCode.SPACE)) {
    		yVelocity = 10 + Math.pow(chargeTime/5, 1.4);
		    chargeTime = 0;// clear chargeTime
		    isJump = true;
		    isCharge = false;
		    isWalk = false;
		    playerVelocity = playerVelocity.add(0, -yVelocity);
    	}
    }
    
    public void updateWalkImage() { 
    	if (this.isWalk) {
    		if (direction.equals("left")) {
    			spriteCounter++;
    			if (spriteCounter > 5) { //control the player's sprite animation.
    				if (spriteNum == 1) {
    					spriteNum = 2;
    					player.setImage(new Image(ClassLoader.getSystemResource("player/player2_walkLeft1.png").toString()));
    				} else if (spriteNum == 2) {
    					spriteNum = 3;
    					player.setImage(new Image(ClassLoader.getSystemResource("player/player2_walkLeft2.png").toString()));
    				} else if (spriteNum == 3) {
    					spriteNum = 1;
    					player.setImage(new Image(ClassLoader.getSystemResource("player/player2_walkLeft3.png").toString()));
    				} 
    				spriteCounter = 0;
    			}
    		} else if (direction.equals("right")) {
    			spriteCounter++;
    			if (spriteCounter > 5) { //control the player's sprite animation.
    				if (spriteNum == 1) {
    					spriteNum = 2;
    					player.setImage(new Image(ClassLoader.getSystemResource("player/player2_walkRight1.png").toString()));
    				} else if (spriteNum == 2) {
    					spriteNum = 3;
    					player.setImage(new Image(ClassLoader.getSystemResource("player/player2_walkRight2.png").toString()));
    				} else if (spriteNum == 3) {
    					spriteNum = 1;
    					player.setImage(new Image(ClassLoader.getSystemResource("player/player2_walkRight3.png").toString()));
    				} 
    				spriteCounter = 0;
    			}
    		} 
    	} 
    }

    public void walkLeft() {
        for (int i = 0; i < this.getWalkSpeed(); i++) { //to simulate the movement of the player to the left in small increments.
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
            
        }
    }

    public void walkRight() {
        for (int i = 0; i < this.getWalkSpeed(); i++) { //to simulate the movement of the player to the left in small increments.
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
//        Image image = new Image(path);
//        entity.setFill(new ImagePattern(image));
        ImageView imageView = new ImageView(new Image(ClassLoader.getSystemResource(path).toString()));
        entity.setFill(new ImagePattern(imageView.getImage()));
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

	public ImageView getPlayer() {
		return player;
	}
    
	public void reset(ImageView player, Pane gameRoot) {
		isDeath = true;
        count += 1;
        player.setTranslateX(80);
        player.setTranslateY(23 * 60);
        gameRoot.setLayoutX(0);
        gameRoot.setLayoutY(-780);
//        System.out.println(count);
        
	}

	public int getCount() {
		return count;
	}

	public boolean isDeath() {
		return isDeath;
	}

	public void setDeath(boolean death) {
		this.isDeath = death;
	}
    
    
}
