import React, { Component } from 'react';
import style from './Classbegins.less';

import { Modal, Button, message } from 'antd';
import { connect } from 'dva';
const confirm = Modal.confirm;
let curDevice = '';//保存当前是否有选择设备
@connect(({ smallnModel, }) => ({
  smallnModel,
}))

class Classbegins extends Component {
  constructor(props) {
    super(props);
    this.state = {
      DiviceId: "",
    }
  }
  componentDidMount(){

  }
  //选择设备
  chooseDevice = (value) => {
    this.setState({
      DiviceId: value,
    },() => {
      curDevice = this.state.DiviceId;
    })
  }
  //更换设备后，保存设备
  saveDeviceHandle = () => {
    const { DiviceId } = this.state
    const { dispatch, isSubject, Id} = this.props;
    dispatch({
      type: "smallnModel/CreateVideoLive",
      payload: {
        "DiviceId": DiviceId, // 房间ID（选择的房间ID）
        "Id": Id, // 课程加密Id
        "IsSubject": isSubject //1课程 2微课
      }
    }).then(result => {
      if (result === 1) {
        message.success("选择成功")
        this.props.close(false,1)
      } else if (result === 2){
        message.info("课程已开课")
        this.props.close(false,1)
        // this.props.Inclass(true) 初始为了改变上课按钮状态，现弃用
      }
      else {
        message.error("选择失败")
      }
      this.setState({
        DiviceId: "",
      })
    });
  }
  //确定选择课程
  handleOk = () => {
    const {hasDeviceId,dataList} = this.props;
    const { DiviceId} = this.state;
    const sef = this;
    if(!curDevice){
      message.warning("请选择设备");
      return false
    }
    let curArr=  dataList.find((obj) => {
      return obj.DiviceId == curDevice
    })
    if(hasDeviceId){
      confirm({
        title: '是否确认更换设备为'+curArr.DiviceName,
        okText:"继续",
        cancelText:"取消",
        mask:false,
        onOk() {
          if(DiviceId && DiviceId != hasDeviceId){//若二次选择时，没有选择或者选择的是一样的，不请求
            sef.saveDeviceHandle()
          }else{//不请求更新设备，直接关闭弹框，不刷新列表
            sef.props.close(false,0)
            sef.setState({
              DiviceId: "",
            })
          }
        },
        onCancel() {
        },
      });
    }else{
      sef.saveDeviceHandle()
    }
    
  }
  //关闭弹框
  handleCancel = () => {
    this.props.close(false,0)
    this.setState({
      DiviceId: "",
    })
  }
  render() {
    const { DiviceId } = this.state
    const { visible,hasDeviceId,dataList} = this.props
    let normalTitle = <div>设备选择<span className={`${style.fontNor} ml-s c-red`}>课程时长不能少于5分钟</span></div>;
    curDevice = DiviceId ? DiviceId :hasDeviceId;
    return (
      <Modal
        visible={visible}
        onCancel={this.handleCancel}
        onOk={this.handleOk}
        // title="设备选择"
        title={normalTitle}
      >
        <div className={style.msgcontent}>
        {
           dataList.map((item,index) => {
             return (
                <Button key={index} onClick={() => { this.chooseDevice(item.DiviceId) }} className={`${style.bottomMargin} ${curDevice == item.DiviceId ? style.chooseSty:''}`}>{item.DiviceName}</Button>
             )
          })
        }
        </div>
      </Modal>
    )
  }
}
export default Classbegins;
