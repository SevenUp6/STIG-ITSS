package com.xjrsoft.common.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.xjrsoft.core.tool.utils.StringUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
			throws IOException {
		String value = p.getValueAsString();
		if (StringUtil.isNotBlank(value)) {
			if ("yyyy-MM-dd".length() == value.length()) {
				LocalDate localDate = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				return LocalDateTime.from(localDate.atStartOfDay());
			} else if ("yyyy-MM-dd hh:mm:ss".length() == value.length()) {
				return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			}
		}
		return null;
	}
}