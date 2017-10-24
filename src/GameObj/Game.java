
package GameObj;


public class Game extends GameStream {
    private String home, away, time, date, gameState, timeRemaining, awayFull, homeFull;
    private int id;
    
    public String getHomeTeam() {
        return home;
    }

    
    public void setHomeTeam(String team) {
        this.home = team;
    }

    
    public String getAwayTeam() {
        return away;
    }

    
    public void setAwayTeam(String team) {
        this.away = team;
    }

    
    public String getTime() {
        return time;
    }

    
    public void setTime(String time) {
        this.time = time;
    }

    
    public String getGameState() {
        return gameState;
    }

    
    public void setGameState(String state) {
        this.gameState = state;
    }

    
    public String getTimeRemaining() {
        return timeRemaining;
    }

    
    public void setTimeRemaining(String timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    
    public String getAwayTeamFull() {
        return awayFull;
    }

    
    public String getHomeTeamFull() {
        return homeFull;
    }

    
    public void setHomeTeamFull(String team) {
        this.homeFull = team;
    }

    
    public void setAwayTeamFull(String team) {
        this.awayFull = team;
    }

    
    public void setGameID(int id) {
        this.id = id;
    }

    
    public int getGameID() {
        return id;
    }

    
    public String getDate() {
        return date;
    }

    
    public void setDate(String date) {
        this.date = date;
    }
    
    @Override
    public String toString(){
        return this.getHomeTeam() + "," + this.getAwayTeam() + "," + this.getTime() + "," + this.getDate() + "," + this.getGameState() + "," + this.getTimeRemaining() + "," + this.getHomeTeamFull() + "," + this.getAwayTeamFull() + "," + this.id;
    }
        
}
