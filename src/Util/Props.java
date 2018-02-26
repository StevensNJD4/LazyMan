package Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class Props {

    private static final Properties PROP = new Properties();

    private static String getConfigLoc() throws IOException {
        String loc;
        File f, wrongf;

        if (!System.getProperty("os.name").toLowerCase().contains("linux")) {
            loc = Paths.get(".").toAbsolutePath().normalize().toString() + System.getProperty("file.separator") + "config.properties";
        } else {
            String test = System.getProperty("user.name");
            loc = System.getProperty("user.home") + System.getProperty("file.separator") + ".config" + System.getProperty("file.separator") + "lazyman" + System.getProperty("file.separator") + "config.properties";
            loc = System.getProperty("user.home") + "/.config/sdfgsdf/config.properties";
        }

        f = new File(loc);
        
        wrongf = new File(f.getParent() + "LazyMan2.execonfig");

        if (f.exists() && wrongf.exists()) {
            wrongf.delete();
        } else if (!f.exists() && wrongf.exists()) {
            wrongf.renameTo(f);
        } else if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        if (!f.canWrite()) {
            String msg = String.format("I can't write to the properties file. Please give the folder containing LazyMan write permissions. (%s)", loc);
            MessageBox.show(msg, "Can't write", 2);
            System.exit(-1);
        }

        return loc;
    }

    public static String getVlcloc() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("VLCLocation")) {
                return PROP.getProperty("VLCLocation");
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setVlcloc(String vlcloc) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("VLCLocation", vlcloc);
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getPW() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("Pass")) {
                return Encryption.decrypt(PROP.getProperty("Pass"));
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setPW(String pw) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("Pass", Encryption.encrypt(pw));
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setBitrate(String br) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("Bitrate", br);
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getBitrate() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("Bitrate")) {
                return PROP.getProperty("Bitrate");
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setCDN(String cdn) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("CDN", cdn);
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getCDN() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("CDN")) {
                return PROP.getProperty("CDN");
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public static String getFavTeam(String league) {
        switch (league) {
            case "MLB":
                return getMLBTeam();
            case "NHL":
                return getNHLTeam();
            default: 
                return "";
        }
    }

    public static String getNHLTeam() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("NHLTeam")) {
                return PROP.getProperty("NHLTeam");
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setNHLTeam(String team) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("NHLTeam", team);
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getMLBTeam() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("MLBTeam")) {
                return PROP.getProperty("MLBTeam");
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setMLBTeam(String team) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("MLBTeam", team);
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setPreferFrench(String f) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("French", f);
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getPreferFrench() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("French")) {
                return PROP.getProperty("French");
            }
            return "0";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String getSaveStreamLoc() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("SaveStreamLoc")) {
                return PROP.getProperty("SaveStreamLoc");
            }
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                return Paths.get(".").toAbsolutePath().normalize().toString();
            } else {
                return new java.io.File(Props.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return Props.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ").substring(1).replace("LazyMan2.jar", "").replace("LazyMan2.exe", "");
        } else {
            return Props.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ").replace("LazyMan2.jar", "").replace("LazyMan2.exe", "");
        }
    }

    public static void setSaveStreamLoc(String saveStreamLoc) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("SaveStreamLoc", saveStreamLoc);
            PROP.store(output, "");
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getStreamLinkArgs() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("LivestreamerArgs")) {
                setStreamlinkArgs(PROP.getProperty("LivestreamerArgs"));
                //  PROP.remove("LivestreamerArgs");
                return PROP.getProperty("StreamlinkArgs");
            } else if (PROP.containsKey("StreamlinkArgs")) {
                return PROP.getProperty("StreamlinkArgs");
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String[] getStreamlinkArgsArray() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("StreamlinkArgs")) {
                return PROP.getProperty("StreamlinkArgs").split("[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setStreamlinkArgs(String lsArgs) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("StreamlinkArgs", lsArgs);
            if (PROP.containsKey("LivestreamerArgs")) {
                PROP.remove("LivestreamerArgs");
            }
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getMediaPlayerrArgs() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("MediaPlayerrArgs")) {
                return PROP.getProperty("MediaPlayerrArgs");
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String[] getMediaPlayerrArgsArray() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            PROP.load(input);
            input.close();
            if (PROP.containsKey("MediaPlayerrArgs")) {
                return PROP.getProperty("MediaPlayerrArgs").split("[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setMediaPlayerrArgs(String lsArgs) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            PROP.setProperty("MediaPlayerrArgs", lsArgs);
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setRefreshRate(int r) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            // set the properties value
            PROP.setProperty("RefreshRate", "" + r);
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }

        }
    }

    public static int getRefreshRate() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            // load a properties file
            PROP.load(input);
            input.close();
            if (PROP.containsKey("RefreshRate")) {
                return Integer.parseInt(PROP.getProperty("RefreshRate"));
            } else {
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setIP(String ip) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getConfigLoc());

            // set the properties value
            PROP.setProperty("IP", ip);
            PROP.store(output, "");

        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }

        }
    }

    public static String getIP() {
        try {
            InputStream input;
            input = new FileInputStream(getConfigLoc());

            // load a properties file
            PROP.load(input);
            input.close();
            if (PROP.containsKey("IP")) {
                return PROP.getProperty("IP");
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
