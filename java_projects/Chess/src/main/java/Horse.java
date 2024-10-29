/**
 * @author valeriali
 * @project MEPHI
 */
public class Horse extends ChessPiece {

    public Horse(String color) {
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
        // Конь не может ходить на то же место
        if (line == toLine && column == toColumn) {
            return false;
        }
        // Проверяем, может ли конь двигаться буквой "Г"
        int dx = Math.abs(line - toLine);
        int dy = Math.abs(column - toColumn);

        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            // Проверка на "съедание" фигуры противника
            ChessPiece targetPiece = chessBoard.board[toLine][toColumn];
            return targetPiece == null || !targetPiece.getColor().equals(this.color); // Либо пустая клетка, либо фигура противника
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return "H";
    }




}
