package enhanced2048;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class App extends PApplet {

    public static int GRID_SIZE = 4; 
    public static final int CELL_SIZE = 100; 
    public static final int CELL_RADIUS = 10;
    public static final int CELL_BUFFER = 8;
    public static int WIDTH;
    public static int HEIGHT;
    public static final int FPS = 90;
    public static int TOP_SPACE = 50;
    boolean spawnPending = false;
    boolean isGameOver = false;
    boolean time_stop = false;
    int start_time;
    int end_time;


    public Cell[][] grid;
    public static Random random = new Random();

    public App(){
    }

    @Override
    public void settings(){
        WIDTH = GRID_SIZE * CELL_SIZE + (GRID_SIZE + 1) * CELL_BUFFER;
        HEIGHT = GRID_SIZE * CELL_SIZE + (GRID_SIZE + 1) * CELL_BUFFER + TOP_SPACE;
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup(){
        frameRate(FPS);
        Cell.load_tiles(this);

        grid = new Cell[GRID_SIZE][GRID_SIZE];
        int filledSlot = 0;
        start_time = millis();

        while (filledSlot < 2) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);

            if (grid[row][col] == null) {
                int cellX = CELL_SIZE * col + CELL_BUFFER * (col + 1);
                int cellY = CELL_SIZE * row + CELL_BUFFER * (row + 1) + TOP_SPACE;
                int initialValue = random.nextDouble() < 0.5 ? 2 : 4;

                grid[row][col] = new Cell(cellX, cellY, initialValue);

                filledSlot++;
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent event){

        if (isGameOver && (key == 'r' || key == 'R')) {
            restartgame();
        }
        if (event.getKeyCode() == LEFT) {
            left();
        }
        if (event.getKeyCode() == RIGHT) {
            right();
        }
        if (event.getKeyCode() == UP) {
            up();
        }
        if (event.getKeyCode() == DOWN) {
            down();
        }
    }


    @Override
    public void mousePressed(MouseEvent e){
        for (int row = 0; row<GRID_SIZE; row ++){
            int cellY = CELL_SIZE * row + CELL_BUFFER * (row + 1) + TOP_SPACE;

            for (int col = 0; col < GRID_SIZE; col++) {
                int cellX = CELL_SIZE * col + CELL_BUFFER * (col + 1);

                if (mouseX >= cellX && mouseX <= cellX + CELL_SIZE && mouseY >= cellY
                        && mouseY <= (cellY + CELL_SIZE) && grid[row][col] == null) {
                    int initialValue = random.nextDouble() < 0.5 ? 4 : 2;
                    grid[row][col] = new Cell(cellX, cellY, initialValue);
                }
            }
        }
    }


    @Override
    public void draw(){
        background(153, 138, 123);
        fill(255);
        textAlign(RIGHT, CENTER);
        textSize(20);
        int time;
        if(time_stop == true){
            time = (end_time-start_time)/1000;
        }
        else{
            time = (millis()-start_time)/1000;
        }
        text("Timer: " + time + "s", WIDTH - 20, TOP_SPACE / 2);
        textAlign(LEFT, CENTER);
        



        for (int row = 0; row < GRID_SIZE; row++) {
            int cellY = CELL_SIZE * row + CELL_BUFFER * (row + 1)+ TOP_SPACE;

            for (int col = 0; col < GRID_SIZE; col++) {
                int cellX = CELL_SIZE * col + CELL_BUFFER * (col + 1);

                if (mouseX >= cellX && mouseX <= cellX + CELL_SIZE && mouseY >= cellY
                        && mouseY <= (cellY + CELL_SIZE)) {
                    fill(206, 193, 173);
                } else {
                    fill(186, 173, 153);
                }

                noStroke();
                rect(cellX, cellY, CELL_SIZE, CELL_SIZE, CELL_RADIUS);
            }
        }

        for (int row = 0; row < GRID_SIZE; row++) {

            for (int col = 0; col < GRID_SIZE; col++) {
                Cell cell = grid[row][col];

                if (cell != null) {
                    cell.draw(this);
                }
            }
        }

        if (spawnPending){
            boolean tospawn = true;

            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    Cell cell = grid[row][col];
                    if (cell != null && (cell.x != cell.targetX || cell.y != cell.targetY)) {
                        tospawn = false;
                        break;
                    }
                }
                if (!tospawn) 
                    break;
            }
        
            if (tospawn) {
                spawn_random();
                spawnPending = false;

                check_gameover();
            }

        }
        if (isGameOver) {
            fill(0, 200);
            rect(0, 0, WIDTH, HEIGHT);
              
            fill(255);
            textSize(48);
            textAlign(CENTER, CENTER);
            text("Game Over", WIDTH / 2, HEIGHT / 2-40);

            int total_time = (end_time - start_time) /1000;
            textSize(24);
            text("Time: " + total_time + "s", WIDTH / 2, HEIGHT / 2 + 10);
            text("Press R to restart", WIDTH / 2, HEIGHT / 2 + 50);

        }


    }

    public void check_gameover(){
        for (int row = 0; row < GRID_SIZE; row++){
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == null){
                    return;
                }
                int value = grid[row][col].getValue();
                if (col < GRID_SIZE - 1 && grid[row][col + 1] != null && grid[row][col + 1].getValue() == value){
                    return;
                } 
                if (row < GRID_SIZE - 1 && grid[row + 1][col] != null && grid[row + 1][col].getValue() == value){
                    return;
                } 
            }
        }
        isGameOver = true;
        if (!time_stop) {
            end_time = millis();
            time_stop = true;
        }
        
    }

    public void restartgame(){
        isGameOver = false;
        time_stop = false;
        start_time = millis();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = null;
            }
        }

        spawn_random();
        spawn_random();
    }
    
    public void spawn_random(){
        ArrayList <int[]> empty_tiles = new ArrayList<>();
        for (int row = 0; row<GRID_SIZE; row++){
            for (int col = 0; col<GRID_SIZE; col++){
                if (grid[row][col] == null) {
                    empty_tiles.add(new int[] {row, col});
                }
            }
        }
        if (!empty_tiles.isEmpty()){
            int[] random_tile = empty_tiles.get(random.nextInt(empty_tiles.size()));
            int row = random_tile[0];
            int col = random_tile[1];

            int initialValue = random.nextDouble() < 0.5 ? 2 : 4;

            int cellX = CELL_SIZE * col + CELL_BUFFER * (col + 1);
            int cellY = CELL_SIZE * row + CELL_BUFFER * (row + 1) + TOP_SPACE;

            grid[row][col] = new Cell(cellX, cellY, initialValue);
        }

    }


    private void moveCell(int InitialRow, int InitialCol, int FinalRow, int FinalCol, int val) {

        int startX = CELL_SIZE * InitialCol + CELL_BUFFER * (InitialCol + 1);
        int startY = CELL_SIZE * InitialRow + CELL_BUFFER * (InitialRow + 1) + TOP_SPACE;
    
        int targetX = CELL_SIZE * FinalCol + CELL_BUFFER * (FinalCol + 1);
        int targetY = CELL_SIZE * FinalRow + CELL_BUFFER * (FinalRow + 1) + TOP_SPACE;
    
        Cell moving = new Cell(startX, startY, val);
        moving.setTarget(targetX, targetY);
        grid[FinalRow][FinalCol] = moving;

    }

    
    public void left(){
        boolean change = false;

        for (int row = 0; row < GRID_SIZE; row++) {
            ArrayList<Integer> values = new ArrayList<>();
            ArrayList<Integer> prevCols = new ArrayList<>();
    
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] != null) {
                    values.add(grid[row][col].getValue());
                    prevCols.add(col);
                }
            }

            ArrayList<Integer> updated_tiles = new ArrayList<>();
            ArrayList<Integer> sourceCols = new ArrayList<>();

            int counter = 0;
            while (counter<values.size()){
                if (counter + 1 < values.size() && values.get(counter).equals(values.get(counter + 1))) {
                    updated_tiles.add(values.get(counter) * 2);
                    sourceCols.add(prevCols.get(counter+ 1));
                    counter += 2;
                }
                else{
                    updated_tiles.add(values.get(counter));
                    sourceCols.add(prevCols.get(counter));
                    counter++;
                }
            }
            while (updated_tiles.size() < GRID_SIZE) {
                updated_tiles.add(null);
                sourceCols.add(-1); 
            }
            for (int col = 0; col<GRID_SIZE; col++){
                Integer value = updated_tiles.get(col);
                int prevCol = sourceCols.get(col);

                if (value == null) {
                    if (grid[row][col] != null) {
                        change = true;
                    }

                grid[row][col] = null;
                } 
                else {
                    if (prevCol != -1 && prevCol != col) {
                        change = true;
                    } 
    
                    moveCell(row, prevCol, row, col, value);
                }
            }
        }
        
        if (change) {
            spawnPending = true;
        }
    }


    public void right(){
        boolean change = false;

        for (int row = 0; row < GRID_SIZE; row++) {
            ArrayList<Integer> values = new ArrayList<>();
            ArrayList<Integer> prevCols = new ArrayList<>();
    
            for (int col = GRID_SIZE - 1; col >= 0; col--) {
                if (grid[row][col] != null) {
                    values.add(grid[row][col].getValue());
                    prevCols.add(col);
                }
            }

            ArrayList<Integer> updated_tiles = new ArrayList<>();
            ArrayList<Integer> sourceCols = new ArrayList<>();

            int counter = 0;
            while (counter<values.size()){
                if (counter + 1 < values.size() && values.get(counter).equals(values.get(counter + 1))) {
                    updated_tiles.add(values.get(counter) * 2);
                    sourceCols.add(prevCols.get(counter + 1));
                    counter += 2;
                }
                else{
                    updated_tiles.add(values.get(counter));
                    sourceCols.add(prevCols.get(counter));
                    counter++;
                }
            }
            while (updated_tiles.size() < GRID_SIZE) {
                updated_tiles.add(null);
                sourceCols.add(-1); 
            }
            for (int col = GRID_SIZE - 1, i = 0; col >= 0; col--, i++){
                Integer value = updated_tiles.get(i);
                int prevCol = sourceCols.get(i);

                if (value == null) {
                    if (grid[row][col] != null) {
                        change = true;
                    }

                grid[row][col] = null;
                } 
                else {
                    if (prevCol != -1 && prevCol != col) {
                        change = true;
                    } 
    
                    moveCell(row, prevCol, row, col, value);
                }
            }
        }
        
        if (change) {
            spawnPending = true;
        }
    }


    public void up(){
        boolean change = false;

        for (int col = 0; col < GRID_SIZE; col++) {
            ArrayList<Integer> values = new ArrayList<>();
            ArrayList<Integer> prevRows = new ArrayList<>();
    
            for (int row = 0; row < GRID_SIZE; row++) {
                if (grid[row][col] != null) {
                    values.add(grid[row][col].getValue());
                    prevRows.add(row);
                }
            }

            ArrayList<Integer> updated_tiles = new ArrayList<>();
            ArrayList<Integer> sourceRows = new ArrayList<>();

            int counter = 0;
            while (counter<values.size()){
                if (counter + 1 < values.size() && values.get(counter).equals(values.get(counter + 1))) {
                    updated_tiles.add(values.get(counter) * 2);
                    sourceRows.add(prevRows.get(counter + 1));
                    counter += 2;
                }
                else{
                    updated_tiles.add(values.get(counter));
                    sourceRows.add(prevRows.get(counter));
                    counter++;
                }
            }
            while (updated_tiles.size() < GRID_SIZE) {
                updated_tiles.add(null);
                sourceRows.add(-1); 
            }
            for (int row = 0; row<GRID_SIZE; row++){
                Integer value = updated_tiles.get(row);
                int prevRow = sourceRows.get(row);

                if (value == null) {
                    if (grid[row][col] != null) {
                        change = true;
                    }

                grid[row][col] = null;
                } 
                else {
                    if (prevRow != -1 && prevRow != row) {
                        change = true;
                    } 
    
                    moveCell(prevRow, col  , row, col, value);
                }
            }
        }
        
        if (change) {
            spawnPending = true;
        }
    }


    public void down(){
        boolean change = false;

        for (int col = 0; col < GRID_SIZE; col++) {
            ArrayList<Integer> values = new ArrayList<>();
            ArrayList<Integer> prevRows = new ArrayList<>();
    
            for (int row = GRID_SIZE - 1; row >= 0; row--) {
                if (grid[row][col] != null) {
                    values.add(grid[row][col].getValue());
                    prevRows.add(row);
                }
            }

            ArrayList<Integer> updated_tiles = new ArrayList<>();
            ArrayList<Integer> sourceRows = new ArrayList<>();

            int counter = 0;
            while (counter<values.size()){
                if (counter + 1 < values.size() && values.get(counter).equals(values.get(counter + 1))) {
                    updated_tiles.add(values.get(counter) * 2);
                    sourceRows.add(prevRows.get(counter+1));
                    counter += 2;
                }
                else{
                    updated_tiles.add(values.get(counter));
                    sourceRows.add(prevRows.get(counter));
                    counter++;
                }
            }
            while (updated_tiles.size() < GRID_SIZE) {
                updated_tiles.add(null);
                sourceRows.add(-1); 
            }
            for (int row = GRID_SIZE - 1, i=0; row >= 0; row--, i++){
                Integer value = updated_tiles.get(i);
                int prevRow = sourceRows.get(i);

                if (value == null) {
                    if (grid[row][col] != null) {
                        change = true;
                    }

                grid[row][col] = null;
                } 
                else {
                    if (prevRow != -1 && prevRow != row) {
                        change = true;
                    } 
    
                    moveCell(prevRow, col  , row, col, value);
                }
            }
        }
        
        if (change) {
            spawnPending = true;
        }
    }


    public static void main(String[] args) {
        if (args.length >0){
            GRID_SIZE = Integer.parseInt(args[0]);
        }
        PApplet.main("enhanced2048.App");
    }

}

