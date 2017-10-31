package com.witiot.cloudbox.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lixin on 2017/9/16.
 */

public class ChangeJfListRes extends BaseRes{

    /**
     * dat : {"pageindex":1,"total":4,"pageSize":10,"pages":1,"rows":[{"id":3,"customerId":"13716543250","deviceId":"f8579312f7cf88bc","billNo":"f8579312f7cf88bc20170916203248","uploadTime":"2017-09-16 20:32:49","uploadPath":"bill/13716543250//730d3eb601294a759ad9813ce99af048.jpg","patientName":"张三","inspectionFee":123,"treatmentFee":456,"hospitalFee":789,"materialFee":10,"drugFee":12456,"fee1":null,"fee2":null,"fee3":null,"fee4":null,"fee5":null,"totalFee":null,"status":0,"score":null,"failReason":null,"verifyTime":null,"verifyId":null,"msg":null,"customerName":"李","totalScore":0,"surplusScore":0,"province":null,"city":null,"district":null,"hospitalId":null},{"id":4,"customerId":"13716543250","deviceId":"f8579312f7cf88bc","billNo":"f8579312f7cf88bc20170916211146","uploadTime":"2017-09-16 21:11:47","uploadPath":"bill/13716543250//61692a2626f041d888241f6b66c081a5.jpg","patientName":"凤凰","inspectionFee":125,"treatmentFee":285,"hospitalFee":486,"materialFee":555,"drugFee":5855,"fee1":null,"fee2":null,"fee3":null,"fee4":null,"fee5":null,"totalFee":null,"status":0,"score":null,"failReason":null,"verifyTime":null,"verifyId":null,"msg":null,"customerName":"李","totalScore":0,"surplusScore":0,"province":null,"city":null,"district":null,"hospitalId":null},{"id":5,"customerId":"13716543250","deviceId":"f8579312f7cf88bc","billNo":"f8579312f7cf88bc20170916212310","uploadTime":"2017-09-16 21:23:11","uploadPath":"bill/13716543250//1ea1f77da81b46309c8b9b8dab69c2bf.jpg","patientName":"分公司","inspectionFee":254,"treatmentFee":485588,"hospitalFee":558,"materialFee":588,"drugFee":588,"fee1":null,"fee2":null,"fee3":null,"fee4":null,"fee5":null,"totalFee":null,"status":0,"score":null,"failReason":null,"verifyTime":null,"verifyId":null,"msg":null,"customerName":"李","totalScore":0,"surplusScore":0,"province":null,"city":null,"district":null,"hospitalId":null},{"id":6,"customerId":"13716543250","deviceId":"f8579312f7cf88bc","billNo":"f8579312f7cf88bc20170916212416","uploadTime":"2017-09-16 21:24:17","uploadPath":"bill/13716543250//d1265ccd9568436b881724d352679c9a.jpg","patientName":"公开","inspectionFee":684,"treatmentFee":46825,"hospitalFee":488,"materialFee":59,"drugFee":58,"fee1":null,"fee2":null,"fee3":null,"fee4":null,"fee5":null,"totalFee":null,"status":0,"score":null,"failReason":null,"verifyTime":null,"verifyId":null,"msg":null,"customerName":"李","totalScore":0,"surplusScore":0,"province":null,"city":null,"district":null,"hospitalId":null}]}
     */

    private DatBean dat;

    public DatBean getDat() {
        return dat;
    }

    public void setDat(DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {
        /**
         * pageindex : 1
         * total : 4
         * pageSize : 10
         * pages : 1
         * rows : [{"id":3,"customerId":"13716543250","deviceId":"f8579312f7cf88bc","billNo":"f8579312f7cf88bc20170916203248","uploadTime":"2017-09-16 20:32:49","uploadPath":"bill/13716543250//730d3eb601294a759ad9813ce99af048.jpg","patientName":"张三","inspectionFee":123,"treatmentFee":456,"hospitalFee":789,"materialFee":10,"drugFee":12456,"fee1":null,"fee2":null,"fee3":null,"fee4":null,"fee5":null,"totalFee":null,"status":0,"score":null,"failReason":null,"verifyTime":null,"verifyId":null,"msg":null,"customerName":"李","totalScore":0,"surplusScore":0,"province":null,"city":null,"district":null,"hospitalId":null},{"id":4,"customerId":"13716543250","deviceId":"f8579312f7cf88bc","billNo":"f8579312f7cf88bc20170916211146","uploadTime":"2017-09-16 21:11:47","uploadPath":"bill/13716543250//61692a2626f041d888241f6b66c081a5.jpg","patientName":"凤凰","inspectionFee":125,"treatmentFee":285,"hospitalFee":486,"materialFee":555,"drugFee":5855,"fee1":null,"fee2":null,"fee3":null,"fee4":null,"fee5":null,"totalFee":null,"status":0,"score":null,"failReason":null,"verifyTime":null,"verifyId":null,"msg":null,"customerName":"李","totalScore":0,"surplusScore":0,"province":null,"city":null,"district":null,"hospitalId":null},{"id":5,"customerId":"13716543250","deviceId":"f8579312f7cf88bc","billNo":"f8579312f7cf88bc20170916212310","uploadTime":"2017-09-16 21:23:11","uploadPath":"bill/13716543250//1ea1f77da81b46309c8b9b8dab69c2bf.jpg","patientName":"分公司","inspectionFee":254,"treatmentFee":485588,"hospitalFee":558,"materialFee":588,"drugFee":588,"fee1":null,"fee2":null,"fee3":null,"fee4":null,"fee5":null,"totalFee":null,"status":0,"score":null,"failReason":null,"verifyTime":null,"verifyId":null,"msg":null,"customerName":"李","totalScore":0,"surplusScore":0,"province":null,"city":null,"district":null,"hospitalId":null},{"id":6,"customerId":"13716543250","deviceId":"f8579312f7cf88bc","billNo":"f8579312f7cf88bc20170916212416","uploadTime":"2017-09-16 21:24:17","uploadPath":"bill/13716543250//d1265ccd9568436b881724d352679c9a.jpg","patientName":"公开","inspectionFee":684,"treatmentFee":46825,"hospitalFee":488,"materialFee":59,"drugFee":58,"fee1":null,"fee2":null,"fee3":null,"fee4":null,"fee5":null,"totalFee":null,"status":0,"score":null,"failReason":null,"verifyTime":null,"verifyId":null,"msg":null,"customerName":"李","totalScore":0,"surplusScore":0,"province":null,"city":null,"district":null,"hospitalId":null}]
         */

        private int pageindex;
        private int total;
        private int pageSize;
        private int pages;
        private List<RowsBean> rows;

        public int getPageindex() {
            return pageindex;
        }

        public void setPageindex(int pageindex) {
            this.pageindex = pageindex;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * id : 3
             * customerId : 13716543250
             * deviceId : f8579312f7cf88bc
             * billNo : f8579312f7cf88bc20170916203248
             * uploadTime : 2017-09-16 20:32:49
             * uploadPath : bill/13716543250//730d3eb601294a759ad9813ce99af048.jpg
             * patientName : 张三
             * inspectionFee : 123.0
             * treatmentFee : 456.0
             * hospitalFee : 789.0
             * materialFee : 10.0
             * drugFee : 12456.0
             * fee1 : null
             * fee2 : null
             * fee3 : null
             * fee4 : null
             * fee5 : null
             * totalFee : null
             * status : 0
             * score : null
             * failReason : null
             * verifyTime : null
             * verifyId : null
             * msg : null
             * customerName : 李
             * totalScore : 0.0
             * surplusScore : 0.0
             * province : null
             * city : null
             * district : null
             * hospitalId : null
             */

            private int id;
            private String customerId;
            private String deviceId;
            private String billNo;
            private String uploadTime;
            private String uploadPath;
            private String patientName;
            private String inspectionFee;
            private String treatmentFee;
            private String hospitalFee;
            private String materialFee;
            private String drugFee;
            private String fee1;
            private String fee2;
            private String fee3;
            private String fee4;
            private String fee5;
            private String totalFee;
            private int status;
            private String score;
            private String failReason;
            private String verifyTime;
            private String verifyId;
            @SerializedName("msg")
            private String msgX;
            private String customerName;
            private String totalScore;
            private String surplusScore;
            private String province;
            private String city;
            private String district;
            private String hospitalId;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getBillNo() {
                return billNo;
            }

            public void setBillNo(String billNo) {
                this.billNo = billNo;
            }

            public String getUploadTime() {
                return uploadTime;
            }

            public void setUploadTime(String uploadTime) {
                this.uploadTime = uploadTime;
            }

            public String getUploadPath() {
                return uploadPath;
            }

            public void setUploadPath(String uploadPath) {
                this.uploadPath = uploadPath;
            }

            public String getPatientName() {
                return patientName;
            }

            public void setPatientName(String patientName) {
                this.patientName = patientName;
            }

            public String getInspectionFee() {
                return inspectionFee;
            }

            public void setInspectionFee(String inspectionFee) {
                this.inspectionFee = inspectionFee;
            }

            public String getTreatmentFee() {
                return treatmentFee;
            }

            public void setTreatmentFee(String treatmentFee) {
                this.treatmentFee = treatmentFee;
            }

            public String getHospitalFee() {
                return hospitalFee;
            }

            public void setHospitalFee(String hospitalFee) {
                this.hospitalFee = hospitalFee;
            }

            public String getMaterialFee() {
                return materialFee;
            }

            public void setMaterialFee(String materialFee) {
                this.materialFee = materialFee;
            }

            public String getDrugFee() {
                return drugFee;
            }

            public void setDrugFee(String drugFee) {
                this.drugFee = drugFee;
            }

            public String getFee1() {
                return fee1;
            }

            public void setFee1(String fee1) {
                this.fee1 = fee1;
            }

            public String getFee2() {
                return fee2;
            }

            public void setFee2(String fee2) {
                this.fee2 = fee2;
            }

            public String getFee3() {
                return fee3;
            }

            public void setFee3(String fee3) {
                this.fee3 = fee3;
            }

            public String getFee4() {
                return fee4;
            }

            public void setFee4(String fee4) {
                this.fee4 = fee4;
            }

            public String getFee5() {
                return fee5;
            }

            public void setFee5(String fee5) {
                this.fee5 = fee5;
            }

            public String getTotalFee() {
                return totalFee;
            }

            public void setTotalFee(String totalFee) {
                this.totalFee = totalFee;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getFailReason() {
                return failReason;
            }

            public void setFailReason(String failReason) {
                this.failReason = failReason;
            }

            public String getVerifyTime() {
                return verifyTime;
            }

            public void setVerifyTime(String verifyTime) {
                this.verifyTime = verifyTime;
            }

            public String getVerifyId() {
                return verifyId;
            }

            public void setVerifyId(String verifyId) {
                this.verifyId = verifyId;
            }

            public String getMsgX() {
                return msgX;
            }

            public void setMsgX(String msgX) {
                this.msgX = msgX;
            }

            public String getCustomerName() {
                return customerName;
            }

            public void setCustomerName(String customerName) {
                this.customerName = customerName;
            }

            public String getTotalScore() {
                return totalScore;
            }

            public void setTotalScore(String totalScore) {
                this.totalScore = totalScore;
            }

            public String getSurplusScore() {
                return surplusScore;
            }

            public void setSurplusScore(String surplusScore) {
                this.surplusScore = surplusScore;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getHospitalId() {
                return hospitalId;
            }

            public void setHospitalId(String hospitalId) {
                this.hospitalId = hospitalId;
            }
        }
    }
}
