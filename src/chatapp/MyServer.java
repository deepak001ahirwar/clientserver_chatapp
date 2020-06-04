package chatapp;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class MyServer extends Frame implements ActionListener, Runnable {
    
int port =4444;
ServerSocket serverSocket;
Socket socket;
Label statusL;
List chatsL;
Panel controlsP;
TextField messageTF;
Button sendB, exitB;
BufferedReader bufferedReader;
BufferedWriter bufferedWriter;


public MyServer(String m)
{
    super(m);
     //creating controls objects
        statusL = new Label("Status");
        chatsL = new List();
        messageTF = new TextField(20);
        controlsP = new Panel();
        sendB = new Button("Send");
        exitB = new Button("Exit"); 
        
        //adding control to pannel
        controlsP.setLayout(new FlowLayout());        
        controlsP.add(messageTF);
        controlsP.add(sendB);                
        controlsP.add(exitB);
        
        
         //adding to controls and panel to Frame
        add(statusL, BorderLayout.NORTH);
        add(chatsL, BorderLayout.CENTER);
        add(controlsP, BorderLayout.SOUTH);
        //adding listeners on Send Button and Exit Button        
        sendB.addActionListener(this);
        exitB.addActionListener(this);
        
        setSize(300, 300);//setting size of frame/window
        setLocation(0, 0);//setting location of frame/window on the screen
        setBackground(Color.YELLOW);//setting background for frame/window
        setVisible(true);//setting it to visible state                
        
        listen();
}
     public void listen() {
        try {
            serverSocket = new ServerSocket(port);            
            statusL.setText("Listening on ip:" + serverSocket.getInetAddress().getHostAddress() + " and port:" + port);
            socket = serverSocket.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("Hello");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Thread th;
            th = new Thread(this);
            th.start();
        } catch (Exception e) {
            statusL.setText(e.getMessage());
        }
    }
 public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(exitB)) {
            System.exit(0);
        } else {
            try {
                bufferedWriter.write(messageTF.getText());
                bufferedWriter.newLine();
                bufferedWriter.flush();
                chatsL.add("You: " + messageTF.getText());
                messageTF.setText("");
            } catch (IOException ioe) {
                statusL.setText(ioe.getMessage());
            }
        }
    }
 public void run() {
        try {
            socket.setSoTimeout(1000);
        } catch (Exception e) {
        }
        statusL.setText("Client Connected");
        while (true) {
            try {           
                String message = bufferedReader.readLine();
                if(message == null) {
                    serverSocket.close();
                    break;
                }
                chatsL.add("Client: " + message);
            } catch (IOException ioe) {                
                
            }
        }                
        
        listen();
    }
  public static void main(String[] ar) {
        new MyServer("Server Program");
    }



}
