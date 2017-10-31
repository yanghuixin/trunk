package com.witiot.cloudbox.model;

import android.text.format.DateUtils;

import com.witiot.cloudbox.utils.TimeUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by lixin on 2017/8/15.
 */

public class CustInfoRes extends BaseRes{

    /**
     * dat : {"id":3,"customerId":"13301135089","customerName":"lyp","customerPwd":"e10adc3949ba59abbe56e057f20f883e","mobile":"13301135089","birth":"\u201d\u201c","gendar":1,"nation":"han","profession":"LD","marriage":"已婚","insurance":"新农合","province":"\u201d\u201c","city":"\u201d\u201c","district":"\u201d\u201c","hospitalId":"\u201d\u201c","department":"\u201d\u201c","pastIllness":"jws","presentIllness":"xbs","complaint":"zs","diagnosis":"zd","surgery":"小手术","surgeryTime":"2017-08-15 11:04:56","hospitalDate":"2017-08-15 11:04:56","hospitalDays":8,"deviceId":"\u201d\u201c","openid":"\u201d\u201c","nickname":"\u201d\u201c","subscribe":0,"subscribeTime":"\u201d\u201c","headimgurl":"\u201d\u201c","deviceToken":"c63d71cfad908ea7","clientId":"c63d71cfad908ea7","token":"25aea1dc6c5a482bb6a9d861e1d64869","tokenTime":"2017-08-15 10:59:47","status":1,"msg":"\u201d\u201c","createTime":"2017-08-03 08:38:54","createId":"\u201d\u201c","updateTime":"\u201d\u201c","updateId":"\u201d\u201c","msg1":"\u201d\u201c","msg2":"\u201d\u201c","msg3":"\u201d\u201c","msg4":"\u201d\u201c","msg5":"\u201d\u201c","totalAmount":0,"surplusScore":0,"totalScore":0,"totalFlow":0,"surplusFlow":0,"totalDays":"\u201d\u201c","surplusDays":"\u201d\u201c","actualFlow":"\u201d\u201c","actualDays":"\u201d\u201c"}
     */

    private CustInfoBean dat;

    public CustInfoBean getDat() {
        return dat;
    }

    public void setDat(CustInfoBean dat) {
        this.dat = dat;
    }

    public static class CustInfoBean implements Serializable{
        private Rows rows;

        public Rows getRows() {
            return rows;
        }

        public void setRows(Rows rows) {
            this.rows = rows;
        }

        public static class Rows implements Serializable{
            /**
             * id : 3
             * customerId : 13301135089
             * customerName : lyp
             * customerPwd : e10adc3949ba59abbe56e057f20f883e
             * mobile : 13301135089
             * birth : ”“
             * gendar : 1
             * nation : han
             * profession : LD
             * marriage : 已婚
             * insurance : 新农合
             * province : ”“
             * city : ”“
             * district : ”“
             * hospitalId : ”“
             * department : ”“
             * pastIllness : jws
             * presentIllness : xbs
             * complaint : zs
             * diagnosis : zd
             * surgery : 小手术
             * surgeryTime : 2017-08-15 11:04:56
             * hospitalDate : 2017-08-15 11:04:56
             * hospitalDays : 8
             * deviceId : ”“
             * openid : ”“
             * nickname : ”“
             * subscribe : 0
             * subscribeTime : ”“
             * headimgurl : ”“
             * deviceToken : c63d71cfad908ea7
             * clientId : c63d71cfad908ea7
             * token : 25aea1dc6c5a482bb6a9d861e1d64869
             * tokenTime : 2017-08-15 10:59:47
             * status : 1
             * msg : ”“
             * createTime : 2017-08-03 08:38:54
             * createId : ”“
             * updateTime : ”“
             * updateId : ”“
             * msg1 : ”“
             * msg2 : ”“
             * msg3 : ”“
             * msg4 : ”“
             * msg5 : ”“
             * totalAmount : 0
             * surplusScore : 0
             * totalScore : 0
             * totalFlow : 0
             * surplusFlow : 0 //剩余流量
             * totalDays : ”“
             * surplusDays : ”“
             * actualFlow : ”“
             * actualDays : ”“
             */

            private int id;
            private String customerId;
            private String customerName;
            private String customerPwd;
            private String mobile;
            private String birth;
            private int gendar;
            private String nation;
            private String profession;
            private String marriage;
            private String insurance;
            private String province;
            private String city;
            private String district;
            private String hospitalId;
            private String department;
            private String pastIllness;
            private String presentIllness;
            private String complaint;
            private String diagnosis;
            private String surgery;
            private String surgeryTime;
            private String hospitalDate;
            private int hospitalDays;
            private String deviceId;
            private String openid;
            private String nickname;
            private int subscribe;
            private String subscribeTime;
            private String headimgurl;
            private String deviceToken;
            private String clientId;
            private String token;
            private String tokenTime;
            private int status;
            private String msg;
            private String createTime;
            private String createId;
            private String updateTime;
            private String updateId;
            private String msg1;
            private String msg2;
            private String msg3;
            private String msg4;
            private String msg5;

            private double totalAmount;

            private int surplusScore;
            private int totalScore;

            private double totalFlow;
            private double surplusFlow;
            private double actualFlow;

            private int totalDays;
            private int surplusDays;
            private int actualDays;

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

            public String getCustomerName() {
                return customerName;
            }
            public void setCustomerName(String customerName) {
                this.customerName = customerName;
            }

            public String getCustomerPwd() {
                return customerPwd;
            }
            public void setCustomerPwd(String customerPwd) {
                this.customerPwd = customerPwd;
            }

            public String getMobile() {
                return mobile;
            }
            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getBirth() {
                //return birth;
                if(birth == null){
                    return null;
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
                return df.format(TimeUtils.string2Date(birth));
            }
            public void setBirth(String birth) {
                this.birth = birth;
            }

            public int getGendar() {
                return gendar;
            }
            public void setGendar(int gendar) {
                this.gendar = gendar;
            }

            public String getNation() {
                return nation;
            }
            public void setNation(String nation) {
                this.nation = nation;
            }

            public String getProfession() {
                return profession;
            }
            public void setProfession(String profession) {
                this.profession = profession;
            }

            public String getMarriage() {
                return marriage;
            }
            public void setMarriage(String marriage) {
                this.marriage = marriage;
            }

            public String getInsurance() {
                return insurance;
            }
            public void setInsurance(String insurance) {
                this.insurance = insurance;
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

            public String getDepartment() {
                return department;
            }
            public void setDepartment(String department) {
                this.department = department;
            }

            public String getPastIllness() {
                return pastIllness;
            }
            public void setPastIllness(String pastIllness) {
                this.pastIllness = pastIllness;
            }

            public String getPresentIllness() {
                return presentIllness;
            }
            public void setPresentIllness(String presentIllness) {
                this.presentIllness = presentIllness;
            }

            public String getComplaint() {
                return complaint;
            }
            public void setComplaint(String complaint) {
                this.complaint = complaint;
            }

            public String getDiagnosis() {
                return diagnosis;
            }
            public void setDiagnosis(String diagnosis) {
                this.diagnosis = diagnosis;
            }

            public String getSurgery() {
                return surgery;
            }
            public void setSurgery(String surgery) {
                this.surgery = surgery;
            }

            public String getSurgeryTime() {
                //return surgeryTime;
                if(surgeryTime == null){
                    return null;
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
                return df.format(TimeUtils.string2Date(surgeryTime));
            }
            public void setSurgeryTime(String surgeryTime) {
                this.surgeryTime = surgeryTime;
            }

            public String getHospitalDate() {
                //return hospitalDate;
                if(hospitalDate == null){
                    return null;
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
                return df.format(TimeUtils.string2Date(hospitalDate));
            }
            public void setHospitalDate(String hospitalDate) {
                this.hospitalDate = hospitalDate;
            }

            public int getHospitalDays() {
                return hospitalDays;
            }
            public void setHospitalDays(int hospitalDays) {
                this.hospitalDays = hospitalDays;
            }

            public String getDeviceId() {
                return deviceId;
            }
            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getOpenid() {
                return openid;
            }
            public void setOpenid(String openid) {
                this.openid = openid;
            }

            public String getNickname() {
                return nickname;
            }
            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getSubscribe() {
                return subscribe;
            }
            public void setSubscribe(int subscribe) {
                this.subscribe = subscribe;
            }

            public String getSubscribeTime() {
                return subscribeTime;
            }
            public void setSubscribeTime(String subscribeTime) {
                this.subscribeTime = subscribeTime;
            }

            public String getHeadimgurl() {
                return headimgurl;
            }
            public void setHeadimgurl(String headimgurl) {
                this.headimgurl = headimgurl;
            }

            public String getDeviceToken() {
                return deviceToken;
            }
            public void setDeviceToken(String deviceToken) {
                this.deviceToken = deviceToken;
            }

            public String getClientId() {
                return clientId;
            }
            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getToken() {
                return token;
            }
            public void setToken(String token) {
                this.token = token;
            }

            public String getTokenTime() {
                return tokenTime;
            }
            public void setTokenTime(String tokenTime) {
                this.tokenTime = tokenTime;
            }

            public int getStatus() {
                return status;
            }
            public void setStatus(int status) {
                this.status = status;
            }

            public String getMsg() {
                return msg;
            }
            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getCreateTime() {
                return createTime;
            }
            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCreateId() {
                return createId;
            }
            public void setCreateId(String createId) {
                this.createId = createId;
            }

            public String getUpdateTime() {
                return updateTime;
            }
            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getUpdateId() {
                return updateId;
            }
            public void setUpdateId(String updateId) {
                this.updateId = updateId;
            }

            public String getMsg1() {
                return msg1;
            }
            public void setMsg1(String msg1) {
                this.msg1 = msg1;
            }

            public String getMsg2() {
                return msg2;
            }
            public void setMsg2(String msg2) {
                this.msg2 = msg2;
            }

            public String getMsg3() {
                return msg3;
            }
            public void setMsg3(String msg3) {
                this.msg3 = msg3;
            }

            public String getMsg4() {
                return msg4;
            }
            public void setMsg4(String msg4) {
                this.msg4 = msg4;
            }

            public String getMsg5() {
                return msg5;
            }
            public void setMsg5(String msg5) {
                this.msg5 = msg5;
            }


            public double getTotalAmount() {
                return totalAmount;
            }
            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }


            public int getSurplusScore() {
                return surplusScore;
            }
            public void setSurplusScore(int surplusScore) {
                this.surplusScore = surplusScore;
            }

            public int getTotalScore() {
                return totalScore;
            }
            public void setTotalScore(int totalScore) {
                this.totalScore = totalScore;
            }


            public double getTotalFlow() {
                return totalFlow;
            }
            public void setTotalFlow(double totalFlow) {
                this.totalFlow = totalFlow;
            }

            public double getSurplusFlow() {
                return surplusFlow;
            }
            public void setSurplusFlow(double surplusFlow) {
                this.surplusFlow = surplusFlow;
            }

            public double getActualFlow() {
                return actualFlow;
            }
            public void setActualFlow(double actualFlow) {
                this.actualFlow = actualFlow;
            }


            public int getTotalDays() {
                return totalDays;
            }
            public void setTotalDays(int totalDays) {
                this.totalDays = totalDays;
            }

            public int getSurplusDays() {
                return surplusDays;
            }
            public void setSurplusDays(int surplusDays) {
                this.surplusDays = surplusDays;
            }

            public int getActualDays() {
                return actualDays;
            }
            public void setActualDays(int actualDays) {
                this.actualDays = actualDays;
            }
        }

    }
}
