/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazyman;

import GameObj.Game;
import GameObj.League;
import Objects.Streamlink;
import Objects.Time;
import Util.EditHosts;
import Util.MessageBox;
import Util.ProcessReader;
import Util.Props;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.SwingWorker;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;



/**
 *
 * @author brady
 */
public class LazyManCLI {

//    private final Streamlink streamlink;
//    private final League[] leagues;
    
    private final List<League> leagues;
    private final CommandLine cmd;
    private League NHL;
    private League MLB;
    public boolean CLIMode;
    
    LazyManCLI(String[] args) throws ParseException{

        NHL = new League();
        MLB = new League();
        NHL.setName("NHL");
        NHL.setKeyURL("mf.svc.nhl.com");
        MLB.setName("MLB");
        MLB.setKeyURL("mlb-ws-mf.media.mlb.com");
        
        leagues = new ArrayList();
        leagues.add(NHL);
        leagues.add(MLB);
        
        Options options = new Options();

        options.addOption("cli", false, "Run in command line mode"); //assumed since we got here already
        options.addOption("getLeagues", false, "Returns a comma separated list of available leauges");
//        
//                // add t option
        options.addOption("league", true, "League - example: NHL");
        options.addOption("gameID", true, "Game ID - example: 12345678");
        options.addOption("feedName", true, "Feed name - example: HOME");
        options.addOption("cdn", true, "CDN - example: akc, l3c");



//        options.addRequiredOption("d", "date", true, "Date - example: 2000-12-31");
//        options.addRequiredOption("m", "mediaID", true, "Media ID - example: 52742303");
//        options.addRequiredOption("q", "quality", true, "Quality - examples: 360p, 540p, 720p, 720p60");
//        options.addRequiredOption("cdn", "cdn", true, "CDN - example: akc");
//        options.addRequiredOption("f", "feed", true, "Feed - examples: HOME, AWAY, FRENCH");

        CommandLineParser parser = new DefaultParser();
        cmd = parser.parse( options, args);
        
        if(cmd.hasOption("cli")){
            CLIMode = true;
        }else{
            CLIMode = false;
        }
                
        
    }
    
    public void run() throws InterruptedException{
        System.out.println("Running in command line mode");

        if(cmd.hasOption("getLeagues")){
            System.out.println(leagues.toString());
            return;
        }
        
        if(!cmd.hasOption("league")){
            throw new Error("\"league\" option not defined. Acceptable values are: " + leagues.toString());
        }
        
        //from here on out we assume that the user defined a league
        
        //now check to make sure the league is valid
        League selectedLeague = null;
        for (League leagueOption : leagues ){
            if(leagueOption.getName().equals(cmd.getOptionValue("league"))){
                selectedLeague = leagueOption;
            }
        }
        
        if(selectedLeague == null){
            throw new Error("\"league\" option is not valid. Acceptable values are: " + leagues.toString());
        }
        
        System.out.println("Selected league: " + selectedLeague);
        
        //check to see if date exists and try to format it, if it fails or doesnt exist, use today
        String selectedDate = null;
        if(cmd.hasOption("date")){
            //TODO - see if date actually formats correctly
            try{
                selectedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(cmd.getOptionValue("date")));
                
            }catch(Exception e){
                System.out.println("Error formatting date");
            }
            
        }
        if(selectedDate == null){
            selectedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            System.out.println("Date invalid or unspecified. Using today instead.");
        }
        
        System.out.println("Selected date: " + selectedDate);
        
        
        selectedLeague.setDate(selectedDate);
        selectedLeague.setGames(selectedDate);
        
        Game[] games = selectedLeague.getGames();
        
        
        if(!cmd.hasOption("gameID")){
            System.out.println("Here are a list of current games:");
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.println("Home Team, Away Team, Time, Date, Game State, Time Remaining, Home Team (Full), Away Team (Full), id");
            for(Game currGame : games){
                System.out.println(currGame.toString());
            }
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.println("");
            System.out.println("Please specify a game ID with \"-gameID\" ");
            return;
        }
        int gameID;
        try{
            gameID = Integer.parseInt(cmd.getOptionValue("gameID"));
        }catch(Exception e){
            throw new Error("Game ID is not a valid integer");
        }
        
        Game selectedGame = null;
        for(Game currGame : games){
            if(currGame.getGameID() == gameID){
                selectedGame = currGame;
            }
        }
        
        if(selectedGame == null){
            throw new Error("Could not find game with ID of: " + gameID);
        }
        
        System.out.println("Found matching game: ");
        System.out.println(selectedGame);
        int numFeeds = selectedGame.getNumOfFeeds();

        if(!cmd.hasOption("feedName")){
            if(numFeeds <= 0){
                System.out.println("There are no available feeds");
                return;
            }
            System.out.println("Here are the available feeds: ");
            System.out.println("-------------------");
            System.out.println("Feed Name, Source");
            for(int currFeedIndex = 0; currFeedIndex < numFeeds; currFeedIndex++){
                System.out.println(selectedGame.getFeedName(currFeedIndex) + "," + selectedGame.getFeedTV(currFeedIndex));
            }
            System.out.println("-------------------");
            System.out.println("Please specify a feed with \"-feedName\"");
            return;
        }
        
        String feedName = null;
        String feedID = null;
        String feedTV = null;

        
        try{
            String usersFeedName = cmd.getOptionValue("feedName");
            
            for(int currFeedIndex = 0; currFeedIndex < numFeeds; currFeedIndex++){
                if(selectedGame.getFeedName(currFeedIndex).equals(usersFeedName)){
                    feedName = selectedGame.getFeedName(currFeedIndex);
                    feedID = selectedGame.getFeedID(currFeedIndex);
                    feedTV = selectedGame.getFeedTV(currFeedIndex);

                }
            }
            
            if(feedName == null){
                throw new Error();
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Error("There was an error getting your feed. Are you sure the feedName entered was correct?");
        }
        
        System.out.println("Found stream: ");
        System.out.println("(" + feedName + "," + feedTV + ")");
        
        String quality = null;
        if(cmd.hasOption("quality")){
            String userQuality = cmd.getOptionValue("quality");
            if(userQuality.equals("360p") || userQuality.equals("540p") || userQuality.equals("720p") || userQuality.equals("720p_alt")){
                quality = userQuality;
            }
        }
        if(quality == null){
            quality = "720p_alt";
            System.out.println("Quality not set. Using: " + quality);
            System.out.println("If you would like to specify a quality, use \"-quality\"");
        }
        
        String cdn = null;
        if(cmd.hasOption("cdn")){
            String userCdn = cmd.getOptionValue("cdn");
            if(userCdn.equals("akc") || userCdn.equals("l3c")){
                cdn = userCdn;
            }
        }
        if(cdn == null){
            cdn = "akc";
            System.out.println("CDN not set. Using: " + quality);
            System.out.println("If you would like to specify a cdn, use \"-cdn\"");
            System.out.println("There are two options, \"akc\" and \"l3c\"");
        }
        
        selectedLeague.getGwi().setCdn(cdn);
        selectedLeague.getGwi().setDate(selectedDate);
        selectedLeague.getGwi().setQuality(quality);
        selectedLeague.getGwi().setUrl(feedID, selectedLeague.getName());
        
        String loc = "/Applications/VLC.app/Contents/MacOS/VLC";
        File file = new java.io.File(loc);
        if (file.exists()) {
            Props.setVlcloc(loc);
        }
                
                
        Streamlink streamlink = new Streamlink();
        streamlink.setLocation(getSLLoc());
        //TODO check hosts
        //TODO save stream
        Process l = streamlink.run(selectedGame, selectedLeague.getGwi());
        System.out.print("\n-----\n");
        System.out.println("Streamlink starting...\n");
        l.waitFor();
        System.out.println(ProcessReader.getProcessOutput(l));
        System.out.println("Streamlink done");

        

        

        

        
        
        

        
        

        
        
//        System.out.println(selectedLeague.getGames());
        
        
        
        
        
        

    }
    
    private String getSLLoc() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            String ls = "streamlink\\streamlink.exe";
            java.io.File f = new java.io.File(ls);
            if (f.exists()) {
                return ls;
            } else {
                System.out.println("Could not find Streamlink. Please extract the folder comtaining Streamlink to the same folder as LazyMan.");
            }
        } else {
            String ls;
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                ls = "/usr/local/bin/streamlink";
            } else {
                ls = "streamlink";
            }
            if (cmdExists(ls)) {
                return ls;
            } else {
                System.out.println("Could not find Streamlink. Please follow the guide on how to install it.");
            }
        }
        return null;

    }
        
    private boolean cmdExists(String cmd) {
        String output;
        try {
            output = ProcessReader.getProcessOutput(new ProcessBuilder("/bin/sh", "-c", cmd).redirectErrorStream(true).start()).toLowerCase();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        return !(output.equals("")
                || output.contains("not found")
                || output.contains("no file")
                || output.contains("no such"));
    }

    
    
}
