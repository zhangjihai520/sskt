import React from 'react';
import { Form, Modal, Icon, Input, Button, message } from 'antd';
import { connect } from 'dva';
import { getUserInfo, goIndex} from 'utils/authorization';
import { getTokenByCookie, setToken } from 'utils/storage';
import { roleEnum } from 'config/enum';
import styles from './index.less';
@Form.create()
@connect(({ commonModel }) => ({
    TeaBasePath: commonModel.TeaBasePath,
    AdminBasePath: commonModel.AdminBasePath,
    StudentBasePath: commonModel.StudentBasePath
}))
/**
 * 用户账号密码登录组件
 */
export default class PasswordLogin extends React.Component {

    /**
     * 构造函数
     * @param {*} props 
     */
    constructor(props) {
        super(props);
        this.state = {
            btnloading: false,
            showRoleSelect: false,
            reset: null,
            yunParams:{
                targetUrl: "",
                sub: "",
                name: ""
            },
            redrect_url: ""
        };
    }

    /**
     * 组件挂载后
     */
    componentDidMount() {

    }

    /**
     * 点击取消
     */
    handleCancel = () => {
        const {changeModalStatus, form} = this.props;
        /** 重置表单数据*/
        form.resetFields();
        changeModalStatus(false);
    }

    /**
     * 取消选择角色登录弹框
     */
    handleRoleCancel = () => {
        this.setState({
            showRoleSelect: false
        })
    }

    /**修改重定向路径 */
    changeRedirectPath(protocol, role) {
        const { 
            TeaBasePath,
            AdminBasePath, 
            StudentBasePath
         } = this.props;
        const host = window.location.host;
        switch (role) {
            case roleEnum.student:
                this.setState({
                    redrect_url: `${protocol}${host}${StudentBasePath}/MinLesson`
                },() => {
                    // 云平台模拟登录
                    document.getElementById("yunPlatLogin").submit();
                })
                break;
            case roleEnum.teacher:
                this.setState({
                    redrect_url: `${protocol}${host}${TeaBasePath}`
                },() => {
                    // 云平台模拟登录
                    document.getElementById("yunPlatLogin").submit();
                })
                break;
            case roleEnum.admin:
                this.setState({
                    redrect_url: `${protocol}${host}${AdminBasePath}`
                },() => {
                    // 云平台模拟登录
                    document.getElementById("yunPlatLogin").submit();
                })
                break;
            default:
                message.info("暂不支持该角色");
                break;
        }
    }

    /**
     * 提交表单
     */
    handleSubmit = (e) => {
        const { dispatch } = this.props;
        e.preventDefault();
        this.props.form.validateFields((err, params) => {
            // 验证通过
            if (!err) {
                this.setState({
                    btnloading: true
                })
                dispatch({
                    type: "commonModel/LoginByPassword",
                    payload: params
                }).then(result => {
                    const {
                        Token,
                        UId,
                        UserName
                    } = result;
                    if(!Token){
                        this.setState({
                            btnloading: false
                        })
                        message.error('输入的账号/密码不正确，请重新输入！');
                        return;
                    }
                    // 将token存储到cookies中
                    setToken(Token);

                    //读取用户信息跳转相应角色的主页
                    getUserInfo((userRoles) => {
                        // 请求云平台数据
                        dispatch({
                            type: "commonModel/LoginCloudplatform",
                            payload: {
                                AccessTypeId: 1
                            }
                        }).then(data => {
                            const {
                                DledcApiUrl,
                                RedirectUri,
                                DledcPlatformUrl
                            } = data;
                            let protocol = "https://";
                            const yunParams = {
                                targetUrl: protocol + "devsso.dledc.com/core/customlogin",
                                sub: UId,
                                name: UserName
                            }
                            this.setState({
                                yunParams,
                                reset: Math.random()
                            }, () => {

                                if(userRoles.length === 1){
                                    this.setState({
                                        btnloading: false
                                    })
                                    protocol = window.location.protocol + "//";
                                    this.changeRedirectPath(protocol, userRoles[0]);
                                }else if(userRoles.includes(roleEnum.teacher) && userRoles.includes(roleEnum.admin)){
                                    this.handleCancel();  //关闭登录弹框
                                    this.setState({
                                        showRoleSelect: true,
                                        btnloading: false
                                    });
                                }

                            });
                        }); 
                    });
                });
            }
        })
    }
    /**
     * 渲染DOM
     */
    render() {
        const {showRoleSelect, yunParams, redrect_url, reset, btnloading} = this.state;
        const { getFieldDecorator } = this.props.form;
        const { visible} = this.props;
        const protocol = window.location.protocol + "//";
        return (
            <div>
                <form 
                    action={yunParams.targetUrl} 
                    method="post" 
                    key={reset}
                    id="yunPlatLogin" 
                    style={{display:"none"}}
                >
                    {/* 云平台用户Id */}
                    <input name="sub" type="text" defaultValue={yunParams.sub} />
                    {/* 云平台用户名 */}
                    <input name="name" type="text" defaultValue={yunParams.name} />
                    {/* 重定向地址 */}
                    <input name="redrect_url" type="text" defaultValue={redrect_url} />
                </form>
                <Modal
                    title="登录"
                    maskClosable={false}
                    visible={visible}
                    onCancel={this.handleCancel}
                    footer={null}
                >
                    <Form onSubmit={this.handleSubmit}>
                        <Form.Item>
                            {getFieldDecorator('UserName', {
                                rules: [{ required: true, message: '账户不能为空' }]
                            })(
                                <Input
                                    prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} 
                                    placeholder="账户" 
                                />
                            )}
                        </Form.Item>
                        <Form.Item>
                            {getFieldDecorator('Password', {
                                rules: [{ required: true, message: '密码不能为空' }]
                            })(
                                <Input
                                    prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} 
                                    type="password" 
                                    placeholder="密码" 
                                />
                            )}
                        </Form.Item>
                        <Form.Item>
                            <Button 
                                type="primary" 
                                htmlType="submit" 
                                loading={btnloading}
                                className={styles.loginsubmit}
                            >
                                登录
                            </Button>
                        </Form.Item>
                    </Form>
                </Modal>
                <Modal
                    title="请选择老师或者机构"
                    maskClosable={false}
                    visible={showRoleSelect}
                    width={550}
                    onCancel={this.handleRoleCancel}
                    footer={null}
                >
                    <ul className={styles.menulist}>
                        <li className={styles.item} onClick={() => this.changeRedirectPath(protocol, 3)} >
                            <img alt='机构人员' src='/images/common/user-admin.png' />
                            <span>机构人员</span>
                        </li>
                        <li className={styles.item} onClick={() => this.changeRedirectPath(protocol, 2)}>
                            <img alt='老师' src='/images/common/user-teacher.png' />
                            <span>老师</span>
                        </li>
                    </ul>
                </Modal>
        </div>
        )
    }
};
