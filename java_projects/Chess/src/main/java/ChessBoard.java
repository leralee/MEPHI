/**
 * @author valeriali
 * @project MEPHI
 */
public class ChessBoard {
    public ChessPiece[][] board = new ChessPiece[8][8];
    String nowPlayer;

    public ChessBoard(String nowPlayer) {
        this.nowPlayer = nowPlayer;
    }

    public String nowPlayerColor() {
        return this.nowPlayer;
    }

    public boolean moveToPosition(int startLine, int startColumn, int endLine, int endColumn) {
        // Проверка, что начальная и конечная позиции находятся на доске
        if (checkPos(startLine) || checkPos(startColumn) || checkPos(endLine) || checkPos(endColumn)) {
            return false;
        }
        // Проверка, что на начальной позиции есть фигура и она принадлежит текущему игроку
        ChessPiece piece = board[startLine][startColumn];
        if (piece == null || !nowPlayerColor().equals(piece.getColor())) {
            return false;
        }
        // Проверка, может ли фигура сделать этот ход
        if (!piece.canMoveToPosition(this, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Если фигура — король или ладья, устанавливаем check в false, чтобы запретить рокировку
        if (piece instanceof King || piece instanceof Rook) {
            piece.check = false;
        }

        // Выполнение перемещения фигуры на доске
        board[endLine][endColumn] = piece;
        board[startLine][startColumn] = null;

        // Переключение текущего игрока
        nowPlayer = nowPlayer.equals("White") ? "Black" : "White";

        return true;

    }

    // Метод для рокировки по 0-му столбцу
    public boolean castling0() {
        int row = nowPlayer.equals("White") ? 0 : 7;

        // Проверка, что король и ладья не двигались, и между ними свободные клетки
        if (board[row][4] instanceof King && board[row][0] instanceof Rook
                                            && board[row][4].check && board[row][0].check
                                            && board[row][1] == null && board[row][2] == null && board[row][3] == null) {
            // Проверка, что клетки, через которые проходят король, не находятся под атакой
            if (!((King) board[row][4]).isUnderAttack(this, row, 2)
                    && !((King) board[row][4]).isUnderAttack(this,row, 3)
                    && !((King) board[row][4]).isUnderAttack(this,row, 4)) {
                // Выполнение рокировки
                board[row][2] = board[row][4]; // Перемещение короля
                board[row][3] = board[row][0]; // Перемещаем ладью
                board[row][4] = null;
                board[row][0] = null;

                // Обновляем статус check
                board[row][2].check = false;
                board[row][3].check = false;

                return true;
            }
        }
        return false;
    }

    // Метод для рокировки по 7-му столбцу
    public boolean castling7() {
        int row = nowPlayer.equals("White") ? 0 : 7;

        // Проверка, что король и ладья не двигались, и между ними свободные клетки
        if (board[row][4] instanceof King && board[row][7] instanceof Rook
                && board[row][4].check && board[row][7].check
                && board[row][5] == null && board[row][6] == null) {
            // Проверка, что клетки, через которые проходят король, не находятся под атакой
            if (!((King) board[row][4]).isUnderAttack(this, row, 4)
                    && !((King) board[row][4]).isUnderAttack(this, row, 5)
                    && !((King) board[row][4]).isUnderAttack(this,row, 6)) {
                // Выполнение рокировки
                board[row][6] = board[row][4]; // Перемещение короля
                board[row][5] = board[row][7]; // Перемещаем ладью
                board[row][4] = null;
                board[row][7] = null;

                // Обновляем статус check
                board[row][6].check = false;
                board[row][5].check = false;

                return true;
            }
        }
        return false;
    }

    public void printBoard() {
        System.out.println("Turn " + nowPlayer);
        System.out.println();
        System.out.println("Player 2(Black)");
        System.out.println();
        System.out.println("\t0\t1\t2\t3\t4\t5\t6\t7");

        for (int i = 7; i > -1; i--) {
            System.out.print(i + "\t");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print(".." + "\t");
                } else {
                    System.out.print(board[i][j].getSymbol() + board[i][j].getColor().substring(0, 1).toLowerCase() + "\t");
                }
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("Player 1(White)");
    }

    public boolean checkPos(int pos) {
        return pos < 0 || pos > 7;
    }
}