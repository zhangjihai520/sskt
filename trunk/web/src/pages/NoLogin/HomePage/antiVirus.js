import React from 'react';
import { Layout, message, Divider } from 'antd';
import Header from '../Header/index.js';
import PasswordLogin from '../../Access/PasswordLogin';
import PublicClassComponent from "components/PublicClass";
import styles from './index.less';

export default class AntiVirus extends React.Component {
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
      <Layout style={{'background': '#f5f5f5'}}>
        <Header {...this.props} visible={loginVisible} changeModalStatus={this.changeModalStatus} />
        <PasswordLogin visible={loginVisible} changeModalStatus={this.changeModalStatus} />
        <PublicClassComponent visible={loginVisible} changeModalStatus={this.changeModalStatus} />
      </Layout>
    );
  }
}
