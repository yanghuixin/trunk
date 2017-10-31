package com.witiot.cloudbox.model;

/**
 * Created by lixin on 2017/8/14.
 */
public class PostAvatarRqs extends BaseRqs{

    private PostAvatarRqs.DatBean dat;

    public PostAvatarRqs.DatBean getDat() {
        return dat;
    }

    public void setDat(PostAvatarRqs.DatBean dat) {
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
            public String customerId;
            public String dataImg;

        public String getDataImg() {
            return dataImg;
        }

        public void setDataImg(String dataImg) {
            this.dataImg = dataImg;
        }

        public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            }
//    }

}
