package Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class SplashScreen implements ActionListener{
    private JDialog mainFrame;
    private JTextField nameArea;

    public void prepareGUI(JFrame parent){

        mainFrame = new JDialog(parent,"Sign-in", true);

        mainFrame.setLayout(new FlowLayout());
        mainFrame.setPreferredSize(new Dimension(500,500));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });


        JButton closeBtn = new JButton("Enter");
        closeBtn.setPreferredSize(new Dimension(100,50));
        closeBtn.addActionListener(this);

        nameArea = new JTextField();
        nameArea.setPreferredSize(new Dimension(300,50));



        mainFrame.add(nameArea);
        mainFrame.add(closeBtn);

        mainFrame.pack();

        mainFrame.setVisible(true);
        mainFrame.setSize(1024,768);





    }




    public String getUserName(){
        return nameArea.getText();
    }





    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
       // mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));

        mainFrame.dispose();

    }



}
