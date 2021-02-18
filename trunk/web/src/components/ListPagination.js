//----------------------------------------------------
//作者:袁燕
//功能描述：通用列表分页
//使用模块：全站
//创建时间：2017/11/8
//------------------修改记录-------------------
//修改人      修改日期        修改目的
//----------------------------------------------------
import { Pagination } from 'antd';
import style from './ListPagination.less';
import PropTypes from 'prop-types';

/**
 * 通用列表分页
 */
const ListPagination = props => {
  const { total, onChange, pageIndex, defaultPageSize, maxShowCount, pageSizeOptions, customShowTotal } = props;

  /**
   *  设置上一页、下一页显示
   * @param current
   * @param type
   * @param originalElement
   * @returns {*}
   */
  function itemRender(current, type, originalElement) {
    if (type === 'prev') {
      return <a>&lt;上一页</a>;
    } else if (type === 'next') {
      return <a>下一页&gt;</a>;
    }
    return originalElement;
  }

  /**
   * 总数显示方式
   * @param total
   * @param range
   * @returns {string}
   */
  function showTotal(total, range) {
    return <span className="tl">共{total}条</span>;
  }

  /**
   * 页索引改变
   * @param pageIndex
   * @param pageSize
   */
  function pageChange(pageIndex, pageSize) {
    setScrollTop(0);
    onChange({
      PageIndex: pageIndex,
      PageSize: pageSize,
    });
  }

  /**
   * 页码改变
   * @param pageIndex
   * @param pageSize
   */
  function pageSizeChange(pageIndex, pageSize) {
    setScrollTop(0);
    onChange({
      PageIndex: 1,
      PageSize: pageSize,
    });
  }

  /**
   * 设置滚动条回到顶部
   * @param value
   */
  function setScrollTop(value) {
    document.body.scrollTop = value;
    document.documentElement.scrollTop = value;
  }

  return (
    <div className={style.page}>
      <Pagination
        className={total > 0 ? 'tr' : 'tr dn'}
        total={maxShowCount > 0 && maxShowCount <= total ? maxShowCount : total}
        onChange={pageChange}
        onShowSizeChange={pageSizeChange}
        itemRender={itemRender}
        showTotal={customShowTotal ? customShowTotal : showTotal}
        showQuickJumper={true}
        current={pageIndex}
        showSizeChanger={true}
        defaultPageSize={defaultPageSize ? defaultPageSize : 25}
        pageSizeOptions={pageSizeOptions ? pageSizeOptions : ['25', '50']}
      />
    </div>
  );
};

export default ListPagination;

ListPagination.propTypes = {
  /**
   * 总数
   */
  total: PropTypes.number,
  /**
   * 页码或页数发生变化时回调方法
   */
  onChange: PropTypes.func,
  /**
   * 页码
   */
  pageIndex: PropTypes.number,
};
