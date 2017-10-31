package com.witiot.cloudbox.model;

import java.util.List;

/**
 * Created by lixin on 2017/8/23.
 */

public class BuyFlowRqs  extends BaseRqs{
    private DatBean dat;

    public DatBean getDat() {
        return dat;
    }

    public void setDat(DatBean dat) {
        this.dat = dat;
    }

    public static class DatBean {

           public String  orderId;// "201708221109001",
            public String  orderType;// "0",
            public String  deviceId;// "68c9c161acb45a30e47faaa2c2a4c723",
            public String  beginTime;// "2017-08-22 00;//00;//00",
            public String  endTime;// "2017-08-26 23;//59;//59",
            public String  orginalAmount;// "0",
            public String  discount;// "0",
            public String  amount;// "0",
            public String  status;// "1",
            public String   customerId;// "15910593998",
            public List<Detail> detail;

            public String getOrderId() {
                return orderId;
            }
            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getOrderType() {
                return orderType;
            }
            public void setOrderType(String orderType) {
                this.orderType = orderType;
            }

            public String getDeviceId() {
                return deviceId;
            }
            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getBeginTime() {
                return beginTime;
            }
            public void setBeginTime(String beginTime) {
                this.beginTime = beginTime;
            }

            public String getEndTime() {
                return endTime;
            }
            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getOrginalAmount() {
                return orginalAmount;
            }
            public void setOrginalAmount(String orginalAmount) {
                this.orginalAmount = orginalAmount;
            }

            public String getDiscount() {
                return discount;
            }
            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getAmount() {
                return amount;
            }
            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getStatus() {
                return status;
            }
            public void setStatus(String status) {
                this.status = status;
            }

            public String getCustomerId() {
                return customerId;
            }
            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public List<Detail> getDetail() {
                return detail;
            }
            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }

            public static class Detail {
                public String   serviceId;// "s1",
                public String  price;// "0",
                public String buyCount;// "0",
                public String amount;// "0"

                public String getServiceId() {
                    return serviceId;
                }
                public void setServiceId(String serviceId) {
                    this.serviceId = serviceId;
                }

                public String getPrice() {
                    return price;
                }
                public void setPrice(String price) {
                    this.price = price;
                }

                public String getBuyCount() {
                    return buyCount;
                }
                public void setBuyCount(String buyCount) {
                    this.buyCount = buyCount;
                }

                public String getAmount() {
                    return amount;
                }
                public void setAmount(String amount) {
                    this.amount = amount;
                }
            }
        }

}
