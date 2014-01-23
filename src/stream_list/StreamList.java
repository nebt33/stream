/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stream_list;
import java.awt.*;
import javax.swing.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class StreamList extends JFrame implements ActionListener {
    
    static private StreamList tw = new StreamList();
    static private JDesktopPane pane = new JDesktopPane();
                
    public StreamList() {
        super("Stream List");
        setLayout(new GridBagLayout());
 
        setSize(300,1000);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screenSize.getWidth() - 300, 0, 300, (int)screenSize.getHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Make frame have title bar and borders
        //getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        
        JMenuBar menubar = new JMenuBar();
        final JMenu menu = new JMenu("Options");
        JMenuItem addStream = new JMenuItem("Add Stream");
        JMenuItem removeStream = new JMenuItem("Remove Stream");
        JMenuItem exit = new JMenuItem("Exit");
        addStream.addActionListener(this);
        removeStream.addActionListener(this);
        exit.addActionListener(this);
        menubar.add(menu);
        menu.add(addStream);       
        menu.add(removeStream);
        menu.add(exit);
        
        menu.addMenuListener(new MenuListener() {
            
            @Override
            public void menuSelected(MenuEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public void menuDeselected(MenuEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.  
            }
            
            @Override
            public void menuCanceled(MenuEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
        //menubar.add(menu);
        this.setJMenuBar(menubar );
    }
 
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
       final Parser parser = new Parser();
        
        // Determine if the GraphicsDevice supports translucency.
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
 
        //If translucent windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(TRANSLUCENT)) {
            System.err.println(
                "Translucency is not supported");
                System.exit(0);
        }
         
        JFrame.setDefaultLookAndFeelDecorated(true);
 
        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //StreamList tw = new StreamList();
                //JDesktopPane pane = new JDesktopPane();
               
                tw.setUndecorated(true);
                // Set the window to 75% opaque (25% translucent).
                tw.setOpacity(0.75f);
                
                //Creates the internal frames on the stream_list
                    //Parser parser = new Parser();
                    for(int i = 0; i < parser.getArchiveList().size(); i++) {
                        String URL = "http://www.leagueoflegendsstreams.com/home/showstream?a=" + parser.getArchiveList().get(i);
                        boolean online = false;
                        for(String stream : parser.getOnlineLibrary()) {
                            if(stream.contains(parser.getArchiveList().get(i))) {
                                online = true;
                                break;
                            }
                        }
                        InternalStreamerFrames streamerFrame = new InternalStreamerFrames(parser.getArchiveList().get(i), online, URL);
                        addInternalToList(streamerFrame);
                    }

                // Display the window.
                tw.setVisible(true);
                
            }
        });
        

        int threeMinutes = 18000;
        //Parser parser = new Parser();
        synchronized (parser.getStreamersOnline()) {
            while(!parser.getSync()) 
              parser.getStreamersOnline().wait();
        while(true) {
            Thread.sleep(threeMinutes);
            try {
                Parser updateParser = new Parser();
                updateGUI(updateParser);
            } catch (FileNotFoundException ex) {Logger.getLogger(StreamList.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {Logger.getLogger(StreamList.class.getName()).log(Level.SEVERE, null, ex);}
        }
        }
        
    }
    
    public static void updateGUI(Parser parser) throws FileNotFoundException, IOException, InterruptedException {
        //Parser parser = new Parser();

        /*synchronized (parser.getStreamersOnline()) {
            while(!parser.getSync()) 
              parser.getStreamersOnline().wait();
        }*/
        for(JInternalFrame tempPane : pane.getAllFrames())
            System.out.println("TempPane: " + tempPane.getTitle());
        System.out.println(parser.getStreamersOnline());
        for(JInternalFrame tempPane : pane.getAllFrames()) {
            System.out.println(tempPane.getTitle());
            if(parser.getStreamersOnline().get(tempPane.getTitle()).booleanValue())
                tempPane.getContentPane().setBackground(Color.GREEN);
            else
                tempPane.getContentPane().setBackground(Color.RED);
        }

        //parser.clear();
        tw.setContentPane(pane);
        tw.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Add Stream")) 
            new AddStreams();
        /*else if(e.getActionCommand().equals("Remove Stream")) {
            try {
                new RemoveStreams();
            } 
            catch (FileNotFoundException ex) {Logger.getLogger(StreamList.class.getName()).log(Level.SEVERE, null, ex);}
            catch (IOException ex) {Logger.getLogger(StreamList.class.getName()).log(Level.SEVERE, null, ex);}
        }*/
        else if(e.getActionCommand().equals("Exit")) {
            dispose();
            System.exit(0);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Adds the streamers panes to the desktop and jframe.  It will also set the location of the pane inside the desktop.
     * @param streamerFrame 
     */
    public static void addInternalToList(InternalStreamerFrames streamerFrame) {
        streamerFrame.setLocation(0, pane.getAllFrames().length*(int)streamerFrame.getHeight());
        pane.add(streamerFrame);
        tw.setContentPane(pane);
        tw.repaint();
    }
    
    /**
     * Removes the streamers pane from the desktop and Jframe.  
     * @param streamer 
     */
    public static void removeInternalFromList(String streamer) {
        JInternalFrame[] allFrames = pane.getAllFrames();
        boolean afterRemove = false;
        for(JInternalFrame frame : allFrames) {
            if(frame.getTitle().equalsIgnoreCase(streamer)) {
                pane.remove(frame);
                //tw.setContentPane(pane);
                //tw.repaint();
                afterRemove = true;
                //break;
            }
            else if(afterRemove == true) {
                System.out.println(frame.getTitle());
                frame.setLocation(0, frame.getLocation().y - frame.getHeight());
            }
        }
        
        tw.setContentPane(pane);
        tw.repaint();
    }
    
    /**
     * Refresh the desktop pane so that the gui's are lined up correctly.
     */
    public static void refresh() {
        
    }
}
