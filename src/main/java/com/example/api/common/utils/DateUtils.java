/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Util class with common operations related to Dates. <br>
 * All methods are static.
 * 
 * @author gaspar
 *
 */
public class DateUtils {

	private static Cache<String, ZonedDateTime> zonedDateTimeCache = 
																CacheBuilder
																.newBuilder()
																.maximumSize(2000)
																.build();

	public static String minutesToHHMM(int minutes) {
		String hhmm = StringUtils.defaultString(null);

		long hours = TimeUnit.MINUTES.toHours(minutes);
		long remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);

		hhmm = String.format("%02d:%02d", hours, remainMinutes);

		return hhmm;
	}

	public static List<String> datesBetween(String fromString, String toString, String inputFormat,
			String outputFormat) {
		final List<String> dates = Collections.synchronizedList(new ArrayList<String>());

		List<LocalDate> datesBetween = datesBetween(fromString, toString, inputFormat);
		datesBetween.forEach(date -> {
			String dateString = format(date, outputFormat);
			dates.add(dateString);
		});

		return dates;
	}

	public static List<LocalDate> datesBetween(String fromString, String toString, String inputFormat) {
		List<LocalDate> dates = Collections.emptyList();

		LocalDate from = parse(fromString, inputFormat);
		LocalDate to = parse(toString, inputFormat);

		dates = datesBetween(from, to);

		return dates;
	}

	public static Long duration(String fromString, String toString) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

		ZonedDateTime from = ZonedDateTime.parse(fromString, dateTimeFormatter);
		ZonedDateTime to = ZonedDateTime.parse(toString, dateTimeFormatter);

		long duration = ChronoUnit.MINUTES.between(from, to);

		return duration;
	}

	public static ZonedDateTime toZonedDateTime(String dateString) {
		ZonedDateTime zonedDateTime = zonedDateTimeCache.getIfPresent(dateString);

		if (zonedDateTime == null) {
			zonedDateTime = ZonedDateTime.now();

			try {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

				zonedDateTime = ZonedDateTime.parse(dateString, dateTimeFormatter);

				zonedDateTimeCache.put(dateString, zonedDateTime);
			} catch (Exception e) {
				LogHelper.error(DateUtils.class, e);
			}
		}

		return zonedDateTime;
	}

	public static List<LocalDate> datesBetween(LocalDate from, LocalDate to) {
		List<LocalDate> dates = new ArrayList<LocalDate>();

		LocalDate startDate = LocalDate.from(from);
		while ((startDate.isBefore(to)) || (startDate.isEqual(to))) {
			dates.add(startDate);
			startDate = startDate.plusDays(1);
		}

		return dates;
	}

	public static String transform(String dateString, String inputFormat, String outputFormat) {
		LocalDate localDate = parse(dateString, inputFormat);
		String transformed = format(localDate, outputFormat);

		return transformed;
	}

	public static String transform(String dateString, DateTimeFormatter inputFormatter, String outputFormat) {
		LocalDate localDate = parse(dateString, inputFormatter);
		String transformed = format(localDate, outputFormat);

		return transformed;
	}

	public static String transformTime(String dateString, DateTimeFormatter inputFormatter, String outputFormat) {
		LocalDateTime localDate = LocalDateTime.parse(dateString, inputFormatter);
		String transformed = format(localDate, outputFormat);

		return transformed;
	}

	public static LocalDate parse(String dateString, String format) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
		LocalDate localDate = LocalDate.parse(dateString, dateTimeFormatter);

		return localDate;

	}

	public static LocalDate parse(String dateString, DateTimeFormatter dateTimeFormatter) {

		LocalDate localDate = LocalDate.parse(dateString, dateTimeFormatter);

		return localDate;

	}

	public static LocalDateTime parseTime(String dateString, DateTimeFormatter dateTimeFormatter) {

		LocalDateTime localDate = LocalDateTime.parse(dateString, dateTimeFormatter);

		return localDate;

	}

	public static String format(Temporal temporal, String format) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
		String formatted = dateTimeFormatter.format(temporal);

		return formatted;

	}

}
