package me.lumpchen.xafp;

import me.lumpchen.xafp.sf.StructureField;

import java.io.IOException;

public class DefaultFont extends Font {
    public DefaultFont(StructureField structField) {
        super(structField);
        try {
            control = (FontControl) new DefaultFontControl();
        } catch (IOException exception) {

        }
    }


}
