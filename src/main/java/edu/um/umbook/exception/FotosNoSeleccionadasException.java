package edu.um.umbook.exception;

public class FotosNoSeleccionadasException extends RuntimeException {
    public FotosNoSeleccionadasException() { super("Seleccioná al menos una foto."); }
}
