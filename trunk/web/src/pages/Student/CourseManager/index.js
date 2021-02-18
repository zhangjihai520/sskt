import React, { Component } from 'react';
import { Layout, Menu } from 'antd';
import ReservationCourse from './ReservationCourse';
import MyCourse from './MyCourse';
import style from './index.less';
import { connect } from 'dva';
import { roleEnum,systemEnum } from 'config/enum';
const { Content, Sider } = Layout;
import teacherBjBlue from '../../../../public/images/home/img_home_teacher_bj.png';
import teacherBjRed from '../../../../public/images/home/img_home_teacher_bj_red.png';

class CourseManager extends Component {
	constructor(props) {
		super(props);
		this.state = {
			curNav: '1',
		}
	}

	handleMenuClick = (item) => {
		this.setState({ curNav: item.key });
	};

	handleSwitchNav = (key) => {
		const { CurUserSystem } = this.props;
		if (key === '1' && CurUserSystem == 1) {
			return (<ReservationCourse />);
		} else {
			return (<MyCourse />);
		}
	};

  render() {
  	const { CurUserSystem,SystemTheme } = this.props;
  	const { curNav } = this.state;
    const isOnline = CurUserSystem == systemEnum.online;

  	const menuContent = () => {
  		if (isOnline) {
  			return [
  				(<Menu.Item key="1">预约课程</Menu.Item>),
		      (<Menu.Item key="2">我的课程</Menu.Item>)
  			];
  		} else {
  			return (
  				<Menu.Item key="1">我的课程</Menu.Item>
  			)
  		}
  	}

    return (
      <div className={style.CourseManager}>
        <div className={style.bannerbox} style={{backgroundImage: isOnline ? `url(${teacherBjRed})` : `url(${teacherBjBlue})` }}>
          <img src={SystemTheme.bannerImg}/>
        </div>
        <Layout className={style.menu}>
          <Sider theme="light" width="180">
            <Menu
              onClick={this.handleMenuClick}
              defaultSelectedKeys={[curNav]}
              mode="inline">
              {menuContent()}
            </Menu>
          </Sider>
          <Content style={{ margin: '0 0 0 12px', background: '#fff' }}>
            {this.handleSwitchNav(curNav)}
          </Content>
        </Layout>
      </div>
    );
  }
}

function stateToProp(state) {
	return {
    CurUserSystem: state.commonModel.CurUserSystem,
    SystemTheme: state.commonModel.SystemTheme
	}
};

export default connect(stateToProp)(CourseManager);
