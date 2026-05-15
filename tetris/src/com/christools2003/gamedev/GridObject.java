package com.christools2003.gamedev;

/**
 * An abstract class that represents an object that can be placed inside a {@link Grid}.
 * 
 * <p>
 * The class is intended to be used with the {@code Grid} class for creating games that naturally have objects laid out on a square grid system. The coordinate positions in the 
 * class are to be interpreted as coordinates within a game's defined coordinate system. The class includes methods for setting/getting the row/column of a {@code GridObject} 
 * (0-indexed in the same way as standard two-dimensional arrays), setting the {@code Grid} a {@code GridObject} belongs to, and getting the precise game coordinates of a 
 * {@code GridObject} relative to its {@code Grid}.
 * </p>
 * 
 * @author Christian Rios
 * @version 1.0
 * @since 1.0
 */
public abstract class GridObject
{
	private Grid grid;
	private int row;
	private int column;
	
	/**
	 * Constructs a {@code GridObject} belonging to the specified {@link Grid} at the specified row and column of the {@code Grid}.
	 * @param grid the desired {@code Grid} to assign the {@code GridObject} to.
	 * @param row the desired row to place the {@code GridObject} in.
	 * @param column the desired column to place the {@code GridObject} in.
	 * @throws ArrayIndexOutOfBoundsException if the provided row and column are not both within the bounds of the {@code Grid}.
	 */
	public GridObject(Grid grid, int row, int column)
	{
		setGrid(grid, row, column);
	}
	
	/**
	 * Assigns the {@code GridObject} to the specified {@link Grid} at the specified row and column of the {@code Grid}. If the {@code GridObject} belonged to a previous 
	 * {@code Grid}, then it is removed from that {@code Grid} before being assigned to the new one.
	 * @param grid the desired {@code Grid} to assign the {@code GridObject} to.
	 * @param row the desired row to place the {@code GridObject} in.
	 * @param column the desired column to place the {@code GridObject} in.
	 * @throws ArrayIndexOutOfBoundsException if the provided row and column are not both within the bounds of the {@code Grid}.
	 */
	public void setGrid(Grid grid, int row, int column)
	{
		if (this.grid != null)
			this.grid.setCell(this.row, this.column, null);
		
		GridObject collidingObject = grid.getCell(row, column);
		if (collidingObject == null || !collidingObject.equals(this))
			grid.setCell(row, column, this);
		
		this.grid = grid;
		this.row = row;
		this.column = column;
	}

	/**
	 * Gets the {@link Grid} that the {@code GridObject} is assigned to.
	 * @return a {@code Grid} object representing the {@code Grid} that the {@code GridObject} is assigned to.
	 */
	public Grid getGrid()
	{
		return grid;
	}
	
	/**
	 * Sets the row of the {@code GridObject} (0-indexed in the same way as standard two-dimensional arrays).
	 * @param row the desired row of the {@code GridObject}.
	 * @throws ArrayIndexOutOfBoundsException if the provided row is not within the bounds of the {@link Grid}.
	 */
	public void setRow(int row)
	{	
		grid.setCell(this.row, column, null);
		this.row = row;
		grid.setCell(this.row, column, this);
	}
	
	/**
	 * Gets the row of the {@code GridObject} (0-indexed in the same way as standard two-dimensional arrays).
	 * @return an {@code int} representing the row of the {@code GridObject}.
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * Sets the column of the {@code GridObject} (0-indexed in the same way as standard two-dimensional arrays).
	 * @param column the desired column of the {@code GridObject}.
	 * @throws ArrayIndexOutOfBoundsException if the provided column is not within the bounds of the {@link Grid}.
	 */
	public void setColumn(int column)
	{
		grid.setCell(row, this.column, null);
		this.column = column;
		grid.setCell(row, this.column, this);
	}
	
	/**
	 * Gets the column of the {@code GridObject} (0-indexed in the same way as standard two-dimensional arrays).
	 * @return an {@code int} representing the column of the {@code GridObject}.
	 */
	public int getColumn()
	{
		return column;
	}
	
	/**
	 * Sets both the row and column of the {@code GridObject} (0-indexed in the same way as standard two-dimensional arrays).
	 * @param row the desired row of the {@code GridObject}.
	 * @param column the desired column of the {@code GridObject}.
	 * @throws ArrayIndexOutOfBoundsException if the provided row and column are not both within the bounds of the {@link Grid}.
	 */
	public void setCell(int row, int column)
	{
		grid.setCell(this.row, this.column, null);
		this.row = row;
		this.column = column;
		grid.setCell(this.row, this.column, this);
	}
	
	/**
	 * Gets the <i>x</i> coordinate of the top-left corner of the {@code GridObject}'s cell.
	 * @return an {@code int} representing the <i>x</i> coordinate of the top-left corner of the {@code GridObject}'s cell.
	 */
	public int getXPosition()
	{
		return grid.getXPosition() + column * grid.getCellSize();
	}
	
	/**
	 * Gets the <i>y</i> coordinate of the top-left corner of the {@code GridObject}'s cell.
	 * @return an {@code int} representing the <i>y</i> coordinate of the top-left corner of the {@code GridObject}'s cell.
	 */
	public int getYPosition()
	{
		return grid.getYPosition() + row * grid.getCellSize();
	}
}
