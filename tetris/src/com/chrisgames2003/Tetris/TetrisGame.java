package com.chrisgames2003.Tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.christools2003.gamedev.GameFrame;
import com.christools2003.gamedev.Grid;

public final class TetrisGame extends GameFrame
{
	protected static final int GAME_WIDTH = 768;
	protected static final int GAME_HEIGHT = 720;
	protected static final int CLOCKWISE = 1;
	protected static final int COUNTERCLOCKWISE = -1;
	protected static final int LEFT = 2;
	protected static final int RIGHT = 3;
	// Use these later
	protected static final String APP_DATA_PATH = System.getenv("LOCALAPPDATA") + "/Tetris";
	protected static final String SAVE_FILE_PATH = APP_DATA_PATH + "/tetris.save";
	protected static final String FONT_PATH = "resources/ARCADECLASSIC.ttf";
	protected static final String ICON_PATH = "resources/icon.png";
	// ------------------------------------------------------------------------------------------------
	protected static final char[] PIECE_TYPES = {'T', 'J', 'L', 'Z', 'S', 'I', 'O', ' '};
	protected static final Color[][] LEVEL_COLORS = new Color[10][2];
	protected final Grid GAME_BOARD = new Grid(276, 98, 22, 10, 24);
	protected final Grid NEXT_BOX = new Grid(0, 0, 2, 4, 24);
	protected final Random RAND = new Random();
	protected final long SEED = RAND.nextLong();
	protected final Set<TetrisBlock> BLOCKS = new HashSet<>();
	protected final ArrayList<Integer> LINES_TO_CLEAR = new ArrayList<>();
	protected int level;
	protected int globalFrameCounter;
	protected int pieceFallFrameInterval;
	protected int pieceFallFrameCounter;
	protected int pieceLockFrameInterval;
	protected int pieceLockFrameCounter;
	protected int autoShiftFrameInterval;
	protected int autoShiftFrameCounter;
	protected int lineClearAnimationStage;
	protected boolean autoShiftTimerStarted;
	protected boolean lineClearsStarted;
	protected TetrisPiece currentPiece;
	protected TetrisPiece nextPiece;
	protected TetrisKeyListener keyListener;
	protected ScheduledExecutorService scheduler;
	protected Font font;
	
	public TetrisGame()
	{
		super("Tetris", GAME_WIDTH, GAME_HEIGHT);
		
		// Initialize the font and icon
		try
		{
			ClassLoader loader = getClass().getClassLoader();
			font = Font.createFont(Font.TRUETYPE_FONT, loader.getResourceAsStream(FONT_PATH)).deriveFont(Font.PLAIN, 0); // font size doesn't matter here
			setIcon(ImageIO.read(loader.getResourceAsStream(ICON_PATH)));
		}
		catch (FontFormatException | IOException e)
		{
			e.printStackTrace();
		}
		
		// Populate level colors array with color constants
		LEVEL_COLORS[0][0] = new Color(0x4240FF);
		LEVEL_COLORS[0][1] = new Color(0x64B0FF);
		LEVEL_COLORS[1][0] = new Color(0x0C9300);
		LEVEL_COLORS[1][1] = new Color(0x88D800);
		LEVEL_COLORS[2][0] = new Color(0xA01ACC);
		LEVEL_COLORS[2][1] = new Color(0xF36AFF);
		LEVEL_COLORS[3][0] = new Color(0x4240FF);
		LEVEL_COLORS[3][1] = new Color(0x5CE430);
		LEVEL_COLORS[4][0] = new Color(0xB71E7B);
		LEVEL_COLORS[4][1] = new Color(0x45E082);
		LEVEL_COLORS[5][0] = new Color(0x45E082);
		LEVEL_COLORS[5][1] = new Color(0x9290FF);
		LEVEL_COLORS[6][0] = new Color(0xB53120);
		LEVEL_COLORS[6][1] = new Color(0x666666);
		LEVEL_COLORS[7][0] = new Color(0x7527FE);
		LEVEL_COLORS[7][1] = new Color(0x6E0040);
		LEVEL_COLORS[8][0] = new Color(0x4240FF);
		LEVEL_COLORS[8][1] = new Color(0xB53120);
		LEVEL_COLORS[9][0] = new Color(0xB53120);
		LEVEL_COLORS[9][1] = new Color(0xEA9E22);
		
		RAND.setSeed(SEED);
		setLevel(0); // also sets pieceFallFrameInterval
		globalFrameCounter = 0;
		pieceFallFrameCounter = 0;
		pieceLockFrameInterval = getNewPieceLockFrameInterval(21);
		pieceLockFrameCounter = 0;
		autoShiftFrameInterval = 16;
		autoShiftFrameCounter = 0;
		lineClearAnimationStage = 1;
		autoShiftTimerStarted = false;
		lineClearsStarted = false;
		spawnNewPiece(PIECE_TYPES[RAND.nextInt(7)]); // initializes the current piece
		generateNextRandomPiece(); // initializes the next piece
		keyListener = new TetrisKeyListener(this);
		GAME_PANEL.addKeyListener(keyListener);
		repaintGame();
		
		// Scheduler contains code that runs for every frame of the game
		// Frame rate in FPS is calculated as follows: FPS = (amount of time unit per second)/(period)
		// To find the appropriate period for a desired FPS: period = (amount of time unit per second)/FPS
		// Since the period can only be a long integer, more precise frame rates can be achieved by using a smaller time unit
		// Frame rate of the game is 1000000000/16639267 = 60.0988012 = 60.0988 FPS (actual frame rate of Classic Tetris is 60.0988)
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> gameLoop(), 0, 16639267, TimeUnit.NANOSECONDS);
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		TetrisGame tetris = new TetrisGame();
	}
	
	protected void gameLoop()
	{	
		// Increment frame counters and reset them if necessary
		globalFrameCounter++;
		globalFrameCounter %= 4;
		
		pieceFallFrameCounter++;
		pieceFallFrameCounter = pieceFallFrameCounter > pieceFallFrameInterval ? 1 : pieceFallFrameCounter;
		
		pieceLockFrameCounter = pieceLockFrameCounter > 0 ? pieceLockFrameCounter + 1 : pieceLockFrameCounter;
		pieceLockFrameCounter = pieceLockFrameCounter > pieceLockFrameInterval ? 0 : pieceLockFrameCounter;
		
		autoShiftFrameCounter = autoShiftFrameCounter > 0 ? autoShiftFrameCounter + 1 : autoShiftFrameCounter;
		autoShiftFrameCounter = autoShiftFrameCounter > autoShiftFrameInterval ? 1 : autoShiftFrameCounter;
		
		// Start the auto shift frame counter
		if (autoShiftTimerStarted)
		{
			autoShiftFrameCounter = 1;
			autoShiftTimerStarted = false;
		}
		
		// Move the current piece using DAS
		if (autoShiftFrameCounter == autoShiftFrameInterval)
		{
			boolean pieceMoved = true;
			
			if (keyListener.KEY_CODES_MAP.get(KeyEvent.VK_LEFT) && !keyListener.KEY_CODES_MAP.get(KeyEvent.VK_RIGHT))
				pieceMoved = currentPiece.move(LEFT);
			else if (keyListener.KEY_CODES_MAP.get(KeyEvent.VK_RIGHT) && !keyListener.KEY_CODES_MAP.get(KeyEvent.VK_LEFT))
				pieceMoved = currentPiece.move(RIGHT);

			autoShiftFrameInterval = pieceMoved ? 6 : 1;
		}
		
		// Cause next piece to spawn when a piece locks onto the board without clearing any lines
		if (pieceLockFrameCounter == pieceLockFrameInterval)
		{
			spawnNewPiece(nextPiece.pieceType);
			generateNextRandomPiece();
			pieceFallFrameInterval = getNewPieceFallFrameInterval(); // used for reseting fall speed in the case the user was holding the DOWN key
			pieceFallFrameCounter = 0;
		}
		
		// Cause the piece to fall
		if (pieceFallFrameCounter == pieceFallFrameInterval && pieceLockFrameCounter == 0 && !lineClearsStarted)
		{
			boolean pieceCanFall = currentPiece.fall();
			if (!pieceCanFall)
			{
				// Lock the piece onto the board if it can't fall
				currentPiece.isLocked = true;
				for (TetrisBlock block : currentPiece.blocks)
				{
					block.belongsToPiece = false;
					BLOCKS.add(block);
				}
				
				// Check for line clears
				int currentRow = currentPiece.blocks[3].getRow();
				int maxRowsToClear = currentRow - currentPiece.blocks[0].getRow() + 1;
				for (int i = 0; i < maxRowsToClear && currentRow >= 2; i++)
				{			
					if (lineIsFull(currentRow))
						LINES_TO_CLEAR.add(currentRow);
					
					currentRow--;
				}
				
				lineClearsStarted = !LINES_TO_CLEAR.isEmpty();
				
				if (!lineClearsStarted) // start the piece lock frame counter if there are no lines to clear
				{
					pieceLockFrameInterval = getNewPieceLockFrameInterval(currentPiece.blocks[3].getRow());
					pieceLockFrameCounter = 1;
				}
			}
		}
		
		// Handle line clear animation and block shifts
		if (lineClearsStarted && globalFrameCounter == 0) 
		{
			int leftColumn = 5 - lineClearAnimationStage;
			int rightColumn = 4 + lineClearAnimationStage;
			
			if (leftColumn >= 0) // if line clear animation should still be active
			{
				for (int row : LINES_TO_CLEAR)
				{
					BLOCKS.remove(GAME_BOARD.getCell(row, leftColumn));
					BLOCKS.remove(GAME_BOARD.getCell(row, rightColumn));
					GAME_BOARD.setCell(row, leftColumn, null);
					GAME_BOARD.setCell(row, rightColumn, null);
				}
				lineClearAnimationStage++;
			}
			else // line clear animation has finished
			{
				// Shifts all required blocks down after line clear animation is done
				int bottomMostClearedRow = LINES_TO_CLEAR.get(0);
				int topMostBlockRow = GAME_BOARD.getRows() - 1;
				for (TetrisBlock block : BLOCKS)
					topMostBlockRow = block.getRow() < topMostBlockRow ? block.getRow() : topMostBlockRow;
				
				int shiftedRow = bottomMostClearedRow;
				for (int i = bottomMostClearedRow - 1; i >= topMostBlockRow; i--)
				{
					if (LINES_TO_CLEAR.contains(i)) continue;
					for (int j = 0; j < GAME_BOARD.getColumns(); j++)
					{
						TetrisBlock block = (TetrisBlock) GAME_BOARD.getCell(i, j);
						if (block != null)
							block.setRow(shiftedRow);
					}
					
					while (!lineIsEmpty(shiftedRow))
						shiftedRow--;
				}
				
				// Resets variables and spawns the next piece
				lineClearsStarted = false;
				lineClearAnimationStage = 1;
				LINES_TO_CLEAR.clear();
				spawnNewPiece(nextPiece.pieceType);
				generateNextRandomPiece();
			}
			
			repaintGame();
		}
	}
	
	protected void setLevel(int level)
	{
		this.level = level;
		pieceFallFrameInterval = getNewPieceFallFrameInterval();
	}
	
	protected int getNewPieceFallFrameInterval()
	{
		int[] pieceFallFrameIntervals = {48, 43, 38, 33, 28, 23, 18, 13, 8, 6, 5, 4, 3, 2, 1};
		if (level < 10) return pieceFallFrameIntervals[level];
		else if (level < 13) return pieceFallFrameIntervals[10];
		else if (level < 16) return pieceFallFrameIntervals[11];
		else if (level < 19) return pieceFallFrameIntervals[12];
		else if (level < 29) return pieceFallFrameIntervals[13];
		else return pieceFallFrameIntervals[14];
	}
	
	protected int getNewPieceLockFrameInterval(int pieceLockRow)
	{
		if (pieceLockRow < 8) return 18;
		else if (pieceLockRow < 12) return 16;
		else if (pieceLockRow < 16) return 14;
		else if (pieceLockRow < 20) return 12;
		else return 10;
	}
	
	@Override
	protected void drawGame()
	{
		// Draw a black background
		setColor(Color.BLACK);
		fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		
		// DEBUG: Test the font
		/*if (font != null)
		{
			setColor(Color.WHITE);
			drawString("LINES", 100, 100, 42, 1, font);
			drawString("TOP", 100, 150, 42, 1, font);
			drawString("SCORE", 100, 200, 42, 1, font);
		}/**/
		
		// Draw the current piece and next piece
		if (currentPiece != null) currentPiece.draw();
		if (nextPiece != null) nextPiece.draw();
		
		// Draw all blocks currently on the board not belonging to the current piece
		if (BLOCKS != null)
		{
			for (TetrisBlock block : BLOCKS)
				block.draw();
		}
		
		// Boundaries for playable game area
		setColor(new Color(0xB5EBF2));
		fillRect(273, 140, 243, 3);
		fillRect(270, 143, 3, 486);
		fillRect(516, 143, 3, 486);
		fillRect(273, 629, 243, 3);
		
		// Boundaries for next box
		fillRect(561, 308, 99, 3);
		fillRect(558, 311, 3, 126);
		fillRect(660, 311, 3, 126);
		fillRect(561, 437, 99, 3);
	}
	
	private boolean lineIsFull(int row)
	{
		for (int i = 0; i < GAME_BOARD.getColumns(); i++)
		{
			if (GAME_BOARD.getCell(row, i) == null)
				return false;
		}
		return true;
	}
	
	private boolean lineIsEmpty(int row)
	{
		for (int i = 0; i < GAME_BOARD.getColumns(); i++)
		{
			if (GAME_BOARD.getCell(row, i) != null)
				return false;
		}
		return true;
	}
	
	private void spawnNewPiece(char pieceType)
	{
		switch (pieceType)
		{
		case 'T', 'J', 'L', 'Z', 'O':
			currentPiece = new TetrisPiece(this, GAME_BOARD, 2, 4, pieceType);
			break;
		case 'S':
			currentPiece = new TetrisPiece(this, GAME_BOARD, 2, 5, pieceType);		
			break;
		case 'I':
			currentPiece = new TetrisPiece(this, GAME_BOARD, 2, 3, pieceType);
			break;
		}
	}
	
	private void generateNextRandomPiece()
	{
		char randomPiece = PIECE_TYPES[RAND.nextInt(8)];
		randomPiece = !(randomPiece == currentPiece.pieceType || randomPiece == ' ') ? randomPiece : PIECE_TYPES[RAND.nextInt(7)]; // basic safety against duplicate pieces
		
		switch (randomPiece)
		{
		case 'T', 'J', 'L', 'Z', 'S':
			NEXT_BOX.setXPosition(576);
			NEXT_BOX.setYPosition(362);
			nextPiece = new TetrisPiece(this, NEXT_BOX, 0, randomPiece != 'S' ? 0 : 1, randomPiece);
			break;
		case 'I':
			NEXT_BOX.setXPosition(564);
			NEXT_BOX.setYPosition(374);
			nextPiece = new TetrisPiece(this, NEXT_BOX, 0, 0, randomPiece);
			break;
		case 'O':
			NEXT_BOX.setXPosition(588);
			NEXT_BOX.setYPosition(362);
			nextPiece = new TetrisPiece(this, NEXT_BOX, 0, 0, randomPiece);
			break;
		}
	}
}
