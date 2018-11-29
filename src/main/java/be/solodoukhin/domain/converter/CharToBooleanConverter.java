package be.solodoukhin.domain.converter;

import javax.persistence.AttributeConverter;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
public class CharToBooleanConverter implements AttributeConverter<String, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(String s) {
        return s.equalsIgnoreCase("t");
    }

    @Override
    public String convertToEntityAttribute(Boolean aBoolean) {

        if(aBoolean.equals(true)){
            return "t";
        } else {
            return "f";
        }
    }
}
