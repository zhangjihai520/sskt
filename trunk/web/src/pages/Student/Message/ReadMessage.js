import React from 'react';
import { connect } from 'dva';
import { List, Pagination } from 'antd';
import Loading from '../../../components/Loading/index';
import style from './index.less';

/**
 * 映射接口模块
 */
@connect(({ commonModel }) => ({
    commonModel
}))

/**
 * 已读消息模块
 */
class ReadMessage extends React.Component {
    /**
     * 构造函数
     * @param {*} props 
     */
    constructor(props) {
        super(props);
        this.state = {
            TotalCount: 0,
            listData: [],
            pageIndex: 1,
            pageSize: 25,
            isLoading: true,
            loadingText: '正在加载中'
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
     * MessageTypeId -1所有0未读1已读
     */
    getList = () => {
        const { pageIndex, pageSize } = this.state;
        const { dispatch } = this.props;

        dispatch({
            type: 'commonModel/GetMessageList',
            payload: {
                "PageIndex": pageIndex,
                "PageSize": pageSize,
                "MessageTypeId": 1
            }
        }).then(data => {
            this.setState({
                TotalCount: data.TotalCount,
                listData: data.MessageList,
                isLoading: false
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
            isLoading: true
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
            isLoading: true
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
                            已读消息
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
                        pageSizeOptions={["25", "50"]}
                        defaultPageSize={25}
                        total={TotalCount}
                        showTotal={(total, range) => `${"显示  " + range[0]}-${range[1]}, 共  ${total} 记录`}
                        onChange={this.pageOnChange}
                        onShowSizeChange={this.pageOnShowSizeChange}
                    /> : ''
                }
                <Loading isLoading={this.state.isLoading} loadingText={this.state.loadingText} />
            </div>
        )
    }
};
export default ReadMessage;