import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
/**
 * Written by Seikyung Jung
 * Warning:
 * You must NOT post this code online.
 * You must NOT share this code without permission from the author
 *
 * Current:
 * One snake and one food, No poison
 * The snake does not die when it bumps into itself
 * when the snake grows, it does not move faster
 *
 * Student MUST write description here:
 * Edited by Joshua Nazaire with help by tutor and heavy disscusion with Aydin Hinton
 * Feature 1 is implemented with 5 food pellets when you start the game.
 * Feature 2 is implemented with 2 poison pellets when player starts game.
 * Poison is set to new coordinate whenever the snake eats the food.
 * Poison kills snake when eaten. prompets "Game over"
 * Feature 3 is implemented, created new boolean statment.
 * goes through iteration and if newhead is the same as the hitTheSnake, it dies.
 * Feature 4 create new delay, delay is subtracted by 5 whenever food is ate
 * creates new timer each time game is restarted.
 * Feature 5 when snake list reaches 20 the game ends and displays "You win"
 *  Feature 1 to 5: Video file is posted on BB
 *
 *  Feature1 (25 points). At least 5 food pellets (currently only 1). You must use java.util.LinkedList. See details from posted MP4 file

 *  Feature2 (25 points). A list of poison pellets (at least 2), which will kill the snake when eaten. Poison should move whenever the snake eats food. Use a different color than food or snake. You must use java.util.LinkedList.  See details from posted MP4 file
 *
 *  Feature3 (25 points). The snake should die when it bumps into itself.
 *
 *  Feature4 (20 points). When the snake grows, move the snake faster.
 *
 *  Feature5 (5 points). When the snake eats 10 pellets, the player wins the game (with "You won" message - you must change the message)
 *
 */
public class SnakeGame extends JFrame {
	// A snake is just a list of coordinates (java.util.LinkedList, not our LinkedList)
	private LinkedList<Coordinate> snake = new LinkedList<Coordinate>();
	private LinkedList<Coordinate> food = new LinkedList<>();
	// The snake grows when it eats food
	private LinkedList<Coordinate> poison = new LinkedList<>();


	// The game is on or over
	private static enum Game {ON, OVER}

	;
	private Game status = Game.ON;

	// Repeatedly moves the snake
	private Timer timer;
	//JN- delay is initialized
	private int delay = 150;

	// The snake can move in one of 4 directions
	public static enum Direction {UP, DOWN, LEFT, RIGHT}

	;
	// The snake's current direction (heading). Default: moving right
	private Direction heading = Direction.RIGHT;

	/**
	 * An (x,y) coordinate in a 64 by 48 grid
	 */
	public static class Coordinate {
		public final int x;
		public final int y;

		// By default, construct a random coordinate not too far from the wall
		Coordinate() {
			this.x = new Random().nextInt(60) + 2;
			this.y = new Random().nextInt(40) + 2;
		}

		Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	// The snake can't switch to the opposite direction
	public boolean oppositeDirection(Direction newHeading) {
		return (heading == Direction.UP && newHeading == Direction.DOWN) ||
				(heading == Direction.DOWN && newHeading == Direction.UP) ||
				(heading == Direction.LEFT && newHeading == Direction.RIGHT) ||
				(heading == Direction.RIGHT && newHeading == Direction.LEFT);
	}

	// Update the heading based on the new heading
	public void changeHeading(Direction newHeading) {
		if (!oppositeDirection(newHeading)) {
			heading = newHeading;
		}
	}

	/**
	 * Handle keyboard input (arrows change the snake's heading)
	 */
	private class KeyControl implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			Direction newHeading = heading;

			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_KP_LEFT:
					newHeading = Direction.LEFT;
					break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_KP_RIGHT:
					newHeading = Direction.RIGHT;
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					newHeading = Direction.UP;
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					newHeading = Direction.DOWN;
					break;
			}
			changeHeading(newHeading);
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	}


	@Override
	// This view renders the snake and food
	// Each snake coordinate is a 10x10 pixel square
	// This method is called from repaint() from AWT
	public void paint(Graphics g) {
		g.clearRect(0, 0, 640, 480);
		Color green = new Color(0, 128, 0);

		g.clearRect(0, 0, 640, 480);
		Color blue = new Color(0, 0, 255);


		g.setColor(green);
		for (Coordinate c : snake) {
			g.fillRect(c.x * 10, c.y * 10, 10, 10);
		}
		// added coordinate for food JN

		for (Coordinate c : food) {
			g.setColor(green);
			g.fillOval(c.x * 10, c.y * 10, 10, 10);
			// added coordinate for poison JN


		for (Coordinate b : poison) {
			g.setColor(blue);
			g.fillOval(b.x * 10, b.y * 10, 10, 10);
		}
	}
}

	// When the snake moves, it can hit the wall, hit the food, poison (not implemented) or itself (not implemented)
	public void move() {
		Coordinate newHead = newHead();

		if (hitTheWall(newHead)) {
			status = Game.OVER;

			return; // will return back to where this method is called
		}
			// If the snake hits the poison its game over
			if (hitThePoison(newHead)) {
				status = Game.OVER;
				return;
			}
			if ( hitTheSnake(newHead)) {
				status = Game.OVER;
				return;}
		if (snake.size() == 21) {
			playAgain("You Win");
		}


		snake.addFirst(newHead);    // java.util.LinkedList.addFirst()
//changed to if statment: If the head hits the food then the food gets a new coordinate
		if (hitTheFood(newHead)) {
			food.add(new Coordinate());
			poison.clear();
			delay = delay -5;
			timer.stop();
			timer = new Timer(delay,new SnakeMover());
			timer.start();
			for (int i = 0; i <2; i++) {
				poison.add(new Coordinate());
			}
			for (int i = 0; i < food.size(); i++) {
				if (newHead.x == food.get(i).x && newHead.y == food.get(i).y) {
					food.remove(i);

				}
			}
		} else {
			snake.removeLast();
		}
	}

	// The snake's heading determines its new head coordinate
	private Coordinate newHead() {
		Coordinate head, newHead;
		head = snake.getFirst();	// java.util.LinkedList.getFirst()

		switch (heading) {
			case DOWN: newHead = new Coordinate(head.x, head.y + 1); break;
			case LEFT: newHead = new Coordinate(head.x - 1, head.y); break;
			case RIGHT: newHead = new Coordinate(head.x + 1, head.y); break;
			case UP: newHead = new Coordinate(head.x, head.y - 1); break;
			// The default case is never reached because we have only 4 events.
			default: newHead = new Coordinate(); break;
		}
		return newHead;
	}
	private boolean hitTheSnake(Coordinate newHead) {
		for (int i = 0; i < snake.size(); i++) {
			if (newHead.x == snake.get(i).x && newHead.y == snake.get(i).y) {
				return true;
			}
		}
		return false;
	}

	private boolean hitTheFood(Coordinate newHead) {
		for (int i = 0; i < food.size(); i++) {
		if (newHead.x == food.get(i).x && newHead.y == food.get(i).y){
			return true;
			} }
			return false;
//Changed to for loop, goes through iteration and gets the index of food and its coordinate
		// Returns true if it gets the food and returns false if it does not.
	}

	public boolean hitTheWall(Coordinate head) {
		return (head.x == 64 || head.y == 48 || head.x == 0 || head.y == 0);
	}
	// not working but need hitthePoison to be a new coordinate
	public boolean hitThePoison(Coordinate head) {
		for (int i = 0; i < poison.size(); i++) {
			if (head.x == poison.get(i).x && head.y == poison.get(i).y){
			return true;
		} }
	return false;
	}
	/**
	 * The timer moves the snake using this class.
	 */
	private class SnakeMover implements ActionListener {
		@Override
		// Listening Action (in this case Timer - every certain millisecond) and execute this method
		public void actionPerformed(ActionEvent e) {
			move();
			repaint();	// from AWT library. It will call paint() automatically
			if (status == Game.OVER) {
				playAgain("The snake's dead");
			}
		}
	}

	// Ask the player what to do when the game is over
	private void playAgain(String message) {
		String[] options = new String[] {"Play again","Quit"};
		int choice = JOptionPane.showOptionDialog(null, message, "Game over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[0]);

		if (choice == 0) {
			timer.stop();
			initialize();
		} else {
			System.exit(0);
		}
	}

	// Initialize game (snake, food, etc)
	private void initialize() {
		status = Game.ON;
		delay = 150;
		timer = new Timer(delay, new SnakeMover());
		timer.start();
		food.clear();
		poison.clear();
		// Make a small snake with 1 node (a 10x10 pixel coordinate)
		snake.clear(); // remove all of the elements of the LinkedList
		snake.add(new Coordinate()); // append the new element to the end of the LinkedList
		for (int i = 0; i <5; i++) {
			food.add(new Coordinate());//JN- adds new coordinate for food
		}
		for (int i = 0; i <2; i++) {
			poison.add(new Coordinate());//JN- adds new coordinate for poison
		}
	}

	public SnakeGame() {
		setSize(640, 480);	// Window size - pixel
		setTitle("Snake Game");
		setVisible(true);

		// Update the snake's direction using keyboard arrows
		// Event Handler: addKeyListener is from AWT library. This is how to "register" event
		addKeyListener(new KeyControl());

		// Make the snake move every 150 milliseconds


		// Initialize game (snake, food)
		initialize();
	}

	public static void main(String[] args) {
		new SnakeGame();
	}
}
