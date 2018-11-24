import java.awt.Graphics;

abstract class Sprite
{
	int x, y, width, height;
	
	abstract void draw(Graphics g, Model m);
	abstract void update(Model m);
	abstract Json marshall();
	abstract void unmarshall(Json obj);
	//abstract Sprite cloneMe();
	
	Sprite(Json obj)
	{
		unmarshall(obj);
	}
	
	Sprite(int inpx, int inpy)
	{
		x = inpx;
		y = inpy;
	}
	
	Sprite(int inpx, int inpy, int inpw, int inph)
	{
		x = inpx;
		y = inpy;
		width = inpw;
		height = inph;
	}
	
	boolean detectCollision(int inpx, int inpy, int inpw, int inph)
	{
		if (x + width < inpx)
			return false;
		if (x > inpx + inpw)
			return false;
		if (y + height < inpy)
			return false;
		if (y > inpy + inph)
			return false;
		return true;
	}

	boolean isACoinBlock()
	{
		return false;
	}

	boolean isACoin()
	{
		return false;
	}

	boolean isABrick()
	{
		return false;
	}

	boolean isAMario()
	{
		return false;
	}

} 