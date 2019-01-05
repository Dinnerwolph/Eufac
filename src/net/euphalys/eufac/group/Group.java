package net.euphalys.eufac.group;

/**
 * @author Dinnerwolph
 */
public class Group {

    private int id;
    private String name;
    private String prefix;
    private String suffix;
    private int ladder;

    public Group(int id, String name, String prefix, String suffix, int ladder) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.ladder = ladder;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getLadder() {
        return ladder;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", ladder=" + ladder +
                '}';
    }
}
