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

package bot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import board.Piece;
import move.Move;

/**
 * bot.BotStarter - Created on 7-3-17
 *
 * Magic happens here. You should edit this file, or more specifically
 * the doMove() method to make your bot do more than random moves.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class BotStarter {

    private Random random;

    private BotStarter() {
        this.random = new Random();
    }

    /**
     * Gets a random piece and moves it to a random valid position.
     * Implement this to make the bot smarter.
     * @param state The current state of the game
     * @return A Move object or null
     */
    public Move doMove(BotState state) {
        ArrayList<Piece> myPieces = new ArrayList<>(state.getBoard().getMyPieces());
        Collections.shuffle(myPieces, this.random);
        Move move = null;

        while (move == null && myPieces.size() > 0) {
            Piece piece = myPieces.remove(0);  // Random piece
            ArrayList<Point> validPoints = state.getBoard().getValidPointsForPiece(piece);

            if (validPoints.size() > 0) { // Random move
                int randomIndex = this.random.nextInt(validPoints.size());
                Point randomTo = validPoints.get(randomIndex);

                move = new Move(piece.getCoordinate(), randomTo);
            }
        }

        return move;
    }

    public static void main(String[] args) {
        BotParser parser = new BotParser(new BotStarter());
        parser.run();
    }
}
