package edu.cmu.cs.cs214.rec09.framework.gui;

class Cell {
    private final int x;
    private final int y;
    private final String text;
    private final boolean playable;

    Cell(int x, int y, String text, boolean playable) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.playable = playable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return this.text;
    }

    public boolean isPlayable() {
        return this.playable;
    }

    @Override
    public String toString() {
        return "{ \"text\": \"" + this.text + "\"," +
                " \"playable\": " + this.playable + "," +
                " \"x\": " + this.x + "," +
                " \"y\": " + this.y + " }" ;
    }
}
