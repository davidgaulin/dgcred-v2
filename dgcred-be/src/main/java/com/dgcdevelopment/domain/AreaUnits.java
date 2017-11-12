package com.dgcdevelopment.domain;

import java.io.IOException;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = AreaUnits.MyEnumDeserializer.class)
@JsonSerialize(using = AreaUnits.MyEnumSerializer.class)
public enum AreaUnits {
	SQUARE_FEET("SQFT", "square feet"), SQUARE_METER("SQM", "square meter");
	private String value; 
	private String key;
	private AreaUnits(String value, String key) { this.value = value; this.key = key; }
    public String toString() {
       return this.value;
    }
    
    
    public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}


	public static class MyEnumDeserializer extends StdDeserializer<AreaUnits> {

		private static final long serialVersionUID = -2262160981963351521L;

		public MyEnumDeserializer() {
            super(AreaUnits.class);
        }

        @Override
        public AreaUnits deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
            final JsonNode jsonNode = jp.readValueAsTree();
            // we may get teh value, we may get the value code, must be prepared for both
            String value = jsonNode.asText();
            if (StringUtils.isEmpty(value)) {
            	value = jsonNode.get("code").asText();
            }
            for (AreaUnits me: AreaUnits.values()) {
                if (me.getValue().equals(value)) {
                    return me;
                }
            }
            throw dc.mappingException("Cannot deserialize MyEnum from key " + value);
        }
    }
	
	public static class MyEnumSerializer extends StdSerializer<AreaUnits> {

		private static final long serialVersionUID = 4782089748518611974L;

		public MyEnumSerializer() {
            super(AreaUnits.class);
        }

        @Override
        public void serialize(AreaUnits swe, 
                              JsonGenerator jgen,
                              SerializerProvider sp) throws IOException, JsonGenerationException {
            jgen.writeString(swe.getValue());
        }
    }
}
