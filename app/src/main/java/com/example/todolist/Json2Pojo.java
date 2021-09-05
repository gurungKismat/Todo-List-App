package com.example.todolist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Json2Pojo implements Parcelable {

    private Boolean correct;
    private List<String> suggestions = null;
    private String word;

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.correct);
        dest.writeStringList(this.suggestions);
        dest.writeString(this.word);
    }

    public void readFromParcel(Parcel source) {
        this.correct = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.suggestions = source.createStringArrayList();
        this.word = source.readString();
    }

    public Json2Pojo() {
    }

    protected Json2Pojo(Parcel in) {
        this.correct = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.suggestions = in.createStringArrayList();
        this.word = in.readString();
    }

    public static final Creator<Json2Pojo> CREATOR = new Creator<Json2Pojo>() {
        @Override
        public Json2Pojo createFromParcel(Parcel source) {
            return new Json2Pojo(source);
        }

        @Override
        public Json2Pojo[] newArray(int size) {
            return new Json2Pojo[size];
        }
    };
}
