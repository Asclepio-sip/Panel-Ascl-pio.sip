package Asclepio.config.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PepperedPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder delegate;
    private final String pepper;

    public PepperedPasswordEncoder(PasswordEncoder delegate, String pepper) {
        this.delegate = delegate;
        this.pepper = pepper;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.encode(aplicarPepper(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return delegate.matches(aplicarPepper(rawPassword), encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return delegate.upgradeEncoding(encodedPassword);
    }

    private String aplicarPepper(CharSequence rawPassword) {
        return rawPassword + pepper;
    }
}