/**
 * @author valeriali
 * @project MEPHI
 */
public class King extends ChessPiece {

    public King(String color) {
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

        // Король не может оставаться на той же клетке
        if (line == toLine && column == toColumn) {
            return false;
        }

        // Король может ходить на одну клетку в любом направлении
        int dx = Math.abs(line - toLine);
        int dy = Math.abs(column - toColumn);

        if (dx <= 1 && dy <= 1) {
            ChessPiece targetPiece = chessBoard.board[toLine][toColumn];
            return targetPiece == null || !targetPiece.getColor().equals(getColor());
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return "K";
    }

    public boolean isUnderAttack(ChessBoard chessBoard, int line, int column) {
        // Проходим по всем клеткам доски и проверяем, атакуют ли они заданную клетку (line, column)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = chessBoard.board[i][j];
                // Если на клетке стоит фигура противоположного цвета и она может атаковать позицию короля
                if (piece != null && !piece.getColor().equals(this.color) &&
                        piece.canMoveToPosition(chessBoard, i, j, line, column)) {
                    return true;
                }
            }
        }
        return false;
    }




}
