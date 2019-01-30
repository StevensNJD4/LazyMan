package Objects;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Proxy {

    private int port;
    private String path, mitm;
    private Process p;
    private boolean ready;

    public Proxy(int port) {
        if (port == -1) {
            this.port = 5024;
        } else {
            this.port = port;
        }
        ready = false;

        if (!System.getProperty("os.name").toLowerCase().contains("linux")) {
            path = Paths.get(".").toAbsolutePath().normalize().toString() + System.getProperty("file.separator");
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                mitm = "win" + System.getProperty("file.separator") + "mlbamproxy.exe";
            } else {
                mitm = "mac" + System.getProperty("file.separator") + "mlbamproxy";
            }
        } else {
            path = new java.io.File(Proxy.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + System.getProperty("file.separator");
            mitm = "linux" + System.getProperty("file.separator") + System.getProperty("os.arch") + System.getProperty("file.separator") + "mlbamproxy";
        }
        path += "mlbamproxy" + System.getProperty("file.separator");
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            File m = new File(path + mitm);
            if (!m.canExecute()) {
                m.setExecutable(true);
            }
        }
    }

    public void run() {
        List<String> args = new ArrayList<>(
            Arrays.asList(
                path + mitm,
                "-p",
                getPort() + "",
                "-d", "freegamez.ga",
                "-s", "mf.svc.nhl.com,playback.svcs.mlb.com,mlb-ws-mf.media.mlb.com"
            )
        );
        ProcessBuilder pb = new ProcessBuilder(args).inheritIO().redirectErrorStream(true);
        try {
            p = pb.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isRunning() {
        if (p == null) {
            return false;
        }
        return p.isAlive();
    }

    public void kill() {
        ready = false;
        if (p != null) {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                try {
                    Runtime.getRuntime().exec("taskkill /f /im mlbamproxy.exe");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                p.destroy();
            }
            p = null;
        }
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    public boolean isReady() {
        try {
            return !p.waitFor(3, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Proxy.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Process get() {
        return p;
    }
}
