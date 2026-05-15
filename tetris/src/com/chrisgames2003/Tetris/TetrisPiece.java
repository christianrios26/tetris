package com.chrisgames2003.Tetris;

import java.util.Arrays;
import com.christools2003.gamedev.Grid;

final class TetrisPiece
{
	protected TetrisGame tetris;
	protected TetrisBlock[] blocks; // Stores blocks in order by row (top row first, left to right across a row, then down to the next row)
	protected char pieceType;
	protected boolean isLocked;
	private int rotationState; // Holds 0, 1, 2, or 3. Represents the number of clockwise rotations from the default piece orientation
	private int pivotRow; // Pivot variables are used for keeping track of the coordinates of the pivot (used for rotations) 
	private int pivotColumn;
	
	protected TetrisPiece(TetrisGame tetris, Grid grid, int row, int column, char pieceType)
	{	
		this.tetris = tetris;
		blocks = new TetrisBlock[4];
		this.pieceType = pieceType;
		isLocked = false;
		rotationState = 0;
		pivotRow = 2; // Somewhat arbitrarily chosen pivot (rest of the rotation code is based on it)
		pivotColumn = 5;
		
		// LEVEL_COLORS[level % 10][1] holds color for Z and L pieces
		// LEVEL_COLORS[level % 10][0] holds color for all other pieces
		switch (this.pieceType)
		{
		case 'T':
			blocks[0] = new TetrisBlock(tetris, grid, row, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			blocks[1] = new TetrisBlock(tetris, grid, row, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			blocks[2] = new TetrisBlock(tetris, grid, row, column + 2, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			blocks[3] = new TetrisBlock(tetris, grid, row + 1, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			break;
		case 'J':
			blocks[0] = new TetrisBlock(tetris, grid, row, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], false);
			blocks[1] = new TetrisBlock(tetris, grid, row, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], false);
			blocks[2] = new TetrisBlock(tetris, grid, row, column + 2, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], false);
			blocks[3] = new TetrisBlock(tetris, grid, row + 1, column + 2, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], false);
			break;
		case 'L':
			blocks[0] = new TetrisBlock(tetris, grid, row, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][1], false);
			blocks[1] = new TetrisBlock(tetris, grid, row, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][1], false);
			blocks[2] = new TetrisBlock(tetris, grid, row, column + 2, TetrisGame.LEVEL_COLORS[tetris.level % 10][1], false);
			blocks[3] = new TetrisBlock(tetris, grid, row + 1, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][1], false);
			break;
		case 'Z':
			blocks[0] = new TetrisBlock(tetris, grid, row, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][1], false);
			blocks[1] = new TetrisBlock(tetris, grid, row, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][1], false);
			blocks[2] = new TetrisBlock(tetris, grid, row + 1, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][1], false);
			blocks[3] = new TetrisBlock(tetris, grid, row + 1, column + 2, TetrisGame.LEVEL_COLORS[tetris.level % 10][1], false);
			break;
		case 'S':
			blocks[0] = new TetrisBlock(tetris, grid, row, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], false);
			blocks[1] = new TetrisBlock(tetris, grid, row, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], false);
			blocks[2] = new TetrisBlock(tetris, grid, row + 1, column - 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], false);
			blocks[3] = new TetrisBlock(tetris, grid, row + 1, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], false);
			break;
		case 'I':
			blocks[0] = new TetrisBlock(tetris, grid, row, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			blocks[1] = new TetrisBlock(tetris, grid, row, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			blocks[2] = new TetrisBlock(tetris, grid, row, column + 2, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			blocks[3] = new TetrisBlock(tetris, grid, row, column + 3, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			break;
		case 'O':
			blocks[0] = new TetrisBlock(tetris, grid, row, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			blocks[1] = new TetrisBlock(tetris, grid, row, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			blocks[2] = new TetrisBlock(tetris, grid, row + 1, column, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
			blocks[3] = new TetrisBlock(tetris, grid, row + 1, column + 1, TetrisGame.LEVEL_COLORS[tetris.level % 10][0], true);
		}
		
		tetris.repaintGame();
	}
	
	protected void draw()
	{
		for (TetrisBlock block : blocks)
		{
			if (!block.belongsToPiece) return;
			block.draw();
		}
	}
	
	protected boolean fall()
	{
		// Check if a piece is blocked from falling (return false if so) 
		for (TetrisBlock block : blocks)
		{
			if (block.getRow() == 21)
				return false;
			TetrisBlock blockBelow = (TetrisBlock) tetris.GAME_BOARD.getCell(block.getRow() + 1, block.getColumn());
			if (blockBelow != null && !blockBelow.belongsToPiece)
				return false;
		}
		
		// Move each block for this piece down a row (traversing blocks array in reverse order)
		for (int i = 3; i >= 0; i--)
		{
			TetrisBlock block = blocks[i];
			block.setRow(block.getRow() + 1);
		}
		
		pivotRow++;
		tetris.repaintGame();
		return true;
	}
	
	protected boolean move(int direction)
	{
		if (isLocked) return false;
		if (direction == TetrisGame.LEFT)
		{
			// Check if a piece is blocked from moving left (return false if so) 
			for (TetrisBlock block : blocks)
			{				
				if (block.getColumn() == 0)
					return false;
				TetrisBlock blockToLeft = (TetrisBlock) tetris.GAME_BOARD.getCell(block.getRow(), block.getColumn() - 1);
				if (blockToLeft != null && !blockToLeft.belongsToPiece)
					return false;
			}
			
			// Move each block for this piece left a column (traversing blocks array in sequential order)
			for (int i = 0; i < 4; i++)
			{
				TetrisBlock block = blocks[i];
				block.setColumn(block.getColumn() - 1);
			}
			
			pivotColumn--;
		}
		else // assume we're moving right
		{
			// Check if a piece is blocked from moving right (return if so)
			for (TetrisBlock block : blocks)
			{
				if (block.getColumn() == 9)
					return false;
				TetrisBlock blockToRight = (TetrisBlock) tetris.GAME_BOARD.getCell(block.getRow(), block.getColumn() + 1);
				if (blockToRight != null && !blockToRight.belongsToPiece)
					return false;
			}
			
			// Move each block for this piece right a column (traversing blocks array in reverse order)
			for (int i = 3; i >= 0; i--)
			{
				TetrisBlock block = blocks[i];
				block.setColumn(block.getColumn() + 1);
			}
			
			pivotColumn++;
		}
		
		tetris.repaintGame();
		return true;
	}
	
	protected boolean rotate(int direction)
	{
		if (isLocked) return false;
		// Update rotation state
		rotationState = (rotationState + direction) % 4;
		rotationState = rotationState < 0 ? 3 : rotationState;
		
		/* newBlockPos[i][j] will contain the row or column (represented by j) that blocks[i] should have when updating 
		 * the blocks array during a rotation */
		int[][] newBlockPos;
		
		switch (pieceType)
		{
		case 'T':
			newBlockPos = getNewBlockPosT();
			break;
		case 'J':
			newBlockPos = getNewBlockPosJ();
			break;
		case 'L':
			newBlockPos = getNewBlockPosL();
			break;
		case 'Z':
			newBlockPos = getNewBlockPosZ();
			break;
		case 'S':
			newBlockPos = getNewBlockPosS();
			break;
		case 'I':
			newBlockPos = getNewBlockPosI();
			break;
		default:
			// Add sound effect here later
			return false;
		}
		
		// Check to see if the piece has room to rotate (return false otherwise)
		for (int i = 0; i < blocks.length; i++)
		{
			int newBlockRow = newBlockPos[i][0];
			int newBlockColumn = newBlockPos[i][1];
			
			if (newBlockRow > 21 || newBlockRow < 0 || newBlockColumn > 9 || newBlockColumn < 0)
				return false;
			
			TetrisBlock blockAtNewPos = (TetrisBlock) tetris.GAME_BOARD.getCell(newBlockRow, newBlockColumn);
			if (blockAtNewPos != null && !blockAtNewPos.belongsToPiece)
				return false;
		}
		
		// Move all the blocks in the blocks array to appropriate positions
		for (int i = 0; i < blocks.length; i++)
		{
			int newBlockRow = newBlockPos[i][0];
			int newBlockColumn = newBlockPos[i][1];
			TetrisBlock block = blocks[i];
			TetrisBlock blockAtNewPos = (TetrisBlock) tetris.GAME_BOARD.getCell(newBlockRow, newBlockColumn);
			
			if (blockAtNewPos == null)
			{
				block.setCell(newBlockRow, newBlockColumn);
			}
			else if (!block.equals(blockAtNewPos))
			{
				// Swap positions of block and blockAtNewPos in the blocks array
				blocks[Arrays.asList(blocks).indexOf(blockAtNewPos)] = block;
				blocks[i] = blockAtNewPos;
			}
		}
		
		tetris.repaintGame();
		return true;
	}
	
	private int[][] getNewBlockPosT()
	{
		int[][] newBlockPos = new int[4][2];
		
		switch (rotationState)
		{
		case 0:
			newBlockPos[0][0] = pivotRow;
			newBlockPos[0][1] = pivotColumn - 1;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn + 1;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn;
			break;
		case 1:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn - 1;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn;
			break;
		case 2:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn - 1;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow;
			newBlockPos[3][1] = pivotColumn + 1;
			break;
		case 3:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn + 1;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn;
		}
		
		return newBlockPos;
	}
	
	private int[][] getNewBlockPosJ()
	{
		int[][] newBlockPos = new int[4][2];
		
		switch (rotationState)
		{
		case 0:
			newBlockPos[0][0] = pivotRow;
			newBlockPos[0][1] = pivotColumn - 1;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn + 1;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn + 1;
			break;
		case 1:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow + 1;
			newBlockPos[2][1] = pivotColumn - 1;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn;
			break;
		case 2:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn - 1;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn - 1;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow;
			newBlockPos[3][1] = pivotColumn + 1;
			break;
		case 3:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn;
			newBlockPos[1][0] = pivotRow - 1;
			newBlockPos[1][1] = pivotColumn + 1;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn;
			break;
		}
		
		return newBlockPos;
	}
	
	private int[][] getNewBlockPosL()
	{
		int[][] newBlockPos = new int[4][2];
		
		switch (rotationState)
		{
		case 0:
			newBlockPos[0][0] = pivotRow;
			newBlockPos[0][1] = pivotColumn - 1;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn + 1;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn - 1;
			break;
		case 1:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn - 1;
			newBlockPos[1][0] = pivotRow - 1;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn;
			break;
		case 2:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn + 1;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn - 1;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow;
			newBlockPos[3][1] = pivotColumn + 1;
			break;
		case 3:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow + 1;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn + 1;
			break;
		}
		
		return newBlockPos;
	}
	
	private int[][] getNewBlockPosZ()
	{
		int[][] newBlockPos = new int[4][2];
		
		switch (rotationState)
		{
		case 0, 2:
			newBlockPos[0][0] = pivotRow;
			newBlockPos[0][1] = pivotColumn - 1;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow + 1;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn + 1;
			break;
		case 1, 3:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn + 1;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn + 1;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn;
			break;
		}
		
		return newBlockPos;
	}
	
	private int[][] getNewBlockPosS()
	{
		int[][] newBlockPos = new int[4][2];
		
		switch (rotationState)
		{
		case 0, 2:
			newBlockPos[0][0] = pivotRow;
			newBlockPos[0][1] = pivotColumn;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn + 1;
			newBlockPos[2][0] = pivotRow + 1;
			newBlockPos[2][1] = pivotColumn - 1;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn;
			break;
		case 1, 3:
			newBlockPos[0][0] = pivotRow - 1;
			newBlockPos[0][1] = pivotColumn;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn + 1;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn + 1;
			break;
		}
		
		return newBlockPos;
	}
	
	private int[][] getNewBlockPosI()
	{
		int[][] newBlockPos = new int[4][2];
		
		switch (rotationState)
		{
		case 0, 2:
			newBlockPos[0][0] = pivotRow;
			newBlockPos[0][1] = pivotColumn - 2;
			newBlockPos[1][0] = pivotRow;
			newBlockPos[1][1] = pivotColumn - 1;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow;
			newBlockPos[3][1] = pivotColumn + 1;
			break;
		case 1, 3:
			newBlockPos[0][0] = pivotRow - 2;
			newBlockPos[0][1] = pivotColumn;
			newBlockPos[1][0] = pivotRow - 1;
			newBlockPos[1][1] = pivotColumn;
			newBlockPos[2][0] = pivotRow;
			newBlockPos[2][1] = pivotColumn;
			newBlockPos[3][0] = pivotRow + 1;
			newBlockPos[3][1] = pivotColumn;
			break;
		}
		
		return newBlockPos;
	}
}