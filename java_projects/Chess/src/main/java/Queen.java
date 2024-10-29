/**
 * @author valeriali
 * @project MEPHI
 */
public class Queen extends ChessPiece {

    public Queen(String color) {
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

        // Ферзь не может оставаться на той же клетке
        if (line == toLine && column == toColumn) {
            return false;
        }

        // Логика движения: либо по диагонали, либо по прямой
        int dx = Math.abs(line - toLine);
        int dy = Math.abs(column - toColumn);

        // Проверка на движение по диагонали
        if (dx == dy) {
            int xStep = (toLine - line) > 0 ? 1 : -1; // Определяем направлением по вертикали
            int yStep = (toColumn - column) > 0 ? 1 : -1; // Определяем направлением по вертикали
            int x = line + xStep;
            int y = column + yStep;

            while (x != toLine && y != toColumn) {
                if (chessBoard.board[x][y] != null) return false; // На пути есть другая фигура
                x += xStep;
                y += yStep;
            }
        }

        // Проверка на движение по прямой линии (горизонтально или вертикально)
        else if (line == toLine || column == toColumn) {
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
                    if (chessBoard.board[i][column] != null) return false; // На пути есть фигура
                }
            }
        } else {
            // Если ферзь не движется ни по прямой, ни по диагонали
            return false;
        }

        // Проверка на "съедание" фигуры противника
        ChessPiece targetPiece = chessBoard.board[toLine][toColumn];
        return targetPiece == null || !targetPiece.getColor().equals(this.color);
    }

    @Override
    public String getSymbol() {
        return "Q";
    }




}
