/*
 * Copyright 2017 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package board;

import java.awt.Point;
import java.util.ArrayList;

/**
 * board.Board - Created on 7-3-17
 *
 * Stores all information about the game board and
 * contains methods to perform calculations on it
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Board {

    private String myId;
    private String opponentId;
    private int width;
    private int height;

    private Piece[][] fields;
    private ArrayList<Piece> myPieces;

    public Board() {}

    /**
     * Parses the input string given by the engine
     * @param input String representation of the board
     */
    public void parseFromString(String input) {
        this.fields = new Piece[this.width][this.height];
        this.myPieces = new ArrayList<>();
        int x = 0;
        int y = 0;

        for (String fieldString : input.split(",")) {
            Piece piece = null;

            if (!fieldString.equals(".")) {
                piece = new Piece(new Point(x, y), fieldString);
            }
            if (fieldString.equals(this.myId)) {
                this.myPieces.add(piece);
            }

            this.fields[x][y] = piece;

            if (++x == this.width) {
                x = 0;
                y++;
            }
        }
    }

    public ArrayList<Point> getValidPointsForPiece(Piece piece) {
        ArrayList<Point> validPointsForPiece = new ArrayList<>();

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dy == 0 && dx == 0) continue;

                Point direction = new Point(dx, dy);
                Point base = piece.getCoordinate();
                int count = getPieceCountOnLine(base, direction);

                Point validPoint = getValidPointInDirection(
                        base, direction, count, piece.getPlayerId());

                if (validPoint != null) {
                    validPointsForPiece.add(validPoint);
                }
            }
        }

        return validPointsForPiece;
    }

    private int getPieceCountOnLine(Point base, Point direction) {
        if (direction.x == 0 && direction.y == 0) {
            throw new RuntimeException("Direction can't be 0,0");
        }

        Point reversedDirection = new Point(direction.x * -1, direction.y * -1);

        // -1 because base is counted twice
        return  getPieceCountInDirection(base, direction) +
                getPieceCountInDirection(base, reversedDirection) - 1;
    }

    private int getPieceCountInDirection(Point base, Point direction) {
        int x = base.x;
        int y = base.y;
        int count = 0;

        while (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            if (this.fields[x][y] != null) {
                count++;
            }

            x += direction.x;
            y += direction.y;
        }

        return count;
    }

    private Point getValidPointInDirection(Point base, Point direction, int count, String playerId) {
        int x = base.x;
        int y = base.y;

        for (int n = 0; n < count; n++) {
            x += direction.x;
            y += direction.y;

            // Outside of board
            if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
                return null;
            }

            Piece piece = this.fields[x][y];

            // Blocked my opponent piece
            if (n < count - 1 && piece != null && !piece.getPlayerId().equals(playerId)) {
                return null;
            }

            // Final position contains own piece
            if (n == count - 1 && piece != null && piece.getPlayerId().equals(playerId)) {
                return null;
            }
        }

        return new Point(x, y);
    }

    public void setMyId(int id) {
        this.myId = id + "";
    }

    public void setOpponentId(int id) {
        this.opponentId = id + "";
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ArrayList<Piece> getMyPieces() {
        return this.myPieces;
    }

    public String getMyId() {
        return this.myId;
    }

    public String getOpponentId() {
        return this.opponentId;
    }
}
