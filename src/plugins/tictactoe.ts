import { GameFramework } from "../core/framework"
import { GamePlugin } from "../core/gameplugin"


// the basic game from rec07:

enum Player {
    X = 0,
    O = 1
}
type PlayerOrEmpty = Player | null


interface Game {
    readonly board: Board
    getNextPlayer(): Player
    getWinner(): PlayerOrEmpty
    play(x: number, y: number): Game
}

interface Board {
    updateCell(x: number, y: number, player: Player): Board
    getCell(x: number, y: number): PlayerOrEmpty
}


function initializeGame(): Game {
    return newGame(newEmptyBoard(), Player.X, [])
}
function newGame(board: Board, nextPlayer: Player, history: Game[]): Game {
    return {
        board: board,
        play: function (x: number, y: number): Game {
            if (board.getCell(x,y)!==null) return this
            if (this.getWinner()!==null) return this
            const newHistory = history.slice()
            newHistory.push(this)
            return newGame(
                board.updateCell(x, y, nextPlayer),
                1 - nextPlayer,
                newHistory)
        },
        getWinner: function (): PlayerOrEmpty {
            for (let row = 0; row < 3; row++)
                if (board.getCell(row, 0) !== null && board.getCell(row, 0) === board.getCell(row, 1) && board.getCell(row, 1) === board.getCell(row, 2)) return board.getCell(row, 0)
            for (let col = 0; col < 3; col++)
                if (board.getCell(0, col) !== null && board.getCell(0, col) === board.getCell(1, col) && board.getCell(0, col) === board.getCell(2, col)) return board.getCell(0, col)
            if (board.getCell(1, 1) !== null && board.getCell(0, 0) === board.getCell(1, 1) && board.getCell(1, 1) === board.getCell(2, 2)) return board.getCell(1, 1)
            if (board.getCell(1, 1) !== null && board.getCell(0, 2) === board.getCell(1, 1) && board.getCell(1, 1) === board.getCell(2, 0)) return board.getCell(1, 1)
            return null
        },
        getNextPlayer: function () {
            return nextPlayer
        }
    }
}

function newEmptyBoard(): Board {
    return newBoard(new Array(9).fill(null, 0, 9))
}




function newBoard(cells: PlayerOrEmpty[]): Board {
    return {
        updateCell: function (x: number, y: number, player: Player): Board {
            const newCells = cells.slice()
            newCells[y * 3 + x] = player
            return newBoard(newCells)
        },
        getCell: function (x, y) {
            return cells[y * 3 + x]
        }
    }
}



// TODO: Implement the plugin

const GAME_START_FOOTER = 'Let\'s play Tic Tac Toe!'

function init (): GamePlugin {
    let framework: GameFramework | null = null
    let game: Game
    return {
      getGameName () { return 'TicTacToe' },
  
      getGridWidth (): number { return 3 },
  
      getGridHeight (): number { return 3 },
      onRegister (f: GameFramework): void { framework = f },
      onNewGame (): void {
        if (framework === null) return
        framework.setFooterText(GAME_START_FOOTER);
        game = initializeGame();
      },
      onNewMove (): void { }, // Nothing to do here.
      isMoveValid (x: number, y: number): boolean {
        return game.getWinner() === null
      },
      isMoveOver (): boolean {
        return true
      },
      onMovePlayed (x: number, y: number): void {
        framework?.setSquare(x, y, Player[game.getNextPlayer()]);
        game = game.play(x, y);
      },
      isGameOver (): boolean {
        return game.getWinner() !== null
      },
      getGameOverMessage (): string {
        const possibleWinner: Player | null = game.getWinner();
        if (possibleWinner == null) return "The game ended in a tie.";
        return ("Player " + Player[possibleWinner] + " won!");
      },
      onGameClosed (): void { }, // Nothing to do here.
      currentPlayer (): string { return Player[game.getNextPlayer()] }
    }
  }

export { init }