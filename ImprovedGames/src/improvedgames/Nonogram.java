package improvedgames;
/*Created By Kelby
Feb 23, 2020
*/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
public class Nonogram 
{
    private int height = 5;
    private int width = 5;
    private int hcols;
    private int wrows;
    private int numActive = 0;
    private int Goal;
    private final int maxh = 30;    //MUST BE Multiple of 5
    private final int maxw = 30;    //MUST BE Multiple of 5
    private Cell cell[][];
        //order of wfields/wlabels
        // 0  1  2  3  4
        // 5  6  7  8  9
        //10 11 12 13 14
    private ArrayList<JLabel> wlabels;
    private ArrayList<JTextField> wfields;
    private int[][] wnums;
        //order of hfields/hlabels
        // 0  1  2
        // 3  4  5
        // 6  7  8
        // 9 10 11
        //12 13 14
    private ArrayList<JLabel> hlabels;
    private ArrayList<JTextField> hfields;
    private int[][] hnums;
    //draw x on button
    private BufferedImage EX;
    private boolean isDone;
    private static Timer time;
    
    public Nonogram(){
        createX();
    }
    
    private void createX(){
        //modified from:
        //https://stackoverflow.com/questions/30059039/java-graphics-draw-shape-on-jbutton
        EX = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = EX.createGraphics();
        g.setBackground(null);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.red);
        g.drawLine(20, 20, 480, 480);
        g.drawLine(20, 480, 480, 20);
        g.dispose();
    }
    
    public void Mode(JFrame frame){ //method for selecting game mode
        frame.setTitle("Games - Nonogram");
        JPanel message = new JPanel();
        frame.setContentPane(message);
        message.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.insets = new Insets(10,10,5,10);
        c.gridx = 0;
        c.gridy = 0;
        message.add(new JLabel("What would you like to do?"), c);
        c.gridy = 1;
        c.insets = new Insets(5,10,10,10);
        JPanel options = new JPanel();
        message.add(options, c);
        options.setLayout(new FlowLayout());
        JButton cr = new JButton("Create");
        cr.addActionListener((ActionEvent e) -> {
            SetBoard(frame, 0);
        });
        options.add(cr);
        JButton so = new JButton("Solve");
        so.addActionListener((ActionEvent e) -> {
            SetBoard(frame, 1);
        });
        options.add(so);
        JButton pl = new JButton("Play");
        pl.addActionListener((ActionEvent e) -> {
            Play(frame);
        });
        options.add(pl);
        frame.pack();
    }
    
    private void SetBoard(JFrame frame, int mode){  //method for setting up board for create and solve modes only
        JPanel pane = new JPanel();
        frame.setContentPane(pane);
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx  = 0;
        c.gridy = 0;
        JPanel board = new JPanel();
        board.setName("Board");
        GridLayout BLay = new GridLayout(height / 5, width / 5);
        BLay.setHgap(1);
        BLay.setVgap(1);
        board.setLayout(BLay);
        //initialize cells to maximum size
        cell = new Cell[maxh][maxw];
        for(int H = 0; H < maxh; H++){
            for(int W = 0; W < maxw; W++){
                Cell acell = new Cell();
                acell.setUp(H, W);
                cell[H][W] = acell;
            }
        }
        //add cells to blocks for easier counting with a border
        int blockH = maxh/5;
        int blockW = maxw/5;
        JPanel[][] Blocks = new JPanel[blockH][blockW];
        for(int H = 0; H < blockH; H++){
            for(int W = 0; W < blockW; W++){
                Blocks[H][W] = new JPanel();
                Blocks[H][W].setLayout(new GridLayout(5, 5));
                Blocks[H][W].setOpaque(false);
                for(int ch = 0; ch < 5; ch++){
                    for(int cw = 0; cw < 5; cw++){
                        Blocks[H][W].add(cell[(5 * H) + ch][(5 * W) + cw]);
                    }
                }
            }
        }
        //add blocks to board as needed
        for(int H = 0; H < height / 5; H++){
            for(int W = 0; W < width / 5; W++){
                board.add(Blocks[H][W]);
            }
        }
        
        //add button to confirm size and go to selected mode
        JButton confirm = new JButton("Confirm size");
        confirm.addActionListener((ActionEvent e) -> {
            if(mode == 0){          //go to create mode
                Create(frame);
            }else if(mode == 1){    //go to solve mode
                Solve(frame);
            }
        });
        pane.add(confirm, c);
        c.fill = GridBagConstraints.BOTH;
        
        //add sliders to select size
        JSlider h = new JSlider(SwingConstants.VERTICAL, 5, maxh, 5);
        h.setMajorTickSpacing(5);
        h.setSnapToTicks(true);
        h.setInverted(true);
        h.setPaintLabels(true);
        h.addChangeListener((ChangeEvent e) -> {
            if(!((JSlider) e.getSource()).getValueIsAdjusting()){
                //resize
                height = h.getValue();
                //and repaint board
                board.removeAll();
                board.repaint();
                BLay.setRows(height / 5);
                BLay.setColumns(width / 5);
                board.setLayout(BLay);
                for (int H = 0; H < height / 5; H++){
                    for (int W = 0; W < width / 5; W++){
                        board.add(Blocks[H][W]);
                    }
                }
                board.validate();
                board.repaint();
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        pane.add(h, c);
        JSlider w = new JSlider(SwingConstants.HORIZONTAL, 5, maxw, 5);
        w.setMajorTickSpacing(5);
        w.setSnapToTicks(true);
        w.setPaintLabels(true);
        w.addChangeListener((ChangeEvent e) -> {
            if(!((JSlider) e.getSource()).getValueIsAdjusting()){
                //resize
                width = w.getValue();
                //and repaint board
                board.removeAll();
                board.repaint();
                BLay.setRows(height / 5);
                BLay.setColumns(width / 5);
                board.setLayout(BLay);
                for (int H = 0; H < height / 5; H++){
                    for (int W = 0; W < width / 5; W++){
                        board.add(Blocks[H][W]);
                    }
                }
                board.validate();
                board.repaint();
            }
        });
        c.gridx = 1;
        c.gridy = 0;
        pane.add(w, c);
        
        //add board to frame
        board.setPreferredSize(new Dimension(500, 500));
        board.setMinimumSize(new Dimension(150, 150));
        board.setBackground(Color.red);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridy = 1;
        c.gridx = 1;
        pane.add(board, c);
        frame.pack();
    }
    
    private void Create(JFrame frame){
        //begin allowing user to create pixel image and tracking key
        for (int H = 0; H < height; H++){
            for (int W = 0; W < width; W++){
                cell[H][W].setupButtonCM();
            }
        }
        
        //set up UI
        JPanel pane = (JPanel) frame.getContentPane();
        GridBagConstraints c = new GridBagConstraints();
        JButton confirm = (JButton) pane.getComponent(0);
        confirm.removeActionListener(confirm.getActionListeners()[0]);
        confirm.setText("Save");
        confirm.addActionListener((ActionEvent e) -> {//<editor-fold desc="save">
            //---------------------------------------TO DO ------------------------------------------
            //save to file at some point
        /*</editor-fold>*/});
        JPanel board = (JPanel) pane.getComponent(3);
        //removing sliders
        while(pane.getComponent(1).getComponentListeners().length != 0){
            pane.getComponent(1).removeComponentListener(pane.getComponent(1).getComponentListeners()[0]);
        }
        pane.remove(1);
        while(pane.getComponent(1).getComponentListeners().length != 0){
            pane.getComponent(1).removeComponentListener(pane.getComponent(1).getComponentListeners()[0]);
        }
        //add labels for key on top and left side
        pane.remove(1);
        JPanel horlabels = new JPanel();
        horlabels.setBackground(Color.GREEN);
        hcols = (int) Math.ceil(width/2.0);
        horlabels.setLayout(new GridLayout(height, hcols));
        hlabels = new ArrayList<>();
        for(int i = 0; i < height*hcols; i++){
            hlabels.add(new JLabel(/*Integer.toString(i)*/));
            hlabels.get(i).setHorizontalAlignment(SwingConstants.RIGHT);
            hlabels.get(i).setBorder(BorderFactory.createLineBorder(Color.GRAY));
            horlabels.add(hlabels.get(i));
        }
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.insets = new Insets(5, 0, 5, 0);
        pane.add(horlabels, c);
        JPanel verlabels = new JPanel();
        verlabels.setBackground(Color.GREEN);
        wrows = (int) Math.ceil(height/2.0);
        verlabels.setLayout(new GridLayout(wrows, width));
        wlabels = new ArrayList<>();
        for(int i = 0; i < width*wrows; i++){
            wlabels.add(new JLabel(/*Integer.toString(i)*/));
            wlabels.get(i).setHorizontalAlignment(SwingConstants.CENTER);
            wlabels.get(i).setBorder(BorderFactory.createLineBorder(Color.GRAY));
            verlabels.add(wlabels.get(i));
        }
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 0, 5);
        pane.add(verlabels, c);
    }
    
    private void Solve(JFrame frame){
        JPanel pane = (JPanel) frame.getContentPane();
        GridBagConstraints c = (GridBagConstraints) ((GridBagLayout) pane.getLayout()).getConstraints(pane);
        JButton confirm = (JButton) pane.getComponent(0);
        confirm.removeActionListener(confirm.getActionListeners()[0]);
        confirm.setText("Solve");
        confirm.addActionListener((ActionEvent e) -> {//<editor-fold defaultstate="collapsed" desc="solve checker">
            //set text to uneditable
            for(int i = 0; i < hfields.size(); i++){
                hfields.get(i).setEditable(false);
            }
            for(int i = 0; i < wfields.size(); i++){
                wfields.get(i).setEditable(false);
            }
            //check for maximum height and width and totals
            boolean ok = true;
            boolean topc = false;
            boolean sidc = false;
            int top = 0;
            int sid = 0;
            for(int col = 0; col < width; col++){
                int count = 0;
                int tally;
                for(int i = 0; i < wrows; i++){
                    try{
                        tally = Integer.parseInt(wfields.get(col + (i*width)).getText());
                    }catch(NumberFormatException ex){
                        wfields.get(col + (i*width)).setText("0");
                        tally = 0;
                    }
                    wnums[i][col] = tally;
                    if(tally != 0 && count != 0){   //for spaces between
                        count++;
                    }
                    count += tally;
                    top += tally;
                }
                if(count > height){
                    ok = false;
                    topc = true;
                }
            }
            for(int row = 0; row < height; row++){
                int count = 0;
                int tally;
                for(int i = 0; i < hcols; i++){
                    try{
                        tally = Integer.parseInt(hfields.get((row*hcols) + i).getText());
                    }catch(NumberFormatException ex){
                        hfields.get((row*hcols) + i).setText("0");
                        tally = 0;
                    }
                    hnums[row][i] = tally;
                    if(tally != 0 && count != 0){
                        count++;
                    }
                    count += tally;
                    sid += tally;
                }
                if(count > width){
                    ok = false;
                    sidc = true;
                }
            }
            //check for if total on side == total on top
            if(sid != top){
                ok = false;
            }else{
                Goal = top;
            }
            //condense visible labels if needed
            for(int col = 0; ok && col < width; col++){
                for(int i = 0; i < wrows; i++){
                    //order to go through
                    //1 4 7 10 13
                    //2 5 8 11 14
                    //3 6 9 12 15
                    //if top row != null and row below == null
                        //shift rows down
                    if((!"0".equals(wfields.get(col + (i*width)).getText())) && (i < (wrows-1)) && ("0".equals(wfields.get(col + ((i+1) * width)).getText()))){
                        for(int x = i; x >= 0; x--){
                            String tmp = wfields.get(col + ((x+1) * width)).getText();
                            wfields.get(col + ((x+1) * width)).setText(wfields.get(col + (x*width)).getText());
                            wfields.get(col + (x*width)).setText(tmp);
                        }
                    }
                }
            }
            for(int row = 0; ok && row < height; row++){
                for(int i = 0; i < hcols; i++){
                    // 1  2  3
                    // 4  5  6
                    // 7  8  9      inner->
                    //10 11 12
                    //13 14 15
                    //if outer row != null and row in == null
                        //shift rows in
                    if((!"0".equals(hfields.get((row*hcols) + i).getText())) && (i < (hcols-1)) && ("0".equals(hfields.get((row*hcols) + i + 1).getText()))){
                        for(int x = i; x >= 0; x--){
                            String tmp = hfields.get((row*hcols) + x + 1).getText();
                            hfields.get((row*hcols) + x + 1).setText(hfields.get((row*hcols) + x).getText());
                            hfields.get((row*hcols) + x).setText(tmp);
                        }
                    }
                }
            }
            //condense logical labels if needed
            for(int col = 0; ok && col < width; col++){
                for(int i = 0; i < wrows-1; i++){
                    //order to go through
                    //1 4 7 10 13
                    //2 5 8 11 14
                    //3 6 9 12 15
                    //if top row == 0 and row below != 0
                        //shift rows up
                    if((wnums[i][col] == 0) && (wnums[i+1][col] !=0)){
                        for(int x = i; x >= 0; x--){
                            if(wnums[x][col] != 0) break;
                            int tmp = wnums[x+1][col];
                            wnums[x+1][col] = wnums[x][col];
                            wnums[x][col] = tmp;
                        }
                    }
                }
            }
            for(int row = 0; ok && row < height; row++){
                for(int i = 0; i < hcols - 1; i++){
                    // 1  2  3
                    // 4  5  6
                    // 7  8  9      inner->
                    //10 11 12
                    //13 14 15
                    //if outer row == 0 and row in != 0
                        //shift rows out
                    if((hnums[row][i] == 0) && (hnums[row][i+1] != 0)){
                        for(int x = i; x >= 0; x--){
                            if(hnums[row][x] != 0) break;
                            int tmp = hnums[row][x+1];
                            hnums[row][x+1] = hnums[row][x];
                            hnums[row][x] = tmp;
                        }
                    }
                }
            }
            //final check
            if(ok == true){
                //send to different solving algorithms
                if((System.currentTimeMillis() * 5) % 2 == 0){
                    BruteSolver();
                }else{
                    Solver();
                }
            }else{
                //display error
                System.out.print("Error Solving: ");
                //possible errors:
                //count>height || count>width
                if(topc || sidc){
                    if(topc){
                        System.out.print("a value in the top is greater than the height ");
                    }
                    if(sidc){
                        System.out.print("a value in the side is greater than the width");
                    }
                    System.out.println();
                }else{  //top!=sid
                    System.out.println("top values != side values");
                }
                //set text back to editable
                for(int i = 0; i < hfields.size(); i++){
                    hfields.get(i).setEditable(true);
                }
                for(int i = 0; i < wfields.size(); i++){
                    wfields.get(i).setEditable(true);
                }
            }
        /*</editor-fold>*/});
        JPanel board = (JPanel) pane.getComponent(3);
        //removing sliders
        while(pane.getComponent(1).getComponentListeners().length != 0){
            pane.getComponent(1).removeComponentListener(pane.getComponent(1).getComponentListeners()[0]);
        }
        pane.remove(1);
        while(pane.getComponent(1).getComponentListeners().length != 0){
            pane.getComponent(1).removeComponentListener(pane.getComponent(1).getComponentListeners()[0]);
        }
        pane.remove(1);
        //add number input fields
        JPanel horlabels = new JPanel();
        horlabels.setBackground(Color.GREEN);
        hcols = (int) Math.ceil(width/2.0);
        hnums = new int[height][hcols];
        horlabels.setLayout(new GridLayout(height, hcols));
        hfields = new ArrayList<>();
        for(int i = 0; i < height*hcols; i++){
            hfields.add(new JTextField(/*Integer.toString(i)*/));
            hfields.get(i).setHorizontalAlignment(SwingConstants.RIGHT);
            hfields.get(i).setBorder(BorderFactory.createLineBorder(Color.GRAY));
            hfields.get(i).setMinimumSize(new Dimension(20, 20));
            horlabels.add(hfields.get(i));
        }
        horlabels.setMinimumSize(horlabels.getLayout().minimumLayoutSize(horlabels));
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.insets = new Insets(5, 0, 5, 0);
        pane.add(horlabels, c);
        JPanel verlabels = new JPanel();
        verlabels.setBackground(Color.GREEN);
        wrows = (int) Math.ceil(height/2.0);
        wnums = new int[wrows][width];
        verlabels.setLayout(new GridLayout(wrows, width));
        wfields = new ArrayList<>();
        for(int i = 0; i < width*wrows; i++){
            wfields.add(new JTextField(/*Integer.toString(i)*/));
            wfields.get(i).setHorizontalAlignment(SwingConstants.CENTER);
            wfields.get(i).setBorder(BorderFactory.createLineBorder(Color.GRAY));
            wfields.get(i).setMinimumSize(new Dimension(20, 20));
            verlabels.add(wfields.get(i));
        }
        verlabels.setMinimumSize(verlabels.getLayout().minimumLayoutSize(verlabels));
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 0, 5);
        pane.add(verlabels, c);
    }
    
    private void BruteSolver(){
        System.out.println("Brute");
        //start at top left cell 
        //go down
        //check across 
        //down again
        //...
        //until CheckDone() == true
        
        //arrays to keep track of empty columns and rows
            //true is empty
        boolean[] ecol = new boolean[width];
        boolean[] erow = new boolean[height];
        //set empty columns and rows
        for(int col = 0; col < width; col++){
            int count = 0;
            int tally;
            for(int i = 0; i < wrows; i++){
                tally = wnums[i][col];
                count += tally;
            }
            if(count > 0){
                ecol[col] = false;
            }else{
                ecol[col] = true;
                for(int h = 0; h < height; h++){
                    cell[h][col].setLS(true);
                }
            }
        }
        for(int row = 0; row < height; row++){
            int count = 0;
            int tally;
            for(int i = 0; i < hcols; i++){
                tally = hnums[row][i];
                count += tally;
            }
            if(count > 0){
                erow[row] = false;
            }else{
                erow[row] = true;
                for(int w = 0; w < width; w++){
                    cell[row][w].setLS(true);
                }
            }
        }
        
        //get positions of valid starting positions
        ArrayList<int[]> start = new ArrayList<>();
        ArrayList<int[]> Nstart = new ArrayList<>();
        int Wmin = 0;
        for(int W = 0; W < width; W++){
            if(ecol[W] == true){
                if(W == Wmin){
                    Wmin++;
                }
                continue;
            }
            for(int H = 0; H < height; H++){
                if(erow[H] == true){
                    continue;
                }
                if(CheckDone() != true){
                    cell[H][W].setActive(true);
                    if(W == Wmin){
                        int[] tmp = {H, W};
                        start.add(tmp);
                    }
                }
            }
        }
        int Hmin = 0;
        for(int H = 0; H < height; H++){
            if(erow[H] == true){
                if(H == Hmin){
                    Hmin++;
                }
                break;
            }
        }
        
        //hmin keeps track of the first cell that can be active for each column
        int[] hmin = new int[width];
        for(int w = 0; w < width; w++){
            if(ecol[w]==true){      //if empty column, no cell need be checked
                hmin[w] = height;
            }else{
                hmin[w] = Hmin;
            }
        }
        
        //hmax keeps track of the bottommost cell that can be active for the rest of 
            //the cells to be active underneath with correct spaces for each column
        int[] hmax = new int[width];
        //hspaces keeps track of the wiggle room of the column (has same value of hmax initially
        int[] hspaces = new int[width];
        for(int w = 0; w < width; w++){
            int count = 0;
            for(int i = 0; i < wrows; i++){
                int tally = wnums[i][w];
                if(tally != 0 && count != 0){   //for spaces between
                    count++;
                }
                count += tally;
            }
            hmax[w] = (height - count);
            hspaces[w] = (height - count);
        }
        
        //Ideas for saving??????????????????????????????????
        System.out.println("hnums:");
        System.out.println("hcols = " + hcols);
        for(int a = 0; a < height; a++){
            for(int b = 0; b < hcols; b++){
                System.out.print(hnums[a][b]);
            }
            System.out.println();
        }
        System.out.println("wnums:");
        System.out.println("wrows = " + wrows);
        for(int a = 0; a < wrows; a++){
            for(int b = 0; b < width; b++){
                System.out.print(wnums[a][b]);
            }
            System.out.println();
        }
        System.out.println("-------------------------------");
        System.out.println("hnums:");
        for(int i = 0; i < height; i++){
            System.out.println(Arrays.toString(hnums[i]));
        }
        System.out.println("wnums:");
        for(int i = 0; i < wrows; i++){
            System.out.println(Arrays.toString(wnums[i]));
        }
        System.out.println("-------------------------------");
        
        //solve using depth first search
        isDone = CheckDone();
        ActionListener solve = new ActionListener(){
            @Override public void actionPerformed(ActionEvent e) {
                if(isDone != true){
                    System.out.println("Not a solution...reseting and continuing.");
                    //reset
                    for(int H = 0; H < height; H++){
                        for(int W = 0; W < width; W++){
                            cell[H][W].setActive(false);
                            if(erow[H]==false && ecol[W]==false){
                                cell[H][W].setLS(false);
                            }
                        }
                    }
                    for(int[] tmp: Nstart){
                        cell[tmp[0]][tmp[1]].setLS(true);
                    }
                    //start at next possible start position
                    if(start.isEmpty()){
                        isDone = true;
                        return;
                    }
                    int startH = start.get(0)[0];
                    int startW = start.get(0)[1];
                    start.remove(0);
                    cell[startH][startW].setActive(true);
                    
                    //hmin keeps track of the first cell that can be active for each column
                    int[] hmin = new int[width];
                    for(int w = 0; w < width; w++){
                        if(ecol[w]==true){  //if empty column, no cell need be checked
                            hmin[w] = height;
                        }else if(w == startW){  //if start column, start position is minimum
                            hmin[w] = startH;   
                        }else{  //else zero is default minimum
                            hmin[w] = 0;
                        }
                    }
                    
                    boolean tst;
                    boolean hasFailed = false;
                    for(int w = 0; w < width; w++){
                        if(hasFailed)break;
                        int a = 0;
                        int cnt = 0;
                        //solve column
                        for(int h = hmin[w]; h < height; h++){
                            if(a == wrows)break;    //should never get here
                            if((wnums[a][w] - cnt) > 0){
                                tst = cell[h][w].setActive(true);
                                if(tst){
                                    cnt++;
                                }else if(cnt == 0){
                                    if(true);//if(true); is for debugging
                                    //cell is a good LS and doesn't conflict with continuity
                                    //so coninue bc nothing is wrong
                                }else{
                                    if(true);
                                    //very bad--------------------------------------------------------------add more code

                                }
                            }else if(wnums[a][w] == 0){
                                cell[h][w].setLS(true);
                            }else{
                                cell[h][w].setLS(true);
                                a++;
                                cnt = 0;
                            }
                        }
                        //check width-wise progress
                        int[] hpos = new int[height];
                        for(int H = 0; H < height; H++){
                            if(hasFailed)break;
                            cnt = 0;
                            for(int W = 0; W <= w; W++){
                                if((cell[H][W].isActive()==false) && (cnt == 0)){
                                    //possible LSs
                                    if(hnums[H][hpos[H]] == 0){
                                        for(int ww = W; ww < width; ww++){
                                            cell[H][ww].setLS(true);
                                        }
                                    }
                                }else if((cell[H][W].isActive()==false) && (cnt != 0)){
                                    if(cnt == hnums[H][hpos[H]]){
                                        cell[H][W].setLS(true);
                                        cnt = 0;
                                        hpos[H]++;
                                        if(hnums[H][hpos[H]] == 0){
                                            for(int ww = W; ww < width; ww++){
                                                cell[H][ww].setLS(true);
                                            }
                                        }
                                    }else{
                                        //fail
                                        hasFailed = true;
                                        break;
                                    }
                                }else if(cell[H][W].isActive()==true){
                                    cnt++;
                                    if(cnt > hnums[H][hpos[H]]){
                                        //fail
                                        hasFailed = true;
                                        break;
                                    }else if(cnt == hnums[H][hpos[H]]){
                                        cell[H][W+1].setLS(true);

                                    }//else all good
                                }
                            }
                        }
                    }
                    isDone = CheckDone();
                    if(hasFailed || !isDone){
                        Nstart.add(new int[]{startH,startW});
                    }
                }else{
                    time.stop();
                    //Pop up for success/failure
                    if(CheckDone()==true){
                        System.out.println("Successfully found a solution.");
                    }else{
                        System.out.println("Couldn't find a solution.");
                    }
                }
            }
            
            //need to go through in following order for DFS:
                //hpos = {startH, 0, 0, 0, 0}
                //hpos = {startH, 0, 0, 0, 1}
                //hpos = {startH, 0, 0, 0, 2}
                //hpos = {startH, 0, 0, 0, 3}
                //hpos = {startH, 0, 0, 0, 4}
                //hpos = {startH, 0, 0, 1, 0}
                //hpos = {startH, 0, 0, 1, 1}
                //hpos = {startH, 0, 0, 1, 2}
                //hpos = {startH, 0, 0, 1, 3}
                //hpos = {startH, 0, 0, 1, 4}
                //hpos = {startH, 0, 0, 2, 0}
                //hpos = {startH, 0, 0, 2, 1}
                //etc
                //hpos = {startH, hmax[1], hmax[2], 4, 3}
                //hpos = {startH, hmax[1], hmax[2], 4, 4}
                //or until found a solution
            //recusive execution of top spaces
            void cycle(int wmin, int[] hmin, int[] hmax){
                if(wmin == width-1){
                    return;
                }
                for(int ws = width-1; ws > wmin; ws--){
                    //reset LSs for ws+1
                    for(int hs = hmin[ws]; hs < hmax[ws]; hs++){
                        //need to cycle through spaces in the middle and top
                        //i.e.  x   x   x   o   x
                            //  x   o   o   x   x
                            //  x   o   x   x   x
                            //  x   x   x   o   x
                            //  x   x   x   o   x
                        
                        
                        cycle(wmin + 1, hmin, hmax);
                    }
                }
            }
        };
        
        time = new Timer(2000, solve);
        time.setRepeats(true);
        time.start();
        
        
    }
    
    private void ColFiller(){
        
    }
    
    private void Solver(){
        System.out.println("Efficient");
        //look to https://en.wikipedia.org/wiki/Nonogram for methods
    }
    
    private boolean CheckDone(){
        //check top
        int[] wpos = new int[width];
        for(int W = 0; W <= width; W++){
            int cnt = 0;
            for(int H = 0; H < height; H++){
                if((cell[H][W].isActive()==false) && (cnt == 0)){
                    //possible LS
                }else if((cell[H][W].isActive()==false) && (cnt != 0)){
                    if(cnt == wnums[wpos[W]][W]){
                        cnt = 0;
                        wpos[W]++;
                    }else{
                        //fail
                        return false;
                    }
                }else if(cell[H][W].isActive()==true){
                    cnt++;
                    if(cnt > wnums[wpos[W]][W]){
                        //fail
                        return false;
                    }//else all good
                }
            }
        }
        //check side
        int[] hpos = new int[height];
        for(int H = 0; H < height; H++){
            int cnt = 0;
            for(int W = 0; W <= width; W++){
                if((cell[H][W].isActive()==false) && (cnt == 0)){
                    //possible LS
                }else if((cell[H][W].isActive()==false) && (cnt != 0)){
                    if(cnt == hnums[H][hpos[H]]){
                        cnt = 0;
                        hpos[H]++;
                    }else{
                        //fail
                        return false;
                    }
                }else if(cell[H][W].isActive()==true){
                    cnt++;
                    if(cnt > hnums[H][hpos[H]]){
                        //fail
                        return false;
                    }//else all good
                }
            }
        }
        if(numActive == Goal){
            return true;
        }else{
            return false;
        }
    }
    
    private void Play(JFrame frame){
        //select saved nonogram file to solve/play
        //set up board and labels from saved file
        //play
    }
    
    private class Cell extends JButton{
        
//        private int Hpos;                     //height position
//        private int Wpos;                     //width position
        private boolean active = false;         //true if clicked/to be dark
        private boolean logicalSpace = false;   //true if forced to be a space
        private Color unactiveC = Color.white;
        private Color activeC = Color.darkGray;
        
        private Cell() {
            
        }
        
        private void setUp(int h, int w) {
            setBackground(unactiveC);
            setMinimumSize(new Dimension(20, 20));
            setPreferredSize(new Dimension(50, 50));
            setBorder(null);
//            Hpos = h;
//            Wpos = w;
        }

        //set up button for create mode
        private void setupButtonCM() {
            addActionListener((ActionEvent e) -> {
                if((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK){
                    //Ctrl pressed so toggle Logical Space
                    if(isLS()){
                        setLS(false);
                    }else{
                        setLS(true);
                    }
                }else{
                    if(isActive()){
                        setActive(false);
                    }else{
                        setActive(true);
                    }
                    //update labels for columns
                        //could be more efficient if Hpos and Wpos are implemented and used
                    for(int col = 0; col < width; col++){
                        int[] count = new int[wrows + 1];
                        int numcounts = 0;
                        for(int row = 0; row < height; row++){
                            if(cell[row][col].isActive()){
                                numcounts++;
                                count[numcounts] = 1;
                                while((row + 1 < height) && (cell[row + 1][col].isActive())){
                                    count[numcounts]++;
                                    row++;
                                }
                            }
                        }
                        for(int i = wrows - 1; i >= 0; i--){
                            if(count[numcounts] == 0){
                                wlabels.get(col + (i*width)).setText(null);
                            }else{
                                wlabels.get(col + (i*width)).setText(Integer.toString(count[numcounts]));
                                numcounts--;
                            }
                        }
                    }
                    //update labels for rows
                    for(int row = 0; row < height; row++){
                        int[] count = new int[hcols + 1];
                        int numcounts = 0;
                        for(int col = 0; col < width; col++){
                            if(cell[row][col].isActive()){
                                numcounts++;
                                count[numcounts] = 1;
                                while((col + 1 < width) && (cell[row][col + 1].isActive())){
                                    count[numcounts]++;
                                    col++;
                                }
                            }
                        }
                        for(int i = hcols - 1; i >= 0; i--){
                            if(count[numcounts] == 0){
                                hlabels.get((row*hcols) + i).setText(null);
                            }else{
                                hlabels.get((row*hcols) + i).setText(Integer.toString(count[numcounts]));
                                numcounts--;
                            }
                        }
                    }
                }
            });
        }
        
        //set up button for solve mode
//        private void setupButtenSM(){
//            // use setActive(true/false);
//        }
        
        //set up button for play mode
        private void setupButtonPM(){
            addActionListener((ActionEvent e) -> {
                if((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK){
                    //Ctrl pressed so toggle Logical Space
                    if(isLS()){
                        setLS(false);
                    }else{
                        setLS(true);
                    }
                }else{
                    //Normal click so toggle Active
                    if(isActive()){
                        setActive(false);
                    }else{
                        setActive(true);
                    }
                    //check if solved
                    if(CheckDone()){
                        //finish
                    }
                }
            });
        }

        //returns true if cell is active
        private boolean isActive() {
            return active;
        }
        
        //sets cell's active state to boolean value of b
        private boolean setActive(boolean b){
            if(b == active){
                return true;
            }
            if(isLS()==false){
                if(b==true){
                    setBackground(activeC);
                    active = b;
                    numActive++;
                    return true;
                }else{
                    setBackground(unactiveC);
                    active = b;
                    numActive--;
                    return true;
                }
            }else{
                return false;
            }
        }
        
        //returns true if cell is a logical space
        private boolean isLS(){
            return logicalSpace;
        }
        
        //sets if cell is a logical space to boolean value of b
        private boolean setLS(boolean b){
            if(b == logicalSpace){
                return true;
            }
            if(isActive()==false){
                if(b == true){
                    //draw x on button
                    setIcon(new ImageIcon(EX));
                    logicalSpace = b;
                    return true;
                }else{
                    //make sure there is no x
                    setIcon(null);
                    logicalSpace = b;
                    return true;
                }
            }else{
                return false;
            }
        }
        
        //sets active color
        private void setAC(Color c){
            activeC = c;
        }
        
        //sets unactive color
        private void setUAC(Color c){
            unactiveC = c;
        }
    }
}
