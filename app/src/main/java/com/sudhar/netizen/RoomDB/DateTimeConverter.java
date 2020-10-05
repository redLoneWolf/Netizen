package com.sudhar.netizen.RoomDB;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


@RequiresApi(api = Build.VERSION_CODES.O)
public class DateTimeConverter {
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public OffsetDateTime toOffsetDateTime(String dataTimeString) {
        return OffsetDateTime.from(formatter.parse(dataTimeString));
    }

    @TypeConverter
    public String fromOffsetDateTime(OffsetDateTime offsetDateTime) {

        return offsetDateTime.format(formatter).toString();
    }

}
