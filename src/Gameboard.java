package blah;
import java.util.ArrayList;
import java.util.Scanner;

public class Gameboard {
	private char[][] board;
	private boolean turnBlack = true;
	private Scanner scan = new Scanner(System.in);
	private boolean print = true;

	public Gameboard() {
		char[][] boardSetup = { { '-', 'w', '-', 'w', '-', 'w', '-', 'w' }, { 'w', '-', 'w', '-', 'w', '-', 'w', '-' },
				{ '-', 'w', '-', 'w', '-', 'w', '-', 'w' }, { '-', '-', '-', '-', '-', '-', '-', '-' },
				{ '-', '-', '-', '-', '-', '-', '-', '-' }, { 'b', '-', 'b', '-', 'b', '-', 'b', '-' },
				{ '-', 'b', '-', 'b', '-', 'b', '-', 'b' }, { 'b', '-', 'b', '-', 'b', '-', 'b', '-' } };
		board = boardSetup;
	}

	public void intro() {
		System.out.println(
				"Welcome to checkers, the goal of the game is to capture all of your enemies pieces. \nAt the beginning of your turn you will be prompted to jump if you can. Note that if a piece can still jump after jumping before, it can jump again. \nIf you do not have to jump, you will be prompted to move.\nIn order to select a piece to jump/move enter the coordinates as given. Note that they represent coordinates on a 2D array. \nThe game will prompt you with what directions you can move each piece, so simply enter the direction when prompted. \nIf any of your pieces reach the far end of the board, they will become kings and can move/jump forwards and backwards. \nLastly, after you select a piece, you must move/jump with it.");
	}

	public void play() {
		if (turnBlack) {
			System.out.println("It is the black player's turn.");
			if (mustJump()) {
			} else {
				move();
			}
			turnBlack = !turnBlack;
		} else {
			System.out.println("It is the white player's turn.");
			if (mustJump()) {
			} else {
				move();
			}
			turnBlack = !turnBlack;
		}
	}

	public void move() {
		int xTemp = -1;
		int yTemp = -1;
		ArrayList<String> movePieces = new ArrayList<String>();
		for (char[] r : board) {
			xTemp++;
			yTemp = -1;
			for (char c : r) {
				yTemp++;
				if (((c + "").toLowerCase().toCharArray()[0] == 'b' && turnBlack)
						|| ((c + "").toLowerCase().toCharArray()[0] == 'w' && !turnBlack)) {
					if (canMove(xTemp, yTemp) || canMoveKing(xTemp, yTemp)) {
						movePieces.add("" + xTemp + yTemp);
					}
				}
			}
		}
		if (movePieces.size() > 0) {
			String str = "";
			if (movePieces.size() > 1) {
				System.out.println("Any of the following pieces can move: " + movePieces.toString());
				System.out.println("Enter the coordinates of one of those pieces.");
				str = validPiece(movePieces);
			} else {
				System.out.println("Your only piece that can move is at: " + movePieces.get(0));
				str = "" + movePieces.get(0);
			}
			int x = Integer.parseInt(str.charAt(0) + "");
			int y = Integer.parseInt(str.charAt(1) + "");
			boolean left = false;
			boolean right = false;
			boolean forwardRight = false;
			boolean forwardLeft = false;
			boolean backwardRight = false;
			boolean backwardLeft = false;
			String kingMoveDirections = "That piece can move";
			if (!isKing(x, y) && canMoveLeft(x, y)) {
				left = true;
			}
			if (!isKing(x, y) && canMoveRight(x, y)) {
				right = true;
			}
			if (isKing(x, y) && canMoveKingForwardRight(x, y)) {
				forwardRight = true;
				kingMoveDirections += " forward right";
			}
			if (isKing(x, y) && canMoveKingForwardLeft(x, y)) {
				if (forwardRight) {
					kingMoveDirections += ",";
				}
				forwardLeft = true;
				kingMoveDirections += " forward left";
			}
			if (isKing(x, y) && canMoveKingBackwardRight(x, y)) {
				if (forwardRight || forwardLeft) {
					kingMoveDirections += ",";
				}
				backwardRight = true;
				kingMoveDirections += " backward right";
			}
			if (isKing(x, y) && canMoveKingBackwardLeft(x, y)) {
				if (forwardRight || forwardLeft || backwardRight) {
					kingMoveDirections += ",";
				}
				backwardLeft = true;
				kingMoveDirections += " backward left";
			}
			if (left && !right) {
				System.out.println("This piece can only move left, it has moved.");
				moveLeft(x, y);
			} else if (right && !left) {
				System.out.println("This piece can only move right, it has moved.");
				moveRight(x, y);
			} else if (right || left) {
				System.out.println("That piece can move left or right. Please enter your direction of choice.");
				String dir = validDirection();
				System.out.println("It has moved.");
				if (dir.equals("right")) {
					moveRight(x, y);
				} else {
					moveLeft(x, y);
				}
			} else if (forwardRight && !forwardLeft && !backwardRight && !backwardLeft) {
				System.out.println("This piece can only move forward right, it has moved.");
				moveKingForwardRight(x, y);
			} else if (!forwardRight && forwardLeft && !backwardRight && !backwardLeft) {
				System.out.println("This piece can only move forward left, it has moved.");
				moveKingForwardLeft(x, y);
			} else if (!forwardRight && !forwardLeft && backwardRight && !backwardLeft) {
				System.out.println("This piece can only move backward right, it has moved.");
				moveKingBackwardRight(x, y);
			} else if (!forwardRight && !forwardLeft && !backwardRight && backwardLeft) {
				System.out.println("This piece can only move backward left, it has moved.");
				moveKingBackwardLeft(x, y);
			} else {
				System.out.println(kingMoveDirections + "." + " Please enter your direction of choice.");
				String dir = validDirectionKing(kingMoveDirections);
				System.out.println("It has moved.");
				if (dir.equals("forward right")) {
					moveKingForwardRight(x, y);
				} else if (dir.equals("forward left")) {
					moveKingForwardLeft(x, y);
				} else if (dir.equals("backward right")) {
					moveKingBackwardRight(x, y);
				} else {
					moveKingBackwardLeft(x, y);
				}
			}
			System.out.println("Your turn has ended.");
		}
		System.out.println("You do not have any pieces which can move.");
	}

	public String validDirection() {
		String dir = "-1";
		while (dir.equals("left") == false && dir.equals("right") == false) {
			dir = scan.nextLine().toLowerCase();
			if (dir.equals("left") == false && dir.equals("right") == false) {
				System.out.println("That is not a vaild direction. Please enter a valid direction.");
				dir = "-1";
			}
		}
		return dir;
	}

	public String validDirectionKing(String str) {
		String dir = "-1";
		while (!str.contains(dir) || !(dir.equals("forward right") || dir.equals("forward left")
				|| dir.equals("backward right") || dir.equals("backward left"))) {
			dir = scan.nextLine().toLowerCase();
			if (!str.contains(dir) || !(dir.equals("forward right") || dir.equals("forward left")
					|| dir.equals("backward right") || dir.equals("backward left"))) {
				System.out.println("That is not a vaild direction. Please enter a valid direction.");
				dir = "-1";
			}
		}
		return dir;
	}

	public boolean isKing(int x, int y) {
		if (board[x][y] == 'B' || board[x][y] == 'W') {
			return true;
		}
		return false;
	}

	public boolean canMoveKingForwardLeft(int x, int y) {
		if ((board[x][y] == 'B') && (x > 0 && y > 0) && (board[x - 1][y - 1] == '-')) {
			return true;
		} else if ((board[x][y] == 'W') && (x < 7 && y < 7) && (board[x + 1][y + 1] == '-')) {
			return true;
		}
		return false;
	}

	public boolean canMoveKingForwardRight(int x, int y) {
		if ((board[x][y] == 'B') && (x > 0 && y < 7) && (board[x - 1][y + 1] == '-')) {
			return true;
		} else if ((board[x][y] == 'W') && (x < 7 && y > 0) && (board[x + 1][y - 1] == '-')) {
			return true;
		}
		return false;
	}

	public boolean canMoveKingBackwardLeft(int x, int y) {
		if ((board[x][y] == 'B') && (x < 7 && y > 0) && (board[x + 1][y - 1] == '-')) {
			return true;
		} else if ((board[x][y] == 'W') && (x > 0 && y < 7) && (board[x - 1][y + 1] == '-')) {
			return true;
		}
		return false;
	}

	public boolean canMoveKingBackwardRight(int x, int y) {
		if ((board[x][y] == 'B') && (x < 7 && y < 7) && (board[x + 1][y + 1] == '-')) {
			return true;
		} else if ((board[x][y] == 'W') && (x > 0 && y > 0) && (board[x - 1][y - 1] == '-')) {
			return true;
		}
		return false;
	}

	public void moveKingForwardLeft(int x, int y) {
		if (board[x][y] == 'B') {
			board[x][y] = '-';
			board[x - 1][y - 1] = 'B';
			System.out.println(toString());
		} else {
			board[x][y] = '-';
			board[x + 1][y + 1] = 'W';
			System.out.println(toString());
		}
	}

	public void moveKingForwardRight(int x, int y) {
		if (board[x][y] == 'B') {
			board[x][y] = '-';
			board[x - 1][y + 1] = 'B';
			System.out.println(toString());
		} else {
			board[x][y] = '-';
			board[x + 1][y - 1] = 'W';
			System.out.println(toString());
		}
	}

	public void moveKingBackwardLeft(int x, int y) {
		if (board[x][y] == 'B') {
			board[x][y] = '-';
			board[x + 1][y - 1] = 'B';
			System.out.println(toString());
		} else {
			board[x][y] = '-';
			board[x - 1][y + 1] = 'W';
			System.out.println(toString());
		}
	}

	public void moveKingBackwardRight(int x, int y) {
		if (board[x][y] == 'B') {
			board[x][y] = '-';
			board[x + 1][y + 1] = 'B';
			System.out.println(toString());
		} else {
			board[x][y] = '-';
			board[x - 1][y - 1] = 'W';
			System.out.println(toString());
		}
	}

	public boolean canMoveKing(int x, int y) {
		if (canMoveKingForwardLeft(x, y) || canMoveKingForwardRight(x, y) || canMoveKingBackwardLeft(x, y)
				|| canMoveKingBackwardRight(x, y)) {
			return true;
		}
		return false;
	}

	public void moveLeft(int x, int y) {
		if (board[x][y] == 'b') {
			board[x][y] = '-';
			board[x - 1][y - 1] = 'b';
			kingMe(x - 1, y - 1);
			System.out.println(toString());
		} else if (board[x][y] == 'w') {
			board[x][y] = '-';
			board[x + 1][y + 1] = 'w';
			kingMe(x + 1, y + 1);
			System.out.println(toString());
		}
	}

	public void moveRight(int x, int y) {
		if (board[x][y] == 'b') {
			board[x][y] = '-';
			board[x - 1][y + 1] = 'b';
			kingMe(x - 1, y + 1);
			System.out.println(toString());
		} else if (board[x][y] == 'w') {
			board[x][y] = '-';
			board[x + 1][y - 1] = 'w';
			kingMe(x + 1, y - 1);
			System.out.println(toString());
		}
	}

	public boolean canMove(int x, int y) {
		if (canMoveLeft(x, y) || canMoveRight(x, y)) {
			return true;
		}
		return false;
	}

	public boolean canMoveLeft(int x, int y) {
		if ((board[x][y] == 'b') && (x > 0 && y > 0) && (board[x - 1][y - 1] == '-')) {
			return true;
		} else if ((board[x][y] == 'w') && (x < 7 && y < 7) && (board[x + 1][y + 1] == '-')) {
			return true;
		}
		return false;
	}

	public boolean canMoveRight(int x, int y) {
		if ((board[x][y] == 'b') && (x > 0 && y < 7) && (board[x - 1][y + 1] == '-')) {
			return true;
		} else if ((board[x][y] == 'w') && (x < 7 && y > 0) && (board[x + 1][y - 1] == '-')) {
			return true;
		}
		return false;
	}

	public String validPiece(ArrayList<String> array) {
		String str = "-1";
		while (array.contains(str) == false) {
			str = scan.nextLine().toLowerCase();
			if (str.matches("[0-9]+") == false || array.contains(str) == false) {
				System.out.println("That is not a vaild piece. Please enter a valid piece.");
				str = "-1";
			}
		}
		return str;
	}

	public boolean mustJump() {
		print = true;
		int xTemp = -1;
		int yTemp = -1;
		int count = -1;
		ArrayList<String> jumpPieces = new ArrayList<String>();
		for (char[] r : board) {
			xTemp++;
			yTemp = -1;
			for (char c : r) {
				yTemp++;
				if (((c + "").toLowerCase().toCharArray()[0] == 'b' && turnBlack)
						|| ((c + "").toLowerCase().toCharArray()[0] == 'w' && !turnBlack)) {
					if (canJump(xTemp, yTemp) || canJumpKing(xTemp, yTemp)) {
						jumpPieces.add("" + xTemp + yTemp);
					}
				}
			}
		}
		if (jumpPieces.size() > 0) {
			String str = "";
			if (jumpPieces.size() > 1) {
				System.out.println("One of the following pieces must jump: " + jumpPieces.toString());
				System.out.println("Enter the coordinates of one of those pieces.");
				str = validPiece(jumpPieces);
			} else {
				System.out.println("Your only piece that can jump is at: " + jumpPieces.get(0));
				str = "" + jumpPieces.get(0);
			}
			xTemp = Integer.parseInt(str.charAt(0) + "");
			yTemp = Integer.parseInt(str.charAt(1) + "");
			if (isKing(xTemp, yTemp)) {

				while (canJumpKing(xTemp, yTemp)) {
					count++;
					if (count > 0) {
						System.out.println("That piece can still jump.");
					}
					boolean forwardRight = false;
					boolean forwardLeft = false;
					boolean backwardRight = false;
					boolean backwardLeft = false;
					String kingJumpDirections = "That piece can jump";
					if (canJumpKingForwardRight(xTemp, yTemp)) {
						forwardRight = true;
						kingJumpDirections += " forward right";
					}
					if (canJumpKingForwardLeft(xTemp, yTemp)) {
						if (forwardRight) {
							kingJumpDirections += ",";
						}
						forwardLeft = true;
						kingJumpDirections += " forward left";
					}
					if (canJumpKingBackwardRight(xTemp, yTemp)) {
						if (forwardRight || forwardLeft) {
							kingJumpDirections += ",";
						}
						backwardRight = true;
						kingJumpDirections += " backward right";
					}
					if (canJumpKingBackwardLeft(xTemp, yTemp)) {
						if (forwardRight || forwardLeft || backwardRight) {
							kingJumpDirections += ",";
						}
						backwardLeft = true;
						kingJumpDirections += " backward left";
					}
					if (forwardRight && !forwardLeft && !backwardRight && !backwardLeft) {
						System.out.println("This piece can only jump forward right, it has jumped.");
						if (board[xTemp][yTemp] == 'B') {
							jumpKingForwardRight(xTemp, yTemp);
							xTemp -= 2;
							yTemp += 2;
						} else if (board[xTemp][yTemp] == 'W') {
							jumpKingForwardRight(xTemp, yTemp);
							xTemp += 2;
							yTemp -= 2;
						}
					} else if (!forwardRight && forwardLeft && !backwardRight && !backwardLeft) {
						System.out.println("This piece can only jump forward left, it has jumped.");
						if (board[xTemp][yTemp] == 'B') {
							jumpKingForwardLeft(xTemp, yTemp);
							xTemp -= 2;
							yTemp -= 2;
						} else if (board[xTemp][yTemp] == 'W') {
							jumpKingForwardLeft(xTemp, yTemp);
							xTemp += 2;
							yTemp += 2;
						}
					} else if (!forwardRight && !forwardLeft && backwardRight && !backwardLeft) {
						System.out.println("This piece can only jump backward right, it has jumped.");
						if (board[xTemp][yTemp] == 'B') {
							jumpKingBackwardRight(xTemp, yTemp);
							xTemp += 2;
							yTemp += 2;
						} else if (board[xTemp][yTemp] == 'W') {
							jumpKingBackwardRight(xTemp, yTemp);
							xTemp -= 2;
							yTemp -= 2;
						}
					} else if (!forwardRight && !forwardLeft && !backwardRight && backwardLeft) {
						System.out.println("This piece can only jump backward left, it has jumped.");
						if (board[xTemp][yTemp] == 'B') {
							jumpKingBackwardLeft(xTemp, yTemp);
							xTemp += 2;
							yTemp -= 2;
						} else if (board[xTemp][yTemp] == 'W') {
							jumpKingBackwardLeft(xTemp, yTemp);
							xTemp -= 2;
							yTemp += 2;
						}
					} else {
						System.out.println(kingJumpDirections + ". Please enter your direction of choice.");
						String dir = validDirectionKing(kingJumpDirections);
						System.out.println("It has jumped.");
						if (dir.equals("forward right")) {
							if (board[xTemp][yTemp] == 'B') {
								jumpKingForwardRight(xTemp, yTemp);
								xTemp -= 2;
								yTemp += 2;
							} else if (board[xTemp][yTemp] == 'W') {
								jumpKingForwardRight(xTemp, yTemp);
								xTemp += 2;
								yTemp -= 2;
							}
						} else if (dir.equals("forward left")) {
							if (board[xTemp][yTemp] == 'B') {
								jumpKingForwardLeft(xTemp, yTemp);
								xTemp -= 2;
								yTemp -= 2;
							} else if (board[xTemp][yTemp] == 'W') {
								jumpKingForwardLeft(xTemp, yTemp);
								xTemp += 2;
								yTemp += 2;
							}
						} else if (dir.equals("backward right")) {
							if (board[xTemp][yTemp] == 'B') {
								jumpKingBackwardRight(xTemp, yTemp);
								xTemp += 2;
								yTemp += 2;
							} else if (board[xTemp][yTemp] == 'W') {
								jumpKingBackwardRight(xTemp, yTemp);
								xTemp -= 2;
								yTemp -= 2;
							}
						} else {
							if (board[xTemp][yTemp] == 'B') {
								jumpKingBackwardLeft(xTemp, yTemp);
								xTemp += 2;
								yTemp -= 2;
							} else if (board[xTemp][yTemp] == 'W') {
								jumpKingBackwardLeft(xTemp, yTemp);
								xTemp -= 2;
								yTemp += 2;
							}
						}
					}
				}
			} else {
				while (canJump(xTemp, yTemp)) {
					count++;
					if (count > 0) {
						System.out.println("That piece can still jump.");
					}
					boolean left = false;
					boolean right = false;
					if (canJumpLeft(xTemp, yTemp)) {
						left = true;
					}
					if (canJumpRight(xTemp, yTemp)) {
						right = true;
					}
					if (left && !right) {
						System.out.println("This piece can only jump left, it has jumped.");
						if (board[xTemp][yTemp] == 'b') {
							jumpLeft(xTemp, yTemp);
							xTemp -= 2;
							yTemp -= 2;
						} else if (board[xTemp][yTemp] == 'w') {
							jumpLeft(xTemp, yTemp);
							xTemp += 2;
							yTemp += 2;
						}
					} else if (right && !left) {
						System.out.println("This piece can only jump right, it has jumped.");
						if (board[xTemp][yTemp] == 'b') {
							jumpRight(xTemp, yTemp);
							xTemp -= 2;
							yTemp += 2;
						} else if (board[xTemp][yTemp] == 'w') {
							jumpRight(xTemp, yTemp);
							xTemp += 2;
							yTemp -= 2;
						}
					} else {
						System.out.println("That piece can jump left or right. Please enter your direction of choice.");
						String dir = validDirection();
						System.out.println("It has jumped.");
						if (dir.equals("right")) {
							if (board[xTemp][yTemp] == 'b') {
								jumpRight(xTemp, yTemp);
								xTemp -= 2;
								yTemp += 2;
							} else if (board[xTemp][yTemp] == 'w') {
								jumpRight(xTemp, yTemp);
								xTemp += 2;
								yTemp -= 2;
							}
						} else {
							if (board[xTemp][yTemp] == 'b') {
								jumpLeft(xTemp, yTemp);
								xTemp -= 2;
								yTemp -= 2;
							} else if (board[xTemp][yTemp] == 'w') {
								jumpLeft(xTemp, yTemp);
								xTemp += 2;
								yTemp += 2;
							}
						}
					}
					if (isKing(xTemp, yTemp) && canJumpKing(xTemp, yTemp)) {
						mustJump();
						print = false;
					}
				}
			}
			if (print) {
				System.out.println("Your turn has ended.");
			}
			return true;
		}
		return false;
	}

	public boolean isDone() {
		String str = "";
		String winner = "The winner is ";
		boolean black = false;
		boolean white = false;
		for (char[] r : board) {
			for (char c : r) {
				if ((c == 'b' || c == 'B') && !black) {
					str += 'b';
					black = true;
					winner += "the black player.";
				} else if ((c == 'w' || c == 'W') && !white) {
					str += 'w';
					white = true;
					winner += "the white player.";
				}
			}
		}
		if (str.length() > 1) {
			return false;
		}
		System.out.println("The game has ended. " + winner);
		return true;
	}

	public boolean canJumpKing(int x, int y) {
		if (canJumpKingForwardRight(x, y) || canJumpKingForwardLeft(x, y) || canJumpKingBackwardRight(x, y)
				|| canJumpKingBackwardLeft(x, y)) {
			return true;
		}
		return false;
	}

	public boolean canJumpKingForwardRight(int x, int y) {
		if ((board[x][y] == 'B') && (x > 1 && y < 6) && ((board[x - 1][y + 1] == 'w') || (board[x - 1][y + 1] == 'W'))
				&& board[x - 2][y + 2] == '-') {
			return true;
		} else if ((board[x][y] == 'W') && (x < 6 && y > 1)
				&& ((board[x + 1][y - 1] == 'b') || (board[x + 1][y - 1] == 'B')) && board[x + 2][y - 2] == '-') {
			return true;
		}
		return false;
	}

	public void jumpKingForwardRight(int x, int y) {
		if (board[x][y] == 'B') {
			board[x][y] = '-';
			board[x - 1][y + 1] = '-';
			board[x - 2][y + 2] = 'B';
		} else {
			board[x][y] = '-';
			board[x + 1][y - 1] = '-';
			board[x + 2][y - 2] = 'W';
		}
		System.out.print(toString() + "\n");
	}

	public void jumpKingForwardLeft(int x, int y) {
		if (board[x][y] == 'B') {
			board[x][y] = '-';
			board[x - 1][y - 1] = '-';
			board[x - 2][y - 2] = 'B';
		} else {
			board[x][y] = '-';
			board[x + 1][y + 1] = '-';
			board[x + 2][y + 2] = 'W';
		}
		System.out.print(toString() + "\n");
	}

	public void jumpKingBackwardRight(int x, int y) {
		if (board[x][y] == 'B') {
			board[x][y] = '-';
			board[x + 1][y + 1] = '-';
			board[x + 2][y + 2] = 'B';
		} else {
			board[x][y] = '-';
			board[x - 1][y - 1] = '-';
			board[x - 2][y - 2] = 'W';
		}
		System.out.print(toString() + "\n");
	}

	public void jumpKingBackwardLeft(int x, int y) {
		if (board[x][y] == 'B') {
			board[x][y] = '-';
			board[x + 1][y - 1] = '-';
			board[x + 2][y - 2] = 'B';
		} else {
			board[x][y] = '-';
			board[x - 1][y + 1] = '-';
			board[x - 2][y + 2] = 'W';
		}
		System.out.print(toString() + "\n");
	}

	public boolean canJumpKingForwardLeft(int x, int y) {
		if ((board[x][y] == 'B') && (x > 1 && y > 1) && ((board[x - 1][y - 1] == 'w') || (board[x - 1][y - 1] == 'W'))
				&& board[x - 2][y - 2] == '-') {
			return true;
		} else if ((board[x][y] == 'W') && (x < 6 && y < 6)
				&& ((board[x + 1][y + 1] == 'b') || (board[x + 1][y + 1] == 'B')) && board[x + 2][y + 2] == '-') {
			return true;
		}
		return false;
	}

	public boolean canJumpKingBackwardRight(int x, int y) {
		if ((board[x][y] == 'B') && (x < 6 && y < 6) && ((board[x + 1][y + 1] == 'w') || (board[x + 1][y + 1] == 'W'))
				&& board[x + 2][y + 2] == '-') {
			return true;
		} else if ((board[x][y] == 'W') && (x > 1 && y > 1)
				&& ((board[x - 1][y - 1] == 'b') || (board[x - 1][y - 1] == 'B')) && (board[x - 2][y - 2] == '-')) {
			return true;
		}
		return false;
	}

	public boolean canJumpKingBackwardLeft(int x, int y) {
		if ((board[x][y] == 'B') && (x < 6 && y > 1) && ((board[x + 1][y - 1] == 'w') || (board[x + 1][y - 1] == 'W'))
				&& (board[x + 2][y - 2] == '-')) {
			return true;
		} else if ((board[x][y] == 'W') && (x > 1 && y < 6)
				&& ((board[x - 1][y + 1] == 'b') || (board[x - 1][y + 1] == 'B')) && (board[x - 2][y + 2] == '-')) {
			return true;
		}
		return false;
	}

	public boolean canJump(int x, int y) {
		if (canJumpLeft(x, y) || canJumpRight(x, y)) {
			return true;
		}
		return false;
	}

	public boolean canJumpLeft(int x, int y) {
		if ((board[x][y] == 'b') && (x > 1 && y > 1) && ((board[x - 1][y - 1] == 'w') || (board[x - 1][y - 1] == 'W'))
				&& (board[x - 2][y - 2] == '-')) {
			return true;
		} else if ((board[x][y] == 'w') && (x < 6 && y < 6)
				&& ((board[x + 1][y + 1] == 'b' || board[x + 1][y + 1] == 'B')) && (board[x + 2][y + 2] == '-')) {
			return true;
		}
		return false;
	}

	public boolean canJumpRight(int x, int y) {
		if ((board[x][y] == 'b') && (x > 1 && y < 6) && ((board[x - 1][y + 1] == 'w') || (board[x - 1][y + 1] == 'W'))
				&& (board[x - 2][y + 2] == '-')) {
			return true;
		} else if ((board[x][y] == 'w') && (x < 6 && y > 1)
				&& ((board[x + 1][y - 1] == 'b') || (board[x + 1][y - 1] == 'B')) && (board[x + 2][y - 2] == '-')) {
			return true;
		}
		return false;
	}

	public void jumpRight(int x, int y) {
		if (canJumpRight(x, y) == true) {
			if (board[x][y] == 'b') {
				board[x][y] = '-';
				board[x - 1][y + 1] = '-';
				board[x - 2][y + 2] = 'b';
				kingMe(x - 2, y + 2);
			} else if (board[x][y] == 'w') {
				board[x][y] = '-';
				board[x + 1][y - 1] = '-';
				board[x + 2][y - 2] = 'w';
				kingMe(x + 2, y - 2);
			}
		}
		System.out.print(toString() + "\n");
	}

	public void jumpLeft(int x, int y) {
		if (canJumpLeft(x, y) == true) {
			if (board[x][y] == 'b') {
				board[x][y] = '-';
				board[x - 1][y - 1] = '-';
				board[x - 2][y - 2] = 'b';
				kingMe(x - 2, y - 2);
			} else if (board[x][y] == 'w') {
				board[x][y] = '-';
				board[x + 1][y + 1] = '-';
				board[x + 2][y + 2] = 'w';
				kingMe(x + 2, y + 2);
			}
		}
		System.out.print(toString() + "\n");
	}

	public void kingMe(int x, int y) {
		if ((x == 0 && board[x][y] == 'b') || (x == 7 && board[x][y] == 'w')) {
			board[x][y] = (board[x][y] + "").toUpperCase().toCharArray()[0];
		}
	}

	public String toString() {
		String str = "";
		for (char[] r : this.board) {
			str += "\n";
			for (char c : r) {
				str += c + " ";
			}
		}
		return str;
	}
}
