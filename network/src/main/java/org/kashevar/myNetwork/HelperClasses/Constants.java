package org.kashevar.myNetwork.HelperClasses;

public enum Constants {

    MB_20(20*1000000);

    private int value;
    Constants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
