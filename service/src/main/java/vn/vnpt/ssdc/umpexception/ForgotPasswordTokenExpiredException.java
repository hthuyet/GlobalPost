package vn.vnpt.ssdc.umpexception;

/**
 * Created by THANHLX on 5/11/2017.
 */
public class ForgotPasswordTokenExpiredException extends RuntimeException {
    public ForgotPasswordTokenExpiredException(String message) {
        super(message);
    }
}
