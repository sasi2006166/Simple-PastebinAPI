package me.candiesjar.objects;

import lombok.Getter;

@Getter
public enum Privacy {

    PUBLIC(0),
    PRIVATE(1);

    private final int privacy;

    Privacy(int privacy) {
        this.privacy = privacy;
    }

}
