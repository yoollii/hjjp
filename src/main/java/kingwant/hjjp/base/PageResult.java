package kingwant.hjjp.base;


import java.util.List;

/**
 * @author brk
 * @since 2018-05-03
 */
public class PageResult<T> {
    private Integer total;
    private Integer currentPage;
    public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	private Integer pageSize;
    private Integer nextPage;
    private List<T> list;

    public PageResult() {
    }

    public PageResult(Integer total, Integer currentPage, Integer pageSize, List<T> list) {
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.list = list;
    }

    
    public PageResult(Integer total, Integer currentPage, Integer pageSize, Integer nextPage, List<T> list) {
		this.total = total;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.nextPage = nextPage;
		this.list = list;
	}

	public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
    
    
    public Integer getNextPage() {
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}

	public Integer getcurrentPage() {
        return currentPage;
    }

    public void setcurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

