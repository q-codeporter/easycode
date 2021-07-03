package org.zhiqiang.lu.easycode.spring.aop.model;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageSerializable;

import java.util.Collection;
import java.util.List;

public class PageInfo<T> extends PageSerializable<T> {
    private static final long serialVersionUID = 1L;
    private int page_num;
    private int page_size;
    private int size;
    private int start_row;
    private int end_row;
    private int pages;
    private int pre_page;
    private int next_page;
    private boolean is_first_page;
    private boolean is_last_page;
    private boolean has_previous_page;
    private boolean has_next_page;
    private int navigate_pages;
    private int[] navigate_page_nums;
    private int navigate_first_page;
    private int navigate_last_page;

    public PageInfo() {
        this.is_first_page = false;
        this.is_last_page = false;
        this.has_previous_page = false;
        this.has_next_page = false;
    }

    public PageInfo(List<T> list) {
        this(list, 8);
    }

    public PageInfo(List<T> list, int navigate_pages) {
        super(list);
        this.is_first_page = false;
        this.is_last_page = false;
        this.has_previous_page = false;
        this.has_next_page = false;
        if (list instanceof Page) {
            Page<?> page = (Page<?>) list;
            this.page_num = page.getPageNum();
            this.page_size = page.getPageSize();
            this.pages = page.getPages();
            this.size = page.size();
            if (this.size == 0) {
                this.start_row = 0;
                this.end_row = 0;
            } else {
                this.start_row = page.getStartRow() + 1;
                this.end_row = this.start_row - 1 + this.size;
            }
        } else if (list instanceof Collection) {
            this.page_num = 1;
            this.page_size = list.size();
            this.pages = this.page_size > 0 ? 1 : 0;
            this.size = list.size();
            this.start_row = 0;
            this.end_row = list.size() > 0 ? list.size() - 1 : 0;
        }

        if (list instanceof Collection) {
            this.navigate_pages = navigate_pages;
            this.calcNavigatepage_nums();
            this.calcPage();
            this.judgePageBoudary();
        }

    }

    private void calcNavigatepage_nums() {
        int i;
        if (this.pages <= this.navigate_pages) {
            this.navigate_page_nums = new int[this.pages];

            for (i = 0; i < this.pages; ++i) {
                this.navigate_page_nums[i] = i + 1;
            }
        } else {
            this.navigate_page_nums = new int[this.navigate_pages];
            i = this.page_num - this.navigate_pages / 2;
            int endNum = this.page_num + this.navigate_pages / 2;
            if (i < 1) {
                i = 1;
                for (i = 0; i < this.navigate_pages; ++i) {
                    this.navigate_page_nums[i] = i++;
                }
            } else if (endNum > this.pages) {
                endNum = this.pages;

                for (i = this.navigate_pages - 1; i >= 0; --i) {
                    this.navigate_page_nums[i] = endNum--;
                }
            } else {
                for (i = 0; i < this.navigate_pages; ++i) {
                    this.navigate_page_nums[i] = i++;
                }
            }
        }

    }

    private void calcPage() {
        if (this.navigate_page_nums != null && this.navigate_page_nums.length > 0) {
            this.navigate_first_page = this.navigate_page_nums[0];
            this.navigate_last_page = this.navigate_page_nums[this.navigate_page_nums.length - 1];
            if (this.page_num > 1) {
                this.pre_page = this.page_num - 1;
            }

            if (this.page_num < this.pages) {
                this.next_page = this.page_num + 1;
            }
        }

    }

    private void judgePageBoudary() {
        this.is_first_page = this.page_num == 1;
        this.is_last_page = this.page_num == this.pages || this.pages == 0;
        this.has_previous_page = this.page_num > 1;
        this.has_next_page = this.page_num < this.pages;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PageInfo{");
        sb.append("page_num=").append(this.page_num);
        sb.append(", page_size=").append(this.page_size);
        sb.append(", size=").append(this.size);
        sb.append(", start_row=").append(this.start_row);
        sb.append(", end_row=").append(this.end_row);
        sb.append(", total=").append(this.total);
        sb.append(", pages=").append(this.pages);
        sb.append(", list=").append(this.list);
        sb.append(", pre_page=").append(this.pre_page);
        sb.append(", next_page=").append(this.next_page);
        sb.append(", is_first_page=").append(this.is_first_page);
        sb.append(", is_last_page=").append(this.is_last_page);
        sb.append(", has_previous_page=").append(this.has_previous_page);
        sb.append(", has_next_page=").append(this.has_next_page);
        sb.append(", navigate_pages=").append(this.navigate_pages);
        sb.append(", navigate_first_page=").append(this.navigate_first_page);
        sb.append(", navigate_last_page=").append(this.navigate_last_page);
        sb.append(", navigate_page_nums=");
        if (this.navigate_page_nums == null) {
            sb.append("null");
        } else {
            sb.append('[');

            for (int i = 0; i < this.navigate_page_nums.length; ++i) {
                sb.append(i == 0 ? "" : ", ").append(this.navigate_page_nums[i]);
            }

            sb.append(']');
        }

        sb.append('}');
        return sb.toString();
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart_row() {
        return start_row;
    }

    public void setStart_row(int start_row) {
        this.start_row = start_row;
    }

    public int getEnd_row() {
        return end_row;
    }

    public void setEnd_row(int end_row) {
        this.end_row = end_row;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPre_page() {
        return pre_page;
    }

    public void setPre_page(int pre_page) {
        this.pre_page = pre_page;
    }

    public int getNext_page() {
        return next_page;
    }

    public void setNext_page(int next_page) {
        this.next_page = next_page;
    }

    public boolean isIs_first_page() {
        return is_first_page;
    }

    public void setIs_first_page(boolean is_first_page) {
        this.is_first_page = is_first_page;
    }

    public boolean isIs_last_page() {
        return is_last_page;
    }

    public void setIs_last_page(boolean is_last_page) {
        this.is_last_page = is_last_page;
    }

    public boolean isHas_previous_page() {
        return has_previous_page;
    }

    public void setHas_previous_page(boolean has_previous_page) {
        this.has_previous_page = has_previous_page;
    }

    public boolean isHas_next_page() {
        return has_next_page;
    }

    public void setHas_next_page(boolean has_next_page) {
        this.has_next_page = has_next_page;
    }

    public int getNavigate_pages() {
        return navigate_pages;
    }

    public void setNavigate_pages(int navigate_pages) {
        this.navigate_pages = navigate_pages;
    }

    public int[] getNavigate_page_nums() {
        return navigate_page_nums;
    }

    public void setNavigate_page_nums(int[] navigate_page_nums) {
        this.navigate_page_nums = navigate_page_nums;
    }

    public int getNavigate_first_page() {
        return navigate_first_page;
    }

    public void setNavigate_first_page(int navigate_first_page) {
        this.navigate_first_page = navigate_first_page;
    }

    public int getNavigate_last_page() {
        return navigate_last_page;
    }

    public void setNavigate_last_page(int navigate_last_page) {
        this.navigate_last_page = navigate_last_page;
    }
}

