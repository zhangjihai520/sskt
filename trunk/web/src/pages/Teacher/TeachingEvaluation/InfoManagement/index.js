import React, { Component } from 'react';
import { connect } from 'dva';
import {
    List, Avatar, Button, Modal, Pagination, Input, message
  } from 'antd';
import broadcastImg from 'assets/common/default_avator.png';
import styles from './index.less'
const { TextArea } = Input;

@connect(({ teachModel }) => ({
    teachModel
}))
class InfoManagement extends Component{
    state = {
        reset: 0,
        loading: true,
        visible: false,  // 弹框的显示状态
        total: null, // 消息总条数
        infoList: [],  // 消息数据
        textAreaVal: "",
        currentInfo:{
            mid: "",  //信息Id
            uid: ""   //用户Id
        }
    }
    
    componentDidMount() {
        this.getData(1, 10);
    }
    /**获取后台数据 */
    getData = (PageIndex, PageSize) => {
        const { dispatch } = this.props;
        dispatch({
            type: "teachModel/getMessageList",
            payload: {
                PageIndex,
                PageSize
            }
        }).then(result => {
            this.setState({
                total: result.TotalCount,
                infoList: result.MessageList,
                loading: false
            })
        });
    }
    /** 每页数据发生变化 */
    onShowSizeChange = (current, pageSize) => {
        this.setState({
            loading: true
        },() => {
            this.getData(current, pageSize);
        })
    }
    /**处理没有返回图片时使用默认图片 */
    showImage = (avator) => {
        if(!avator){
            return broadcastImg;
        }
        return avator;
    }
    /**
     * mid: 消息Id
     * uid: 用户Id
     */
    modalShow = (mid, uid) => {
        let {currentInfo} = this.state;
        currentInfo.mid = mid;
        currentInfo.uid = uid;
        this.setState({
            currentInfo,
            textAreaVal: "",
            reset: Math.random(),
            visible: true
        });
    }

    /**修改textarea的值 */
    textAreaChange = (e) => {
        this.setState({
            textAreaVal: e.target.value.trim()
        })
    }

    // 回复信息
    answerInfo = (params) => {
        const { dispatch } = this.props;
        const { infoList, currentInfo } = this.state;
        const list = infoList.map(item => {
            if (item.MessageId === currentInfo.mid && item.UserId === currentInfo.uid) {
                return { ...item, IsReply: 1 }
            } else {
                return item;
            }
        });

        dispatch({
            type: "teachModel/replayMessage",
            payload: params
        }).then(result => {
            this.setState({
                visible: false,
                infoList: list
            });
            message.success('消息回复成功');
        });
    }

    /**弹框点击确定 */
    handleOk = (e) => {
        const {
            textAreaVal,
            currentInfo
        } = this.state;
        if(!textAreaVal){
            message.info('你还没有输入信息');
            return;
        }
        const params = {
            MessageId: currentInfo.mid,
            TargetUserId: currentInfo.uid,
            Content: textAreaVal
        }
        this.answerInfo(params);
    }

    /**弹框点击取消 */
    handleCancel = (e) => {
        this.setState({
            visible: false
        });
    }

    render(){
        const {
            reset,
            loading,
            visible,
            total,
            infoList
        } = this.state;
        return (<div className={styles.Infoset}>
            <h3>消息管理</h3>
            <List
                className="demo-loadmore-list"
                itemLayout="horizontal"
                loading={loading}
                dataSource={infoList}
                renderItem={item => (
                    <List.Item actions={
                            [
                                item.IsReply ? 
                                <span className={styles.replied}>已回复</span> : 
                                <Button 
                                    type="default" 
                                    className={styles.replying}
                                    onClick={() => this.modalShow(item.MessageId, item.UserId)}
                                >
                                    回复
                                </Button>
                            ]
                        }>
                            <List.Item.Meta
                                avatar={
                                    <div className={styles.avatarGroups}>
                                        <Avatar className={styles.avatar} src={this.showImage(item.UserFace)} />
                                        <span className={styles.sign}>学生</span>    
                                    </div>
                                }
                                title={<span className={styles.title}>{item.UserTrueName}</span>}
                                description={<p className={styles.content}>{item.Content}</p>}
                            />
                    </List.Item>
                )}
            />
            { 
                infoList.length > 0 ?
                <Pagination 
                    className={styles.paginationStyle}
                    showSizeChanger 
                    showQuickJumper 
                    showTotal={(total, range) => `显示${range[0]}-${range[1]}，共${total}记录`}
                    onChange={this.onShowSizeChange}
                    onShowSizeChange={this.onShowSizeChange} 
                    defaultCurrent={1} 
                    total={total} 
                /> : ""
            }
            <Modal
                title="回复"
                visible={visible}
                onOk={this.handleOk}
                onCancel={this.handleCancel}
            >
                <TextArea 
                    key={reset}
                    autosize={{minRows: 4, maxRows: 4}}                    
                    placeholder="请输入回复信息"
                    onChange={this.textAreaChange}
                />
            </Modal>
        </div>)
    }
}

function stateToProp(state) {
    return {
        CurUserSystem: state.commonModel.CurUserSystem,
        TeaBasePath: state.commonModel.TeaBasePath
    }
};

export default connect(stateToProp)(InfoManagement);