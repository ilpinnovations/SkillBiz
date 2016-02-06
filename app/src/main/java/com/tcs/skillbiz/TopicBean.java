package com.tcs.skillbiz;

/**
 * Created by 966893 on 1/23/2016.
 */
public class TopicBean
{

    int id, quizID;
    String name,audioURL,videoURL;

    public TopicBean(int id, String name, String audioURL, String videoURL, int quizID) {
        this.id = id;
        this.name = name;
        this.audioURL = audioURL;
        this.videoURL = videoURL;
        this.quizID = quizID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }


}
