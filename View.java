import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import javax.swing.JButton;

class View extends JPanel
{
	Model model;
	Image image, background;
	int imageIndex;
	boolean mirror;

	View(Controller c, Model m)
	{
		mirror = false;
		imageIndex = 0;
		c.setView(this);
		model = m;
		loadImage("scrollingBackground.png"); background = image;
		//loadImage("brickImage.png"); brick = image;
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

	public void paintComponent(Graphics g)
	{
		//System.out.printf("JPanel xmax: %d Jpanel ymax: %d%n", getWidth(), getHeight());
		//Print 
		int picX = -5 * 1600;
		int width = 1600;
		for (int i = 1; i <= 100; i++)
		{
			if (i % 2 == 1)
			{
				g.drawImage(this.background, picX + (model.camera.getOffset() / 2), 0, width, 500, null);
				picX += (2 * width);
			}
			else
			{
				g.drawImage(this.background, picX + (model.camera.getOffset() / 2), 0, -width, 500, null);
			}
		}

		g.setColor(Color.black);
		g.drawLine(0, 500, 3000, 500);
		g.setColor(new Color(80, 170, 0));
		g.fillRect(0, 500, 2501, 800);
		
		for (int i = 0; i < model.sprites.size(); i++)
		{
			model.sprites.get(i).draw(g, model);
		}
	}
} 