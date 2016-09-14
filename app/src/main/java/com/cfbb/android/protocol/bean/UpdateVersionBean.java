package com.cfbb.android.protocol.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author MrChang
 *         created  at  2015/12/11.
 * @description
 */
public class UpdateVersionBean {


    @SerializedName("version_code")
    public int version_code;

    @SerializedName("version_desc")
    public String version_desc;

    @SerializedName("url")
    public String url;

    @SerializedName("force_update")
    public int force_update;

}
