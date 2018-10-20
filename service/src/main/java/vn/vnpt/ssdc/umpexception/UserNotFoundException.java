package vn.vnpt.ssdc.umpexception;

/**
 * Created by THANHLX on 5/11/2017.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
