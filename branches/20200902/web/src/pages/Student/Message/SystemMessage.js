import React from 'react';
import { connect } from 'dva';
import { List, Pagination } from 'antd';
import style from './index.less';

/**
 * 映射接口模块
 */
@connect(({ commonModel }) => ({
    commonModel
}))

/**
 * 系统消息模块
 */
class SystemMessage extends React.Component {
    /**
     * 构造函数
     * @param {} props 
     */
    constructor(props) {
        super(props);
        this.state = {
            TotalCount: 0,
            listData: [],
            pageIndex: 1,
            pageSize: 10,
        }
    }
    /**
     * 组件挂载
     */
    componentDidMount() {
        this.getList();
    }

    /**
     * 获取消息列表数据
     */
    getList = () => {
        const { dispatch, pageIndex, pageSize } = this.props;
        dispatch({
            type: 'commonModel/GetMessageList',
            payload: {
                "PageIndex": pageIndex,
                "PageSize": pageSize
            }
        }).then(data => {
            this.setState({
                TotalCount: data.TotalCount,
                listData: data.MessageList,
            })
        })
    }

    /**
   * 修改第几页回调
   * @param e pageIndex
   */
    pageOnChange = (e) => {
        this.setState({
            pageIndex: e,
            pageSize: this.state.pageSize,
        }, () => {
            this.getList();
        });
    };
    
    /**
    * 修改每页显示数量回调
    * @param current pageIndex
    * @param size pageSize
    */
    pageOnShowSizeChange = (current, size) => {
        this.setState({
            pageSize: size,
            pageIndex: 1,
        }, () => {
            this.getList();
        });
    };

    /**
     * 渲染DOM
     */
    render() {
        const { listData, TotalCount, pageIndex } = this.state;
        return (
            <div>
                <List
                    className={style.messageBox}
                    header={
                        <div>
                            系统消息
                        </div>
                    }
                    dataSource={listData}
                    renderItem={item => (
                        <List.Item key={item.MessageId}
                        >
                            {item.Content}
                            <div className={style.time}>
                            {item.CreateDateTime}
                            </div>
                        </List.Item>
                    )}

                />
                {
                    TotalCount > 0 ? <Pagination
                        className="tr"
                        showQuickJumper
                        showSizeChanger
                        defaultCurrent={pageIndex}
                        current={pageIndex}
                        total={TotalCount}
                        showTotal={(total, range) => `${"显示  " + range[0]}-${range[1]}, 共  ${total} 记录`}
                        onChange={this.pageOnChange}
                        onShowSizeChange={this.pageOnShowSizeChange}
                    /> : ''
                }

            </div>
        )
    }
};
export default SystemMessage;