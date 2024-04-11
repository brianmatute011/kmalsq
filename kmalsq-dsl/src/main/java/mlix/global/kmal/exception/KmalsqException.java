package mlix.global.kmal.exception;

public class KmalsqException extends RuntimeException{

        public KmalsqException(String message) {
            super(message);
        }

        public KmalsqException(String message, Throwable cause) {
            super(message, cause);
        }
}
