package com.witiot.cloudbox.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class ApplicationBean {
	public String pkgName;
	public String appName;
	public Drawable appIcon;
	public Intent appIntent;
	public Long rx;		// 接收
	public Long tx;		// 上传

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getAppNme() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public Intent getAppIntent() {
		return appIntent;
	}

	public void setAppIntent(Intent appIntent) {
		this.appIntent = appIntent;
	}

	public long getTx() {
		return tx;
	}

	public void setTx(long tx) {
		this.tx = tx;
	}

	public long getRx() {
		return rx;
	}

	public void setRx(long rx) {
		this.rx = rx;
	}
}

