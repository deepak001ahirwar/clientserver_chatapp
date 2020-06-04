package chatapp;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyClient  extends Frame implements ActionListener,Runnable{
   Socket socket;
   Panel topControlsP,bottomControlsP;
   List chatsL;
   TextField ipTF,portTF,messageTF;
   Button connectB,sendB,exitB;
   BufferedReader bufferedReader;
   BufferedWriter bufferedWriter;
   Thread th;
   
   MyClient(String str)   {
       super(str);
       //creating control object
       topControlsP=new Panel();
       bottomControlsP=new Panel();
       
       
       ipTF=new TextField(15);
       portTF=new TextField(5);
       messageTF=new TextField(20);
       
       chatsL=new List();
       
       connectB=new Button("connect");
       sendB=new Button("send");
       exitB=new Button("exit");
       
       ///adding element to the top panel
       topControlsP.add(ipTF);
       topControlsP.add(portTF);
       topControlsP.add(connectB);
       
       //adding element to the bottom panel
       bottomControlsP.add(messageTF);
       bottomControlsP.add(sendB);
       bottomControlsP.add(exitB);
       
       
       //adding elements and panel to the frame 
       add(topControlsP, BorderLayout.NORTH);
       add(chatsL, BorderLayout.CENTER);
       add(bottomControlsP, BorderLayout.SOUTH);
       
       
        setSize(300, 300);//setting size of frame/window
        setLocation(300, 0);//setting location of frame/window on the screen
        setBackground(Color.MAGENTA);//setting background for frame/window
        setVisible(true);//setting it to visible state  
        
        //adding listeners on connect , send , exit button
        connectB.addActionListener(this);
        sendB.addActionListener(this);
        exitB.addActionListener(this);
   }
  
   public void actionPerformed(ActionEvent event){
       if(event.getSource().equals(exitB)){
           System.exit(0);
          }else if(event.getSource().equals(connectB))
       {
           try{
               //create client 
               socket=new Socket(ipTF.getText(),Integer.parseInt(portTF.getText()));
               bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
               bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
               ipTF.setText("Connected");
               th=new Thread(this);
               th.start();
                            
               } catch (IOException ioe) {
               ipTF.setText(ioe.getMessage());
               }
       }else{           
           try{
               if(bufferedWriter !=null){
                   bufferedWriter.write(messageTF.getText());
                   bufferedWriter.newLine();
                   bufferedWriter.flush();
                   chatsL.add("you:"+messageTF.getText());  
           }
       } catch (IOException ioe) {
               ipTF.setText(ioe.getMessage());
       }  
   }
}
   public void run()
   {   try{
           socket.setSoTimeout(1);
               }
       catch (Exception e) {
            ipTF.setText(e.getMessage());
        }
       while(true)
       {
           try{
               String message=bufferedReader.readLine();
               if(message==null)
               {
                   break;
                   
               }
              chatsL.add("Server:"+message); 
           }
           catch (Exception e) {
                
            }   
       }
   }
   
   
   public static void main(String[] ar) {
        new MyClient("Client Program");
    }
    
}
