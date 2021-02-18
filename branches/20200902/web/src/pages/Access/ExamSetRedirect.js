import React from 'react';
import { connect } from 'dva';
import router from 'umi/router';
import { Link } from 'dva/router';
import { getUrlQuery } from '../../utils/utils';

/**
 * 魔题库组题完毕后的回调页面
 * 由于线上线下路由不一致，需要中转
 */
class ExamSetRedirect extends React.Component {
    state = {
    }

    componentDidMount() {
        const {location} =this.props
        let str = location.query.backUrl;
        let online= str.substring(str.lastIndexOf("_") + 1)
        if (online == 1){
            router.push(`/TeaOnline/Curriculum/Work${location.search}`)
        }else{
            router.push(`/TeaOffline/Curriculum/Work${location.search}`)

        }
    }

    render() {
        return <div>
            正在跳转，请稍后......
        </div>;
    }
}

export default connect()(ExamSetRedirect);
