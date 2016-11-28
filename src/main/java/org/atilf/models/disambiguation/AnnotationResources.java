package org.atilf.models.disambiguation;

/**
 * @author Simon Meoni Created on 28/11/16.
 */
public enum AnnotationResources {
    NO_DM ("noDM"),
    DM0("DM0"),
    DM1("DM1"),
    DM2("DM2"),
    DM3("DM3"),
    DM4("DM4");

    public String value;

    AnnotationResources(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
