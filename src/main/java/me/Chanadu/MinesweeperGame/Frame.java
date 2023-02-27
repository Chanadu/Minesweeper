package me.Chanadu.MinesweeperGame;

import javax.swing.*;
import java.awt.*;
public class Frame extends JFrame {
	Color backgroundColor = new Color(22, 22, 22);
	GamePanel gamePanel = new GamePanel();
	
	public Frame() {
		this.setSize(1000, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setBackground(backgroundColor);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		setVisible(true);
		add(gamePanel, BorderLayout.CENTER);
	}
}
