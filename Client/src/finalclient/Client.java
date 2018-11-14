package finalclient;

import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class Client {

    public static JFrame f;
    JButton[][] bt;
    static boolean flat = false;
    boolean winner;

    JTextArea content;
    JTextField nhap, enterchat;
    JButton send;
    //Timer thoigian;
    Integer second, minute;
    JLabel demthoigian;
    TextField textField;
    JPanel p;
    String temp = "";
    String strNhan = "";
    int xx, yy, x, y;
    int[][] matran;
    int[][] matrandanh;

    // Server Socket
    ServerSocket serversocket;
    Socket socket;
    OutputStream os;// ....
    InputStream is;// ......
    ObjectOutputStream oos;// .........
    ObjectInputStream ois;// 

    //MenuBar
    MenuBar menubar;
    int checkWin(){
        int countRed=0,countBlack=0,countHint=0;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (bt[i][j].getBackground()==Color.RED) countRed++;
                if (bt[i][j].getBackground()==Color.BLACK) countBlack++;
                if (bt[i][j].getBackground()==Color.YELLOW) countHint++;
            }
        }
        if (countRed+countBlack==64){
            if (countRed>countBlack){
                return 1;
            }
            if (countRed<countBlack){
                return 2;
            }
            return 3;
        }
        if (countHint==0){
            return 4;
        }
        return 0;
    }
    public Client() {
        f = new JFrame();
        f.setTitle("Game Caro Client");
        f.setSize(750, 500);
        x = 8;
        y = 8;
        f.getContentPane().setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f.setVisible(true);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        matran = new int[x][y];
        matrandanh = new int[x][y];
        menubar = new MenuBar();
        p = new JPanel();
        p.setBounds(10, 30, 400, 400);
        p.setLayout(new GridLayout(x, y));
        f.add(p);

        f.setMenuBar(menubar);// tao menubar cho frame
        Menu game = new Menu("Game");
        menubar.add(game);
        Menu help = new Menu("Help");
        menubar.add(help);
        MenuItem helpItem = new MenuItem("Help");
        help.add(helpItem);
        MenuItem about = new MenuItem("About ..");
        help.add(about);
        help.addSeparator();
        MenuItem newItem = new MenuItem("New Game");
        game.add(newItem);
        MenuItem exit = new MenuItem("Exit");
        game.add(exit);
        game.addSeparator();
        newItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                newgame();
                try {
                    oos.writeObject("newgame,123");
                } catch (IOException ie) {
                    //
                }
            }

        });
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Object[] options = {"OK"};
                JOptionPane.showConfirmDialog(f,
                        "Đào Văn Đô", "Information",
                        JOptionPane.CLOSED_OPTION);
            }
        });
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Object[] options = {"OK"};
                JOptionPane.showConfirmDialog(f,
                        "Google không tính phí", "Luật Chơi",
                        JOptionPane.CLOSED_OPTION);
            }
        });
        //khung chat
        Font fo = new Font("Arial", Font.BOLD, 15);
        content = new JTextArea();
        content.setFont(fo);
        content.setBackground(Color.white);

        content.setEditable(false);
        JScrollPane sp = new JScrollPane(content);
        sp.setBounds(430, 170, 300, 180);
        send = new JButton("Gui");
        send.setBounds(640, 390, 70, 40);
        nhap = new JTextField(30);
        nhap.setFont(fo);
        enterchat = new JTextField("");
        enterchat.setFont(fo);
        enterchat.setBounds(430, 400, 200, 30);
        enterchat.setBackground(Color.white);
        f.add(enterchat);
        f.add(send);
        f.add(sp);
        f.setVisible(true);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(send)) {
                    try {

                        temp += "Tôi: " + enterchat.getText() + "\n";
                        content.setText(temp);
                        oos.writeObject("chat," + enterchat.getText());
                        enterchat.setText("");
                        //temp = "";
                        enterchat.requestFocus();
                        content.setVisible(false);
                        content.setVisible(true);

                    } catch (Exception r) {
                        r.printStackTrace();
                    }
                }
            }
        });

        demthoigian = new JLabel("Thời Gian:");
        demthoigian.setFont(new Font("TimesRoman", Font.ITALIC, 16));
        demthoigian.setForeground(Color.BLACK);
        f.add(demthoigian);
        demthoigian.setBounds(430, 120, 300, 50);
        second = 0;
        minute = 0;
//        thoigian = new Timer(1000, new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String temp = minute.toString();
//                String temp1 = second.toString();
//                if (temp.length() == 1) {
//                    temp = "0" + temp;
//                }
//                if (temp1.length() == 1) {
//                    temp1 = "0" + temp1;
//                }
//
//                if (second == 10) {
//                    try {
//                        oos.writeObject("checkwin,123");
//                    } catch (IOException ex) {
//                    }
//                    Object[] options = {"Dong y", "Huy bo"};
//                    int m = JOptionPane.showConfirmDialog(f,
//                            "Ban da thua.Ban co muon choi lai khong?", "Thong bao",
//                            JOptionPane.YES_NO_OPTION);
//                    if (m == JOptionPane.YES_OPTION) {
//                        second = 0;
//                        minute = 0;
//                        setVisiblePanel(p);
//                        newgame();
//                        try {
//                            oos.writeObject("newgame,123");
//                        } catch (IOException ie) {
//                            //
//                        }
//                    } else if (m == JOptionPane.NO_OPTION) {
//                        thoigian.stop();
//                    }
//                } else {
//                    demthoigian.setText("Thời Gian:" + temp + ":" + temp1);
//                    second++;
//                }
//
//            }
//
//        });

        bt = new JButton[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                final int a = i, b = j;
                bt[a][b] = new JButton();
                bt[a][b].setBackground(Color.LIGHT_GRAY);
                bt[a][b].addActionListener(new ActionListener() {
                    public boolean InBoard(int xPos,int yPos){
                        return (0<=xPos && xPos<x && 0<=yPos && yPos<y);
                    }
                    public boolean validMove(int xPos,int yPos,int xDirection,int yDirection){
                        try{
                            if (!InBoard(xPos, yPos)) return false;
                            if (bt[xPos][yPos].getBackground()==Color.RED){
                            while (true){
                                if (InBoard(xPos+xDirection, yPos+yDirection)){
                                    xPos+=xDirection;
                                    yPos+=yDirection;
                                    if (InBoard(xPos,yPos)&& bt[xPos][yPos].getBackground()==Color.BLACK){
                                        return true;
                                    }
                                    if (InBoard(xPos,yPos)&& bt[xPos][yPos].getBackground()!=Color.RED){
                                        break;
                                    }
                                    } else{
                                        break;
                                    }
                                }
                            }
                            return false;
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        return false;
                    }
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flat = true;// server da click
                        //thoigian.start();

                        second = 0;
                        minute = 0;

                        
                        boolean check=false;
                        //bt[a][b].setIcon(new ImageIcon(getClass().getResource("o.png")));
                        if(bt[a][b].getBackground()!= Color.RED && bt[a][b].getBackground()!= Color.BLACK){
                            int dx[]={-1,-1,-1,0,0,1,1,1};
                            int dy[]={-1,0,1,-1,1,-1,0,1};
                            for (int direction=0;direction<8;direction++){
                                if (InBoard(a+dx[direction], b+dy[direction]) 
                                        && (validMove(a+dx[direction], b+dy[direction],dx[direction],dy[direction]))){
                                    int xPos=a,yPos=b,xDirection=dx[direction],yDirection=dy[direction];
                                    while (true){
                                        bt[xPos][yPos].setBackground(Color.BLACK);
                                        matrandanh[xPos][yPos] = 1;
                                        bt[xPos][xPos].setEnabled(false);
                                        xPos+=xDirection;
                                        yPos+=yDirection;
                                        if (bt[xPos][yPos].getBackground()==Color.BLACK){
                                            check=true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (check){
                                matrandanh[a][b] = 1;
                                bt[a][b].setEnabled(false);
                                bt[a][b].setBackground(Color.BLACK);
                                
                                for (int i=0;i<8;i++){
                                    for (int j=0;j<8;j++){

                                    if (bt[i][j].getBackground()==Color.YELLOW){
                                            bt[i][j].setBackground(Color.LIGHT_GRAY);
                                        }
                                    }
                                }
                            }
                        }
                        if (check){
                            try {
                                oos.writeObject("caro," + a + "," + b);
                                setEnableButton(false);
                                int stt=checkWin();
                                JOptionPane.showMessageDialog(f, stt);
                            } catch (Exception ie) {
                                ie.printStackTrace();
                            }
                            //thoigian.stop();
                        }
                    }

                });
                p.add(bt[a][b]);
                p.setVisible(false);
                p.setVisible(true);
            }
        }
        bt[3][3].setBackground(Color.RED);
        bt[3][4].setBackground(Color.BLACK);
        bt[4][3].setBackground(Color.BLACK);
        bt[4][4].setBackground(Color.RED);
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                int dx[]={-1,-1,-1,0,0,1,1,1};
                int dy[]={-1,0,1,-1,1,-1,0,1};
                for (int direction=0;direction<8;direction++){
                    if (InBoard(i+dx[direction], j+dy[direction]) && bt[i][j].getBackground()==Color.LIGHT_GRAY
                            && (validMove1(i+dx[direction], j+dy[direction],dx[direction],dy[direction]))){
                        bt[i][j].setBackground(Color.YELLOW);
                    }
                }
            }
        }
        
        try {
            socket = new Socket("192.168.0.104", 1234);
            System.out.println("Da ket noi toi server!");
            os = socket.getOutputStream();
            is = socket.getInputStream();
            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            while (true) {
                String stream = ois.readObject().toString();
                String[] data = stream.split(",");
                if (data[0].equals("chat")) {
                    temp += "Khách:" + data[1] + '\n';
                    content.setText(temp);
                } else if (data[0].equals("caro")) {
                    //thoigian.start();
                    second = 0;
                    minute = 0;
                    caro(data[1], data[2]);
                    setEnableButton(true);
                    if (winner == false) {
                        setEnableButton(true);
                    }
                } else if (data[0].equals("newgame")) {
                    newgame();
                    second = 0;
                    minute = 0;
                } else if (data[0].equals("checkwin")) {
                    ///thoigian.stop();
                }
            }
        } catch (Exception ie) {

        }
        textField = new TextField();

    }

    public boolean validMove1(int xPos,int yPos,int xDirection,int yDirection){
        if (!InBoard(xPos, yPos)) return false;
        if (bt[xPos][yPos].getBackground()==Color.RED){
            while (true){
                if (InBoard(xPos+xDirection, yPos+yDirection)){
                    xPos+=xDirection;
                    yPos+=yDirection;
                    if (bt[xPos][yPos].getBackground()==Color.BLACK){
                        return true;
                    }
                    if (InBoard(xPos,yPos)&& bt[xPos][yPos].getBackground()!=Color.RED){
                                        break;
                                    }
                } else{
                    break;
                }
            }
        }
        return false;
    }
    public void newgame() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                bt[i][j].setBackground(Color.LIGHT_GRAY);
                matran[i][j] = 0;
                matrandanh[i][j] = 0;
            }
        }
        setEnableButton(true);
        second = 0;
        minute = 0;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                for (int direction=0;direction<8;direction++){
                    int dx[]={-1,-1,-1,0,0,1,1,1};
                    int dy[]={-1,0,1,-1,1,-1,0,1};
                    if (InBoard(i+dx[direction], j+dy[direction]) && bt[i][j].getBackground()==Color.LIGHT_GRAY 
                            && (validMove1(i+dx[direction], j+dy[direction],dx[direction],dy[direction]))){
                        bt[i][j].setBackground(Color.YELLOW);
                    }
                }
            }
        }
        //thoigian.stop();
    }

    public void setVisiblePanel(JPanel pHienthi) {
        f.add(pHienthi);
        pHienthi.setVisible(true);
        pHienthi.updateUI();// ......

    }

    public void setEnableButton(boolean b) {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (matrandanh[i][j] == 0) {
                    bt[i][j].setEnabled(b);
                }
            }
        }
    }
    public boolean InBoard(int xPos,int yPos){
        return (0<=xPos && xPos<x && 0<=yPos && yPos<y);
    }
    public boolean validMove(int xPos,int yPos,int xDirection,int yDirection){
        if (!InBoard(xPos, yPos)) return false;
        if (bt[xPos][yPos].getBackground()==Color.BLACK){
            while (true){
                if (InBoard(xPos+xDirection, yPos+yDirection)){
                    xPos+=xDirection;
                    yPos+=yDirection;
                    if (bt[xPos][yPos].getBackground()==Color.RED){
                        return true;
                    }
                    if (InBoard(xPos,yPos)&& bt[xPos][yPos].getBackground()!=Color.BLACK){
                        break;
                    }
                } else{
                    break;
                }
            }
        }
        return false;
    }
    public void caro(String x, String y) {
        xx = Integer.parseInt(x);
        yy = Integer.parseInt(y);
        // danh dau vi tri danh
        int dx[]={-1,-1,-1,0,0,1,1,1};
        int dy[]={-1,0,1,-1,1,-1,0,1};
        for (int direction=0;direction<8;direction++){
            if (InBoard(xx+dx[direction], yy+dy[direction]) 
                    && (validMove(xx+dx[direction], yy+dy[direction],dx[direction],dy[direction]))){
                
                int xPos=xx,yPos=yy,xDirection=dx[direction],yDirection=dy[direction];
                while (true){
                    matran[xPos][yPos] = 1;
                    matrandanh[xPos][yPos] = 1;
                    bt[xPos][yPos].setEnabled(false);
                    bt[xPos][yPos].setBackground(Color.RED);
                    xPos+=xDirection;
                    yPos+=yDirection;
                    if (!InBoard(xPos, yPos)) break;
                    if (bt[xPos][yPos].getBackground()==Color.RED){
                        break;
                    }
                }
            }
        }

        matran[xx][yy] = 1;
        matrandanh[xx][yy] = 1;
        bt[xx][yy].setEnabled(false);
        //bt[xx][yy].setIcon(new ImageIcon("x.png"));
        bt[xx][yy].setBackground(Color.RED);
        
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                for (int direction=0;direction<8;direction++){
                    if (InBoard(i+dx[direction], j+dy[direction]) && bt[i][j].getBackground()==Color.LIGHT_GRAY 
                            && (validMove1(i+dx[direction], j+dy[direction],dx[direction],dy[direction]))){
                        bt[i][j].setBackground(Color.YELLOW);
                    }
                }
            }
        }
        int stt=checkWin();
        JOptionPane.showMessageDialog(f, stt);
    }

    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Enter Input:");
        JOptionPane.showMessageDialog(null, input);
        new Client();
    }

}
