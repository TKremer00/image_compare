package sample;

public enum CheckMethods {
    CHECK_NEIGHBORS("neighbors", 0),  CHECK_LINIAR("liniar", 1);

    private final String name;
    private final int number;

    CheckMethods(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName(){
        return name;
    }

    public int getNumber(){
        return number;
    }

}
