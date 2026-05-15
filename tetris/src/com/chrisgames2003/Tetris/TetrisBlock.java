package com.chrisgames2003.Tetris;

import java.awt.Color;
import com.christools2003.gamedev.Grid;
import com.christools2003.gamedev.GridObject;

final class TetrisBlock extends GridObject
{
	protected TetrisGame tetris;
	protected boolean belongsToPiece;
	private Color color;
	private boolean isBordered;
	
	
	protected TetrisBlock(TetrisGame tetris, Grid grid, int row, int column, Color color, boolean isBordered)
	{
		super(grid, row, column);
		this.tetris = tetris;
		belongsToPiece = true;
		this.color = color;
		this.isBordered = isBordered;
	}
	
	// Draws a solid color or bordered block of a specific color
	protected void draw()
	{
		tetris.setColor(color);
		tetris.fillRect(getXPosition(), getYPosition(), 21, 21);
		tetris.setColor(Color.WHITE);
		tetris.fillRect(getXPosition(), getYPosition(), 3, 3);
		if (!isBordered)
		{
			tetris.fillRect(getXPosition() + 3, getYPosition() + 3, 6, 3);
			tetris.fillRect(getXPosition() + 3, getYPosition() + 6, 3, 3);
		}
		else
		{
			tetris.fillRect(getXPosition() + 3, getYPosition() + 3, 15, 15);
		}
	}
}