package Client;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class ChatSwingLayout implements ActionListener{
    private JavaClient client;
    private SplashScreen splash;
    private JFrame mainFrame;
    JTextField sendArea;
    JTextPane  msgArea = new JTextPane();
    JTextPane roomParticipants = new JTextPane();
    private String userName;



    public ChatSwingLayout(String ip){
        splash = new SplashScreen();


        prepareGUI();
        splash.prepareGUI(mainFrame);
        userName = splash.getUserName();
        client = new JavaClient(ip, 50000, true, msgArea, userName, roomParticipants);

    }
    public static void main(String[] args)throws BadLocationException{
        String ip = args[0];
        if(ip == null){
            ip = "192.168.1.42";
        }
        ChatSwingLayout chatSwingLayout = new ChatSwingLayout(ip);
        chatSwingLayout.showBorderLayoutDemo();
    }




    private void prepareGUI(){

        mainFrame = new JFrame("Java SWING Examples");

        mainFrame.setLayout(new GridLayout(1, 1));



        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });





        mainFrame.setVisible(true);
        mainFrame.setSize(1024,768);
    }
    private void showBorderLayoutDemo() throws BadLocationException {

        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, true);
        JPanel panel = new JPanel();
        panel.setBackground(Color.darkGray);
        panel.setSize(1024,768);
        BorderLayout layout = new BorderLayout();
        layout.setHgap(1);
        layout.setVgap(1);

        //Message Pane

        msgArea.setPreferredSize(new Dimension(300,300));
        msgArea.setCharacterAttributes(attributeSet, true);
        /*for(int i = 0; i < 5; i++){
            msgArea.setText(msgArea.getText() + "\nMessage #: " + i);
        }*/

        StyleConstants.setItalic(attributeSet, true);
        StyleConstants.setForeground(attributeSet, Color.black);
        StyleConstants.setBackground(attributeSet, Color.white);




        roomParticipants.setPreferredSize(new Dimension(100,300));
        roomParticipants.setCharacterAttributes(attributeSet, true);



        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());


        JButton sendButton = new JButton("send");
        //sendButton.setActionCommand("send");
        sendButton.addActionListener(this);

        sendArea = new JTextField();


        sendPanel.add(sendArea, BorderLayout.CENTER);
        sendPanel.add(sendButton, BorderLayout.EAST);


        panel.setLayout(layout);
        panel.add(new JScrollPane(msgArea),BorderLayout.CENTER);
        panel.add(roomParticipants,BorderLayout.EAST);
        panel.add(sendPanel, BorderLayout.SOUTH);

        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        client.sendMessage(this.sendArea.getText());
        this.sendArea.setText("");


    }


}


