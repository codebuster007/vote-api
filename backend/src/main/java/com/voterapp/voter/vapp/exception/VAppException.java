package com.voterapp.voter.vapp.exception;

import com.voterapp.voter.vapp.config.PropertiesConfig;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

import static com.voterapp.voter.vapp.exception.ExceptionType.*;

@Component
public class VAppException {

    private static PropertiesConfig propertiesConfig;

    public VAppException(PropertiesConfig propertiesConfig) {
        VAppException.propertiesConfig = propertiesConfig;
    }

    /**
     * Returns new {@link RuntimeException} based on template and args
     *
     * @param messageTemplate
     * @param args
     * @return
     */
    public static RuntimeException throwException(String messageTemplate, String... args){
        return new RuntimeException(format(messageTemplate, args));
    }

    /**
     * Returns new {@link RuntimeException} based on the {@link EntityType}, {@link ExceptionType} and args
     *
     * @param entityType
     * @param exceptionType
     * @param args
     * @return
     */
    public static RuntimeException throwException(EntityType entityType, ExceptionType exceptionType, String... args){
        String messageTemplate = getMessageTemplate(entityType, exceptionType);
        return throwException(exceptionType, messageTemplate, args);
    }

    /**
     *  Returns new {@link RuntimeException} based on the {@link EntityType}, {@link ExceptionType}, id and args
     *
     * @param entityType
     * @param exceptionType
     * @param id
     * @param args
     * @return
     */
    public static RuntimeException throwExceptionWithId(EntityType  entityType, ExceptionType exceptionType, String id, String... args) {
        String messageTemplate = getMessageTemplate(entityType, exceptionType).concat(".").concat(id);
        return throwException(exceptionType, messageTemplate, args);
    }


    public static class EntityNotFoundException extends RuntimeException{
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicateEntityException extends RuntimeException{
        public DuplicateEntityException(String message) {
            super(message);
        }
    }

    public static class PollTimeExpiredException extends RuntimeException {
        public PollTimeExpiredException(String message){
            super(message);
        }
    }
    /**
     *  Returns new {@link RuntimeException} based on template and args
     *
     * @param exceptionType
     * @param messageTemplate
     * @param args
     * @return
     */
    private static RuntimeException throwException(ExceptionType exceptionType, String messageTemplate, String... args){
        if(ENTITY_NOT_FOUND.equals(exceptionType)){
            return new EntityNotFoundException(format(messageTemplate, args));
        }else if(DUPLICATE_ENTITY.equals(exceptionType)){
            return new DuplicateEntityException(format(messageTemplate, args));
        }else if(POLL_TIME_EXPIRED.equals(exceptionType)){
            return new PollTimeExpiredException(format(messageTemplate, args));
        }

        return new RuntimeException(format(messageTemplate, args));
    }

    private static String getMessageTemplate(EntityType entityType, ExceptionType exceptionType){
        return entityType.name().concat(".").concat(exceptionType.getValue()).toLowerCase();
    }

    /**
     * Returns a formatted {@link String} of the error message substituted with the passed args
     *
     * @param template
     * @param args
     * @return
     */
    private static String format(String template, String... args){
        Optional<String> templateContent = Optional.ofNullable(propertiesConfig.getConfigValue(template));

        return templateContent.map(s -> MessageFormat.format(s, args)).orElseGet(() -> MessageFormat.format(template, args));

    }


}
