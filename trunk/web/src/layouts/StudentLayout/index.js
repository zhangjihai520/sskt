import React, { Component } from 'react';
import { connect } from 'dva';
import { Route, Switch, message } from 'dva/router';
import { Layout } from 'antd';
import { getRouteData } from '../routerNav';
import HeaderDom from '../../components/StudentHeader/header';
import FooterDom from '../../components/StudentFooter/footer';
import style from './index.less';
import { Authorization } from 'utils/authorization';
import { roleEnum, systemEnum } from 'config/enum';

const { Content } = Layout;

class StudentLayout extends Component {
	constructor(props) {
		super(props);
		this.state = {};
	}
	componentWillMount() {
		//学生端权限验证
		Authorization(roleEnum.student);
		if (this.props.CurUserSystem == systemEnum.online) {
			document.title = '在线互动实验室- 南昌市现代教育技术中心';
		}
	}
	render() {
		const { CurUserSystem, SystemTheme } = this.props;

		return (
			<Layout>
				<HeaderDom props={this.props} />
				<Layout>
					<Content className={style.studentContentLoyout}>
						<Switch>
							{getRouteData('StudentLayout', CurUserSystem).map(item => (
								<Route exact={item.exact} key={item.path} path={item.path} component={item.component} />
							))}
						</Switch>
					</Content>
				</Layout>
				<FooterDom SystemTheme={SystemTheme} />
			</Layout>
		);
	}
}

function stateToProp(state) {
	const { UnReadCount, UserBaseInfo, CurUserSystem, SystemTheme } = state.commonModel;
	return {
		UnReadCount,
		UserBaseInfo,
		CurUserSystem,
		SystemTheme
	};
}

export default connect(stateToProp)(StudentLayout);
