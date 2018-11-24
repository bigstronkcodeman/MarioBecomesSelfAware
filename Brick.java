import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;

enum Direction4
{
	UP, DOWN, LEFT, RIGHT;
}

public class Brick extends Sprite
{
	static Image brickPic = null;

	Brick(Brick copyBrick)
	{
		super(copyBrick.x, copyBrick.y, copyBrick.width, copyBrick.height);
	}
	
	Brick(Json obj)
	{
		super(obj);
	}

	Brick(int x, int y, int w, int h)
	{
		super(x, y, w, h);
		if (brickPic == null);
		{
			try
			{
				brickPic = ImageIO.read(new File("brickImage.png"));
			} catch(Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}

	//Unmarshaling constructor
	@Override void unmarshall(Json obj)
	{
		x = (int)obj.getLong("x");
		y = (int)obj.getLong("y");
		width = (int)obj.getLong("width");
		height = (int)obj.getLong("height");
		if (brickPic == null);
		{
			try
			{
				brickPic = ImageIO.read(new File("brickImage.png"));
			} catch(Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}

	//Copy brick data into json file
	@Override Json marshall()
	{
		Json obj = Json.newObject();
		obj.add("x", x);
		obj.add("y", y);
		obj.add("width", width);
		obj.add("height", height);
		return obj;
	}

	@Override void update(Model m)
	{
		//do nothing?
	}

	@Override void draw(Graphics g, Model m)
	{
		g.drawImage(brickPic, x + m.camera.getOffset(), y, width, height, null);
	}

	@Override boolean isABrick()
	{
		return true;
	}
} 