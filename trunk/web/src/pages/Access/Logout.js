import React from 'react';
import { connect } from 'dva';
import { Layout } from 'antd';
import { clearAllStorage } from 'utils/storage';
import styles from './Logout.less';

const { Header, Content, Footer } = Layout;

/**
 * 退出登录
 */
class Logout extends React.Component {

    componentDidMount() {
        clearAllStorage();
    }

    render() {
        const {SystemTheme } = this.props;
        return <Layout className={styles.layout}>
            <Header className={styles.header}>
                <div className={styles.logo}><img alt='南昌市双师课堂' src={SystemTheme.homeImg} /></div>
            </Header>
            <Content className={styles.main}>
                <h1>双师直播.课堂</h1>
                <h2>全国一线名师面授课</h2>
                 <div className={styles.loading}>
                    您已退出登录！
                </div>
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
        SystemTheme: state.commonModel.SystemTheme
    }
};

export default connect(stateToProp)(Logout);
