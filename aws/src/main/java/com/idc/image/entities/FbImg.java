package com.idc.image.entities;

import com.amazonaws.services.rekognition.model.Label;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idc.image.utils.ImgUtils;

import java.nio.ByteBuffer;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FbImg {

    private ByteBuffer img;

    @JsonProperty("added")
    private String added;

    @JsonProperty("id")
    private String id;

    @JsonProperty("url")
    private String url;

    private List<Label> labels;

    public ByteBuffer getImg() {
        if(this.img == null) setImg();
        return img;
    }

    public void setImg() {
        this.img = new ImgUtils().imgUrlToByte(this.url);
    }

    public void setImg(ByteBuffer img) {
        this.img = img;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }
}
