package com.anshaysaboo.socalbeach4life.Objects;

public class Exceptions {

    // Thrown when attempting to register an account with an email that already exists
    public static class EmailExistsException extends Exception {
        public EmailExistsException(String errorMessage) {
            super(errorMessage);
        }
    }

    // Thrown when a network request finishes with an unknown error
    public static class RequestException extends Exception {
        public RequestException(String errorMessage) {
            super(errorMessage);
        }
    }

    // Thrown when an attempted login returns some kind of error
    public static class LoginException extends Exception {
        public LoginException(String errorMessage) {
            super(errorMessage);
        }
    }
}
