
package lazyman;

import java.awt.Toolkit;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class LazyMan {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            LazyManCLI cli = new LazyManCLI(args);

            
            if(cli.CLIMode){
                cli.run();
            }else{
                LazyMan lm = new LazyMan();
                lm.runGUI();
            }

            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void runGUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "LazyMan");

            com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage(LazyMan.class.getResource("/Logos/LM.png"));
            application.setDockIconImage(image);
            application.setEnabledAboutMenu(false);
            application.setEnabledPreferencesMenu(false);
        }
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        MainGUI m = new MainGUI();
        m.setLocationRelativeTo(null);
        m.setVisible(true);

    }
    
}
