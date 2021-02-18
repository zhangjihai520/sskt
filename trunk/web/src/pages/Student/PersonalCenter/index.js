import React from 'react';
import { Layout, Menu } from 'antd';
import BaseSet from './BaseSet';
import SafeSet from './SafeSet';
import style from './index.less';

const { Content, Sider } = Layout;

/**
 * 学生端消息中心页面
 */
class PersonalCenter extends React.Component {

    /**
     * 构造函数
     * @param {*} props 
     */
    constructor(props) {
        super(props);
        this.state = {
            curNav: '1',
        };
    }

    /**
     * 切换tab
     */
    handleMenuClick = (item) => {
        this.setState({ curNav: item.key });
    };

    /**
     * 根据当前key值渲染不同的DOM
     */
    handleSwitchNav = (key) => {
        switch(key){
            case '1':
                return (<BaseSet />);
                break;
            case '2':
                return (<SafeSet />);
                break;
            default:
                break;
        }
    };

    /**
     * 渲染DOM
     */
    render() {
        const { curNav } = this.state;
        return (
            <Layout className={style.personalBox}>
                <Sider>
                    <Menu
                        onClick={this.handleMenuClick}
                        defaultSelectedKeys={[curNav]}
                        mode="inline">
                        <Menu.Item key="1">基本设置</Menu.Item>
                        <Menu.Item key="2">安全设置</Menu.Item>
                    </Menu>
                </Sider>
                <Content>
                    {this.handleSwitchNav(curNav)}
                </Content>
            </Layout>
        )
    }
};

export default PersonalCenter;