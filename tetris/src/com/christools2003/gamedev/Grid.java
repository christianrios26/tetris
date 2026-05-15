package com.christools2003.gamedev;

/**
 * A class that represents a grid with square cells containing objects of type {@link GridObject}.
 * 
 * <p>
 * The class is intended to be used with the {@code GridObject} class for creating games that naturally have objects laid out on a square grid system. The coordinate positions 
 * in the class are to be interpreted as coordinates within a game's defined coordinate system. The class includes methods for setting/getting the position of the entire grid, 
 * defining the side length ({@code cellSize}) of each square cell, and setting/getting objects in cells using their row and column (0-indexed in the same way as standard 
 * two-dimensional arrays). Empty cells contain {@code null}. The number of rows/columns of a {@code Grid} cannot be changed after creation.
 * </p>
 * 
 * @author Christian Rios
 * @version 1.0
 * @since 1.0
 */
public class Grid
{
	private int xPosition;
	private int yPosition;
	private int cellSize;
	private final GridObject[][] CELL;
	
	/**
	 * Constructs a {@code Grid} with the specified position, dimensions, and square cell size.
	 * @param xPosition the <i>x</i> coordinate of the top-left corner of the {@code Grid}.
	 * @param yPosition the <i>y</i> coordinate of the top-left corner of the {@code Grid}.
	 * @param rows the number of rows to construct the {@code Grid} with.
	 * @param columns the number of columns to construct the {@code Grid} with.
	 * @param cellSize the side length of each square cell of the {@code Grid}.
	 * @throws NegativeArraySizeException if one or more of the provided row and column amounts are negative.
	 * @throws IllegalArgumentException if {@code cellSize} is not greater than 0.
	 */
	public Grid(int xPosition, int yPosition, int rows, int columns, int cellSize)
	{
		if (cellSize <= 0) throw new IllegalArgumentException("Illegal argument \"" + cellSize + "\". Argument cellSize must be a positive integer.");
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.cellSize = cellSize;
		CELL = new GridObject[rows][columns];
	}

	// Getters and Setters --------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Sets the <i>x</i> coordinate of the top-left corner of the {@code Grid}.
	 * @param xPosition the desired <i>x</i> coordinate of the top-left corner of the {@code Grid}.
	 */
	public void setXPosition(int xPosition)
	{
		this.xPosition = xPosition;
	}
	
	/**
	 * Gets the <i>x</i> coordinate of the top-left corner of the {@code Grid}.
	 * @return an {@code int} representing the <i>x</i> coordinate of the top-left corner of the {@code Grid}.
	 */
	public int getXPosition()
	{
		return xPosition;
	}

	/**
	 * Sets the <i>y</i> coordinate of the top-left corner of the {@code Grid}.
	 * @param yPosition the desired <i>y</i> coordinate of the top-left corner of the {@code Grid}.
	 */
	public void setYPosition(int yPosition)
	{
		this.yPosition = yPosition;
	}
	
	/**
	 * Gets the <i>y</i> coordinate of the top-left corner of the {@code Grid}.
	 * @return an {@code int} representing the <i>y</i> coordinate of the top-left corner of the {@code Grid}.
	 */
	public int getYPosition()
	{
		return yPosition;
	}

	/**
	 * Sets the side length of each square cell in the {@code Grid}.
	 * @param cellSize the desired side length of each square cell in the {@code Grid}.
	 * @throws IllegalArgumentException if {@code cellSize} is not greater than 0.
	 */
	public void setCellSize(int cellSize)
	{
		if (cellSize <= 0) throw new IllegalArgumentException("Illegal argument \"" + cellSize + "\". Argument cellSize must be a positive integer.");
		this.cellSize = cellSize;
	}
	
	/**
	 * Gets the side length of each square cell in the {@code Grid}.
	 * @return an {@code int} representing the side length of each square cell in the {@code Grid}.
	 */
	public int getCellSize()
	{
		return cellSize;
	}

	/**
	 * Places the specified {@link GridObject} in the specified row and column of the {@code Grid}, overwriting the object previously in the cell.
	 * @param row the desired row to place the {@code GridObject} in.
	 * @param column the desired column to place the {@code GridObject} in.
	 * @param object the desired {@code GridObject} to place in the {@code Grid}. To make this cell empty, use {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if the provided row and column are not both within the bounds of the {@code Grid}.
	 */
	public void setCell(int row, int column, GridObject object)
	{
		CELL[row][column] = object;
	}
	
	/**
	 * Gets the {@link GridObject} in the specified cell of the {@code Grid}.
	 * @param row the row of the desired {@code GridObject}.
	 * @param column the column of the desired {@code GridObject}.
	 * @return the {@code GridObject} at the specified row and column of the {@code Grid}. If the cell is empty, returns {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if the provided row and column are not both within the bounds of the {@code Grid}.
	 */
	public GridObject getCell(int row, int column)
	{
		return CELL[row][column];
	}
	
	/**
	 * Sets both the <i>x</i> and <i>y</i> coordinates of the top-left corner of the {@code Grid}.
	 * @param xPosition the desired <i>x</i> coordinate of the top-left corner of the {@code Grid}.
	 * @param yPosition the desired <i>y</i> coordinate of the top-left corner of the {@code Grid}.
	 */
	public void setPosition(int xPosition, int yPosition)
	{
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	/**
	 * Gets the number of rows the {@code Grid} contains.
	 * @return an {@code int} representing the number of rows the {@code Grid} contains.
	 */
	public int getRows()
	{
		return CELL.length;
	}

	/**
	 * Gets the number of columns the {@code Grid} contains.
	 * @return an {@code int} representing the number of columns the {@code Grid} contains.
	 */
	public int getColumns()
	{
		return CELL[0].length;
	}
}
