package me.lumpchen.xafp;

import me.lumpchen.xafp.sf.Introducer;
import me.lumpchen.xafp.sf.StructureField;

import java.io.IOException;

public class DefaultFontControl extends FontControl{
    public DefaultFontControl() throws IOException {
        super();
        this.xShapeResolution = 36;
        this.yShapeResolution = 36;
    }
}
