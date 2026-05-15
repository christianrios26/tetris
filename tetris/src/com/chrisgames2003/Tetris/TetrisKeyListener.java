package com.chrisgames2003.Tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

final class TetrisKeyListener implements KeyListener
{
	protected final HashMap<Integer, Boolean> KEY_CODES_MAP = new HashMap<>();
	protected TetrisGame tetris;
	private int prevPieceFallFrameCount;
	
	protected TetrisKeyListener(TetrisGame tetris)
	{
		KEY_CODES_MAP.put(KeyEvent.VK_LEFT, false);
		KEY_CODES_MAP.put(KeyEvent.VK_RIGHT, false);
		KEY_CODES_MAP.put(KeyEvent.VK_Z, false);
		KEY_CODES_MAP.put(KeyEvent.VK_X, false);
		KEY_CODES_MAP.put(KeyEvent.VK_0, false);
		KEY_CODES_MAP.put(KeyEvent.VK_1, false);
		KEY_CODES_MAP.put(KeyEvent.VK_2, false);
		KEY_CODES_MAP.put(KeyEvent.VK_3, false);
		KEY_CODES_MAP.put(KeyEvent.VK_4, false);
		KEY_CODES_MAP.put(KeyEvent.VK_5, false);
		KEY_CODES_MAP.put(KeyEvent.VK_6, false);
		KEY_CODES_MAP.put(KeyEvent.VK_7, false);
		KEY_CODES_MAP.put(KeyEvent.VK_8, false);
		KEY_CODES_MAP.put(KeyEvent.VK_9, false);
		KEY_CODES_MAP.put(KeyEvent.VK_ENTER, false);
		KEY_CODES_MAP.put(KeyEvent.VK_SHIFT, false);
		KEY_CODES_MAP.put(KeyEvent.VK_DOWN, false);
		this.tetris = tetris;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		Boolean keyIsBeingHeld = KEY_CODES_MAP.get(keyCode);
		
		if (keyIsBeingHeld != null && !keyIsBeingHeld)
		{
			switch (keyCode)
			{
			case KeyEvent.VK_LEFT:
				if (!KEY_CODES_MAP.get(KeyEvent.VK_RIGHT))
				{
					tetris.autoShiftFrameInterval = tetris.currentPiece.move(TetrisGame.LEFT) ? tetris.autoShiftFrameInterval : 1;
					tetris.autoShiftTimerStarted = true;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (!KEY_CODES_MAP.get(KeyEvent.VK_LEFT))
				{
					tetris.autoShiftFrameInterval = tetris.currentPiece.move(TetrisGame.RIGHT) ? tetris.autoShiftFrameInterval : 1;
					tetris.autoShiftTimerStarted = true;
				}
				break;
			case KeyEvent.VK_Z:
				tetris.currentPiece.rotate(TetrisGame.CLOCKWISE);
				break;
			case KeyEvent.VK_X:
				tetris.currentPiece.rotate(TetrisGame.COUNTERCLOCKWISE);
				break;
			// DEBUG HOTKEYS
			case KeyEvent.VK_0:
				tetris.setLevel(0);
				break;
			case KeyEvent.VK_1:
				tetris.setLevel(1);
				break;
			case KeyEvent.VK_2:
				tetris.setLevel(2);
				break;
			case KeyEvent.VK_3:
				tetris.setLevel(3);
				break;
			case KeyEvent.VK_4:
				tetris.setLevel(4);
				break;
			case KeyEvent.VK_5:
				tetris.setLevel(5);
				break;
			case KeyEvent.VK_6:
				tetris.setLevel(6);
				break;
			case KeyEvent.VK_7:
				tetris.setLevel(7);
				break;
			case KeyEvent.VK_8:
				tetris.setLevel(8);
				break;
			case KeyEvent.VK_9:
				tetris.setLevel(9);
				break;
			case KeyEvent.VK_SHIFT: // Set to faster levels for testing purposes
				if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT)
					tetris.setLevel((tetris.level + 10) % 20);
				break;
			case KeyEvent.VK_ENTER: // Clear game board
				for (int i = 2; i < 22; i++)
    			{
    				for (int j = 0; j < 10; j++)
    				{
						TetrisBlock block = (TetrisBlock) tetris.GAME_BOARD.getCell(i, j);
						if (block != null && !block.belongsToPiece)
						{
							tetris.BLOCKS.remove(block);
							tetris.GAME_BOARD.setCell(i, j, null);
						}
    				}
    			}
				break;
			case KeyEvent.VK_DOWN:
				tetris.pieceFallFrameInterval = 2;
				prevPieceFallFrameCount = tetris.pieceFallFrameCounter;
				break;
			}
		}
		
		KEY_CODES_MAP.put(keyCode, true); // this key has been pressed, so put true in the map
		/**/
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		KEY_CODES_MAP.put(keyCode, false); // this key has been released, so put false in the map
		
		switch (keyCode)
		{
		case KeyEvent.VK_LEFT:
			tetris.autoShiftFrameCounter = 0;
			tetris.autoShiftFrameInterval = 16;
			if (KEY_CODES_MAP.get(KeyEvent.VK_RIGHT))
			{
				tetris.currentPiece.move(TetrisGame.RIGHT);
				tetris.autoShiftTimerStarted = true;
			}
			break;
		case KeyEvent.VK_RIGHT:
			tetris.autoShiftFrameCounter = 0;
			tetris.autoShiftFrameInterval = 16;
			if (KEY_CODES_MAP.get(KeyEvent.VK_LEFT))
			{
				tetris.currentPiece.move(TetrisGame.LEFT);
				tetris.autoShiftTimerStarted = true;
			}
			break;
		case KeyEvent.VK_DOWN:
			tetris.pieceFallFrameInterval = tetris.getNewPieceFallFrameInterval();
			tetris.pieceFallFrameCounter = prevPieceFallFrameCount;
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
}
