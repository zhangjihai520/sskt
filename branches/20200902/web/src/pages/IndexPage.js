import React from 'react';
import { connect } from 'dva';
import router from 'umi/router';
import { Layout, message } from 'antd';
import styles from './IndexPage.less';

import { getTokenByCookie } from 'utils/storage';
const { Header, Content, Footer } = Layout;

function IndexPage({ TeaBasePath, AdminBasePath, dispatch, StudentBasePath, SystemTheme}) {
  if(SystemTheme.logoutLocation){
    window.location.href = SystemTheme.logoutLocation;  //跳转到外部的路径
 }
 if (getTokenByCookie()) {
    dispatch({
      type: 'commonModel/getUserBaseInfo'
    });
  }
  return (
    <Layout className={styles.layout} style={{ minHeight: '100vh' }}>
      <Header className={styles.header}>
        <div className={styles.logo}><img alt='南昌市双师课堂' src={SystemTheme.homeImg} /></div>
      </Header>
      <Content className={styles.main}>
        <h1>{SystemTheme.homeTitle}</h1>
        <h2>{SystemTheme.homeDesc}</h2>
        <div className={styles.menulist}>
          <table className={styles.item} onClick={() => router.push(`${StudentBasePath}/CourseManager`)}>
            <tbody>
              <tr>
                <td rowSpan='2'><img alt='' src='/images/common/user-student.png' /></td>
                <td className={styles.title}>学生端</td>
              </tr>
            </tbody>
          </table>
          <table className={styles.item} onClick={() => router.push(`${TeaBasePath}`)}>
            <tbody>
              <tr>
                <td rowSpan='2'><img alt='' src='/images/common/user-teacher.png' /></td>
                <td className={styles.title}>老师端</td>
              </tr>
            </tbody>
          </table>
          <table className={styles.item} onClick={() => router.push(`${AdminBasePath}`)}>
            <tbody>
              <tr>
                <td rowSpan='2'><img alt='' src='/images/common/user-admin.png' /></td>
                <td className={styles.title}>管理端</td>
              </tr>
            </tbody>
          </table>
        </div>
      </Content>
      <Footer className={[styles.footer, 'tc']}>
          <p>{SystemTheme.copyrightTextTitle}</p>
          <p>{SystemTheme.copyrightText}</p>
      </Footer>
    </Layout>
  );
}

export default connect(
  ({ commonModel }) => ({
    TeaBasePath: commonModel.TeaBasePath,
    AdminBasePath: commonModel.AdminBasePath,
    StudentBasePath: commonModel.StudentBasePath,
    SystemTheme: commonModel.SystemTheme
  })
)(IndexPage);
