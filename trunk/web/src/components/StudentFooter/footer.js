import React from 'react';
import { Layout } from 'antd';
import style from './footer.less';

const {
    Footer,
} = Layout;

/**
 * 学生端尾部组件
 */
class FooterDom extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
      const {SystemTheme} =this.props;
        return (
            <Footer className={style.studentFooterBox}>
              {SystemTheme.copyrightTextTitle}<br/>
              {SystemTheme.copyrightText}
            </Footer>
        )
    }
};

export default FooterDom;
