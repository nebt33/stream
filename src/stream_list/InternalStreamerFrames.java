/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stream_list;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 * Internal Frames in the jFrame(StreamList).  These internal frames will contain
 * the streamer's information.
 * @author Nathan
 */
public class InternalStreamerFrames extends JInternalFrame implements MouseListener {
    
    private String streamerName;
    private boolean online;
    private String URLOfStream;
    
    public InternalStreamerFrames(String streamerName, boolean online, String URLOfStream) {
        
        //inti
        this.streamerName = streamerName;
        this.online = online;
        this.URLOfStream = URLOfStream;
        
        //remove titlebar and set size
        InternalFrameUI internal = getUI();
        ((BasicInternalFrameUI)internal).setNorthPane(null);
        
        setSize(289, 70);
        setTitle(streamerName);
        
        //puts streamer's name in frame
        JLabel label = new JLabel(streamerName.toUpperCase(), SwingConstants.CENTER);
        add(label).setLocation(getWidth()/2, getHeight()/2);
        
        //sets background color
        if(online == false)
            getContentPane().setBackground(Color.RED);
        else
            getContentPane().setBackground(Color.GREEN);
        
        addMouseListener(this);
        
        //create border
        Border border;
        border = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),BorderFactory.createLoweredBevelBorder());
        setBorder(border);
        
        setVisible(true);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            Desktop.getDesktop().browse((new URI(URLOfStream)));
        }
        catch (IOException ex) {Logger.getLogger(InternalStreamerFrames.class.getName()).log(Level.SEVERE, null, ex);} 
        catch (URISyntaxException ex) {Logger.getLogger(InternalStreamerFrames.class.getName()).log(Level.SEVERE, null, ex);}
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Border border;
        border = BorderFactory.createCompoundBorder(BorderFactory.createRaisedSoftBevelBorder(),BorderFactory.createLoweredSoftBevelBorder());
        setBorder(border);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Border border;
        border = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),BorderFactory.createLoweredBevelBorder());
        setBorder(border);
    }

}
