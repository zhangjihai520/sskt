import React from 'react';
import { connect } from 'dva';
import { List, Input, Button, Modal, message } from 'antd';
import { getTokenByCookie, getSessionStorage, setSessionStorage, } from 'utils/storage';
import Loading from '../../../components/Loading/index';
import style from './index.less';

/**
 * List 数据
 */
const listData = [
    {
        key: 2,
        title: '密保手机',
        info: '已绑定手机：',
    }, {
        key: 3,
        title: '邮箱',
        info: '已绑定邮箱：',
    }
]
/**
 * 映射接口模块
 */
@connect(({ commonModel, studentPersonalModel }) => ({
    commonModel,
    studentPersonalModel
}))

/**
 * 已读消息模块
 */
class SafeSet extends React.Component {
    /**
     * 构造函数
     * @param {*} props 
     */
    constructor(props) {
        super(props);
        this.state = {
            passworldVisible: false,
            phonVisible: false,
            emailVisible: false,
            newPhone: '',
            newEmail: '',
            hideValue: 'xxxxxxxxx',
            phone: getSessionStorage('Phone'),
            email: getSessionStorage('Email'),
            isLoading: false,
            loadingText: '正在加载中'
        }
    }

    /**
     * 渲染DOM
     */
    renderSubInfo = (key) => {
        const { phone, email, hideValue } = this.state;
        switch (key) {
            case 1:
                break;
            case 2:
                return phone ? (phone.slice(0, 3) + '****' + phone.slice(7, 11)) : ('暂无');
                break;
            case 3:
                return email ? (hideValue + email.slice(email.indexOf('@'), email.length)) : ('暂无');
                break;
            default:
                break;
        }
    }

    /**
     * 不同场景显示弹框
     */
    onShow = (type) => {
        switch (type) {
            case 1:
                break;
            case 2:
                this.setState({ phonVisible: true });
                break;
            case 3:
                this.setState({ emailVisible: true });
                break;
            default:
                break;
        }
    }

    /**
     * 不同场景隐藏弹框
     */
    hideModal = (type, flag) => {
        const emailReg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
        const phoneReg = /^[1][3,4,5,7,8][0-9]{9}$/;
        switch (type) {
            case 1:
                break;
            case 2:
                flag ? this.handleSubmit('studentPersonalModel/ChangePhoneNum', phoneReg, 2) : this.setState({ phonVisible: false, newPhone: '' });
                break;
            case 3:
                flag ? this.handleSubmit('studentPersonalModel/ChangeEmail', emailReg, 3) : this.setState({ emailVisible: false, newEmail: '' });
                break;
            default:
                break;
        }
    }

    /**
     * 获取表单值
     * type 类型 1：密码 2：手机 3：邮箱
     */
    getInputValue = (type, e) => {
        if (type === 2) {
            const value = e.target.value.replace(/\D/g, '');
            if (value.length >= 12) {
                return;
            }
            this.setState({
                newPhone: value
            });
        } else {
            this.setState({
                newEmail: e.target.value
            });
        }
    }

    /**
     * 提交表单
     * url：接口地址
     * reg 正则
     * type 类型 1：密码 2：手机 3：邮箱
     */
    handleSubmit = (url, reg, type) => {
        let payload = {},
            value = '',
            text = '';
        if (type === 2) {
            value = this.state.newPhone;
            if (value === this.state.phone) {
                message.error('请输入新手机号', 1);
                return false;
            }
            payload.Phone = value;
            text = value ? '格式错误' : '请输入手机号';
        } else {
            value = this.state.newEmail;
            if (value === this.state.email) {
                message.error('请输入新邮箱', 1);
                return false;
            }
            payload.Email = value;
            text = value ? '格式错误' : '请输入邮箱';
        }
        if (reg.test(value)) {
            this.props.dispatch({
                type: url,
                payload: payload
            }).then(data => this.resultFun(type, data))
        } else {
            message.error(text, 1);
        }
    }

    /**
     * 接口返回数据后处理事件
     * type 类型 1：密码 2：手机 3：邮箱
     * data 当前数据
     */
    resultFun = (type, data) => {
        if (type === 2) {
            this.setState({
                phonVisible: false,
                newPhone: ''
            }, () => {
                if (data === 1) {
                    message.success('修改成功', 1, () => this.getUserInfo());
                } else {
                    message.warning('修改失败', 1)
                }
            })
        } else {
            this.setState({
                emailVisible: false,
                newEmail: ''
            }, () => {
                if (data === 1) {
                    message.success('修改成功', 1, () => this.getUserInfo());
                } else {
                    message.warning('修改失败', 1)
                }
            })
        }
    }

    /**
     * 获取个人信息
     */
    getUserInfo = () => {
        const { dispatch } = this.props;
        this.setState({ isLoading: true },()=>{
            dispatch({
                type: 'studentPersonalModel/GetUserInfo',
                payload: {
                    "Authorization": getTokenByCookie()
                }
            }).then(data => {
                setSessionStorage('Email', data.Email || '');
                setSessionStorage('Phone', data.Phone || '');
                this.setState({
                    phone: data.Phone,
                    email: data.Email,
                    isLoading: false
                })
            });
        });
    }


    /**
     * 渲染DOM
     */
    render() {
        return (
            <div className={style.personalBox}>
                <List
                    header={
                        <div className={style.formTitle}>
                            系统消息
                        </div>
                    }
                    dataSource={listData}
                    renderItem={(item) => (
                        <List.Item key={item.key}
                        // actions={item.key !== 1 && [<Button onClick={() => { this.onShow(item.key) }}>修改</Button>]}
                        >
                            <h5 className={style.h5}>{item.title}</h5>
                            <p className={style.info}>
                                {item.info}
                                <span>{this.renderSubInfo(item.key)}</span>
                            </p>
                        </List.Item>
                    )}

                />
                <Modal
                    title="更改手机号"
                    visible={this.state.phonVisible}
                    onOk={() => { this.hideModal(2, 1) }}
                    onCancel={() => { this.hideModal(2, 0) }}
                    okText="确认"
                    cancelText="取消"
                    wrapClassName="stu-safeset-modal"
                >
                    <Input type="text" placeholder="新手机号" onChange={(event) => { this.getInputValue(2, event) }} value={this.state.newPhone} />
                </Modal>
                <Modal
                    title="更改邮箱"
                    visible={this.state.emailVisible}
                    onOk={() => { this.hideModal(3, 1) }}
                    onCancel={() => { this.hideModal(3, 0) }}
                    okText="确认"
                    cancelText="取消"
                    wrapClassName="stu-safeset-modal"
                >
                    <Input value={this.state.newEmail} type="text" placeholder="新邮箱" onChange={(event) => { this.getInputValue(3, event) }} />
                </Modal>
                <Loading isLoading={this.state.isLoading} loadingText={this.state.loadingText} />
            </div>
        )
    }
};
export default connect()(SafeSet);