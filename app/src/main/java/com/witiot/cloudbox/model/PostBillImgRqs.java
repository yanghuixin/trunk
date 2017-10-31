package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class PostBillImgRqs extends BaseRqs{

    private PostBillImgRqs.DatBean dat;

    public PostBillImgRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(PostBillImgRqs.DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

//        private DatBean.Paramlist paramlist;
//
//
//        public DatBean.Paramlist getParamlist() {
//            return paramlist;
//        }
//
//        public void setParamlist(DatBean.Paramlist paramlist) {
//            this.paramlist = paramlist;
//        }
//
//
//        public static class Paramlist {
            public String savePath;
            public String dataImg;

        public String getDataImg() {
            return dataImg;
        }

        public void setDataImg(String dataImg) {
            this.dataImg = dataImg;
        }

        public String getSavePath() {
            return savePath;
        }

        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }
    }
//    }

}
