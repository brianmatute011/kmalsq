package mlix.global.kmal.model.value;
import lombok.NonNull;
import mlix.global.kmal.exception.KmalsqException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

//Date value doc
/**
 * Represents a date value in the rules.
 * <p>
 * The value can be a single date, a range of dates or a list of dates.
 * </p>
 */
public class DateValue extends Value<Long> {

    /**
     * Date format
     */
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("[dd/MM/yyyy-HH:mm:ss][dd/MM/yyyy]");

    /**
     * Constructor.
     * @param value Real value.
     */
    public DateValue(@NonNull Long value) {
        super("datetime", value, null, null, null);
    }

    /**
     * Constructor.
     * @param date Real value.
     */
    public DateValue(@NonNull String date) {
        this(getDateNumber(date, true));
    }

    /**
     * Constructor.
     * @param value List of values.
     */
    public DateValue(@NonNull List<Long> value) {
        super("datetime", null, null, null, value);
    }

    /**
     * Constructor.
     * @param from If it is not null, the value is a range and this is its start.
     * @param to If it is not null, the value is a range and this is its end.
     */
    public DateValue(@NonNull Long from, @NonNull Long to) {
        super("datetime", null, from, to, null);
    }

    /**
     * Constructor.
     * @param from If it is not null, the value is a range and this is its start.
     * @param to If it is not null, the value is a range and this is its end.
     */
    public DateValue(@NonNull String from, @NonNull String to) {
        this(getDateNumber(from, true), getDateNumber(to, false));
    }

    /**
     * Get the date and hour number.
     * @param year Year.
     * @param month Month.
     * @param day Day.
     * @param hour Hour.
     * @param minutes Minutes.
     * @param seconds Seconds.
     * @return Date and hour number.
     */
    public static Long getDateAndHourNumber(int year, int month, int day, int hour, int minutes, int seconds) {
        LocalDateTime date = LocalDateTime.of(year, month, day, hour, minutes, seconds);

        return Timestamp.valueOf(date).getTime();
    }

    /**
     * Get the date number.
     * @param year Year.
     * @param month Month.
     * @param day Day.
     * @param start If it is the start of the day.
     * @return Date number.
     */
    public static Long getDateNumber(int year, int month, int day, boolean start) {
        LocalDateTime date = LocalDateTime.of(year, month, day, 23, 59, 59);

        if (start) date = date.withHour(0).withMinute(0).withSecond(0);

        return Timestamp.valueOf(date).getTime();
    }

    /**
     * Get the date number.
     * @param date Date.
     * @param start If it is the start of the day.
     * @return Date number.
     */
    public static Long getDateNumber(String date, boolean start) {
        try {
            dateFormat.parse(date);

            String[] values = date.split("-");

            if (values.length > 1)  {
                // Se especifica en la cadena, las horas
                String[] dateValues = values[0].split("/");
                String[] hoursValues = values[1].split(":");

                return getDateAndHourNumber(Integer.parseInt(dateValues[2]), Integer.parseInt(dateValues[1]),
                        Integer.parseInt(dateValues[0]), Integer.parseInt(hoursValues[0]),
                        Integer.parseInt(hoursValues[1]), Integer.parseInt(hoursValues[0]));
            } else {
                String[] dateValues = date.split("/");
                return getDateNumber(Integer.parseInt(dateValues[2]), Integer.parseInt(dateValues[1]),
                        Integer.parseInt(dateValues[0]), start);
            }

        } catch (DateTimeParseException e) {
            throw new KmalsqException("Date format is not valid. Use dd/MM/yyyy-HH:mm:ss or dd/MM/yyyy.");
        }
    }

    /**
     * Get Date Start
     * @return Date Start
     */
    public Long getDateStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(this.value);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime().getTime();
    }

    /**
     * Get Date End
     * @return Date End
     */
    public Long getDateEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(this.value);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        return calendar.getTime().getTime();
    }
}