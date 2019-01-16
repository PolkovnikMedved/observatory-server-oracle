package be.solodoukhin.domain.converter;

import javax.persistence.AttributeConverter;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: This converter helps us to convert CHAR(1) database field into a Boolean Java field
 */
public class CharToBooleanConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "t" : "f";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return dbData != null && dbData.equalsIgnoreCase("t");
    }
}
