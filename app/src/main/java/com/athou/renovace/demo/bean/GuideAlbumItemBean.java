/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.athou.renovace.demo.bean;

public class GuideAlbumItemBean{

	private String al_id;
	private String user_id;
	private String al_name;
	private String al_face;
	private int al_type;
	private int al_count;
	private String al_sort;
	private String al_utime;
	private int al_status;

	public GuideAlbumItemBean(String albumId) {
		this.al_id = albumId;
	}

	public String getAl_id() {
		return al_id;
	}

	public void setAl_id(String al_id) {
		this.al_id = al_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getAl_name() {
		return al_name;
	}

	public void setAl_name(String al_name) {
		this.al_name = al_name;
	}

	public String getAl_face() {
		return al_face;
	}

	public void setAl_face(String al_face) {
		this.al_face = al_face;
	}

	public int getAl_type() {
		return al_type;
	}

	public void setAl_type(int al_type) {
		this.al_type = al_type;
	}

	public int getAl_status() {
		return al_status;
	}

	public void setAl_status(int al_status) {
		this.al_status = al_status;
	}

	public int getAl_count() {
		return al_count;
	}

	public void setAl_count(int al_count) {
		this.al_count = al_count;
	}

	public String getAl_sort() {
		return al_sort;
	}

	public void setAl_sort(String al_sort) {
		this.al_sort = al_sort;
	}

	public String getAl_utime() {
		return al_utime;
	}

	public void setAl_utime(String al_utime) {
		this.al_utime = al_utime;
	}

}
