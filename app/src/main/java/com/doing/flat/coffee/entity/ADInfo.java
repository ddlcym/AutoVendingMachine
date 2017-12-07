package com.doing.flat.coffee.entity;

/**
 * 描述：广告信息</br>
 */
public class ADInfo {

	private long id;
	private String rule_id;//策略ID
    private String resource_id;//资源ID
    private String file_key;//资源唯一标识key
	private String file_url = "";//资源下载路径
	private String file_hash = "";//资源hash校验码
	private	String content = "";
	private	String file_type = ""; //1 image   2  video
	private	String remain_time="";//显示时间
	private	String is_delete="";//资源是否被删除，0否1是
	private	String pathUrl;//本地地址
	private	String download_number;//下载次数
	private String is_success;//是否下载成功，  1是校验成功，0是失败，  -1的是没下
	private String weight;//体重


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPathUrl() {
		return pathUrl;
	}

	public void setPathUrl(String pathUrl) {
		this.pathUrl = pathUrl;
	}

	public String getResource_id() {
		return resource_id;
	}

	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}

	public String getFile_key() {
		return file_key;
	}

	public void setFile_key(String file_key) {
		this.file_key = file_key;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public String getFile_hash() {
		return file_hash;
	}

	public void setFile_hash(String file_hash) {
		this.file_hash = file_hash;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getRemain_time() {
		return remain_time;
	}

	public void setRemain_time(String remain_time) {
		this.remain_time = remain_time;
	}

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public String getDownload_number() {
		return download_number;
	}

	public void setDownload_number(String download_number) {
		this.download_number = download_number;
	}

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	public String getRule_id() {
		return rule_id;
	}

	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
}
