package kingwant.hjjp.base;

import java.io.Serializable;
import java.util.List;


public class PageMongo<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5646775605281827512L;
	// 结果集
	private List<T> datas;
	// 查询记录数
	private int totalSize;
	// 每页多少条数据
	private int pageSize = 10;
	// 第几页
	private int pageIndex = 1;
	// 跳过几条数
	private int skip = 0;
	//查询bean
	private T obj;
	
	/**
	 * 有参构造，设置当前页及每页大小以及查询条件bean
	 * @param pageIndex 当前页
	 * @param pageSize 每页大小
	 * @param obj bean对象
	 */
	public PageMongo(int pageIndex,int pageSize,T obj) {
		this.pageIndex=pageIndex;
		this.pageSize=pageSize;
		this.obj=obj;
	}
	
	/**
	 * 无参构造，默认当前页为1，每页大小为10
	 */
	public PageMongo() {
		super();
	}

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPages() {
		int totalPages=totalSize/pageSize;
		if ((totalSize%pageSize)!=0) {
			totalPages+=1;
		}		
		return totalPages;
	}

	/**
	 * 获取分页数据
	 * @return
	 */
	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	/**
	 * 获取总记录数
	 * @return
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * 设置总记录数
	 * @param totalSize
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页大小，默认10条
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取跳过条数
	 * @return
	 */
	public int getSkip() {
		skip = (pageIndex - 1) * pageSize;
		return skip;
	}

	/**
	 * 设置跳过条数
	 * @param skip
	 */
	public void setSkip(int skip) {
		this.skip = skip;
	}

	/**
	 * 获取当前页
	 * @return
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * 设置当前页
	 * @param pageIndex
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * 获取bean对象
	 * @return
	 */
	public T getObj() {
		return obj;
	}

	/**
	 * 设置bean对象
	 * @param obj
	 */
	public void setObj(T obj) {
		this.obj = obj;
	}

}
