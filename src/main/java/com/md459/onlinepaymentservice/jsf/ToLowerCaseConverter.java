package com.md459.onlinepaymentservice.jsf;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Converts values to lower-case. Used when logging in as usernames are
 * stored in lowercase in the DB.
 * 
 */
@FacesConverter("toLowerCaseConverter")
public class ToLowerCaseConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        return (submittedValue != null) ? submittedValue.toLowerCase() : null;
    }
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        return (modelValue != null) ? modelValue.toString() : "";
    }
}
