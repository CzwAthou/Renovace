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

import java.io.Serializable;

public class StoryListItemBean implements Serializable {
	private String st_id;
	private String st_title;
	private String st_area;
	private String st_face;
	private int st_class;
	private long st_start;
	private long st_end;
	private int st_sort;
	private long st_utime;
	private int st_status;

	public String getSt_id() {
		return st_id;
	}

	public void setSt_id(String st_id) {
		this.st_id = st_id;
	}

	public String getSt_title() {
		return st_title;
	}

	public void setSt_title(String st_title) {
		this.st_title = st_title;
	}

	public String getSt_area() {
		return st_area;
	}

	public void setSt_area(String st_area) {
		this.st_area = st_area;
	}

	public String getSt_face() {
		return st_face;
	}

	public void setSt_face(String st_face) {
		this.st_face = st_face;
	}

	public int getSt_class() {
		return st_class;
	}

	public void setSt_class(int st_class) {
		this.st_class = st_class;
	}

	public long getSt_start() {
		return st_start;
	}

	public void setSt_start(long st_start) {
		this.st_start = st_start;
	}

	public long getSt_end() {
		return st_end;
	}

	public void setSt_end(long st_end) {
		this.st_end = st_end;
	}

	public int getSt_sort() {
		return st_sort;
	}

	public void setSt_sort(int st_sort) {
		this.st_sort = st_sort;
	}

	public long getSt_utime() {
		return st_utime;
	}

	public void setSt_utime(long st_utime) {
		this.st_utime = st_utime;
	}

	public int getSt_status() {
		return st_status;
	}

	public void setSt_status(int st_status) {
		this.st_status = st_status;
	}
}
