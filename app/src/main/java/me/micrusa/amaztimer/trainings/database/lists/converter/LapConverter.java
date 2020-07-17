package me.micrusa.amaztimer.trainings.database.lists.converter;

import com.dbflow5.converter.TypeConverter;
import com.google.gson.Gson;

import me.micrusa.amaztimer.trainings.database.lists.LapList;

public class LapConverter extends TypeConverter<String, LapList> {

    @Override
    public String getDBValue(LapList model) {
        return new Gson().toJson(model);
    }

    @Override
    public LapList getModelValue(String data) {
        return new Gson().fromJson(data, LapList.class);
    }

}
