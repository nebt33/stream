package stream_list;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nathan
 */
public class Parser {
    static private BufferedReader reader;
    static private ArrayList<String> onlineLibrary = new ArrayList<>();
    static private ArrayList<String> archiveList = new ArrayList<>();
    static private Map<String,Boolean> streamersOnline = new HashMap<String,Boolean>();
    static private boolean sync = false;

    public Parser() throws FileNotFoundException, IOException {
        this.reader = new BufferedReader(new FileReader("stream_archive.txt"));
        
        Document doc = Jsoup.connect("http://www.leagueoflegendsstreams.com/home/").userAgent("Mozilla").get();
        Elements links = doc.select("a[href*=http://www.leagueoflegendsstreams.com/home/showstream]");
        for (Element link : links) {
            onlineLibrary.add(link.attr("abs:href"));
            //print("%s", link.attr("abs:href"), trim(link.text(), 35));
        }
        readInSavedStreamers();
        checkStreamOnline();
    }
    
    public void clear() throws IOException {
        reader.close();
        onlineLibrary.clear();
        archiveList.clear();
        streamersOnline.clear();
        sync = false;
    }
    
    public boolean getSync() {
        return sync;
    }
    
    public static Map<String,Boolean> getStreamersOnline() throws InterruptedException {
        synchronized (streamersOnline) {
            sync = true;
            streamersOnline.notifyAll();
            return streamersOnline;
        }
    } 
    
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
    
    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Parser parser = new Parser();
        readInSavedStreamers();
        checkStreamOnline();
    }
    
    /**
     * Read in the whole file of streamers saved by user.
     * @throws IOException 
     */
    private static void readInSavedStreamers() throws IOException {
        String line;
        while((line = reader.readLine()) != null) {
            archiveList.add(line);
        }
    }
    
    /**
     * To add new streamers to data structure when a new one is entered in by user.
     * @param newStream 
     */
    public static void addNewStreamer(String newStream) {
        archiveList.add(newStream);
    }
    
    public static void removeOldStreamer(String oldStream) {
        archiveList.remove(oldStream);
    }
    
    /**
     * To check if the streamer's stream is currently online.
     */
    public static void checkStreamOnline() {
        for(String streamer : archiveList) {
            for(String stream : onlineLibrary) {
                if(stream.contains(streamer)) {
                    streamersOnline.put(streamer, Boolean.TRUE);
                    break;
                }
                else
                    streamersOnline.put(streamer, Boolean.FALSE);
            }
        }
        System.out.println("Parser: " + streamersOnline);
    }
    
    public static ArrayList<String> getArchiveList() {
        return archiveList;
    }
    
    public ArrayList<String> getOnlineLibrary() {
        return onlineLibrary;
    }
}
