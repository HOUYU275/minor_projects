package weka.fuzzy.similarity;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 09-Nov-2010
 * Time: 12:34:38
 * To change this template use File | Settings | File Templates.
 */
public class Relations {
    private Relation[] relations;

    public Relations(int size) {
        relations = new Relation[size];
    }

    public Relation[] getRelations() {
        return relations;
    }

    public void setRelations(Relation[] relations) {
        this.relations = relations;
    }

    public Relation getRelation(int index) {
        return relations[index];
    }

    public void setRelation(int index, Relation relation) {
        this.relations[index] = relation;
    }
}
