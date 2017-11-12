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
@JsonDeserialize(using = PaymentPeriod.MyEnumDeserializer.class)
@JsonSerialize(using = PaymentPeriod.MyEnumSerializer.class)
public enum PaymentPeriod {
	MONTHS("M", "Months"), YEARS("Y", "Years"), WEEKS("W", "Weeks"), DAYS("D", "Days"), HOURS("H", "Hours");
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

	private PaymentPeriod(String value, String key) { this.value = value; this.key = key;}

    public String toString() {
       return this.value;
    }
    
    
    
	public static class MyEnumDeserializer extends StdDeserializer<PaymentPeriod> {

		private static final long serialVersionUID = 1647930679390203350L;

		public MyEnumDeserializer() {
            super(PaymentPeriod.class);
        }

        @Override
        public PaymentPeriod deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        	final JsonNode jsonNode = jp.readValueAsTree();
            String value = jsonNode.asText();
            if (StringUtils.isEmpty(value)) {
            	value = jsonNode.get("code").asText();
            }
            for (PaymentPeriod me: PaymentPeriod.values()) {
                if ( me.getValue().equals(value)) {
                    return me;
                }
            }
            throw dc.mappingException("Cannot deserialize PaymentPeriod from key " + value);
        }
    }
	
	public static class MyEnumSerializer extends StdSerializer<PaymentPeriod> {

		private static final long serialVersionUID = 7379835801561263773L;

		public MyEnumSerializer() {
            super(PaymentPeriod.class);
        }

        @Override
        public void serialize(PaymentPeriod swe, 
                              JsonGenerator jgen,
                              SerializerProvider sp) throws IOException, JsonGenerationException {
            jgen.writeString(swe.getValue());
        }
    }	
}
