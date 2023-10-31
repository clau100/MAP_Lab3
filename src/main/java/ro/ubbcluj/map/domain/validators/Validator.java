package ro.ubbcluj.map.domain.validators;

public interface Validator<T> {
    // Template for a validator
    void validate(T entity) throws ValidationException;
}