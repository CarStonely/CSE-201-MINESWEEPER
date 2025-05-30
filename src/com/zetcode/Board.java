package com.zetcode;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {

//Difficulty Settings

public static final int BEGINNER = 0;
public static final int INTERMEDIATE = 1;
public static final int EXPERT = 2;



public static final int BEGINNER_ROWS = 8;
public static final int BEGINNER_COLS = 8;
public static final int BEGINNER_MINES = 10;


public static final int INTERMEDIATE_ROWS = 16;
public static final int INTERMEDIATE_COLS = 16;
public static final int INTERMEDIATE_MINES = 40;

public static final int EXPERT_ROWS = 16;
public static final int EXPERT_COLS = 30;
public static final int EXPERT_MINES = 99;
    
    private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private  int N_MINES = 40;
    private int N_ROWS = 16;
    private  int N_COLS = 16;

    private int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
    private int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;

    private final int TREASURE_CELL = 8;
    private int extraLives = 0;


    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private Image[] img; //IMAGES MUST BE 15X15
    private boolean isPaused = false;
    private Image pauseImage;


    private int allCells;
    private final JLabel statusbar;

    public Board(JLabel statusbar, int difficulty) {
        this.statusbar = statusbar;
    

        //Set Difficulty
        switch (difficulty) {
            case 0: // Beginner
                N_ROWS = BEGINNER_ROWS;
                N_COLS = BEGINNER_COLS;
                N_MINES = BEGINNER_MINES;
                break;
            case 1: // Intermediate
                N_ROWS = INTERMEDIATE_ROWS;
                N_COLS = INTERMEDIATE_COLS;
                N_MINES = INTERMEDIATE_MINES;
                break;
            case 2: // Expert
                N_ROWS = EXPERT_ROWS;
                N_COLS = EXPERT_COLS;
                N_MINES = EXPERT_MINES;
                break;
            default: // Default to intermediate
                N_ROWS = INTERMEDIATE_ROWS;
                N_COLS = INTERMEDIATE_COLS;
                N_MINES = INTERMEDIATE_MINES;
           
        }
        
        BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
        BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;
        
        initBoard();
    }

    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        img = new Image[NUM_IMAGES + 1];

        for (int i = 0; i < NUM_IMAGES; i++) {

            var path = "/resources/" + i + ".png"; // Add leading slash for classpath reference
        img[i] = new ImageIcon(getClass().getResource(path)).getImage();
            //LOAD MY IMAGES HERE
        }
        var treasurePath = "/resources/13.png"; //  13.png is the treasure image
        img[8] = new ImageIcon(getClass().getResource(treasurePath)).getImage();


        var pausePath = "/resources/14.png"; //14.png is the pause image. Press p to pause
        pauseImage = new ImageIcon(getClass().getResource(pausePath)).getImage();

        addMouseListener(new MinesAdapter());
        newGame();

        
     setFocusable(true);
    addKeyListener((KeyListener) new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) { // P key to pause/unpause
            isPaused = !isPaused;
            repaint();
        }
    }
});
        



    }

    private void newGame() {
        //ADD HIDDEN TREASURE HERE, MAKE CONSTANT VARIABLE

        int cell;

        var random = new Random();
        inGame = true;
        minesLeft = N_MINES;
        extraLives = 0;

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {

            field[i] = COVER_FOR_CELL;
        }

        statusbar.setText(Integer.toString(minesLeft));

        int i = 0;

        while (i < N_MINES) {

            int position = (int) (allCells * random.nextDouble());

            if ((position < allCells)
                    && (field[position] != COVERED_MINE_CELL)) {

                int current_col = position % N_COLS;
                field[position] = COVERED_MINE_CELL;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - N_COLS;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + N_COLS - 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - N_COLS;
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position + N_COLS;
                if (cell < allCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (N_COLS - 1)) {
                    cell = position - N_COLS + 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + N_COLS + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }

 int treasurePosition = (int) (allCells * random.nextDouble()); //THIS HIDES THE pppCELL TESTING EXTRA LIFE
 while (field[treasurePosition] == COVERED_MINE_CELL) {
      treasurePosition = (int) (allCells * random.nextDouble());
 }
 field[treasurePosition] = TREASURE_CELL + COVER_FOR_CELL;  // Treasure is hidden but marked
    
}

    private void find_empty_cells(int j) {

        int current_col = j % N_COLS;
        int cell;

        if (current_col > 0) {
            cell = j - N_COLS - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS - 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - N_COLS;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + N_COLS;
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (N_COLS - 1)) {
            cell = j - N_COLS + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        
        if (isPaused) {
            // Draw pause screen
            g.drawImage(pauseImage, 
                   0, 0,                // X,Y position (top-left corner)
                   BOARD_WIDTH,         // Width to stretch to
                   BOARD_HEIGHT,        // Height to stretch to
                   this);
            return; // Skip the rest of painting while paused
        }

        //ADD PAUSED PLAY SCREEN HERE

        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {

            for (int j = 0; j < N_COLS; j++) {

                int cell = field[(i * N_COLS) + j];

               
                    //ADD OPTION FOR TREASURE HERE, IF TREASURE FOUND IN GAME != FALSE.
                    //Special message pops up that says you found treasure + 1 life. When bomb found message goes away
                
                if (inGame && cell == TREASURE_CELL) {
                    g.drawImage(img[13], (j * CELL_SIZE), (i * CELL_SIZE), this); // Assuming treasure is image 13
                }
    

              
                
                if (!inGame) {

                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }

                } else {

                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
            
        }

        if (uncover == 0 && inGame) {

            statusbar.setText("Game won");

        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
    }

    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if(isPaused){return
            ;}

            
            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            boolean doRepaint = false;

            if (!inGame) {

                newGame();
                repaint();
            }

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {

                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {

                            if (minesLeft > 0) {
                                field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                statusbar.setText(msg);
                            } else {
                                statusbar.setText("No marks left");
                            }
                        } else {

                            field[(cRow * N_COLS) + cCol] -= MARK_FOR_CELL;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText(msg);
                        }
                    }

                } else {

                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {

                        return;
                    }
                    
                    if (field[(cRow * N_COLS) + cCol] == TREASURE_CELL) {
                        extraLives++;
                        field[(cRow * N_COLS) + cCol] = EMPTY_CELL; // Uncover the cell
                        statusbar.setText("Extra life found! Mines: " + minesLeft + " | Lives: " + extraLives);
                        doRepaint = true;
                        return;
                    }
                    if (field[(cRow * N_COLS) + cCol] == COVERED_MINE_CELL) {
                        // Mine clicked
                        if (extraLives > 0) {
                            extraLives--;
                            minesLeft--;
                            field[(cRow * N_COLS) + cCol] = MINE_CELL; // Uncover the mine
                            statusbar.setText("Used extra life! Mines: " + minesLeft + " | Lives: " + extraLives);
                            doRepaint = true;
                        } else {
                            field[(cRow * N_COLS) + cCol] = MINE_CELL; // Uncover the mine
                            inGame = false;
                            statusbar.setText("Game over! Mines: " + minesLeft + " | Lives: " + extraLives);
                            doRepaint = true;
                        }
                    } else if (field[(cRow * N_COLS) + cCol] > MINE_CELL && 
                              field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL) {
                        // Regular cell clicked
                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
                            find_empty_cells((cRow * N_COLS) + cCol);
                        }
                    }
                }
                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
    
}