package dao;

import java.util.List;

public class Page<T> {
	int pageNo;
	int pageSize;
	
	int totalCount;
	
	List<T> records;

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<T> getRecords() {
		return records;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
