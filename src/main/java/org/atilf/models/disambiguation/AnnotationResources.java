package org.atilf.models.disambiguation;

/**
 * @author Simon Meoni Created on 28/11/16.
 */
public enum AnnotationResources {
    DA_ON("#DAOn"),
    DA_OFF("#DAOff"),
    NO_DA("#noDA"),
    NO_DM ("#noDM", NO_DA),
    DM0("#DM0", DA_OFF),
    DM1("#DM1", DA_OFF),
    DM2("#DM2", DA_OFF),
    DM3("#DM3", DA_OFF),
    DM4("#DM4", DA_ON),
    LEX_ON("_lexOn"),
    LEX_OFF("_lexOff");

    private String value;

    private AnnotationResources autoAnnotation = null;

    AnnotationResources(String value) {
        this.value = value;
    }

    AnnotationResources(String value, AnnotationResources autoAnnotation) {
        this.value = value;
        this.autoAnnotation = autoAnnotation;
    }

    public AnnotationResources getAutoAnnotation() {
        return autoAnnotation;
    }

    public String getValue() {
        return value;
    }

    public static AnnotationResources getAnnotations(String annotation){
        for (AnnotationResources annotationResources : AnnotationResources.values()) {
            if (annotationResources.getValue().equals(annotation)){
                return annotationResources;
            }
        }
        return null;
    }
}
