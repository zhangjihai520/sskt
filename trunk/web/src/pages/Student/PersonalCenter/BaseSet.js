import React from 'react';
import { connect } from 'dva';
import { Form, Select, Input, Button, message } from 'antd';
import style from './index.less';
import { getTokenByCookie, setSessionStorage, } from 'utils/storage';
import Loading from '../../../components/Loading/index';
const { Option } = Select;
const { TextArea } = Input;
let oldValue = '';
const textTips = '这个人很懒，什么都没留下';
/**
 * 映射接口模块
 */
@connect(({ commonModel, studentPersonalModel }) => ({
    commonModel,
    studentPersonalModel,
    GradeList: commonModel.GradeList,
    userInfo: commonModel.userInfo
}))

/**
 * 已读消息模块
 */
class BaseSet extends React.Component {
    /**
     * 构造函数
     * @param {*} props 
     */
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            userName: '',
            phoneNum: '',
            schoolName: '',
            greadName: '',
            gradeId: '',
            userInfo: '',
            btnDisabled: true,
            isLoading: true,
            loadingText: '正在加载中'
        }
    }
    /**
     * 组件挂载
     */
    componentDidMount() {
        this.getUserInfo();
    }

    /**
     * 获取个人信息
     */
    getUserInfo = () => {
        const { dispatch } = this.props;
        dispatch({
            type: 'studentPersonalModel/GetUserInfo',
            payload: {
                "Authorization": getTokenByCookie()
            }
        }).then(data => {
            this.setLocalStorage(data);
            this.setState({
                email: data.Email,
                userName: data.UserTrueName,
                phoneNum: data.Phone,
                schoolName: data.SchoolName,
                greadName: data.GradeName,
                gradeId: data.GradeId,
                userInfo: data.Comment,
                visible: false,
                btnDisabled: true,
                isLoading: false
            });
            oldValue = data.Comment;
        });
    }

    /**
     * 设置localStorage值
     * data: 数据
     */
    setLocalStorage = (data) => {
        setSessionStorage('Email', data.Email || '');
        setSessionStorage('Phone', data.Phone || '');
    }

    /**
     * 渲染年级DOM
     */
    renderGrade = () => {
        const { GradeList } = this.props;
        if (GradeList && GradeList.length) {
            return GradeList.map(item =>
                <Option key={item.Id} value={item.Id}>{item.Name}</Option>
            )
        }
    }

    /**
     * 获取TextArea的值
     */
    onChangeText = (e) => {
        if (oldValue === e.target.value) {
            this.setState({ userInfo: e.target.value, btnDisabled: true })
        } else {
            this.setState({ userInfo: e.target.value, btnDisabled: false })
        }
    }

    /**
     * 提交表单
     */
    handleSubmit = (e) => {
        e.preventDefault();
        const { userName, schoolName, gradeId, userInfo } = this.state;
        if (oldValue === userInfo) {
            return false;
        }
        this.props.dispatch({
            type: 'studentPersonalModel/EditStudentInfo',
            payload: {
                "UserTrueName": userName,
                "SchoolName": schoolName,
                "GradeId": gradeId,
                "Comment": userInfo
            }
        }).then(data => {
            if (data === 1) {
                message.success('修改成功', 1, () => this.setState({ newTexValues: '', isLoading:true }, () => this.getUserInfo()));
            } else {
                message.warning('修改失败', 1)
            }
        })
    }

    /**
     * 渲染DOM
     */
    render() {
        const { getFieldDecorator } = this.props.form;
        const { schoolName, email, greadName, userName, userInfo, btnDisabled } = this.state;
        return (
            <div className={style.personalBox}>
                <h1 className={style.formTitle}>基本设置</h1>
                <Form labelCol={{ span: 1 }} wrapperCol={{ span: 5 }} onSubmit={this.handleSubmit}>
                    <Form.Item
                        label="邮箱"
                    >
                        {getFieldDecorator('Email', {
                            initialValue: email
                        })(
                            <Input placeholder={email} disabled={true} />
                        )}
                    </Form.Item>
                    <Form.Item
                        label="姓名"
                    >
                        <Input placeholder={userName} disabled={true} />
                    </Form.Item>
                    <Form.Item
                        label="学校"
                    >
                        <Input placeholder={schoolName} disabled={true} />
                    </Form.Item>
                    <Form.Item
                        label="年级"
                    >
                        <Select
                            disabled={true}
                            placeholder={greadName}
                        >
                            {this.renderGrade()}
                        </Select>
                    </Form.Item>
                    {/* <Form.Item
                        label="个人简介"
                    >
                        <TextArea autosize={{ minRows: 2, maxRows: 6 }} placeholder={!userInfo ? textTips:''} value={userInfo} onChange={this.onChangeText} />
                    </Form.Item>
                    <Form.Item
                        wrapperCol={{ span: 12, offset: 4 }}
                    >
                        <Button type="primary" htmlType="submit" disabled={btnDisabled}>
                            更新信息
                        </Button>
                    </Form.Item> */}
                </Form>
                <Loading isLoading={this.state.isLoading} loadingText={this.state.loadingText} />
            </div>
        )
    }
};

export default connect()(Form.create()(BaseSet));