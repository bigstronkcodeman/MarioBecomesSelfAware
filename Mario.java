import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Image;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

enum DirectionX
{
	RIGHT, LEFT, NONE;
}

class Mario extends Sprite
{
	int framesSinceLand, imageIndex;
	public int coins, jumpCount;
	double vertVeloc, gravity, jumpForce;
	DirectionX dir;
	boolean justCollided, jumping, airborne, mirror;
	int prevX, prevY;
	static Image mario_images[] = null;
	Image image;
	
	Mario(Json obj)
	{
		super(obj);
	}
	
	Mario(Mario m)
	{
		super(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		coins = m.coins;
		jumpCount = m.jumpCount;
		framesSinceLand = m.framesSinceLand;
		imageIndex = m.imageIndex;
		vertVeloc = m.vertVeloc;
		gravity = m.gravity;
		jumpForce = m.jumpForce;
		dir = m.dir;
		justCollided = m.justCollided;
		jumping = m.jumping;
		airborne = m.airborne;
		mirror = m.mirror;
		prevX = m.prevX;
		prevY = m.prevY;
		if (mario_images == null)
		{
			mario_images = new Image[5];
			for (int i = 0; i < 5; i++)
			{
				loadImage("mario" + (i + 1) + ".png");
				mario_images[i] = image;
			}
		}
	}
	
	@Override void unmarshall(Json obj)
	{
		x = (int)obj.getLong("x");
		y = (int)obj.getLong("y");
		width = (int)obj.getLong("width");
		height = (int)obj.getLong("height");
		coins = (int)obj.getLong("coins");
		jumpCount = (int)obj.getLong("jumpCount");
		framesSinceLand = (int)obj.getLong("framesSinceLand");
		imageIndex = (int)obj.getLong("imageIndex");
		vertVeloc = obj.getDouble("vertVeloc");
		gravity = obj.getDouble("gravity");
		jumpForce = obj.getDouble("jumpForce");
		dir = DirectionX.NONE;
		justCollided = obj.getBool("justCollided");
		jumping = obj.getBool("jumping");
		airborne = obj.getBool("airborne");
		mirror = obj.getBool("mirror");
		prevX = (int)obj.getLong("prevX");
		prevY = (int)obj.getLong("prevY");
		if (mario_images == null)
		{
			mario_images = new Image[5];
			for (int i = 0; i < 5; i++)
			{
				loadImage("mario" + (i + 1) + ".png");
				mario_images[i] = image;
			}
		}
		
	}
	
	@Override Json marshall()
	{
		Json obj = Json.newObject();
		obj.add("x", x);
		obj.add("y", y);
		obj.add("width", width);
		obj.add("height", height);
		obj.add("coins", coins);
		obj.add("jumpCount", jumpCount);
		obj.add("framesSinceLand", framesSinceLand);
		obj.add("imageIndex", imageIndex);
		obj.add("vertVeloc", vertVeloc);
		obj.add("gravity", gravity);
		obj.add("jumpForce", jumpForce);
		obj.add("justCollided", justCollided);
		obj.add("jumping", jumping);
		obj.add("airborne", airborne);
		obj.add("mirror", mirror);
		obj.add("prevX", prevX);
		obj.add("prevY", prevY);
		return obj;
	}

	Mario(int inpX, int inpY) 
	{
		super(inpX, inpY, 60, 95);
		if (mario_images == null)
		{
			mario_images = new Image[5];
			for (int i = 0; i < 5; i++)
			{
				loadImage("mario" + (i + 1) + ".png");
				mario_images[i] = image;
			}
		}
		jumpCount = 0;
		mirror = false;
		imageIndex = 0;
		framesSinceLand = 0;
		jumping = false;
		justCollided = false;
		airborne = true;
		jumpForce = 15;
		gravity = 2.8;
		dir = DirectionX.NONE;
		prevX = x;
		prevY = y;
		vertVeloc = 0;
		coins = 0;
	}

	void loadImage(String fileName)
	{
		try
		{
			image = ImageIO.read(new File(fileName));
		} catch(Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	void saveState()
	{
		prevX = x;
		prevY = y;
	}

	void setXY(int inpX, int inpY) 
	{
		x = inpX;
		y = inpY;
	}

	void setX(int inpX)
	{
		x = inpX;
	}

	void setY(int inpY)
	{
		y = inpY;
	}

	int getPrevX()
	{
		return prevX;
	}

	int getPrevY()
	{
		return prevY;
	}

	DirectionX getDir()
	{
		return dir;
	}

	double getVertVeloc()
	{
		return vertVeloc;
	}

	int getX() 
	{
		return x;
	}

	int getY() 
	{
		return y;
	}

	int getWidth() 
	{
		return width;
	}

	int getHeight() 
	{
		return height;
	}

	void moveRight() 
	{
		x += 10;
	}

	void moveLeft() 
	{
		x -= 10;
	}

	void setCollide(boolean collided) 
	{
		justCollided = collided;
	}

	void setVertVeloc(int inpVeloc)
	{
		vertVeloc = inpVeloc;
	}

	boolean getCollide() 
	{
		return justCollided;
	}

	void setDir(DirectionX inp) 
	{
		dir = inp;
	}
	
	void landUpdate()
	{
		airborne = false;
		framesSinceLand = 0;
	}
	
	boolean onGround() 
	{
		return (y == 483 - height);
	}

	boolean isAirborne()
	{
		return airborne;
	}

	@Override void update(Model m) 
	{
   		if (y >= 500 - height || y + vertVeloc + gravity >= 500 - height) 
		{
			if (jumping)
			{
				forceUp();
			}
			else
			{
				vertVeloc = 0;
				y = 500 - height;
				airborne = false;
				framesSinceLand = 0;
			}
		}
		else
		{ 
			if (vertVeloc != 0)
				airborne = true;
			if (jumping)
			{
				forceUp();
			}
			else
			{
				vertVeloc += gravity;
				y += vertVeloc;
			}
			framesSinceLand++;
		}

		if (vertVeloc != 0)
			airborne = true;
		
		brickCollisionFix(m.sprites, m.camera);
		saveState();
	}

	private void forceUp()
	{
		vertVeloc -= jumpForce;
		y += vertVeloc;
		jumping = false;
	}

	void jump()
	{
		jumpCount++;
		if (!airborne)
		{
			//jumpCount++;
			airborne = true;
			vertVeloc = -10;
			jumping = true;
		}
		if (framesSinceLand < 5)
		{
			vertVeloc -= 2.05;
		}
	}
	
	@Override boolean isAMario()
	{
		return true;
	}

	void brickCollisionFix(ArrayList<Sprite> sprites, Camera camera) 
	{
		camera.wakeUp();
		for (int i = 0; i < sprites.size(); i++) //no longer using iterator because it was just making my life more difficult than it needed to be
		{
			Sprite sprite = sprites.get(i);
			if ((sprite.isABrick() || sprite.isACoinBlock()) && detectCollision(sprite.x + camera.getOffset(), sprite.y, sprite.width, sprite.height))
			{
				//Mario was previously to the right of the brick
	  			if (prevX >= sprite.x + sprite.width + camera.getOffset() - 6)
				{
					camera.tuckIn();
					setX(sprite.x + camera.getOffset() + sprite.width);
				}
				//Mario was previously to the left of the brick
	  			else if (prevX + width <= sprite.x + camera.getOffset() + 6)
				{
					camera.tuckIn();
					setX(sprite.x + camera.getOffset() - width);
				}
				//Mario was previously above the brick
				else if (prevY + height <= sprite.y)
				{
					setY(sprite.y - height);
					setVertVeloc(0);
					landUpdate();
				}
				//Mario was previously below the brick
				else if (prevY >= sprite.y + sprite.height)
				{
					setY(sprite.y + sprite.height);
					setVertVeloc(5);
					if (sprite.isACoinBlock()) { //Mario was previously below a coinblock. Pop a coin out!
						if (((CoinBlock)sprite).popCoin(sprites))
							coins++;
					}
				}
			}
		}
	}

	@Override void draw(Graphics g, Model m)
	{
		//Disgusting switch statement that handles mario's simple animations
		switch (getDir())
		{
			case RIGHT:
				if (isAirborne())
					g.drawImage(Mario.mario_images[0], getX(), getY(), null);
				else
				{
					g.drawImage(Mario.mario_images[imageIndex], getX(), getY(), null);
					imageIndex = (imageIndex + 1) % 5;
				}
				mirror = false;
				break;
			case LEFT:
				if (isAirborne())
					g.drawImage(Mario.mario_images[0], getX() + getWidth(), getY(), -getWidth(), getHeight(), null);
				else
				{
					g.drawImage(Mario.mario_images[imageIndex], getX() + getWidth(), getY(), -getWidth(), getHeight(), null);
					imageIndex = (imageIndex + 1) % 5;
				}
				mirror = true;
				break;
			case NONE:
			default:
				if (mirror)
				{
					if (isAirborne())
					{
						g.drawImage(Mario.mario_images[0], getX() + getWidth(), getY(), -getWidth(), getHeight(), null);
					}
					else
					{
						g.drawImage(Mario.mario_images[imageIndex], getX() + getWidth(), getY(), -getWidth(), getHeight(), null);
					}
				}
				else
				{
					if (isAirborne())
					{
						g.drawImage(Mario.mario_images[0], getX(), getY(), null);
					}
					else
					{
						g.drawImage(Mario.mario_images[imageIndex], getX(), getY(), null);
					}
				}
				if (getCollide()) 
		 		{
		 			imageIndex = (imageIndex + 1) % 5;
				}
				break;
		}
	}
} 