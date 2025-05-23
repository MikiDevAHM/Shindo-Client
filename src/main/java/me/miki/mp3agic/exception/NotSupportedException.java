package me.miki.mp3agic.exception;

import me.miki.mp3agic.BaseException;

public class NotSupportedException extends BaseException {

    private static final long serialVersionUID = 1L;

    public NotSupportedException() {
        super();
    }

    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
