import React, { PureComponent } from 'react';
import { Table } from 'antd';
import PropTypes from 'prop-types';
import styles from './index.less';

class StandardTable extends PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      selectedRowKeys: []
    };
  }

  static getDerivedStateFromProps(nextProps) {
    // clean state
    if (nextProps.selectedRows && nextProps.selectedRows.length === 0) {
      return {
        selectedRowKeys: []
      };
    }
    return null;
  }

  handleRowSelectChange = (selectedRowKeys, selectedRows) => {
    const { onSelectRow } = this.props;
    if (onSelectRow) {
      onSelectRow(selectedRows);
    }

    this.setState({ selectedRowKeys });
  };

  handleTableChange = (pagination, filters, sorter) => {
    const { onChange } = this.props;
    if (onChange) {
      onChange(pagination, filters, sorter);
    }
  };
  /**
   * 总数显示方式
   * @param total
   * @param range
   * @returns {string}
   */
  showTotal = (total, range) => {
    return `显示${range[0]}-${range[1]}，共${total}记录`;
  };
  /**
   *
   */
  renderTable(isShowRow) {
    const { selectedRowKeys } = this.state;
    const { data = {}, rowKey, ...rest } = this.props;
    const { list = [], pagination } = data;

    const paginationProps = {
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: this.showTotal,
      defaultPageSize: 25,
      pageSizeOptions: ['25', '50'],
      ...pagination
    };

    const rowSelection = {
      selectedRowKeys,
      onChange: this.handleRowSelectChange,
      getCheckboxProps: record => ({
        disabled: record.disabled
      })
    };

    if (isShowRow) {
      return (
        <Table
          rowKey={rowKey || 'key'}
          rowSelection={rowSelection}
          dataSource={list}
          pagination={paginationProps}
          onChange={this.handleTableChange}
          {...rest}
        />
      );
    } else {
      return <Table dataSource={list} pagination={paginationProps} onChange={this.handleTableChange} {...rest} />;
    }
  }
  render() {
    const { selectedRowKeys } = this.state;
    const { data = {}, isRowSelection, rowKey, ...rest } = this.props;
    const { list = [], pagination } = data;

    const paginationProps = {
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: this.showTotal,
      ...pagination
    };

    const rowSelection = {
      selectedRowKeys,
      onChange: this.handleRowSelectChange,
      getCheckboxProps: record => ({
        disabled: record.disabled
      })
    };

    return <div className={styles.standardTable}>{this.renderTable(isRowSelection)}</div>;
  }
}

export default StandardTable;

StandardTable.propTypes = {
  data: PropTypes.object.isRequired,
  isRowSelection: PropTypes.bool
};
StandardTable.defaultProps = {
  isRowSelection: true
};
//传过来的data 示例
// const data = {
//   list: dataSource,
//   pagination: {
//     total: dataSource.length,
//     current:  1,
//   },
// };
