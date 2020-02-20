package com.demo.network.dialogs;

/**
 * Created by abdulkhan on 26/10/15.
 */
public enum EMessageStatus {
    NOTDELIVERED(1), DELIVERED(2), READ(3), RESPOND(4);

    EMessageStatus(final int i) {
        type = i;
    }

    private final int type;

    public int getNumericType() {
        return type;
    }
}
