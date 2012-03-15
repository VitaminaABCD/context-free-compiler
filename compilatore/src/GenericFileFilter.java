import java.io.File;
import javax.swing.filechooser.FileFilter;

public class GenericFileFilter extends FileFilter {
    private String descrizione;
    private String finale;

    public GenericFileFilter(String descrizione, String estensione) {
        this.descrizione = descrizione;
        finale = "." + estensione;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;         // Accetta sempre le directory!
        }

        String name = f.getName().toLowerCase();

        return name.endsWith(finale);
    }

    public String getDescription() {
        return descrizione;
    }
}
