package co.com.dk.juanvaldez.jvsignupmc.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SignUpMCException extends Exception {

    public SignUpMCException(String message) {
        super(message);
    }
}
