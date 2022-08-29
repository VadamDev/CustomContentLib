package net.vadamdev.customcontent.lib.exceptions;

/**
 * @author VadamDev
 */
public class AlreadyRegisteredException extends Exception {
    public AlreadyRegisteredException(String registryName) {
        super(registryName + " is already registred please use an another registry name !");
    }
}
