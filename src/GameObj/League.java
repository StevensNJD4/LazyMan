
package GameObj;

import com.toedter.calendar.JDateChooser;
import java.util.ArrayList;
import java.util.Timer;
import javax.swing.JTable;
import lazyman.GetMLBInfo;
import lazyman.GetNHLInfo;


public class League {
    private Game[] games;
    private GameWatchInfo gwi;
    private int selectedGame, streamlinkSwitch;
    private boolean favGameSelected, hostsFileEdited;
    private ArrayList<String> playbackIDs = new ArrayList<>();
    private JTable table;
    private Timer timer;
    private String date, name, keyURL, favoriteTeam;
    private JDateChooser dateTF;
    
    public League() {
        selectedGame = 0;
        streamlinkSwitch = 0;
        favGameSelected = false;
        hostsFileEdited = false;
        gwi = new GameWatchInfo();
    }

    public Game[] getGames() {
        return games;
    }

    public void setGames(String date) {
        if (name.equals("NHL"))
            games = GetNHLInfo.getGames(date);
        else
            games = GetMLBInfo.getGames(date);
    }

    public GameWatchInfo getGwi() {
        return gwi;
    }

    public void setGwi(GameWatchInfo gwi) {
        this.gwi = gwi;
    }

    public int getSelectedGame() {
        return selectedGame;
    }

    public void setSelectedGame(int selectedGame) {
        this.selectedGame = selectedGame;
    }

    public int getStreamlinkSwitch() {
        return streamlinkSwitch;
    }

    public void setStreamlinkSwitch(int streamlinkSwitch) {
        this.streamlinkSwitch = streamlinkSwitch;
    }

    public boolean isFavGameSelected() {
        return favGameSelected;
    }

    public void setFavGameSelected(boolean favGameSelected) {
        this.favGameSelected = favGameSelected;
    }

    public boolean isHostsFileEdited() {
        return hostsFileEdited;
    }

    public void setHostsFileEdited(boolean hostsFileEdited) {
        this.hostsFileEdited = hostsFileEdited;
    }

    public ArrayList<String> getPlaybackIDs() {
        return playbackIDs;
    }

    public void setPlaybackIDs(ArrayList<String> playbackIDs) {
        this.playbackIDs = playbackIDs;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JDateChooser getDateTF() {
        return dateTF;
    }

    public void setDateTF(JDateChooser dateTF) {
        this.dateTF = dateTF;
    }

    public String getKeyURL() {
        return keyURL;
    }

    public void setKeyURL(String keyURL) {
        this.keyURL = keyURL;
    }

    public String getFavoriteTeam() {
        return favoriteTeam;
    }

    public void setFavoriteTeam(String favoriteTeam) {
        this.favoriteTeam = favoriteTeam;
    }
    
    @Override
    public String toString(){
        return this.getName();
    }
}
