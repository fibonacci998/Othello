package pkgfinal;

import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class Sever {

    public static JFrame f;
    JButton[][] bt;
    static boolean flat = false;
    boolean winner;
    JButton send;
    Timer thoigian;
    String temp = "", strNhan = "";
    Integer second, minute;
    JLabel demthoigian;
    JTextArea content;
    JTextField nhap, enterchat;
    JPanel p;
    int xx, yy, x, y;
    int[][] matran;
    int[][] matrandanh;
    //

    private int userOneCont = 0;
    private int userTwoCont = 0;

    public Cell gameCells[][];

    // Server Socket
    ServerSocket serversocket;
    Socket socket;
    OutputStream os;// ....
    InputStream is;// ......
    ObjectOutputStream oos;// .........
    ObjectInputStream ois;// 

    //MenuBar
    MenuBar menubar;

    public Sever() {
        f = new JFrame();
        f.setTitle("Game Caro Sever");
        f.setSize(750, 500);
        x = 8;
        y = 8;

        //bt[3][3].setBackground(Color.LIGHT_RED);
        f.getContentPane().setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setResizable(false);

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
        demthoigian = new JLabel("Thời Gian:");
        demthoigian.setFont(new Font("TimesRoman", Font.ITALIC, 16));
        demthoigian.setForeground(Color.BLACK);
        f.add(demthoigian);
        demthoigian.setBounds(430, 120, 300, 50);
        second = 0;
        minute = 0;
        thoigian = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = minute.toString();
                String temp1 = second.toString();
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                if (temp1.length() == 1) {
                    temp1 = "0" + temp1;
                }

                if (second == 10) {
                    try {
                        oos.writeObject("checkwin,123");
                    } catch (IOException ex) {
                    }
                    Object[] options = {"Dong y", "Huy bo"};
                    int m = JOptionPane.showConfirmDialog(f,
                            "Ban da thua.Ban co muon choi lai khong?", "Thong bao",
                            JOptionPane.YES_NO_OPTION);
                    if (m == JOptionPane.YES_OPTION) {
                        second = 0;
                        minute = 0;
                        setVisiblePanel(p);
                        newgame();
                        try {
                            oos.writeObject("newgame,123");
                        } catch (IOException ie) {
                            //
                        }
                    } else if (m == JOptionPane.NO_OPTION) {
                        thoigian.stop();
                    }
                } else {
                    demthoigian.setText("Thời Gian:" + temp + ":" + temp1);
                    second++;
                }

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
                        
                        enterchat.requestFocus();
                        content.setVisible(false);
                        content.setVisible(true);

                    } catch (Exception r) {
                        r.printStackTrace();
                    }
                }
            }
        });

        //button caro
        int mid;
        mid = x / 2;
        bt = new JButton[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                final int a = i, b = j;
                bt[a][b] = new JButton();
                bt[a][b].setBackground(Color.LIGHT_GRAY);
                bt[a][b].addActionListener(new ActionListener() {
                    public boolean InBoard(int xPos,int yPos){
                        return (0<=xPos && xPos<=x && 0<=yPos && yPos<=y);
                    }
                    public boolean validMove(int xPos,int yPos,int xDirection,int yDirection){
                        if (bt[xPos][yPos].getBackground()==Color.BLACK){
                            while (true){
                                if (InBoard(xPos+xDirection, yPos+yDirection)){
                                    xPos+=xDirection;
                                    yPos+=yDirection;
                                    if (bt[xPos][yPos].getBackground()==Color.RED){
                                        return true;
                                    }
                                } else{
                                    break;
                                }
                            }
                        }
                        return false;
                    }
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        flat = true;// server da click
                        thoigian.start();
                        second = 0;
                        minute = 0;
                        matrandanh[a][b] = 1;
                        bt[a][b].setEnabled(false);
                        //bt[a][b].setIcon(new ImageIcon(getClass().getResource("dark.png")));
                        if(bt[a][b].getBackground()!= Color.RED && bt[a][b].getBackground()!= Color.BLACK){
                            int dx[]={-1,-1,-1,0,0,1,1,1};
                            int dy[]={-1,0,1,-1,1,-1,0,1};
                            for (int direction=0;direction<8;direction++){
                                if (InBoard(a+dx[direction], b+dy[direction]) 
                                        && (validMove(a+dx[direction], b+dy[direction],dx[direction],dy[direction]))){
                                    int xPos=a,yPos=b,xDirection=dx[direction],yDirection=dy[direction];
                                    while (true){
                                        bt[xPos][yPos].setBackground(Color.red);
                                        xPos+=xDirection;
                                        yPos+=yDirection;
                                        if (bt[xPos][yPos].getBackground()==Color.RED){
                                            break;
                                        }
                                    }
                                }
                            }
                            bt[a][b].setBackground(Color.RED);
                        }
                        
                        try {
                            oos.writeObject("caro," + a + "," + b+",");
                            setEnableButton(false);
                        } catch (Exception ie) {
                            ie.printStackTrace();
                        }
                        thoigian.stop();
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
        try {
            serversocket = new ServerSocket(1234);
            System.out.println("Dang doi client...");
            socket = serversocket.accept();
            System.out.println("Client da ket noi!");
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
                    thoigian.start();
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
                    thoigian.stop();

                }
            }
        } catch (Exception ie) {

        }

    }
    
    public void newgame() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                bt[i][j].setBackground(Color.LIGHT_GRAY);
                matran[i][j] = 0;
                matrandanh[i][j] = 0;
            }
        }
        bt[3][3].setBackground(Color.RED);
        bt[3][4].setBackground(Color.BLACK);
        bt[4][3].setBackground(Color.BLACK);
        bt[4][4].setBackground(Color.RED);
        setEnableButton(true);
        second = 0;
        minute = 0;
        thoigian.stop();
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

    public void caro(String x, String y) {
        xx = Integer.parseInt(x);
        yy = Integer.parseInt(y);
        // danh dau vi tri danh
        matran[xx][yy] = 1;
        matrandanh[xx][yy] = 1;
        bt[xx][yy].setEnabled(false);
        //bt[xx][yy].setIcon(new ImageIcon("x.png"));
        bt[xx][yy].setBackground(Color.BLACK);

    }

    private void printTable() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(gameCells[i][j].getCh());
            }
            System.out.println("");
        }

    }

    public static void main(String[] args) {
        new Sever();
    }

}
