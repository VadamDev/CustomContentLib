package net.vadamdev.customcontent.lib.exceptions;

public class AlreadyRegisteredException extends Exception {
    public AlreadyRegisteredException(String registryName) {
        super(registryName + " is already registred !");
    }
}
