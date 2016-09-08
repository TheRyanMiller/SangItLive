
package com.rtmillerprojects.sangitlive.model.musicbrainzartistresourcemodel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relation {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("target-type")
    @Expose
    private String targetType;
    @SerializedName("type-id")
    @Expose
    private String typeId;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("ended")
    @Expose
    private Boolean ended;
    @SerializedName("attribute-values")
    @Expose
    private AttributeValues attributeValues;
    @SerializedName("target-credit")
    @Expose
    private String targetCredit;
    @SerializedName("end")
    @Expose
    private Object end;
    @SerializedName("url")
    @Expose
    private Url url;
    @SerializedName("begin")
    @Expose
    private Object begin;
    @SerializedName("source-credit")
    @Expose
    private String sourceCredit;
    @SerializedName("attributes")
    @Expose
    private List<Object> attributes = new ArrayList<Object>();

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The targetType
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * 
     * @param targetType
     *     The target-type
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    /**
     * 
     * @return
     *     The typeId
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * 
     * @param typeId
     *     The type-id
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     * 
     * @return
     *     The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * 
     * @param direction
     *     The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * 
     * @return
     *     The ended
     */
    public Boolean getEnded() {
        return ended;
    }

    /**
     * 
     * @param ended
     *     The ended
     */
    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    /**
     * 
     * @return
     *     The attributeValues
     */
    public AttributeValues getAttributeValues() {
        return attributeValues;
    }

    /**
     * 
     * @param attributeValues
     *     The attribute-values
     */
    public void setAttributeValues(AttributeValues attributeValues) {
        this.attributeValues = attributeValues;
    }

    /**
     * 
     * @return
     *     The targetCredit
     */
    public String getTargetCredit() {
        return targetCredit;
    }

    /**
     * 
     * @param targetCredit
     *     The target-credit
     */
    public void setTargetCredit(String targetCredit) {
        this.targetCredit = targetCredit;
    }

    /**
     * 
     * @return
     *     The end
     */
    public Object getEnd() {
        return end;
    }

    /**
     * 
     * @param end
     *     The end
     */
    public void setEnd(Object end) {
        this.end = end;
    }

    /**
     * 
     * @return
     *     The url
     */
    public Url getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(Url url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The begin
     */
    public Object getBegin() {
        return begin;
    }

    /**
     * 
     * @param begin
     *     The begin
     */
    public void setBegin(Object begin) {
        this.begin = begin;
    }

    /**
     * 
     * @return
     *     The sourceCredit
     */
    public String getSourceCredit() {
        return sourceCredit;
    }

    /**
     * 
     * @param sourceCredit
     *     The source-credit
     */
    public void setSourceCredit(String sourceCredit) {
        this.sourceCredit = sourceCredit;
    }

    /**
     * 
     * @return
     *     The attributes
     */
    public List<Object> getAttributes() {
        return attributes;
    }

    /**
     * 
     * @param attributes
     *     The attributes
     */
    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }

}
