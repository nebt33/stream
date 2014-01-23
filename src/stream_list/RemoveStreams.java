/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stream_list;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 *
 * @author Nathan
 */
public class RemoveStreams extends StreamList implements ActionListener {

    JComboBox removeMenu;
    static BufferedReader reader = null;
    static private ArrayList<String> archiveList = new ArrayList<>();

    public RemoveStreams() throws FileNotFoundException, IOException {
        System.out.println("new Remove Stream: " + Parser.getArchiveList());
        this.removeMenu = new JComboBox(Parser.getArchiveList().toArray());
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        //get rid of options at top
        setJMenuBar(null);
        
        setLayout(gridbag);
        
        //label & textbox
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        //JTextField nameStreamer = new JTextField("Name of Streamer", 25);
        removeMenu.addActionListener(this);
        //gridbag.setConstraints(nameStreamer, c);
        //add(nameStreamer);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        //JTextField urlStream = new JTextField("URL of Streamer" , 25);
        //gridbag.setConstraints(urlStream, c);
        //add(urlStream);
        
        setSize(300,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //Buttons
        JButton remove = new JButton("Remove");
        JButton cancel = new JButton("Cancel");
        
        //adding to the frame
        add(removeMenu);
        add(remove);
        add(cancel);
        
        remove.addActionListener(this);
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
        //drop down menu listener to remove streams to txt file
        if(e.getActionCommand().equals("Remove")) {
         
            try {
                reader = new BufferedReader(new FileReader("stream_archive.txt"));
                removeStreamerFromFile(removeMenu.getSelectedItem().toString());
                
                Parser.removeOldStreamer(removeMenu.getSelectedItem().toString());
                
                StreamList.removeInternalFromList(removeMenu.getSelectedItem().toString());
                
                //StreamList.updateGUI();

                dispose();
            } catch (IOException ex) {Logger.getLogger(AddStreams.class.getName()).log(Level.SEVERE, null, ex);} /*catch (InterruptedException ex) {
                Logger.getLogger(RemoveStreams.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
        else if(e.getActionCommand().equals("Cancel")) {
            dispose();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Read in the whole file of streamers saved by user.
     * @throws IOException 
     */
    private static void removeStreamerFromFile(String streamer) throws IOException {
        String line;
        while((line = reader.readLine()) != null) {
            if(streamer.equalsIgnoreCase(line)) {
                //do nothing
            }
            else
              archiveList.add(line);
        }
        reWriteFile();
    }
    
    private static void reWriteFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("stream_archive.txt"));
        for(String name : archiveList) 
            writer.write(name + "\n");
        writer.flush();
        writer.close();
    }
}
