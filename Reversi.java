import java.util.*;

class State {
    char[] board;
    // keep at neg 1
    int lastCheckedIndex = -1;
    boolean foundPlace = false;
    //char comparePlayer = 'z';
    State optimalMoveAlpha = null;
    State optimalMoveBeta = null;
    State parent = null;


    public State(char[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
    }

    public int getScore() {
        int ones = 0;
        int twos = 0;
        for (int i = 0; i < 16; i++) {
            if (board[i] == '1') {
                ones++;
            } else if (board[i] == '2') {
                twos++;
            }
        }

        if (ones > twos) return 1;
        if (twos > ones) return -1;
        return 0;
    }

    public boolean isTerminal() {

        // TO DO: determine if the board is a terminal node or not and return boolean
        // maybe call get successors for both players and check that both are the original board that was input
        if (getSuccessors('1')[0].equals(this)) {
            if (getSuccessors('2')[0].equals(this)) {
                return true;
            }
        }
        return false;
    }

    public char[] recursiveMultiCheck(char[] successorBoard, char player) {
        int i = lastCheckedIndex;
        char[] newSuccessorBoard = Arrays.copyOf(successorBoard, board.length);
        if (lastCheckedIndex != -1) {

            // nw
            if (i - 5 > 0 && (((i-5)/4) == (i/4)-1)&& successorBoard[i-5] != '0' && successorBoard[i-5] != player) {
                newSuccessorBoard = recursiveMoveNW(i-5, newSuccessorBoard, player);
            }
            // up
            if (i - 4 > 0 && successorBoard[i-4] != '0' && successorBoard[i-4] != player) {
                newSuccessorBoard = recursiveMoveUp(i-4, newSuccessorBoard, player);
            }
            // ne
            if (i - 3 > 0 && (((i-3)/4) == (i/4)-1)&& successorBoard[i-3] != '0' && successorBoard[i-3] != player) {
                newSuccessorBoard = recursiveMoveNE(i-3, newSuccessorBoard, player);
            }
            //left
            if (i - 1 > 0 && (((i-1)/4) == (i/4))&& successorBoard[i-1] != '0' && successorBoard[i-1] != player) {
                newSuccessorBoard = recursiveMoveLeft(i-1, newSuccessorBoard, player);
            }
            // right
            if (i + 1 < 15 && (((i+1)/4) == (i/4))&& successorBoard[i+1] != '0' && successorBoard[i+1] != player) {
                newSuccessorBoard = recursiveMoveRight(i+1, newSuccessorBoard, player);
            }
            // sw
            if (i + 3 < 15 && (((i + 3)/4) == (i/4)+1)&& successorBoard[i+3] != '0' && successorBoard[i+3] != player) {
                newSuccessorBoard = recursiveMoveSW(i+3, newSuccessorBoard, player);
            }
            // down
            if (i + 4 < 15 && successorBoard[i+4] != '0' && successorBoard[i+4] != player) {
                newSuccessorBoard = recursiveMoveDown(i+4, newSuccessorBoard, player);
            }
            // se
            if (i + 5 < 15 && (((i + 5)/4) == (i/4)+1)&& successorBoard[i+5] != '0' && successorBoard[i+5] != player) {
                newSuccessorBoard = recursiveMoveSE(i+5, newSuccessorBoard, player);
            }

        }
        return newSuccessorBoard;
    }

    public State[] getSuccessors(char player) {
        // save original board so that it can be returned if there are no valid moves?
        char[] origBoard = board;
        // starting arraylist for successors, because we might not always have the same number of successors
        ArrayList<State> succ = new ArrayList<State>();

        // loop through each index to find "not player ((player + 1) mod 2)"
        for (int i = 0; i < 16; i++) {
            char[] successorBoard = Arrays.copyOf(board, board.length);
            // once at an index where you see player, look adjacent to find the other player
            if (board[i] == player) {
                lastCheckedIndex = -1;
                // there are at most 8 tiles around a piece

                // nw
                if (i - 5 > 0 && (((i - 5) / 4) == (i / 4) - 1) && successorBoard[i - 5] != '0' && successorBoard[i - 5] != player) {
                    foundPlace = false;
                    successorBoard = recursiveMoveNW(i - 5, successorBoard, player);
                    State newState = new State(successorBoard);
                    if (!newState.equals(this)) {
                        char[] newSuccessorBoard = recursiveMultiCheck(successorBoard, player);
                        State newerState = new State(newSuccessorBoard);
                        boolean found = false;
                        for (int j = 0; j < succ.size(); j++) {
                            if (succ.get(j).equals(newerState)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            succ.add(newerState);
                        }
                        lastCheckedIndex = -1;
                    } else {
                        lastCheckedIndex = -1;
                    }
                    successorBoard = Arrays.copyOf(board, board.length);
                }
            }
        }

        for (int i = 0; i < 16; i++) {
            char[] successorBoard = Arrays.copyOf(board, board.length);
            // once at an index where you see player, look adjacent to find the other player
            if (board[i] == player) {
                lastCheckedIndex = -1;
                // there are at most 8 tiles around a piece

                // up
                // if the one above is in bounds, not 0, and is the other player
                if (i - 4 > 0 && successorBoard[i-4] != '0' && successorBoard[i-4] != player) {
                    foundPlace = false;
                    successorBoard = recursiveMoveUp(i - 4, successorBoard, player);
                    State newState = new State(successorBoard);
                    if (!newState.equals(this)) {
                        char[] newSuccessorBoard = recursiveMultiCheck(successorBoard, player);
                        State newerState = new State(newSuccessorBoard);
                        boolean found = false;
                        for (int j = 0; j < succ.size(); j++) {
                            if (succ.get(j).equals(newerState)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            succ.add(newerState);
                        }
                        lastCheckedIndex = -1;
                    } else {
                        lastCheckedIndex = -1;
                    }
                    successorBoard = Arrays.copyOf(board, board.length);
                }
            }
        }

        for (int i = 0; i < 16; i++) {
            char[] successorBoard = Arrays.copyOf(board, board.length);
            // once at an index where you see player, look adjacent to find the other player
            if (board[i] == player) {
                lastCheckedIndex = -1;
                // there are at most 8 tiles around a piece
                // ne
                if (i - 3 > 0 && (((i-3)/4) == (i/4)-1)&& successorBoard[i-3] != '0' && successorBoard[i-3] != player) {
                    foundPlace = false;
                    successorBoard = recursiveMoveNE(i - 3, successorBoard, player);
                    State newState = new State(successorBoard);
                    if (!newState.equals(this)) {
                        char[] newSuccessorBoard = recursiveMultiCheck(successorBoard, player);
                        State newerState = new State(newSuccessorBoard);
                        boolean found = false;
                        for (int j = 0; j < succ.size(); j++) {
                            if (succ.get(j).equals(newerState)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            succ.add(newerState);
                        }
                        lastCheckedIndex = -1;
                    } else {
                        lastCheckedIndex = -1;
                    }
                    successorBoard = Arrays.copyOf(board, board.length);
                }
            }
        }

        for (int i = 0; i < 16; i++) {
            char[] successorBoard = Arrays.copyOf(board, board.length);
            // once at an index where you see player, look adjacent to find the other player
            if (board[i] == player) {
                lastCheckedIndex = -1;
                // there are at most 8 tiles around a piece
                // left
                if (i - 1 > 0 && (((i-1)/4) == (i/4))&& successorBoard[i-1] != '0' && successorBoard[i-1] != player) {
                    foundPlace = false;
                    successorBoard = recursiveMoveLeft(i - 1, successorBoard, player);
                    State newState = new State(successorBoard);
                    if (!newState.equals(this)) {
                        char[] newSuccessorBoard = recursiveMultiCheck(successorBoard, player);
                        State newerState = new State(newSuccessorBoard);
                        boolean found = false;
                        for (int j = 0; j < succ.size(); j++) {
                            if (succ.get(j).equals(newerState)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            succ.add(newerState);
                        }
                        lastCheckedIndex = -1;
                    } else {
                        lastCheckedIndex = -1;
                    }
                    successorBoard = Arrays.copyOf(board, board.length);
                }

            }
        }

        for (int i = 0; i < 16; i++) {
            char[] successorBoard = Arrays.copyOf(board, board.length);
            // once at an index where you see player, look adjacent to find the other player
            if (board[i] == player) {
                lastCheckedIndex = -1;
                // there are at most 8 tiles around a piece
                // right
                if (i + 1 < 15 && (((i+1)/4) == (i/4))&& successorBoard[i+1] != '0' && successorBoard[i+1] != player) {
                    foundPlace = false;
                    successorBoard = recursiveMoveRight(i + 1, successorBoard, player);
                    State newState = new State(successorBoard);
                    if (!newState.equals(this)) {
                        char[] newSuccessorBoard = recursiveMultiCheck(successorBoard, player);
                        State newerState = new State(newSuccessorBoard);
                        boolean found = false;
                        for (int j = 0; j < succ.size(); j++) {
                            if (succ.get(j).equals(newerState)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            succ.add(newerState);
                        }
                        lastCheckedIndex = -1;
                    } else {
                        lastCheckedIndex = -1;
                    }
                    successorBoard = Arrays.copyOf(board, board.length);
                }

            }
        }

        for (int i = 0; i < 16; i++) {
            char[] successorBoard = Arrays.copyOf(board, board.length);
            // once at an index where you see player, look adjacent to find the other player
            if (board[i] == player) {
                lastCheckedIndex = -1;
                // there are at most 8 tiles around a piece
                // sw
                if (i + 3 < 15 && (((i + 3)/4) == (i/4)+1)&& successorBoard[i+3] != '0' && successorBoard[i+3] != player) {
                    foundPlace = false;
                    successorBoard = recursiveMoveSW(i + 3, successorBoard, player);
                    State newState = new State(successorBoard);
                    if (!newState.equals(this)) {
                        char[] newSuccessorBoard = recursiveMultiCheck(successorBoard, player);
                        State newerState = new State(newSuccessorBoard);
                        boolean found = false;
                        for (int j = 0; j < succ.size(); j++) {
                            if (succ.get(j).equals(newerState)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            succ.add(newerState);
                        }
                        lastCheckedIndex = -1;
                    } else {
                        lastCheckedIndex = -1;
                    }
                    successorBoard = Arrays.copyOf(board, board.length);
                }
            }
        }

        for (int i = 0; i < 16; i++) {
            char[] successorBoard = Arrays.copyOf(board, board.length);
            // once at an index where you see player, look adjacent to find the other player
            if (board[i] == player) {
                lastCheckedIndex = -1;
                // there are at most 8 tiles around a piece
                // down
                if (i + 4 < 15 && successorBoard[i+4] != '0' && successorBoard[i+4] != player) {
                    foundPlace = false;
                    successorBoard = recursiveMoveDown(i + 4, successorBoard, player);
                    State newState = new State(successorBoard);
                    if (!newState.equals(this)) {
                        char[] newSuccessorBoard = recursiveMultiCheck(successorBoard, player);
                        State newerState = new State(newSuccessorBoard);
                        boolean found = false;
                        for (int j = 0; j < succ.size(); j++) {
                            if (succ.get(j).equals(newerState)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            succ.add(newerState);
                        }
                        lastCheckedIndex = -1;
                    } else {
                        lastCheckedIndex = -1;
                    }
                    successorBoard = Arrays.copyOf(board, board.length);
                }
            }
        }

        for (int i = 0; i < 16; i++) {
            char[] successorBoard = Arrays.copyOf(board, board.length);
            // once at an index where you see player, look adjacent to find the other player
            if (board[i] == player) {
                lastCheckedIndex = -1;
                // there are at most 8 tiles around a piece
                // se
                if (i + 5 < 15 && (((i + 5)/4) == (i/4)+1)&& successorBoard[i+5] != '0' && successorBoard[i+5] != player) {
                    foundPlace = false;
                    successorBoard = recursiveMoveSE(i + 5, successorBoard, player);
                    State newState = new State(successorBoard);
                    if (!newState.equals(this)) {
                        char[] newSuccessorBoard = recursiveMultiCheck(successorBoard, player);
                        State newerState = new State(newSuccessorBoard);
                        boolean found = false;
                        for (int j = 0; j < succ.size(); j++) {
                            if (succ.get(j).equals(newerState)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            succ.add(newerState);
                        }
                        lastCheckedIndex = -1;
                    } else {
                        lastCheckedIndex = -1;
                    }
                    successorBoard = Arrays.copyOf(board, board.length);
                }
            }
        }



        // there were no available moves
        // TODO: get rid of this and change in printState instead
        if (succ.size() == 0) {
            // if terminal, print nothing
            /*if (isTerminal()) {
                return;
            }*/
            // add this state as the only successor because it is a pass
            succ.add(this);
            State[] successors ={succ.get(0)};
            return successors;
        }

        /*// sorts successors and returns
        comparePlayer = player;
        //Collections.sort(succ, Collections.reverseOrder());
        comparePlayer = 'z';*/
        State[] successors = new State[succ.size()];
        for (int i = 0; i < succ.size(); i++) {
            successors[i] = succ.get(i);
        }
        return successors;
    }

    // TODO:
    public char[] recursiveMoveUp(int index, char[] successorBoard, char player) {
        if (index < 0 || index > 15) {
            return successorBoard;
        }

        if(successorBoard[index] != player && successorBoard[index] != '0') {
            successorBoard = recursiveMoveUp(index-4, successorBoard, player);
            if (((index - 4) >= 0) && successorBoard[index - 4] == player) {
                if (foundPlace) {
                    successorBoard[index] = player;
                }
            }
        }
        // we can play here
        //TODO: need to check that all other directions can be hit as well!! Call the other recursive methods?
        if (successorBoard[index] == '0' && (lastCheckedIndex == -1)|| (successorBoard[index] == player && lastCheckedIndex != -1)) {
            foundPlace = true;
            if (lastCheckedIndex == -1) {
                lastCheckedIndex = index;
            }
            successorBoard[index] = player;
            return successorBoard;
        }
        return successorBoard;
    }

    public char[] recursiveMoveDown(int index, char[] successorBoard, char player) {
        if (index < 0 || index > 15) {
            return successorBoard;
        }

        if(successorBoard[index] != player && successorBoard[index] != '0') {
            successorBoard = recursiveMoveDown(index+4, successorBoard, player);
            if (((index + 4) <= 15) && successorBoard[index + 4] == player) {
                if (foundPlace) {
                    successorBoard[index] = player;
                }
            }
        }
        // we can play here
        //TODO: need to check that all other directions can be hit as well!! Call the other recursive methods?
        if (successorBoard[index] == '0' && (lastCheckedIndex == -1)|| (successorBoard[index] == player && lastCheckedIndex != -1)) {
            foundPlace = true;
            if (lastCheckedIndex == -1) {
                lastCheckedIndex = index;
            }
            successorBoard[index] = player;
            return successorBoard;
        }
        return successorBoard;
    }

    public char[] recursiveMoveNW(int index, char[] successorBoard, char player) {
        if (index < 0 || index > 15) {
            return successorBoard;
        }

        if(successorBoard[index] != player && successorBoard[index] != '0') {
            if ((index/4)-1 == (index-5)/4) {
                successorBoard = recursiveMoveNW(index - 5, successorBoard, player);
                if (((index - 5) >= 0) && successorBoard[index - 5] == player) {
                    if (foundPlace) {
                        successorBoard[index] = player;
                    }
                }
            }
        }
        // we can play here
        //TODO: need to check that all other directions can be hit as well!! Call the other recursive methods?
        if (successorBoard[index] == '0' && (lastCheckedIndex == -1) || (successorBoard[index] == player && lastCheckedIndex != -1)) {
            foundPlace = true;
            if (lastCheckedIndex == -1) {
                lastCheckedIndex = index;
            }
            successorBoard[index] = player;
            return successorBoard;
        }
        return successorBoard;
    }

    public char[] recursiveMoveLeft(int index, char[] successorBoard, char player) {
        if (index < 0 || index > 15) {
            return successorBoard;
        }

        if(successorBoard[index] != player && successorBoard[index] != '0') {
            if (index/4 == (index-1)/4) {
                successorBoard = recursiveMoveLeft(index - 1, successorBoard, player);
                if (((index - 1) >= 0) && successorBoard[index - 1] == player) {
                    if (foundPlace) {
                        successorBoard[index] = player;
                    }
                }
            }
        }
        // we can play here
        //TODO: need to check that all other directions can be hit as well!! Call the other recursive methods?
        if (successorBoard[index] == '0' && (lastCheckedIndex == -1) || (successorBoard[index] == player && lastCheckedIndex != -1)) {
            foundPlace = true;
            if (lastCheckedIndex == -1) {
                lastCheckedIndex = index;
            }
            successorBoard[index] = player;
            return successorBoard;
        }
        return successorBoard;
    }

    public char[] recursiveMoveRight(int index, char[] successorBoard, char player) {
        if (index < 0 || index > 15) {
            return successorBoard;
        }

        if(successorBoard[index] != player && successorBoard[index] != '0') {
            if (index/4 == (index+1)/4) {
                successorBoard = recursiveMoveRight(index + 1, successorBoard, player);
                if (((index + 1) <= 15) && successorBoard[index + 1] == player) {
                    if (foundPlace) {
                        successorBoard[index] = player;
                    }
                }
            }
        }
        // we can play here
        //TODO: need to check that all other directions can be hit as well!! Call the other recursive methods?
        if (successorBoard[index] == '0' && (lastCheckedIndex == -1) || (successorBoard[index] == player && lastCheckedIndex != -1)) {
            foundPlace = true;
            if (lastCheckedIndex == -1) {
                lastCheckedIndex = index;
            }
            successorBoard[index] = player;
            return successorBoard;
        }
        return successorBoard;
    }

    public char[] recursiveMoveNE(int index, char[] successorBoard, char player) {
        if (index < 0 || index > 15) {
            return successorBoard;
        }

        if(successorBoard[index] != player && successorBoard[index] != '0') {
            if ((index/4)-1 == (index-3)/4) {
                successorBoard = recursiveMoveNE(index - 3, successorBoard, player);
                if (((index - 3) >= 0) && successorBoard[index - 3] == player) {
                    if (foundPlace) {
                        successorBoard[index] = player;
                    }
                }
            }
        }
        // we can play here
        //TODO: need to check that all other directions can be hit as well!! Call the other recursive methods?
        if (successorBoard[index] == '0' && (lastCheckedIndex == -1) || (successorBoard[index] == player && lastCheckedIndex != -1)) {
            foundPlace = true;
            if (lastCheckedIndex == -1) {
                lastCheckedIndex = index;
            }
            successorBoard[index] = player;
            return successorBoard;
        }
        return successorBoard;
    }

    public char[] recursiveMoveSW(int index, char[] successorBoard, char player) {
        if (index < 0 || index > 15) {
            return successorBoard;
        }

        if(successorBoard[index] != player && successorBoard[index] != '0') {
            if ((index/4)+1 == (index+3)/4) {
                successorBoard = recursiveMoveSW(index + 3, successorBoard, player);
                if (((index + 3) <= 15) && successorBoard[index + 3] == player) {
                    if (foundPlace) {
                        successorBoard[index] = player;
                    }
                }
            }
        }
        // we can play here
        //TODO: need to check that all other directions can be hit as well!! Call the other recursive methods?
        if (successorBoard[index] == '0' && (lastCheckedIndex == -1) || (successorBoard[index] == player && lastCheckedIndex != -1)) {
            foundPlace = true;
            if (lastCheckedIndex == -1) {
                lastCheckedIndex = index;
            }
            successorBoard[index] = player;
            return successorBoard;
        }
        return successorBoard;
    }

    public char[] recursiveMoveSE(int index, char[] successorBoard, char player) {
        if (index < 0 || index > 15) {
            return successorBoard;
        }

        if(successorBoard[index] != player && successorBoard[index] != '0') {
            if ((index/4)+1 == (index+5)/4) {
                successorBoard = recursiveMoveSE(index + 5, successorBoard, player);
                if (((index + 5) <= 15) && successorBoard[index + 5] == player) {
                    if (foundPlace) {
                        successorBoard[index] = player;
                    }
                }
            }
        }
        // we can play here
        //TODO: need to check that all other directions can be hit as well!! Call the other recursive methods?
        if (successorBoard[index] == '0' && (lastCheckedIndex == -1)|| (successorBoard[index] == player && lastCheckedIndex != -1)) {
            foundPlace = true;
            if (lastCheckedIndex == -1) {
                lastCheckedIndex = index;
            }
            successorBoard[index] = player;
            return successorBoard;
        }
        return successorBoard;
    }

    public void printState(int option, char player) {

        // TO DO: print a State based on option (flag)
        switch (option) {
            case 1:
                State[] successors = getSuccessors(player);
                // if there was only one successor and it was a pass (same board)
                if(successors[0].equals(this)) {
                    if (!(successors[0]).isTerminal()) {
                        System.out.println(this.getBoard());
                        return;
                    } else {
                        return;
                    }
                }
                for (int i = 0; i < successors.length; i++) {
                    System.out.println(successors[i].getBoard());
                }
                break;
            case 2:
                if (isTerminal()) {
                    System.out.println(getScore());
                    return;
                } else {
                    System.out.println("non-terminal");
                }
                break;
            case 3:
                Minimax.option = 3;
                int gameVal = Minimax.run(this, player);
                System.out.println(gameVal);
                System.out.println(Minimax.statesExplored);
                break;
            case 4:
                Minimax.option = 4;
                if (!this.isTerminal()) {
                    if (getSuccessors(player).length == 1) {
                        System.out.println(getSuccessors(player)[0].getBoard());
                        return;
                    }
                    if (player == '1') {
                        Minimax.alphaGuard = false;
                        Minimax.betaGuard = false;
                        Minimax.optimalMoveAlpha = null;
                        Minimax.optimalMoveBeta = null;
                        Minimax.orig_board = this;
                        Minimax.run(this, '1');
                        System.out.println(Minimax.optimalMoveAlpha.getBoard());
                        Minimax.orig_board = null;
                    } else {
                        Minimax.alphaGuard = false;
                        Minimax.betaGuard = false;
                        Minimax.optimalMoveAlpha = null;
                        Minimax.optimalMoveBeta = null;
                        Minimax.orig_board = this;
                        Minimax.run(this, '2');
                        System.out.println(Minimax.optimalMoveBeta.getBoard());
                        Minimax.orig_board = null;
                    }
                }
                break;
            case 5:
                Minimax.option = 5;
                int gameVal2 = Minimax.run_with_pruning(this, player);
                System.out.println(gameVal2);
                System.out.println(Minimax.statesExplored);
                break;
            case 6:
                Minimax.option = 6;
                if (!this.isTerminal()) {
                    if (getSuccessors(player).length == 1) {
                        System.out.println(getSuccessors(player)[0].getBoard());
                        return;
                    }
                    if (player == '1') {
                        Minimax.alphaGuard = false;
                        Minimax.betaGuard = false;
                        Minimax.optimalMoveAlpha = null;
                        Minimax.optimalMoveBeta = null;
                        Minimax.orig_board = this;
                        Minimax.run_with_pruning(this, '1');
                        System.out.println(Minimax.optimalMoveAlpha.getBoard());
                        Minimax.orig_board = null;
                    } else {
                        Minimax.alphaGuard = false;
                        Minimax.betaGuard = false;
                        Minimax.optimalMoveAlpha = null;
                        Minimax.optimalMoveBeta = null;
                        Minimax.orig_board = this;
                        Minimax.run_with_pruning(this, '2');
                        System.out.println(Minimax.optimalMoveBeta.getBoard());
                        Minimax.orig_board = null;
                    }
                }
                break;
        }

    }

    public String getBoard() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            builder.append(this.board[i]);
        }
        return builder.toString().trim();
    }

    public boolean equals(State src) {
        for (int i = 0; i < 16; i++) {
            if (this.board[i] != src.board[i])
                return false;
        }
        return true;
    }
}

class Minimax {
    static int statesExplored = 0;
    static State optimalMoveAlpha;
    static State optimalMoveBeta;
    public static boolean alphaGuard = false;
    public static boolean betaGuard = false;
    public static State orig_board = null;
    public static int option = 0;

    private static int max_value(State curr_state) {
        if (option == 3) {
            statesExplored++;
            if (curr_state.isTerminal()) {
                return curr_state.getScore();
            } else {
                double alpha = Double.NEGATIVE_INFINITY;
                for (State state : curr_state.getSuccessors('1')) {
                    alpha = Math.max(alpha, min_value(state));
                }
                return (int) alpha;
            }
        } else {
            if (curr_state.isTerminal()) {
                return curr_state.getScore();
            } else {
                double alpha = Double.NEGATIVE_INFINITY;
                for (State state : curr_state.getSuccessors('1')) {
                    if (!state.equals(curr_state)) {
                    state.parent = curr_state;
                    double prevAlpha = alpha;
                    alpha = Math.max(alpha, min_value(state));
                    if (orig_board != null && alpha > prevAlpha && state.parent.equals(orig_board) && alphaGuard == false) {
                        alphaGuard = true;
                        optimalMoveAlpha = state;
                    }
                    }
                }
                return (int) alpha;
            }
        }
    }

    private static int min_value(State curr_state) {
        if (option == 3) {
            statesExplored++;
            if (curr_state.isTerminal()) {
                return curr_state.getScore();
            } else {
                double beta = Double.POSITIVE_INFINITY;
                for (State state : curr_state.getSuccessors('2')) {
                    beta = Math.min(beta, max_value(state));
                }
                return (int) beta;
            }
        } else {
            if (curr_state.isTerminal()) {
                return curr_state.getScore();
            } else {
                double beta = Double.POSITIVE_INFINITY;
                for (State state : curr_state.getSuccessors('2')) {
                    if (!state.equals(curr_state)) {
                        state.parent = curr_state;
                        double prevBeta = beta;
                        beta = Math.min(beta, max_value(state));
                        if (orig_board != null && beta < prevBeta && state.parent.equals(orig_board) && betaGuard == false) {
                            betaGuard = true;
                            optimalMoveBeta = state;
                        }
                    }
                }
                return (int)beta;
            }
        }
    }

    private static int max_value_with_pruning(State curr_state, int alpha, int beta) {
        statesExplored++;
        if (option == 5) {
            if (curr_state.isTerminal()) {
                return curr_state.getScore();
            } else {

                    for (State state : curr_state.getSuccessors('1')) {
                        alpha = Math.max(alpha, min_value_with_pruning(state, alpha, beta));
                        if (alpha >= beta) {
                            return beta;
                        }
                    }

                return alpha;
            }
        } else {
            if (curr_state.isTerminal()) {
                return curr_state.getScore();
            } else {
                for (State state : curr_state.getSuccessors('1')) {
                    //if (!state.equals(curr_state)) {
                    if (state.parent == null) {
                        state.parent = curr_state;
                    }   double prevAlpha = alpha;
                        alpha = Math.max(alpha, min_value_with_pruning(state, alpha, beta));
                        if (alpha >= beta) {
                            return beta;
                        }
                        if (orig_board != null && alpha > prevAlpha && state.parent.equals(orig_board) && alphaGuard == false) {
                            alphaGuard = true;
                            optimalMoveAlpha = state;
                        }
                    //}
                }
                return alpha;
            }
        }
    }

    private static int min_value_with_pruning(State curr_state, int alpha, int beta) {
        statesExplored++;
        if (option == 5) {
            if (curr_state.isTerminal()) {
                return curr_state.getScore();
            } else {

                    for (State state : curr_state.getSuccessors('2')) {
                        beta = Math.min(beta, max_value_with_pruning(state, alpha, beta));
                        if (alpha >= beta) {
                            return alpha;
                        }
                    }

                return beta;
            }
        } else {
            if (curr_state.isTerminal()) {
                return curr_state.getScore();
            } else {
                for (State state : curr_state.getSuccessors('2')) {
                    //if (!state.equals(curr_state)) {
                    if (state.parent == null) {
                        state.parent = curr_state;
                    }
                        double prevBeta = beta;
                        beta = Math.min(beta, max_value_with_pruning(state, alpha, beta));
                        if (alpha >= beta) {
                            return alpha;
                        }
                        if (orig_board != null && beta < prevBeta && state.parent.equals(orig_board) && betaGuard == false) {
                            betaGuard = true;
                            optimalMoveBeta = state;
                        }
                    //}
                }
                return beta;
            }
        }
    }

    public static int run(State curr_state, char player) {
        // TO DO: run the Minimax algorithm and return the game theoretic value
        if (option == 4) {
            orig_board = curr_state;
        }
        if (player == '1') {
            return max_value(curr_state);
        } else {
            return  min_value(curr_state);
        }
    }

    public static int run_with_pruning(State curr_state, char player) {

        // TO DO: run the alpha-beta pruning algorithm and return the game theoretic value
        if (option == 6) {
            orig_board = curr_state;
        } else {
            orig_board = null;
        }
        if (player == '1') {
            return max_value_with_pruning(curr_state, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            return  min_value_with_pruning(curr_state, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }
}

public class Reversi {
    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }
        int flag = Integer.valueOf(args[0]);
        char[] board = new char[16];
        for (int i = 0; i < 16; i++) {
            board[i] = args[2].charAt(i);
        }
        int option = flag / 100;
        char player = args[1].charAt(0);
        if ((player != '1' && player != '2') || args[1].length() != 1) {
            System.out.println("Invalid Player Input");
            return;
        }
        State init = new State(board);
        init.printState(option, player);
    }
}

