import React, { Component } from 'react';
import { Layout, Menu } from 'antd';
import ReservationCourse from './ReservationCourse';
//import MyCourse from './MyCourse';
import style from './index.less';
import { connect } from 'dva';
import { systemEnum } from 'config/enum';
import BGImg from 'assets/common/bg_img.jpg';
import BGContent from 'assets/common/bg_content.png';
const { Content, Sider } = Layout;
const noLoginKey = 2;  //我的课程状态

class CourseManager extends Component {
	constructor(props) {
		super(props);
		this.state = {
			curNav: '1',
		}
  }

  componentWillMount(){
    if(this.props.CurUserSystem == systemEnum.online){
      document.title = "在线互动实验室- 南昌市现代教育技术中心"
    }
	}

	handleSwitchNav = (key) => {
		const { changeModalStatus } = this.props;
		return (<ReservationCourse changeModalStatus={changeModalStatus}/>);
	};

  render() {
  	const { CurUserSystem , SystemTheme} = this.props;
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
        <div className={style.bg}>
          <div className={style.bgImg} />
          <div className={style.bgContent} />
        </div>
        <Layout className={style.menu}>
          <Content>
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
