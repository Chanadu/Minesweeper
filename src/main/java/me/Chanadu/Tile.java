package me.Chanadu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public  class Tile extends JPanel {
	
	public final int x, y;
	public boolean isFlag = false;
	public boolean isRevealed = false;
	public boolean isMine = false;
	Border blackLine = BorderFactory.createLineBorder(Color.black);
	JLabel text = new JLabel(" ");
	public TileState currentState = TileState.EMPTY;
	
	Tile(GamePanel gamePanel, int x, int y) {
		this.x = x;
		this.y = y;
		setFocusable(true);
		setBackground(Color.DARK_GRAY);
		setBorder(blackLine);
		add(text);
		
		createListener(gamePanel, this.x, this.y);
	}
	
	private void createListener(final GamePanel gamePanel, final int x, final int y) {
		MouseListener mouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!gamePanel.gameOver) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						gamePanel.tileLeftClicked(x, y);
					}
					if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
						gamePanel.tileRightClicked(x, y);
					}
				}
			}
		};
		addMouseListener(mouseListener);
	}
	
	public void setBackgroundColor(Color color) {
		setBackground(color);
	}

	public void setState(TileState state) {
		currentState = state;
		switch (state) {
			case EMPTY -> {
				setBackground(Color.DARK_GRAY);
				text.setText("");
			}
			case MINE -> {
				text.setForeground(Color.BLACK);
				setBackground(Color.RED);
				text.setText("M");
			}
			case FLAG -> {
				text.setForeground(Color.RED);
				setBackground(Color.DARK_GRAY);
				text.setText("F");
			}
		}
	}
	
	public void setState(int state) {
		currentState = TileState.NUMBER;
		text.setForeground(Color.BLACK);
		text.setBackground(Color.LIGHT_GRAY);
		text.setText(Integer.toString(state));
	}
	
}
