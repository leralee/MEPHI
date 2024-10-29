/**
 * @author valeriali
 * @project MEPHI
 */
public class Pawn extends ChessPiece {

    public Pawn(String color) {
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

        // Пешка не может оставаться на той же клетке
        if (line == toLine && column == toColumn) {
            return false;
        }

        // Определяем направление движения в зависимости от цвета
        int direction = this.color.equals("White") ? 1 : -1;

        // Проверяем обычный ход на 1 клетку вперед
        if (toLine == line + direction && column == toColumn) {
            // Пешка может идти вперед только если клетка перед ней пуста
            return chessBoard.board[toLine][toColumn] == null;
        }

        // Проверяем первый ход на 2 клетки вперед
        int startLine = this.color.equals("White") ? 1 : 6;
        if (line == startLine && toLine == line + 2 * direction && column == toColumn) {
            // Пешка может идти на 2 клетки вперед, если обе клетки пусты
            return chessBoard.board[line + direction][column] == null && chessBoard.board[toLine][toColumn] == null;
        }

        // Проверяем "съедание" фигуры противника по диагонали
        if (toLine == line + direction && Math.abs(column - toColumn) == 1) {
            ChessPiece targetPiece = chessBoard.board[toLine][toColumn];
            return targetPiece != null && !targetPiece.getColor().equals(this.color);
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return "P";
    }




}
