package boggle;

import java.util.ArrayList;
import java.util.Collection;

class Board {
  private char[][] board;
  private final int size;

  public Board(int size) {
    board = new char[size][size];
    this.size = size;
  }

  private void setRow(int y, char... c) {
    if (c.length != size) {
      throw new IllegalStateException("# of characters passed must = size of board.");
    }

    for(int x = 0; x < size; x++) {
      board[y][x] = c[x];
    }
  }

  public char getChar(int x, int y) {
    return board[y][x];
  }

  public char getChar(Position pos) {
    return getChar(pos.x, pos.y); 
  }

  public int getSize() {
    return size;
  }

  public Collection<Position> allPositions() {
    ArrayList<Position> positions = new ArrayList<>(size * size);
    for(int x = 0; x < size; x++) {
      for(int y = 0; y < size; y++) {
        positions.add(new Position(x, y, this));
      }
    }

    return positions;
  }

  //TODO: refactor the double for loop to some HOF utility
  @Override
  public String toString() {
    StringBuffer buff = new StringBuffer();
    for(int y = 0; y < size; y++) {
      for(int x = 0; x < size; x++) {
        buff.append(getChar(x, y));
        buff.append(" ");
      } 
      buff.append("\n");
    }

    return buff.toString();
  }

  public static Board testBoard1() {
    Board b = new Board(4);
    b.setRow(0, 'z', 'm', 'l', 'y');
    b.setRow(1, 't', 'm', 'q', 'e');
    b.setRow(2, 'n', 'a', 'r', 't');
    b.setRow(3, 'p', 'u', 'n', 'n');

    return b;
  }

  public static Board testBoard2() {
    Board b = new Board(5);
    b.setRow(0, 'p', 'p', 'a', 'w', 'v');
    b.setRow(1, 'o', 'r', 'e', 'r', 'e');
    b.setRow(2, 'g', 'y', 'e', 's', 'd');
    b.setRow(3, 'a', 'e', 'p', 'y', 'c');
    b.setRow(4, 'n', 's', 't', 'r', 'a');

    return b;
  }
}
