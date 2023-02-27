package me.Chanadu.MinesweeperGame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class GamePanel extends JPanel {
	final static int HORIZONTAL_TILE_COUNT = 5;
	final static int VERTICAL_TILE_COUNT = 5;
	final static int NUMBER_OF_MINES = 3;
	final static int NUMBER_OF_TILES_NEEDED = HORIZONTAL_TILE_COUNT * VERTICAL_TILE_COUNT  - NUMBER_OF_MINES;
	Tile[][] tiles = new Tile[HORIZONTAL_TILE_COUNT][VERTICAL_TILE_COUNT];
	GridLayout layout = new GridLayout(HORIZONTAL_TILE_COUNT, VERTICAL_TILE_COUNT);
	boolean gameOver = false;
	boolean firstClick = true;
	int tileClickedCounter = 0;
	
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
		Random rand = new Random();
		for (int i = 0; i < NUMBER_OF_MINES; i++) {
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
		tileClickedCounter++;
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
	
	
	public void tileLeftClicked(int xPos, int yPos) {
		if (firstClick) {
			firstClick = false;
			if (tiles[xPos][yPos].isMine || findMinesNear(xPos, yPos) > 0) {
				do {
					setUpMines();
				} while (tiles[xPos][yPos].isMine || findMinesNear(xPos, yPos) > 0);
				tileLeftClicked(xPos, yPos);
			}
		}
		int result = revealTile(xPos, yPos);
		if (result == 2) {
				gameOver = true;
				endGame(false);
		} else if (tileClickedCounter >= NUMBER_OF_TILES_NEEDED) {
			endGame(true);
		}
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
	
	public void endGame(boolean gameWon)  {
		TileState state = gameWon ? TileState.WIN_MINE : TileState.MINE;
		for (Tile[] value : tiles) {
			for (Tile tile : value) {
				if (tile.isMine) {
					tile.setState(state);
				}
			}
		}
	}
}
