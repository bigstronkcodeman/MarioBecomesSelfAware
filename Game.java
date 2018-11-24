import javax.swing.JFrame;
import java.awt.Toolkit;

public class Game extends JFrame
{
	Model model;
	Controller controller;
	View view;

	public Game()
	{
		model = new Model();
		controller = new Controller(model);
		view = new View(controller, model);
		this.setTitle("It's-a me !");
		this.setSize(1700, 625);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		view.addMouseListener(controller);
		this.addKeyListener(controller);
	}

public void run() 
{
	while(true)
	{
		//controller.update();
		controller.updateAI();
		model.update();
		view.repaint(); // Indirectly calls View.paintComponent
		Toolkit.getDefaultToolkit().sync(); // Updates screen

		// Go to sleep for 50 milliseconds
		try
		{
			Thread.sleep(48);
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}

	public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
	}
}