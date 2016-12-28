/*
 * The JASDB software and code is Copyright protected 2011 and owned by Renze de Vries
 * 
 * All the code and design principals in the codebase are also Copyright 2011 
 * protected and owned Renze de Vries. Any unauthorized usage of the code or the 
 * design and principals as in this code is prohibited.
 */
package nl.renarj.jasdb.api.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: renarj
 * Date: 2/10/12
 * Time: 12:35 PM
 */
public class MultivalueProperty implements Property {
    private List<Value> values;
    private String propertyName;
    private boolean isCollection;

    public MultivalueProperty(String propertyName) {
        this.values = new ArrayList<>();
        this.propertyName = propertyName;
        this.isCollection = false;
    }

    public MultivalueProperty(String propertyName, boolean isCollection) {
        this.values = new ArrayList<>();
        this.propertyName = propertyName;
        this.isCollection = isCollection;
    }


    @Override
    public String getPropertyName() {
        return propertyName; 
    }

    @Override
    public Object getFirstValueObject() {
        return getFirstValue().getValue();
    }

    @Override
    public boolean hasValues() {
        return !values.isEmpty();
    }

    @Override
    public Value getFirstValue() {
        if(values.size() > 0) {
            return values.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Value> getValues() {
        return values;
    }

    @Override
    public <T> List<T> getValueObjects() {
        List<T> valueObjects = new ArrayList<T>();
        for(Value value : values) {
            valueObjects.add((T)value.getValue());
        }
        return valueObjects;
    }

    @Override
    public boolean isMultiValue() {
        return isCollection;
    }

    @Override
    public Property addValue(Value value) {
        values.add(value);
        validateAndSetCollection();
        return this;
    }

    @Override
    public Property removeValue(Value v) {
        for(Iterator i=values.iterator(); i.hasNext(); ) {
            if(i.next().equals(v)) {
                i.remove();
            }
        }
        validateAndSetCollection();
        return this;
    }

    private void validateAndSetCollection() {
        isCollection = isCollection && values.size() > 0 || !isCollection && values.size() > 1;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(propertyName).append("{");
        for(Value value : values) {
            builder.append(value.toString()).append(";");
        }
        builder.append("}");
        return builder.toString();
    }
}
