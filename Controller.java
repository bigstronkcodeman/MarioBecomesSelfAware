import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Robot;

class Controller implements ActionListener, MouseListener, KeyListener
{
	Robot robot;
	Model model;
	View view;
	boolean keyLeft, keyRight, keyUp, keyDown, sPressed, lPressed, spacePushed;
	boolean saveSpace;

	Controller(Model m)
	{
		saveSpace = false;;
		model = m;
		Json loadObj = Json.load("map.json"); //load in data from previous save to new Json obj
		Model temp = new Model(loadObj); //Create temp model with same state as previous save
		model.copy(temp); //copy temp model over into actual game model
		model.mario.jumpCount = 0;
	}

	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = true; break;
			case KeyEvent.VK_LEFT: keyLeft = true; break;
			case KeyEvent.VK_UP: keyUp = true; break;
			case KeyEvent.VK_DOWN: keyDown = true; break;
			case KeyEvent.VK_SPACE: spacePushed = true; saveSpace = true; break;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = false; break;
			case KeyEvent.VK_LEFT: keyLeft = false; break;
			case KeyEvent.VK_UP: keyUp = false; break;
			case KeyEvent.VK_DOWN: keyDown = false; break;
			case KeyEvent.VK_SPACE: spacePushed = false; break;
			case KeyEvent.VK_S: model.marshall().save("map.json"); break; //Save model's state to map.json
			case KeyEvent.VK_L: Json loadObj = Json.load("map.json"); //load in data from previous save to new Json obj
								Model temp = new Model(loadObj); //Create temp model with same state as previous save
								model.copy(temp); break; //copy temp model over into actual game model
		}
	}

	public void keyTyped(KeyEvent e)
	{
	}

	void update()
	{
		if (spacePushed || saveSpace)
		{
			model.mario.jump();
			saveSpace = false;
		}

		if(keyRight)
		{
			//if (model.mario.getDir() != DirectionX.NONE) 
				model.camera.decrement(); //Move camera left
			model.mario.moveRight();
			model.mario.setDir(DirectionX.RIGHT);
		}
		else if (keyLeft) 
		{
			//if (model.mario.getDir() != DirectionX.NONE) 
				model.camera.increment(); //Move camera right
			model.mario.moveLeft();
			model.mario.setDir(DirectionX.LEFT);
		}
		else
		{
			model.mario.setDir(DirectionX.NONE);
			model.camera.tuckIn();
		}
	}

	void updateAI() {
		// Evaluate each possible action
		double score_run = model.evaluateAction(Action.RUN, 0);
		double score_jump = model.evaluateAction(Action.JUMP, 0);
		double score_jump_and_run =
			model.evaluateAction(Action.JUMP_AND_RUN, 0);

		// Do the best one
		if(score_jump_and_run > score_jump &&
				score_jump_and_run > score_run)
			model.do_action(Action.JUMP_AND_RUN);
		else if(score_jump > score_run)
			model.do_action(Action.JUMP);
		else
			model.do_action(Action.RUN);
	}

	public void mousePressed(MouseEvent e)
	{
		model.setInitClickLocation(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) 
	{  
		int x_diff = Math.abs(model.initClick_x - e.getX()),
			y_diff = Math.abs(model.initClick_y - e.getY());

		if (e.getX() != model.initClick_x && e.getY() != model.initClick_y)
		{
			if (e.getX() >= model.initClick_x)
			{
				//Relative to initial click location:
				//below and to the right
				if (e.getY() > model.initClick_y)
				{
					model.sprites.add(new Brick(model.initClick_x - model.camera.getOffset(), model.initClick_y, x_diff, y_diff));
				}
				//above and to the right
				else
				{
					model.sprites.add(new Brick(model.initClick_x - model.camera.getOffset(), model.initClick_y - y_diff, x_diff, y_diff));
				}
			}
			else
			{
				//below and to the left
				if (e.getY() > model.initClick_y)
				{
					model.sprites.add(new Brick(model.initClick_x - x_diff - model.camera.getOffset(), model.initClick_y, x_diff, y_diff));
				}
				//above and to the left
				else //makes a coin block, not a regular brick!
				{
					model.sprites.add(new CoinBlock(model.initClick_x - x_diff - model.camera.getOffset(), model.initClick_y - y_diff/*, x_diff, y_diff*/));
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {    }

	public void mouseExited(MouseEvent e) {    }

	public void mouseClicked(MouseEvent e) 
	{
		if(e.getY() < 100)
		{
			System.out.println("break here");
		}
	}

	void setView(View v)
	{
		view = v;
	}

	public void actionPerformed(ActionEvent e)
	{
		robot.mouseWheel(0);
	}
}
 