/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stream_list;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Class to support the adding of streams into the list
 * @author Nathan
 */
public class AddStreams extends StreamList {
    
    private JTextField nameStreamer = new JTextField("Name of Streamer", 25);
    //JTextField urlStream = new JTextField("URL of Streamer" , 25);
    
    public AddStreams() {
        
        System.out.println("new newStreams()");
        //setLayout(new GridBagLayout());
        
        //get rid of options at top
        setJMenuBar(null);
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        setLayout(gridbag);
        
        //label & textbox
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        //JTextField nameStreamer = new JTextField("Name of Streamer", 25);
        gridbag.setConstraints(nameStreamer, c);
        add(nameStreamer);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        //JTextField urlStream = new JTextField("URL of Streamer" , 25);
        //gridbag.setConstraints(urlStream, c);
        //add(urlStream);
        
        setSize(300,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //Buttons
        JButton add = new JButton("Add");
        JButton cancel = new JButton("Cancel");
        
        //adding to the frame
        add(add);
        add(cancel);
        
        add.addActionListener(this);
        cancel.addActionListener(this);
        //undecroated
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        // Set the window to 55% opaque (45% translucent).
        setOpacity(0.85f);
        
        // Display the window.
        setVisible(true);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //button listener to add streams to txt file
        if(e.getActionCommand().equals("Add")) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter("stream_archive.txt", true));
                System.out.println(nameStreamer.getText());
                writer.write(nameStreamer.getText());
                
                //add new streamer to data structure instead of reading in all streamers when program starts every time
                Parser.addNewStreamer(nameStreamer.getText());
                
                //add new streamer's internal frame to streamList
                String URL = "http://www.leagueoflegendsstreams.com/home/showstream?a=" + nameStreamer.getText();
                InternalStreamerFrames streamerFrame = new InternalStreamerFrames(nameStreamer.getText(), false, URL);
                StreamList.addInternalToList(streamerFrame);
                
                nameStreamer.setText("Name of Streamer");
                writer.newLine();

                writer.flush();
                writer.close();
            } catch (IOException ex) {Logger.getLogger(AddStreams.class.getName()).log(Level.SEVERE, null, ex);}
        }
        else if(e.getActionCommand().equals("Cancel")) {
            dispose();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
