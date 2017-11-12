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
@JsonDeserialize(using = EventType.MyEnumDeserializer.class)
@JsonSerialize(using = EventType.MyEnumSerializer.class)
public enum EventType {
	FINANCING("F", "Financing"), LEASING("L", "Leasing"), NOTIFICATIONS("N", "Notification");
	private String value;
	private String key;
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private EventType(String value, String key) { this.value = value; this.key = key;}

    public String toString() {
       return this.value;
    }
    
    
    
	public static class MyEnumDeserializer extends StdDeserializer<EventType> {

		private static final long serialVersionUID = 1647930679390203350L;

		public MyEnumDeserializer() {
            super(EventType.class);
        }

        @Override
        public EventType deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        	final JsonNode jsonNode = jp.readValueAsTree();
            String value = jsonNode.asText();
            if (StringUtils.isEmpty(value)) {
            	value = jsonNode.get("code").asText();
            }
            for (EventType me: EventType.values()) {
                if ( me.getValue().equals(value)) {
                    return me;
                }
            }
            throw dc.mappingException("Cannot deserialize DurationUnits from key " + value);
        }
    }
	
	public static class MyEnumSerializer extends StdSerializer<EventType> {

		private static final long serialVersionUID = 7379835801561263773L;

		public MyEnumSerializer() {
            super(EventType.class);
        }

        @Override
        public void serialize(EventType swe, 
                              JsonGenerator jgen,
                              SerializerProvider sp) throws IOException, JsonGenerationException {
            jgen.writeString(swe.getValue());
        }
    }	
}
