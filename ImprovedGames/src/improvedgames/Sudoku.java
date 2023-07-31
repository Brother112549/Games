package improvedgames;
/*Created By Kelby Morrison
Sep 26, 2022
 */

//TO DO: add visuals for progress, option to not print to console, and option to solve manually

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.util.Pair;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

public class Sudoku {

    public void Setup(JFrame frame) {
        //get user input for Board size
        JPanel message = new JPanel();
        frame.setContentPane(message);
        message.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.insets = new Insets(10,10,5,10);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        message.add(new JLabel("How many squares should make up the side of a subgrid?"), c);
        JSlider Width = new JSlider(1, 3, 3);
        JLabel WLabel = new JLabel("NxN subgrid: N=" + Integer.toString(Width.getValue()));
        JButton Confirm = new JButton("Confirm");
        c.gridwidth = 1;
        c.gridy = 1;
        message.add(WLabel, c);
        c.gridx = 1;
        message.add(Width, c);
        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 2;
        message.add(Confirm, c);
        
        //slider actively displays value
        Width.addChangeListener((ChangeEvent e) -> {
            WLabel.setText("NxN subgrid: N=" + Integer.toString(Width.getValue()));
            frame.pack();
        });
        Confirm.addActionListener((ActionEvent e) -> {
            makeBoard(frame, Width.getValue());
        });
        frame.pack();
    }

    //get user input for initial setup of board
    private void makeBoard(JFrame frame, int dim) {
        //Set up Game Board
        frame.setTitle("Games - Sudoku - Fill in starting squares");
        JPanel pane = new JPanel();
        frame.setContentPane(pane);
        JPanel board = new JPanel();
        GridLayout boardLayout = new GridLayout(dim, dim);
        boardLayout.setHgap(2);
        boardLayout.setVgap(2);
        board.setLayout(boardLayout);
        board.setBackground(Color.BLACK);
        Cell[][] Cells = new Cell[dim * dim][dim * dim];
        for(int outerH = 0; outerH < dim; outerH++){
            for(int outerW = 0; outerW < dim; outerW++){
                JPanel SubGrid = new JPanel();
                SubGrid.setLayout(new GridLayout(dim, dim));
                for(int h = 0; h < dim; h++){
                    for(int w = 0; w < dim; w++){
                        Cell acell = new Cell(dim);
                        Cells[(outerH * dim) + h][(outerW * dim) + w] = acell;
                        SubGrid.add(acell.getTextField());
                    }
                }
                board.add(SubGrid);
            }
        }
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.ipadx = 10;
        c.ipady = 10;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(board, c);
        
        JButton Confirm = new JButton("Confirm");
        Confirm.addActionListener((ActionEvent e) -> {
            new Sudoku().Selection(frame, Cells, dim);
        });
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.insets = new Insets(1,1,1,1);
        c.gridy = 1;
        pane.add(Confirm, c);
        frame.pack();
        frame.setMinimumSize(frame.getMinimumSize());
    }
    
    //select method for solving the board
    private void Selection(JFrame frame, Cell[][]Cells, int dim){
        frame.setMinimumSize(null);
        frame.setTitle("Games - Sudoku");
        JPanel board = (JPanel) frame.getContentPane().getComponent(0);
        frame.setContentPane(new JPanel());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        JLabel Label = new JLabel("What method would you like to use to solve the problem?");
        frame.add(Label);
        c.gridwidth = 1;
        c.gridy = 1;
        JButton Back = new JButton("Backtracking");
        Back.addActionListener((ActionEvent e) -> {
            frame.setContentPane(board);
            frame.pack();
            frame.setMinimumSize(frame.getPreferredSize());
            for(int h = 0; h < (dim * dim); h++){
                for(int w = 0; w < (dim * dim); w++){
                    Cells[h][w].lockValue();
                }
            }
            if(new Sudoku().Backtrack(Cells, dim, 0)){
                System.out.println("Success");
                printBoard(Cells, dim);
            }else{
                System.out.println("Failure");
            }
        });
        frame.add(Back);
        JButton Fore = new JButton("Forward Checking");
        c.gridx = 1;
        Fore.addActionListener((ActionEvent e) -> {
            frame.setContentPane(board);
            frame.pack();
            frame.setMinimumSize(frame.getPreferredSize());
            for(int h = 0; h < (dim * dim); h++){
                for(int w = 0; w < (dim * dim); w++){
                    Cells[h][w].lockValue();
                }
            }
            if(forwardCheckingInitial(Cells, dim)){
                if(new Sudoku().Backtrack(Cells, dim, 1)){
                    System.out.println("Success");
                    printBoard(Cells, dim);
                }else{
                    System.out.println("Failure");
                }
            }else{
                System.out.println("Unsolvable Board Detected");
            }
        });
        frame.add(Fore);
        JButton Arcc = new JButton("Arc Consistency");
        c.gridx = 2;
        Arcc.addActionListener((ActionEvent e) -> {
            frame.setContentPane(board);
            frame.pack();
            frame.setMinimumSize(frame.getPreferredSize());
            for(int h = 0; h < (dim * dim); h++){
                for(int w = 0; w < (dim * dim); w++){
                    Cells[h][w].lockValue();
                }
            }
            Pair<Integer, Cell[][]> result = arcConsistencyInitial(Cells, dim);
            if(result.getKey() == 1){
                System.out.println("Solution found through arc consistency");
                printBoard(result.getValue(), dim);
            }else if(result.getKey() == 2){
                System.out.println("Multiple possible solutions detected");
                if(new Sudoku().Backtrack(Cells, dim, 2)){
                    System.out.println("Success");
                    printBoard(Cells, dim);
                }else{
                    System.out.println("Failure");
                }
            }else{
                System.out.println("Unsolvable Board Detected");
            }
        });
        frame.add(Arcc);
        frame.pack();
    }
    
    //print completed board to terminal
    private void printBoard(Cell[][] Cells, int dim){
        int Dim = dim * dim;
        String line = "--";
        for(int col = 0; col < Dim; col++){
            System.out.print(line);
        }
        System.out.println("-");
        for(int row = 0; row < Dim; row++){
            for(int col = 0; col < Dim; col++){
                System.out.print("|" + Cells[row][col].getValue());
            }
            System.out.println("|");
            for(int col = 0; col < Dim; col++){
                System.out.print(line);
            }
            System.out.println("-");
        }
    }
    
    //returns a copy that has all data from the original
    private Cell[][] deepcopy(Cell[][] Cells, int dim){
        int Dim = dim * dim;
        Cell[][] newCells = new Cell[Dim][Dim];
        for(int h = 0; h < Dim; h++){
            for(int w = 0; w < Dim; w++){
                Cell acell = new Cell(dim);
                acell.deepcopy(Cells[h][w]);
                newCells[h][w] = acell;
            }
        }
        return newCells;
    }
    
    //Values in Cells are replaced by values in Data
    private void datacopy(Cell[][] Cells, Cell[][] Data, int dim){
        int Dim = dim * dim;
        for(int h = 0; h < Dim; h++){
            for(int w = 0; w < Dim; w++){
                Cells[h][w].setValue(Data[h][w].getValue());
            }
        }
    }
    
    //checks to see if there are any unassigned cells
    private boolean isAssignmentComplete(Cell[][] Cells){
        for(Cell[] cells: Cells){
            for(Cell cell: cells){
                if(cell.getValue() == 0){
                    return false;
                }
            }
        }
        return true;
    }
    
    //checks for standard sudoku board rules consistency
    private boolean isConsistent(Cell[][] Cells, int dim){
        int Dim = dim * dim;
        //check row and col consistency
        for(int y = 0; y < Dim; y++){
            Set<Integer> rowCheck = new HashSet<>();
            Set<Integer> colCheck = new HashSet<>();
            for(int x = 0; x < Dim; x++){
                if(Cells[y][x].getValue() != 0){
                    //if duplicate number on the same row
                    if(rowCheck.add(Cells[y][x].getValue()) == false){
                        return false;
                    }
                }
                if(Cells[x][y].getValue() != 0){
                    //if duplicate number on the same col
                    if(colCheck.add(Cells[x][y].getValue()) == false){
                        return false;
                    }
                }
            }
        }
        //check subgrid consistency
        for(int outerH = 0; outerH < dim; outerH++){
            for(int outerW = 0; outerW < dim; outerW++){
                //for each subgrid:
                Set<Integer> gridCheck = new HashSet<>();
                for(int h = 0; h < dim; h++){
                    for(int w = 0; w < dim; w++){
                        if(Cells[(outerH * dim) + h][(outerW * dim) + w].getValue() != 0){
                            //if duplicate number in the same subgrid
                            if(gridCheck.add(Cells[(outerH * dim) + h][(outerW * dim) + w].getValue()) == false){
                                return false;
                            }
                        }
                    }
                }
                
            }
        }
        return true;
    }
    
    //called before backtrack but after lockValue
    //updates domain based on user input
    private boolean forwardCheckingInitial(Cell[][] Cells, int dim){
        int Dim = dim * dim;
        //update row and col domains
        for(int h = 0; h < Dim; h++){
            for(int w = 0; w < Dim; w++){
                if(Cells[h][w].getValue() != 0){
                    //remove number from domains of cells on same row
                    for(int col = 0; col < Dim; col++){
                        if(col != w){
                            Cells[h][col].removeFromDomain(Cells[h][w].getValue());
                        }
                    }
                    //remove number from domains of cells on same col
                    for(int row = 0; row < Dim; row++){
                        if(row != h){
                            Cells[row][w].removeFromDomain(Cells[h][w].getValue());
                        }
                    }
                }
            }
        }
        //update subgrid domains
        for(int outerH = 0; outerH < dim; outerH++){
            for(int outerW = 0; outerW < dim; outerW++){
                for(int H = 0; H < dim; H++){
                    for(int W = 0; W < dim; W++){
                        if(Cells[(outerH * dim) + H][(outerW * dim) + W].getValue() != 0){
                            //remove number from domains of cells in same subgrid
                            for(int h = 0; h < dim; h++){
                                for(int w = 0; w < dim; w++){
                                    if(h != H && w != W){
                                        Cells[(outerH * dim) + h][(outerW * dim) + w].removeFromDomain(
                                                Cells[(outerH * dim) + H][(outerW * dim) + W].getValue());
                                    }
                                }
                            }
                        }
                    }
                }
                
            }
        }
        //check if any cell has an empty domain (meaning board is unsolvable)
        for(int row = 0; row < Dim; row++){
            for(int col = 0; col < Dim; col++){
                if(Cells[row][col].getDomain().isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }
    
    //called to update domains during backtrack
    private Pair<Boolean, Cell[][]> forwardChecking(Cell[][] Cells, int dim, int H, int W, int val){
        int Dim = dim * dim;
        //assign new value in a deepcopy of Cells to check without messing up Cells data
        Cell[][] newCells = deepcopy(Cells, dim);
        
        //update row and col domains
        for(int x = 0; x < Dim; x++){
            if(x != W){
                newCells[H][x].removeFromDomain(val);
                if(newCells[H][x].getDomain().isEmpty()){
                    return new Pair(false, Cells);
                }
            }
            if(x != H){
                newCells[x][W].removeFromDomain(val);
                if(newCells[x][W].getDomain().isEmpty()){
                    return new Pair(false, Cells);
                }
            }
        }
        //update subgrid domains
        int outerH = H / dim;
        int outerW = W / dim;
        for(int h = 0; h < dim; h++){
            for(int w = 0; w < dim; w++){
                if(((outerH * dim) + h) != H && ((outerW * dim) + w) != W){
                    newCells[(outerH * dim) + h][(outerW * dim) + w].removeFromDomain(val);
                    if(newCells[(outerH * dim) + h][(outerW * dim) + w].getDomain().isEmpty()){
                        return new Pair(false, Cells);
                    }
                }
            }
        }
        return new Pair(true, newCells);
    }
    
    //return 0 if no solution possible
    //return 1 if solution found
    //return 2 if multiple solutions possible
    private Pair<Integer, Cell[][]> arcConsistencyInitial(Cell[][] Cells, int dim){
        int Dim = dim * dim;
        //queue contents: <<r1, c1>, <r2, c2>>, where ri and ci make position of a cell and the cells are neighbors
        ArrayList<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> queue = new ArrayList<>();
        //add row and col neighbors to queue if at least one is assigned
        for(int h = 0; h < Dim; h++){
            for(int w = 0; w < Dim; w++){
                if(Cells[h][w].getValue() != 0){
                    //add nieghbors on same row to queue
                    for(int col = 0; col < Dim; col++){
                        if(col != w){
                            queue.add(new Pair(new Pair(h, w), new Pair(h, col)));
                        }
                    }
                    //add neighbors on same col to queue
                    for(int row = 0; row < Dim; row++){
                        if(row != h){
                            queue.add(new Pair(new Pair(h, w), new Pair(row, w)));
                        }
                    }
                }
            }
        }
        //add subgrid neighbors to queue if at least one is assigned
        for(int outerH = 0; outerH < dim; outerH++){
            for(int outerW = 0; outerW < dim; outerW++){
                for(int H = 0; H < dim; H++){
                    for(int W = 0; W < dim; W++){
                        if(Cells[(outerH * dim) + H][(outerW * dim) + W].getValue() != 0){
                            //add neighbors in same subgrid to queue
                            for(int h = 0; h < dim; h++){
                                for(int w = 0; w < dim; w++){
                                    if(h != H && w != W){
                                        queue.add(new Pair(new Pair((outerH * dim) + H, (outerW * dim) + W), 
                                                new Pair((outerH * dim) + h, (outerW * dim) + w)));
                                    }
                                }
                            }
                        }
                    }
                }
                
            }
        }
        
        //while queue isn't empty
        while(!queue.isEmpty()){
            Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> locs = queue.remove(0);
            int r1 = locs.getKey().getKey();
            int c1 = locs.getKey().getValue();
            int r2 = locs.getValue().getKey();
            int c2 = locs.getValue().getValue();
            //note: Cells[r1][c1].getValue() never = 0
            
//            System.out.println("Considering: <" + r1 + "," + c1 + "> to domain of <" + r2 + "," + c2 + ">");
//            System.out.println("Value  of <" + r1 + "," + c1 + ">: " + Cells[r1][c1].getValue());
//            System.out.println("Domain of <" + r2 + "," + c2 + ">: " + Cells[r2][c2].getDomain().toString());
            
            int lenBefore = Cells[r2][c2].getDomain().size();
            Cells[r2][c2].removeFromDomain(Cells[r1][c1].getValue());
            int lenAfter = Cells[r2][c2].getDomain().size();
            
//            System.out.println("Domain of <" + r2 + "," + c2 + ">: " + Cells[r2][c2].getDomain().toString() + "\n");
            
            //if Cells[r2][c2] Domain changed
            if(lenBefore != lenAfter){
                if(Cells[r2][c2].getDomain().isEmpty()){
                    //invalid board b/c domain of a cell is empty
                    return new Pair(0, Cells);
                }else if(lenAfter == 1){
                    //if domain of a cell has size one, value of cell is determined
                    Cells[r2][c2].setValue(Cells[r2][c2].getDomain().get(0));
                    //the neighbors of this cell need their domains updated
                    for(int x = 0; x < Dim; x++){
                        if(x != c2){
                            //add row neighbors to queue
                            queue.add(new Pair(new Pair(r2, c2), new Pair(r2, x)));
                        }
                        if(x != r2){
                            //add col neighbors to queue
                            queue.add(new Pair(new Pair(r2, c2), new Pair(x, c2)));
                        }
                    }
                    //add subgrid neighbors to queue
                    int outerH = r2 / dim;
                    int outerW = c2 / dim;
                    for(int h = 0; h < dim; h++){
                        for(int w = 0; w < dim; w++){
                            if(((outerH * dim) + h) != r2 && ((outerW * dim) + w) != c2){
                                queue.add(new Pair(new Pair(r2, c2), new Pair((outerH * dim) + h, (outerW * dim) + w)));
                            }
                        }
                    }
                }
            }
        }
        
        //check to see if any cell has multiple options
        for(int row = 0; row < Dim; row++){
            for(int col = 0; col < Dim; col++){
                if(Cells[row][col].getDomain().size() != 1){
                    return new Pair(2, Cells);
                }
            }
        }
        
        //return found solution
        return new Pair(1, Cells);
    }
    
    private Pair<Boolean, Cell[][]> arcConsistency(Cell[][] Cells, int dim, int H, int W, int val){
        int Dim = dim * dim;
        //queue contents: <<r1, c1>, <r2, c2>>, where ri and ci make position of a cell and the cells are neighbors
        ArrayList<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> queue = new ArrayList<>();
        
        //assign new value in a deepcopy of Cells to check without messing up Cells data
        Cell[][] newCells = deepcopy(Cells, dim);
        newCells[H][W].setDomain(val);
        //the neighbors of this cell need their domains updated
        for(int x = 0; x < Dim; x++){
            if(x != W){
                //add row neighbors to queue
                queue.add(new Pair(new Pair(H, W), new Pair(H, x)));
            }
            if(x != H){
                //add col neighbors to queue
                queue.add(new Pair(new Pair(H, W), new Pair(x, W)));
            }
        }
        //add subgrid neighbors to queue
        int outerH = H / dim;
        int outerW = W / dim;
        for(int h = 0; h < dim; h++){
            for(int w = 0; w < dim; w++){
                if(((outerH * dim) + h) != H && ((outerW * dim) + w) != W){
                    queue.add(new Pair(new Pair(H, W), new Pair((outerH * dim) + h, (outerW * dim) + w)));
                }
            }
        }
        
        //while queue isn't empty
        while(!queue.isEmpty()){
            Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> locs = queue.remove(0);
            int r1 = locs.getKey().getKey();
            int c1 = locs.getKey().getValue();
            int r2 = locs.getValue().getKey();
            int c2 = locs.getValue().getValue();
            //note: Cells[r1][c1].getValue() never = 0
            
//            System.out.println("Considering: <" + r1 + "," + c1 + "> to domain of <" + r2 + "," + c2 + ">");
//            System.out.println("Value  of <" + r1 + "," + c1 + ">: " + Cells[r1][c1].getValue());
//            System.out.println("Domain of <" + r2 + "," + c2 + ">: " + Cells[r2][c2].getDomain().toString());
            
            int lenBefore = newCells[r2][c2].getDomain().size();
            newCells[r2][c2].removeFromDomain(newCells[r1][c1].getValue());
            int lenAfter = newCells[r2][c2].getDomain().size();
            
//            System.out.println("Domain of <" + r2 + "," + c2 + ">: " + Cells[r2][c2].getDomain().toString() + "\n");
            
            //if Cells[r2][c2] Domain changed
            if(lenBefore != lenAfter){
                if(newCells[r2][c2].getDomain().isEmpty()){
                    //invalid board b/c domain of a cell is empty
                    //return original board
                    return new Pair(false, Cells);
                }else if(lenAfter == 1){
                    //if domain of a cell has size one, value of cell is determined
                    newCells[r2][c2].setValue(newCells[r2][c2].getDomain().get(0));
                    //the neighbors of this cell need their domains updated
                    for(int x = 0; x < Dim; x++){
                        if(x != c2){
                            //add row neighbors to queue
                            queue.add(new Pair(new Pair(r2, c2), new Pair(r2, x)));
                        }
                        if(x != r2){
                            //add col neighbors to queue
                            queue.add(new Pair(new Pair(r2, c2), new Pair(x, c2)));
                        }
                    }
                    //add subgrid neighbors to queue
                    outerH = r2 / dim;
                    outerW = c2 / dim;
                    for(int h = 0; h < dim; h++){
                        for(int w = 0; w < dim; w++){
                            if(((outerH * dim) + h) != r2 && ((outerW * dim) + w) != c2){
                                queue.add(new Pair(new Pair(r2, c2), new Pair((outerH * dim) + h, (outerW * dim) + w)));
                            }
                        }
                    }
                }
            }
        }
        
        //return new board
        return new Pair(true, newCells);
    }

    //To be called when Cells is part of a jframe and will update to completed values
    //mode = 0: normal backtrack
    //mode = 1: backtrack with forward checking
    //mode = 2: backtrack with arc consistency
    private boolean Backtrack(Cell[][] Cells, int dim, int mode) {
        if(isAssignmentComplete(Cells)){
            return true;
        }
        
        //determine position of next unassigned cell
        int Dim = dim * dim;
        int H = 0;
        int W = 0;
        boolean found = false;
        for(H = 0; H < Dim; H++){
            for(W = 0; W < Dim; W++){
                if(Cells[H][W].getValue() == 0){
                    found = true;
                    break;
                }
            }
            if(found){
                break;
            }
        }
        
        //check all possible values in the domain of the cell
        for(int val: Cells[H][W].getDomain()){
            Cells[H][W].setValue(val);
            if(isConsistent(Cells, dim)){
                
                //foreward checking or arc consistency inference update
                boolean inference = true;
                if(mode == 1){
                    //foreward checking
                    Pair<Boolean, Cell[][]> result = forwardChecking(Cells, dim, H, W, val);
                    inference = result.getKey();
                    if(inference){
                        Pair<Boolean, Cell[][]> ret = recursiveBacktrack(result.getValue(), dim, mode);
                        if(ret.getKey()){
                            //solution found
                            datacopy(Cells, ret.getValue(), dim);
                            return true;
                        }
                    }
                }else if(mode == 2){
                    //arc consistency
                    Pair<Boolean, Cell[][]> result = arcConsistency(Cells, dim, H, W, val);
                    inference = result.getKey();
                    if(inference){
                        Pair<Boolean, Cell[][]> ret = recursiveBacktrack(result.getValue(), dim, mode);
                        if(ret.getKey()){
                            //solution found
                            datacopy(Cells, ret.getValue(), dim);
                            return true;
                        }
                    }
                }else if(inference){    //always true for normal backtracking
                    Pair<Boolean, Cell[][]> ret = recursiveBacktrack(Cells, dim, mode);
                    if(ret.getKey()){
                        //solution found
                        datacopy(Cells, ret.getValue(), dim);
                        return true;
                    }
                }
            }
        }
        
        //backtrack => no solution found
        return false;
    }

    //To be called internally or when Cell data will be extracted externally after completion
    //mode = 0: normal backtrack
    //mode = 1: backtrack with forward checking
    //mode = 2: backtrack with arc consistency
    private Pair<Boolean, Cell[][]> recursiveBacktrack(Cell[][] Cells, int dim, int mode) {
        if(isAssignmentComplete(Cells)){
            return new Pair(true, Cells);
        }
        
        //determine position of next unassigned cell
        int Dim = dim * dim;
        int H = 0;
        int W = 0;
        boolean found = false;
        for(H = 0; H < Dim; H++){
            for(W = 0; W < Dim; W++){
                if(Cells[H][W].getValue() == 0){
                    found = true;
                    break;
                }
            }
            if(found){
                break;
            }
        }
        
        //check all possible values in the domain of the cell
        for(int val: Cells[H][W].getDomain()){
            Cells[H][W].setValue(val);
            if(isConsistent(Cells, dim)){
                
                //foreward checking or arc consistency inference update
                boolean inference = true;
                if(mode == 1){
                    //foreward checking
                    Pair<Boolean, Cell[][]> result = forwardChecking(Cells, dim, H, W, val);
                    inference = result.getKey();
                    if(inference){
                        Pair<Boolean, Cell[][]> ret = recursiveBacktrack(result.getValue(), dim, mode);
                        if(ret.getKey()){
                            //solution found
                            return new Pair(true, ret.getValue());
                        }
                    }
                }else if(mode == 2){
                    //arc consistency
                    Pair<Boolean, Cell[][]> result = arcConsistency(Cells, dim, H, W, val);
                    inference = result.getKey();
                    if(inference){
                        Pair<Boolean, Cell[][]> ret = recursiveBacktrack(result.getValue(), dim, mode);
                        if(ret.getKey()){
                            //solution found
                            return new Pair(true, ret.getValue());
                        }
                    }
                }else if(inference){    //always true for normal backtracking
                    Pair<Boolean, Cell[][]> ret = recursiveBacktrack(Cells, dim, mode);
                    if(ret.getKey()){
                        //solution found
                        return new Pair(true, ret.getValue());
                    }else{
                        //continue searching
                    }
                }
            }
        }
        
        //backtrack
        Cells[H][W].setValue(0);
        return new Pair(false, Cells);
    }
    
    private class Cell{
        
        private ArrayList<Integer> domain;
        private JTextField Field;
        private int value = 0;
        private final Color Initial = Color.BLACK;
        private final Color Worked = Color.GRAY;
        
        Cell(int N){
            Field = new JTextField();
            domain = new ArrayList<>();
            //establish default domain based on board size
            int range = N*N;
            for(int i = 1; i <= range; i++){
                domain.add((Integer) i);
            }
            //set up JTextField appearance
            Field.setPreferredSize(new Dimension(32,32));
            Field.setFont(new Font("SansSerif", Font.BOLD, 16));
            Field.setHorizontalAlignment(JTextField.CENTER);
        }
        
        private JTextField getTextField(){
            return Field;
        }
        
        private void lockValue(){
            try{
                Integer val = Integer.parseInt(Field.getText());
                //only accept values in calculated domain
                if(domain.contains(val)){
                    value = val;
                    domain.clear();
                    domain.add(val);
                    Field.setForeground(Initial);
                }else{
                    Field.setForeground(Worked);
                    Field.setText("");
                }
            } catch (Exception e){
                //non-integer value in TextField
                Field.setForeground(Worked);
                Field.setText("");
            }
            Field.setEditable(false);
        }
        
        private int getValue(){
            return value;
        }
        
        //sets text in JTextField and value to val
        private void setValue(int val){
            value = val;
            if(val == 0){
                Field.setText("");
            }else{
                Field.setText(Integer.toString(value));
            }
        }
        
        private ArrayList<Integer> getDomain(){
            return domain;
        }
        
        //set domain to specific value to be used when using setValue and, ideally, a deepcopy of Cell[][]
        private void setDomain(int val){
            domain.clear();
            domain.add(val);
        }
        
        //copies data from this cell to new cell
        //does not copy JTextField appearance data
        private void deepcopy(Cell cell){
            this.Field.setText(cell.Field.getText());
            this.domain.clear();
            for(int val: cell.domain){
                this.domain.add(val);
            }
            this.value = cell.value;
        }

        //remove value from domain
        //note: cast to Integer so as to not remove value at domain[value]
        private void removeFromDomain(int val) {
            domain.remove((Integer) val);
        }
    }

}