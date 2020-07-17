package me.micrusa.amaztimer.trainings.database.lists.converter;

import com.dbflow5.converter.TypeConverter;
import com.google.gson.Gson;

import me.micrusa.amaztimer.trainings.database.lists.HrDataList;
import me.micrusa.amaztimer.trainings.database.lists.LapList;

public class HrDataConverter extends TypeConverter<String, HrDataList> {

    @Override
    public String getDBValue(HrDataList model) {
        return new Gson().toJson(model);
    }

    @Override
    public HrDataList getModelValue(String data) {
        return new Gson().fromJson(data, HrDataList.class);
    }

}
