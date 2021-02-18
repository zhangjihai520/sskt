import React from 'react';
import { Layout, message, Divider } from 'antd';
import Header from '../Header/index.js';
import PasswordLogin from '../../Access/PasswordLogin';
import CourseManager from './CourseManager/index.js';
import styles from './index.less';

export default class HomePage extends React.Component {
  state = {
    loginVisible: false
  };

  /**
   * 修改弹窗显示隐藏状态
   */

  changeModalStatus = status => {
    this.setState({
      loginVisible: status
    });
  };

  render() {
    const { loginVisible } = this.state;
    return (
      <Layout>
        <Header {...this.props} visible={loginVisible} changeModalStatus={this.changeModalStatus} />
        <PasswordLogin visible={loginVisible} changeModalStatus={this.changeModalStatus} />
        <CourseManager visible={loginVisible} changeModalStatus={this.changeModalStatus} />
      </Layout>
    );
  }
}
