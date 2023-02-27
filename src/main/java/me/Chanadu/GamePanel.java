package me.Chanadu;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class GamePanel extends JPanel {
	final static int HORIZONTAL_TILE_COUNT = 16;
	final static int VERTICAL_TILE_COUNT = 16;
	final static int NUMBER_OF_MINES = 40;
	Tile[][] tiles = new Tile[HORIZONTAL_TILE_COUNT][VERTICAL_TILE_COUNT];
	GridLayout layout = new GridLayout(HORIZONTAL_TILE_COUNT, VERTICAL_TILE_COUNT);
	boolean gameOver = false;
	boolean firstClick = true;
	public GamePanel() {
		setLayout(layout);
		setBackground(Color.BLACK);
		setUpTiles();
		setUpMines();
	}
	
	private void setUpTiles() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = new Tile(this, i, j);
				add(tiles[i][j]);
			}
		}
	}
	
	private void setUpMines() {
		for (Tile[] tile : tiles) {
			for (Tile value : tile) {
				value.isMine = false;
			}
		}
		for (int i = 0; i < NUMBER_OF_MINES; i++) {
			Random rand = new Random();
			int x, y;
			do {
				x = rand.nextInt(HORIZONTAL_TILE_COUNT);
				y = rand.nextInt(VERTICAL_TILE_COUNT);
			} while (tiles[x][y].isMine);
			tiles[x][y].isMine = true;
		}
	}
	
	private int findMinesNear(int xPos, int yPos) {
		int counter = 0;
		for (int offsetX = -1; offsetX <= 1; offsetX++) {
			for (int offsetY = -1; offsetY <= 1; offsetY++) {
				if (xPos + offsetX < 0 || yPos + offsetY < 0 || xPos + offsetX >= HORIZONTAL_TILE_COUNT || yPos + offsetY >= VERTICAL_TILE_COUNT) {
					continue;
				}
				if (tiles[offsetX + xPos][offsetY + yPos].isMine) {
					counter++;
				}
			}
		}
		return counter;
	}
	
	private int findFlagsNear(int xPos, int yPos) {
		int counter = 0;
		for (int offsetX = -1; offsetX <= 1; offsetX++) {
			for (int offsetY = -1; offsetY <= 1; offsetY++) {
				if (xPos + offsetX < 0 || yPos + offsetY < 0 || xPos + offsetX >= HORIZONTAL_TILE_COUNT || yPos + offsetY >= VERTICAL_TILE_COUNT) {
					continue;
				}
				if (tiles[offsetX + xPos][offsetY + yPos].isFlag) {
					counter++;
				}
			}
		}
		return counter;
	}
	
	private int revealTile(int xPos, int yPos) {
		if (xPos < 0 || yPos < 0 || xPos >= HORIZONTAL_TILE_COUNT || yPos >= VERTICAL_TILE_COUNT) {
			return -1;
		}
		Tile tile = tiles[xPos][yPos];
		if(tile.isRevealed) {
			return -1;
		}
		if (tile.isMine) {
			tile.isRevealed = true;
			tile.setState(TileState.MINE);
			return 2;
		}
		tile.isRevealed = true;
		tile.setBackgroundColor(Color.LIGHT_GRAY);
		if(findMinesNear(xPos,yPos) != 0) {
			tile.setState(findMinesNear(xPos,yPos));
			return 1;
		}
		
		revealTile(xPos - 1,yPos - 1);
		revealTile(xPos - 1,yPos + 1);
		revealTile(xPos + 1,yPos - 1);
		revealTile(xPos + 1,yPos + 1);
		revealTile(xPos - 1,yPos);
		revealTile(xPos + 1,yPos);
		revealTile(xPos,yPos - 1);
		revealTile(xPos,yPos + 1);
		return 0;
	}
	
	private void doubleClickTile(int xPos, int yPos) {
		if (xPos < 0 || yPos < 0 || xPos >= HORIZONTAL_TILE_COUNT || yPos >= VERTICAL_TILE_COUNT) {
			 return;
		}
		revealTile(xPos - 1,yPos - 1);
	}
	
	public void tileLeftClicked(int xPos, int yPos) {
		int result = revealTile(xPos, yPos);
		if (result == 2) {
			if (firstClick) {
				do {
					setUpMines();
				} while (tiles[xPos][yPos].isMine);
				firstClick = false;
				tileLeftClicked(xPos, yPos);
			}
			gameOver = true;
			endGame();
		}
		/*
		if (tiles[xPos][yPos].isRevealed && !tiles[xPos][yPos].isMine && findFlagsNear(xPos, yPos) == findMinesNear(xPos, yPos)) {
			System.out.println(findFlagsNear(xPos, yPos) + " " + findMinesNear(xPos, yPos));
			doubleClickTile(xPos - 1,yPos - 1);
			doubleClickTile(xPos - 1,yPos + 1);
			doubleClickTile(xPos + 1,yPos - 1);
			doubleClickTile(xPos + 1,yPos + 1);
			doubleClickTile(xPos - 1,yPos);
			doubleClickTile(xPos + 1,yPos);
			doubleClickTile(xPos,yPos - 1);
			doubleClickTile(xPos,yPos + 1);
		}
		*/
		
	}
	
	public void tileRightClicked(int xPos, int yPos) {
		Tile tile = tiles[xPos][yPos];
		if (tile.isRevealed) {
			return;
		}
		tile.isFlag ^= true;
		if (tile.isFlag) {
			tile.setState(TileState.FLAG);
		} else {
			tile.setState(TileState.EMPTY);
		}
	}
	
	public void endGame()  {
		for (Tile[] value : tiles) {
			for (Tile tile : value) {
				if (tile.isMine) {
					tile.setState(TileState.MINE);
				}
			}
		}
	}
}
