import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Exception;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.io.InputStream;
import java.io.FileInputStream;

public class Ghomlos extends JPanel {
	private final Color[] pieceColors = {
		(new Color(233, 102, 172)), (new Color(136, 105, 173)), (new Color(50, 118, 181)), (new Color(51, 184, 165)), (new Color(239, 73, 80)), (new Color(255, 243, 52)), (new Color(244, 140, 76))
	};
	
	private Point pieceOrigin;
	private int currentPiece;
	private int rotation;
	private ArrayList<Integer> nextPieces = new ArrayList<Integer>();

	private static long lines=99;
	private Color[][] arena;
	private static JFrame loser = new JFrame("YOU LOSE");
	
	private final Point[][][] pieces = {
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},
			
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
			},
			
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
			},
			
			{
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
			},
			
			{
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
			},
			
			{
                		{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
			},
			
			{
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
			}
	};
	
	private void init() {
		arena = new Color[12][24];
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				if (i == 0 || i == 11 || j == 22) {
					arena[i][j] = Color.LIGHT_GRAY;
				} else {
					arena[i][j] = Color.DARK_GRAY;
				}
			}
		}
		newPiece();
	}
	
	
	public void newPiece() {
		pieceOrigin = new Point(3, 2);
		rotation = 0;
		if (nextPieces.isEmpty()) {
			for(int i=0; i<7; i++)
                nextPieces.add((int)(Math.random()*7));
		}
		currentPiece = nextPieces.get(0);
		nextPieces.remove(0);
	}
	
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : pieces[currentPiece][rotation]) {
			if (arena[p.x + x][p.y + y] != Color.DARK_GRAY) {
				return true;
			}
		}
		return false;
	}
	
    public void rotate() {
		int newRotation = (rotation + 1) % 4;
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
			rotation = newRotation;
		}
		repaint();
	}
	
	public void move(int i) {
		if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
			pieceOrigin.x += i;	
		}
		repaint();
	}
	
	public void dropDown() {
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
			pieceOrigin.y += 1;
		} else {
			catchPiece();
		}	
		repaint();
	}
	
	public void drop() {
        while(!collidesAt(pieceOrigin.x,pieceOrigin.y+1,rotation))
            pieceOrigin.y++;
        catchPiece();
        repaint();
	}
	
	public void catchPiece() {
		for (Point p : pieces[currentPiece][rotation]) {
			arena[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = pieceColors[currentPiece];
		}
		if(clearRows())
            newPiece();
        else if(pieceOrigin.y<3 && lines<200) {
            loser.setSize(110,300);
            JPanel pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            JButton newGame = new JButton("New Game");
            newGame.setSize(100,100);
            pane.add(newGame);
            pane.add(new JLabel("Scores"));
            try {
                PrintWriter writer = new PrintWriter(new FileOutputStream(new File("scores.txt"),true/* append = true */));
            	writer.println(lines);
            	writer.close();
            } catch(IOException e) {e.printStackTrace();}
            try {
                BufferedReader reader = new BufferedReader(new FileReader("scores.txt"));
                String score=reader.readLine();
                while(score!=null) {
                    System.out.println(score);
                    pane.add(new JLabel(score));
                    score=reader.readLine();
                }
                reader.close();
            } 
            catch(IOException e) {e.printStackTrace();}
            loser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loser.add(pane);
            loser.setVisible(true);
            newGame.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loser.setVisible(false);
                    lines=0;
                    init();
                    run();
                }
            });
        }
        else if(lines>=200) {
            loser.setSize(1600,900);
            try {
                loser.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("Ghomlos.png")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            loser.pack();
            loser.setVisible(true);
        }
        else
            newPiece();
	}
	
	public void deleteRow(int row) {
		for (int j = row-1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				arena[i][j+1] = arena[i][j];
			}
		}
	}
	
	public static void playSound(String n) {
        InputStream sound;
        try {
            sound=new FileInputStream(new File(n));
            AudioStream audios=new AudioStream(sound);
            AudioPlayer.player.start(audios);
        } catch(Exception e) {System.out.println(e);}
	}

	public boolean clearRows() {
        long start=lines;
		boolean gap;
		for (int j = 21; j > 0; j--) {
			gap = false;
			for (int i = 1; i < 11; i++) {
				if (arena[i][j] == Color.DARK_GRAY) {
					gap = true;
					break;
				}
			}
			if (!gap) {
                if(lines==99) {
                    new Thread() {
                        @Override public void run() {
                            long time=2000;
                            while (true) {
                                //try {
                                    if(loser.isVisible()) {
                                        break;
                                    }
                                    //Thread.sleep(time);
                                    playSound("scream.wav");
                                //} 
                                //catch (InterruptedException l) {System.out.println("[!] Error");}
                            }
                        }
                    }.start();
                }
                playSound("Ghomlos.wav");
				deleteRow(j);
				j += 1;
				lines++;
			}
		}
		if(start<lines)
            return true;
        return false;
	}
	
	// Draw the falling piece
	private void drawPiece(Graphics g) {		
		g.setColor(pieceColors[currentPiece]);
		for (Point p : pieces[currentPiece][rotation]) {
			g.fillRect((p.x + pieceOrigin.x) * 40, (p.y + pieceOrigin.y) * 40, 39, 39);
		}
	}
	
	@Override 
	public void paintComponent(Graphics g) {
		// Paint the arena
		g.fillRect(0, 0, 40*12, 40*23);
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				g.setColor(arena[i][j]);
				g.fillRect(40*i, 40*j, 39, 39);
			}
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Roboto", Font.PLAIN, 25));
		g.drawString("" + lines, 19*12, 40);
		
		drawPiece(g);
	}

	public static void main(String[] args) {
		run();
	} 
	
	public static void run() {		
        JFrame f = new JFrame("Ghomlos");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(12*40+20, 23*40+40);
		f.setVisible(true);
		final Ghomlos game = new Ghomlos();
		game.init();
		f.add(game);
		
		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					game.rotate();
					break;
				case KeyEvent.VK_DOWN:
					game.dropDown();
					break;
				case KeyEvent.VK_LEFT:
					game.move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					game.move(+1);
					break;
				case KeyEvent.VK_SPACE:
					game.drop();
					break;
				} 
			}
			
			public void keyReleased(KeyEvent e) {
			}
		});
		
		new Thread() {
			@Override public void run() {
				long time=1000;
				while (true) {
					try {
                        if(loser.isVisible()) {
                            break;
                        }
						Thread.sleep(time);
						if(time>200) {
                            time=(long)(time*0.997);
                            if(time<200)
                                time=200;
                        }
						game.dropDown();
					} catch ( InterruptedException e ) {}
				}
			}
		}.start();
	}
}
