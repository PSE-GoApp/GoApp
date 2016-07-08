package edu.kit.pse.client.goapp.converter;

import java.util.List;

/**
 * The Converters who convert JSON-Strings to Objects have to implement this Interface.
 * Created by paula on 29.06.16.
 */
public interface Converter<T> {

    /**
     * Creates an Object T out of the given JSON-String
     * @param jsonString String in the format of a JSON-String
     * @return String as Object T
     */
    public T deserialize(String jsonString);

    /**
     * Creates a List of Objects T out of the given JSON-String
     * @param jsonString String in the format of a JSON-String
     * @return String as List<T>
     */
    public List<T> deserializeList(String jsonString);
    
    


}
