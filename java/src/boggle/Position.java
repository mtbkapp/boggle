package boggle;

import java.util.Collection;
import java.util.ArrayList;
import boggle.Board;

public class Position {
  public final int x;
  public final int y;
  public final Board board; 

  public Position(int x, int y, Board board) {
    this.x = x;
    this.y = y;
    this.board = board;

    validate();
  }

  private void validate() {
    int size = board.getSize();

    if (x < 0 || y < 0 || y >= size || y >= size) {
      throw new IllegalStateException("Coordinates " + toString() + " are not in a board of size " + size);
    }
  }

  public Collection<Position> adjacentPositions() {
    ArrayList<Position> positions = new ArrayList<>();
    int maxSize = board.getSize();
    int[] deltas = {-1, 0, 1};

    for(int dx : deltas) {
      for(int dy : deltas) {
        int nx = x + dx;
        int ny = y + dy;

        if (nx >= 0 && ny >= 0 && nx < maxSize && ny < maxSize) {
          positions.add(new Position(nx, ny, board));
        }
      }
    }

    return positions;
  }

  @Override
  public String toString() {
    return "[" + x + ", " + y + "]";
  }
}
