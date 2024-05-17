package br.edu.infnet.socialnetwork.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String detail) {
        super(detail);
    }
}
