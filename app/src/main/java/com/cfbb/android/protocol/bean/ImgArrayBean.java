package com.cfbb.android.protocol.bean;

import java.util.List;

/**
 * @author MrChang45
 * @time 2017/1/17
 * @desc
 */
public class ImgArrayBean {
    public String name;
    public List<ImgData> imgDatas;
    public void setName(String name) {
        this.name = name;
    }

    public void setImgDatas(List<ImgData> imgDatas) {
        this.imgDatas = imgDatas;
    }
}
