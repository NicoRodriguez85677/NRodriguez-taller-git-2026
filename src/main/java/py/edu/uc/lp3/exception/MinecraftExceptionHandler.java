
package py.edu.uc.lp3.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MinecraftExceptionHandler {

    public record ErrorRespuesta(
            int status,
            String error,
            String mensaje,
            LocalDateTime fecha) {
    }

    @ExceptionHandler(MobNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> manejarNoEncontrado(
            MobNoEncontradoException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorRespuesta(
                        404,
                        "MOB_NO_ENCONTRADO",
                        ex.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(AccionIlegalException.class)
    public ResponseEntity<ErrorRespuesta> manejarAccionIlegal(
            AccionIlegalException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorRespuesta(
                        409,
                        "ACCION_ILEGAL",
                        ex.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRespuesta> manejarArgumentoInvalido(
            IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRespuesta(
                        400,
                        "ARGUMENTO_INVALIDO",
                        ex.getMessage(),
                        LocalDateTime.now()));
    }
}
