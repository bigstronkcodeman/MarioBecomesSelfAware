import java.util.Stack;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;

class Coin extends Sprite
{
	double vertVeloc, horizontVeloc;
	boolean stored = true;
	static Image coinPic = null;

	Coin(Coin copyCoin)
	{
		super(copyCoin.x, copyCoin.y, copyCoin.width, copyCoin.height);
		vertVeloc = copyCoin.vertVeloc;
		horizontVeloc = copyCoin.horizontVeloc;
		stored = copyCoin.stored;
	}
	
	//Dummy coin unmarshalling constructor
	Coin(Json obj)
	{
		super(obj);
	}

	//Standard coin constructor (invokes super constructor)
	Coin(int inpx, int inpy)
	{
		super(inpx, inpy - 50, 75, 75);
		if (coinPic == null)
		{
			try
			{
				coinPic = ImageIO.read(new File("coin.png"));
			} catch(Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
		Random rand = new Random();
		vertVeloc = -30;
		horizontVeloc = (rand.nextDouble() * 30) - 15;
	}
	
	//Copy object over from json dom
	@Override void unmarshall(Json obj)
	{
		x = (int)obj.getLong("x");
		y = (int)obj.getLong("y");
		width = (int)obj.getLong("width");
		height = (int)obj.getLong("height");
		vertVeloc = obj.getDouble("vertVeloc");
		horizontVeloc = obj.getDouble("horizontVeloc");
		stored = obj.getBool("stored");
		if (coinPic == null)
		{
			try
			{
				coinPic = ImageIO.read(new File("coin.png"));
			} catch(Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}
	
	//stores object into a json dom and returns it
	@Override Json marshall()
	{
		Json obj = Json.newObject();
		obj.add("x", x);
		obj.add("y", y);
		obj.add("width", width);
		obj.add("height", height);
		obj.add("vertVeloc", vertVeloc);
		obj.add("horizontVeloc", horizontVeloc);
		obj.add("stored", stored);
		if (coinPic == null)
		{
			try
			{
				coinPic = ImageIO.read(new File("coin.png"));
			} catch(Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
		return obj;
	}

	@Override void update(Model m)
	{
		if (!stored) //only update the coin if it hasn't left the coinblock!
		{
			x += horizontVeloc;
			y += vertVeloc;
			vertVeloc += 2.8;
			if (y >= 1100) //remove coin from sprite list if off of screen
				m.sprites.remove(this);
		}
	}

	@Override void draw(Graphics g, Model m)
	{
		if (!stored) //only draw the coin if it has left the coinblock!
		{
			g.drawImage(coinPic, x + m.camera.getOffset(), y, width, height, null);
		}
	}

	void emerge()
	{
		stored = false;
	}

	@Override boolean isACoin()
	{
		return true;
	}
}

class CoinBlock extends Sprite
{
	Stack<Coin> bank;
	static Image richBlock = null, poorBlock = null;
	boolean hasCoins;

	CoinBlock(CoinBlock copyBlock)
	{
		super(copyBlock.x, copyBlock.y, copyBlock.width, copyBlock.height);
		bank = new Stack<Coin>();
		for (int i = 0; i < copyBlock.bank.size(); i++)
		{
			bank.push(new Coin(copyBlock.bank.get(i)));
		}
		hasCoins = copyBlock.hasCoins;
	}
	
	CoinBlock(Json obj)
	{
		super(obj);
	}

	CoinBlock(int inpx, int inpy)
	{
		super(inpx, inpy, 89, 83);
		if (richBlock == null)
		{
			try
			{
				richBlock = ImageIO.read(new File("block1.png"));
			} catch(Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
		if (poorBlock == null)
		{
			try
			{
				poorBlock = ImageIO.read(new File("block2.png"));
			} catch(Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
		bank = new Stack<Coin>();
		for (int i = 0; i < 5; i++)
		{
			bank.push(new Coin(x, y));
		}
		hasCoins = true;
	}

	boolean popCoin(ArrayList<Sprite> sprites)
	{
		if (hasCoins)
		{
			Coin poppedCoin = bank.pop();
			sprites.add(poppedCoin);
			poppedCoin.emerge();
			if (bank.size() == 0)
			{
				hasCoins = false;
			}
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	@Override void unmarshall(Json obj)
	{
		x = (int)obj.getLong("x");
		y = (int)obj.getLong("y");
		width = (int)obj.getLong("width");
		height = (int)obj.getLong("height");
		hasCoins = obj.getBool("hasCoins");
		bank = new Stack<Coin>();
		Json tempList = obj.get("bank");
		for (int i = 0; i < tempList.size(); i++)
		{
			bank.add(new Coin(tempList.get(i)));
		}
		if (richBlock == null)
		{
			try
			{
				richBlock = ImageIO.read(new File("block1.png"));
			} catch(Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
		if (poorBlock == null)
		{
			try
			{
				poorBlock = ImageIO.read(new File("block2.png"));
			} catch(Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}
	
	@Override void update(Model m)
	{
		//do nothing
	}
	
	@Override Json marshall()
	{
		Json obj = Json.newObject();
		obj.add("x", x);
		obj.add("y", y);
		obj.add("width", width);
		obj.add("height", height);
		obj.add("hasCoins", hasCoins);
		Json tempList = Json.newList();
		obj.add("bank", tempList);
		for (int i = 0; i < bank.size(); i++)
		{
			tempList.add(bank.get(i).marshall());
		}
		return obj;
	}

	@Override boolean isACoinBlock()
	{
		return true;
	}

	@Override void draw(Graphics g, Model m)
	{
		if (hasCoins)
			g.drawImage(richBlock, x + m.camera.getOffset(), y, width, height, null);
		else
			g.drawImage(poorBlock, x + m.camera.getOffset(), y, width, height, null);
	}
} 