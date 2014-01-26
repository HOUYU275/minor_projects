/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/09/11
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class Author {
    private String name;
    private String institution;

    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public Author(String name, String institution) {
        this.name = name;
        this.institution = institution;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
