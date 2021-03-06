/*
 * The JASDB software and code is Copyright protected 2011 and owned by Renze de Vries
 * 
 * All the code and design principals in the codebase are also Copyright 2011 
 * protected and owned Renze de Vries. Any unauthorized usage of the code or the 
 * design and principals as in this code is prohibited.
 */
package nl.renarj.jasdb.core.exceptions;

/**
 * User: renarj
 * Date: 1/16/12
 * Time: 9:52 PM
 */
public class LocatorException extends JasDBStorageException {
    public LocatorException(String message, Throwable e) {
        super(message, e);
    }
    
    public LocatorException(String message) {
        super(message);
    }
}
