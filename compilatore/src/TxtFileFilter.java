import java.io.File;
import javax.swing.filechooser.FileFilter;

public class TxtFileFilter extends FileFilter {
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;         // Accetta sempre le directory!
        }

        String name = f.getName().toLowerCase();

        return name.endsWith(".txt");
    }

    public String getDescription() {
        return "Text File (*.txt)";
    }
}