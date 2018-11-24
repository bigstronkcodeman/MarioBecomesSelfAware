import java.util.ArrayList;

enum Action {
	RUN,
	JUMP,
	JUMP_AND_RUN;
}

class Model
{
	int initClick_x; 
	int initClick_y;
	int dest_x;
	int dest_y;
	Camera camera;
	ArrayList<Sprite> sprites;
	ArrayList<String> spriteBook;
	Mario mario;

	Model()
	{
		spriteBook = new ArrayList<String>();
		sprites = new ArrayList<Sprite>();
		camera = new Camera();
		mario = new Mario(100, 100);
		sprites.add(mario);
		//sprites.add(new CoinBlock(300, 200));
		//sprites.add(new CoinBlock(600, 200));
	}

	Model(Model copyModel)
	{
		initClick_x = copyModel.initClick_x;
		initClick_y = copyModel.initClick_y;
		dest_x = copyModel.dest_x;
		dest_y = copyModel.dest_y;
		camera = new Camera(copyModel.camera);
		sprites = new ArrayList<Sprite>();
		spriteBook = new ArrayList<String>();
		for (int i = 0; i < copyModel.spriteBook.size(); i++)
		{
			spriteBook.add(copyModel.spriteBook.get(i));
		}
		for (int i = 0; i < copyModel.sprites.size(); i++)
		{
			if (copyModel.sprites.get(i).isAMario())
			{
				mario = new Mario((Mario)copyModel.sprites.get(i));
				sprites.add(mario);
			}
			else if (copyModel.sprites.get(i).isABrick())
			{
				sprites.add(new Brick((Brick)copyModel.sprites.get(i)));
			}
			else if (copyModel.sprites.get(i).isACoin())
			{
				sprites.add(new Coin((Coin)copyModel.sprites.get(i)));
			}
			else if (copyModel.sprites.get(i).isACoinBlock())
			{
				sprites.add(new CoinBlock((CoinBlock)copyModel.sprites.get(i)));
			}
		}
	}

	//Unmarshaling constructor (copy state from json file)
	Model(Json obj) 
	{
		initClick_x = (int)obj.getLong("initClick_x");
		initClick_y = (int)obj.getLong("initClick_y");
		dest_x = (int)obj.getLong("dest_x");
		dest_y = (int)obj.getLong("dest_y");
		camera = new Camera(obj.get("camera"));
		sprites = new ArrayList<Sprite>();
		spriteBook = new ArrayList<String>();
		mario = new Mario(obj.get("mario"));
		Json tempList = obj.get("spriteBook");
		for (int i = 0; i < tempList.size(); i++)
		{
			spriteBook.add(tempList.getString(i));
		}
		Json tempList2 = obj.get("sprites");
		for (int i = 0; i < tempList.size(); i++)
		{
			switch (spriteBook.get(i))
			{
			case "mario":
				sprites.add(mario);
				break;
			case "brick":
				sprites.add(new Brick(tempList2.get(i)));
				break;
			case "coinblock":
				sprites.add(new CoinBlock(tempList2.get(i)));
				break;
			case "coin":
				sprites.add(new Coin(tempList2.get(i)));
				break;
			}
		}
	}

	//Save state of the model into json file
	Json marshall() 
	{
		spriteBook.clear();
		for (int i = 0; i < sprites.size(); i++)
		{
			if (sprites.get(i).isAMario())
				spriteBook.add("mario");
			else if (sprites.get(i).isABrick())
				spriteBook.add("brick");
			else if (sprites.get(i).isACoinBlock())
				spriteBook.add("coinblock");
			else if (sprites.get(i).isACoin())
				spriteBook.add("coin");
		}
		System.out.println("sprite book:");
		for (int i = 0; i < spriteBook.size(); i++)
		{
			System.out.println(spriteBook.get(i));
		}
		Json obj = Json.newObject();
		obj.add("initClick_x", initClick_x);
		obj.add("initClick_y", initClick_y);
		obj.add("dest_x", dest_x);
		obj.add("dest_y", dest_y);
		obj.add("camera", camera.marshall());
		obj.add("mario", mario.marshall());
		Json tempList = Json.newList();
		Json tempList2 = Json.newList();
		obj.add("spriteBook", tempList2);
		obj.add("sprites", tempList);
		for (int i = 0; i < sprites.size(); i++)
		{
			tempList.add(sprites.get(i).marshall());
			if (i < spriteBook.size())
				tempList2.add(spriteBook.get(i));
		}
		return obj;
	}

	void do_action(Action action)
	{
		switch (action)
		{
			case JUMP_AND_RUN:
				mario.jump();
			case RUN:
				camera.decrement();
				mario.moveRight();
				mario.setDir(DirectionX.RIGHT);
				break;
			case JUMP:
				mario.jump();
				break;
		}
	}

	double evaluateAction(Action action, int depth)
	{
		// Evaluate the state
		if(depth >= 20)
			return (-1 * camera.getOffset()) + (50000 * mario.coins) - (2 * mario.jumpCount);

		// Simulate the action
		Model copy = new Model(this); // uses the copy constructor
		copy.do_action(action); // like what Controller.update did before
		copy.update(); // advance simulated time

		// Recurse
		if(depth % 4 != 0)
		   return copy.evaluateAction(action, depth + 1);
		else
		{
		   double best = copy.evaluateAction(Action.RUN, depth + 1);
		   best = Math.max(best,
			   copy.evaluateAction(Action.JUMP, depth + 1));
		   best = Math.max(best,
			   copy.evaluateAction(Action.JUMP_AND_RUN, depth + 1));
		   return best;
		}
	}

	//Copy from input model to current model (shallow)
	void copy(Model m) 
	{
		this.initClick_x = m.initClick_x;
		this.initClick_y = m.initClick_y;
		this.dest_x = m.dest_x;
		this.dest_y = m.dest_y;
		this.camera = m.camera;
		this.mario = m.mario;
		this.sprites = m.sprites;
	}

	void update() 
	{
  		for (int i = 0; i < sprites.size(); i++)
  		{
  			sprites.get(i).update(this);
  		}
	}

	void setDestination(int x, int y) 
	{
		this.dest_x = x;
		this.dest_y = y;
	}

	void setInitClickLocation(int x, int y) 
	{
		this.initClick_x = x;
		this.initClick_y = y;
	}
} 