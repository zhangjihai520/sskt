import React from 'react';
import { Layout, message } from 'antd';
import { connect } from 'dva';
import styles from './Login.less';
import { getUrlQuery } from '../../utils/utils';
import { getUserInfo, goIndex } from '../../utils/authorization';
import { setToken } from 'utils/storage';
//import { routerRedux } from 'dva/router';
import { roleEnum } from 'config/enum';

const { Header, Content, Footer } = Layout;

/**
 * 线上线下登录（从云平台直接跳转过来）
 */
class Login extends React.Component {
    state = {
        /**
         * 展示角色选择
         */
        showRoleSelect: false
    }

    componentDidMount() {
        const { dispatch, CurUserSystem } = this.props;
        const code = getUrlQuery(this.props.location.search, 'code');
        const IsTest = getUrlQuery(this.props.location.search, 'IsTest');
        const Name = getUrlQuery(this.props.location.search, 'name');
        const Uid = getUrlQuery(this.props.location.search, 'uid');
        const T = getUrlQuery(this.props.location.search, 't');
        if (!code) {
            message.error("code校验失败");
        } else {
            //获取用户信息，跳转到相应的系统
            dispatch({
                type: "commonModel/LoginByCode",
                payload: {
                    Code: code,
                    IsTest: IsTest,
                    AccessTypeId: CurUserSystem,
                    Name: Name,
                    Uid: Uid,
                    T: T
                }
            })
            .then(result => {
                if (result.LoginStaus === 1) {
                    setToken(result.Token);
                    getUserInfo((userRoles) => {
                        if(userRoles.length === 1){
                            goIndex(userRoles[0]);
                        }else if(userRoles.includes(roleEnum.teacher) && userRoles.includes(roleEnum.admin)){
                            this.setState({ showRoleSelect: true });
                        }
                    });
                } else {
                    message.error('登录失败');
                }
            });
        }
    }

    render() {
        const { showRoleSelect } = this.state;
        const { SystemTheme } = this.props;
        return <Layout className={styles.layout}>
            <Header className={styles.header}>
                <div className={styles.logo}><img alt='南昌市双师课堂' src={SystemTheme.homeImg} /></div>
            </Header>
            <Content className={styles.main}>
                <h1>双师直播.课堂</h1>
                <h2>全国一线名师面授课</h2>
                {showRoleSelect && <ul className={styles.menulist}>
                    <li className={styles.item} onClick={() => goIndex(3)} >
                        <img alt='机构人员' src='/images/common/user-admin.png' />
                        <span>机构人员</span>
                    </li>
                    <li className={styles.item} onClick={() => goIndex(2)}>
                        <img alt='老师' src='/images/common/user-teacher.png' />
                        <span>老师</span>
                    </li>
                </ul>}
                {!showRoleSelect && <div className={styles.loading}>
                    自动登录中，请稍后......
                </div>}
            </Content>
            <Footer className={[styles.footer, 'tc']}>
               <p>{SystemTheme.copyrightTextTitle}</p>
              <p>{SystemTheme.copyrightText}</p>
            </Footer>
        </Layout>;
    }
}

function stateToProp(state) {
    return {
        CurUserSystem: state.commonModel.CurUserSystem,
        TeaBasePath: state.commonModel.TeaBasePath,
        AdminBasePath: state.commonModel.AdminBasePath,
		StudentBasePath: state.commonModel.StudentBasePath,
        SystemTheme: state.commonModel.SystemTheme
    }
};
export default connect(stateToProp)(Login);
