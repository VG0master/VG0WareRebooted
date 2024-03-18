public class Tanorian {
    private int whiteCount;
    private boolean shinyStatus;
    private String name;

    public Tanorian(int whiteCount, String name) {
        this.whiteCount = whiteCount;
        this.name = name;
        this.shinyStatus = false;
    }

    public Tanorian(int whiteCount,  String name, boolean shinyStatus) {
        this.whiteCount = whiteCount;
        this.name = name;
        this.shinyStatus = shinyStatus;
    }

    public int getWhiteCount() {
        return whiteCount;
    }
    public boolean getShinyStatus() {
        return shinyStatus;
    }
    public String getName() {
        return name;
    }
}
