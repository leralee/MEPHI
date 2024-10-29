/**
 * @author valeriali
 * @project MEPHI
 */
public class Rook extends ChessPiece {

    public Rook(String color) {
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

        // Ладья не может оставаться на той же клетке
        if (line == toLine && column == toColumn) {
            return false;
        }

        // Проверяем движение по прямой: либо ряд тот же, либо столбец тот же
        if (!(line == toLine || column == toColumn)) {
            return false;
        }

        // Проверка, что на пути нет других фигур
        if (line == toLine) { // Движение по горизонтали
            int start = Math.min(column, toColumn);
            int end = Math.max(column, toColumn);
            for (int i = start + 1; i < end; i++) {
                if (chessBoard.board[i][column] != null) return false;

            }
        } else {
            int start = Math.min(line, toLine);
            int end = Math.max(line, toLine);
            for (int i = start + 1; i < end; i++) {
                if (chessBoard.board[i][column] != null) return false;
            }
        }

        // Проверка на "съедание" фигуры противника
        ChessPiece targetPiece = chessBoard.board[toLine][toColumn];
        return targetPiece == null || !targetPiece.getColor().equals(this.color); // Либо пустая клетка, либо фигура противника

    }

    @Override
    public String getSymbol() {
        return "R";
    }




}
