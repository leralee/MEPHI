/**
 * @author valeriali
 * @project MEPHI
 */
public class Bishop extends ChessPiece {

    public Bishop(String color) {
        super(color);
    }

    @Override
    public String getColor() {
        return super.getColor();
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        // Проверяем, что координаты находятся на доске
        if (chessBoard.checkPos(line) || chessBoard.checkPos(column)
                || chessBoard.checkPos(toLine) || chessBoard.checkPos(toColumn)) {
            return false;
        }
        // Слон не может остаться на той же клетке
        if (line == toLine && column == toColumn) {
            return false;
        }
        // Проверка, что слон движется по диагонали
        int dx = Math.abs(line - toLine);
        int dy = Math.abs(column - toColumn);

        if (dx != dy) {
            return false; // Слон может ходить только по диагонали
        }

        // Проверка, что на пути нет других фигур
        int xStep = (toLine - line) > 0 ? 1 : -1; // Определяем направлением по вертикали
        int yStep = (toColumn - column) > 0 ? 1 : -1; // Определяем направлением по вертикали
        int x = line + xStep;
        int y = column + yStep;

        while (x != toLine && y != toColumn) {
            if (chessBoard.board[x][y] != null) return false; // На пути есть другая фигура
            x += xStep;
            y += yStep;
        }

        // Проверка на "съедание" фигуры противника
        ChessPiece targetPiece = chessBoard.board[toLine][toColumn];
        return targetPiece == null || !targetPiece.getColor().equals(this.color);
    }

    @Override
    public String getSymbol() {
        return "B";
    }

}
