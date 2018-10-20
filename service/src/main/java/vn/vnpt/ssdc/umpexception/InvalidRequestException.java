package vn.vnpt.ssdc.umpexception;

/**
 * Created by THANHLX on 5/11/2017.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
